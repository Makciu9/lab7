import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Console_client {

    private static ZMQ.Socket socket;
    public static void main(String[] args) {
        ZContext context = new ZContext();
        socket = context.createSocket(SocketType.REQ);
        socket.connect("tcp://localhost:3585");
        System.out.println("Client start on tcp://localhost:3585");
        Scanner in = new Scanner(System.in);

        while (true) {
            String message = in.nextLine();
            if (message.equals("STOP")

        }





    }
}
