package com.og.app.asiantour;

import com.og.app.util.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;
import java.lang.StringBuffer;
import java.util.*;

public class IndexFeedXMLHandler extends DefaultHandler
{
    StringBuffer sb = null;
    ACategoryObj item = null;
    boolean bStarted = false;
    Vector tmpstorage = null;
    String lastbuilddate = null;
    
    public IndexFeedXMLHandler(String lastbuilddate) {
        this.lastbuilddate = lastbuilddate;
        tmpstorage = new Vector(); 
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
        //System.out.println(System.currentTimeMillis()+">>startDocument");
    }
    public void endDocument() throws SAXException
    {

        // we've concluded this document, safe to create the feed.
    }
    public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException
    {
        //System.out.println(System.currentTimeMillis()+">>startElement [" + localName + "] Attributes [" + atts.toString() + "]");
        sb = new StringBuffer("");
        if (localName.equals("item"))
        {
            bStarted = true;
            // new item, let's set up!
            item = new ACategoryObj();
            return;
        }
    
        if (localName.equals("enclosure"))
        {
            if ( atts.getLength()>0 ){ 
                for ( int i=0; i<atts.getLength(); i++ ){
                    if ( atts.getLocalName(i).equals("url") ){
                         item.iconurl=atts.getValue(i);
                         return;
                    }
                }//end for
            }
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
            if ( item.category.equalsIgnoreCase("list") )               
                tmpstorage.addElement(item);
            return;
        }
        
        if (localName.equals("title")) {
            item.title=sb.toString().trim();
            return;
        }
        
        if (localName.equals("link")){
            item.link=Utility.turnOffURLSpecialChar(sb.toString().trim());
            return;
        }

        if (localName.equals("description")){
            item.description=sb.toString().trim();
            return;
        }

        if (localName.equals("category")){
            item.category=sb.toString().trim();
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
    public Vector getCategoryList(){
            return tmpstorage;
    }
} 
