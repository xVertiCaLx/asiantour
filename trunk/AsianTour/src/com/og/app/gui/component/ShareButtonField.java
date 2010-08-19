package com.og.app.gui.component;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;

import com.og.app.gui.listener.ImageButtonListener;
import com.og.app.gui.social.TwitterLoginScreen;
import com.og.app.util.DataCentre;
import com.og.xml.ANewsItemObj;

public class ShareButtonField extends Field {

	Bitmap shareButton = null;
	String buttonName;
	String shareType;

	private short focusstatus = 0;

	private int fieldWidth;
	private int fieldHeight;
	private int padding = 2;
	private ANewsItemObj newsItem = null;
	private DataCentre obj = null;
	private static ImageButtonListener listener = null;
	private TwitterLoginScreen twitter;
		
	public ShareButtonField(String buttonName, String shareType,
			DataCentre obj, ANewsItemObj newsItem) {
		super(Field.FOCUSABLE | ButtonField.CONSUME_CLICK);
		this.newsItem = newsItem;
		this.shareType = shareType;
		this.obj = obj;
		// addImageButtonListener(listener);
		this.buttonName = buttonName;
		if (buttonName == "fb") {
			shareButton = Bitmap.getBitmapResource("res/fb_share.png");
		} else if (buttonName == "tw") {
			shareButton = Bitmap.getBitmapResource("res/tw_share.png");
		} else {
			Dialog
					.alert("An error has occurred. Unable to draw social networking buttons.");
		}

		fieldHeight = shareButton.getHeight();
		fieldWidth = shareButton.getWidth() + padding;// GuiConst.SCREENWIDTH;
	}

	public void addImageButtonListener(ImageButtonListener listener) {

		ShareButtonField.listener = listener;
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
		System.out.println("focusing this");
		focusstatus = 1;
		invalidate();
		if (listener != null)
			listener.imageButtonOnFocus(this.getIndex());
	}

	protected void onUnfocus() {
		System.out.println("unfocusing this");
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

	protected void paint(Graphics g) {
		if (isFocus()) {
			shareButton = Bitmap.getBitmapResource("res/tw_share_hover.png");

		} else {
			shareButton = Bitmap.getBitmapResource("res/tw_share.png");
		}
		g.drawBitmap(padding, 0, shareButton.getWidth(), shareButton
				.getHeight(), shareButton, 0, 0);
	}

	protected boolean navigationClick(int status, int time) {
		System.out.println("aloy.ShareButtonField.navigationClick::" + status
				+ "::");
		focusstatus = 1;
		invalidate();
		fieldChangeNotify(1);

		if (buttonName == "fb") {
			Dialog.alert("FACEBOOK!");
		} else if (buttonName == "tw") {
			if (shareType == "News") {
				twitter = new TwitterLoginScreen("AT News");
			} else if (shareType == "TV") {
				twitter = new TwitterLoginScreen("AT TV News");
			} else if (shareType == "Tour") {
				twitter = new TwitterLoginScreen("AT Tour News");
			}
			UiApplication.getUiApplication().pushScreen(twitter);
		}

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
