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

public class XmlParser {
	
	public static Vector parse(String strToParse, String parseType) {
		Vector xmlItemCollection = new Vector();
		byte[] xmlByteArray = strToParse.getBytes();
		ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlByteArray);
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		if (parseType == "TV") {
			// Vector<XmlTvTimesItem>
			try {
				DocumentBuilder docBuilder = docBuilderFactory
						.newDocumentBuilder();
				Document doc = docBuilder.parse(xmlStream);
				doc.getDocumentElement().normalize();
				NodeList list = doc.getElementsByTagName("*");
				String node = "";
				String element = "";

				XmlTvTimesItem xmlItem = new XmlTvTimesItem();
				for (int i = 0; i < list.getLength(); i++) {
					System.out.println(i);
					Node value = list.item(i).getChildNodes().item(0);
					node = list.item(i).getNodeName();

					if (value != null)
						element = value.getNodeValue();

					if (node.equals("anyType") && i != 1) {
						xmlItemCollection.addElement(xmlItem);
						xmlItem = new XmlTvTimesItem();
					} else if (node.equals("Country")) {
						xmlItem.region = element;
					} else if (node.equals("Region")) {
						xmlItem.region += " (" + element + ")";
					} else if (node.equals("Programme")) {
						xmlItem.programmeName = element;
					} else if (node.equals("Event")) {
						xmlItem.programmeName += " (" + element + ")";
					} else if (node.equals("Broadcaster")) {
						xmlItem.broadcaster = element;
					} else if (node.equals("Date")) {
						xmlItem.date = element;
					} else if (node.equals("From")) {
						if (!element.equals("TBC"))
							xmlItem.time = element + " - ";
						else
							xmlItem.time = element;
					} else if (node.equals("To")) {
						if (!xmlItem.equals("TBC"))
							xmlItem.time += element + "hrs";
					}
				}
				xmlItemCollection.addElement(xmlItem);

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
		} else if (parseType == "Tour") {
			// Vector<XmlTourScheduleItem>
			try {
				DocumentBuilder docBuilder = docBuilderFactory
						.newDocumentBuilder();
				Document doc = docBuilder.parse(xmlStream);
				doc.getDocumentElement().normalize();
				NodeList list = doc.getElementsByTagName("*");
				String node = "";
				String element = "";

				XmlTourScheduleItem xmlItem = new XmlTourScheduleItem();
				for (int i = 0; i < list.getLength(); i++) {
					System.out.println(i);
					Node value = list.item(i).getChildNodes().item(0);
					node = list.item(i).getNodeName();

					if (value != null)
						element = value.getNodeValue();

					if (node.equals("anyType") && i != 1) {
						xmlItemCollection.addElement(xmlItem);
						xmlItem = new XmlTourScheduleItem();
					} else if (node.equals("Name")) {
						xmlItem.tourName = element;
					} else if (node.equals("DisplayDate")) {
						xmlItem.date = element;
					} else if (node.equals("PrizeMoney")) {
						xmlItem.prizeMoney = element;
					} else if (node.equals("Venue")) {
						xmlItem.golfClub = element;
					} else if (node.equals("Country")) {
						xmlItem.country = element;
					} else if (node.equals("Winner")) {
						xmlItem.defChamp = element;
					} 
				}
				xmlItemCollection.addElement(xmlItem);

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
		}
		return xmlItemCollection;
	}
}
