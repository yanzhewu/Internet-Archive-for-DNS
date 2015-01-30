/**
 * Created by wuyanzhe on 1/14/15.
 */
/**
 * this is used to do queries for top 500 sites with 5 threads
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class queryOnce {
    public static void doQueryOnce(sites sites){
        ArrayList<record> list = new ArrayList<record>();
        ExecutorService executor = Executors.newFixedThreadPool(30);
        String server = null;
        int i=0;
        while((server=sites.nextSite()) != null){
            System.out.println(server);
            Runnable worker = new query(server,String.valueOf(i++),list);
            executor.execute(worker);
        }
        executor.shutdown();
        while(!executor.isTerminated()){}
//        for(record record:list){
//            System.out.println(record.getResult().toString());
//        }
        try{
        SerialClient.send(list);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        System.out.println("First Round Finished.");

//        ExecutorService executor1 = Executors.newFixedThreadPool(30);
//        missedSites missedSites = new missedSites("website.txt");
//        sites sites1 = new sites(missedSites.getMissed());
//        while((server=sites1.nextSite())!=null){
//            System.out.println(server);
//            Runnable worker = new query(server,String.valueOf(i++));
//            executor1.execute(worker);
//        }
//        executor1.shutdown();
//        while(!executor1.isTerminated()){}
//        System.out.println("Second Round Finished.");

    }
}
