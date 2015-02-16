import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 2/2/15.
 */
public class HistoryByType {
    String Type;
    ArrayList<Period> history;

    public HistoryByType(ArrayList<record> list, String TYPE) throws SQLException{
        this.Type = TYPE;
        if(TYPE.equalsIgnoreCase("A")){
            history = getAHistory(list);
            for(Period period:history){
                //IPrecord.storeIPinMySQL(period, period.getServer());
            }
        }
    }

    public ArrayList<Period> getAHistory(ArrayList<record> list) throws SQLException{
        ArrayList<Period> result = new ArrayList<Period>();
        record last = list.get(0);
        Period period = new Period(last);
        int length = list.size();
        for(int i=0;i<length;i++){
            record curr = list.get(i);
            if(i==length-1){
                period.setEnd(curr.getDate());
                result.add(period);
            }
            if(Period.recordMatch(Period.getARecordList(last), Period.getARecordList(curr))){
                //period.setEnd(curr.getDate());
                continue;
            }
            else{
                period.setEnd(curr.getDate());
                result.add(period);
                last = curr;
                period = new Period(last);
            }
        }
        return result;
    }

    public HistoryByType(String type, ArrayList<Period> history) {
        Type = type;
        this.history = history;
    }
}
