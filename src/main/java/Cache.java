import org.zeromq.ZFrame;

public class Cache {
    private ZFrame frame;
    private String id;
    private long timeout;
    private int start;
    private int end;

    public Cache(ZFrame frame, String id, long timeout, int start, int end) {
        this.frame = frame;
        this.id = id;
        this.timeout = timeout;
        this.start = start;
        this.end = end;

    }

    public int getStart() {
        return start;

    }

    public int getEnd() {
        return end;
    }

    public ZFrame getFrame() {
        return frame;
    }

    public boolean checkID(String id) {
        return this.id.equals(id);
    }
}
