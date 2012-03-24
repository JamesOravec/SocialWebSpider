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

import java.io.File;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;
import edu.unlv.cs673.socialwebspider.spider.Cryptography;

/**
 * The following file is based on the ImageCrawlController example provided 
 * by the crawler4j website. It has been modified so that it works with our project.
 *
 * Updates/Modifications by: James Oravec (http://www.jamesoravec.com)
 */
public class CrawlerImagesOnlyImpl extends WebCrawler {

	private static final Pattern patternToFilterOut = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private static final Pattern patternToSave = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

	private static File storageFolder;
	private static String[] crawlDomains;
	
	final static int MIN_IMAGE_SIZE = 10 * 1024 * 10;

	public static void configure(final String[] crawlDomains, final String storageFolderName) {
		CrawlerImagesOnlyImpl.crawlDomains = crawlDomains;

		storageFolder = new File(storageFolderName);
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}
	}

	@Override
	public final boolean shouldVisit(final WebURL url) {
		String href = url.getURL().toLowerCase();
		if (patternToFilterOut.matcher(href).matches()) {
			return false;
		}

		if (patternToSave.matcher(href).matches()) {
			return true;
		}

		for (String domain : crawlDomains) {
			if (href.startsWith(domain)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final void visit(final Page page) {
		String url = page.getWebURL().getURL();

		// We are only interested in processing images
		if (!(page.getParseData() instanceof BinaryParseData)) {
			return;
		}

		if (!patternToSave.matcher(url).matches()) {
			return;
		}

		// Not interested in very small images
		// if (page.getContentData().length < 10 * 1024) {
		if (page.getContentData().length < MIN_IMAGE_SIZE) {
			return;
		}

		// get a unique name for storing this image
		String extension = url.substring(url.lastIndexOf("."));
		String hashedName = Cryptography.MD5(url) + extension;

		// store image
		IO.writeBytesToFile(page.getContentData(), storageFolder.getAbsolutePath() + "/" + hashedName);

		System.out.println("Stored: " + url);
	}
}