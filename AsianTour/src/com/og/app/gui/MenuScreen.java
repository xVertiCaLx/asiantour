package com.og.app.gui;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.component.AnimatedImageField;
import com.og.app.gui.component.TabField;
import com.og.app.gui.component.TransitionableMainScreen;
import com.og.app.gui.listener.ListFieldListener;
import com.og.app.gui.listener.TabListener;
import com.og.app.gui.schedule.DataPanel;
import com.og.xml.XmlHelper;

public class MenuScreen extends TransitionableMainScreen implements
		TabListener, ListFieldListener {

	// Vector<ANewsItemObj>
	public Vector newsCollection = new Vector();

	// Vector<DataCentre>
	public Vector tvTimesCollection = new Vector();

	// Vector<DataCentre>
	public Vector tourScheduleCollection = new Vector();

	// Vector<DataCentre>
	public Vector countryCollection = new Vector();

	// Vector<DataCentre>
	public Vector meritCollection = new Vector();

	public static MenuScreen thisInstance = null;

	public int selectedTab;
	private boolean isTabFocus = true;

	private LogoPanel logoPanel = null;
	private TabPanel tabPanel = null;
	private TablePanel tablePanel = null;
	private DataPanel dataPanel = null;

	public NewsPanel newsPanel = null;

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
		initTablePkg(0);
		fadeToAndRun(Graphics.BLACK /* color to fade to */, 800 /* fade duration */);
		System.out
				.println("-----------------------------------------------------");
		System.out.println("Display height is: " + GuiConst.SCREENHEIGHT
				+ "TABPANEL: " + GuiConst.TABPANEL_HEIGHT + "LOGOPANEL: "
				+ GuiConst.LOGOPANEL_HEIGHT);
		System.out
				.println("-----------------------------------------------------");

		add(logoPanel);
		add(tabPanel);
		add(newsPanel);
		// addPanels("loading", 1);

		newsCollection = RecordStoreHelper.getNewsCollection();
		System.out.println(GuiConst.SCREENWIDTH);

	}

	public void initTablePkg(int tableNo) {
		logoPanel = LogoPanel.getInstance();
		tabPanel = TabPanel.getInstance(this);

		GuiConst.LOGOPANEL_HEIGHT = logoPanel.getPreferredHeight();
		GuiConst.TABPANEL_HEIGHT = tabPanel.getPreferredHeight();

		newsPanel = NewsPanel.getInstance(this, GuiConst.SCREENHEIGHT
				- GuiConst.TABPANEL_HEIGHT - GuiConst.LOGOPANEL_HEIGHT);
		tablePanel = TablePanel.getInstance(this, GuiConst.SCREENHEIGHT
				- GuiConst.TABPANEL_HEIGHT - GuiConst.LOGOPANEL_HEIGHT,
				tableNo, 1);
	}

	public void initSchedulePkg(int tableNo) {
		logoPanel = LogoPanel.getInstance();
		tabPanel = TabPanel.getInstance(this);

		GuiConst.LOGOPANEL_HEIGHT = logoPanel.getPreferredHeight();
		GuiConst.TABPANEL_HEIGHT = tabPanel.getPreferredHeight();

		dataPanel = DataPanel
				.getInstance(this, GuiConst.SCREENHEIGHT
						- GuiConst.TABPANEL_HEIGHT - GuiConst.LOGOPANEL_HEIGHT,
						tableNo);
	}

	public void addPanels(String status, int tabNo) {
		VerticalFieldManager vFM = new VerticalFieldManager(
				Manager.USE_ALL_WIDTH);
		Bitmap load_icon = Bitmap.getBitmapResource("res/loading.png");
		AnimatedImageField loading = new AnimatedImageField(300, load_icon
				.getHeight(), load_icon, 12, 100, Field.FIELD_HCENTER
				| Field.FOCUSABLE);

		add(logoPanel);
		add(tabPanel);
		if (status == "loaded") {
			if (tabNo == 1)
				add(newsPanel);
			else if ((tabNo == 3) || (tabNo == 4))
				add(dataPanel);
			else if ((tabNo == 2) || (tabNo == 5))
				add(tablePanel);
		} else if (status == "loading") {
			vFM.deleteAll();
			loading.startAnimation();
			vFM.add(loading);
			add(vFM);
		}
	}

	public boolean keyChar(char key, int status, int time) {
		switch (key) {
		case Characters.ESCAPE:
			RecordStoreHelper.setNewsCollection(newsCollection);
			//clearResource();
			
			Screen s = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(s);
			s.deleteAll();
			
			CarouselMenuScreen screen = new CarouselMenuScreen();//CarouselMenuScreen.getInstance();
			UiApplication.getUiApplication().pushScreen(screen); 
			break;
		}
		return true;
	}

//	 public boolean onClose() {
//
//	 if (Dialog.ask(Dialog.D_YES_NO, "Do you want to exit?") == Dialog.YES) {
//	 System.out.println("aloy.endapp");
//	 RecordStoreHelper.setNewsCollection(newsCollection);
//	 clearResource();
//	 System.exit(0);
//	 }
//			
//			
//			
//	 return true;
//	 }

	public void clearResource() {
		tabPanel = null;
		logoPanel = null;
		newsPanel = null;
		tablePanel = null;
		dataPanel = null;
	}

	public void repainteverything() {
		this.deleteAll();
		tablePanel.reinitThisThing();
		if (dataPanel != null)
			dataPanel.reinitThisThing();
	}

	public void repaintTab() {
		tabPanel.repaintTab();
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int tabID) {
		selectedTab = tabID;
		Vector tabFields = TabPanel.getInstance(this).tabFields;
		for (int i = 0; i < tabFields.size(); i++) {
			TabField tabField = (TabField) tabFields.elementAt(i);
			if (tabID == tabField.getTabID()) {
				tabField.setFocus();
				System.out.println("MenuScreen:setSelectedTab(" + tabID + ")");
			}
		}
	}

	public void setTabOnFocus(boolean isFocus) {
		isTabFocus = isFocus;
	}

	public boolean isTabOnFocus() {
		return isTabFocus;
	}

	public void reinitTab() {
		tabPanel.setTabFocus(selectedTab);
	}

	public void repaintScreen() {
		logoPanel.invalidate();
	}

	public void drawBottomPanel() {
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

	public boolean isSelectedTabHasNews() {
		return true;
	}

	// -----------------------------------------------------------------------------------------------------
	// START -- method for ListFieldListener
	public void onListFieldUnfocus() {
		// System.out.println("onListFieldUnfocus:tabSelected:"+selectedTab);
		tabPanel.setTabFocus(selectedTab);
		isTabFocus = true;
	}

	public boolean isListFieldFocus() {
		if (isTabFocus)
			return false;
		return true;
	}

	// END -- method for ListFieldListener

	public void showNewsTab() {
		// logoPanel.repaintLogoPanel("default");
		repainteverything();
		initTablePkg(0);
		newsPanel.loadNews(0);
		NewsPanel.newsPanel.newsList.setSize(newsCollection.size());
		// logoPanel.repaintLogoPanel("default");
		add(logoPanel);
		add(tabPanel);
		add(newsPanel);

	}

	public void showLiveScoreTab() {
		// logoPanel.repaintLogoPanel("titlebar_livescore");
		repainteverything();
		initTablePkg(3);
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
	}

	public void showOOMTab() {
		// logoPanel.repaintLogoPanel("titlebar_orderofmerit");
		repainteverything();
		initTablePkg(4);
		add(logoPanel);
		add(tabPanel);
		add(tablePanel);
	}

	public void showTVScheduleTab() {
		// logoPanel.repaintLogoPanel("titlebar_tvschedule");
		repainteverything();
		// fieldInit(1);
		initSchedulePkg(1);
		add(logoPanel);
		add(tabPanel);
		add(dataPanel);
	}

	public void showTourScheduleTab() {
		repainteverything();
		// logoPanel.repaintLogoPanel("titlebar_tourschedule");
		// fieldInit(2);
		initSchedulePkg(3);
		add(logoPanel);
		add(tabPanel);
		add(dataPanel);
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
