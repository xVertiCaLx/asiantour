package com.og.app.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.UiApplication;

import com.og.app.Const;
import com.og.app.gui.Lang;
import com.og.rss.ANewsFeed;

public class Utility{
	
	public static int connCount = 0;
	
    static Hashtable MONTHARR= new Hashtable();

    static{
        MONTHARR.put("Jan","0");
        MONTHARR.put("Feb","1");
        MONTHARR.put("Mar","2");
        MONTHARR.put("Apr","3");
        MONTHARR.put("May","4");
        MONTHARR.put("Jun","5");
        MONTHARR.put("Jul","6");
        MONTHARR.put("Aug","7");
        MONTHARR.put("Sep","8");
        MONTHARR.put("Oct","9");
        MONTHARR.put("Nov","10");
        MONTHARR.put("Dec","11");                                                                                   
    }


    public static long getCurrentDateInMillis(){
        try{
            Calendar rightnow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            Date newdate = rightnow.getTime();
            newdate.setTime(newdate.getTime()+Const.TIMEDIFF);
            rightnow.setTime(newdate);
             
            rightnow.set(Calendar.SECOND, 0);
            rightnow.set(Calendar.HOUR_OF_DAY, 0);
            rightnow.set(Calendar.MINUTE, 0);     
            rightnow.set(Calendar.MILLISECOND, 0);     
            return (rightnow.getTime()).getTime();
        }catch(Exception e){}
        return 0;
    }
        
    public static long getCurrentTimeInMillis(){
        try{
            Calendar rightnow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            //System.out.println("rightnow:"+rightnow);
            //System.out.println("Const.TIMEDIFF:"+Const.TIMEDIFF);
            return (rightnow.getTime()).getTime()+Const.TIMEDIFF;
        }catch(Exception e){}
        return 0;
    }
    
    //Fri, 04 Sep 2009 12:21:00 +0800
    public static long getTimeInMillis(String strdate){
        try{
            String yy = strdate.substring(12,16);
            String mm = strdate.substring(8,11);
            String dd = strdate.substring(5,7).trim();
            String hh = strdate.substring(17,19);
            String mn = strdate.substring(20,22);
            String ss = strdate.substring(23,25);
                            
            Calendar rightnow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            rightnow.set(Calendar.YEAR, Integer.parseInt(yy));
            rightnow.set(Calendar.MONTH, getMonth(mm));
            rightnow.set(Calendar.DATE, Integer.parseInt(dd));
            rightnow.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hh));
            rightnow.set(Calendar.MINUTE, Integer.parseInt(mn));
            rightnow.set(Calendar.SECOND, Integer.parseInt(ss));
            rightnow.set(Calendar.MILLISECOND, 0);
            return (rightnow.getTime()).getTime();
        }catch(Exception e){}
        return 0;
    }
    
    public static int getMonth(String strmonth){
        if ( MONTHARR.get(strmonth)!=null )
            return Integer.parseInt(MONTHARR.get(strmonth).toString());
        return -1;
    }
    
    public static String getDeviceID(){
        return Integer.toHexString(DeviceInfo.getDeviceId());
    }

    public static ANewsFeed getDefaultBookmarkFeed(){
        return new ANewsFeed (Const.ID_SAVED,
                    Lang.TAB_SAVED,
                    "", 
                    "res/tab_saved.png", 
                    "", ANewsFeed.FEEDTYPE_NEWS);         
    }
        
   /* public static ANewsFeed getDefaultPhotoGalleryFeed(){
        return new ANewsFeed (Const.ID_PHOTOGALLERY,
                    Lang.TAB_PHOTOGALLERY,
                    Const.FEED_PHOTO, 
                    "res/tab_photogallery.png", 
                    "", ANewsFeed.FEEDTYPE_PHOTO);
    }*/

    //once got paragraph stop
    public static Vector breakIntoWordsSpecial(String paragraph, int maxword ){
        Vector v = new Vector();
        
        int paraindex = paragraph.indexOf("\n");
        if ( paraindex>0 )
            paragraph=paragraph.substring(0,paraindex);
        do{
            paragraph=paragraph.trim();
            int index = paragraph.indexOf(' ');
            if (  index>=0 ){
                v.addElement(paragraph.substring(0, index));
                paragraph=paragraph.substring(index+1, paragraph.length());
                
                if ( v.size()>=maxword )
                    break;
            }else
                break;
        }while(true);
        v.addElement(paragraph);
        return v;
    }
        
    public static Vector breakIntoWords(String paragraph ){
        Vector v = new Vector();
        do{
            paragraph=paragraph.trim();
            int index = paragraph.indexOf(' ');
            if (  index>=0 ){
                v.addElement(paragraph.substring(0, index));
                paragraph=paragraph.substring(index+1, paragraph.length());
            }else
                break;
        }while(true);
        v.addElement(paragraph);
        return v;
    }
    

    private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2)
    {
            int out[] = new int[x2*y2];
        for (int yy = 0; yy < y2; yy++)
            {
                    int dy = yy * y / y2;
                for (int xx = 0; xx < x2; xx++)
                    {
                            int dx = xx * x / x2;
                        out[(x2 * yy) + xx] = ini[(x * dy) + dx];
                    }
            }
            return out;
    }


    public static Bitmap resizeBitmap(Bitmap image, int width, int height)
    {       
            // Note from DCC:
            // an int being 4 bytes is large enough for Alpha/Red/Green/Blue in an 8-bit plane...
        // my brain was fried for a little while here because I am used to larger plane sizes for each
        // of the color channels....
        //
    
        //Need an array (for RGB, with the size of original image)
            //
            int rgb[] = new int[image.getWidth()*image.getHeight()];
    
            //Get the RGB array of image into "rgb"
            //
            image.getARGB(rgb, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
    
        //Call to our function and obtain RGB2
        //
            int rgb2[] = rescaleArray(rgb, image.getWidth(), image.getHeight(), width, height);
    
        //Create an image with that RGB array
        //
            Bitmap temp2 = new Bitmap(width, height);
    
            temp2.setARGB(rgb2, 0, width, 0, 0, width, height);
        
            return temp2;
    }

    public static Bitmap bestFit(Bitmap image, int maxWidth, int maxHeight)
    {
            // getting image properties
        int w = image.getWidth();
            int h = image.getHeight();
    
        //  get the ratio
            int ratiow = 100 * maxWidth / w;
            int ratioh = 100 * maxHeight / h;
    
                            // this is to find the best ratio to
                        // resize the image without deformations
            int ratio = Math.min(ratiow, ratioh);
    
        // computing final desired dimensions
        int desiredWidth = w * ratio / 100;
        int desiredHeight = h * ratio / 100;
    
        //resizing
            return resizeBitmap(image, desiredWidth, desiredHeight);
    }
         
    public static String turnOffURLSpecialChar(String str){
        String[][] HTMLARR = new String[][]{
            {" ","%20"},            
            {"+","%2B"},                                   
        };         
        for ( int i=0; i<HTMLARR.length; i++ ){
                int index = 0;
                do{
                        int tmpindex = str.indexOf(HTMLARR[i][0],index);
                        if ( tmpindex<0 )
                            break;
                        str=str.substring(0,tmpindex)+HTMLARR[i][1]+str.substring(tmpindex+HTMLARR[i][0].length(), str.length());
                        index=tmpindex;
                }while(true);            
        }        
        return str;
    }     
    public static String turnOffHtmlTag(String str){
        String[][] HTMLARR = new String[][]{
            {"<p></p>","\n"},
            {"<p>",""},
            {"</p>","\n\n"},
            {"<b>",""},
            {"</b>",""},
            {"&copy;","?"},
            {"&reg;","?"},            
            {"&lt;","<"},            
            {"&gt;",">"},           
             {"&amp;","&"},          
             {"\n ","\n"},  
             {"&apos;","'"},                   
             {"&quot;","\""},         
            {"&#8211;","¨C"},            
            {"&#8212;","¡ª"},         
            {"&#8216;","¡®"},             
            {"&#8217;","¡¯"},             
            {"&#8218;","?"},          
            {"&#8220;","¡°"},                
            {"&#8221;","¡±"},    
            {"&#8222;","?"},   
            {"&#8224;","?"},   
            {"&#8225;","?"},          
            {"&#8226;","?"},    
            {"&#8230;","¡­"},    
            {"&#8240;","¡ë"},    
            {"&#8364;","?"},    
            {"&#8482;","?"},    
                                            
        }; 
        for ( int i=0; i<HTMLARR.length; i++ ){
                int index = 0;
                do{
                        int tmpindex = str.indexOf(HTMLARR[i][0],index);
                        if ( tmpindex<0 )
                            break;
                        str=str.substring(0,tmpindex)+HTMLARR[i][1]+str.substring(tmpindex+HTMLARR[i][0].length(), str.length());
                        index=tmpindex;
                }while(true);            
        }
        
       int startindex = 0;
        do{
                int tmpindex = str.indexOf("&#", startindex);
                if ( tmpindex<0 )
                    break;    
                int tmpindex2= str.indexOf(";", tmpindex);    
                String tmpno = str.substring(tmpindex+2, tmpindex2);
                try{
                    int intno = Integer.parseInt(tmpno);
                    if ( intno<256 ){
                        char ch = (char)intno;
                        str=str.substring(0,tmpindex)+ch+str.substring(tmpindex2+1, str.length());
                        startindex=tmpindex+1;
                    }
                    else
                        startindex=tmpindex2;
                }catch(Exception e){
                    startindex=tmpindex2;
                }
        }while(true);               
        return str;
    }
    
    
    public static void getWebData(final String url, final WebDataCallback callback) throws IOException
    {
    	
    	System.out.println("Get Web Data: " + url);
    	Thread t = new Thread(new Runnable()
    	{
    		public void run()
    		{
    			connCount++;
    			try
    			{
    				System.out.println("Get Web Data:Connection.open ");
    				final byte[] result = ConnectionMgr.loadImage(url);
    				UiApplication.getUiApplication().invokeLater(new Runnable()
    				{
    					public void run()
    					{
    						if(result==null){
    							callback.callback(new byte[]{});
    						}else{
    							callback.callback(result);
    						}
    						
    					}
    				});
    			}
    			catch (final Exception ex)
    			{
    				UiApplication.getUiApplication().invokeLater(new Runnable()
    				{
    					public void run()
    					{
    						callback.callback(new byte[]{});
    					}
    				});
    			}
    			
    		}
    	});
    	retry:
    	if(connCount < 2){
    		t.start();
    	}else{
    		try {
				Thread.sleep(1000);
				System.out.println("Thread retry...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break retry;
    	}
    }	
    
    public static void getWebDataStr(final String url, final WebDataCallbackStr callback) throws IOException
    {
    	
    	System.out.println("Get Web Data: " + url);
    	Thread t = new Thread(new Runnable()
    	{
    		public void run()
    		{
    			connCount++;
    			try
    			{
    				System.out.println("Get Web Data:Connection.open ");
    				final String result = ConnectionMgr.wgetData(url);
    				UiApplication.getUiApplication().invokeLater(new Runnable()
    				{
    					public void run()
    					{
    						if(result==null){
    							callback.callback("");
    						}else{
    							callback.callback(result);
    						}
    						
    					}
    				});
    			}
    			catch (final Exception ex)
    			{
    				UiApplication.getUiApplication().invokeLater(new Runnable()
    				{
    					public void run()
    					{
    						callback.callback("");
    					}
    				});
    			}
    			
    		}
    	});
    	t.start();
//    	retry:
//    	if(connCount < 1){
//    		
//    	}else{
//    		try {
//				Thread.sleep(1000);
//				System.out.println("Thread retry...");
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			break retry;
//    	}
    }	
}
