package com.og.app.gui;

import com.og.app.*;

import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.component.*;

public class LogoPanel extends HorizontalFieldManager {
    private static LogoPanel logoPanel = null;
    private Bitmap logoIcon = Bitmap.getBitmapResource("res/logo.png");
    
    public synchronized static LogoPanel getInstance() {
        if (logoPanel == null) {
            logoPanel = new LogoPanel();
        }
        return logoPanel;
    }
    
    private LogoPanel()
    {
        super(Field.USE_ALL_WIDTH);
    }
    
    protected void paint (Graphics g) {
        g.drawBitmap(0,0,logoIcon.getWidth(),logoIcon.getHeight(),logoIcon, 0,0);
        //Font font = setFontSize(GuiConst.FONT_BOLD);//((logoIcon.getHeight()-7)/3));
        g.setFont(GuiConst.FONT_BOLD);
        g.setColor(GuiConst.FONT_COLOR_BLACK);
        //int line1width = font.getAdvance("Asian Tour BlackBerry Application");
        String applicationTitle = "Asian Tour BlackBerry App";
        g.drawText(applicationTitle, ((GuiConst.SCREENWIDTH - GuiConst.FONT_BOLD.getAdvance(applicationTitle)/*font.getAdvance(applicationTitle)*/)/2), logoIcon.getHeight()/3);
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
