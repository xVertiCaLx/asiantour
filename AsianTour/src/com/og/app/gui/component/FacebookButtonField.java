package com.og.app.gui.component;

import com.og.app.gui.listener.ImageButtonListener;
import com.og.app.gui.GuiConst;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.*;

public class FacebookButtonField extends Field {

	Bitmap button = Bitmap.getBitmapResource("res/fb_share.png");
	//final Bitmap tw = Bitmap.getBitmapResource("res/tw_share.png");

	private short focusstatus = 0;

	private int fieldWidth;
	private int fieldHeight;
	private int padding = 2;
	private int sharing = 0;

	private ImageButtonListener listener = null;

	public FacebookButtonField(long style) {
		super(style);
		fieldWidth = GuiConst.SCREENWIDTH;
//		if (fb.getHeight() > tw.getHeight()) {
//			fieldHeight = fb.getHeight() + (padding * 2);
//		} else {
//			fieldHeight = tw.getHeight() + (padding * 2);
//		}
		fieldHeight = button.getHeight();
	}

	public void addImageButtonListener(ImageButtonListener listener) {
		this.listener = listener;
	}

	public void setUnfocus() {
		onUnfocus();
	}

	public boolean isFocus() {
		if (focusstatus == 1)
			return true;
		return false;
	}

	protected void onFocus(int direction) {
		focusstatus = 1;
		invalidate();
		if (listener != null)
			listener.imageButtonOnFocus(this.getIndex());
	}

	protected void onUnfocus() {
		focusstatus = 0;
		invalidate();
		if (listener != null)
			listener.imageButtonOnUnfocus(this.getIndex());
	}

	public int getPreferredWidth() {
		return fieldWidth;
	}

	public int getPreferredHeight() {
		return fieldHeight;
	}

	protected void layout(int width, int height) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	protected void paint(Graphics graphics) {
		
		if (sharing == 0) {
			graphics.drawBitmap(0, 0, button.getWidth(), button.getHeight(), button, 0, 0);
			sharing ++;
		} else {
			button = Bitmap.getBitmapResource("res/tw_share.png");
			graphics.drawBitmap(0, 0, button.getWidth(), button.getHeight(), button, 0, 0);
		}
		
	}

	protected boolean navigationClick(int status, int time) {
		System.out.println("navigationClick::" + status + "::");
		
		invalidate();
		fieldChangeNotify(1);
		return true;
	}

	protected boolean navigationUnclick(int status, int time) {
		focusstatus = 0;
		invalidate();
		return true;
	}

	protected void drawFocus(Graphics graphics, boolean on) {

	}

	protected void fieldChangeNotify(int context) {
		try {
			this.getChangeListener().fieldChanged(this, context);
		} catch (Exception e) {
		}
	}

	public boolean keyChar(char key, int status, int time) {
		System.out.println("keyChar::" + key + "::");
		boolean retval = false;
		if (key == Characters.ENTER || key == Characters.SPACE) {
			fieldChangeNotify(1);
			retval = true;
		} else {
			retval = super.keyChar(key, status, time);
		}
		return retval;
	}

}
