package com.og.app.gui.component;

import com.og.app.gui.GuiConst;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;

public class CarouselMenuField extends ListField implements Runnable {
	private Thread thread = null;
	private boolean animate = true;
	private int interval = 70;
	private int index = 0;
	private Bitmap bitmap = null;
	private int frameno = 5;
	private int fieldHeight = 120;
	private int fieldWidth = GuiConst.SCREENWIDTH;
	private Bitmap bg = null;

	private Bitmap finalbitmap = null;
	private int imgHeight = 120;
	private int imgWidth = 320;
	public String menu;
	private String movement;

	public CarouselMenuField(String menu, Bitmap bitmap, Bitmap finalbitmap,
			String movement, int fieldHeight) {
		super();
		this.bitmap = bitmap;
		this.menu = menu;
		this.movement = movement;
		this.fieldHeight = fieldHeight;
		this.finalbitmap = finalbitmap;
		bg = Bitmap.getBitmapResource("res/titleBar/W" + GuiConst.SCREENWIDTH
				+ "/background.jpg");
		if (movement == "right") {
			index = 0;
		} else {
			index = 4;
		}
	}

	protected void paint(Graphics graphics) {

		graphics.drawBitmap(0, 0, bg.getWidth(), fieldHeight, bg, 0, 0);

		if (animate)
			graphics.drawBitmap((fieldWidth - imgWidth) / 2,
					((fieldHeight - imgHeight) / 2)-10, imgWidth,
					bitmap.getHeight(), bitmap, imgWidth * index, 0);
		else {
			if (movement == "right")
				graphics.drawBitmap((fieldWidth - imgWidth) / 2,
						((fieldHeight - imgHeight) / 2)-10, imgWidth, bitmap
								.getHeight(), bitmap, imgWidth * index, 0);
			else
				graphics.drawBitmap((fieldWidth - imgWidth) / 2,
						((fieldHeight - imgHeight) / 2)-10, imgWidth, finalbitmap
								.getHeight(), finalbitmap, imgWidth*4, 0);
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

	public void startAnimation() {
		// System.out.println("startAnimation");
		animate = true;
		thread = new Thread(this);
		thread.start();
	}

	public String checkCurrentMenu() {
		return menu;
	}

	public boolean checkRunStatus() {
		return animate;
	}

	public void updateLayout(int height, int width) {
		// System.out.println("updateLayout:height:"+height);
		this.fieldHeight = height;
		this.fieldWidth = width;
		super.updateLayout();
	}

	public void stopAnimation() {
		animate = false;
	}

	public void run() {
		while (animate) {
			// System.out.println("run:animate:"+animate);
			try {
				Thread.sleep(interval);
			} catch (Exception e) {
			}
			if (movement == "right") {
				if (index + 1 == frameno)
					stopAnimation();
				else
					index++;
			} else if (movement == "left") {
				if (index == 0)
					stopAnimation();
				else
					index--;
			}

			invalidate();
		}
	}

}
