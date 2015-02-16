
import java.util.Timer;

/**
 * Created by wuyanzhe on 1/14/15.
 */
// Ari makes a change
public class main {
    public static String location;
    public static void main(String[] args) {

        //default rate is 5 min
        //-rate=300000
        //-check=google.com
        //-check=missed
        if(args.length == 1){
            location = args[0];
            Timer t = new Timer();
            MyTask mTask = new MyTask();
            // This task is scheduled to run every 5 minutes

            t.scheduleAtFixedRate(mTask, 0, 300000);
        }
        else{
            if(args[0].contains("rate")){
                String[] part = args[0].split("=");
                int rate = Integer.valueOf(part[1]);
                Timer t = new Timer();
                MyTask mTask = new MyTask();
                t.scheduleAtFixedRate(mTask, 0, rate);
            }
            if(args[0].contains("check")){
                String[] part = args[0].split("=");
                if(part[1].equals("missed")){
                    missedSites missedSites = new missedSites("website.txt");
                    missedSites.printMissedSites();
                }
                else {
                    store store = new store();
                    store.printRecord(part[1]);
                }
            }
        }

//        Timer t = new Timer();
//        MyTask mTask = new MyTask();
//        // This task is scheduled to run every 5 minutes
//
//        t.scheduleAtFixedRate(mTask, 0, 60000);

        //USE THIS TO CHECK THE HISTORY OF A PARTICULAR DOMAIN
//        store store = new store();
//        store.printRecord("google.com");

        // USE THIS TO CHECK WHAT IS MISSED
        //missedSites missedSites = new missedSites("website.txt");
        //missedSites.printMissedSites();
    }
}
