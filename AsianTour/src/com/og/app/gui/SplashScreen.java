package com.og.app.gui;

//import com.og.app.fakeRecordStore.*;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

import com.og.xml.XmlHelper;

public class SplashScreen extends MainScreen implements Runnable
{
	private Thread thread = null;
	private Bitmap logoicon= null;
	boolean showopeningads = false;
	//public NewsStore ns = new NewsStore;
	public SplashScreen() {

		try{               
			GuiConst.reinitFont();
		} catch(Exception e) {
			e.printStackTrace();
		}  
		
		XmlHelper.downloadOOM();
		XmlHelper.downloadCountry();
		XmlHelper.downloadNews();
		XmlHelper.downloadTourSchedule();
		
		logoicon= Bitmap.getBitmapResource("res/ASIAN-TOUR-SPLASH.jpg");
		//wait for 3 sec then go into main screen, can be changed
		thread = new Thread(this);
		thread.start();
			
	}
//
	protected void paint(Graphics graphics) {

		int scrwidth = net.rim.device.api.system.Display.getWidth();
		int scrheight =  net.rim.device.api.system.Display.getHeight();      
		int logowidth  = logoicon.getWidth();
		int logoheight=  logoicon.getHeight();

		int x = (scrwidth-logowidth)/2;
		int y = (scrheight-logoheight)/2;
		//graphics.setColor(0);
		//graphics.fillRect(-10, -10, getWidth()+20, getHeight()+20);
		graphics.drawBitmap(x, y, logoicon.getWidth(), logoicon.getHeight(), logoicon , 0, 0);        

	}

	public void run(){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				loadMenuScreen();
				e.printStackTrace();
			}
			loadMenuScreen();
	}

	private void loadMenuScreen(){
		synchronized(Application.getEventLock() ){
			try{
				Screen s = UiApplication.getUiApplication().getActiveScreen();
				UiApplication.getUiApplication().popScreen(s);
				s.deleteAll();
				//MenuScreen screen = MenuScreen.getInstance();
				CarouselMenuScreen screen = CarouselMenuScreen.getInstance();
				UiApplication.getUiApplication().pushScreen(screen); 
			}catch(Exception e){
			}
		}        
	}    
} 


