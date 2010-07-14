package com.og.rss;

import com.og.app.util.*;

import net.rim.device.api.system.*;
import net.rim.device.api.collection.util.*;
import net.rim.device.api.util.*;

import java.util.*;

public class ARssDB {
   //LIVE-change for v1.1 release
   public static long DB_ID_V1_0 = 0xf123456789abc00L; // change to 01 for patch v1.1
   public static long DB_ID = 0xf123456789abc01L;   

   private static PersistentObject store;
   private static ARssDB instance;
   private LongHashtableCollection  contentsPool;

    private ARssDB() throws Exception {
        try {
            PersistentStore.destroyPersistentObject(DB_ID_V1_0); 
        } catch(Exception e) {}

        store = PersistentStore.getPersistentObject(DB_ID);
        contentsPool = (LongHashtableCollection)store.getContents();
        if (null == contentsPool) {
            contentsPool = new LongHashtableCollection();
        }
    }
    
   public synchronized static ARssDB getInstance() throws Exception{
        if (null == instance) {
            instance = new ARssDB();
        }
        return instance; 
    }

    /*public  boolean isPhotoItemExists(int key, PhotoItemObj newsitem) {
        synchronized(store) {
            PhotoItemObj[] ht = null;
            if ( contentsPool.get(key)==null )
               return false;
            else{
                ht = (PhotoItemObj[])contentspool.get(key);
                if ( ht.length >0 ){
                    for ( int i=0; i<ht.length; i++ ){
                        if ( ht[i].guid.equals(newsitem.guid) ){
                            //System.out.println("newsitem.status:"+newsitem.status);
                            if ( (newsitem.status!=null && newsitem.status.equalsIgnoreCase("Amended")) || 
                                    (newsitem.status==null && !newsitem.pubDate.equals(ht[i].pubDate) ) ){
                                //System.out.println("AMENDED NEWS:"+newsitem.title);        
                                byte[] tmpthumbnail = ht[i].thumbnail;
                                byte[] tmpcontent= ht[i].content;
                                ht[i] = newsitem;
                                if ( newsitem.thumbnailurl.equals(ht[i].thumbnailurl) )
                                    ht[i].thumbnail=tmpthumbnail;
                                if ( newsitem.contenturl.equals(ht[i].contenturl) )
                                    ht[i].content=tmpcontent;           
                                    
                                contentspool.put(key,ht);   
                                store.setContents(contentspool);
                                store.commit();                                                            
                            }
                            return true;
                        }
                    }
                }//if ( ht.length >0 )
            }
        }
        return false;
    } */  
    /*
    public  boolean isPhotoItemExists(int key, PhotoItemObj newsitem) {
        synchronized(store) {
            PhotoItemObj[] ht = null;
            if ( contentspool.get(key)==null )
               return false;
            else{
                ht = (PhotoItemObj[])contentspool.get(key);
                if ( ht.length >0 ){
                    for ( int i=0; i<ht.length; i++ ){
                        PhotoItemObj tmp = (PhotoItemObj)ht[i];
                        if ( tmp.guid.equals(newsitem.guid) )
                            return true;
                    }
                }
            }
        }
        return false;
    }   
    */
    
    public  boolean isNewsItemExists(int key, ANewsItemObj newsItem) {
        synchronized(store) {
            ANewsItemObj[] ht = null;
            if ( contentsPool.get(key)==null )
               return false;
            else{
                ht = (ANewsItemObj[])contentsPool.get(key);
                System.out.println("Aloy's contentPool Key:" + key);
                if ( ht.length >0 ){
                    for ( int i=0; i<ht.length; i++ ){
                        if ( ht[i].guid.equals(newsItem.guid) ) {
                            if ( (newsItem.status!=null && newsItem.status.equalsIgnoreCase("Amended")) || 
                                    (newsItem.status==null && !newsItem.pubDate.equals(ht[i].pubDate) ) ){
                                //System.out.println("AMENDED NEWS:"+newsitem.title);        
                                byte[] tmpthumbnail = ht[i].thumbnail;
                                byte[] tmpimage = ht[i].image;
                                ht[i] = newsItem;
                                if ( newsItem.thumbnailurl.equals(ht[i].thumbnailurl) )
                                    ht[i].thumbnail=tmpthumbnail;
                                if ( newsItem.imageurl.equals(ht[i].imageurl) )
                                    ht[i].image=tmpimage;           
                                    
                                contentsPool.put(key,ht);   
                                store.setContents(contentsPool);
                                store.commit();                                                            
                            }
                            return true;
                        }
                    }
                }//if ( ht.length >0 )
            }
        }
        return false;
    }   

    /*public synchronized int addPhotoItem(int key, Vector newsitems, RssListener listener) throws Exception {
        int recordsize = 0;
        synchronized(store) {
            int xdayscache = listener.getXDaysCache(key);
            Vector ht = null;
            if ( contentspool.get(key)==null )
                ht=new Vector();
            else{
                if ( xdayscache==0 )
                    ht=new Vector();
                else{
                    PhotoItemObj[] arr = (PhotoItemObj[])contentspool.get(key);
                    ht = getPhotoItemVector(arr);
                }
            }
            for ( int i=newsitems.size()-1; i>=0; i-- ){
                PhotoItemObj tmp = (PhotoItemObj)newsitems.elementAt(i);
                if ( tmp.pubDate==null || tmp.pubDate.equals("") ){
                    continue;
                }
                if ( tmp.longpubDate==0 )
                    tmp.longpubDate=Utility.getTimeInMillis(tmp.pubDate);
                
                boolean inserted=false;                         
                for ( int k=0; k<ht.size(); k++ ){
                    PhotoItemObj dbobj = (PhotoItemObj)ht.elementAt(k);
                    if ( Long.parseLong(dbobj.guid)<Long.parseLong(tmp.guid) ){
                        ht.insertElementAt(tmp, k);
                        inserted=true;
                        break;
                    }
                }
                if ( inserted==false )
                    ht.addElement(tmp);                        
            }        

           //System.out.println("ht size:"+ht.size() );
            if ( xdayscache>0 ){
                long longtime = Utility.getCurrentDateInMillis();
                longtime = longtime -(xdayscache*24*60*60*1000);
                for ( int i=ht.size()-1; i>=0; i-- ){
                    PhotoItemObj tmp = (PhotoItemObj)ht.elementAt(i);
                    if ( tmp.longpubDate<longtime ){
                            //System.out.println("remove::"+tmp.pubDate+"::"+tmp.title+"::"+tmp.longpubDate+"::"+longtime);                        
                            ht.removeElementAt(i);
                    }else{
                    }
                }                
            }
            
            int maxitem = listener.getMaxItemNo(key);
            if ( ht.size()>maxitem ){
                for ( int i=ht.size()-1; i>=maxitem; i-- )
                    ht.removeElementAt(i);
            }
            recordsize =ht.size();  
            PhotoItemObj[] arr =getPhotoItemArr(ht);
            contentspool.put(key,arr);   
            store.setContents(contentspool);
            store.commit();
        }
        return recordsize ;
    }   */

    /*
    public synchronized int addPhotoItem(int key, Vector newsitems, RssListener listener) throws Exception {
            int recordsize = 0;
            synchronized(store) {
                int xdayscache = listener.getXDaysCache(key);
                Vector ht = null;
                if ( contentspool.get(key)==null )
                    ht=new Vector();
                else{
                    if ( xdayscache==0 )
                        ht=new Vector();
                    else{
                        PhotoItemObj[] arr = (PhotoItemObj[])contentspool.get(key);
                        ht = getPhotoItemVector(arr);
                    }
                }
                for ( int i=newsitems.size()-1; i>=0; i-- ){
                    PhotoItemObj tmp = (PhotoItemObj)newsitems.elementAt(i);
                    if ( tmp.pubDate==null || tmp.pubDate.equals("") ){
                        continue;
                    }
                    if ( tmp.longpubDate==0 )
                        tmp.longpubDate=Utility.getTimeInMillis(tmp.pubDate);
                    
                    boolean inserted=false;                         
                    for ( int k=0; k<ht.size(); k++ ){
                        PhotoItemObj dbobj = (PhotoItemObj)ht.elementAt(k);
                        if ( dbobj.longpubDate<=tmp.longpubDate ){
                            ht.insertElementAt(tmp, k);
                            inserted=true;
                            break;
                        }
                    }
                    if ( inserted==false )
                        ht.addElement(tmp);                        
                }        
    
            //System.out.println("ht size:"+ht.size() );
                if ( xdayscache>0 ){
                    long longtime = Utility.getCurrentDateInMillis();
                    longtime = longtime -(xdayscache*24*60*60*1000);
                    for ( int i=ht.size()-1; i>=0; i-- ){
                        PhotoItemObj tmp = (PhotoItemObj)ht.elementAt(i);
                        if ( tmp.longpubDate<longtime ){
                                //System.out.println("remove::"+tmp.pubDate+"::"+tmp.title+"::"+tmp.longpubDate+"::"+longtime);                        
                                ht.removeElementAt(i);
                        }else{
                        }
                    }                
                }
                
                int maxitem = listener.getMaxItemNo(key);
                if ( ht.size()>maxitem ){
                    for ( int i=ht.size()-1; i>=maxitem; i-- )
                        ht.removeElementAt(i);
                }
                //System.out.println("ADD NEWS ITEM (final):"+key+":"+ht.size());        
                recordsize =ht.size();  
                PhotoItemObj[] arr =getPhotoItemArr(ht);
                contentspool.put(key,arr);   
                store.setContents(contentspool);
                store.commit();
            }
            return recordsize ;
        }   
    */
    /*public synchronized int getPhotoItemSize(int key) throws Exception {
         synchronized(store) {
             if ( contentspool.get(key)==null )
                return 0;
             PhotoItemObj[] arr = (PhotoItemObj[])contentspool.get(key);
             return arr.length;
         }
    }*/
            
    public synchronized int getNewsItemSize(int key) throws Exception {
         synchronized(store) {
             if ( contentsPool.get(key)==null )
                return 0;
             ANewsItemObj[] arr = (ANewsItemObj[])contentsPool.get(key);
             return arr.length;
         }
    }
 
    public synchronized int addNewsItem(int key, Vector newsitems, RssListener listener) throws Exception {
        int recordsize = 0;
        //System.out.println("ADD NEWS ITEM:"+key+":"+newsitems.size());
        synchronized(store) {
            int xdayscache = listener.getXDaysCache(key);
            //System.out.println("xdayscache: "+xdayscache);
            Vector ht = null;
            if ( contentsPool.get(key)==null )
                ht =new Vector();
            else{
                if ( xdayscache==0 )
                    ht =new Vector();
                else{
                    ANewsItemObj[] arr = (ANewsItemObj[])contentsPool.get(key);
                    ht = getNewsItemVector(arr);
                }
            }
            
            for ( int i=newsitems.size()-1; i>=0; i-- ){
                ANewsItemObj tmp = (ANewsItemObj)newsitems.elementAt(i);
                if ( tmp.pubDate==null || tmp.pubDate.equals("") ){
                    continue;
                }
                if ( tmp.longpubDate==0 )
                    tmp.longpubDate=Utility.getTimeInMillis(tmp.pubDate);
                
                boolean inserted=false;                         
                for ( int k=0; k<ht.size(); k++ ){
                    ANewsItemObj dbobj = (ANewsItemObj)ht.elementAt(k);
                    if ( dbobj.longpubDate<=tmp.longpubDate ){
                        //System.out.println(ht.size()+"::"+k+"::"+tmp.title+"::");          
                        ht.insertElementAt(tmp, k);
                        inserted=true;
                        break;
                    }
                }
                if ( inserted==false ){
                    //System.out.println(ht.size()+"::0::"+tmp.title);
                    ht.addElement(tmp);
                }
            }//for ( int i=newsitems.size()-1; i>=0; i-- ){        

            if ( xdayscache>0 ){
                long longtime = Utility.getCurrentDateInMillis();

                longtime = longtime -(xdayscache*24*60*60*1000);
                //System.out.println("longtime::"+longtime);
                for ( int i=ht.size()-1; i>=0; i-- ){
                    ANewsItemObj tmp = (ANewsItemObj)ht.elementAt(i);
                    //System.out.println("ht("+i+")::"+tmp.longpubDate);
                    if ( tmp.longpubDate<longtime ){
                            //System.out.println("remove::"+tmp.pubDate+"::"+tmp.title+"::"+tmp.longpubDate+"::"+longtime);
                            ht.removeElementAt(i);
                    }else{
                    }
                }      
             
            }
            
            int maxitem = listener.getMaxItemNo(key);
            if ( ht.size()>maxitem ){
                for ( int i=ht.size()-1; i>=maxitem; i-- )
                    ht.removeElementAt(i);
            }
            recordsize =ht.size();            
            //System.out.println("ADD NEWS ITEM (final):"+key+":"+ht.size());          
            ANewsItemObj[] arr =getNewsItemArr(ht);
            contentsPool.put(key,arr);   
            store.setContents(contentsPool);
            store.commit();
        }
        return recordsize;
    }   
    
    public void setObjectPool(int key, net.rim.device.api.util.Persistable value) throws Exception {
        //System.out.println("setObjectPool(int key, Object value)"+key+","+value); 
        synchronized(store) {
            contentsPool.put(key,value);   
            store.setContents(contentsPool);
            store.commit();
        }
    } 
        
    public void setObjectPool(int key, Vector ht) throws Exception {
         //System.out.println("setObjectPool(int key, Vector value):"+key+","+ht.size()); 
        synchronized(store) {
            Object obj = ht.elementAt(0);
            if ( obj instanceof ANewsItemObj ){
                ANewsItemObj[] arr =getNewsItemArr(ht);
                contentsPool.put(key,arr);   
            }/*else if (obj instanceof PhotoItemObj ){
                PhotoItemObj[] arr =getPhotoItemArr(ht);
                contentspool.put(key,arr);   
            }*/
            store.setContents(contentsPool);
            store.commit();
        }
    }    
    
   /* private Vector  getPhotoItemVector(PhotoItemObj[] arr){
        Vector  ht = new Vector();
        for ( int i=0; i<arr.length; i++ ){
            ht.addElement(arr[i]);
        }
        return ht;
    }
        
    private PhotoItemObj[] getPhotoItemArr(Vector ht){
        PhotoItemObj[] arr = new PhotoItemObj[ht.size()];
        for ( int i=0; i<ht.size(); i++ ){
            arr[i] = (PhotoItemObj)ht.elementAt(i);
            arr[i].title=Utility.turnOffHtmlTag(arr[i].title.trim());
            arr[i].description=Utility.turnOffHtmlTag(arr[i].description.trim());            
        }
        return arr;
    }*/
        
    private Vector  getNewsItemVector(ANewsItemObj[] arr){
        Vector  ht = new Vector();
        for ( int i=0; i<arr.length; i++ ){
            ht.addElement(arr[i]);
        }
        return ht;
    } 
        
    private ANewsItemObj[] getNewsItemArr(Vector ht){
        ANewsItemObj[] arr = new ANewsItemObj[ht.size()];
        for ( int i=0; i<ht.size(); i++ ){
            arr[i] = (ANewsItemObj)ht.elementAt(i);
            arr[i].title=Utility.turnOffHtmlTag(arr[i].title.trim());
            arr[i].description=Utility.turnOffHtmlTag(arr[i].description.trim());
        }
        return arr;
    }
      
    /*public void savePhotoItemObj(int key, PhotoItemObj pobj) {
        synchronized(store) {
            if (null != contentspool && contentspool.size() != 0) {
                Object obj =  contentspool.get(key);
                if ( obj instanceof PhotoItemObj[] ){
                    PhotoItemObj[] arr = (PhotoItemObj[])obj;
                    for ( int i=0; i<arr.length; i++ ){
                        if ( arr[i].guid.equals(pobj.guid) ){
                            arr[i]=pobj;
                            contentspool.put(key,arr);   
                            store.setContents(contentspool);
                            store.commit();            
                            break;                
                        }
                    }
                }//if ( obj instanceof NewsItemObj[] ){
            }
        }
    }*/
          
    public void saveNewsItemObj(int key, ANewsItemObj pobj) {
        synchronized(store) {
            if (null != contentsPool && contentsPool.size() != 0) {
                Object obj =  contentsPool.get(key);
                if ( obj instanceof ANewsItemObj[] ){
                    ANewsItemObj[] arr = (ANewsItemObj[])obj;
                    for ( int i=0; i<arr.length; i++ ){
                        if ( arr[i].guid.equals(pobj.guid) ){
                            arr[i]=pobj;
                            contentsPool.put(key,arr);   
                            store.setContents(contentsPool);
                            store.commit();            
                            break;                
                        }
                    }
                }//if ( obj instanceof NewsItemObj[] ){
            }
        }
    }    
    
    public Object getObjectPool(int key) {
        //System.out.println("GET OBJECT POOL:"+key+".");    
        synchronized(store) {
            if (null != contentsPool && contentsPool.size() != 0) {
                Object obj =  contentsPool.get(key);
                if ( obj instanceof ANewsItemObj[] ){
                    ANewsItemObj[] arr = (ANewsItemObj[])obj;
                    return getNewsItemVector(arr);
                }/*else if ( obj instanceof PhotoItemObj[] ){
                    PhotoItemObj[] arr = (PhotoItemObj[])obj;
                    return getPhotoItemVector(arr);
                }*/
                return contentsPool.get(key);
            } else {
                 return null;
            }
        }
    }
    
    public void removeObjectPool(int key) throws Exception {
         //System.out.println("removeObjectPool(int key, Vector value)"+key); 
        synchronized(store) {
            contentsPool.remove(key);
            store.setContents(contentsPool);
            store.commit();
        }
    }       
     
    public boolean removeObjectPool(int key, String guid) throws Exception {
         boolean found = false;        
         //System.out.println("removeObjectPool(int key, String guid):"+key+":"+guid); 
        synchronized(store) {
            if (null != contentsPool && contentsPool.size() != 0) {
                Object obj =  contentsPool.get(key);
                if ( obj instanceof ANewsItemObj[] ){
                    ANewsItemObj[] arr = (ANewsItemObj[])obj;
                    ANewsItemObj[] finalarr = new ANewsItemObj[arr.length-1];
                    int index=0;
                    for ( int i=0; i<arr.length; i++ ){
                        if ( arr[i].guid.equals(guid) ){
                            found = true;
                            continue;
                        }
                        finalarr[index++] = arr[i];
                    }
                    if ( found ){
                        contentsPool.put(key,finalarr);   
                        store.setContents(contentsPool);
                        store.commit();
                    }
                }/*else if ( obj instanceof PhotoItemObj[] ){
                    PhotoItemObj[] arr = (PhotoItemObj[])obj;
                    PhotoItemObj[] finalarr = new PhotoItemObj[arr.length-1];
                    int index=0;
                    for ( int i=0; i<arr.length; i++ ){
                        if ( arr[i].guid.equals(guid) ){
                            found = true;
                            continue;
                        }
                        finalarr[index++] = arr[i];
                    }
                    if ( found ){
                        contentspool.put(key,finalarr);   
                        store.setContents(contentspool);
                        store.commit();
                    }
                }*/
            } 
        }
        return found;
    } 
    
    public void removeAllItems(){
         LongEnumeration allkeys = contentsPool.getKeys();
         while(allkeys.hasMoreElements()){
             try{
                long key = allkeys.nextElement();
                //category key are more than 0
                if ( key>0 || key==com.og.app.Const.ID_PHOTOGALLERY ){
                        removeObjectPool( (int)(key));
                }
            }catch(Exception e){}
         }
    }          
} 
