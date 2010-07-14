package com.og.app.gui;

import com.og.app.gui.listener.*;
import com.og.app.gui.component.*;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;

public class NewsPanel extends VerticalFieldManager {
    
    private static NewsPanel newsPanel = null;
    private NewsListField newsList = null;
    private int fixHeight = 0;
    private ChildNewsPanel childNewsPanel = null;
    
    Bitmap imgUp = null;
    Bitmap imgDown = null;
    ListFieldListener listener = null;
    
    private int newsID = -1;
    private boolean hasNews = false;
    
    private NewsPanel (ListFieldListener listener, int fixHeight) {
        super();
        this.fixHeight = fixHeight;
        this.listener = listener;
        imgUp = Bitmap.getBitmapResource("res/up.png");
        imgDown = Bitmap.getBitmapResource("res/down.png");
        
        newsList = new NewsListField(this, listener);
        loadNews(0);
        childNewsPanel = new ChildNewsPanel(fixHeight);
        
        
        
        childNewsPanel.add(newsList);
        add(childNewsPanel);
    }
    
    public synchronized static NewsPanel getInstance(ListFieldListener listener, int fixHeight) {
        if (newsPanel ==null)
            newsPanel  = new NewsPanel (listener, fixHeight);
        return newsPanel;
    }
    
    public void updateLayout(int height) {
        super.updateLayout();
        childNewsPanel.updateLayout(height);
    }   
    
    public void setFocus() {
           newsList.setFocus();
    }
    public boolean hasNews(){
        return  hasNews;
    }
    
    public synchronized void loadNews(int newsID) {
        this.newsID = newsID;
        newsList.loadNews(newsID);
        
        Field field = childNewsPanel.getField(0);
        
        if (newsList.getSize() == 0) {
            childNewsPanel.deleteAll();
            String txtNoNews = "There are no available content at the moment.";
            
            LabelField displayLabel = new LabelField(txtNoNews, Field.FIELD_HCENTER) {
                protected void paintBackground(net.rim.device.api.ui.Graphics g) {
                    g.clear();
                    g.setColor(GuiConst.FONT_COLOR_NONEWS);
                }
            };
            
            displayLabel.setFont(GuiConst.FONT_PLAIN);
            HorizontalFieldManager hfm = new HorizontalFieldManager();
            hfm.add(new SpaceField(3));
            hfm.add(displayLabel);
            hfm.add(new SpaceField(3));
            childNewsPanel.add(hfm);
        } else {
            hasNews = true;
            if (field instanceof ListField) {
                
            } else {
                childNewsPanel.deleteAll();
                childNewsPanel.add(newsList);
            }
        }
    }
    
    protected void paint (Graphics g) {
        super.paint(g);
        if (listener.isListFieldFocus()) {
            if (childNewsPanel.getVerticalScroll() > 0) {
                g.drawBitmap(Display.getWidth()-imgUp.getWidth(), 0, imgUp.getWidth(), imgUp.getHeight(), imgUp, 0,0);   
            }
            if ((childNewsPanel.getVerticalScroll() + fixHeight) < (childNewsPanel.getVirtualHeight())) {
                g.drawBitmap(Display.getWidth()-imgDown.getWidth(), fixHeight-imgDown.getHeight(), imgDown.getWidth(), imgDown.getHeight(), imgDown, 0,0);
            }
        }
    }
    
    //---------------------------- ChildTabPanel ------------------------------
    class ChildNewsPanel extends VerticalFieldManager {
        int fixheight = 0;
        
        public ChildNewsPanel(int fixheight) {
            super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
            this.fixheight=fixheight;
            return;
        }   

        public int getPreferredHeight() {
            return fixheight;
        }
        
        protected void sublayout(int width, int height) {
            super.sublayout(width, fixheight);
            setExtent(width, fixheight);
        }        
        
        public void updateLayout(int height) {
            this.fixheight=height;
            super.updateLayout();
        }        
    }
} 
