package com.og.app.gui;

import java.util.Vector;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.Dialog;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.component.TabField;
import com.og.app.gui.component.TransitionableMainScreen;
import com.og.app.gui.listener.ImageButtonListener;
import com.og.app.gui.listener.ListFieldListener;
import com.og.app.gui.listener.TabListener;
import com.og.xml.XmlHelper;

public class MenuScreen extends TransitionableMainScreen implements TabListener, ListFieldListener {

	//Vector<ANewsItemObj>
	public Vector newsCollection = new Vector();
	
	//Vector<DataCentre>
	public Vector tvTimesCollection = new Vector();
	
	//Vector<DataCentre>
	public Vector tourScheduleCollection = new Vector();

	public static MenuScreen thisInstance = null;

	public int selectedTab;
	private boolean isTabFocus = true;

	private LogoPanel logoPanel = null;
	private TabPanel tabPanel = null;
	private TablePanel tablePanel = null;
	
	public NewsPanel newsPanel = null;
	
	//--------------------------- Single Turn Control --------------------------------
	public synchronized static MenuScreen getInstance() {
		if (thisInstance == null) { 
			thisInstance = new MenuScreen();
		}
		return thisInstance;
	}   

	public MenuScreen() {
		super();
		selectedTab = 1;
		thisInstance = this;
		GuiConst.reinitFont();
		fieldInit(0);
		fadeToAndRun(Graphics.BLACK /* color to fade to */, 800 /* fade duration */);
		System.out.println("-----------------------------------------------------");
		System.out.println("Display height is: " + GuiConst.SCREENHEIGHT + "TABPANEL: " + GuiConst.TABPANEL_HEIGHT + "LOGOPANEL: " + GuiConst.LOGOPANEL_HEIGHT); 
		System.out.println("-----------------------------------------------------");
		
		add(logoPanel);
		add(tabPanel);
		add(newsPanel);

		XmlHelper.downloadNews();
		XmlHelper.downloadTvTimes();
    	XmlHelper.downloadTourSchedule();

		newsCollection = RecordStoreHelper.getNewsCollection();
		
		
	}

	public void fieldInit(int tableNo) {
		logoPanel = LogoPanel.getInstance();
		tabPanel = TabPanel.getInstance(this);

		GuiConst.LOGOPANEL_HEIGHT=logoPanel.getPreferredHeight();
		GuiConst.TABPANEL_HEIGHT=tabPanel.getPreferredHeight();

		newsPanel = NewsPanel.getInstance(this, GuiConst.SCREENHEIGHT-GuiConst.TABPANEL_HEIGHT-GuiConst.LOGOPANEL_HEIGHT);
		tablePanel = TablePanel.getInstance(this, GuiConst.SCREENHEIGHT-GuiConst.TABPANEL_HEIGHT-GuiConst.LOGOPANEL_HEIGHT, tableNo,1);

	}

	public boolean onClose() {
		if(Dialog.ask(Dialog.D_YES_NO, "Do you want to exit?") == Dialog.YES)
		{
			System.out.println("aloy.endapp");
			RecordStoreHelper.setNewsCollection(newsCollection);
			clearResource();
			System.exit(0);
		}
		return true;
	}

	public void clearResource() { 
		tabPanel = null;        
		logoPanel = null;
		newsPanel = null;
		tablePanel = null;
	}

	public void repainteverything() {
		this.deleteAll();
		tablePanel.reinitThisThing();
	}

	public void repaintTab(){
		tabPanel.repaintTab();
	}

	public int getSelectedTab(){
		return selectedTab;
	}

	public void setSelectedTab(int tabID) {
		selectedTab = tabID;
		Vector tabFields = TabPanel.getInstance(this).tabFields;
		for (int i = 0; i < tabFields.size(); i++) {
			TabField tabField = (TabField)tabFields.elementAt(i);
			if(tabID==tabField.getTabID()){
				tabField.setFocus();
				System.out.println("MenuScreen:setSelectedTab("+tabID+")");
			}
		}
	}

	public void setTabOnFocus(boolean isFocus){
		isTabFocus = isFocus;
	}    
	public boolean isTabOnFocus(){
		return isTabFocus;
	}

	public void reinitTab(){
		tabPanel.setTabFocus(selectedTab);
	}

	public void repaintScreen(){
		//this.removeAllMenuItems();
		//addMenuItems();
		logoPanel.invalidate();
	}

	public void drawBottomPanel(){
		switch (selectedTab) {
		case TabPanel.TAB_NEWS:
			showNewsTab();
			break;
		case TabPanel.TAB_LIVE_SCORE:
			showLiveScoreTab();
			break;
		case TabPanel.TAB_TV_SCHEDULE:		
			showTVScheduleTab();
			break;
		case TabPanel.TAB_TOUR_SCHEDULE:	
			showTourScheduleTab();
			break;
		case TabPanel.TAB_ORDER_OF_MERIT:	
			showOOMTab();
			break;
		}
	}

	public boolean isSelectedTabHasNews(){
		/*if ( selectedTab==Const.ID_SETTING){
                return true;    
        }else if ( selectedTab==Const.ID_PHOTOGALLERY){
               return photopanel.hasPhoto();     
        }    
        return newspanel.hasNews();*/
		return true;
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



	private void showNewsTab(){
		logoPanel.repaintLogoPanel("default");
		repainteverything();
		fieldInit(0);
		newsPanel.loadNews(0);
		NewsPanel.newsPanel.newsList.setSize(newsCollection.size());
		logoPanel.repaintLogoPanel("default");
		add(logoPanel);
		add(tabPanel);
		add(newsPanel);
		
	}

	private void showLiveScoreTab(){
		logoPanel.repaintLogoPanel("titlebar_livescore");
		repainteverything();
		fieldInit(3);
		logoPanel.repaintLogoPanel("titlebar_livescore");
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
	}
	
	private void showOOMTab(){
		logoPanel.repaintLogoPanel("titlebar_orderofmerit");
		repainteverything();
		logoPanel.repaintLogoPanel("titlebar_orderofmerit");
		fieldInit(4);
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
	}
	
	private void showTVScheduleTab(){
		logoPanel.repaintLogoPanel("titlebar_tvschedule");
		repainteverything();
		logoPanel.repaintLogoPanel("titlebar_tvschedule");
		fieldInit(1);
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
	}

	private void showTourScheduleTab(){
		repainteverything();
		logoPanel.repaintLogoPanel("titlebar_tourschedule");
		fieldInit(2);
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
	}

	/*--------------------------- Example of making a Horizontal Field Manager ----------------

        //--------------------------- Panel Manager Initialisation ----------------------------
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
