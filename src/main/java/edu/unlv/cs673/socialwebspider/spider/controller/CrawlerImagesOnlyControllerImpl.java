/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.unlv.cs673.socialwebspider.spider.controller;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.unlv.cs673.database.BlobHandler;

/**
 * The following file is based on the ImageCrawlController example provided by
 * the crawler4j website. It has been modified so that it works with our
 * project.
 * 
 * Updates/Modifications by: James Oravec (http://www.jamesoravec.com)
 */
public class CrawlerImagesOnlyControllerImpl extends AbstractCrawlerController implements CrawlerController {
	public CrawlerImagesOnlyControllerImpl() {
	}

	@Override
	public final void startSpider(final int userId, final int userSpecificCategoryId, final String configFolder, final int numberOfCrawlers, final String storageFolder, final int maxDepth,
			final int politenessDelay, final String entryPoint, final int minBinarySize) throws Exception {
		startNewCrawler(configFolder, numberOfCrawlers, storageFolder, maxDepth, politenessDelay, entryPoint, minBinarySize);
		bh = new BlobHandler();
		storeSpiderResults(userId, userSpecificCategoryId, storageFolder);
		finishSpider();
	}

	/**
	 * The following is used to start a new crawl.
	 * 
	 * Example Values: configFolder spider/config/UUID numberOfCrawlers 1
	 * storageFolder spider/images/UUID maxDepth 3 politenessDelay 200
	 * entryPoint http://www.funnypix.ca/main.php
	 * 
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
	public final void startNewCrawler(final String configFolder, final int numberOfCrawlers, final String storageFolder, final int maxDepth, final int politenessDelay, final String entryPoint,
			final int minBinarySize) throws Exception {

		CrawlConfig config = setupCrawlConfig(configFolder, maxDepth, politenessDelay);
		String[] crawlDomains = new String[] { "" + entryPoint };
		CrawlController controller = setupCrawlController(entryPoint, config);

		CrawlerImagesOnlyImpl crawler = new CrawlerImagesOnlyImpl(minBinarySize);
		CrawlerImagesOnlyImpl.configure(crawlDomains, storageFolder);

		controller.start(CrawlerImpl.class, numberOfCrawlers);
	}

}