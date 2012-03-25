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

	/**
	 * Default constructor.
	 */
	public AbstractCrawlerController() {
		super();
	}

	public void storeSpiderResults(int userId, int userSpecificCategoryId, String storageFolder) {

	}

	/**
	 * Used for database connection clean up.
	 */
	public void finishSpider() {
		bh.closeDbConn();
	}

	/**
	 * Setups the CrawlConfig, needed and used by crawler4j.
	 * 
	 * @param configFolder
	 *            A unique folder where crawler4j can store its configuration
	 *            files.
	 * @param maxDepth
	 *            Max depth of spidering.
	 * @param politenessDelay
	 *            Politeness delay when spidering.
	 * @return
	 */
	public CrawlConfig setupCrawlConfig(final String configFolder, final int maxDepth, final int politenessDelay) {
		/**
		 * Set up the config so crawler4j to do our bidding...
		 */
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(configFolder);
		config.setMaxDepthOfCrawling(maxDepth);
		config.setPolitenessDelay(politenessDelay);
		config.setIncludeBinaryContentInCrawling(true);
		return config;
	}

	/**
	 * Setups the CrawlController, needed and used by crawler4j.
	 * 
	 * @param entryPoint
	 *            The entry point for our spidering.
	 * @param config
	 *            The CrawlConfig, used by crawler4j.
	 * @return Returns the CrawlController that crawler4j can use.
	 * @throws Exception
	 *             Possible exceptions that can be thrown.
	 */
	public CrawlController setupCrawlController(final String entryPoint, CrawlConfig config) throws Exception {
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		controller.addSeed(entryPoint);
		return controller;
	}

}
