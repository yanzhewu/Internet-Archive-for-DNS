import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by wuyanzhe on 2/25/15.
 */
public class populate {
    public static void main(String[] args) {
        String line = "";
        JDBCsqlite db = new JDBCsqlite();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader("website.txt"));
            line = bufferedReader.readLine();
            while(line!=null){
                line = bufferedReader.readLine();
                System.out.println(line);
                db.InsertWebsiteQuery(line);
            }
            bufferedReader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        db.coles();
    }
}
