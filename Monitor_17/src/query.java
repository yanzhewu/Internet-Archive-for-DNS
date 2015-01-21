import org.xbill.DNS.Message;

import java.io.IOException;

/**
 * Created by wuyanzhe on 1/14/15.
 */
/**
 * this class is used to do query for a list of sites and call store at the same time
 * functions: Message doQuery(String server);
 *            void querySitesOnce(sites sites);
 */
public class query implements  Runnable{
    static String TYPE257 = "TYPE257";
    static String MX = "MX";
    static String SOA = "SOA";
    static String TXT = "TXT";
    static String ANY = "ANY";
    private Thread thread;
    private String threadname;
    private String server;

    public Message doQuery(){
        String[] query1 = {server,TYPE257};
        String[] query2 = {server,MX};
        String[] query3 = {server,SOA};
        String[] query4 = {server,TXT};
        String[] query5 = {server,ANY};
        Lookup lookup = new Lookup();
        Message result = null;;
        try{
            result = lookup.Lookup(query1);
            result = lookup.Lookup(query2);
            result = lookup.Lookup(query3);
            result = lookup.Lookup(query4);
            result = lookup.Lookup(query5);
        }catch (IOException e){
            System.out.println("Time out.");
            return null;
        }
        return result;
    }

    public query(String server, String threadname) {
        this.server = server;
        this.threadname = threadname;
    }

    //    public void querySitesOnce(sites sites){
//        store store = new store();
//        String server = null;
//        while ((server = sites.nextSite()) != null){
//            Message result = doQuery(server);
//            if(result != null){
//                store.store(doQuery(server),server); //successful query
//            }
//            else{
//                sites.tryLater(server);
//            }
//        }
//    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try{
            Message result = doQuery();
            if(result != null){
                store.store(result,server); //successful query
                System.out.println(result.toString());
            }
            else{
                sites.tryLater(server);  // push back the site if failed
            }
            Thread.sleep(50);
        }catch (InterruptedException e){
            System.out.println("Thread "+threadname+" interrupted");
        }
    }


}
