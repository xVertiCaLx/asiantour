package com.og.rss;

import com.og.app.util.*;

import net.rim.device.api.system.*;
import java.io.*;
import java.lang.Thread;
import java.util.Vector;

import javax.microedition.io.HttpConnection;

import org.xml.sax.helpers.*;

public class RssManager implements Runnable
{

    private RssListener rsslistener = null;
    private ANewsFeed rssfeed=null;
 
    private static RssManager instance = null;
    private long dbid = ARssDB.DB_ID;
    Vector feedlist = new Vector();
    Vector listenerlist = new Vector();
    private Object lock = new Object();
    
    byte TELCO = 0;
    final byte STARHUB = 1;
    final byte SINGTEL = 2;
    final byte M1 = 3;

    private RssManager(){
        if(GPRSInfo.getHomeMCC() == 1317)
        {
                switch(GPRSInfo.getHomeMNC())
                {
                        case 1:
                        case 2:
                                TELCO = SINGTEL;
                                break;
                        case 3:
                                TELCO = M1;
                                break;
                        case 5:
                                TELCO = STARHUB;
                                break;
                }
        }           
        Thread thread = new Thread(this);
        thread.start();        
    }
    
    public synchronized static RssManager getInstance(){
           if( instance==null ){
                instance = new RssManager();
            }
            return instance;
    }
    public synchronized boolean doUpdate(ANewsFeed rssfeed, RssListener rsslistener){
        if ( rsslistener!=null ){
             synchronized(lock){
                //System.out.println("RssManager:doUpdate:"+rssfeed.id);
                feedlist.addElement(rssfeed);
                listenerlist.addElement(rsslistener);
                lock.notifyAll();
                return true;
            }
        }
        return false;
    }
    
    public void run()
    {
        while(true){
            synchronized(lock){
                if ( listenerlist.size()==0 ){
                    try{
                        lock.wait();
                    }catch(Exception e){
                    }
                    continue;
                }
            }
            rssfeed = (ANewsFeed)feedlist.elementAt(0);
            feedlist.removeElementAt(0);
            rsslistener = (RssListener)listenerlist.elementAt(0);
            listenerlist.removeElementAt(0);
            
            RssStatus rssstatus = null;
            //System.out.println("RssManager:run:"+rssfeed.id);
            try{
                rssstatus = processRssFeed(rssfeed);
                //System.out.println("ERRORCODE:"+rssstatus.errorcode);
                if ( rssstatus.errorcode!=0 ){
                }
            }catch (Exception e){
                //System.err.println("eree:General Error " + e.getMessage());
                e.printStackTrace();
            }
            rsslistener.updateStatus(rssstatus);
        }
    }
    
    public RssStatus processRssFeed(ANewsFeed afeed)
    {
        //System.out.println("processRssFeed:"+afeed.id);
        InputStream inputStream = null;
        HttpConnection httpConnection = null;
        RssStatus rssstatus = new RssStatus(afeed.id, 0,"");
        try
        {
                if ( afeed.feedType==ANewsFeed.FEEDTYPE_NEWS ){
                        RssXMLHandler myHandler= new RssXMLHandler(rsslistener,afeed);
                        myHandler = (RssXMLHandler)(ConnectionMgr.requestFeed(afeed.url, myHandler));
                }/*else{
                        PhotoXMLHandler myHandler= new PhotoXMLHandler(rsslistener,afeed);
                        myHandler = (PhotoXMLHandler)(ConnectionMgr.requestFeed(afeed.url, (DefaultHandler)myHandler));                    
                }*/
        }catch (FeedException e){
            switch(e.getErrorCode()){
                case FeedException.ERRORCODE_SAMELASTBUILDDATE:
                   break;             
                case FeedException.ERRORCODE_INVALIDRESPONSECODE:
                    rssstatus.errordesc="Invalid response code";                   
                    rssstatus.errorcode= -1;
                    break;
                case FeedException.ERRORCODE_IOEXCEPTION:
                    rssstatus.errorcode= -2;        
                    rssstatus.errordesc="IO Exception";                         
                    break;
                case FeedException.ERRORCODE_SAXEXCEPTION:
                    rssstatus.errorcode= -3;     
                    rssstatus.errordesc="SAX Exception";                      
                    break;       
                case FeedException.ERRORCODE_EXCEPTION:
                    rssstatus.errorcode= -4;        
                    rssstatus.errordesc="Exception";                    
                    break;
                case FeedException.ERRORCODE_UNKNOWNEXCEPTION:
                    rssstatus.errorcode= -5;       
                    rssstatus.errordesc="Unknown Exception";
                    break;                                                                     
            }
        }        

        return rssstatus;
    }    

} 
