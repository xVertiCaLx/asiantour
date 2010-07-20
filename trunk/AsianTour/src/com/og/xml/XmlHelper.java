package com.og.xml;

import java.io.IOException;
import java.util.Vector;

import net.rim.device.api.i18n.DateFormat;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.MenuScreen;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;
import com.og.rss.ANewsItemObj;

public class XmlHelper {
	
	private static final String url = "http://10.11.1.103/news.xml";
	
	public static String newsXmlString = "";
	
	public static void downloadNews(){
		try {
			Utility.getWebData(url, new WebDataCallback() {
				public void callback(String data) {
					newsXmlString = data;
					Vector xmlNewsItemCollection = parseDownloadedNews(data);
					for(int i=0; i<xmlNewsItemCollection.size();i++){
						XmlNewsItem xmlNewsItem = (XmlNewsItem)xmlNewsItemCollection.elementAt(i);
						boolean isNewsExistInRecordStore = RecordStoreHelper.isNewsExist(xmlNewsItem.id);
						if(!isNewsExistInRecordStore){
							StringBuffer sb = new StringBuffer();
							StringBuffer sb2 = new StringBuffer();
							String longdate = DateFormat.getInstance(DateFormat.DATE_FULL).format(xmlNewsItem.pubDate, sb, null).toString();
							String shortdate = DateFormat.getInstance(DateFormat.DATE_SHORT).format(xmlNewsItem.pubDate, sb2, null).toString();
							ANewsItemObj aNewsItemObj = new ANewsItemObj(xmlNewsItem.id,xmlNewsItem.title,xmlNewsItem.description,xmlNewsItem.thumbnailURL,xmlNewsItem.imageURL,xmlNewsItem.author, longdate, shortdate);
							MenuScreen.getInstance().newsCollection.addElement(aNewsItemObj);
							System.out.println("Added news : " + aNewsItemObj.guid);
						}else{
							System.out.println("News already exist: " + xmlNewsItem.id);
						}
					}
					MenuScreen.getInstance().invalidate();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	//Vector<XmlNewsItem>
	private static Vector parseDownloadedNews(String data){
		Vector newsCollection = null;
		try{
			data = data.substring(data.indexOf("<news>"));
		}catch (Exception e) {
			return new Vector();
		}
		newsCollection = XmlNewsParser.parse(data);
		return newsCollection;
	}
}
