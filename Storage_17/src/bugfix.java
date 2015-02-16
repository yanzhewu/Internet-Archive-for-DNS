import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 2/11/15.
 */
public class bugfix {
    public static ArrayList<record> getRecord(String server){
        record record = null;
        ArrayList<record> arrayList = new ArrayList<record>();
        String filename = "Record/"+server+".txt";
        try{
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            arrayList = (ArrayList<record>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Fail to get the record.");
            return null;
        }
        return arrayList;
    }

    public static void main(String[] args) {
        ArrayList<record> res = getRecord("google.com.tw");
        System.out.println(res.get(0).toString());
    }
}
