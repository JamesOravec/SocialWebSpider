package edu.unlv.cs673.socialwebspider.spider.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.unlv.cs673.socialwebspider.uuid.UUIDFactoryImpl;

public class CrawlerImagesOnlyImplTest {

	/**
	 * This is the same test as CrawlControllerImplTest. It tests a class that extends the CrawlerImpl,
	 * as crawler4j requires the patterns to be static, thus we need different classes for different
	 * pattern searches.
	 */
	@Test
	public void test() {

		UUIDFactoryImpl UUIDFactory = new UUIDFactoryImpl();
		String configFolder = "spider/configs/" + UUIDFactory.generateUUID();
		String storageFolder = "spider/images/" + UUIDFactory.generateUUID();

		System.out.println("configFolder: " + configFolder);
		System.out.println("storageFolder: " + storageFolder);

		CrawlerImagesOnlyControllerImpl crawlController = new CrawlerImagesOnlyControllerImpl();
		try {
			crawlController.startNewCrawler(configFolder, 1, storageFolder, 3, 20, "http://www.funnypix.ca/main.php", BinarySizes.twentyKb);
		} catch (Exception e) {
			fail("Exception occurred during crawling.");
		}

		System.out.println("done");

	}

}
