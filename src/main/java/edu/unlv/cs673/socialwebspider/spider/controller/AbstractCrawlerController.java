package edu.unlv.cs673.socialwebspider.spider.controller;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.unlv.cs673.database.BlobHandler;
import edu.unlv.cs673.socialwebspider.util.files.FolderManipImpl;

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

	@Override
	public void storeSpiderResults(int userId, String blobBinaryFolderPath, int userSpecificCategoryId) {
			bh.insertBlobsFromFolder(userId, blobBinaryFolderPath, userSpecificCategoryId);
	}

	@Override
	public void finishSpider(String configFolder, String storageFolder) {
		System.out.println("Start db close");
		// Close db connection.
		bh.closeDbConn();
		System.out.println("End db close");
		
		System.out.println("Start delete folders");
		// Delete temp folders.
		FolderManipImpl fm = new FolderManipImpl();
		fm.deleteFolder(storageFolder);
		fm.deleteFolder(configFolder);
		System.out.println("End delete folders");
	}

	@Override
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

	@Override
	public CrawlController setupCrawlController(final String entryPoint, CrawlConfig config) throws Exception {
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		controller.addSeed(entryPoint);
		return controller;
	}

}
