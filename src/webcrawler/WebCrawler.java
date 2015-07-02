/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webcrawler; 
/**
 *
 * @author shobhitgarg
 */
public class WebCrawler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        boolean multi_threaded = true;
        String url = "http://python.org";
        int crawl_limit = 100;
        int no_of_threads = 4; //No of parallel threads in case of nultithreaded
        

        if(multi_threaded)
        {
            MultithreadedCrawlerLogic cl = new MultithreadedCrawlerLogic(url,crawl_limit);
            CrawlerRunnbale cr = new CrawlerRunnbale(cl);
            Thread[] thread_arr = new Thread[no_of_threads];
            for(int i = 0; i < no_of_threads ; i++)
            {
                String thread_name = "Thread"+i;
                Thread crawler = new Thread(cr,thread_name);
                thread_arr[i] = crawler; 
                crawler.start();
            }
            
            //first all threads should get executed then the program make further progress 
            for(int i = 0; i < no_of_threads ; i++)
            {
                try{
                thread_arr[i].join();
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            
            cl.printVistedLinks();
            
        }   
        else
        {   
            SingleThreadedCrawlerLogic cl = new SingleThreadedCrawlerLogic(url,crawl_limit);
            cl.crawl();
            cl.printVistedLinks();
        }    
    }
    
}
 

class CrawlerRunnbale implements Runnable
{
    
 MultithreadedCrawlerLogic cl;
 CrawlerRunnbale(MultithreadedCrawlerLogic cl)
 {
     this.cl = cl;
 }       

 @Override
 public void run()
 {
    cl.crawl();
 }


}
