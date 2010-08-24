package com.og.app.gui;

import com.og.app.gui.component.CarouselMenuField;
import com.og.app.gui.listener.ListFieldListener;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.MainScreen;

public class CarouselMenuScreen extends MainScreen implements ListFieldListener {
	
	private static CarouselMenuScreen thisInstance = null;
	//LogoPanel logoPanel;
	BitmapField logoPanel = new BitmapField(Bitmap.getBitmapResource("res/titleBar/W" + GuiConst.SCREENWIDTH + "/default.png"));
	int fixHeight;
	
	public synchronized static CarouselMenuScreen getInstance() {
		if (thisInstance == null) {
			thisInstance = new CarouselMenuScreen();
		}
		return thisInstance;
	}
	
	public CarouselMenuScreen() {
		//logoPanel = LogoPanel.getInstance();
		fixHeight = GuiConst.SCREENHEIGHT - logoPanel.getHeight();
		//fixHeight = 200;
		CarouselMenuField menu = new CarouselMenuField("news", Bitmap.getBitmapResource("res/carousel/news_transit.png"), Bitmap.getBitmapResource("res/carousel/live_transit.png"), "right", fixHeight);
		CarouselPanel menuPanel = CarouselPanel.getInstance(menu, this, fixHeight);
		add(logoPanel);
		add(menuPanel);
	}
	
	public void clearResources() {
		thisInstance = null;
	}

	public boolean isListFieldFocus() {
		// TODO Auto-generated method stub
		
		return false;
	}

	public void onListFieldUnfocus() {
		// TODO Auto-generated method stub
		
	}

}
