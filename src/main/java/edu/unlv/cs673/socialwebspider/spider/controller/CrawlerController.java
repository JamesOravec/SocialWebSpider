package edu.unlv.cs673.socialwebspider.spider.controller;

public interface CrawlerController {

	/**
	 * Used to start a spidering.
	 * 
	 * @param configFolder		Unique folder where crawler4j can store its configuration files.
	 * @param numberOfCrawlers	Number of crawlers for this spidering.
	 * @param storageFolder		Unique location of where to store the binary files.
	 * @param maxDepth			How many 
	 * @param politenessDelay
	 * @param entryPoint
	 * @param minBinarySize
	 * @throws Exception
	 */
	
	/**
	 * The following is used to start a new spidering.
	 * 
	 * Example Values: configFolder spider/config/UUID numberOfCrawlers 1
	 * storageFolder spider/images/UUID maxDepth 3 politenessDelay 200
	 * entryPoint http://www.funnypix.ca/main.php
	 * 
	 * @param userId
	 *            The user's unique id that identifies them in the database..
	 * @param userSpecificCategoryId
	 *            The id of the .
	 * @param configFolder
	 *            This is where the configuration settings are stored for a
	 *            crawl.
	 * @param numberOfCrawlers
	 *            The number of crawlers (threads).
	 * @param storageFolder
	 *            Where the output of the spidering goes, e.g. images files.
	 * @param maxDepth
	 *            Max link depth from the entryPoint.
	 * @param politenessDelay
	 *            200 <-- Minimum for spidering laws.
	 * @param entryPoint
	 *            URL of where to start.
	 * @param minBinarySize
	 *            Minimum size of binary in bytes.
	 * @throws Exception
	 *             Possible exception that can be thrown.
	 */
	void startSpider(int userId, int userSpecificCategoryId, String configFolder, int numberOfCrawlers, String storageFolder, 
			int maxDepth, int politenessDelay, String entryPoint, int minBinarySize) throws Exception;

	void storeSpiderResults(int userId, int userSpecificCategoryId, String storageFolder);

	void finishSpider();
}
