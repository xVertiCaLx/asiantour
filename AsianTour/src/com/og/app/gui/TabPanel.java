package com.og.app.gui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.og.app.Const;
import com.og.app.gui.component.EmptyTabField;
import com.og.app.gui.component.TabField;
import com.og.app.gui.listener.TabListener;

class TabPanel extends HorizontalFieldManager {
    
    private static TabPanel tabPanel = null;
    private ChildTabPanel childTabPanel = null;
    private TabListener listener = null;
    private TabField tabNext= null;
    private TabField tabPrev = null;
    private EmptyTabField emptyTabField = null;
    
    public Vector tabFields = new Vector();
    private int fixHeight = 0;
    public final static int TABID_NEXT = -10;
    public final static int TABID_PREV = -11;
    
    public static final int TAB_NEWS = 1;
    public static final int TAB_LIVE_SCORE = 2;
    public static final int TAB_TV_SCHEDULE = 3;
    public static final int TAB_TOUR_SCHEDULE = 4;
    public static final int TAB_ORDER_OF_MERIT = 5;
    
    int count = 0;
    
    public synchronized static TabPanel getInstance(TabListener listener){
        if ( tabPanel == null ) {
            tabPanel = new TabPanel(listener);
        }
        return tabPanel;
    }
    
    private TabPanel(TabListener listener) {
        super();        
        tabPanel = this;
        this.listener=listener;
        /*-- START -- for bold & gemini version-- */
        tabNext = new TabField(TABID_NEXT, "", Bitmap.getBitmapResource("res/tab_next.png"), GuiConst.FONT_PLAIN, GuiConst.FONT_PLAIN, Field.NON_FOCUSABLE);
        tabPrev = new TabField(TABID_PREV, "", Bitmap.getBitmapResource("res/tab_prev.png"), GuiConst.FONT_PLAIN, GuiConst.FONT_PLAIN, Field.NON_FOCUSABLE);
        /*-- END -- for bold & gemini version--*/    
        
        /*-- START -- for storm version--   
        tabnext = new TabField  (TABID_NEXT, "", Bitmap.getBitmapResource("res/tab_next.png"), GUIConst.FONT_PLAIN, GUIConst.FONT_PLAIN, true);
        tabprev = new TabField  (TABID_PREV, "", Bitmap.getBitmapResource("res/tab_prev.png"), GUIConst.FONT_PLAIN, GUIConst.FONT_PLAIN, true);
        tabnext.setTabListener(listener);
        tabprev.setTabListener(listener);
        /*-- END -- for storm version--*/       

        fixHeight = tabNext.getPreferredHeight();
        
        int childTabWidth = GuiConst.SCREENWIDTH;
        childTabPanel = new ChildTabPanel(childTabWidth);            
        
        reinitTab();
    }
    
    public void reinitTab() {
        childTabPanel.deleteAll();
        emptyTabField = null;                
        childTabPanel.reinit();
        tabFields.removeAllElements();
        addTabs("News");
        addTabs("Live Score");
        addTabs("TV Schedule");
        addTabs("Tour Schedule");
        addTabs("Order of Merit");
        deleteAll();
        childTabPanel.updateLayout(GuiConst.SCREENWIDTH);
        add(childTabPanel);
        repaintTab();    
    }
    
    private void addTabs(String text) {
        count++;
        try {
            TabField tabField = new TabField(count,text,null,GuiConst.FONT_BOLD,GuiConst.FONT_BOLD_UNDERLINED);//new TabField(count,text,null,GuiConst.FONT_BOLD,GuiConst.FONT_BOLD_UNDERLINED);
            System.out.println("Aloys's 4ID: " + count + ", Name: " + text);
            tabField.setTabListener(listener);
            tabFields.addElement(tabField);
            childTabPanel.add(tabField);    
            childTabPanel.addChildWidth(tabField.getPreferredWidth());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void repaintTab(){
        int horizontalScrollPos = childTabPanel.getHorizontalScroll();

        deleteAll();
        if (childTabPanel.getChildWidth()>this.getPreferredWidth() ){
            if (emptyTabField != null){
                childTabPanel.delete(emptyTabField);
                emptyTabField=null;
            }

            if (horizontalScrollPos > 0 || listener.getSelectedTab() == Const.ID_SETTING) {
                int tabPrevWidth = 0;
                TabField tabField = (TabField)tabFields.elementAt(0);
                if (listener.getSelectedTab() != tabField.getTabID()) {//If the first tab is not selected.
                    tabPrevWidth = tabPrev.getPreferredWidth();
                    add(tabPrev);
                }
                
                TabField lastTabField = (TabField)tabFields.elementAt(tabFields.size()-1);
                if (listener.getSelectedTab() != lastTabField.getTabID()) {//If the last tab is not selected                
                    childTabPanel.updateLayout(GuiConst.SCREENWIDTH - tabNext.getPreferredWidth()-tabPrevWidth);
                    add(childTabPanel);
                    add(tabNext);
                   // childTabPanel.setHorizontalScroll(position)
                } else {
                    childTabPanel.updateLayout(GuiConst.SCREENWIDTH - tabNext.getPreferredWidth());
                    add(childTabPanel);
                }
            } else {
                if (childTabPanel.getChildWidth() - horizontalScrollPos > this.getPreferredWidth()){
                    childTabPanel.updateLayout(GuiConst.SCREENWIDTH - tabNext.getPreferredWidth());
                     add(childTabPanel);
                      add(tabNext);
                } else {
                    childTabPanel.updateLayout(GuiConst.SCREENWIDTH);
                    add(childTabPanel);
                }
            }
        } else {
            int tabPanelWidth = childTabPanel.getChildWidth();
            int emptyTabWidth = GuiConst.SCREENWIDTH - tabPanelWidth;

            if (emptyTabField == null){
                emptyTabField = new  EmptyTabField(emptyTabWidth, false);
                childTabPanel.add(emptyTabField);
            }
            childTabPanel.updateLayout(GuiConst.SCREENWIDTH);
            add(childTabPanel);
        }
    }
    
    public int getPreferredHeight() {
        return fixHeight ;
    }
        
    public int getPreferredWidth() {
        return GuiConst.SCREENWIDTH;
    }
    
    protected void sublayout(int width, int height)
    {
        super.sublayout(GuiConst.SCREENWIDTH,fixHeight);
        setExtent(GuiConst.SCREENWIDTH,fixHeight);
    }
    
    public void setTab(int index){
        if (tabFields.size() > index) {
            TabField tabField = (TabField)tabFields.elementAt(index);
            listener.setSelectedTab(tabField.getTabID());    
            System.out.println("setTab("+index+")");
        }
    }
    
    protected synchronized boolean navigationMovement(int dx, int dy, int status, int time) {
        if (dy < 0) {
            return true;
        }
        
        if (dy > 0){
            if (listener.isSelectedTabHasNews() == false){
                return true;
            }
            listener.setTabOnFocus(false);
            return super.navigationMovement(dx, dy, status, time);
        }
        
        if (dx > 0) {
            nextTab();
        } else if (dx < 0) {
            prevTab();       
        }
        listener.drawBottomPanel();
        return true;
    }
    
    public void nextTab() {
        int tempIndex = 0;
        int selectedTab = listener.getSelectedTab();
        
        for (int i=0; i<tabFields.size(); i++) {
            TabField tabField = (TabField)tabFields.elementAt(i);
            if (selectedTab == tabField.getTabID()) {
                tempIndex=i;
                break;
            }
        }              

        tempIndex++;
        if (tempIndex >= tabFields.size()){
            return;
        }
        TabField tabField = (TabField)tabFields.elementAt(tempIndex);
        System.out.println("nextTab():setSelectedTab("+tabField.getTabID()+")");
        listener.setSelectedTab(tabField.getTabID());         
        tabField.setFocus();         
        repaintTab();     
        tabField.setFocus();
    }
    
    public void prevTab(){
        int tempIndex = 0;
        int selectedTab = listener.getSelectedTab();
        for (int i=0; i<tabFields.size(); i++) {
            TabField tabField = (TabField)tabFields.elementAt(i);
            if (selectedTab == tabField.getTabID()){
                tempIndex=i;
                break;
            }
        }              

        tempIndex--;
        if ( tempIndex<0 ){
        	return;
        }
        TabField tabField = (TabField)tabFields.elementAt(tempIndex);
        listener.setSelectedTab(tabField.getTabID());
        System.out.println("prevTab():setSelectedTab("+tabField.getTabID()+")");
        tabField.setFocus();  
        repaintTab();
        tabField.setFocus();    
    }
    
    public void setTabFocus(int id){
    		System.out.println("TabPanel:setTabFocus("+id+")");
            for (int i=0; i<tabFields.size(); i++) {
                TabField tabField = (TabField)tabFields.elementAt(i);
                if ( tabField.getTabID() == id ) {
                    tabField.setFocus();
                    repaintTab();    
                    tabField.setFocus();     
                    break;
                }
            }
    }
    
    //---------------------------- ChildTabPanel ------------------------------
    
    class ChildTabPanel extends HorizontalFieldManager {
        int fixwidth = 0;
        int totalchildwidth = 0;
        public ChildTabPanel(int width) {
            super(Manager.HORIZONTAL_SCROLL | Manager.HORIZONTAL_SCROLLBAR);
            this.fixwidth = width;
        }
        
        public void reinit() {
            totalchildwidth = 0;
        }

        public int getChildWidth() {
            return totalchildwidth ;
        }        
        public void addChildWidth(int childwidth) {
            totalchildwidth = totalchildwidth +childwidth;
        }
                
        public void updateLayout(int width) {
            this.fixwidth = width;
            super.updateLayout();
        }
        
        public int getPreferredWidth() {
            return fixwidth;
        }
        
        protected void sublayout(int width, int height) {
            super.sublayout(fixwidth, height);
            setExtent(fixwidth, height);
        }        
    }
} 
