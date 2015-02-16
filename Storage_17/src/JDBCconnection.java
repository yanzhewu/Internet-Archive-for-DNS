import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
* Created by wuyanzhe on 2/12/15.
*/
public class JDBCconnection {
    private Connection conn = null;
    //private final String url = "jdbc:mysql://localhost/practicum";
    private final String url = "jdbc:mysql://localhost/test";
    private final String username = "wuyanzhe";
    private final String password = "wuyanzhe";
    int count = 0;
    private ResultSet resultSet = null;
    private PreparedStatement pstmt = null;
    public JDBCconnection(){
        this.conn = connectionDB();
    }

    public Connection connectionDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection(url,username,password);
            System.out.println("Succeed to connect to database.");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Fail to connect to database!");
        }finally{
            return conn;
        }
    }



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

    public int update(String sql){
        try {
            this.pstmt = conn.prepareStatement(sql);
            this.count=pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("执行更新出错了");
        }

        return count;
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

    public void testQuery() throws SQLException{
        resultSet =query("select * from Website");
        if(resultSet.next()){
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(3));
            System.out.println(resultSet.getString(2));
        }
    }
    public void testUpdate(){
        count = update("insert into Website(ID,name) values(1,'google.com')");
        if(count>0){
            System.out.println("Update success");
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JDBCconnection db = new JDBCconnection();
        //db.testQuery();
        //db.testUpdate();
//        Date date = new Date();
//        db.InsertIPrecordQuery("0.2.3.2",date,date,"google.com");
//        if(!IPexist("01.2.3.2","google.com")) System.out.println("no");
        db.coles();
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
        if(resultSet.wasNull()){
            return false;
        }else{
        return true;}
    }

    public static boolean IPexist(String ip, String server, String location) throws SQLException{
        JDBCconnection db = new JDBCconnection();
        boolean res = db.IPexistQuery(ip,server,location);
        db.coles();
        return res;
    }

    public void InsertIPrecordQuery(String ip,Date start, Date end, String server, String location){
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

    public static void InsertIPrecord(String ip, Date start, Date end, String server,String location){
        JDBCconnection db = new JDBCconnection();
        db.InsertIPrecordQuery(ip,start,end,server,location);
        db.coles();
    }

    public void UpdateValidTimeQuery(String ip, Date start, Date end, String server){
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

    public static void UpdateValidTime(String ip, Date start, Date end, String server){
        JDBCconnection db = new JDBCconnection();
        db.UpdateValidTimeQuery(ip,start,end,server);
        db.coles();
    }

    public void UpdateEndTimeQuery(String ip,Date end, String server, String location){
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

    public static void UpdateEndTime(String ip, Date end, String server, String location){
        JDBCconnection db = new JDBCconnection();
        db.UpdateEndTimeQuery(ip,end,server,location);
        db.coles();
    }
}
