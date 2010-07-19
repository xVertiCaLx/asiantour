package com.og.app.gui;

import com.og.app.gui.listener.*;
import com.og.app.gui.component.*;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;

public class TablePanel extends VerticalFieldManager{
    private static TablePanel tablePanel = null;
    private TableListField tableList = null;
    private int fixHeight = 0;
    private ChildNewsPanel childNewsPanel = null;
    
    Bitmap imgUp = null;
    Bitmap imgDown = null;
    
    private int tableNo = 1;
    
    private TablePanel (ListFieldListener listener, int fixHeight, int tableNo, int page) {
        super();
        this.fixHeight = fixHeight;
        imgUp = Bitmap.getBitmapResource("res/up.png");
        imgDown = Bitmap.getBitmapResource("res/down.png");
        
        /*newsList = new NewsListField(this, listener);
        childNewsPanel = new ChildNewsPanel(fixHeight);
        childNewsPanel.add(newsList);
        add(childNewsPanel);
        loadNews(0);*/
        
        tableList = new TableListField(this, listener, tableNo, page);
        childNewsPanel = new ChildNewsPanel(fixHeight);
        childNewsPanel.add(tableList);
        add(childNewsPanel);
        loadNews(tableNo);
        
    }
    
    public synchronized static TablePanel getInstance(ListFieldListener listener, int fixHeight, int tableNo, int page) {
        if (tablePanel == null)
            tablePanel = new TablePanel(listener, fixHeight, tableNo, page);
        return tablePanel;
    }
    
    public void updateLayout(int height) {
        super.updateLayout();
        childNewsPanel.updateLayout(height);
    } 
    
    public void setFocus() {
           tableList.setFocus();
    }
    
    
    public synchronized void loadNews(int tableNo) {
    
        tableList.loadNews(tableNo);
        
        Field field = childNewsPanel.getField(0);
        
        if (tableList.getSize() == 0) {
        
            childNewsPanel.deleteAll();
            String txtNoNews = "There are no available content for this tab at the moment.";
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
        
            //hasNews = true;
            if (field instanceof ListField) {
            } else {
            
                childNewsPanel.deleteAll();
                childNewsPanel.add(tableList);
            
            }
        
        }
        
    }
    
    protected void paint (Graphics g) {
        super.paint(g);
        //if (listener.isListFieldFocus()) {
            if (childNewsPanel.getVerticalScroll() > 0) {
                g.drawBitmap(Display.getWidth()-imgUp.getWidth(), 0, imgUp.getWidth(), imgUp.getHeight(), imgUp, 0,0);   
            }
            if ((childNewsPanel.getVerticalScroll() + fixHeight) < (childNewsPanel.getVirtualHeight())) {
                g.drawBitmap(Display.getWidth()-imgDown.getWidth(), fixHeight-imgDown.getHeight(), imgDown.getWidth(), imgDown.getHeight(), imgDown, 0,0);
            }
        //}
    }
    
    //child
    class ChildNewsPanel extends VerticalFieldManager {
        int fixHeight = 0;
        
        public ChildNewsPanel(int fixHeight) {
            super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
            this.fixHeight = fixHeight;
            return;
        }
        
        public int getPreferredHeight() {
            return fixHeight;
        }
        
        protected void sublayout(int width, int height) {
            super.sublayout(width, fixHeight);
            setExtent(width, fixHeight);
        }
        
        public void updateLayout(int height) {
            this.fixHeight = height;
            super.updateLayout();
        }
    }
} 
