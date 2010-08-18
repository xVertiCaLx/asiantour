package com.og.app.gui;

import com.og.app.gui.listener.*;
import com.og.app.gui.component.*;
import com.og.xml.XmlHelper;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;

public class TablePanel extends VerticalFieldManager{
    private static TablePanel tablePanel = null;
    private MeritListField tableList = null;
    private int fixHeight = 0;
    private ChildNewsPanel childNewsPanel = null;
    
    Bitmap imgUp = null;
    Bitmap imgDown = null;    
    
    private TablePanel (ListFieldListener listener, int fixHeight, int tableNo, int page) {
        super();
        this.fixHeight = fixHeight;
        imgUp = Bitmap.getBitmapResource("res/up.png");
        imgDown = Bitmap.getBitmapResource("res/down.png");
        
        tableList = new MeritListField(this, listener, tableNo, page);
        childNewsPanel = new ChildNewsPanel(fixHeight);
        childNewsPanel.add(tableList);
        add(childNewsPanel);
        loadRows(tableNo);
        
    }
    
    public synchronized static TablePanel getInstance(ListFieldListener listener, int fixHeight, int tableNo, int page) {
        if (tablePanel == null) {
            tablePanel = new TablePanel(listener, fixHeight, tableNo, page);
        }
        return tablePanel;
    }
    
    public void updateLayout(int height) {
        super.updateLayout();
        childNewsPanel.updateLayout(height);
    } 
    
    public void setFocus() {
       tableList.setFocus();
    }
    
    public synchronized void loadRows(int tableNo) {
        tableList.loadTableData(tableNo);
        
        Field field = childNewsPanel.getField(0);
        
        if (tableList.getSize() == 0) {
        
            childNewsPanel.deleteAll();
            String txtNoNews = "There are no available contents for this tab at the moment.";
            if (tableNo == 3) {
            	txtNoNews = "Live Score has no available content at the moment. To view, please try again when there is a match. Thank You.";
            }
            LabelField displayLabel = new LabelField(txtNoNews, Field.FIELD_HCENTER) {
                protected void paintBackground(net.rim.device.api.ui.Graphics g) {
                    g.clear();
                    g.setColor(GuiConst.FONT_COLOR_NONEWS);
                }
            };
            
            displayLabel.setFont(GuiConst.FONT_PLAIN);
            HorizontalFieldManager hfm = new HorizontalFieldManager();
            hfm.add(displayLabel);
            childNewsPanel.add(hfm);
            
        } else {
            if (field instanceof ListField) {
            } else {
                childNewsPanel.deleteAll();
                childNewsPanel.add(tableList);
            
            }
        
        }
        
    }
    
    protected void paint (Graphics g) {

    	super.paint(g);
        
       if (childNewsPanel.getVerticalScroll() > 0) {
        	g.drawBitmap(Display.getWidth()-imgUp.getWidth(), 0, imgUp.getWidth(), imgUp.getHeight(), imgUp, 0,0);   
        }
        if ((childNewsPanel.getVerticalScroll() + fixHeight) < (childNewsPanel.getVirtualHeight())) {
        	g.drawBitmap(Display.getWidth()-imgDown.getWidth(), fixHeight-imgDown.getHeight(), imgDown.getWidth(), imgDown.getHeight(), imgDown, 0,0);
        }
    }
    
    public void reinitThisThing() {
    	tablePanel = null;
    }
    
    //child
    class ChildNewsPanel extends VerticalFieldManager {
        int fixHeight = 0;
        
        public ChildNewsPanel(int fixHeight) {
            super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
            /*GridFieldManager g = new GridFieldManager(6, VERTICAL_SCROLL | VERTICAL_SCROLLBAR);
            this.fixHeight = fixHeight;
            FontFamily fontFamily[] = FontFamily.getFontFamilies(); 
            net.rim.device.api.ui.Font font = fontFamily[1].getFont(FontFamily.CBTF_FONT, 12); 

            for(int i=0; i<4000; i++){
            	LabelField labelField = new LabelField("Test " + i, Field.FOCUSABLE);
            	labelField.select(true);
            	
            	labelField.setFont(GuiConst.FONT_TABLE);//font);
            	g.add(labelField);
            }
            add(g);*/
            
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
