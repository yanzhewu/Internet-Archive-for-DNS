import org.xbill.DNS.Message;

import java.io.Serializable;
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

    public record(Message result) {
        this.result = result.toString();
        date = new Date();
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
}
