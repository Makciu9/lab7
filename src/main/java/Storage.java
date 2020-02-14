import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Storage {
    private static ZContext context;
    public static void main(String[] args) {
        context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        Scanner in = new Scanner(System.in);
        int start = in.nextInt();
        int end = in.nextInt();

        ZFrame initFrame = new ZFrame("INIT" + " " + start + " " + end);
        initFrame.send(socket, 0);




    }
}
