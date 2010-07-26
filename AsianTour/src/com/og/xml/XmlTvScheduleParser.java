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
				System.out.println(i);
				Node value = list.item(i).getChildNodes().item(0);
				node = list.item(i).getNodeName();
				System.out.println("aloy.XmlTvScheduleParser.parse.getNodeName = " + node);
				System.out.println("aloy.XmlTvScheduleParser.parse.getChildNodes = " + value);
				if (value != null)
					element = value.getNodeValue();
			
				if(node.equals("anyType") && i!=1){
					xmlTvItemCollection.addElement(xmlItem);
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					xmlItem = new XmlTvTimesItem();
				}else if(node.equals("Country")){
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.region = element;
				} else if(node.equals("Region")){
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.region += " (" + element + ")";
				} else if (node.equals("TimeZone")) {
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
				} else if(node.equals("Programme")){
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.programmeName = element;
				}else if(node.equals("Event")){
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.programmeName += " (" + element + ")";
				}else if(node.equals("Broadcaster")){
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.broadcaster = element;
				}else if(node.equals("Date")){
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.date = element;
				}else if(node.equals("From")){
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.time = element + " - ";
				} else if (node.equals("To")) {
					System.out.println("aloy.XmlTvScheduleParser.parse node: " + node);
					System.out.println("aloy.XmlTvScheduleParser.parse element: " + element);
					xmlItem.time += element + "hrs";
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
