package edu.unlv.cs673.socialwebspider.spider.controller;

public interface CrawlerController {
	public void startSpider();
	public void storeSpiderResults();
	public void finishSpider();
}
