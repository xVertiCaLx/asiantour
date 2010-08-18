package com.og.app.gui;

//import com.og.app.fakeRecordStore.*;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

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

		logoicon= Bitmap.getBitmapResource("res/ASIAN-TOUR-SPLASH.png");

		thread = new Thread(this);
		thread.start();
		
	}

	protected void paint(Graphics graphics) {

		int scrwidth = net.rim.device.api.system.Display.getWidth();
		int scrheight =  net.rim.device.api.system.Display.getHeight();      
		int logowidth  = logoicon.getWidth();
		int logoheight=  logoicon.getHeight();

		int x = (scrwidth-logowidth)/2;
		int y = (scrheight-logoheight-GuiConst.FONT_VERSION.getHeight()*2)/2;
		//System.out.println("x:"+x+" , y:"+y);
		graphics.setColor(0);
		graphics.fillRect(-10, -10, getWidth()+20, getHeight()+20);
		graphics.drawBitmap(x, y+30, 300, 300, logoicon , 0, 0);        

	}

	public boolean keyChar(char key, int status, int time) {
		switch (key) {
		case Characters.ESCAPE:
			System.exit(0);
			break;
		}
		return true;
	}  

	public void run(){
			try {
				Thread.sleep(3000);
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
				MenuScreen screen = MenuScreen.getInstance();
				UiApplication.getUiApplication().pushScreen(screen); 
			}catch(Exception e){
			}
		}        
	}    
} 


