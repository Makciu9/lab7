import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Console_client {

    private static ZMQ.Socket socket;
    public static void main(String[] args) {
        ZContext context = new ZContext();
        socket = context.createSocket(SocketType.REQ);
        socket.connect("tcp://localhost:3585");
        


    }
}
