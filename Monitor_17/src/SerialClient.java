/**
 * Created by wuyanzhe on 1/25/15.
 */
import com.sun.prism.impl.Disposer;
import org.xbill.DNS.Message;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SerialClient {
    private ArrayList<record> list;

    public static void send(ArrayList<record> list) throws IOException {

        Socket echoSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        try {
            // echoSocket = new Socket("taranis", 7);
            echoSocket = new Socket("127.0.0.1", 10007);

            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: taranis.");
            System.exit(1);
        }


        System.out.println ("Sending record: " + list + " to Server");
        out.writeObject(list);
        out.flush();
//        System.out.println ("Send point, waiting for return value");

//        try {
//            pt2 = (Point3d) in.readObject();
//        }
//        catch (Exception ex)
//        {
//            System.out.println (ex.getMessage());
//        }
//
//        System.out.println("Got point: " + pt2 + " from Server");

        out.close();
        in.close();
        echoSocket.close();
    }

    public SerialClient(){
        list = new ArrayList<record>();
    }

    public void addRecord(Message result, String server){
        list.add(new record(result,server));
    }

}
