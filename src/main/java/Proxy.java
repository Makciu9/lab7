import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Proxy {
    private static ZMQ.Socket socketClient;
    private static ZMQ.Socket socketCache;
    private static ZContext context;
    private static ZMQ.Poller poller;

    public static void main(String[] args) {
        context = new ZContext();
        socketClient = context.createSocket(SocketType.ROUTER);
        socketClient.bind("tcp://localhost:3585");

        socketCache = context.createSocket(SocketType.ROUTER);
        socketCache.bind("tcp://localhost:3586");
        System.out.println("Proxy start");

        poller = context.createPoller(2);



    }


}
