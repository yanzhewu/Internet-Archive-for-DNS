/**
 * Created by wuyanzhe on 1/25/15.
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SerialServer extends Thread{
    protected static boolean serverContinue = true;
    protected Socket clientSocket;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        if(args.length==0) {
            try {
                serverSocket = new ServerSocket(10007);
                try {
                    while (serverContinue) {
                        serverSocket.setSoTimeout(60000);
                        System.out.println("Waiting for Client");
                        try {
                            new SerialServer(serverSocket.accept());
                        } catch (SocketTimeoutException ste) {
                            System.out.println("Timeout Occurred.");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Accept failed.");
                    System.exit(1);
                }
            } catch (IOException e) {
                System.err.println("Could not listen on port: 10007.");
                System.exit(1);
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("Could not close port: 10007");
                    System.exit(1);
                }
            }
        }
        else{
            if(args[0].equalsIgnoreCase("show")){
                HistoryCheck.historyCheck(args[1]);
            }
        }
    }

    private SerialServer (Socket clientSoc){
        this.clientSocket = clientSoc;
        start();
    }

    public void run(){
        System.out.println("New TCP connection thread started.");
        try{
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
        }catch (IOException e){
            System.err.println("Server error.");
            System.exit(1);
        }
    }
}
