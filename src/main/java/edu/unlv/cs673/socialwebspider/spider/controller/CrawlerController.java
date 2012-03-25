package edu.unlv.cs673.socialwebspider.spider.controller;

public interface CrawlerController {

	void startSpider(String configFolder, int numberOfCrawlers, String storageFolder, int maxDepth, int politenessDelay, String entryPoint, int minBinarySize) throws Exception;

	void storeSpiderResults(String storageFolder);

	void finishSpider();
}
