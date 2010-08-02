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

public class XmlHelper {

	// Download news
	private static final String url = "http://119.75.4.94/singtel/bpl/news.xml";
	public static String newsXmlString = "";

	public static void downloadNews() {
		try {
			Utility.getWebData(url, new WebDataCallback() {
				public void callback(byte[] data) {
					newsXmlString = new String(data);
					Vector xmlNewsItemCollection = parse(newsXmlString, "News");
					for (int i = 0; i < xmlNewsItemCollection.size(); i++) {
						XmlNewsItem xmlNewsItem = (XmlNewsItem) xmlNewsItemCollection
								.elementAt(i);
						boolean isNewsExistInRecordStore = RecordStoreHelper
								.isNewsExist(xmlNewsItem.id);
						if (!isNewsExistInRecordStore) {
							StringBuffer sb = new StringBuffer();
							StringBuffer sb2 = new StringBuffer();
							String longdate = DateFormat.getInstance(
									DateFormat.DATE_FULL).format(
									xmlNewsItem.pubDate, sb, null).toString();
							String shortdate = DateFormat.getInstance(
									DateFormat.DATE_SHORT).format(
									xmlNewsItem.pubDate, sb2, null).toString();
							ANewsItemObj aNewsItemObj = new ANewsItemObj(
									xmlNewsItem.id, xmlNewsItem.title,
									xmlNewsItem.description,
									xmlNewsItem.thumbnailURL,
									xmlNewsItem.imageURL, xmlNewsItem.author,
									longdate, shortdate);
							MenuScreen.getInstance().newsCollection
									.addElement(aNewsItemObj);
							System.out.println("Added news : "
									+ aNewsItemObj.guid);
						} else {
							System.out.println("News already exist: "
									+ xmlNewsItem.id);
						}
					}
					MenuScreen.getInstance().newsPanel.loadNews(0);
					NewsPanel.newsPanel.newsList.setSize(MenuScreen
							.getInstance().newsCollection.size());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	// Vector<XmlNewsItem>
//	private static Vector parseDownloadedNews(String data) {
//		Vector newsCollection = null;
//		try {
//			data = data.substring(data.indexOf("<news>"));
//		} catch (Exception e) {
//			return new Vector();
//		}
//		newsCollection = XmlNewsParser.parse(data);
//		return newsCollection;
//	}

	// download data for TV Schedule data
	private static final String tvTimes_url = "http://203.116.81.61:9191/BlackBerry/BlackBerryWebService.asmx/ListTVTimes";
	public static String tvTimes_xml = "";

	public static void downloadTvTimes() {
		System.out.println("enter downloadTvTimes");
		try {
			Utility.getWebData(tvTimes_url, new WebDataCallback() {
				public void callback(byte[] data) {
					tvTimes_xml = new String(data);
					Vector xmlTvTimes = parse(tvTimes_xml, "TV");
					for (int i = 0; i < xmlTvTimes.size(); i++) {
						XmlTvTimesItem xmlTvItem = (XmlTvTimesItem) xmlTvTimes
								.elementAt(i);
						DataCentre itemObj = new DataCentre(
								xmlTvItem.programmeName, xmlTvItem.date,
								xmlTvItem.time, xmlTvItem.broadcaster,
								xmlTvItem.region);
						System.out.println("Added tv times : " + itemObj);
						MenuScreen.getInstance().tvTimesCollection
								.addElement(itemObj);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("aloy.downloadedTvTimes.exceptione: " + e);
			return;
		}
	}
	
	// download data for TV Schedule data
	private static final String tour_url = "http://203.116.81.61:9191/BlackBerry/BlackBerryWebService.asmx/ListSeasonCurrentSchedule";
	public static String tour_xml = "";

	public static void downloadTourSchedule() {
		System.out.println("enter downloadTourSchedule");
		try {
			Utility.getWebData(tour_url, new WebDataCallback() {
				public void callback(String data) {
					tour_xml = data;
					Vector xmlTourSchedule = parse(data, "Tour");
					for (int i = 0; i < xmlTourSchedule.size(); i++) {
						XmlTourScheduleItem xmlTourItem = (XmlTourScheduleItem) xmlTourSchedule
								.elementAt(i);
						DataCentre itemObj = new DataCentre(
								xmlTourItem.date, xmlTourItem.country, xmlTourItem.tourName, xmlTourItem.golfClub, xmlTourItem.defChamp, xmlTourItem.prizeMoney);
						System.out.println("Added tour schedules : " + itemObj);
						MenuScreen.getInstance().tourScheduleCollection
								.addElement(itemObj);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("aloy.downloadedTvTimes.exceptione: " + e);
			return;
		}
	}

	private static Vector parse(String data, String parseType) {
		Vector collection = null;
		if (parseType == "TV") {
			try {
				data = data.substring(data.indexOf("<ArrayOfAnyType"));
				System.out.println(data);
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlParser.parse(data, parseType);	
		} else if (parseType == "News") {
			try {
				data = data.substring(data.indexOf("<news>"));
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlNewsParser.parse(data);
		} else if (parseType == "Tour") {
			try {
				data = data.substring(data.indexOf("<ArrayOfAnyType"));
				System.out.println(data);
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlParser.parse(data, parseType);
		}
		return collection;
	}

}
