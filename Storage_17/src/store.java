import org.xbill.DNS.Message;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by wuyanzhe on 1/14/15.
 */

/**
*this class is used to store the dns query results grouped by their server names
*function: boolean store(Message message, String server);
*/
public class store {

    public static boolean store(record record, String server){ArrayList<record> arrayList = new ArrayList<record>();
        String filename = "Record/"+server+".txt";
        File file = new File(filename);
        if(file.exists()){
            try{
                FileInputStream fileInputStream = new FileInputStream(filename);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                arrayList = (ArrayList<record>)inputStream.readObject();
                inputStream.close();
                fileInputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this record.");
                return false;
            }
            arrayList.add(record);
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(arrayList);
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this record.");
                return false;
            }
            return true;
        }
        else{
            File file1 = new File(filename);
            arrayList.add(record);
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(arrayList);
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this record.");
                return false;
            }
            return true;
        }
    }


    //this function is used to store one message by server name
    public static boolean store(Message message,String server){
        ArrayList<record> arrayList = new ArrayList<record>();
        String filename = "Record/"+server+".txt";
        File file = new File(filename);
        if(file.exists()){
            try{
                FileInputStream fileInputStream = new FileInputStream(filename);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                arrayList = (ArrayList<record>)inputStream.readObject();
                inputStream.close();
                fileInputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this record.");
                return false;
            }
            record record = new record(message);
            arrayList.add(record);
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(arrayList);
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this record.");
                return false;
            }
            return true;
        }
        else{
            File file1 = new File(filename);
            record record = new record(message);
            arrayList.add(record);
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(arrayList);
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this record.");
                return false;
            }
            return true;
        }
    }

    public static ArrayList<record> getRecord(String server){
        record record = null;
        ArrayList<record> arrayList = new ArrayList<record>();
        String filename = "Record/"+server+".txt";
        try{
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            arrayList = (ArrayList<record>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Fail to get the record.");
            return null;
        }
        return arrayList;
    }

    public static void printRecord(String server){
        ArrayList<record> arrayList = getRecord(server);
        for(record record:arrayList){
            System.out.println(record.getDate());
            System.out.println("This is A record:"+"\n"+record.getA_record()+"\n");
            System.out.println("This is AAAA record:"+"\n"+record.getAAAA_record()+"\n");
            System.out.println("This is AAAA record:"+"\n"+record.getAAAA_record()+"\n");
            System.out.println("This is MX record:"+"\n"+record.getMX_record()+"\n");
            System.out.println("This is TXT record:"+"\n"+record.getTXT_record()+"\n");
            System.out.println("This is SOA record:"+"\n"+record.getSOA_record()+"\n");
            System.out.println("This is TYPE257 record:"+"\n"+record.getTYPE257_record()+"\n");
        }
    }
}
