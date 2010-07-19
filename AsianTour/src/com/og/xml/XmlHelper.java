package com.og.xml;

import java.io.IOException;

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
	
	private static void downloadNews(){
		if(STATE==STATE_DOWNLOADING){
			return;
		}
		STATE = STATE_DOWNLOADING;
		try {
			Utility.getWebData(url, new WebDataCallback() {
				public void callback(String data) {
					STATE = STATE_DOWNLOAD_COMPLETE;
					newsXmlString = data;
				}
			});
		} catch (IOException e) {
			STATE = STATE_ERROR;
			e.printStackTrace();
			return;
		}
	}
	
	public static String getDownloadedNews(){
		downloadNews();
		if (STATE == STATE_DOWNLOADING) {
			return "";
		}		
		return newsXmlString;
	}
}
