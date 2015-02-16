import org.xbill.DNS.Message;

import java.io.IOException;
import java.util.ArrayList;

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
    static String A = "A";
    static String AAAA = "AAAA";
    static
    private Thread thread;
    private String threadname;
    private String server;
    private ArrayList<record> list;
    public ArrayList<Message> doQuery(){
        ArrayList<Message> list = new ArrayList<Message>();
        String[] query1 = {server,A};
        String[] query2 = {server,AAAA};
        String[] query3 = {server,TYPE257};
        String[] query4 = {server,MX};
        String[] query5 = {server,SOA};
        String[] query6 = {server,TXT};
        Lookup lookup = new Lookup();
        try{
            list.add(lookup.Lookup(query1));
            list.add(lookup.Lookup(query2));
            list.add(lookup.Lookup(query3));
            list.add(lookup.Lookup(query4));
            list.add(lookup.Lookup(query5));
            list.add(lookup.Lookup(query6));
        }catch (IOException e){
            System.out.println("Time out.");
            return null;
        }
        return list;
    }

    public query(String server, String threadname, ArrayList<record> arrayList) {
        this.server = server;
        this.threadname = threadname;
        this.list = arrayList;
    }

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
            ArrayList<Message> result = doQuery();
            if(result != null){
                //store.store(result,server); //successful query
                list.add(new record(result,server));
                //System.out.println(result.toString());
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
