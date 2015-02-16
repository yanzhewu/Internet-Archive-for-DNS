import com.sun.xml.internal.bind.v2.runtime.output.InPlaceDOMOutput;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wuyanzhe on 2/10/15.
 */
public class checkIP {
    private static ArrayList<IPrecord> IPlist;
    private static String server;

    public static void main(String[] args) {
        IPlist = getIPRecord("google.com");
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");

        String input = "2015-02-03";

        //System.out.print(input + " Parses as ");

        Date t = new Date();

        try {
            t = ft.parse(input);
            //System.out.println(t);
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }
        System.out.println("Time point is: "+t);
        IPsinceTimeT(t,"google.com");
        newIPsinceTimeT(t,"google.com");
        stoppedIPsinceTimeT(t,"google.com");
    }

    public static void IPsinceTimeT(Date date, String server){
        System.out.println("What IPs have you seen google.com using since time t?");
        for(IPrecord iPrecord:getIPRecord(server)){
            if(iPrecord.getEnd_time().after(date)){
                System.out.println(iPrecord.getIP());
            }
        }
    }

    public static void newIPsinceTimeT(Date date, String server){
        System.out.println("What new IPs has google.com started using since time t?");
        for(IPrecord iPrecord:getIPRecord(server)){
            if(iPrecord.getStart_time().after(date)){
                System.out.println(iPrecord.getIP());
            }
        }
    }

    public static void stoppedIPsinceTimeT(Date date, String server){
        System.out.println("Which IPs does it seem like google.com has stopped using since time t?");
        for(IPrecord iPrecord:getIPRecord(server)){
            if(iPrecord.getEnd_time().before(date)){
                System.out.println(iPrecord.getIP());
            }
        }
    }

    public static ArrayList<IPrecord> getIPRecord(String server){
        IPrecord IPrecord = null;
        ArrayList<IPrecord> arrayList = new ArrayList<IPrecord>();
        String filename = "IPrecord/"+server+".txt";
        try{
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            arrayList = (ArrayList<IPrecord>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Fail to get the record.");
            return null;
        }
        return arrayList;
    }
}
