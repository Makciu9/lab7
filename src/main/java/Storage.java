import org.zeromq.*;

import java.util.Scanner;

public class Storage {
    private static ZContext context;
    private static ZMQ.Poller poller;
    private static long timeout;
    private static Map<Integer, String> storage;
    public static void main(String[] args) {
        context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        Scanner in = new Scanner(System.in);
        int start = in.nextInt();
        int end = in.nextInt();

        ZFrame initFrame = new ZFrame("INIT" + " " + start + " " + end);
        initFrame.send(socket, 0);
        System.out.println("Storage start on tcp://localhost:3586");

        poller = context.createPoller(1);
        poller.register(socket, ZMQ.Poller.POLLIN);

        timeout = System.currentTimeMillis() + 3000;
        while (poller.poll(3000) != -1){
            isTimeout(socket);
            if (poller.pollin(0)){
                ZMsg recv = ZMsg.recvMsg(socket);
                if (recv.size() == 3) {
                    String[] message = recv.getLast().toString().split(" ");
                    String command = message[0];
                    if (command.equals("GET")){
                        int key = Integer.parseInt(message[1]);
                        sendGET(key, recv, socket);
                    } else if (command.equals("PUT")){
                        int key = Integer.parseInt(message[1]);
                        String val = message[2];

                    }
                }

            }

        }





    }

    private static void sendGET(int key, ZMsg recv, ZMQ.Socket socket) {
        String response = storage.getOrDefault(key, "ex");
        recv.getLast().reset(response);
        recv.send(socket);
        System.out.println("GET | key: " + key);
    }

    private static void isTimeout(ZMQ.Socket socket) {
        if (System.currentTimeMillis() >= timeout) {
            System.out.println("TIMEOUT");
            timeout = System.currentTimeMillis() + 3000;
            ZFrame frame = new ZFrame("TIMEOUT");
            frame.send(socket, 0);
        }

        }

}

