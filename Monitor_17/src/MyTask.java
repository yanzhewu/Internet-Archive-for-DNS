

import java.util.TimerTask;

/**
 * Created by wuyanzhe on 1/16/15.
 */
//This is a task for qury once
public class MyTask extends TimerTask {
    public MyTask(){
    }

    @Override
    public void run() {
        queryOnce queryOnce = new queryOnce();
        sites sites = new sites("website.txt");
        queryOnce.doQueryOnce(sites);
    }
}
