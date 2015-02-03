import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by wuyanzhe on 2/1/15.
 */
public class Period {
    Date Start;
    Date End;
    ArrayList<String> recordList;

    public Period(record record){
        this.Start = record.getDate();
        this.recordList = getARecordList(record);
    }

    //get the IP address of A record and return
    public static ArrayList<String> getARecordList(record record){
        ArrayList<String> list = new ArrayList<String>();
        String result = record.getA_record();
        String[] lines = result.split("ANSWERS:");
        //printLines(lines);
        String[] lines1 = lines[1].split(";");
        String s = lines1[0];
        s = s.replace("\u005cn","");
        s = s.replace("\u0009"," ");
        String[] lines2 = s.split(" ");
        if(lines2.length == 6){
            list.add(lines2[5]);
        }
        else{
            String server = record.getServer();
            int i = 0;
            while(i+5<lines2.length){
                i = i+5;
                String[] lines3 = lines2[i].split(server);
                list.add(lines3[0]);
            }
        }
        return list;
    }

    public static boolean recordMatch(ArrayList<String> list1, ArrayList<String> list2){
        if(list1.size() != list2.size()) return false;
        for(String s1:list1){
            boolean judge = false;
            for(String s2:list2){
                if(s1.equalsIgnoreCase(s2)){
                    judge = true;
                }
            }
            if(judge == false){
                return false;
            }
        }
        return true;
    }


    public void printPeriod() {
        System.out.println("Start time:"+this.Start);
        System.out.println("End time:"+this.End);
        System.out.println("Records:");
        printLines(recordList);
    }

    public static void printLines(ArrayList<String> lines){
        for(String s:lines){
                System.out.println(s);
        }
    }

    public static void printLines(String[] lines){
        for(String s:lines){
            if(s != null){
            System.out.println(s);}
        }
    }

    public Period(Date start, Date end, ArrayList<String> recordList) {
        Start = start;
        End = end;
        this.recordList = recordList;
    }

    public Date getStart() {
        return Start;
    }

    public void setStart(Date start) {
        Start = start;
    }

    public Date getEnd() {
        return End;
    }

    public void setEnd(Date end) {
        End = end;
    }

    public ArrayList<String> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<String> recordList) {
        this.recordList = recordList;
    }
}
