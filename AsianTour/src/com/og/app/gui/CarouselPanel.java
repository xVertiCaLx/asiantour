package com.og.app.gui;

import com.og.app.gui.component.CarouselMenuField;
import com.og.app.gui.listener.ListFieldListener;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;

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
					menu = new CarouselMenuField("oom", image, "right");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "oom") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tour_transit.png");
					menu = new CarouselMenuField("tour", image, "right");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tour") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tv_transit.png");
					menu = new CarouselMenuField("tv", image, "right");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tv") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/live_transit.png");
					menu = new CarouselMenuField("live", image, "right");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "live") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/news_transit.png");
					menu = new CarouselMenuField("news", image, "right");
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
					menu = new CarouselMenuField("live", image, "left");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "oom") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/oom_transit.png");
					menu = new CarouselMenuField("news", image, "left");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tour") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tour_transit.png");
					menu = new CarouselMenuField("oom", image, "left");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "tv") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/tv_transit.png");
					menu = new CarouselMenuField("tour", image, "left");
					reinitMenu();
				}
			} else if (menu.checkCurrentMenu() == "live") {
				if (!menu.checkRunStatus()) {
					image = Bitmap.getBitmapResource("res/carousel/live_transit.png");
					menu = new CarouselMenuField("tv", image, "left");
					reinitMenu();
				}
			}
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
