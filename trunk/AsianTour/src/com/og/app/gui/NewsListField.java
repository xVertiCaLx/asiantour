package com.og.app.gui;

import com.og.app.gui.listener.*;
import com.og.rss.*;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;

public class NewsListField extends CustomListField {
    private NewsPanel newsPanel = null;
    private String feedname = "";
    int newsID = 0;
    ANewsItemObj[] item = new ANewsItemObj[5];
    
    public NewsListField(NewsPanel newsPanel, ListFieldListener listener) {
        super(listener);
        this.newsPanel = newsPanel;
    }
    
    protected void onFocus(int direction) {
        newsPanel.invalidate();
    }
    
    protected synchronized boolean navigationMovement(int dx, int dy, int status, int time) {
        
        if ( dy<0 ){
             int tmpy = dy*-1;
             if ( getSelectedIndex()-tmpy<0 ){
                if ( getSelectedIndex()>0 ){
                    do{
                        setSelectedIndex(getSelectedIndex()-1);
                        tmpy--;
                    }while(tmpy>0 );
                }
                listener.onListFieldUnfocus();
                newsPanel.invalidate();        
                return true;         
             }
         }     
        newsPanel.invalidate();
        return false;
    }
    
    public boolean navigationClick(int status, int time) {
        if(getSize() > 0)
        {
                 try{
                    synchronized(Application.getEventLock() ){
                        ANewsItemObj ni = (ANewsItemObj)_elements.elementAt(getSelectedIndex());
                        Screen s = UiApplication.getUiApplication().getActiveScreen();
                        ni.index=getSelectedIndex();
                        UiApplication.getUiApplication().pushScreen(new NewsDetailScreen(this, ni));
                    }
                    return true;
                }catch(Exception e){
                    //System.out.println(e);
                }              
        }
        return false;
    }
    
    public void loadNews(int newsID) {
        setRowHeight();
        
        
        synchronized(lock) {
            this.newsID = newsID;
            
            removeAll();
            for (int i = 0; i < 5; i++) {
                System.out.println("aloy.NewsListField.loadNews: " + i);
                item[i] = new ANewsItemObj("Test news "+i, "Aloysius Ong", " really really long string that is more than 11 character 5mins ago", "Some very long description or news content that must store more than 50 characters in order to try and let it show the triple dots which we have created earlier. So folks, this is news " + i + ".", "1");
                
                add(item[i]);
            }
            
        }
    }
    
}
