package com.og.app.gui.component;

import com.og.app.util.*;
import com.og.app.gui.GuiConst;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;

public class TitleField extends Field {
    final Bitmap bg_title = Bitmap.getBitmapResource("res/bg_title_320.png");
    
    private String text = "";
    private int padding = 3;
    private int fieldHeight = 0;
    private int fieldWidth = 0;
    private Bitmap finalBg = null;
    private Bitmap icon = null;
    
    public TitleField(String text, Bitmap icon) {
        super(Field.NON_FOCUSABLE);
        this.text = text;
        this.icon = icon;
        fieldHeight = GuiConst.FONT_BOLD.getHeight() + padding * 2;
        if (icon.getHeight() + 2 > fieldHeight) {
            fieldHeight = icon.getHeight() + 2;
        }
        fieldWidth = Display.getWidth();// = GuiConst.SCREENWIDTH;
        finalBg = Utility.resizeBitmap(bg_title, fieldWidth, fieldHeight);
    }
    
    protected void paint(Graphics g) {
        g.drawBitmap(0,0, fieldWidth, fieldHeight, finalBg, 0, 0);
        g.drawBitmap(padding, ((fieldHeight - icon.getHeight())/2), icon.getWidth(), icon.getHeight(), icon, 0, 0);
        g.setFont(GuiConst.FONT_BOLD);
        g.setColor(GuiConst.FONT_COLOR_TITLE);
        g.drawText(text, padding + icon.getWidth() + 3, ((fieldHeight - GuiConst.FONT_BOLD.getHeight())/2));
    }
    
    public void repaintScreen() {
        fieldHeight = GuiConst.FONT_BOLD.getHeight() + padding * 2;
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
    
} 
