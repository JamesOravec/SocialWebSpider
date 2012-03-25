package edu.unlv.cs673.socialwebspider.spider.controller;

public class Patterns {
	public Patterns() {
	}
	
	public static String patternImage = ".*(\\.(bmp|gif|jpe?g|png|tiff?))$";
	public static String filterOutNonImages = ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$";
	
	public static String patternGeneral = ".*(\\.(mid|mp2|mp3|mp4|wav|avi|mov|mpeg|m4v|pdf|bmp|wmv|wma|zip|rar|gz|gif|jpe?g|png|tiff?))$";
	public static String filterOutNonGeneral = ".*(\\.(css|js|ram|rm|smil|swf))$";
}
