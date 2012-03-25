package edu.unlv.cs673.socialwebspider.spider.controller;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.unlv.cs673.database.BlobHandler;

public abstract class AbstractCrawlerController implements CrawlerController {
	/**
	 * Need access to a blob handler for storing blobs to the database.
	 */
	protected BlobHandler bh = null;
	
	public AbstractCrawlerController(){
		super();
	}
	
	public void storeSpiderResults(int userId, int userSpecificCategoryId, String storageFolder){
		
	}

	public void finishSpider(){
		bh.closeDbConn();		
	}
	
	public CrawlConfig setupCrawlConfig(final String configFolder, final int maxDepth, final int politenessDelay) {
		// Set things up and call crawler4j to do our bidding...
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(configFolder);
		config.setMaxDepthOfCrawling(maxDepth);
		config.setPolitenessDelay(politenessDelay);
		/**
		 * Binary content requires the following line to be set to true.
		 */
		config.setIncludeBinaryContentInCrawling(true);
		return config;
	}

	public CrawlController setupCrawlController(final String entryPoint, CrawlConfig config) throws Exception {
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		controller.addSeed(entryPoint);
		return controller;
	}
	

}
