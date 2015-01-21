import java.io.File;

/**
 * Created by wuyanzhe on 1/16/15.
 */
public class missedSites {
    private String[] sites;

    public missedSites(String[] sites) {
        this.sites = sites;
    }

    public missedSites(String filename){
        int i=0;
        sites = new String[500];
        sites target = new sites(filename);
        File file;
        String s;
        while((s=target.nextSite()) != null) {
            file = new File("Record/"+s+".txt");
            if (!file.exists()) {
                sites[i] = s;
                i++;
            }
        }
    }

    public String[] getMissed(){
        int i = 0;
        for(String s:sites){
            if(s != null){
                i++;
            }
        }
        String[] list = new String[i];
        for(String s:sites){
            if(s != null){
                list[i-1] = s;
                i--;
            }
        }
        return list;
    }

    public void printMissedSites(){
        for(String s:sites){
            if(s != null){
                System.out.println(s);
            }
        }
    }

}
