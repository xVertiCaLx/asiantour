package com.og.app.asiantour;

import org.xml.sax.helpers.*;
import org.xml.sax.*;
import java.lang.StringBuffer;
import java.util.*;

public class StatusFeedXMLHandler extends DefaultHandler
{
    StringBuffer sb = null;
    StatusObj statusobj = new StatusObj();
    boolean bStarted = false;
    String item_type = "";
    String item_title = "";
    String item_lastBuildDate = "";
    
    public StatusFeedXMLHandler () {
    }

    public void warning(SAXParseException e) 
    {
        System.err.println("warning: " + e.getMessage());
        
        bStarted = false;
    }
    
    public void error(SAXParseException e) 
    {
        System.err.println("error: " + e.getMessage());
        
    }
    
    public void fatalError(SAXParseException e) 
    {
        System.err.println("fatalError: " + e.getMessage());
        
        bStarted = false;
    }
    
    public void startDocument() throws SAXException
    {
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
            return;
        }
    }
    
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
        //System.out.println(System.currentTimeMillis()+">>endElement [" + localName + "] value = [" + sb.toString() + "]");
        if ( bStarted==false && localName.equals("lastBuildDate")){
            return;
        }
        if ( localName.equals("serverDateTime")){
            statusobj.serverDateTime=sb.toString();
            return;
        }
        if ( localName.equals("typeB")){
            statusobj.version_bold=sb.toString();
            return;
        }
        if ( localName.equals("typeG")){
            statusobj.version_gemini=sb.toString();
            return;
        }
        if ( localName.equals("typeS")){
            statusobj.version_storm=sb.toString();
            return;
        }

        if (bStarted == false) return;
     
        if ( localName.equals("item")){
            if ( item_type.equalsIgnoreCase("news") ){
                //System.out.println("type:news:statusobj.put:title("+item_title+"), lastbuilddate("+item_lastBuildDate+")");
                statusobj.categorylist.put(item_title, item_lastBuildDate);
            }else{
                //System.out.println("type:others:statusobj.put:title("+item_title+"), lastbuilddate("+item_lastBuildDate+")");
                if ( item_title.equalsIgnoreCase("index") ){
                    statusobj.lastBuildDate_index=item_lastBuildDate;
                }else if ( item_title.equalsIgnoreCase("weather") ){
                    statusobj.lastBuildDate_weather=item_lastBuildDate;
                }else if ( item_title.equalsIgnoreCase("ads_bb") ){
                    statusobj.lastBuildDate_ads=item_lastBuildDate;
                }else if ( item_title.equalsIgnoreCase("photos") ){
                    statusobj.lastBuildDate_photos=item_lastBuildDate;
                }
            }
            return;
        }
        
        if (localName.equals("lastBuildDate")) {
            item_lastBuildDate=sb.toString().trim();
            return;
        }
        if (localName.equals("title")){
            item_title=sb.toString().trim();
            return;
        }        
        if (localName.equals("type")){
            item_type=sb.toString().trim();
            return;
        }
                
        sb = new StringBuffer("");
    }
    
    public void characters(char ch[], int start, int length)
    {
        String theString = new String(ch,start,length);
        sb.append(theString);
    }

    public StatusObj getStatusObj(){
            return statusobj;
    }
} 
