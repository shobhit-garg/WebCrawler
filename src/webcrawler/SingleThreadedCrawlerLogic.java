/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webcrawler;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shobhitgarg
 */
public class SingleThreadedCrawlerLogic {
    
    private static final Logger logger = WebCrawlerLogger.getLoggerInstance();
    private int MAX_PAGES;
    private Set<String> pagesVisited = new HashSet<String>();
    private Queue<String> pagesToVisit = new LinkedList<String>();
    
    
    public SingleThreadedCrawlerLogic(String url,int maxPages)
    {
        addUrlToVisit(url);
        this.MAX_PAGES = maxPages;
    }
    private String getNextUrlToCrawl()
    {
        String nextUrl;
        do
        {
            nextUrl = this.pagesToVisit.poll();
            if(nextUrl  == null)
               return null ;
        } while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
    
    private void addUrlToVisit(String url)
    {
        if(!this.pagesVisited.contains(url))
            this.pagesToVisit.offer(url);
    }
    
    
    public void crawl()
    {        
      while(pagesToVisit.size() > 0 && pagesVisited.size() < MAX_PAGES)
      {
          String currentUrl = getNextUrlToCrawl();
          PageFetcher fetcher = new PageFetcher();
          logger.log(Level.INFO, "Crawling Url Starting: {0}", currentUrl);
          List<String> fetchedUrls = fetcher.crawlAndReturnLinks(currentUrl);
          if(fetchedUrls != null)
          {
              formatAndAddUrlsInQueue(fetchedUrls);
          }
          logger.log(Level.INFO, "Crawling Url Finished: {0}\n\n", currentUrl);
      }
      logger.log(Level.INFO, "No of Pages Visited: {0}", pagesVisited.size());
    }
    
    private void formatAndAddUrlsInQueue(List<String> fetchedUrls)
    {
        Iterator<String> it = fetchedUrls.iterator();
        while(it.hasNext())
        {
            String url = it.next();
            url = removeFragmentUrlPart(url);
            if(url.equals(""))
            {
                continue;
            }
            addUrlToVisit(url);
        }
    
    }
    
    private String removeFragmentUrlPart(String url)
    {
        //In http request is send only for the part before hash
        //part after hash is used for javascript and ajax operations
        //Refer https://en.wikipedia.org/wiki/Fragment_identifier
        String [] s = url.split("#");
        return s[0];
    }
  
    public void printVistedLinks()
    {
        Iterator<String> it = pagesVisited.iterator();
        while(it.hasNext())
        {
            System.out.println(it.next());
        }
        
    }
    
}
