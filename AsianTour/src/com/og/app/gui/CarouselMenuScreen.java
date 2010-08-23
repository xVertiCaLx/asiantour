package com.og.app.gui;

import com.og.app.gui.component.CarouselMenuField;
import com.og.app.gui.listener.ListFieldListener;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.container.MainScreen;

public class CarouselMenuScreen extends MainScreen implements ListFieldListener {
	
	private static CarouselMenuScreen thisInstance = null;
	
	public synchronized static CarouselMenuScreen getInstance() {
		if (thisInstance == null) {
			thisInstance = new CarouselMenuScreen();
		}
		return thisInstance;
	}
	
	public CarouselMenuScreen() {
		CarouselMenuField menu = new CarouselMenuField("news", Bitmap.getBitmapResource("res/carousel/news_transit.png"), "right", 120);
		CarouselPanel menuPanel = CarouselPanel.getInstance(menu, this);
		add(menuPanel);
	}

	public boolean isListFieldFocus() {
		// TODO Auto-generated method stub
		
		return false;
	}

	public void onListFieldUnfocus() {
		// TODO Auto-generated method stub
		
	}

}
