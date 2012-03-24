package edu.unlv.cs673.socialwebspider.spider.controller;

public interface CrawlerController {

	public void startSpider(String configFolder, int numberOfCrawlers, String storageFolder, int maxDepth, int politenessDelay, String entryPoint, int minBinarySize);

	public void storeSpiderResults(String storageFolder);

	public void finishSpider();
}
