package edu.unlv.cs673.socialwebspider.spider.controller;

public class Patterns {
	public Patterns() {
	}
	
	public static String patternImage = ".*(\\.(bmp|gif|jpe?g|png|tiff?))$";
	public static String filterOutNonImages = ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$";
}
