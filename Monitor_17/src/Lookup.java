import org.xbill.DNS.*;
import org.xbill.DNS.DClass;import org.xbill.DNS.Message;import org.xbill.DNS.Name;import org.xbill.DNS.Rcode;import org.xbill.DNS.Record;import org.xbill.DNS.ReverseMap;import org.xbill.DNS.Section;import org.xbill.DNS.SimpleResolver;import org.xbill.DNS.Type;

import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;import java.lang.String;import java.lang.System;import java.net.InetAddress;

/**
 * Created by wuyanzhe on 1/14/15.
 */
public class Lookup {
    static Name name = null;
    static int type = Type.A, dclass = DClass.IN;

    static void usage() {
        System.out.println("Usage: dig [@server] name [<type>] [<class>] " +
                "[options]");
        System.exit(0);
    }

    static void doQuery(Message response, long ms) throws IOException {
        //System.out.println("; java dig 0.0");
        String s = String.valueOf(response);
        //System.out.println(s);
        //System.out.println(response);
        //System.out.println(";; Query time: " + ms + " ms");
    }

    static void doAXFR(Message response) throws IOException {
        System.out.println("; java dig 0.0 <> " + name + " axfr");
        if (response.isSigned()) {
            System.out.print(";; TSIG ");
            if (response.isVerified())
                System.out.println("ok");
            else
                System.out.println("failed");
        }

        if (response.getRcode() != Rcode.NOERROR) {
            System.out.println(response);
            return;
        }

        Record[] records = response.getSectionArray(Section.ANSWER);
        for (int i = 0; i < records.length; i++)
            System.out.println(records[i]);

        System.out.print(";; done (");
        System.out.print(response.getHeader().getCount(Section.ANSWER));
        System.out.print(" records, ");
        System.out.print(response.getHeader().getCount(Section.ADDITIONAL));
        System.out.println(" additional)");
    }

    public static Message Lookup(String[] argv) throws IOException {
        String server = null;
        int arg;
        Message query, response;
        Record rec;
        SimpleResolver res = null;
        boolean printQuery = false;
        long startTime, endTime;

        if (argv.length < 1) {
            usage();
        }

        try {
            arg = 0;
            if (argv[arg].startsWith("@"))
                server = argv[arg++].substring(1);

            if (server != null)
                res = new SimpleResolver(server);
            else
                res = new SimpleResolver();

            String nameString = argv[arg++];
            if (nameString.equals("-x")) {
                name = ReverseMap.fromAddress(argv[arg++]);
                type = Type.PTR;
                dclass = DClass.IN;
            }
            else {
                name = Name.fromString(nameString, Name.root);
                type = Type.value(argv[arg]);
                if (type < 0)
                    type = Type.A;
                else
                    arg++;

                dclass = DClass.value(argv[arg]);
                if (dclass < 0)
                    dclass = DClass.IN;
                else
                    arg++;
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            if (name == null)
                usage();
        }
        if (res == null)
            res = new SimpleResolver();

        rec = Record.newRecord(name, type, dclass);
        query = Message.newQuery(rec);
        if (printQuery)
            System.out.println(query);
        startTime = System.currentTimeMillis();
        response = res.send(query);
        endTime = System.currentTimeMillis();

        if (type == Type.AXFR)
            doAXFR(response);
        else
            doQuery(response, endTime - startTime);
        return response;
    }
}
