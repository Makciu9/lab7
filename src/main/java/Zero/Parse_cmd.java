package Zero;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parse_cmd {
    enum Type{
        GET, PUT, INIT, TIMEOUT,
    }
    private ArrayList<Integer> tmp;

    public Parse_cmd(String cmd){
        tmp = new ArrayList<>();
        parseCmd(cmd);

    }

    private void parseCmd(String cmd) {
    }
}
