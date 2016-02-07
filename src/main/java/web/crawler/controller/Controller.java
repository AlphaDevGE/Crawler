package web.crawler.controller;




import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	public static void main(String[] args) throws Exception {
		/*
		if (args.length != 2) {
			System.out.println("Needed parameters: ");
			System.out.println("\t rootFolder (it will contain intermediate crawl data)");
			System.out.println("\t numberOfCralwers (number of concurrent threads)");
			return;
		}
		*/

		/*
		 * crawlStorageFolder is a folder where intermediate crawl data is
		 * stored.
		 */
		String crawlStorageFolder = "D:/webcrawler";
		
		String userAgent = "UCI IR 44727666 34925619 27209372";

		/*
		 * numberOfCrawlers shows the number of concurrent threads that should
		 * be initiated for crawling.
		 */
		int numberOfCrawlers = 10;

		CrawlConfig config = new CrawlConfig();
		
		config.setUserAgentString(userAgent);

		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setSocketTimeout(90000);

		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(300);

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		config.setMaxDepthOfCrawling(2);

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		config.setMaxPagesToFetch(-1);


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

		controller.addSeed("http://www.calstatela.edu/");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MultithreadedCrawler.class, numberOfCrawlers);
		
		System.out.println("Finish crawling! Congratulation!");
	}
}

