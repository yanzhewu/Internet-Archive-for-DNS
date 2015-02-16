import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 2/15/15.
 */
public class recordPacket implements Serializable{
    private ArrayList<record> list;
    private String location;

    public recordPacket(ArrayList<record> list, String location) {
        this.list = list;
        this.location = location;
    }

    public ArrayList<record> getList() {
        return list;
    }

    public void setList(ArrayList<record> list) {
        this.list = list;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

