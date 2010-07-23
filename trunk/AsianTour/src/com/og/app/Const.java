package com.og.app;
import com.og.app.gui.GuiConst;

public class Const{
    public static int ERRORCODE_1STTIMEUSERERROR   = 1;
    public static int ERRORCODE_LOADIMAGE                       = 2;
    public static int ERRORCODE_NOCATEGORY                  = 3;
    public static int ERRORCODE_EXECEPTION                     = 4;
   
    public static long TIMEDIFF = 0;
     
    public static int DEFAULT_FONTSIZE = GuiConst.FONTSIZE_ARR[1];  
    static{
        if (GuiConst.SCREENWIDTH > 320 )
            DEFAULT_FONTSIZE=GuiConst.FONTSIZE_ARR2[1];         
    }
    public final static String VERSION = "CLOSED BETA";
    public final static String DEVICENAME = net.rim.device.api.system.DeviceInfo.getDeviceName();
    
    public final static int DEFAULT_DELETEITEMEXPIREDDAYS= 10;

    public final static int DEFAULT_XDAYSBOOKMARK = 7;
    public final static int DEFAULT_XDAYSCACHE = GuiConst.CACHE_ARR[GuiConst.CACHE_ARR.length-1];
    public final static String DEFAULT_FONTNAME= GuiConst.FONTNAME_ARR[0];//"BBAlpha Sans";
    public final static int DEFAULT_NEWSIMGPERCENTAGE= GuiConst.PREVIEWIMAGE_PERCENTAGE[1]; //50%

    public final static int ID_SETTING      = -1;
    public final static int ID_SAVED = -2;    
    public final static int ID_CATEGORY = -3;
    public final static int ID_WEATHER = -4;
    public final static int ID_ADS= -5;
    public final static int ID_DELETE= -6;
    public final static int ID_PHOTOGALLERY= -7;

    public final static int ID_UNKNOWN= -99;
    
    //DEV     
     /*
    public final static String FEED_WEATHER= "http://mobile.mediacorp.com.sg/Today/weather_dev.xml";
    public final static String FEED_ADS= "http://mobile.mediacorp.com.sg/Today/ads_bb_dev.xml";
    public final static String FEED_INDEX = "http://mobile.mediacorp.com.sg/Today/index_dev.xml";
    public final static String FEED_PHOTO= "http://mobile.mediacorp.com.sg/Today/Photos_dev.xml";
    public final static String FEED_STATUS= "http://mobile.mediacorp.com.sg/Today/status_dev.xml";
    public final static String API_PAGETRACK= "http://mobile.mediacorp.com.sg/Today/page_track_dev.php";
    */
    
    //LIVE
//    public final static String FEED_WEATHER= "http://mobile.mediacorp.com.sg/Today/weather.xml";
//    public final static String FEED_ADS= "http://mobile.mediacorp.com.sg/Today/ads_bb.xml";
//    public final static String FEED_INDEX = "http://mobile.mediacorp.com.sg/Today/index.xml";
//    public final static String FEED_PHOTO= "http://mobile.mediacorp.com.sg/Today/Photos.xml";
//    public final static String FEED_STATUS= "http://mobile.mediacorp.com.sg/Today/status.xml";
//    public final static String API_PAGETRACK= "http://mobile.mediacorp.com.sg/Today/page_track.php";
//    
//    public final static String FEED_DELETE= "http://mobile.mediacorp.com.sg/Today/delete.xml";    
//    public final static String API_VERSION  = "http://mobile.mediacorp.com.sg/Today/app/version.php";
//    public final static String DOWNLOAD_URL= "http://mobile.mediacorp.sg/Today/app/bb.php";
        
     public final static String PAGETRACKER_NAME ="Photo";

    
}
