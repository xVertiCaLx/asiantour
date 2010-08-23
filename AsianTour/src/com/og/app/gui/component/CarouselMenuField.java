package com.og.app.gui.component;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;

public class CarouselMenuField extends BitmapField implements Runnable {
	private Thread thread = null;
	private boolean animate = true;
	private int interval = 100;
	private int index = 0;
	private Bitmap bitmap = null;
	private int frameno = 5;
	private int fieldHeight = 120;
	private int fieldWidth = net.rim.device.api.system.Display.getWidth();
	private Bitmap finalbitmap = null;
	private int imgHeight = 120;
	private int imgWidth = 320;
	public String menu;
	

	public CarouselMenuField(String menu, Bitmap bitmap,
			long style) {
		super(bitmap, style);
		this.bitmap = bitmap;
		this.menu = menu;
	}

	protected void paint(Graphics graphics) {
		if (animate)
			graphics.drawBitmap((fieldWidth - imgWidth) / 2,
					(fieldHeight - imgHeight) / 2, imgWidth,
					bitmap.getHeight(), bitmap, imgWidth * index, 0);
		else
			graphics.drawBitmap((fieldWidth - finalbitmap.getWidth()) / 2,
					(fieldHeight - finalbitmap.getHeight()) / 2, finalbitmap
							.getWidth(), finalbitmap.getHeight(), finalbitmap,
					0, 0);
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
		// System.out.println("stopAnimation");
		index = 0;
		animate = false;
		finalbitmap = Bitmap.getBitmapResource(menu + ".png");
	}

	public void run() {
		while (animate) {
			// System.out.println("run:animate:"+animate);
			try {
				Thread.sleep(interval);
			} catch (Exception e) {
			}
			/*if (index + 1 >= frameno)
				index = 0;
			else*/ if (index + 1 == frameno)
				stopAnimation();
			else
				index++;
			invalidate();
		}
	}
	
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		if (dx > 0) {
			//right
			System.out.println("right movement");
			if (checkCurrentMenu() == null) {
				
			} else if (checkCurrentMenu() == "news") {
				if (!checkRunStatus()) {
					bitmap = Bitmap.getBitmapResource("oom_transit.png");
					//menu = new CarouselMenuField("oom", image, Field.FIELD_HCENTER);
					startAnimation();
				}
			}
			
		} else if (dx < 0) {
			//left
		}
		return false;
	}
}
