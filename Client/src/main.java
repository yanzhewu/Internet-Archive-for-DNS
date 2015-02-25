import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 2/24/15.
 */
public class main {
    public static void main(String[] args) {
        String query = "";
        for(int i=0;i<args.length-1;i++){
            query = query + args[i];
            query = query + " ";
        }
        query = query + args[args.length-1];
        System.out.println(query);
        try{
        send(query);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void send(String query) throws IOException {

        Socket echoSocket = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            // echoSocket = new Socket("taranis", 7);
            echoSocket = new Socket("127.0.0.1", 10007);

            //echoSocket = new Socket("128.135.164.171", 10007);
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

        System.out.println ("Sending Query: " + query + " to Server");
        out.writeObject(query);
        Object object = null;
        try{
        object = in.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(object instanceof ArrayList){
            ArrayList<String> list = (ArrayList<String>)object;
            for(String s:list){
                System.out.println(s);
            }
        }
        out.flush();
        out.close();
        in.close();
        echoSocket.close();
    }
}
