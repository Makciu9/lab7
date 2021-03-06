package Zero;
import org.zeromq.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Storage {
    private static ZContext context;
    private static ZMQ.Poller poller;
    private static long timeout;
    private static Map<Integer, Integer> storage;
    private static String server_add = "tcp://localhost:3586";
    private static String time = "TIMEOUT";
    public static void main(String[] args) {

        context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect(server_add);
        //слушаем сокет
        initStorage();
        sendInitFrame(socket);
        initPoller(socket);

        timeout = System.currentTimeMillis() + 3000;

        while (poller.poll(3000) != -1){
            isTimeout(socket);
            if (poller.pollin(0)){
                ZMsg recv = ZMsg.recvMsg(socket);
                if (recv.size() == 3) {
                    String tmp = recv.getLast().toString();
                    Parse_cmd cmd = new Parse_cmd(tmp);
                    if (cmd.getType().equals("GET")){
                        int key = cmd.getInd();
                        sendGET(key, recv, socket);
                    } else if (cmd.getType().equals("PUT")){
                        int key = cmd.getInd();
                        int val = cmd.getVal();
                        sendPUT(key, val, recv);

                    }
                }

            }

        }
        context.destroySocket(socket);
        context.destroy();
    }

    private static void initPoller(ZMQ.Socket socket) {
        poller = context.createPoller(1);
        poller.register(socket, ZMQ.Poller.POLLIN);
    }

    private static void sendInitFrame(ZMQ.Socket socket) {
        Scanner in = new Scanner(System.in);

        int start = in.nextInt();
        int end = in.nextInt();
        ZFrame initFrame = new ZFrame("INIT" + " " + start + " " + end);

        initFrame.send(socket, 0);
        System.out.println("Storage start on tcp://localhost:3586");
    }

    private static void initStorage() {
        storage = new HashMap<>();
    }

    private static void sendGET(int key, ZMsg recv, ZMQ.Socket socket) {
        Integer response = storage.getOrDefault(key, -1);
        recv.getLast().reset(String.valueOf(response));
        recv.send(socket);
        System.out.println("GET | key: " + key);
    }

    private static void sendPUT(int key, int val, ZMsg recv) {
        storage.put(key, val);
        recv.destroy();
        System.out.println("PUT | key: " + key + " | val: " + val);
    }

    private static void isTimeout(ZMQ.Socket socket) {
        if (System.currentTimeMillis() >= timeout) {
            System.out.println("TIMEOUT");
            timeout = System.currentTimeMillis() + 3000;
            ZFrame frame = new ZFrame(time);
            frame.send(socket, 0);
        }

        }

}

