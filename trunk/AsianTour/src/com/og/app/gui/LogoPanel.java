package com.og.app.gui;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class LogoPanel extends HorizontalFieldManager {
    private static LogoPanel logoPanel = null;
    private static Bitmap logoIcon = null;
    private static String imgName = "default.png";
    //private static int tabID = 1;
    
    public synchronized static LogoPanel getInstance() {
        if (logoPanel == null) {
            logoPanel = new LogoPanel();            
            System.out.println("--------------------------\nres/titleBar/W" + GuiConst.SCREENWIDTH + "/" + imgName);
            logoIcon = Bitmap.getBitmapResource("res/titleBar/W" + GuiConst.SCREENWIDTH + "/" + imgName);
        }
        return logoPanel;
    }
    
    private LogoPanel()
    {
        super(Field.USE_ALL_WIDTH);
    }
    
    protected void paint (Graphics g) {
    	int x = 0;
        g.drawBitmap(x,0,logoIcon.getWidth(),logoIcon.getHeight(),logoIcon, 0,0);
    }
    
    //------------------------- Mandatory ------------------------------
    protected void sublayout(int width, int height)
    {
        super.sublayout(width, logoIcon.getHeight());
        setExtent(width, logoIcon.getHeight());
    }
    
    public void repaint() {
        this.invalidate();
    }
    
    public int getPreferredWidth()
    {
        return GuiConst.SCREENWIDTH;
    }
        
    public int getPreferredHeight()
    {
        return logoIcon.getHeight();
    }
    
    public void repaintLogoPanel(String imgName) {
    	logoPanel = null;
    	this.imgName = imgName + ".png";
    }
} 
