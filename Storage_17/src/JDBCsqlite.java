import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by wuyanzhe on 2/16/15.
 */
public class JDBCsqlite {
    private Connection conn = null;
    //private final String url = "jdbc:mysql://localhost/practicum";
    private final String url = "jdbc:sqlite:practicum.db";
    int count = 0;
    private ResultSet resultSet = null;
    private PreparedStatement pstmt = null;
    public JDBCsqlite(){
        this.conn = connectionDB();
    }

    public ArrayList<String> showWebsites() throws SQLException{
        ArrayList<String> res = new ArrayList<String>();
        try{
            pstmt = conn.prepareStatement("SELECT Server FROM Website");
            resultSet = pstmt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(resultSet.next()){
            res.add(resultSet.getString(1));
            while(resultSet.next()){
                res.add(resultSet.getString(1));
            }
            return  res;
        }else{
            return null;}
    }

    public ArrayList<String> whocansee(String ip) throws SQLException{
        ArrayList<String> res = new ArrayList<String>();
        try{
            pstmt = conn.prepareStatement("SELECT DISTINCT Location FROM IPrecord WHERE IPrecord.IP = ?");
            pstmt.setString(1,ip);
            resultSet = pstmt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(resultSet.next()){
            res.add(resultSet.getString(1));
            while(resultSet.next()){
                res.add(resultSet.getString(1));
            }
            return  res;
        }else{
            return null;}
    }

    public ArrayList<String> get_valid_ip_query(JDBCsqlite db, String server, Date t1, Date t2) throws SQLException{
        long timestamp1 = t1.getTime();
        long timestamp2 = t2.getTime();
        System.out.println(timestamp1);
        System.out.println(timestamp2);
        ArrayList<String> res = new ArrayList<String>();
        try{
            pstmt = conn.prepareStatement("SELECT IP FROM IPrecord WHERE IPrecord.Server = ? AND IPrecord.Start/1000 < ? AND IPrecord.End/1000 > ?");
            pstmt.setString(1,server);
            pstmt.setLong(2,timestamp1/1000);
            pstmt.setLong(3,timestamp2/1000);
            resultSet = pstmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(resultSet.next()){
            res.add(resultSet.getString(1));
            while(resultSet.next()){
                res.add(resultSet.getString(1));
            }
            return  res;
        }else{
            return null;}
    }

    public void storeClientIPInSQL(JDBCsqlite db, String ip, String location) throws  SQLException{
        try{
            pstmt = conn.prepareStatement("INSERT INTO Client(Location,IP) VALUES(?,?)");
            pstmt.setString(1,location);
            pstmt.setString(2,ip);
            this.count=pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        if(count>0){
            System.out.println("Insert success");
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDBCsqlite db = new JDBCsqlite();
        //db.testQuery();
        //db.testUpdate();
        Date date = new Date(1424137748);
        //db.InsertIPrecordQuery("1.2.3.2",date,date,"google.com","Chicago");
        ArrayList<String> res = db.get_valid_ip_query(db, "google.com", date, date);
        for(String s: res){
            System.out.println(s);
        }
//        if(!IPexist("01.2.3.2","google.com")) System.out.println("no");
        db.coles();
    }

    public Connection connectionDB(){
        try{
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection(url);
            System.out.println("Succeed to connect to database.");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Fail to connect to database!");
        }finally{
            return conn;
        }
    }

    public boolean coles(){
        boolean isColes = false;
        if(resultSet!=null){
            try {
                resultSet.close();
                resultSet=null;
                isColes=true;
            } catch (SQLException e) {
                isColes=false;
                e.printStackTrace();
                System.out.println("Wrong in closing resultSet");
            }
        }
        if(pstmt!=null){
            try {
                pstmt.close();
                pstmt=null;
                isColes=true;
            } catch (SQLException e) {
                isColes=false;
                e.printStackTrace();
                System.out.println("Wrong in closing pstmt");
            }
        }
        if(conn!=null){
            try{
                conn.close();
                conn=null;
                isColes=true;
            }catch (Exception e) {
                isColes=false;
                e.printStackTrace();
                System.out.println("Wrong in closing conn");
            }
        }
        return isColes;
    }

    public boolean IPshowAgain(String ip, String server, String location, Date now) throws SQLException{
        long timestamp1 = now.getTime();
        try{
            pstmt = conn.prepareStatement("SELECT * FROM IPrecord WHERE IPrecord.IP = ? AND IPrecord.Location=? AND (? - IPrecord.End)/1000 > 86400");
            pstmt.setString(1,ip);
            pstmt.setString(2,location);
            pstmt.setLong(3, timestamp1);
            resultSet = pstmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(resultSet.next()){
            return true;
        }else{
            return false;}
    }

    public boolean ClientIPexist(String ip, String location) throws SQLException{
        try{
            pstmt = conn.prepareStatement("SELECT * FROM Client WHERE Client.IP = ? AND Client.Location=?  ");
            pstmt.setString(1,ip);
            pstmt.setString(2,location);
            resultSet = pstmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(resultSet.next()){
            return true;
        }else{
            return false;}
    }

    public boolean IPexistQuery(String ip, String server,String location) throws SQLException{
        try{
            pstmt = conn.prepareStatement("SELECT * FROM IPrecord WHERE IPrecord.IP = ? AND IPrecord.Location=?");
            pstmt.setString(1,ip);
            pstmt.setString(2,location);
            resultSet = pstmt.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        if(resultSet.next()){
            return true;
        }else{
            return false;}
    }

    public void InsertIPrecordQuery(String ip, java.util.Date start, java.util.Date end, String server, String location){
        java.sql.Timestamp sqlStart = new java.sql.Timestamp(start.getTime());
        java.sql.Timestamp sqlEnd = new java.sql.Timestamp(end.getTime());
        try {
            this.pstmt = conn.prepareStatement("INSERT INTO IPrecord(IP,Start,End,Server,Location) VALUES(?,?,?,?,?)");
            pstmt.setString(1,ip);
            pstmt.setTimestamp(2, sqlStart);
            pstmt.setTimestamp(3, sqlEnd);
            pstmt.setString(4, server);
            pstmt.setString(5,location);
            this.count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Wrong");
        }
        if(count > 0){
            System.out.println("Insert success");
        }
    }

    public void UpdateValidTimeQuery(String ip, java.util.Date start, java.util.Date end, String server){
        java.sql.Timestamp sqlStart = new java.sql.Timestamp(start.getTime());
        java.sql.Timestamp sqlEnd = new java.sql.Timestamp(end.getTime());
        try {
            this.pstmt = conn.prepareStatement("UPDATE IPrecord SET Start = ?, End = ? WHERE IP = ?");
            pstmt.setTimestamp(1, sqlStart);
            pstmt.setTimestamp(2, sqlEnd);
            pstmt.setString(3, ip);
            this.count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Wrong");
        }
        if(count > 1){
            System.out.println("Insert success");
        }
    }

    public void UpdateEndTimeQuery(String ip, java.util.Date end, String server, String location){
        java.sql.Timestamp sqlEnd = new java.sql.Timestamp(end.getTime());
        try {
            this.pstmt = conn.prepareStatement("UPDATE IPrecord SET End = ? WHERE IP = ? AND Location = ?");
            pstmt.setTimestamp(1, sqlEnd);
            pstmt.setString(2, ip);
            pstmt.setString(3,location);
            this.count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Wrong in update");
        }
        if(count > 1){
            System.out.println("Insert success");
        }
    }


}
