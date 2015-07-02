/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webcrawler;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author shobhitgarg
 */
public class PageFetcher {
    
    private static final Logger logger = WebCrawlerLogger.getLoggerInstance();
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.67 Safari/537.36";
    private Document htmlDocument;
    private List<String> links ;
    public List<String> crawlAndReturnLinks(String url)
    {
        String thread_name = Thread.currentThread().getName();
        Object[] log_obj = new Object[] {thread_name, url};
        try
        {  
            Connection connection;
            try
            {
                connection = Jsoup.connect(url).userAgent(USER_AGENT);
            }
            catch(IllegalArgumentException  e)
            {
                logger.log(Level.WARNING, "{0} : Incorrect Url/Issue in establishing connection : {1}", log_obj);
                return null;
            }
            this.htmlDocument = connection.get(); 
            if(!(connection.response().statusCode() == 200))
             {
                 logger.log(Level.SEVERE, "{0} : Status code not 200 : {1}", log_obj);
                 return null;
             }
             if(!connection.response().contentType().contains("text/html"))
             {
                logger.log(Level.INFO, "{0} : File type not HTML  : {1}", log_obj);
                return null;
             }
            
            logger.log(Level.INFO, "{0} : Received Webpage at Url  : {1}", log_obj);
            links = new LinkedList<String>();
            Elements linksOnPage = htmlDocument.select("a[href]");
            logger.log(Level.INFO, "{0} : Found ( {0} ) links", linksOnPage.size());
            for(Element link : linksOnPage)
            {
                //remove fragment url and check the set
                links.add(link.absUrl("href"));
            }
            return links;
        }
        catch(Exception e)
        {
            // We were not successful in our HTTP request
            logger.log(Level.SEVERE, "{0} : Exception in executing  crawlAndReturnLinks for Url : {1}", log_obj);
            return null;
        }
    }   
}
