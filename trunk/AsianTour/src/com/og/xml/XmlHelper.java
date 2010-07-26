package com.og.xml;

import java.io.IOException;
import java.util.Vector;

import net.rim.device.api.i18n.DateFormat;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.MenuScreen;
import com.og.app.gui.NewsPanel;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;
import com.og.rss.ANewsItemObj;
import com.og.app.util.DataCentre;
import com.og.app.gui.TableListField;

public class XmlHelper {
	
	//download news
	private static final String url = "http://119.75.4.94/singtel/bpl/news.xml";
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
					MenuScreen.getInstance().newsPanel.loadNews(0);
					NewsPanel.newsPanel.newsList.setSize(MenuScreen.getInstance().newsCollection.size());
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
	
	//download data for TV Schedule data
	private static final String tvTimes_url = "http://203.116.81.61:9191/BlackBerry/BlackBerryWebService.asmx/ListTVTimes";
	public static String tvTimes_xml = "";
	
	public static void downloadTvTimes(){
		System.out.println("enter downloadTvTimes");
		try {
			Utility.getWebData(tvTimes_url, new WebDataCallback() {
				public void callback(String data) {
					tvTimes_xml = data;
					Vector xmlTvTimes = parseDownloadedTvTimes(data);
					for(int i=0; i<xmlTvTimes.size();i++){
						XmlTvTimesItem xmlTvItem = (XmlTvTimesItem)xmlTvTimes.elementAt(i);
						//String tvName, String tvDate, String tvBroadcastTime, String tvBroadcaster, String tvRegion
						DataCentre itemObj = new DataCentre(xmlTvItem.programmeName, xmlTvItem.date, xmlTvItem.time, xmlTvItem.broadcaster, xmlTvItem.region);
						//MenuScreen.getInstance().newsCollection.addElement(itemObj);
						System.out.println("Added tv times : " + itemObj);
						MenuScreen.getInstance().tvTimesCollection.addElement(itemObj);
					}
					//MenuScreen.getInstance().newsPanel.loadNews(0);
					
					//NewsPanel.newsPanel.newsList.setSize(MenuScreen.getInstance().newsCollection.size());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("aloy.downloadedTvTimes.exceptione: " + e);
			return;
		}
	}
	
	private static Vector parseDownloadedTvTimes(String data){
		Vector tvTimesCollection = null;
		try{
			data = data.substring(data.indexOf("<ArrayOfAnyType"));
			System.out.println(data);
		}catch (Exception e) {
			return new Vector();
		}
		tvTimesCollection = XmlTvScheduleParser.parse(data);
		return tvTimesCollection;
	}
	
}
