import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by wuyanzhe on 2/10/15.
 */
public class IPrecord implements Serializable{
    private String server;
    private String IP;
    private Date start_time;
    private Date end_time;

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "IP: " + this.IP + " Start: " + this.getStart_time() + " End: " + this.getEnd_time();
    }

    //store data in MySQL
//    public static boolean storeIPinMySQL(Period period, String server) throws SQLException{
//        //for every ip just discovered, if it is new, just add, if exists, renew its end time
//        for(String ip:period.getRecordList()){
//            if(!JDBCconnection.IPexist(ip,server)){
//                JDBCconnection.InsertIPrecord(ip,period.getStart(),period.getEnd(),server);
//            }
//            else{
//                JDBCconnection.UpdateValidTime(ip,period.getStart(),period.getEnd(),server);
//            }
//        }
//        return true;
//    }

    public static boolean storeIP(Period period, String server){
        ArrayList<IPrecord> arrayList = new ArrayList<IPrecord>();
        String filename = "IPrecord/"+server+".txt";
        File file = new File(filename);
        if(file.exists()){
            try{
                FileInputStream fileInputStream = new FileInputStream(filename);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                arrayList = (ArrayList<IPrecord>)inputStream.readObject();
                inputStream.close();
                fileInputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this IPrecord.");
                return false;
            }
            for(String ip:period.getRecordList()){
                boolean judge = false;
                for(IPrecord exist:arrayList){
                    if(exist.getIP().equalsIgnoreCase(ip)){
                        exist.setEnd_time(period.getStart());
                        judge = true;
                        continue;
                    }
                }
                if(judge == false){
                    IPrecord newIP = new IPrecord(period.getServer(),ip,period.getStart(),period.getStart());
                    arrayList.add(newIP);
                }
            }
            Collections.sort(arrayList, new Comparator<IPrecord>() {
                @Override
                public int compare(IPrecord o1, IPrecord o2) {
                    String[] temp = {o1.getIP(),o2.getIP()};
                    Arrays.sort(temp);
                    if(o1.getIP().equalsIgnoreCase(temp[0])){
                        return -1;
                    }else{
                        if(temp[0].equalsIgnoreCase(temp[1])){
                            return 0;
                        }else{
                            return 1;
                        }
                    }
                }
            });
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(filename);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(arrayList);
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this IPrecord.");
                return false;
            }
            return true;
        }
        else{
            File file1 = new File(filename);
            for(String ip:period.getRecordList()){
                IPrecord newIP = new IPrecord(period.getServer(),ip,period.getStart(),period.getStart());
                arrayList.add(newIP);
            }
            try{
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(arrayList);
                objectOutputStream.close();
                fileOutputStream.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Fail to save this IPrecord.");
                return false;
            }
            return true;
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



    public IPrecord(String server, String IP, Date start_time) {
        this.server = server;
        this.IP = IP;
        this.start_time = start_time;
    }

    public IPrecord(String server, String IP, Date start_time, Date end_time) {
        this.server = server;
        this.IP = IP;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}
