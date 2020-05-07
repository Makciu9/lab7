package Zero;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Parse_cmd {
    public int getInd;

    enum Type{
        GET, PUT, INIT, TIMEOUT,
    }
    private ArrayList<Integer> tmp;
    private Type type;
    public Parse_cmd(String cmd){
        tmp = new ArrayList<>();
        parseCmd(cmd);

    }

    private void parseCmd(String cmd) {
        String[] split = cmd.split(" ");
        if(split[0].equals("GET")){
            this.type=Type.GET;
            tmp.add(Integer.parseInt(split[1]));
        }

        if(split[0].equals("PUT")){
            this.type=Type.PUT;
            tmp.add(Integer.parseInt(split[1]));
            tmp.add(Integer.parseInt(split[2]));
        }
    }
    public String getType(){
        return type.name();
    }
}
