package group.controller.util;

import org.apache.log4j.Logger;

public class LogPrinter {
    public static void printException(Logger logger, Exception e) {
        logger.error(e);
        for (StackTraceElement element : e.getStackTrace()) {
            if (element.toString().contains("javax.servlet.http.HttpServlet.service")) {
                return;
            }
            logger.error(element);
        }
    }

    public static void printReqResp(Logger logger, Object res, Object resp) {
        logger.info(res);
        logger.info(resp);
    }
}
