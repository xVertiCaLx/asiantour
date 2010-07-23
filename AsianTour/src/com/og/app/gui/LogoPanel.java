package com.og.app.gui;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class LogoPanel extends HorizontalFieldManager {
    private static LogoPanel logoPanel = null;
    private static Bitmap logoIcon = null;
    private static int tabID = 1;
    
    public synchronized static LogoPanel getInstance() {
        if (logoPanel == null) {
            logoPanel = new LogoPanel();
//            if(GuiConst.SCREENWIDTH == 320) {
//        		logoIcon = Bitmap.getBitmapResource("res/titleBar/W380/default.png");
//        	} else if (GuiConst.SCREENWIDTH == 360) {
//        		logoIcon = Bitmap.getBitmapResource("res/titleBar/W360/default.png.png");
//        	} else if (GuiConst.SCREENWIDTH == 480) {
//        		logoIcon = Bitmap.getBitmapResource("res/titleBar/W480/default.png.png");
//        	} 
            
            logoIcon = Bitmap.getBitmapResource("res/titleBar/W" + GuiConst.SCREENWIDTH + "/default.png");
        } else {
        	if (tabID == 1) {
        		logoIcon = Bitmap.getBitmapResource("res/titleBar/W" + GuiConst.SCREENWIDTH + "/default.png");
        	}
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
    
    public Font setFontSize(int font_size){
        try {
            FontFamily ff = FontFamily.forName("");

            return ff.getFont(Font.BOLD, font_size);
        } catch (Exception e) {}

        Font defaultFont =Font.getDefault();
        return defaultFont.getFontFamily().getFont( Font.UNDERLINED, font_size);
    } 
} 
