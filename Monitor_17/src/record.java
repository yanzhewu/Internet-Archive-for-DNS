import org.xbill.DNS.Message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wuyanzhe on 1/14/15.
 */
/**
 * this class is used to make the message serializable
 */
public class record implements Serializable {
    private String result;
    private Date date;
    private String server;
    private String A_record;
    private String AAAA_record;
    private String TYPE257_record;
    private String MX_record;
    private String TXT_record;
    private String SOA_record;


    public record(ArrayList<Message> list, String server) {
        this.server = server;
        this.A_record = list.get(0).toString();
        this.AAAA_record = list.get(1).toString();
        this.TYPE257_record = list.get(2).toString();
        this.MX_record = list.get(3).toString();
        this.TXT_record = list.get(5).toString();
        this.SOA_record = list.get(4).toString();
        this.date = new Date();
    }

    public String getTYPE257_record() {
        return TYPE257_record;
    }

    public void setTYPE257_record(String TYPE257_record) {
        this.TYPE257_record = TYPE257_record;
    }

    public String getA_record() {
        return A_record;
    }

    public void setA_record(String a_record) {
        A_record = a_record;
    }

    public String getAAAA_record() {
        return AAAA_record;
    }

    public void setAAAA_record(String AAAA_record) {
        this.AAAA_record = AAAA_record;
    }

    public String getMX_record() {
        return MX_record;
    }

    public void setMX_record(String MX_record) {
        this.MX_record = MX_record;
    }

    public String getTXT_record() {
        return TXT_record;
    }

    public void setTXT_record(String TXT_record) {
        this.TXT_record = TXT_record;
    }

    public String getSOA_record() {
        return SOA_record;
    }

    public void setSOA_record(String SOA_record) {
        this.SOA_record = SOA_record;
    }

    public record(Message result) {
        this.result = result.toString();
        date = new Date();
    }

    public record(String result){
        this.result = result;
        date = new Date();
    }

    public void setResult(String result) {
        this.result = result;
    }

    public record(Message result, String server){
        this.result = result.toString();
        this.server = server;
    }

    public String getResult() {
        return result;
    }

    public void setResult(Message result) {
        this.result = result.toString();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
