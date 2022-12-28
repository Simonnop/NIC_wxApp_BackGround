import group.controller.util.LogPrinter;
import org.apache.log4j.Logger;

import java.net.URLDecoder;

public class TestLog {


    public static void main(String[] args) {
        Logger logger = Logger.getLogger(TestLog.class);

        String a = "method=take&data=%7B%22missionID%22:%20%222345121201%22,%22username%22:%20%22test4%22,%22kind%22:%20%22hi%22%7D";

        String s = URLDecoder.decode(a);

        System.out.println(s);

        LogPrinter.printException(logger, new Exception());
    }
}
