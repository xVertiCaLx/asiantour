package com.og.app.gui;

import com.og.rss.*;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;
import com.og.app.gui.GuiConst;
import com.og.app.gui.listener.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

import java.io.IOException;
import java.util.*;

public class CustomListField extends ListField implements ListFieldCallback, Runnable {
    protected Vector _pelements = new Vector();
    protected Vector _elements = new Vector();
    protected Vector imgLoadingList = new Vector();
    
    protected Bitmap news_bg_selected = null;
    protected Bitmap news_bg_notSelected = null;
    protected Bitmap news_thumbnail = null;
    
    protected int thumbnail_leftRightSpacing = 2;
    protected int dateTopSpacing = 1;
    protected int rowTopSpacing = 1;
    protected int rowBottomSpacing = 1;
    protected int textSpacing = 1;
    protected int previewHeight = -1;
    protected int previewWidth = -1;
    protected int newsID = 0;
    
    //protected Thread mythread = null;
    protected Object lock = new Object();
    protected ListFieldListener listener = null;
    
    public CustomListField(ListFieldListener listener) {
        super();
        this.listener = listener;
        setEmptyString("", DrawStyle.HCENTER);
        setCallback(this);
        if (GuiConst.SCREENWIDTH > 320) {
            news_bg_selected = Bitmap.getBitmapResource("res/news_list_selected_65.png");
            news_bg_notSelected = Bitmap.getBitmapResource("res/news_list_65.png"); 
            news_thumbnail = Bitmap.getBitmapResource("res/previewnews2.png");
            previewWidth = 65;
            previewHeight = 65;
            rowTopSpacing = 15;
        } else {
            previewWidth = 50;
            previewHeight = 50;
            news_bg_selected = Bitmap.getBitmapResource("res/news_list_selected_50.png");
            news_bg_notSelected = Bitmap.getBitmapResource("res/news_list_50.png"); 
            news_thumbnail = Bitmap.getBitmapResource("res/previewnews.png");
            rowTopSpacing = 5;
        }
        setRowHeight();
        
    }
    
    protected void setRowHeight() {
        
        System.out.println("did it come in the set row height method?");
        setRowHeight(rowTopSpacing+GuiConst.FONT_BOLD.getHeight()*2+GuiConst.FONT_PLAIN.getHeight()*2+textSpacing*3+dateTopSpacing+GuiConst.FONT_DATE.getHeight());//+rowBottomSpacing);
        System.out.println(getRowHeight());
        if (getRowHeight() < previewHeight) {
            this.setRowHeight(previewHeight+3);
            System.out.println("previewHeight: " + previewHeight);
        }
        
        if ((getRowHeight() != news_bg_selected.getHeight()) || (this.getPreferredWidth() != news_bg_selected.getWidth())) {
            
            if ((getRowHeight() == 104) || (getRowHeight() == 103)) {
                news_bg_selected = Bitmap.getBitmapResource("res/news_list_selected_104.png");
                news_bg_notSelected = Bitmap.getBitmapResource("res/news_list_104.png");
                news_bg_selected = Utility.resizeBitmap(news_bg_selected, this.getPreferredWidth(), getRowHeight());
                news_bg_notSelected = Utility.resizeBitmap(news_bg_notSelected, this.getPreferredWidth(), getRowHeight());
            } else {
                news_bg_selected = Utility.resizeBitmap(news_bg_selected, this.getPreferredWidth(), getRowHeight());
                news_bg_notSelected = Utility.resizeBitmap(news_bg_notSelected, this.getPreferredWidth(), getRowHeight());
            }
        }
    }
    
    public void onUnfocus() {
    }
    
    public void run() {
        //if there are thumbnails from the rss
    }
    
    public Vector getAllElements() {
        return _elements;
    }
    
    public int getSize() {
        return _elements.size();
    }
    
    public void addElements(ANewsItemObj element) {
        add(element);
    }
    
    protected void add(ANewsItemObj element) {
        synchronized(lock) {
            _elements.addElement(element);
            setSize(getSize());
        }
    }
    
    public void drawListRow(ListField listField, final Graphics g, int index, int y, int width) {
        if (index < getSize()) {
            int rowHeight = getRowHeight();
            if (index == this.getSelectedIndex() && listener.isListFieldFocus()) {
                System.out.println("aloy.CustomListField(drawListRow index==this.getselectedIndex && listener.isListFieldFocus).info: this news item is in focus");
                //since this field is being selected, draw the bg using news_bg_selected
                g.drawBitmap(0, y, news_bg_selected.getWidth(), news_bg_selected.getHeight(), news_bg_selected, 0,0);
                g.setColor(GuiConst.FONT_COLOR_NEWSSELECTED);
            } else {
                System.out.println("aloy.CustomListField(drawListRow index==this.getselectedIndex && listener.isListFieldFocus).info: this news item is not in focus");
                //g.drawBitmap(0,y, news_bg_notSelected.getWidth(), news_bg_notSelected.getHeight(), news_bg_notSelected, 0, 0);
            }
            
            ANewsItemObj ni = (ANewsItemObj)_elements.elementAt(index);
            
            //if no image to load {
            int imgy = y + (rowHeight - previewHeight)+5;///2;
            if ((rowHeight - previewHeight)/2 > rowTopSpacing) {
                imgy = y + rowTopSpacing + 1;
            }
            //} else draw the thumbnail from rss {}
            g.drawBitmap(3, imgy, previewWidth, previewHeight, news_thumbnail, 0, 0);
            
            //getting output
            Font textFont = GuiConst.FONT_BOLD;
            if (ni.hasread) {
                g.setColor(GuiConst.FONT_COLOR_NEWSREAD);
            }
            
            String text = ni.title;
            g.setFont(textFont);
            Vector vText = Utility.breakIntoWords(ni.title);
            //if there are news thumbnail, int tmpx = thumbnail_leftRightSpacing;
            int tmpx = thumbnail_leftRightSpacing + news_thumbnail.getWidth() + thumbnail_leftRightSpacing;
            
            int defaulttmpx = tmpx;
            int tmpy = y + rowTopSpacing - 2;
            int lineno = 1;
            for (int i = 0; i < vText.size(); i++) {
                if (lineno >2) {
                    break;
                }
                String strtemp = (String)vText.elementAt(i);
                int wordWidth = GuiConst.FONT_BOLD.getAdvance(strtemp+ " ");
                if (tmpx+wordWidth >= this.getWidth()) {
                    // if title is longer than screen width, cut the rest of the string and change to "..."
                    if (lineno == 2) {
                        strtemp = "...";
                    } else {
                        tmpy = tmpy + GuiConst.FONT_BOLD.getHeight()+textSpacing;
                    }
                    lineno++;
                }
                g.drawText (strtemp+ " ", tmpx, tmpy);
                tmpx = tmpx+wordWidth;
            }
            
            g.setFont(GuiConst.FONT_PLAIN);
            //this will truncate the news content to 50 char
            vText = Utility.breakIntoWordsSpecial(ni.description, 50);
            tmpy = tmpy+GuiConst.FONT_BOLD.getHeight()+textSpacing;
            tmpx = defaulttmpx;
            lineno = 1;
            for (int i = 0; i < vText.size(); i++) {
                if (lineno > 2) {
                    break;
                }
                
                String strtemp = (String)vText.elementAt(i);
                int wordWidth = GuiConst.FONT_PLAIN.getAdvance(strtemp + " ");
                if ((tmpx + wordWidth >= this.getWidth()) || ((lineno == 2) && (tmpx+wordWidth >= this.getWidth()*75/100))) {
                    if (lineno == 2) {
                        strtemp = "...";
                    } else {
                        tmpy = tmpy + GuiConst.FONT_PLAIN.getHeight()+textSpacing;
                        tmpx = defaulttmpx;
                    }
                    lineno++;
                }
                
                g.drawText(strtemp + " ", tmpx, tmpy);
                tmpx = tmpx+ wordWidth;
            }
            
            String strdate = ni.pubDate;
            g.setFont(GuiConst.FONT_DATE);
            g.drawText(strdate, defaulttmpx, tmpy+textFont.getHeight()+dateTopSpacing);
        } 
    }
    
    protected boolean navigationMovement(int dx,
                                     int dy,
                                     int status,
                                     int time) {

         //System.out.println("navigationMovement:"+dx+","+dy+","+status+","+time);        
         invalidate();
         return false;
    }
    
    public Object get(ListField listField, int index) {
        if ((index >= 0) && (index < getSize())) {
            return _elements.elementAt(index);
        }
        return null;
    }
    
    public int getPreferredWidth(ListField listField) {
        return Display.getWidth();
    }
    
    public int indexOfList(ListField listField, String prefix, int start) {
        return listField.indexOfList(prefix, start);
    }
    
    public boolean keyChar(char key, int status, int time) {
        if (getSize() > 0 && key == Characters.SPACE) {
            getScreen().scroll(Manager.DOWNWARD);
            return true;
        }
        return super.keyChar(key, status, time);
    }
    
    public synchronized void saveChanges(ANewsItemObj ni, int index) {
        try {
            //to save news as loaded/read
            System.out.println("saveeeededdd");
        } catch (Exception e) {
            System.out.println("aloy.CustomListField.exceptione.saveChanges(ni, index):"+e);
        }
    }
    
    public boolean navigationClick(int status, int time) {
        System.out.println("aloy.CustomListField.navigationClick: got enter or not?");
        if (getSize() > 0) {
            try {
                synchronized(Application.getEventLock()) {
                    ANewsItemObj ni = (ANewsItemObj)_elements.elementAt(getSelectedIndex());
                    Screen s = UiApplication.getUiApplication().getActiveScreen();
                    ni.index = getSelectedIndex();
                    UiApplication.getUiApplication().pushScreen(new NewsDetailScreen(this, ni));
                }
                return true;
            } catch (Exception e) {
                System.out.println("aloy.CustomListField.navigationClick.exceptione: " + e);
            }
        }
        
        return false;
    }
    
    public void remove(int index) {
        synchronized(lock){
            _elements.removeElementAt(index);
            _pelements.removeElementAt(index);
            setSize(getSize());
        }
        invalidate();
        
    }
        
    protected void removeAll() {
        synchronized(lock){
            _elements.removeAllElements();
            _pelements.removeAllElements();
            setSize(0);
        }
        invalidate();
    }
} 
