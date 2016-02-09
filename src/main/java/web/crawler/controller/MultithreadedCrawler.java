package web.crawler.controller;

import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MultithreadedCrawler extends WebCrawler {


     @Override
     public void visit(Page page) {
         String url = page.getWebURL().getURL();
         String hash;
         String parentUrl=page.getWebURL().getParentUrl();
         int  parentdocId=page.getWebURL().getParentDocid();
         Date TimeStamp=new Date();
//         String metadata;
//         String type
//         String status;
//        String geoLocation;
        int docId=page.getWebURL().getDocid();
         

       

         if (page.getParseData() instanceof HtmlParseData) {
             
    	    	 
        	 
        	 HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//             String text = htmlParseData.getText();
             String content= htmlParseData.getHtml();
             Set<WebURL> links = htmlParseData.getOutgoingUrls();

             System.out.println("URL: " + url);
             System.out.println(" Parent URL: " + parentUrl);
             System.out.println("Parent docId " + parentdocId);
             System.out.println("Timestamp: " + TimeStamp);
             System.out.println("docId: " + docId);
             System.out.println("content :"+content);
             System.out.println("Number of outgoing links: " + links.size());
         }
    }
}