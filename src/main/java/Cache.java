import org.zeromq.ZFrame;

public class Cache {
    private ZFrame frame;
    private String id;
    private long timeout;
    private int start;
    private int end;

    public Cache(ZFrame frame, String id, long timeout, int start, int end) {
        this.frame = frame;
        
    }
}
