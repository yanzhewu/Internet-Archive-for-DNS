/**
 * Created by wuyanzhe on 1/25/15.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SerialServer {
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10007);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10007.");
            System.exit(1);
        }

        Socket clientSocket = null;

        try {
            System.out.println ("Waiting for Client");
            clientSocket = serverSocket.accept();
        }
        catch (IOException e)
        {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        ObjectOutputStream out = new ObjectOutputStream(
                clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(
                clientSocket.getInputStream());

        ArrayList<record> list = null;

        try {
            list = (ArrayList<record>) in.readObject();
            for(record record:list){
                store.store(record,record.getServer());
            }
        }
        catch (Exception ex)
        {
            System.out.println (ex.getMessage());
        }


        System.out.println ("Server recieved point: " + list + " from Client");

        out.flush();


        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
