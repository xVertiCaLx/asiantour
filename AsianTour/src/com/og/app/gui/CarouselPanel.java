package com.og.app.gui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.og.app.gui.component.CarouselMenuField;
import com.og.app.gui.listener.ListFieldListener;

public class CarouselPanel extends VerticalFieldManager {
	private static CarouselPanel carouselPanel = null;
	//private ChildPanel childPanel = null;
	private Bitmap image;
	private int fixHeight = 0;
	int count = 0;
	CarouselMenuField menu;
	
	public synchronized static CarouselPanel getInstance(CarouselMenuField menu, ListFieldListener listener){
		if ( carouselPanel == null ) {
			carouselPanel = new CarouselPanel(menu, listener);
		}
		return carouselPanel;
	}
	
	private CarouselPanel(CarouselMenuField menu, ListFieldListener listener) {
		super(Manager.FOCUSABLE);        
		fixHeight = 120;
		carouselPanel = this;

		//childPanel = new ChildPanel(childTabWidth);   
		this.menu = menu;
		
		reinitMenu();
		
		
	}
	
	public void reinitMenu() {
		menu.startAnimation();
		deleteAll();
		add(menu);
//		childPanel.updateLayout(childTabWidth);
//		childPanel.add(menu);
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
	
	protected synchronized boolean navigationMovement(int dx, int dy, int status, int time) {
		System.out.println(dx + " " + dy);
		if (dx > 0) {
			//right
			System.out.println("right movement");
			if (menu.checkCurrentMenu() == null) {
				Dialog.alert("Error Loading Menu");
			} else if (menu.checkCurrentMenu() == "news") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/oom_transit.png");
					menu = new CarouselMenuField("oom", image, "right", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "oom") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tour_transit.png");
					menu = new CarouselMenuField("tour", image, "right", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tour") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tv_transit.png");
					menu = new CarouselMenuField("tv", image, "right", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tv") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/live_transit.png");
					menu = new CarouselMenuField("live", image, "right", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "live") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/news_transit.png");
					menu = new CarouselMenuField("news", image, "right", 120);
					reinitMenu();
				}
			}
			
		} else if (dx < 0) {
			//left
			
			if (menu.checkCurrentMenu() == null) {
				Dialog.alert("Error Loading Menu");
			} else if (menu.checkCurrentMenu() == "news") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/news_transit.png");
					menu = new CarouselMenuField("live", image, "left", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "oom") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/oom_transit.png");
					menu = new CarouselMenuField("news", image, "left", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tour") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tour_transit.png");
					menu = new CarouselMenuField("oom", image, "left", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tv") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tv_transit.png");
					menu = new CarouselMenuField("tour", image, "left", 120);
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "live") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/live_transit.png");
					menu = new CarouselMenuField("tv", image, "left", 120);
					reinitMenu();
				}
			}
		}
		return true;
	}
	
	public boolean navigationClick(int status, int time) {
		System.out.println("Clicked at "+ menu.checkCurrentMenu() +" item");
		if (menu.checkCurrentMenu() == "tv") {
			Screen s = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(s);
			s.deleteAll();
			MenuScreen screen = MenuScreen.getInstance();
			screen.showTVScheduleTab();
			screen.setSelectedTab(3);
			UiApplication.getUiApplication().pushScreen(screen); 
		} else if (menu.checkCurrentMenu() == "news") {
			Screen s = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(s);
			s.deleteAll();
			MenuScreen screen = MenuScreen.getInstance();
			UiApplication.getUiApplication().pushScreen(screen); 
		} else if (menu.checkCurrentMenu() == "live") {
			Screen s = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(s);
			s.deleteAll();
			MenuScreen screen = MenuScreen.getInstance();
			screen.showLiveScoreTab();
			screen.setSelectedTab(2);
			UiApplication.getUiApplication().pushScreen(screen); 
		} else if (menu.checkCurrentMenu() == "tour") {
			Screen s = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(s);
			s.deleteAll();
			MenuScreen screen = MenuScreen.getInstance();
			screen.showTourScheduleTab();
			screen.setSelectedTab(4);
			UiApplication.getUiApplication().pushScreen(screen); 
		} else if (menu.checkCurrentMenu() == "oom") {
			Screen s = UiApplication.getUiApplication().getActiveScreen();
			UiApplication.getUiApplication().popScreen(s);
			s.deleteAll();
			MenuScreen screen = MenuScreen.getInstance();
			screen.showOOMTab();
			screen.setSelectedTab(5);
			UiApplication.getUiApplication().pushScreen(screen); 
		}
		return false;
	}
	
	public boolean onClose() {
		if (Dialog.ask(Dialog.D_YES_NO, "Do you want to exit?") == Dialog.YES) {
			System.out.println("aloy.endapp");
			
			System.exit(0);
		}
		return true;
	}
	
//	class ChildPanel extends HorizontalFieldManager {
//		int fixwidth = 0;
//		int totalchildwidth = 0;
//		public ChildPanel(int width) {
//			super(Manager.HORIZONTAL_SCROLL | Manager.HORIZONTAL_SCROLLBAR);
//			this.fixwidth = width;
//		}
//
//		public void reinit() {
//			totalchildwidth = 0;
//		}
//
//		public int getChildWidth() {
//			return totalchildwidth ;
//		}        
//		public void addChildWidth(int childwidth) {
//			totalchildwidth = totalchildwidth +childwidth;
//		}
//
//		public void updateLayout(int width) {
//			this.fixwidth = width;
//			super.updateLayout();
//		}
//
//		public int getPreferredWidth() {
//			return fixwidth;
//		}
//
//		protected void sublayout(int width, int height) {
//			super.sublayout(fixwidth, height);
//			setExtent(fixwidth, height);
//		}        
//	}
}
