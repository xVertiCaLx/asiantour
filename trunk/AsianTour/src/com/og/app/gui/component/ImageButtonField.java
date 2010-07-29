package com.og.app.gui.component;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;

import com.og.app.gui.GuiConst;
import com.og.app.gui.listener.ImageButtonListener;

public class ImageButtonField extends Field {
	final Bitmap btn_left = Bitmap.getBitmapResource("res/btn_left.png");
	final Bitmap btn_right = Bitmap.getBitmapResource("res/btn_right.png");
	final Bitmap btn_center = Bitmap.getBitmapResource("res/btn_center.png");

	final Bitmap btn_focus_left = btn_left;
	final Bitmap btn_focus_right = btn_right;
	final Bitmap btn_focus_center = btn_center;
	// final Bitmap btn_focus_left=
	// Bitmap.getBitmapResource("res/btn_focus_left.png");
	// final Bitmap btn_focus_right=
	// Bitmap.getBitmapResource("res/btn_focus_right.png");
	// final Bitmap btn_focus_center=
	// Bitmap.getBitmapResource("res/btn_focus_center.png");

	Bitmap[] btn_leftarr = new Bitmap[] { btn_left, btn_focus_left };
	Bitmap[] btn_rightarr = new Bitmap[] { btn_right, btn_focus_right };
	Bitmap[] btn_centerarr = new Bitmap[] { btn_center, btn_focus_center };

	private short focusstatus = 0;

	private int fieldWidth;
	private int fieldHeight;
	private String text;
	private int padding = 5;

	private ImageButtonListener listener = null;

	public ImageButtonField(String text, long style) {
		super(style);
		this.text = text;
		fieldHeight = btn_left.getHeight();// defaultFont.getHeight() + padding;
		fieldWidth = btn_left.getWidth() + btn_right.getWidth()
				+ GuiConst.FONT_PLAIN.getAdvance(text) + padding * 2;// btn.getWidth();//defaultFont.getAdvance(text)
																		// +
																		// (padding
																		// * 2);
	}

	public ImageButtonField(String text) {
		super(Field.FOCUSABLE | ButtonField.CONSUME_CLICK);
		this.text = text;
		fieldHeight = btn_left.getHeight();// defaultFont.getHeight() + padding;
		fieldWidth = btn_left.getWidth() + btn_right.getWidth()
				+ GuiConst.FONT_PLAIN.getAdvance(text) + padding * 2;// btn.getWidth();//defaultFont.getAdvance(text)
																		// +
																		// (padding
																		// * 2);
	}

	public String getLabel() {
		return text;
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

	protected void layout(int arg0, int arg1) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	protected void paint(Graphics graphics) {

		// graphics.drawBitmap(0, 0, fieldWidth, fieldHeight,
		// bitmaparr[focusstatus] , 0, 0);
		int x = 0;
		graphics.drawBitmap(x, 0, btn_left.getWidth(), fieldHeight,
				btn_leftarr[focusstatus], 0, 0);
		x = x + btn_left.getWidth();
		int textwidth = fieldWidth - btn_left.getWidth() - btn_right.getWidth();
		do {
			graphics.drawBitmap(x, 0, btn_center.getWidth(), fieldHeight,
					btn_centerarr[focusstatus], 0, 0);
			textwidth = textwidth - btn_center.getWidth();
			x = x + btn_center.getWidth();
		} while (textwidth > 0);
		graphics.drawBitmap(fieldWidth - btn_rightarr[focusstatus].getWidth(),
				0, btn_right.getWidth(), fieldHeight,
				btn_rightarr[focusstatus], 0, 0);

		graphics.setColor(GuiConst.FONT_COLOR_WHITE);
		if (focusstatus == 0)
			graphics.setFont(GuiConst.FONT_BOLD);
		else
			graphics.setFont(GuiConst.FONT_BOLD_UNDERLINED);
		textwidth = GuiConst.FONT_BOLD.getAdvance(text);
		int textheight = GuiConst.FONT_BOLD.getHeight();
		graphics.drawText(text, (fieldWidth - textwidth) / 2,
				(fieldHeight - textheight) / 2);
	}

	protected boolean navigationClick(int status, int time) {
		System.out.println("navigationClick::" + text + "::");
		focusstatus = 1;
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
		System.out.println("keyChar::" + text + "::");
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
