package com.og.app.asiantour;

import org.xml.sax.helpers.*;
import org.xml.sax.*;
import java.lang.StringBuffer;
import java.util.*;

public class DeleteFeedXMLHandler extends DefaultHandler
{
    StringBuffer sb = null;
    ADeleteObj item = null;
    boolean bStarted = false;
    Vector vadsbanner = new Vector();
    String lastbuilddate = null;
    
    public DeleteFeedXMLHandler (String lastbuilddate) {
        this.lastbuilddate = lastbuilddate;
    }

    public void warning(SAXParseException e) 
    {
        bStarted = false;
    }
    
    public void error(SAXParseException e) 
    {
    }
    
    public void fatalError(SAXParseException e) 
    {
        bStarted = false;
    }
    
    public void startDocument() throws SAXException
    {
        //System.out.println(System.currentTimeMillis()+">>startDocument");
    }
    public void endDocument() throws SAXException
    {

        // we've concluded this document, safe to create the feed.
    }
    public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException
    {
        sb = new StringBuffer("");
        if (localName.equals("item"))
        {
            bStarted = true;
            // new item, let's set up!
            item = new ADeleteObj();
            return;
        }
    }
    
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
        //System.out.println(System.currentTimeMillis()+">>endElement [" + localName + "] value = [" + sb.toString() + "]");
        if ( localName.equals("lastBuildDate")){
            if ( sb.toString().equals(lastbuilddate) ){
                //throw exception to skip
                //System.out.println("No news update");
               throw new SAXException();
            }
            lastbuilddate=sb.toString();
            return;
        }

        if (bStarted == false) return;
    
        if ( localName.equals("item")){
            vadsbanner.addElement(item);
            return;
        }
        
        if (localName.equals("pubDate")) {
            item.publishdate=sb.toString().trim();
            item.longpublishdate=com.og.app.util.Utility.getTimeInMillis(item.publishdate);
            return;
        }
        
        if (localName.equals("guid")){
            item.guid=sb.toString().trim();
            return;
        }
                
        sb = new StringBuffer("");
    }
    
    public void characters(char ch[], int start, int length)
    {
        String theString = new String(ch,start,length);
        sb.append(theString);
    }
    
    public String getLastBuildDate(){
        return lastbuilddate;
    }
    public Vector getDeleteList(){
            return vadsbanner;
    }
} 
