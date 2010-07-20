package com.og.xml;

import java.io.IOException;
import java.util.Vector;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.MenuScreen;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;
import com.og.rss.ANewsItemObj;

public class XmlHelper {
	
	private static final String url = "http://10.11.1.100/news.xml";
	
	public static String newsXmlString = "";
	
	public static void downloadNews(){
		try {
			Utility.getWebData(url, new WebDataCallback() {
				public void callback(String data) {
					newsXmlString = data;
					System.out.println("Download is complete: " + data);
					Vector xmlNewsItemCollection = parseDownloadedNews(data);
					for(int i=0; i<xmlNewsItemCollection.size();i++){
						XmlNewsItem xmlNewsItem = (XmlNewsItem)xmlNewsItemCollection.elementAt(i);
						boolean isNewsExistInRecordStore = RecordStoreHelper.isNewsExist(xmlNewsItem.id);
						if(!isNewsExistInRecordStore){
							//ANewsItemObj aNewsItemObj = new ANewsItemObj(xmlNewsItem.id,xmlNewsItem.title,xmlNewsItem.description,xmlNewsItem.thumbnailURL,xmlNewsItem.imageURL,xmlNewsItem.author);
						}
					}
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
		data = data.substring(data.indexOf("<news>"));
		newsCollection = XmlNewsParser.parse(data);
		return newsCollection;
	}
}
