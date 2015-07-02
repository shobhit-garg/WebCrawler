/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webcrawler;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 *
 * @author shobhitgarg
 */

 class WebCrawlerLogger {
    private static Logger logger ;
    private static FileHandler fh = null;
    private static void initialize_logger() {
        logger = Logger.getLogger("WebCrawlerLogger");
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-HHmmss");
        try {
            fh = new FileHandler("././logs/"
                    + format.format(System.currentTimeMillis()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }
    
    public static Logger getLoggerInstance()
    {
        if(logger == null)
            initialize_logger();
        return logger;
    }
}
