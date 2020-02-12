import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Proxy {
    private static ZMQ.Socket socketClient;
    private static ZMQ.Socket socketCache;
    private static ZContext context;

    public static void main(String[] args) {
        context = new ZContext();
        socketClient = context.createSocket(SocketType.ROUTER);
        
    }


}
