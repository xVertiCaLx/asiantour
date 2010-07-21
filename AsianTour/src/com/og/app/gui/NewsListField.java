package com.og.app.gui;

import java.io.IOException;

import com.og.app.gui.listener.*;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;
import com.og.rss.*;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;

public class NewsListField extends CustomListField {
	private NewsPanel newsPanel = null;
	private String feedname = "";
	private int newsID = 0;
	ANewsItemObj[] item = new ANewsItemObj[5];

	public NewsListField(NewsPanel newsPanel, ListFieldListener listener) {
		super(listener);
		this.newsPanel = newsPanel;
	}

	protected void onFocus(int direction) {
		newsPanel.invalidate();
	}

	protected synchronized boolean navigationMovement(int dx, int dy, int status, int time) {

		if ( dy<0 ){
			int tmpy = dy*-1;
			if ( getSelectedIndex()-tmpy<0 ){
				if ( getSelectedIndex()>0 ){
					do{
						setSelectedIndex(getSelectedIndex()-1);
						tmpy--;
					}while(tmpy>0 );
				}
				listener.onListFieldUnfocus();
				newsPanel.invalidate();        
				return true;         
			}
		}     
		newsPanel.invalidate();
		return false;
	}

	public boolean navigationClick(int status, int time) {
		System.out.println("Clicked at news item");
		if(MenuScreen.getInstance().newsCollection.size() > 0)
		{
			try{
				synchronized(Application.getEventLock() ){
					ANewsItemObj ni = (ANewsItemObj)MenuScreen.getInstance().newsCollection.elementAt(getSelectedIndex());
					Screen s = UiApplication.getUiApplication().getActiveScreen();
					ni.index=getSelectedIndex();
					UiApplication.getUiApplication().pushScreen(new NewsDetailScreen(this, ni));
				}
				return true;
			}catch(Exception e){
				System.out.println(e);
			}              
		}
		return false;
	}

	public void loadNews(int newsID) {
		setRowHeight();

	}

}
