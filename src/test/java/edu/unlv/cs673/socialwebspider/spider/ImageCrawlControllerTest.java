package edu.unlv.cs673.socialwebspider.spider;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.unlv.cs673.socialwebspider.spider.controller.CrawlerImagesOnlyControllerImpl;
import edu.unlv.cs673.socialwebspider.uuid.UUIDFactoryImpl;

public class ImageCrawlControllerTest {

	@Test
	public void test() {

		UUIDFactoryImpl UUIDFactory = new UUIDFactoryImpl();
		String configFolder = "spider/configs/" + UUIDFactory.generateUUID();
		String storageFolder = "spider/images/" + UUIDFactory.generateUUID();
		
		System.out.println("configFolder: " + configFolder);
		System.out.println("storageFolder: " + storageFolder);

		CrawlerImagesOnlyControllerImpl crawlController = new CrawlerImagesOnlyControllerImpl();
		try {
			crawlController.startNewCrawler(configFolder, 1, storageFolder, 3, 20, "http://www.funnypix.ca/main.php");
		} catch (Exception e) {
			fail("Exception occurred during crawling.");
		}

		System.out.println("done");
	}

}
