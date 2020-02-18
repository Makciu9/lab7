import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;

public class Proxy {
    private static ZMQ.Socket socketClient;
    private static ZMQ.Socket socketStorage;
    private static ZContext context;
    private static ZMQ.Poller poller;
    private static ArrayList<Cache> caches;

    public static void main(String[] args) {
        context = new ZContext();
        socketClient = context.createSocket(SocketType.ROUTER);
        socketClient.bind("tcp://localhost:3585");

        socketStorage = context.createSocket(SocketType.ROUTER);
        socketStorage.bind("tcp://localhost:3586");
        System.out.println("Proxy start");

        poller = context.createPoller(2);
        poller.register(socketClient, ZMQ.Poller.POLLIN);
        poller.register(socketStorage, ZMQ.Poller.POLLIN);

        //caches = new ArrayList<>();
        while (poller.poll(4000) != -1) {
            if (poller.pollin(0)){
                ZMsg recv = ZMsg.recvMsg(socketClient);
                String message = new String(recv.getLast().getData(), ZMQ.CHARSET);
                String[] messageSplit = message.split(" ");
                String command = messageSplit[0];
                System.out.println(messageSplit[0] + " " + messageSplit[1]);
                if (command.equals("GET")){
                    int key = Integer.parseInt(messageSplit[1]);

                }



            }
        }



    }
    private static void sendGET(int key, ZMsg recv) {
        boolean flag = false;
        for (Cache cache : caches) {

        }
    }


}
