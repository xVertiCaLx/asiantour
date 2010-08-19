package com.og.xml;

import java.io.IOException;
import java.util.Vector;

import net.rim.device.api.i18n.DateFormat;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.MenuScreen;
import com.og.app.gui.NewsPanel;
import com.og.app.util.DataCentre;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;

public class XmlHelper {

	final static long now = System.currentTimeMillis();
	// date formatter
	final static DateFormat dateFormat = DateFormat
			.getInstance(DateFormat.DATETIME_DEFAULT);// .DATETIME_DEFAULT);
	// format date as a string
	final static String formattedDate = dateFormat.formatLocal(now).substring(
			8, 12);

	// Download news
	private static final String url = "http://203.116.88.166:9191/BlackBerry/BlackBerryWebService.asmx/ListNews";
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
									.insertElementAt(aNewsItemObj, 0);
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
	// private static Vector parseDownloadedNews(String data) {
	// Vector newsCollection = null;
	// try {
	// data = data.substring(data.indexOf("<news>"));
	// } catch (Exception e) {
	// return new Vector();
	// }
	// newsCollection = XmlNewsParser.parse(data);
	// return newsCollection;
	// }

	private static final String country_url = "http://203.116.88.166:9191/BlackBerry/BlackBerryWebService.asmx/ListTVTimesCountries";
	public static String country_xml = "";

	public static void downloadCountry() {
		System.out.println("enter downloadTourSchedule");
		try {
			Utility.getWebData(country_url, new WebDataCallback() {
				public void callback(byte[] data) {
					country_xml = new String(data);
					Vector xmlCountry = parse(country_xml, "Country");
					for (int i = 0; i < xmlCountry.size(); i++) {
						XmlCountryItem xmlCountryItem = (XmlCountryItem) xmlCountry
								.elementAt(i);
						DataCentre itemObj = new DataCentre(xmlCountryItem.country);
						// System.out.println("Added tour schedules : " +
						// itemObj);
						MenuScreen.getInstance().countryCollection
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
	private static final String tvTimes_url = "http://203.116.88.166:9191/BlackBerry/BlackBerryWebService.asmx/ListTVTimesViaCountry?Country=";
	public static String tvTimes_xml = "";

	public static void downloadTvTimes(String country) {
		System.out.println("enter downloadTvTimes");
		try {
			Utility.getWebData(tvTimes_url+country+"$", new WebDataCallback() {
				public void callback(byte[] data) {
					tvTimes_xml = new String(data);
					Vector xmlTvTimes = parse(tvTimes_xml, "TV");
					for (int i = 0; i < xmlTvTimes.size(); i++) {
						XmlTvTimesItem xmlTvItem = (XmlTvTimesItem) xmlTvTimes
								.elementAt(i);
						DataCentre itemObj = new DataCentre(xmlTvItem.index,
								xmlTvItem.programmeName, xmlTvItem.date,
								xmlTvItem.time, xmlTvItem.broadcaster,
								xmlTvItem.region);
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
		
		MenuScreen.getInstance().repainteverything();
		MenuScreen.getInstance().initSchedulePkg(2);
		MenuScreen.getInstance().addPanels("loaded");
	}

	// download data for TV Schedule data
	private static final String tour_url = "http://203.116.88.166:9191/BlackBerry/BlackBerryWebService.asmx/ListSeasonCurrentSchedule";
	public static String tour_xml = "";

	public static void downloadTourSchedule() {
		System.out.println("enter downloadTourSchedule");
		try {
			Utility.getWebData(tour_url, new WebDataCallback() {
				public void callback(byte[] data) {
					tour_xml = new String(data);
					Vector xmlTourSchedule = parse(tour_xml, "Tour");
					for (int i = 0; i < xmlTourSchedule.size(); i++) {
						XmlTourScheduleItem xmlTourItem = (XmlTourScheduleItem) xmlTourSchedule
								.elementAt(i);
						DataCentre itemObj = new DataCentre(xmlTourItem.index,
								xmlTourItem.date, xmlTourItem.country,
								xmlTourItem.tourName, xmlTourItem.golfClub,
								xmlTourItem.defChamp, xmlTourItem.prizeMoney);
						// System.out.println("Added tour schedules : " +
						// itemObj);
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

	private static final String oom_url = "http://203.116.88.166:9191/BlackBerry/BlackBerryWebService.asmx/ListOOM?Year=2010";// + formattedDate;
	public static String oom_xml = "";

	public static void downloadOOM() {
		System.out.println("enter downloadOOM");
		System.out
				.println("----------------------------------------------------==");
		System.out.println("this year: full?" + formattedDate);
		System.out
				.println("----------------------------------------------------==");
		try {
			Utility.getWebData(oom_url, new WebDataCallback() {
				public void callback(byte[] data) {
					oom_xml = new String(data);
					Vector xmlOOM = parse(oom_xml, "Merit");
					for (int i = 0; i < xmlOOM.size(); i++) {
						XmlMeritItem xmlMeritItem = (XmlMeritItem) xmlOOM
								.elementAt(i);
						DataCentre itemObj = new DataCentre(xmlMeritItem.index,
								xmlMeritItem.playerName, xmlMeritItem.pos,
								xmlMeritItem.earnings);
						// System.out.println("Added tour schedules : " +
						// itemObj);
						MenuScreen.getInstance().meritCollection
								.addElement(itemObj);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("aloy.downloadedOOM.exceptione: " + e);
			return;
		}
	}

	private static Vector parse(String data, String parseType) {
		Vector collection = null;
		if (parseType == "TV") {
			try {
				data = data.substring(data.indexOf("<ArrayOfAnyType"));
				// System.out.println(data);
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlParser.parse(data, parseType);
		} else if (parseType == "Tour") {
			try {
				data = data.substring(data.indexOf("<ArrayOfAnyType"));
				// System.out.println(data);
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlParser.parse(data, parseType);
		} else if (parseType == "Merit") {
			try {
				data = data.substring(data.indexOf("<ArrayOfAnyType"));
				// System.out.println(data);
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlParser.parse(data, parseType);
		} else if (parseType == "News") {
			try {
				data = data.substring(data.indexOf("<ArrayOfAnyType"));
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlNewsParser.parse(data);
		} else if (parseType == "Country") {
			try {
				data = data.substring(data.indexOf("<ArrayOfAnyType"));
				// System.out.println(data);
			} catch (Exception e) {
				return new Vector();
			}
			collection = XmlParser.parse(data, parseType);
		}
		return collection;
	}

}
