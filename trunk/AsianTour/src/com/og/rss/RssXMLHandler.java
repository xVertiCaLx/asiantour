package com.og.rss;

import org.xml.sax.helpers.*;
import org.xml.sax.*;

import java.lang.StringBuffer;
import java.util.*;

class RssXMLHandler extends DefaultHandler
{
    StringBuffer sb = null;
    ANewsItemObj item = null;
    boolean bStarted = false;
    
    ARssDB rssdb = null;
    private int key;
    ANewsFeed feed = null;
    RssListener listener = null;
    Vector tmpstorage = null;
    
    RssXMLHandler(RssListener listener, ANewsFeed feed) {
        this.listener = listener;
        this.feed =feed ;
        this.key=feed.id;
        tmpstorage = new Vector();
        try{
            rssdb = ARssDB.getInstance();
        }catch(Exception e){
            e.printStackTrace();
        }        
    }

    public void warning(SAXParseException e) 
    {
        //System.err.println("warning: " + e.getMessage());
        //bStarted = false;
    }
    
    public void error(SAXParseException e) 
    {
        //System.err.println("error: " + e.getMessage());
    }
    
    public void fatalError(SAXParseException e) 
    {
        //System.err.println("fatalError: " + e.getMessage());
        //bStarted = false;
    }
    
    public void startDocument() throws SAXException
    {
        //System.out.println(System.currentTimeMillis()+">>startDocument");
    }
    public void endDocument() throws SAXException
    {
           try{
                rssdb.addNewsItem(key, tmpstorage, listener); 
            }catch(Exception e){
                //System.out.println("endDocument:Failed to save into database");
                 throw new SAXException("Database error");
            }
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
            item = new ANewsItemObj();
            item.feedid=feed.id;
            return;
        }
        
        if (localName.equals("thumbnail"))
        {
            if ( atts.getLength()>0 ){ 
                for ( int i=0; i<atts.getLength(); i++ ){
                    if ( atts.getLocalName(i).equals("url") ){
                         item.thumbnailurl=atts.getValue(i);
                         return;
                    }
                }//end for
            }
        }
        if (localName.equals("content")&& item.imageurl.equals(""))
        {
            if ( atts.getLength()>0 ){ 
                for ( int i=0; i<atts.getLength(); i++ ){
                    if ( atts.getLocalName(i).equals("url") ){
                         item.imageurl=atts.getValue(i);
                    }else if ( atts.getLocalName(i).equals("height") ){
                        if ( atts.getValue(i)!=null && !atts.getValue(i).equals("") ){
                            item.imageheight=Integer.parseInt(atts.getValue(i));
                        }
                    }else if ( atts.getLocalName(i).equals("width") ){
                        if ( atts.getValue(i)!=null && !atts.getValue(i).equals("") ){                          
                            item.imagewidth=Integer.parseInt(atts.getValue(i));
                        }
                    }                          
                }//end for
            }
        }
    }
    
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException
    {
        //System.out.println(System.currentTimeMillis()+">>endElement [" + localName + "] :"+sb.toString().trim());

        if ( localName.equals("lastBuildDate")){
            if ( sb.toString().equals(feed.lastBuildDate) ){
                //throw exception to skip
                //System.out.println("No news update");
               //throw new SAXException();
            }
            feed.lastBuildDate=sb.toString();
            listener.updateRssFeedLastBuildDate(feed.id, sb.toString());
            return;
        }

        if (bStarted == false) return;

        if ( localName.equals("item")){
            if ( item.status!=null && item.status.equalsIgnoreCase("Delete") ){
                //System.out.println("DELETE news.......:"+item.title);
                listener.addDeleteItem(item.guid, item.pubDate);
                return;
            }
            
            if ( rssdb.isNewsItemExists(key, item) ){
                //save will be done on endDocument() method
                return;
                /*
                try{
                    rssdb.addNewsItem(key, tmpstorage, listener); 
                }catch(Exception e){
                     //System.out.println("Failed to save into database");
                      throw new SAXException("Database error");
                }
                //throw exception to skip
                throw new SAXException();
                */
            }

            tmpstorage.addElement(item);
            return;
        }
        if (localName.equals("caption")) {
            item.caption=sb.toString().trim();
            return;
        }              
        if (localName.equals("author")) {
            item.author=sb.toString().trim();
            return;
        }        
        if (localName.equals("title")) {
            item.title=sb.toString().trim();
            return;
        }
        
        if (localName.equals("link")){
            item.link=sb.toString().trim();
            return;
        }

        if (localName.equals("description")){
            if ( item.description==null || item.description.equals("") ){
                item.description=sb.toString().trim();
            }
            return;
        }

        if (localName.equals("pubDate")){
            item.pubDate=sb.toString().trim();
            item.longpubDate=com.og.app.util.Utility.getTimeInMillis(item.pubDate);            
            return;
        }                        
        
        if (localName.equals("guid")){
            item.guid=sb.toString().trim();
            return;            
        }       
        
        if ( localName.equals("status")){
            //System.out.println("status:"+sb.toString().trim());        
            item.status = sb.toString().trim();
            return;                
        }
                
        sb = new StringBuffer("");
    }
    
    public void characters(char ch[], int start, int length)
    {
        String theString = new String(ch,start,length);
        sb.append(theString);
    }
    
} 
