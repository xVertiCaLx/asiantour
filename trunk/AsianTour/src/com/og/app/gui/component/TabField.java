package com.og.app.gui.component;

import com.og.app.gui.GuiConst;
import com.og.app.gui.MenuScreen;
import com.og.app.gui.listener.TabListener;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.*;

public class TabField extends Field {
	private int fieldWidth;
	private int fieldHeight;
	private String text;
	private int padding = 2;
	private Font font = null;
	private Font selectedFont = null;
	private TabListener listener = null;
	private int tabID = 0;
	private Bitmap image = null;

	final Bitmap btn_nonfocusable_left= Bitmap.getBitmapResource("res/tabLeftNoFocus.png");
	final Bitmap btn_nonfocusable_right= Bitmap.getBitmapResource("res/tabRightNoFocus.png");
	final Bitmap btn_nonfocusable_center= Bitmap.getBitmapResource("res/tabMiddleNoFocus.png");

	final Bitmap btn_left= Bitmap.getBitmapResource("res/tabLeft.png");
	final Bitmap btn_right= Bitmap.getBitmapResource("res/tabRight.png");
	final Bitmap btn_center= Bitmap.getBitmapResource("res/tabMiddle.png");

	final Bitmap btn_focus_left= Bitmap.getBitmapResource("res/tabLeftSelected.png");
	final Bitmap btn_focus_right= Bitmap.getBitmapResource("res/tabRightSelected.png");
	final Bitmap btn_focus_center= Bitmap.getBitmapResource("res/tabMiddleSelected.png");

	final Bitmap btn_highlight_left= btn_focus_left;
	final Bitmap btn_highlight_right= btn_focus_right;
	final Bitmap btn_highlight_center= btn_focus_center;

	Bitmap[] btn_leftarr = new Bitmap[]{btn_left, btn_focus_left, btn_highlight_left}; 
	Bitmap[] btn_rightarr = new Bitmap[]{btn_right, btn_focus_right, btn_highlight_right}; 
	Bitmap[] btn_centerarr = new Bitmap[]{btn_center, btn_focus_center, btn_highlight_center};

	public TabField (int tabID, String text, Bitmap image, Font font, Font selectedFont, long style) {
		super(style);
		if (style == Field.NON_FOCUSABLE) {
			btn_leftarr[0] = btn_nonfocusable_left;
			btn_rightarr[0] = btn_nonfocusable_right;
			btn_centerarr[0] = btn_nonfocusable_center;
		}
		this.text = text;
		this.font = font;
		this.tabID = tabID;
		this.image = image;
		this.selectedFont = selectedFont;
		init();
	}

	public TabField (int tabID, String text, Bitmap image, Font font, Font selectedFont, boolean setUnfocusing) {
		this(tabID, text, image, font, selectedFont);
		if (setUnfocusing) {
			btn_leftarr[0]=btn_nonfocusable_left;
			btn_rightarr[0]=btn_nonfocusable_right;
			btn_centerarr[0]=btn_nonfocusable_center;                
		}
	}

	public TabField (int tabID, String text, Bitmap image, Font font, Font selectedFont) {
		super(Field.FOCUSABLE|ButtonField.CONSUME_CLICK);
		this.text = text;
		this.font=font;
		this.tabID=tabID;
		this.selectedFont=selectedFont;
		this.image = image;
		init();
	}

	private void init(){
		try{

			fieldHeight = btn_leftarr[0].getHeight();//defaultFont.getHeight() + padding;
			if ( text==null || text.equals("") ){
				fieldWidth=btn_leftarr[0].getWidth()+btn_rightarr[0].getWidth()+image.getWidth()+padding*2;
			}else{
				fieldWidth = btn_leftarr[0].getWidth()+btn_rightarr[0].getWidth()+font.getAdvance(text)+padding*2;//btn.getWidth();//defaultFont.getAdvance(text) + (padding * 2);
				if ( image!=null && fieldWidth<image.getWidth() )
					fieldWidth=btn_leftarr[0].getWidth()+btn_rightarr[0].getWidth()+image.getWidth()+padding*2;
			}

		}catch(Exception e){
			System.out.println("ERROR:TabField:"+e);
		}             
	}

	public int getTabID() {
		return this.tabID;
	}

	public void setTabListener(TabListener listener) {
		this.listener=listener;
	}

	protected boolean navigationClick(int status, int time) {
		System.out.println("TabField:navigationClick:" + tabID+", selectedTab=" + MenuScreen.getInstance().selectedTab);
		if ( listener!=null ){
			listener.setSelectedTab(MenuScreen.getInstance().selectedTab);
			listener.repaintTab();
			listener.setTabOnFocus(false);
		}
		//fieldChangeNotify(1);
		//return true;
		return true;
	}

	protected void onFocus(int direction) {
		
		if ( listener!=null ){
			System.out.println("TabField:onFocus:"+text+", selectedTab=" + MenuScreen.getInstance().selectedTab);
			listener.setTabOnFocus(true);
			listener.setSelectedTab(MenuScreen.getInstance().selectedTab);
		}            
		invalidate();
	} 

	protected void onUnfocus() {
		
		if ( listener!=null ){
			System.out.println("TabField:onUnfocus:"+text+", selectedTab=" + MenuScreen.getInstance().selectedTab);
			listener.setTabOnFocus(false);
		}                     
		invalidate();
	}

	protected void fieldChangeNotify(int context) {
		try {
			this.getChangeListener().fieldChanged(this, context);
		} catch (Exception e) {
			System.out.println("fieldChangeNotify error:" + e);
		}
	}

	public int getPreferredWidth() {
		return fieldWidth;
	}

	public int getPreferredHeight() {
		return fieldHeight;
	}

	protected void layout(int arg0, int arg1) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	protected void paint(Graphics g) {
		int colour = GuiConst.TABCOLOR_NORMAL;
		int focusStatus = 0;
		int x = 0;
		int textWidth = 0;
		int textHeight = 0;
		int y = 0;

		if ((listener != null) && (listener.getSelectedTab() == tabID)) {
			focusStatus=1;
			colour = GuiConst.TABCOLOR_FOCUS;
			if (listener.isTabOnFocus() == true){
				colour = GuiConst.TABCOLOR_HIGHLIGHT;
				focusStatus=2;
			}
		}

		g.setColor(colour);
		g.drawBitmap(x,0,btn_leftarr[focusStatus].getWidth(),btn_leftarr[focusStatus].getHeight(),btn_leftarr[focusStatus],0,0);
		x += btn_leftarr[focusStatus].getWidth();
		textWidth = fieldWidth - btn_rightarr[focusStatus].getWidth() - btn_leftarr[focusStatus].getWidth();
		//continue to draw the middle part until it reaches the end of the text
		do {
			g.drawBitmap(x,0,btn_centerarr[focusStatus].getWidth(),fieldHeight,btn_centerarr[focusStatus],0,0);
			textWidth = textWidth - btn_centerarr[focusStatus].getWidth();
			x = x + btn_centerarr[focusStatus].getWidth();

		} while(textWidth > 0);

		g.drawBitmap(x,0,btn_rightarr[focusStatus].getWidth(),fieldHeight,btn_rightarr[focusStatus],0,0);
		g.setFont(font);
		if ( focusStatus==2 )
			g.setFont(selectedFont);
		textWidth = font.getAdvance(text);
		textHeight = font.getHeight();

		if ((text != null) && (!text.equals(""))) {
			if (image == null)
				y = (fieldHeight - textHeight)/2;
			else
				y = (fieldHeight - image.getHeight() - textHeight - 1)/2;
		} else {
			y = (fieldHeight - image.getHeight())/2;
		}
		if (image != null){
			g.drawBitmap((fieldWidth - image.getWidth())/2, y,image.getWidth(),image.getHeight(),image,0,0);       
			y = y + image.getHeight() + 1;  
		}
		if ((text!=null) && (!text.equals(""))){
			g.drawText(text,(fieldWidth - textWidth)/2, y);            
		}
	}

	protected void drawFocus(Graphics graphics, boolean on) {

	}

	protected boolean navigationMovement(int dx,
			int dy,
			int status,
			int time){
		System.out.println("B:navigationMovement:"+dx+","+dy+","+status+","+time);                                                
		return false;
	}
} 
