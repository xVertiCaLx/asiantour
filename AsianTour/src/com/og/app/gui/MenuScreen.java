package com.og.app.gui;

import java.util.Vector;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.component.TabField;
import com.og.app.gui.listener.ListFieldListener;
import com.og.app.gui.listener.TabListener;
import com.og.xml.XmlHelper;

public class MenuScreen extends MainScreen implements TabListener, ListFieldListener, FocusChangeListener {

	//Vector<ANewsItemObj>
	public Vector newsCollection = new Vector();
	public Vector tvTimesCollection = new Vector();

	public static MenuScreen thisInstance = null;

	private int selectedTab = 1;
	private int prevSelectedTab = 0;
	private boolean isTabFocus = true;

	private LogoPanel logoPanel = null;
	private TabPanel tabPanel = null;

	public NewsPanel newsPanel = null;
	private TablePanel tablePanel = null;

	private boolean isFocusEventBlocked = false;
	//--------------------------- Single Turn Controll --------------------------------
	public synchronized static MenuScreen getInstance() {
		if (thisInstance == null) { 
			thisInstance = new MenuScreen();
		}
		return thisInstance;
	}   

	public MenuScreen() {
		super();
		thisInstance = this;
		GuiConst.reinitFont();
		fieldInit(1);
		System.out.println("-----------------------------------------------------");
		System.out.println("Display height is: " + GuiConst.SCREENHEIGHT + "TABPANEL: " + GuiConst.TABPANEL_HEIGHT + "LOGOPANEL: " + GuiConst.LOGOPANEL_HEIGHT); 
		System.out.println("-----------------------------------------------------");
		add(logoPanel);
		add(tabPanel);
		add(newsPanel);
		//LabelField lblTitle = new LabelField("Asian Tour", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
		//setTitle(lblTitle);
		newsCollection = RecordStoreHelper.getNewsCollection();
		XmlHelper.downloadNews();
		XmlHelper.downloadTvTimes();
	}

	public void fieldInit(int tableNo) {
		logoPanel = LogoPanel.getInstance();
		tabPanel = TabPanel.getInstance(this);

		GuiConst.LOGOPANEL_HEIGHT=logoPanel.getPreferredHeight();
		GuiConst.TABPANEL_HEIGHT=tabPanel.getPreferredHeight();

		newsPanel = NewsPanel.getInstance(this, Display.getHeight()-GuiConst.TABPANEL_HEIGHT-GuiConst.LOGOPANEL_HEIGHT);
		tablePanel = TablePanel.getInstance(this, Display.getHeight()-GuiConst.TABPANEL_HEIGHT-GuiConst.LOGOPANEL_HEIGHT, tableNo,1);

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
		for(int i=0; i<getFieldCount(); i++){
			Field f = getField(i);
			if(f instanceof TabField){
				TabField tf = (TabField)f;
				if(tabID == tf.getTabID()){
					tf.setFocus();
					selectedTab = tabID;
					return;
				}
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
		tabPanel.reinitTab();
		tabPanel.setTabFocus(selectedTab);
	}

	public void repaintScreen(){
		//this.removeAllMenuItems();
		//addMenuItems();

		logoPanel.invalidate();
		setSelectedTab(this.selectedTab); //9);//this.selectedTab);
		tabPanel.reinitTab();      

//		if (this.selectedTab == 9) {
//			setSelectedTab(9);
//		} else if (this.selectedTab == 10) {
//			setSelectedTab(10);
//		}
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
		System.out.println("onListFieldUnfocus:tabSelected:"+selectedTab);
		tabPanel.setTabFocus(selectedTab);
		isTabFocus=true;
	}
	public boolean isListFieldFocus(){
		if ( isTabFocus )
			return false;
		return true;
	}     
	//END -- method for ListFieldListener

	public void focusChanged(Field field, int eventType) {
		if(isFocusEventBlocked){
			return;
		}
		if(field instanceof TabField){
			TabField f = (TabField)field;
			int currTabId = f.getTabID();
			if(eventType == FOCUS_LOST){
				System.out.println("TAB_FOCUS_LOST: " + currTabId);
				if(currTabId==TabPanel.TAB_ORDER_OF_MERIT){
					if(selectedTab==TabPanel.TAB_TOUR_SCHEDULE){
						prevSelectedTab = TabPanel.TAB_ORDER_OF_MERIT;
					}
					else if(selectedTab!=TabPanel.TAB_ORDER_OF_MERIT){
						return;
					}
				}
				prevSelectedTab = currTabId;
			}
			else{
				System.out.println("TAB_FOCUS_GAINED: " + currTabId);
				switch(currTabId){
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
					if(prevSelectedTab!=TabPanel.TAB_ORDER_OF_MERIT){
						System.out.println("Jumping back to : " + prevSelectedTab);
						if(prevSelectedTab == TabPanel.TAB_TV_SCHEDULE){
							showTVScheduleTab();
							return;
						}
						if(prevSelectedTab == TabPanel.TAB_TOUR_SCHEDULE){
							showTourScheduleTab();
							return;
						}
						if(prevSelectedTab==TabPanel.TAB_NEWS){
							return;
						}
					}
					showOOMTab();
					break;
				}
				selectedTab = f.getTabID();
			}					
		}		
	}

	private void showNewsTab(){
		
//		if (getSelectedTab() == TabPanel.TAB_LIVE_SCORE) {
//			tabPanel.prevTab();
//		}
		logoPanel.repaintLogoPanel("default");
		repainteverything();
		fieldInit(1);
		newsPanel.loadNews(0);
		NewsPanel.newsPanel.newsList.setSize(newsCollection.size());
		isFocusEventBlocked = true;
		add(logoPanel);
		add(tabPanel);
		add(newsPanel);
		setSelectedTab(TabPanel.TAB_NEWS);
		isFocusEventBlocked = false;
	}

	private void showLiveScoreTab(){
		
//		if (getSelectedTab() == TabPanel.TAB_NEWS) {
//			tabPanel.nextTab();
//		} else {
//			tabPanel.prevTab();
//		}
		logoPanel.repaintLogoPanel("titlebar_livescore");
		repainteverything();
		fieldInit(3);
		isFocusEventBlocked = true;
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
		setSelectedTab(TabPanel.TAB_LIVE_SCORE);
		isFocusEventBlocked = false;
	}
	
	private void showOOMTab(){
		
//		if (getSelectedTab() == TabPanel.TAB_TOUR_SCHEDULE) {
//			tabPanel.nextTab();
//		} else {
//			tabPanel.prevTab();
//		}
		
		logoPanel.repaintLogoPanel("titlebar_orderofmerit");
		
		repainteverything();
		fieldInit(4);
		isFocusEventBlocked = true;
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
		setSelectedTab(TabPanel.TAB_ORDER_OF_MERIT);
		isFocusEventBlocked = false;
	}
	
	private void showTVScheduleTab(){

//		if (getSelectedTab() == TabPanel.TAB_LIVE_SCORE) {
//			tabPanel.nextTab();
//		} else {
//			tabPanel.prevTab();
//		}
		logoPanel.repaintLogoPanel("titlebar_tvschedule");
		repainteverything();
		fieldInit(1);
		
		isFocusEventBlocked = true;
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
		setSelectedTab(TabPanel.TAB_TV_SCHEDULE);
		isFocusEventBlocked = false;
	}

	private void showTourScheduleTab(){
		
//		if (getSelectedTab() == TabPanel.TAB_TV_SCHEDULE) {
//			tabPanel.nextTab();
//		} else {
//			tabPanel.prevTab();
//		}
		logoPanel.repaintLogoPanel("titlebar_tourschedule");
		repainteverything();
		fieldInit(2);
		isFocusEventBlocked = true;
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
		setSelectedTab(TabPanel.TAB_TOUR_SCHEDULE);
		isFocusEventBlocked = false;
	}

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
