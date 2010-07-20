package com.og.xml;

import java.io.IOException;
import java.util.Vector;

import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;

public class XmlHelper {
	
	private static final String url = "http://10.11.1.100/news.xml";
	
	public static String newsXmlString = "";
	
	public static void downloadNews(){
		try {
			Utility.getWebData(url, new WebDataCallback() {
				public void callback(String data) {
					newsXmlString = data;
					System.out.println("Download is complete: " + data);
					//Vector newsCollection = 
					parseDownloadedNews(data);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private static Vector parseDownloadedNews(String data){
		Vector newsCollection = null;
		data = data.substring(data.indexOf("<news>"));
		newsCollection = XmlNewsParser.parse(data);
		return newsCollection;
	}
}
