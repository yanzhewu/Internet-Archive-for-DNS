/**
 * Created by wuyanzhe on 2/24/15.
 */
import java.sql.*;
import java.util.Date;

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

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDBCsqlite db = new JDBCsqlite();
        //db.testQuery();
        //db.testUpdate();
        Date date = new Date();
        db.InsertIPrecordQuery("1.2.3.2",date,date,"google.com","Chicago");
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
                System.out.println("关闭结果集发生错误");
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
                System.out.println("关闭pstmt发生异常");
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
                System.out.println("关闭conn发生异常");
            }
        }
        return isColes;
    }

    public boolean IPexistQuery(String ip, String server,String location) throws SQLException{
        try{
            pstmt = conn.prepareStatement("SELECT * FROM IPrecord WHERE IP = ? AND Location=?");
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
            pstmt.setString(4,server);
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
            System.out.println("执行更新出错了");
        }
        if(count > 1){
            System.out.println("Insert success");
        }
    }


}

