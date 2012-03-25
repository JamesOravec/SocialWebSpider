package edu.unlv.cs673.socialwebspider.spider.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.unlv.cs673.socialwebspider.spider.controller.BinarySizes;
import edu.unlv.cs673.socialwebspider.spider.controller.CrawlerControllerImpl;
import edu.unlv.cs673.socialwebspider.uuid.UUIDFactoryImpl;

public class CrawlControllerImplTest {

	@Test
	public final void test() {

		UUIDFactoryImpl UUIDFactory = new UUIDFactoryImpl();
		String configFolder = "spider/configs/" + UUIDFactory.generateUUID();
		String storageFolder = "spider/images/" + UUIDFactory.generateUUID();

		System.out.println("configFolder: " + configFolder);
		System.out.println("storageFolder: " + storageFolder);

		CrawlerControllerImpl crawlController = new CrawlerControllerImpl();
		try {
			crawlController.startNewCrawler(configFolder, 1, storageFolder, 3, 20, "http://www.funnypix.ca/main.php", BinarySizes.TWENTY_KB);
		} catch (Exception e) {
			fail("Exception occurred during crawling.");
		}

		System.out.println("done");
	}

}
