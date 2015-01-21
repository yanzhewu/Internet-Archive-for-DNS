import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 * Created by wuyanzhe on 1/14/15.
 */
/**
 * this class is used to provide the list of servers
 * function: String nextSite();
 */
public class sites {
    private String[] list;
    private int num;
    private static Stack<String> stack;

    public sites(String[] list) {
        this.list = list;
        stack = new Stack<String>();
        for(String s:list){
            stack.push(s);
        }
    }

    //this is used to load sites from file
    public sites(String filename){
        String line = "";
        stack = new Stack<String>();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            line = bufferedReader.readLine();
            while(line!=null){
                stack.push(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    //this is used to provide the next server, if no server left, return null
    public String nextSite(){
        if(!stack.isEmpty()){
            return stack.pop();
        }
        else {
            return null;
        }
    }

    //this is used to push back the server to query it later
    public static void tryLater(String server){
        stack.push(server);
    }
}
