import java.util.ArrayList;

/**
 * Created by wuyanzhe on 1/30/15.
 */
// We can transform the initial record to organized ones
public class HistoryCheck {
    public static void historyCheck(String server){
        ArrayList<record> arrayList = store.getRecord(server);
        checkARecord(arrayList);
    }

    public static ArrayList<HistoryByType> checkARecord(ArrayList<record> list){
        //result will store the history of 5 types
        ArrayList<HistoryByType> result = new ArrayList<HistoryByType>();
        //get the history of A record
        HistoryByType A_History = new HistoryByType(list,"A");

        return result;
    }


}
