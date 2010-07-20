package com.og.app.gui;

import javax.microedition.lcdui.Font;

import com.og.app.gui.listener.*;
import com.og.rss.*;
import com.og.xml.XmlHelper;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.component.*;

public class MenuScreen extends MainScreen implements TabListener, ListFieldListener {
    
    public static MenuScreen thisInstance = null;
    
    private int selectedTab = 1;
    private boolean isTabFocus = true;
    
    private LogoPanel logoPanel = null;
    private TabPanel tabPanel = null;
    private NewsPanel newsPanel = null;
    private TablePanel tablePanel = null;
    private ARssDB rssDB = null;
        
    //--------------------------- Single Turn Controll --------------------------------
    public synchronized static MenuScreen getInstance() {
        if (thisInstance == null) { 
            thisInstance = new MenuScreen();
        }
        return thisInstance;
    }   
    
    public MenuScreen() {
        super();
        GuiConst.reinitFont();
        fieldInit();
        //LabelField lblTitle = new LabelField("Asian Tour", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        //setTitle(lblTitle);
        XmlHelper.downloadNews();
    }
    
    public void fieldInit() {
        logoPanel = LogoPanel.getInstance();
        tabPanel = TabPanel.getInstance(this);
        
        GuiConst.LOGOPANEL_HEIGHT=logoPanel.getPreferredHeight();
        GuiConst.TABPANEL_HEIGHT=tabPanel.getPreferredHeight();
        
        newsPanel = NewsPanel.getInstance(this, Display.getHeight()-GuiConst.TABPANEL_HEIGHT-GuiConst.LOGOPANEL_HEIGHT);
        tablePanel = TablePanel.getInstance(this, Display.getHeight()-GuiConst.TABPANEL_HEIGHT-GuiConst.LOGOPANEL_HEIGHT, 1,1);
        
        System.out.println("Display height is: " + Display.getHeight() + "TABPANEL: " + GuiConst.TABPANEL_HEIGHT + "LOGOPANEL: " + GuiConst.LOGOPANEL_HEIGHT); 
        add(logoPanel);
        add(tabPanel);
        //add(newsPanel);
        add(tablePanel);
        
    }
    
    public boolean onClose() {
        if(Dialog.ask(Dialog.D_YES_NO, "Do you want to exit?") == Dialog.YES)
        {
            System.out.println("aloy.endapp");
            clearResource();
            System.exit(0);
        }
        return true;
    }
    
    public void clearResource() {       
        tabPanel = null;        
        logoPanel = null;
        newsPanel = null;
    }
    
    public void repaintTab(){
        tabPanel.repaintTab();
    }
    
    public int getSelectedTab(){
        return selectedTab;
    }
    
    public void setSelectedTab(int index) {
        int previndex = selectedTab;
        
        if (index == TabPanel.TABID_NEXT) {
            //showNextCategory();
            return;
        } else if (index == TabPanel.TABID_PREV) {
            //showPrevCategory();
            return;
        } else {
            this.selectedTab = index;
        }
        
        if (previndex == 9) {
            delete(newsPanel);
        } else if (previndex == 10) {
            delete(tablePanel);
        }
        
        if (index == 9) {
            add(newsPanel);
        } else if (index == 10) {
            add(tablePanel);
        }
    
        //setting tab
        /*if ( previndex==Const.ID_SETTING)
            delete(newscategorypanel);
        else if ( previndex==Const.ID_PHOTOGALLERY)
            delete(photopanel);        
        else
            delete(newspanel);
            
        if ( index==Const.ID_SETTING){
                newscategorypanel = new NewsCategoryPanel(rssdb, newspanel.getPreferredHeight(),this);
                add(newscategorypanel);
        }else if ( index==Const.ID_PHOTOGALLERY){
                this.removeAllMenuItems();
                addMenuItems();                            
                add(photopanel);        
        }else{
                this.removeAllMenuItems();
                addMenuItems();                
                add(newspanel);                        
        }
        
        drawBottomPanel();    */
        
    }
    
    public void setTabOnFocus(boolean isFocus){
         isTabFocus = isFocus;
    }    
    public boolean isTabOnFocus(){
         return isTabFocus;
    }
    
    public void reinitTab(){
        tabPanel.reinitTab();
        tabPanel.setTabFocus(selectedTab);
    }
    
    public void repaintScreen(){
        //this.removeAllMenuItems();
        //addMenuItems();
        
        logoPanel.invalidate();
        setSelectedTab(9);//this.selectedTab);
        tabPanel.reinitTab();      
        
        if (this.selectedTab == 9) {
            setSelectedTab(9);
        } else if (this.selectedTab == 10) {
            setSelectedTab(10);
        }
        
        //newsPanel.loadNews(selectedTab);          
        /*        
        if ( this.selectedTab==Const.ID_SETTING ){
            delete(newscategorypanel);
            newscategorypanel = new NewsCategoryPanel(rssdb, newspanel.getPreferredHeight(),this);
            add(newscategorypanel);
        }else if ( this.selectedTab==Const.ID_PHOTOGALLERY ){
            setSelectedTab(Const.ID_PHOTOGALLERY );
        }else
            newsPanel.loadNews(selectedTab);
        */
    }
    
    public void drawBottomPanel(){
    	
            /*if ( selectedTab==9)
                //photopanel.loadPhoto(selectedTab);    
                newsPanel.loadNews(selectedTab);          
            else if ( selectedTab==10 )
                newsPanel.loadNews(selectedTab);  */      
    }
    
    public boolean isSelectedTabHasNews(){
        /*if ( selectedTab==Const.ID_SETTING){
                return true;    
        }else if ( selectedTab==Const.ID_PHOTOGALLERY){
               return photopanel.hasPhoto();     
        }    
        return newspanel.hasNews();*/
        return false;
    }
    
    
    //-----------------------------------------------------------------------------------------------------    
//START -- method for ListFieldListener
    public void onListFieldUnfocus(){
        //System.out.println("onListFieldUnfocus:tabSelected:"+selectedTab);
        tabPanel.setTabFocus(selectedTab);
        isTabFocus=true;
    }
    public boolean isListFieldFocus(){
        if ( isTabFocus )
            return false;
        return true;
    }     
//END -- method for ListFieldListener
    
    
    /*--------------------------- Example of making a Horizontal Field Manager ----------------
    
        //--------------------------- Panel Manager Initialization ----------------------------
        HorizontalFieldManager tabPanel = new HorizontalFieldManager();
        
        //--------------------------- Adding it to the screen ---------------------------------
        add(tabPanel);
        
        //--------------------------- Horizontal Tab Manager ----------------------------------
        BitmapField TAB_BG_LEFT = new BitmapField(selectedTabLeft);
        BitmapField TAB_BG_MIDDLE = new BitmapField(selectedTabMiddle, BitmapField.ELLIPSIS |  BitmapField.USE_ALL_WIDTH);
        BitmapField TAB_BG_RIGHT = new BitmapField(selectedTabRight);
        
        tabPanel.add(TAB_BG_LEFT);
        tabPanel.add(TAB_BG_RIGHT);
        
        --------------------------- End of Horizontal Field Manager Example -------------------*/
} 
