import org.xbill.DNS.Message;

import javax.swing.text.DateFormatter;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wuyanzhe on 1/14/15.
 */

/**
*this class is used to store the dns query results grouped by their server names
*function: boolean store(Message message, String server);
*/
public class store {

    private Connection conn = null;
    //private final String url = "jdbc:mysql://localhost/practicum";
//    private final String url = "jdbc:mysql://localhost/test";
//    private final String username = "wuyanzhe";
//    private final String password = "wuyanzhe";
    private final String url = "jdbc:sqlite:practicum.db";
    int count = 0;
    private ResultSet resultSet = null;
    private PreparedStatement pstmt = null;
    private static final String PATTERN = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    //query consists of domain+t1+t2 time should be like: 2015-02-24 18:18:54
    public static ArrayList<String> get_valid_ip(String query) throws SQLException{
        JDBCsqlite db = new JDBCsqlite();
        ArrayList<String> res = new ArrayList<String>();
        String[] parts = query.split(" ");
        String server = parts[0];
        String t1 = parts[1]+"T"+parts[2];
        String t2 = parts[3]+"T"+parts[4];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date1 = formatter.parse(t1);
            Date date2 = formatter.parse(t2);
            res =  get_valid_ip(db, server, date1, date2);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return res;
    }

    public static void storeRecordInMySQL(ArrayList<record> list, String location) throws SQLException{
        //JDBCconnection db = new JDBCconnection();
        JDBCsqlite db = new JDBCsqlite();
        for(record record:list){
            ArrayList<String> IP_list = getARecordList(record);
            for(String ip:IP_list){
                if(validate(ip)){
                System.out.println(ip);
                if(!IPexist(db,ip, record.getServer(),location)){
                    InsertIPrecord(db, ip, record.getDate(), record.getDate(), record.getServer(), location);
                }
                else{
                    UpdateEndTime(db, ip, record.getDate(), record.getServer(),location);
                }
                }
            }
        }
        db.coles();
    }

    public static boolean validate(String ip){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static ArrayList<String> get_valid_ip(JDBCsqlite db, String server, Date date1, Date date2) throws SQLException{
        return db.get_valid_ip_query(db,server,date1,date2);
    }

    public static boolean IPexist(JDBCsqlite db, String ip, String server,String location) throws SQLException{
        boolean res = db.IPexistQuery(ip,server,location);
        return res;
    }

    public static void InsertIPrecord(JDBCsqlite db, String ip, Date start, Date end, String server,String location){
        db.InsertIPrecordQuery(ip, start, end, server,location);
    }

    public static void UpdateEndTime(JDBCsqlite db, String ip, Date end, String server, String location) {
        db.UpdateEndTimeQuery(ip,end,server,location);
    }

//    public static boolean IPexist(JDBCconnection db, String ip, String server,String location) throws SQLException{
//        boolean res = db.IPexistQuery(ip,server,location);
//        return res;
//    }
//
//    public static void InsertIPrecord(JDBCconnection db, String ip, Date start, Date end, String server,String location){
//        db.InsertIPrecordQuery(ip, start, end, server,location);
//    }
//
//    public static void UpdateEndTime(JDBCconnection db, String ip, Date end, String server, String location) {
//        db.UpdateEndTimeQuery(ip,end,server,location);
//    }

    public ResultSet query(String sql){
        try{
            pstmt = conn.prepareStatement(sql);
            resultSet = pstmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            return resultSet;
        }
    }

    public static ArrayList<String> getARecordList(record record){
        ArrayList<String> list = new ArrayList<String>();
        String result = record.getA_record();
        String[] lines = result.split("ANSWERS:");
        //printLines(lines);
        String[] lines1 = lines[1].split(";");
        String s = lines1[0];
        s = s.replace("\u005cn","");
        s = s.replace("\u0009"," ");
        String[] lines2 = s.split(" ");
        if(lines2.length == 6){
            list.add(lines2[5]);
        }
        else{
            String server = record.getServer();
            int i = 0;
            while(i+5<lines2.length){
                i = i+5;
                String[] lines3 = lines2[i].split(server);
                list.add(lines3[0]);
            }
        }
        return list;
    }

    public static boolean store(record record, String server){
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