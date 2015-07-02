/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webcrawler;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
/**
 *
 * @author shobhitgarg
 */
public class MultithreadedCrawlerLogic {
    
    private static final Logger logger = WebCrawlerLogger.getLoggerInstance();
    private int MAX_PAGES;
    private Set<String> pagesVisited = new HashSet<String>();
    private Queue<String> pagesToVisit = new LinkedList<String>();
    
    
    public MultithreadedCrawlerLogic(String url,int maxPages)
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
    
    private synchronized void addBulkUrlToVisit(List<String> urls)
    {
        this.pagesToVisit.addAll(urls);
        notifyAll();
    }
    
    
    public void crawl()
    {
      while(true)
      {
        String currentUrl = null;
        synchronized(this)
        {
           if(pagesVisited.size() >= MAX_PAGES)
           {
               break;
           }
           long before_timestamp = new Date().getTime();
           int  before_size = pagesVisited.size();
           while(pagesToVisit.isEmpty())
           {
               try
               {
                   wait(120000); //wait maximum 2 mins 
               }
               catch(InterruptedException e)
               {
                  logger.log(Level.WARNING, "Interrupted Exception After Wait");
               }
               
               //if thread is waiting for the object (when Queue is not empty) for more than 4 mins then we need to check the dead end(no further urls)
               //for this check the size of pageVisited. If it is same from the last 4 mins that means no thread is visiting any page.
               //so we should break the wait loop too.
               long after_timestamp = new Date().getTime();
               if(after_timestamp - before_timestamp > 240000 && before_size == pagesVisited.size())
               {
                   logger.log(Level.WARNING, "PageVisited is not getting popluated from the last 4 mins");
                   break;
               }
           }
           //the time this thread get the control, this might already be done so better check it
           if(pagesVisited.size() >= MAX_PAGES)
           {
               break;
           }
           currentUrl = getNextUrlToCrawl();
        }
        //when all the url in Queue is already visited
        if(currentUrl == null)
            break;
        String thread_name = Thread.currentThread().getName();
        Object[] log_obj = new Object[] {thread_name, currentUrl};
        PageFetcher fetcher = new PageFetcher();
        logger.log(Level.INFO, "{0}  : Crawling Url Starting: {1}", log_obj);
        List<String> fetchedUrls = fetcher.crawlAndReturnLinks(currentUrl);
        if(fetchedUrls != null)
        {
            formatAndAddUrlsInQueue(fetchedUrls);
        }
        logger.log(Level.INFO, "{0} :  Crawling Url Finished: {1}", log_obj);
      }
    }
    
    private void formatAndAddUrlsInQueue(List<String> fetchedUrls)
    {
        List<String> finalUrls = new LinkedList<String>();
        Iterator<String> it = fetchedUrls.iterator();
        while(it.hasNext())
        {
            String url = it.next();
            url = removeFragmentUrlPart(url);
            if(url.equals(""))
            {
                continue;
            }
            if(!this.pagesVisited.contains(url))
                finalUrls.add(url);
        }
        if(!finalUrls.isEmpty())
        {
            addBulkUrlToVisit(finalUrls);
        
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
    
    public synchronized void printVistedLinks()
    {
        Iterator<String> it = pagesVisited.iterator();
        while(it.hasNext())
        {
            System.out.println(it.next());
        }
        
    }
    
}
