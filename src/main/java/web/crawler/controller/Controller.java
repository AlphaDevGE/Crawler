package web.crawler.controller;


import java.util.ArrayList;
import java.util.List;

import web.crawler.constant.Paths;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	static boolean shouldExtract=false;
	static String specificDomain="ocean.pcwerk.com";

	public static void main(String[] args) throws Exception {
		
			String crawlStorageFolder = "D:/webcrawler/crawlingFiles";
			int numberOfCrawlers = 8;
			CrawlConfig config = new CrawlConfig();
			config.setCrawlStorageFolder(crawlStorageFolder);
			config.setSocketTimeout(-1);

		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(300);
		config.setIncludeBinaryContentInCrawling(true);

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		config.setMaxDepthOfCrawling(Integer.parseInt(args[1]));

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		


		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(false);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
//same thing here. ok ..
		controller.addSeed(args[0]);

		if(args.length>2){
		if(args[2]!=null&&args[2].length()>0&&args[2].trim().equals("-e")){
			shouldExtract=true;
			}
		}


		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MultithreadedCrawler.class, numberOfCrawlers);
		System.out.println("<-------------CRAWLING FINISHED----------------->");
		
		
	}
}



