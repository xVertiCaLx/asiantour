package com.og.app.gui.component;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.Dialog;

public class CustomDialog extends Dialog {
	
	private Bitmap thisImage = Bitmap.getBitmapResource("res/titleBar/logo.png");
	
	public CustomDialog(String message) {
		super(Dialog.D_OK, message, 1, Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION), Manager.FOCUSABLE);
	}

	public void paint(Graphics graphics) {        
        graphics.setBackgroundColor(Color.RED);
        graphics.drawBitmap(0, 0, thisImage.getWidth(), thisImage.getHeight(), thisImage, 0, 0);
        graphics.clear();
        super.paint(graphics);
    } 
	
}
