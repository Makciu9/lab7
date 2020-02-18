import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Storage {
    private static ZContext context;
    private static ZMQ.Poller poller;
    private static long timeout;
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

        }





    }
    private static void isTimeout(ZMQ.Socket socket) {
        if (System.currentTimeMillis() >= timeout){
            System.out.println("TIMEOUT");
            timeout = System.currentTimeMillis() + 3000;
            ZFrame frame = new ZFrame("TIMEOUT");

        }

    }
}
