/**
 * Created by wuyanzhe on 1/25/15.
 */
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

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader ini = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = ini.readLine(); //you get the IP as a String
        //System.out.println(ip);

        recordPacket packet = new recordPacket(list,main.location,ip);
        try {
            // echoSocket = new Socket("taranis", 7);
            //echoSocket = new Socket("127.0.0.1", 10008);

            echoSocket = new Socket("128.135.164.171", 10008);
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


        System.out.println ("Sending recordPacket: " + list + " to Server");
        out.writeObject(packet);
        out.flush();

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
