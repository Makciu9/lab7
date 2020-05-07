package Zero;
import org.zeromq.*;

import java.util.ArrayList;

public class Proxy {
    private static ZMQ.Socket socketClient;
    private static ZMQ.Socket socketStorage;
    private static ZContext context;
    private static ZMQ.Poller poller;
    private static ArrayList<Cache> caches;
    

    public static void main(String[] args) {
        initContextSockets();
        //открываем два сокета, один для клиета, от другого команды нотифи
        initPoller();
        //клиет 0, хранилища 1

        caches = new ArrayList<>();
        while (poller.poll(3000) != -1) {
            if (poller.pollin(0)){
                ZMsg recv = ZMsg.recvMsg(socketClient);
                Parse_cmd cmd = new Parse_cmd(recv.getLast().toString());
                System.out.println(cmd.getType());

                if (cmd.getType().equals("GET")){
                    int key = cmd.getInd();
                    sendGET(key, recv);
                    //Случайному серверу содер данные
                } else if (cmd.getType().equals("PUT")){
                    int key = cmd.getInd();
                    sendPUT(key, recv);
                    //Серверу и он обновляет данные
                }
            } else if (poller.pollin(1)){
                //список подключеных серваков
                ZMsg recv = ZMsg.recvMsg(socketStorage);
                ZFrame frame = recv.unwrap();
                String id = new String(frame.getData(), ZMQ.CHARSET);
                String tmp = recv.getLast().toString();
                Parse_cmd cmd = new Parse_cmd(tmp);
                System.out.println(id);

                if (cmd.getType().equals("INIT")) {
                    int start = cmd.getStart();
                    int end = cmd.getEnd();
                    caches.add(new Cache(frame, id, System.currentTimeMillis(), start, end));
                }else if (cmd.getType().equals("TIMEOUT")){
                    changeTimeout(id);
                } else {recv.send(socketClient);} //отправка клиету
            }
            System.out.println("Proxy loop...");
        }
        context.destroySocket(socketClient);
        context.destroySocket(socketStorage);
        context.destroy();
    }

    private static void initPoller() {
        poller = context.createPoller(2);
        poller.register(socketClient, ZMQ.Poller.POLLIN);
        poller.register(socketStorage, ZMQ.Poller.POLLIN);
    }

    private static void initContextSockets() {
        context = new ZContext();
        socketClient = context.createSocket(SocketType.ROUTER);
        socketClient.bind("tcp://localhost:3585");

        socketStorage = context.createSocket(SocketType.ROUTER);
        socketStorage.bind("tcp://localhost:3586");
        System.out.println("Proxy start");
    }

    private static void sendGET(int key, ZMsg recv) {
        boolean flag = false;
        for (Cache cache : caches) {
            if (cache.getStart()  <= key && cache.getEnd() >= key){
                cache.getFrame().send(socketStorage, ZFrame.REUSE + ZFrame.MORE);
                recv.send(socketStorage, false);
                flag = true;

            }
        }
        if (!flag) {
            recv.getLast().reset("er");
            recv.send(socketClient);

        }
    }

    private static void sendPUT(int key, ZMsg recv) {
        int count = 0;
        for (Cache cache : caches) {
            if (cache.getStart() <= key && cache.getEnd() >= key){
                cache.getFrame().send(socketStorage, ZFrame.REUSE + ZFrame.MORE);
                recv.send(socketStorage, false);
                count++;
            }
        }
        ZMsg response = new ZMsg();
        response.add(new ZFrame("put to " + count));
        response.wrap(recv.getFirst());
        // отпрака клиету
        response.send(socketClient);
    }
    private static void changeTimeout(String id) {
        for (Cache cache : caches) {
            if (cache.checkID(id)) {
                cache.setTimeout(System.currentTimeMillis());

            }
        }
    }


}
