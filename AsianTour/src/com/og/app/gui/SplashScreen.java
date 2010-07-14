package com.og.app.gui;

//import com.og.app.fakeRecordStore.*;
import com.og.app.asiantour.*;
import com.og.app.object.*;
import com.og.app.*;
import com.og.app.util.*;
import com.og.rss.*;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;
import java.util.*;

public class SplashScreen extends MainScreen implements Runnable
{
    private Thread thread = null;
    private Bitmap logoicon= null;
    String text = "Version "+Const.VERSION;
    String text2 = "";//"Type:"+ConnectionMgr.CONNECTIONTYPE;
    String text3 = "";    
    private Object lock = new Object();
    boolean showopeningads = false;
    private Bitmap openingads = null;
    //public NewsStore ns = new NewsStore;
    private ANewsItemObj newsItemObj = null;
    
    public SplashScreen() {
        
        try{      
            Settings settingobj = Utility.loadSetting();         
            GuiConst.reinitFont();
        } catch(Exception e) {
            e.printStackTrace();
        }  
                                     
        logoicon= Bitmap.getBitmapResource("res/slogo.png");
    
        thread = new Thread(this);
        thread.start();
    }
    
     protected void paint(Graphics graphics) {
        
        int scrwidth = net.rim.device.api.system.Display.getWidth();
        int scrheight =  net.rim.device.api.system.Display.getHeight();      
        int logowidth  = logoicon.getWidth();
        int logoheight=  logoicon.getHeight();
        
        int x = (scrwidth-logowidth)/2;
        int y = (scrheight-logoheight-GuiConst.FONT_VERSION.getHeight()*2)/2;
        //System.out.println("x:"+x+" , y:"+y);
        graphics.drawBitmap(x, y, logowidth, logoheight, logoicon , 0, 0);
        
        int textwidth = GuiConst.FONT_VERSION.getAdvance(text);        
        graphics.setFont(GuiConst.FONT_VERSION);
        x = (scrwidth-textwidth)/2;
        y=y+logoheight+10;
        graphics.drawText(text, x, y);           
        
        textwidth = GuiConst.FONT_VERSION.getAdvance(text2);        
        x = (scrwidth-textwidth)/2;
        y=y+GuiConst.FONT_VERSION.getHeight()+2;
        graphics.drawText(text2, x, y);                  

        textwidth = GuiConst.FONT_VERSION.getAdvance(text3);        
        x = (scrwidth-textwidth)/2;
        y=y+GuiConst.FONT_VERSION.getHeight()+2;
        graphics.drawText(text3, x, y);              
        
    }
    
    public boolean keyChar(char key, int status, int time) {
        switch (key) {
            case Characters.ESCAPE:
                System.exit(0);
                break;
        }
        return true;
    }  
       
    public void run(){      
        boolean firsttimeuser = false;
        try{
                String lastbuilddate = "";
        
                //check is 1st time user?
                if ( FeedReader.loadCategoryList()==null ){
                    firsttimeuser = true;
                } 
                
                StatusObj statusobj = FeedReader.loadStatus(Const.FEED_STATUS);
                if ( statusobj!=null ){
                    if ( statusobj.serverDateTime!=null && !statusobj.serverDateTime.equals("") ){
                        long servertime = Utility.getTimeInMillis(statusobj.serverDateTime);
                        Calendar cal = Calendar.getInstance();
                        long currenttime = (cal.getTime()).getTime();
                        long diff = servertime-currenttime;
                        if ( diff<0 )
                            diff=diff*-1;
                        if ( diff>15*60*1000 )
                            Const.TIMEDIFF=servertime-currenttime;
                        
                    }
                } 
                //System.out.println("Const.TIMEDIFF:"+Const.TIMEDIFF);
                
                String lastBuildDate_index = "";
                if ( statusobj!=null )
                    lastBuildDate_index = statusobj.lastBuildDate_index;
                int indexfeedstatus = FeedReader.reloadIndexFeed(Const.FEED_INDEX, lastBuildDate_index);
                ACategoryList list = FeedReader.loadCategoryList();
                if ( list==null ){
                    if ( firsttimeuser==true){
                            text = Lang.ERRORMSG_1stTIMENOCONN1;
                            text2 = Lang.ERRORMSG_1stTIMENOCONN2;
                            text3 = "[code= "+Const.ERRORCODE_1STTIMEUSERERROR+"."+ConnectionMgr.CONNECTIONTYPE+"]";                              
                            invalidate();
                            try{Thread.sleep(5000);}catch(Exception e){}   
                            System.exit(0);        
                            return;     
                    }   else{
                            text = Lang.ERRORMSG_NOCONN1;
                            text2 = Lang.ERRORMSG_NOCONN2;           
                            text3 = "[code= "+Const.ERRORCODE_NOCATEGORY+"."+ConnectionMgr.CONNECTIONTYPE+"]";   
                            invalidate();                 
                            try{Thread.sleep(5000);}catch(Exception e){}   
                    }         
                }        

                //load image if the version type need load image from server
                if ( list!=null && indexfeedstatus==1 && GuiConst.DEFAULT_SHOWTABICON==true){
                    boolean haschanges = false;
                    for ( int i=0; i<list.categorylist.length; i++ ){
                        System.out.println(list.categorylist[i].id+"::"+list.categorylist[i].title+"::"+list.categorylist[i].iconurl+"::"+list.categorylist[i].category+"::"+list.categorylist[i].link+"::"+list.categorylist[i].description);                        
                        if (  list.categorylist[i].icon==null ){
                            list.categorylist[i].icon = ConnectionMgr.loadImage(list.categorylist[i].iconurl);
                            //exit application if failed to load icon.
                            if ( list.categorylist[i].icon==null ){
                                text = Lang.ERRORMSG_NOCONN1;
                                text2 = Lang.ERRORMSG_NOCONN2;
                                text3 = "[code "+Const.ERRORCODE_LOADIMAGE+"."+ConnectionMgr.CONNECTIONTYPE+"]";   
                                invalidate();
                                try{Thread.sleep(5000);}catch(Exception e){}   
                                System.exit(0);
                                return;
                            }
                            haschanges = true;
                        }
                    }
                    //save changes
                    if ( haschanges ){
                            FeedReader.saveCategoryList(list);
                    }
                }
 
                Settings settingobj = Utility.loadSetting();
                if ( settingobj.rssfeeds==null ){
                    settingobj = Utility.initAppSettingObj();
                    Utility.saveAppSetting(settingobj);
                }else{
                      if ( indexfeedstatus==1 ){
                        //remove category that not in index and no article
                        try{
                            MenuScreen menuscreen = MenuScreen.getInstance();
                            ARssDB rssdb= ARssDB.getInstance();
                            ANewsFeed[] rssfeedarr = settingobj.rssfeeds;
                            if ( rssfeedarr!=null ){ 
                                Vector v = new Vector();
                                for ( int i=0; i<rssfeedarr.length; i++ ){
                                    //update icon image if there is any update
                                    /*if ( GuiConst.DEFAULT_SHOWTABICON==true ){
                                                for ( int z=0; z<list.categorylist.length; z++ ){
                                                        if ( list.categorylist[z].title.equals(rssfeedarr[i].name) ){
                                                                if ( !list.categorylist[z].iconurl.equals(rssfeedarr[i].iconimageurl) ){
                                                                    rssfeedarr[i].iconimageurl=list.categorylist[z].iconurl;
                                                                    rssfeedarr[i].iconimage = list.categorylist[z].icon;
                                                                }
                                                                break;
                                                        }
                                                }                                                    
                                    }*/
                                    
                                    //clean expired article
                                    if ( rssfeedarr[i].feedType==ANewsFeed.FEEDTYPE_NEWS ){
                                        int recordsize = 0;//rssdb.addNewsItem(rssfeedarr[i].id, new Vector(), menuscreen);
                                        if ( recordsize<=0 ){
                                            if ( rssfeedarr[i].id==Const.ID_SAVED ){
                                                v.addElement(rssfeedarr[i]);
                                            }else{
                                                boolean isfound = false;
                                                for ( int z=0; z<list.categorylist.length; z++ ){
                                                        if ( list.categorylist[z].title.equals(rssfeedarr[i].name) ){
                                                            
                                                            v.addElement(rssfeedarr[i]);
                                                            isfound = true;
                                                            break;
                                                        }
                                                }
                                                //removed if no article and category not found in latest category listing
                                                if ( isfound==false ){
                                                        rssdb.removeObjectPool(rssfeedarr[i].id);
                                                }
                                            }
                                        }else
                                            v.addElement(rssfeedarr[i]);
                                    }/*else{
                                        rssdb.addPhotoItem(rssfeedarr[i].id, new Vector(), menuscreen);         
                                        v.addElement(rssfeedarr[i]);
                                    }  */                     
                                }//for ( int i=0; i<rssfeedarr.length; i++ ){
                                    
                                ANewsFeed[] newrssfeedarr =  new ANewsFeed[v.size()];
                                for ( int i=0; i<v.size(); i++ ){
                                    newrssfeedarr[i] = (ANewsFeed)v.elementAt(i);                 
                                }// for ( int i=0; i<v.size(); i++ ){
                                settingobj.rssfeeds=newrssfeedarr;
                                Utility.saveAppSetting(settingobj);
                            }
                        }catch(Exception ee){}   
                                                                                        }
                }//if ( settingobj.rssfeeds==null ){
            
                boolean hasnewversion = false;
                if ( statusobj!=null ){
                    switch(GuiConst.VERSION_TYPE){
                        case GuiConst.VERSION_TYPE_B:
                            if ( compareVersion(statusobj.version_bold, Const.VERSION)>0 )
                                hasnewversion = true;
                            break;
                        case GuiConst.VERSION_TYPE_G:
                            if ( compareVersion(statusobj.version_gemini, Const.VERSION)>0 )
                                hasnewversion = true;            
                            break;
                        case GuiConst.VERSION_TYPE_S:
                            if ( compareVersion(statusobj.version_storm, Const.VERSION)>0 )
                                hasnewversion = true;            
                            break;
                    }
                }
                
                if ( hasnewversion )
                    loadVersionScreen(statusobj);
                else
                    loadMenuScreen(statusobj);
                    
        }catch(Exception e){
                if ( firsttimeuser==true){
                        text = Lang.ERRORMSG_1stTIMENOCONN1;
                        text2 = Lang.ERRORMSG_1stTIMENOCONN2;
                        text3 = "[code= "+Const.ERRORCODE_1STTIMEUSERERROR+"."+ConnectionMgr.CONNECTIONTYPE+"]";                              
                        invalidate();
                        try{Thread.sleep(5000);}catch(Exception e1){}   
                }   else{
                        text = Lang.ERRORMSG_NOCONN1;
                        text2 = Lang.ERRORMSG_NOCONN2;           
                        text3 = "[code= "+Const.ERRORCODE_EXECEPTION+"."+ConnectionMgr.CONNECTIONTYPE+"]";   
                        invalidate();                 
                        try{Thread.sleep(5000);}catch(Exception e1){}                     
                        loadMenuScreen(null);
                        return;   
                }         
                System.exit(0);                                         
        }
    }

    private int compareVersion (String serverversion, String appversion){
        try{
            int index = serverversion.indexOf(".");
            int majorversion_server = Integer.parseInt(serverversion.substring(0, index));
            int minorversion_server = Integer.parseInt(serverversion.substring(index+1, serverversion.length()));
            
            int index2 = appversion.indexOf(".");
            int majorversion_app= Integer.parseInt(appversion.substring(0, index2));
            int minorversion_app= Integer.parseInt(appversion.substring(index2+1, appversion.length()));     
            if (  majorversion_server>majorversion_app )
                return 1;
            
            if (  majorversion_server<majorversion_app )
                return -1;
    
            if (  minorversion_server>minorversion_app )
                return 1;
                
            if (  minorversion_server<minorversion_app )
                return -1;

        }catch(Exception e){
        }
        return 0;
    }
                
    /*private boolean loadOpeningAds(){
        AdsBannerList bannerlist=FeedReader.loadAdsBannerList();
        if ( bannerlist!=null && bannerlist.openingadsbannerlist !=null && bannerlist.openingadsbannerlist .length>0 ){
            Random ran = new Random(System.currentTimeMillis());
            int ranno = ran.nextInt()%bannerlist.openingadsbannerlist .length;
            if ( ranno<0 )
                ranno=ranno*-1;
            if ( bannerlist.openingadsbannerlist [ranno].image!=null ){
                openingads = Bitmap.createBitmapFromBytes(bannerlist.openingadsbannerlist[ranno].image, 0, -1, 1); 
                if ( openingads.getWidth()>GuiConst.SCREENWIDTH ){
                    int bannerheight =openingads.getHeight()*(GuiConst.SCREENWIDTH)/openingads.getWidth();                     
                    openingads=Utility.resizeBitmap(openingads, GuiConst.SCREENWIDTH, bannerheight);                    
                } 
                if ( openingads.getHeight()>GuiConst.SCREENHEIGHT ){
                    int bannerwidth=openingads.getWidth()*(GuiConst.SCREENHEIGHT)/openingads.getHeight();                     
                    openingads=Utility.resizeBitmap(openingads, bannerwidth, GuiConst.SCREENHEIGHT);                    
                }                 
            }
            return true;
        }else
            return false;
    }*/
    
    private void loadVersionScreen(StatusObj statusobj){
        synchronized(Application.getEventLock() ){
            try{
                Screen s = UiApplication.getUiApplication().getActiveScreen();
                UiApplication.getUiApplication().popScreen(s);
                s.deleteAll();
                VersionScreen versionscreen = new VersionScreen(statusobj);
                UiApplication.getUiApplication().pushScreen(versionscreen);                
            }catch(Exception e){
                //System.out.println("ERROR:loadVersionScreen:"+e);
            }
        }        
    }     
        
    private void loadMenuScreen(StatusObj statusobj){
        synchronized(Application.getEventLock() ){
            try{
                Screen s = UiApplication.getUiApplication().getActiveScreen();
                UiApplication.getUiApplication().popScreen(s);
                s.deleteAll();
                MenuScreen screen = MenuScreen.getInstance();
                //TestListField screen = TestListField.getInstance();
                //screen.refreshAllCategories(statusobj);
                //screen.reinitTab();
                //screen.repaintScreen();
                UiApplication.getUiApplication().pushScreen(screen); 
            }catch(Exception e){
                //System.out.println("ERROR:loadMenuScreen:"+e);
            }
        }        
    }    
} 


