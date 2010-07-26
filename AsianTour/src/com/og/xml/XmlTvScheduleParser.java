package com.og.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.Vector;

import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;
import net.rim.device.api.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlTvScheduleParser {
	//Vector<XmlNewsItem>
	public static Vector parse(String strToParse){
		byte[] xmlByteArray = strToParse.getBytes();
		ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory. newInstance(); 
		Vector xmlTvItemCollection = new Vector();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlStream);
			doc.getDocumentElement().normalize();
			NodeList list = doc.getElementsByTagName("*");
			String node = "";
			String element = "";
			
			XmlTvTimesItem xmlItem = new XmlTvTimesItem();
			for(int i=0; i<list.getLength(); i++){
				Node value = list.item(i).getChildNodes().item(0);
				node = list.item(i).getNodeName();
				element = value.getNodeValue();
			
				if(node.equals("AnyType") && i!=1){
					xmlTvItemCollection.addElement(xmlItem);
					xmlItem = new XmlTvTimesItem();
				}else if(node.equals("Country")){
					xmlItem.region = element;
				}else if(node.equals("Region")){
					xmlItem.region += " (" + element + ")";
				}else if(node.equals("Programme")){
					xmlItem.programmeName = element;
				}else if(node.equals("Event")){
					xmlItem.programmeName += " (" + element + ")";
				}else if(node.equals("Broadcaster")){
					xmlItem.broadcaster = element;
				}else if(node.equals("Date")){
					xmlItem.date = element;
				}else if(node.equals("From")){
					xmlItem.time = element + "HRS - ";
				} else if (node.equals("To")) {
					xmlItem.time += element + "HRS";
				}
				xmlTvItemCollection.addElement(xmlItem);
			}
			xmlTvItemCollection.addElement(xmlItem);
			
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

		return xmlTvItemCollection;
	}
}
