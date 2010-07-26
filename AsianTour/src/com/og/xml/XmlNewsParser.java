package com.og.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import net.rim.device.api.util.StringUtilities;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;
import net.rim.device.api.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlNewsParser {
	//Vector<XmlNewsItem>
	public static Vector parse(String strToParse){
		byte[] xmlByteArray = strToParse.getBytes();
		ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory. newInstance(); 
		Vector xmlNewsItemCollection = new Vector();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlStream);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("*");
			String node = "";
			String element = "";
			XmlNewsItem xmlNewsItem = new XmlNewsItem();
			
			for(int i=0; i<list.getLength(); i++){
				Node value = list.item(i).getChildNodes().item(0);
				node = list.item(i).getNodeName();
				element = value.getNodeValue();
			
				if(node.equals("item") && i!=1){
					xmlNewsItemCollection.addElement(xmlNewsItem);
					xmlNewsItem = new XmlNewsItem();
				}else if(node.equals("link")){
					String id = element.substring("http://www.asiantour.com/news.aspx?sid=".length());
					xmlNewsItem.id = id;
				}else if(node.equals("title")){
					xmlNewsItem.title = element;
				}else if(node.equals("description")){
					xmlNewsItem.description = element;
				}else if(node.equals("thumbnail")){
					xmlNewsItem.thumbnailURL = element;
				}else if(node.equals("image")){
					xmlNewsItem.imageURL = element;
				}else if(node.equals("pubDate")){
					String dateString = element;
					String gmt = "GMT" + dateString.substring(dateString.indexOf(" +"));
					Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(gmt));
					String [] dateWords = StringUtilities.stringToWords(dateString);
					String[] MONTHS = { "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec" };
					int month = 0;
					for(int j=0; j<MONTHS.length;j++){
						if(MONTHS[j].equals(dateWords[2])){
							month = j;
							break;
						}
					}
					calendar.set(Calendar.DATE, Integer.parseInt(dateWords[1]));
					calendar.set(Calendar.MONTH, month);
					calendar.set(Calendar.YEAR, Integer.parseInt(dateWords[3]));
					calendar.set(Calendar.HOUR, Integer.parseInt(dateWords[4]));
					calendar.set(Calendar.MINUTE, Integer.parseInt(dateWords[5]));
					calendar.set(Calendar.SECOND, Integer.parseInt(dateWords[6]));
					xmlNewsItem.pubDate = calendar;
				}else if(node.equals("author")){
					xmlNewsItem.author = element;
				}
//				xmlNewsItemCollection.addElement(xmlNewsItem);
			}
			xmlNewsItemCollection.addElement(xmlNewsItem);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xmlNewsItemCollection;
	}
}
