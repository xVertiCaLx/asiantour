package com.og.xml;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import net.rim.device.api.xml.jaxp.XMLParser;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;
import net.rim.device.api.xml.parsers.ParserConfigurationException;

import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;

public class XmlHelper {
	
	private static final String url = "http://10.11.1.100/news.xml";
	public static int STATE = 0;
	
	public static final int STATE_IDLE = 0;
	public static final int STATE_DOWNLOADING = 1;
	public static final int STATE_ERROR = -1;
	public static final int STATE_DOWNLOAD_COMPLETE = 2;
	
	public static String newsXmlString = "";
	
	public static void downloadNews(){
		if(STATE==STATE_DOWNLOADING){
			return;
		}
		STATE = STATE_DOWNLOADING;
		try {
			Utility.getWebData(url, new WebDataCallback() {
				public void callback(String data) {
					STATE = STATE_DOWNLOAD_COMPLETE;
					newsXmlString = data;
					System.out.println("Download is complete: " + data);
					Vector newsCollection = parseDownloadedNews(data);
					
				}
			});
		} catch (IOException e) {
			STATE = STATE_ERROR;
			e.printStackTrace();
			return;
		}
	}
	
	private static Vector parseDownloadedNews(String data){
		Vector newsCollection = new Vector();
		data = data.substring(data.indexOf("<news>"));
		XmlNewsParser.parse(data);
		return newsCollection;
	}
}
