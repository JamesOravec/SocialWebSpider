package edu.unlv.cs673.socialwebspider.spider.types;

public interface CrawlerFactory {

	/**
	 * Creates a new crawler.
	 * 
	 * @return
	 */
	public GeneralCrawler createNewCrawler();
	
}
