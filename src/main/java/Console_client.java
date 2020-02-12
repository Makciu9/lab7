import org.zeromq.*;

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
            if (message.equals("STOP")){
                break;
            }
            sendAndListen(message);

        }
        context.destroySocket(socket);
        context.destroy();





    }

    private static void sendAndListen(String message) {
        ZFrame frame = new ZFrame(message);
        frame.send(socket, 0);

        ZMsg recv = ZMsg.recvMsg(socket);

    }
}
