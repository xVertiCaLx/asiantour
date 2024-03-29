package com.og.app.gui;

import java.util.Vector;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

import com.og.app.gui.listener.ListFieldListener;
import com.og.app.util.Utility;
import com.og.xml.ANewsItemObj;

public class CustomListField extends ListField implements ListFieldCallback, Runnable {
	protected Vector _pelements = new Vector();
	//protected Vector _elements = new Vector();
	protected Vector imgLoadingList = new Vector();

	protected Bitmap news_bg_selected = null;
	protected Bitmap news_bg_notSelected = null;
	protected Bitmap news_thumbnail = null;
	protected Bitmap news_border = null;

	protected int thumbnail_leftRightSpacing = 4;
	protected int dateTopSpacing = 1;
	protected int rowTopSpacing = 4;
	protected int rowBottomSpacing = 1;
	protected int textSpacing = 1;
	protected int previewHeight = -1;
	protected int previewWidth = -1;
	protected int newsID = 0;

	//protected Thread mythread = null;
	protected Object lock = new Object();
	protected ListFieldListener listener = null;

	public CustomListField(ListFieldListener listener) {
		super();
		this.listener = listener;
		setEmptyString("", DrawStyle.HCENTER);
		setCallback(this);
		
		news_bg_selected = Bitmap.getBitmapResource("res/news_list_selected.png");
		news_bg_notSelected = Bitmap.getBitmapResource("res/news_list.png");
		news_border = Bitmap.getBitmapResource("res/news_border.png");
		
//		if (GuiConst.SCREENWIDTH > 320) {
//			news_bg_selected = Bitmap.getBitmapResource("res/news_list_selected_65.png");
//			news_bg_notSelected = Bitmap.getBitmapResource("res/news_list_65.png"); 
//			news_thumbnail = Bitmap.getBitmapResource("res/previewnews2.png");
//			previewWidth = 65;
//			previewHeight = 65;
//			rowTopSpacing = 15;
//		} else {
//			previewWidth = 50;
//			previewHeight = 50;
//			news_bg_selected = Bitmap.getBitmapResource("res/news_list_selected_50.png");
//			news_bg_notSelected = Bitmap.getBitmapResource("res/news_list_50.png"); 
//			news_thumbnail = Bitmap.getBitmapResource("res/previewnews.png");
//			rowTopSpacing = 5;
//		}
		
		if (GuiConst.SCREENWIDTH > 320) {
			news_thumbnail = Bitmap.getBitmapResource("res/previewnews2.png");
			previewWidth = 65;
			previewHeight = 65;
			//rowTopSpacing = 2;
		} else {
			news_thumbnail = Bitmap.getBitmapResource("res/previewnews.png");
			previewWidth = 50;
			previewHeight = 50;
		}
		
		setRowHeight();

	}

	protected void setRowHeight() {
		setRowHeight(rowTopSpacing + (GuiConst.FONT_BOLD.getHeight() * 2) + (GuiConst.FONT_PLAIN.getHeight() * 2) + (textSpacing * 3) + dateTopSpacing + GuiConst.FONT_DATE.getHeight() + news_border.getHeight());//+rowBottomSpacing);
		if (getRowHeight() < previewHeight) {
			this.setRowHeight(previewHeight+3);
			System.out.println("previewHeight: " + previewHeight);
		}

		if ((getRowHeight() != news_bg_selected.getHeight()) || (this.getPreferredWidth() != news_bg_selected.getWidth())) {
			news_bg_selected = Utility.resizeBitmap(news_bg_selected, this.getPreferredWidth(), (getRowHeight() - news_border.getHeight()));
			news_bg_notSelected = Utility.resizeBitmap(news_bg_notSelected, this.getPreferredWidth(), (getRowHeight() - news_border.getHeight()));
			news_border = Utility.resizeBitmap(news_border, this.getPreferredWidth(), news_border.getHeight());
		}
	}

	public void onUnfocus() {
	}

	public void run() {
		//if there are thumbnails from the rss
	}

	public Vector getAllElements() {
		// return _elements;
		return MenuScreen.getInstance().newsCollection;
	}

	public int getSize() {
		//        return _elements.size();
		return MenuScreen.getInstance().newsCollection.size();
	}

	public void addElements(ANewsItemObj element) {
		add(element);
	}

	protected void add(ANewsItemObj element) {
		synchronized(lock) {
			MenuScreen.getInstance().newsCollection.addElement(element);
			setSize(getSize());
		}
	}

	public void drawListRow(ListField listField, final Graphics g, int index, int y, int width) {
		if (index < MenuScreen.getInstance().newsCollection.size()) {
			int rowHeight = getRowHeight();
			if (index == this.getSelectedIndex() && listener.isListFieldFocus()) {
				//since this field is being selected, draw the bg using news_bg_selected
				g.drawBitmap(0, y, news_bg_selected.getWidth(), news_bg_selected.getHeight(), news_bg_selected, 0,0);
				y += news_bg_selected.getHeight();
				g.drawBitmap(0, y, news_border.getWidth(), news_border.getHeight(), news_border, 0, 0);
				g.setColor(GuiConst.FONT_COLOR_NEWSSELECTED);
				y -= news_bg_selected.getHeight();
			} else {
				g.drawBitmap(0,y, news_bg_notSelected.getWidth(), news_bg_notSelected.getHeight(), news_bg_notSelected, 0, 0);
				y += news_bg_selected.getHeight();
				g.drawBitmap(0, y, news_border.getWidth(), news_border.getHeight(), news_border, 0, 0);
				y -= news_bg_selected.getHeight();
			}
			
			

			ANewsItemObj ni = (ANewsItemObj)MenuScreen.getInstance().newsCollection.elementAt(index);
			int imgy = y + ((rowHeight-news_border.getHeight()) - previewHeight)/2;

			Bitmap thumbnailBitmap = null;
			refreshCacheReload:
			if(ni.thumbnail!=null && ni.thumbnail.length > 2){
				try{
				thumbnailBitmap = Bitmap.createBitmapFromBytes(ni.thumbnail, 0, ni.thumbnail.length, 1);
				}catch(Exception e){
					ni.thumbnail = null;
					break refreshCacheReload;
				}
				thumbnailBitmap = Utility.resizeBitmap(thumbnailBitmap, previewWidth, previewHeight);
				g.drawBitmap(thumbnail_leftRightSpacing, imgy, previewWidth, previewHeight,thumbnailBitmap, 0, 0);
			}
			else{
				news_thumbnail = Utility.resizeBitmap(news_thumbnail, previewWidth, previewHeight);
				g.drawBitmap(thumbnail_leftRightSpacing, imgy, previewWidth, previewHeight, news_thumbnail, 0, 0);
			}

			//getting output
			Font textFont = GuiConst.FONT_BOLD;
			if (ni.hasread) {
				g.setColor(GuiConst.FONT_COLOR_NEWSREAD);
			}

			String text = ni.title;
			g.setFont(textFont);
			Vector vText = Utility.breakIntoWords(ni.title);
			//if there are news thumbnail, int tmpx = thumbnail_leftRightSpacing;
			int tmpx = thumbnail_leftRightSpacing + news_thumbnail.getWidth() + thumbnail_leftRightSpacing;

			int defaulttmpx = tmpx;
			int tmpy = y + ((rowHeight-news_border.getHeight()) - previewHeight)/2;
			int lineno = 1;
			for (int i = 0; i < vText.size(); i++) {
				if (lineno >2) {
					break;
				}
				String strtemp = (String)vText.elementAt(i);
				int wordWidth = GuiConst.FONT_BOLD.getAdvance(strtemp+ " ");
				if (tmpx+wordWidth >= this.getWidth()) {
					// if title is longer than screen width, cut the rest of the string and change to "..."
					if (lineno == 2) {
						strtemp = "...";
					} else {
						tmpy = tmpy + GuiConst.FONT_BOLD.getHeight()+textSpacing;
						tmpx = defaulttmpx;
					}
					lineno++;
				}
				g.drawText (strtemp+ " ", tmpx, tmpy);
				tmpx = tmpx+wordWidth;
			}

			g.setFont(GuiConst.FONT_PLAIN);
			//this will truncate the news content to 50 char
			vText = Utility.breakIntoWordsSpecial(ni.description, 50);
			tmpy = tmpy+GuiConst.FONT_BOLD.getHeight()+textSpacing;
			tmpx = defaulttmpx;
			lineno = 1;
			for (int i = 0; i < vText.size(); i++) {
				if (lineno > 2) {
					break;
				}

				String strtemp = (String)vText.elementAt(i);
				int wordWidth = GuiConst.FONT_PLAIN.getAdvance(strtemp + " ");
				if ((tmpx + wordWidth >= this.getWidth()) || ((lineno == 2) && (tmpx+wordWidth >= this.getWidth()*75/100))) {
					if (lineno == 2) {
						strtemp = "...";
					} else {
						tmpy = tmpy + GuiConst.FONT_PLAIN.getHeight()+textSpacing;
						tmpx = defaulttmpx;
					}
					lineno++;
				}

				g.drawText(strtemp + " ", tmpx, tmpy);
				tmpx = tmpx+ wordWidth;
			}

			String strdate = ni.pubDate;
			g.setFont(GuiConst.FONT_DATE);
			g.drawText(strdate, defaulttmpx, tmpy+textFont.getHeight()+dateTopSpacing);
		} 
	}

	protected boolean navigationMovement(int dx,
			int dy,
			int status,
			int time) {

		//System.out.println("navigationMovement:"+dx+","+dy+","+status+","+time);        
		invalidate();
		return false;
	}

	public Object get(ListField listField, int index) {
		if ((index >= 0) && (index < MenuScreen.getInstance().newsCollection.size())) {
			return MenuScreen.getInstance().newsCollection.elementAt(index);
		}
		return null;
	}

	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		return listField.indexOfList(prefix, start);
	}

	public boolean keyChar(char key, int status, int time) {
		if (MenuScreen.getInstance().newsCollection.size() > 0 && key == Characters.SPACE) {
			getScreen().scroll(Manager.DOWNWARD);
			return true;
		}
		return super.keyChar(key, status, time);
	}

	public synchronized void saveChanges(ANewsItemObj ni, int index) {
		try {
			//to save news as loaded/read
		} catch (Exception e) {
			System.out.println("aloy.CustomListField.exceptione.saveChanges(ni, index):"+e);
		}
	}

	public boolean navigationClick(int status, int time) {
		//System.out.println("aloy.CustomListField.navigationClick: got enter or not?");
		if (MenuScreen.getInstance().newsCollection.size() > 0) {
			try {
				synchronized(Application.getEventLock()) {
					ANewsItemObj ni = (ANewsItemObj)MenuScreen.getInstance().newsCollection.elementAt(getSelectedIndex());
					Screen s = UiApplication.getUiApplication().getActiveScreen();
					ni.index = getSelectedIndex();
					UiApplication.getUiApplication().pushScreen(new NewsDetailScreen(this, ni));
				}
				return true;
			} catch (Exception e) {
				System.out.println("aloy.CustomListField.navigationClick.exceptione: " + e);
			}
		}

		return false;
	}

	public void remove(int index) {
		synchronized(lock){
			MenuScreen.getInstance().newsCollection.removeElementAt(index);
			_pelements.removeElementAt(index);
			setSize(getSize());
		}
		invalidate();

	}

	protected void removeAll() {
		synchronized(lock){
			MenuScreen.getInstance().newsCollection.removeAllElements();
			_pelements.removeAllElements();
			setSize(0);
		}
		invalidate();
	}
} 
