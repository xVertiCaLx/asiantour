package com.og.app.asiantour;

import com.og.rss.*;
import com.og.app.*;
import com.og.app.util.*;

import net.rim.device.api.xml.parsers.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.system.*;
import net.rim.device.api.servicebook.*;

import java.lang.Thread;
import java.util.*;
import java.util.Vector;
import java.io.*;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import javax.microedition.rms.*;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class FeedReader
{

  public synchronized static StatusObj loadStatus(String url)
    {
        try {
                StatusFeedXMLHandler myHandler = new StatusFeedXMLHandler  ();
                myHandler = (StatusFeedXMLHandler)(ConnectionMgr.requestFeed(url, (DefaultHandler)myHandler));
                return myHandler.getStatusObj();
        }catch (Exception e){                                                                     
        }
        return null;
    }    
        
    public synchronized static boolean saveDeleteList(ADeleteList obj){
        try{
                long currentdatetime = Utility.getCurrentTimeInMillis();
                ARssDB rssdb=ARssDB.getInstance();
                if ( obj.deletelist!=null ){
                    Vector v = new Vector();
                    for ( int i=0; i<obj.deletelist.length; i++ ){
                        if ( obj.deletelist[i].longpublishdate+(Const.DEFAULT_DELETEITEMEXPIREDDAYS*24*60*60*1000)>=currentdatetime ){
                                v.addElement(obj.deletelist[i]);
                        }else{
                            //System.out.println("remove item from delete db:"+ obj.deletelist[i].guid+":"+ obj.deletelist[i].publishdate);
                        }
                    }
                    if ( v.size()==0 )
                        obj.deletelist=null;
                    else{
                        obj.deletelist= new ADeleteObj[v.size()];
                        for ( int i=0; i<v.size(); i++ )                            
                            obj.deletelist[i] = (ADeleteObj)v.elementAt(i);
                    }
                }
                rssdb.setObjectPool(Const.ID_DELETE, obj);
                return true;
        }catch(Exception e){

            e.printStackTrace();
        }
        return false;
    }      
    public synchronized static ADeleteList loadDeleteList(){
        try{
            ARssDB rssdb=ARssDB.getInstance();
            Object obj = rssdb.getObjectPool(Const.ID_DELETE);
            if ( obj!=null && obj instanceof ADeleteList){
                ADeleteList categorylist = (ADeleteList)obj;
                return categorylist;
            }
        }catch(Exception e){

            e.printStackTrace();
        }
        return null;
    }     
    public static int reloadDeleteFeed(String url, String statuslastBuildDate)
    {
        try {
            ADeleteList deletelist = loadDeleteList();
            String lastbuilddate = "";
            if ( deletelist!=null )
                lastbuilddate  = deletelist.lastbuilddate;

            if ( deletelist==null || !statuslastBuildDate.equalsIgnoreCase(lastbuilddate) ){
                                        
                DeleteFeedXMLHandler  myHandler= new DeleteFeedXMLHandler (lastbuilddate);
                myHandler = (DeleteFeedXMLHandler )(ConnectionMgr.requestFeed(url, (DefaultHandler)myHandler));

                Vector v = myHandler.getDeleteList();
                ADeleteList list = new ADeleteList ();
                list.lastbuilddate=myHandler.lastbuilddate;
                
                if ( v.size()>0 ){
                    ADeleteObj[] listarr = new ADeleteObj[v.size()];
                    for ( int i=0; i<v.size(); i++ ){
                        listarr [i] = (ADeleteObj)v.elementAt(i);
                    }
                    list.deletelist =listarr;
                }else
                        list.deletelist = null;
                        
                if ( saveDeleteList(list)==true ){
                    return 1;
                }
            }
        }catch (FeedException e){
            switch(e.getErrorCode()){
                case FeedException.ERRORCODE_SAMELASTBUILDDATE:
                    return 0;
                case FeedException.ERRORCODE_INVALIDRESPONSECODE:
                    return -1;
                case FeedException.ERRORCODE_IOEXCEPTION:
                    return -2;          
                case FeedException.ERRORCODE_SAXEXCEPTION:
                    return -3;            
                case FeedException.ERRORCODE_EXCEPTION:
                    return -4;        
                case FeedException.ERRORCODE_UNKNOWNEXCEPTION:
                    return -5;                                                                            
            }
        }
        return 0;
    }    
        
  /*public synchronized static boolean saveAdsList(AdsBannerList obj){
        try{
                ARssDB rssdb=ARssDB.getInstance();
                rssdb.setObjectPool(Const.ID_ADS, obj);
                return true;
        }catch(Exception e){

            e.printStackTrace();
        }
        return false;
    }      
    public synchronized static AdsBannerList loadAdsBannerList(){
        try{
            ARssDB rssdb=ARssDB.getInstance();
            Object obj = rssdb.getObjectPool(Const.ID_ADS);
            if ( obj!=null && obj instanceof AdsBannerList ){
                AdsBannerList categorylist = (AdsBannerList)obj;
                return categorylist;
            }
        }catch(Exception e){

            e.printStackTrace();
        }
        return null;
    }     
    public static int reloadAdsBannerFeed(String url, String statuslastbuilddate)
    {
        try {
            AdsBannerList adsbannerlist = loadAdsBannerList();
            String lastbuilddate = "";
            if ( adsbannerlist!=null )
                lastbuilddate  = adsbannerlist.lastbuilddate;
                        
            //System.out.println("reloadAdsBannerFeed:"+statuslastbuilddate+":"+lastbuilddate);
            
            if ( adsbannerlist==null || !statuslastbuilddate.equalsIgnoreCase(lastbuilddate) ){
                            
                AdsBannerXMLHandler myHandler= new AdsBannerXMLHandler (lastbuilddate);
                myHandler = (AdsBannerXMLHandler)(ConnectionMgr.requestFeed(url, (DefaultHandler)myHandler));

                Vector v = myHandler.getAdsBannerList();
                AdsBannerList list = new AdsBannerList();
                list.lastbuilddate=myHandler.lastbuilddate;
                if ( v.size()>0 ){
                    AdsBannerObj[] adsbannerarr = new AdsBannerObj[v.size()];
                    for ( int i=0; i<v.size(); i++ ){
                        adsbannerarr[i] = (AdsBannerObj)v.elementAt(i);
                    }
                    list.adsbannerlist =adsbannerarr;     
                }
                
                
                Vector v2 = myHandler.getOpeningAdsBannerList();
                if ( v2.size()>0 ){
                    AdsBannerObj[] openingadsbannerarr = new AdsBannerObj[v2.size()];
                    for ( int i=0; i<v2.size(); i++ ){
                        openingadsbannerarr[i] = (AdsBannerObj)v2.elementAt(i);
                    }
                    list.openingadsbannerlist =openingadsbannerarr; 
                }                           
                if ( saveAdsList(list)==true ){
                    return 1;
                }
            }
        }catch (FeedException e){
            switch(e.getErrorCode()){
                case FeedException.ERRORCODE_SAMELASTBUILDDATE:
                    return 0;
                case FeedException.ERRORCODE_INVALIDRESPONSECODE:
                    return -1;
                case FeedException.ERRORCODE_IOEXCEPTION:
                    return -2;          
                case FeedException.ERRORCODE_SAXEXCEPTION:
                    return -3;            
                case FeedException.ERRORCODE_EXCEPTION:
                    return -4;        
                case FeedException.ERRORCODE_UNKNOWNEXCEPTION:
                    return -5;                                                                            
            }
        }
        return 0;
    }    
       
      
    public synchronized static boolean saveWeatherObj(WeatherObj obj){
        ARssDB rssdb=null;        
        try{
                rssdb=ARssDB.getInstance();
                rssdb.setObjectPool(Const.ID_WEATHER, obj);
                return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }      
    public synchronized static WeatherObj loadWeatherObj(){
        ARssDB rssdb=null;
        try{
            rssdb=ARssDB.getInstance();
            Object obj = rssdb.getObjectPool(Const.ID_WEATHER);
            if ( obj!=null && obj instanceof WeatherObj ){
                WeatherObj categorylist = (WeatherObj)obj;
                return categorylist;
            }
        }catch(Exception e){
            
        }
        return null;
    }     */
    /*
    public static int reloadWeatherFeed(String url, String statuslastbuilddate)
    {
        try {
                WeatherObj weatherobj = loadWeatherObj();
                String lastbuilddate = "";
                if ( weatherobj!=null )
                    lastbuilddate  = weatherobj.lastbuilddate;
 
                System.out.println("reloadWeatherFeed:"+statuslastbuilddate+":"+lastbuilddate);
                
                if ( weatherobj==null || !statuslastbuilddate.equalsIgnoreCase(lastbuilddate) ){
                    WeatherFeedXMLHandler myHandler= new WeatherFeedXMLHandler (lastbuilddate);
                    myHandler = (WeatherFeedXMLHandler)(ConnectionMgr.requestFeed(url, (DefaultHandler)myHandler));
                    if ( saveWeatherObj(myHandler.getWeatherObj())==true ){
                        return 1;
                    }
                }
        }catch (FeedException e){
            switch(e.getErrorCode()){
                case FeedException.ERRORCODE_SAMELASTBUILDDATE:
                    return 0;
                case FeedException.ERRORCODE_INVALIDRESPONSECODE:
                    return -1;
                case FeedException.ERRORCODE_IOEXCEPTION:
                    return -2;          
                case FeedException.ERRORCODE_SAXEXCEPTION:
                    return -3;            
                case FeedException.ERRORCODE_EXCEPTION:
                    return -4;        
                case FeedException.ERRORCODE_UNKNOWNEXCEPTION:
                    return -5;                                                                            
            }
        }catch(Exception e){
        }
        return 0;
    }  */  
       
    public synchronized static ACategoryList loadCategoryList(){
        ARssDB rssdb=null;
        try{
            rssdb=ARssDB.getInstance();
            Object obj = rssdb.getObjectPool(Const.ID_CATEGORY);
            if ( obj!=null && obj instanceof ACategoryList ){
                ACategoryList categorylist = (ACategoryList)obj;
                return categorylist;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
          
    public static int reloadIndexFeed(String url, String statuslastBuildDate)
    {
        try {
                //System.out.println("reloadIndexFeed:statusobj:lastbuilddate:"+statuslastBuildDate);
                ACategoryList categorylist = loadCategoryList();
                String lastbuilddate = "";
                if ( categorylist!=null )
                    lastbuilddate  = categorylist.lastbuilddate;

                //System.out.println("reloadIndexFeed:categorylist:lastbuilddate:"+lastbuilddate);
                    
                if ( categorylist==null || !statuslastBuildDate.equalsIgnoreCase(lastbuilddate) ){
                    // connect to feed's URL
                    IndexFeedXMLHandler myHandler= new IndexFeedXMLHandler (lastbuilddate);
                    myHandler = (IndexFeedXMLHandler)(ConnectionMgr.requestFeed(url, (DefaultHandler)myHandler));
                    
                    if ( saveCategoryList(myHandler.getCategoryList(), myHandler.getLastBuildDate())==true ){
                        return 1;
                    }
                }//else
                    //System.out.println("index feed no update");
        }catch (FeedException e){
            switch(e.getErrorCode()){
                case FeedException.ERRORCODE_SAMELASTBUILDDATE:
                    return 0;
                case FeedException.ERRORCODE_INVALIDRESPONSECODE:
                    return -1;
                case FeedException.ERRORCODE_IOEXCEPTION:
                    return -2;          
                case FeedException.ERRORCODE_SAXEXCEPTION:
                    return -3;            
                case FeedException.ERRORCODE_EXCEPTION:
                    return -4;        
                case FeedException.ERRORCODE_UNKNOWNEXCEPTION:
                    return -5;           
                default:
                    return e.getErrorCode();
            }
        }
        return 0;
    }    

    public synchronized static boolean saveCategoryList(Vector vcategory, String lastbuilddate){
        ARssDB rssdb=null;        
        try{
            if ( vcategory.size()>0 ){
                rssdb=ARssDB.getInstance();
                Object obj = rssdb.getObjectPool(Const.ID_CATEGORY);
                int maxid = 1;                
                Vector newcategory = new Vector();
                
                if ( obj!=null ){
                    if ( obj instanceof ACategoryList ){
                        ACategoryList list = (ACategoryList)obj;
                        //get max id
                        for ( int z=0; z<list.categorylist.length; z++ ){
                            if ( list.categorylist[z].id>maxid )
                                maxid=list.categorylist[z].id;
                        }
    
                        //check existing category with new list
                        //update icon url if there is changes
                        //remove frm new list if exist
                        //add existing category to new vector
                        
                        for ( int z=0; z<list.categorylist.length; z++ ){
                            boolean isexist = false;
                            for ( int i=0; i<vcategory.size(); i++ ){
                            ACategoryObj tmpobj = (ACategoryObj)vcategory.elementAt(i);
                                if ( list.categorylist[z].title.equals(tmpobj.title) ){
                                    System.out.println(tmpobj.title+"::"+list.categorylist[z].iconurl+"::"+tmpobj.iconurl);
                                    if ( !list.categorylist[z].iconurl.equals(tmpobj.iconurl) ){
                                        list.categorylist[z].iconurl=tmpobj.iconurl;
                                        list.categorylist[z].icon=null;
                                    }
                                    vcategory.removeElementAt(i);
                                    isexist = true;
                                    break;
                                }
                            }// for ( int i=0; i<vcategory.size(); i++ ){
                            if ( isexist==true ){
                                newcategory.addElement(list.categorylist[z]);
                                if ( list.categorylist[z].id>maxid )
                                    maxid = list.categorylist[z].id;
                            }
                        }
                        
                        //run thru new list. existing category had been removed. remaining are new categories
                        //add to new  vector
                        for ( int i=0; i<vcategory.size(); i++ ){
                            ACategoryObj tmpobj = (ACategoryObj)vcategory.elementAt(i);
                            tmpobj.id=maxid+1;
                            maxid++;
                            newcategory.addElement(tmpobj);
                        }
                    }//if ( obj instanceof CategoryList ){
                    else{
                        //if database corrupted
                        //remove all articles and reset category listing
                        rssdb.removeAllItems();
                        newcategory=vcategory;
                    }
                }//if ( obj!=null)
                                                                else
                                                                        newcategory = vcategory;
                
                ACategoryList categorylist = new ACategoryList();
                categorylist.lastbuilddate = lastbuilddate;
                
                //update new vector to replace existing
                categorylist.categorylist = new ACategoryObj[newcategory.size()];
                for ( int i=0; i<newcategory.size(); i++ ){
                    categorylist.categorylist[i] = (ACategoryObj)newcategory.elementAt(i);
                    if  ( categorylist.categorylist[i].id==-1 ){
                        categorylist.categorylist[i].id=(maxid+1);                    
                        maxid++;
                    }
                }
                rssdb.setObjectPool(Const.ID_CATEGORY, categorylist);
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }    

    public synchronized static boolean saveCategoryList(ACategoryList list){
        try{
                ARssDB rssdb=ARssDB.getInstance();
                rssdb.setObjectPool(Const.ID_CATEGORY, list);
                return true;
        }catch(Exception e){

            e.printStackTrace();
        }
        return false;
    }    

} 
