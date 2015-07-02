# WebCrawler

There are five classes in this application:

1.WebCrawler.java : Contains the main method. 
In main method you can opt you want to run crawler in single thread or in multiple threads.
Please change these settings according to your need.

        boolean multi_threaded = true;
        String url = "http://python.org";
        int crawl_limit = 100;
        int no_of_threads = 4; 

2.PageFetcher.java : Contains the code for fetching the webpage over the network. 
  For doing this is class is importing Jsoup library.
  
3.WebCrawlerLogger.java : For returning the logger instance.This creates a file in logs/ folder which contains all the logs.

4.SingleThreadedCrawlerLogic.java : If you want to run crawler as a single threaded application, 
  the logic related to it present here.

5.MultithreadedCrawlerLogic.java : If want to run crawler as multithreaded application.Then the logic contains here.


External Dependency: Jsoup library for fetching and parsing the web pages.Visit http://jsoup.org/ 
You can download the jars from http://jsoup.org/download .

Note:You can run a single thread in multithreaded architecture, i created SingleThreadedCrawlerLogic to better give you the 
idea of crawler logic which is very easy to understand.



