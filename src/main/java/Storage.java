import org.zeromq.ZContext;

public class Storage {
    private static ZContext context;
    public static void main(String[] args) {
        context = new ZContext();

    }
}
