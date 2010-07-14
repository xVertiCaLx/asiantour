package com.og.app.gui.component;

import com.og.app.gui.GuiConst;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;

public class EmptyTabField extends Field {
    final Bitmap btn_nonfocusable_left= Bitmap.getBitmapResource("res/tabLeftNoFocus.png");
    final Bitmap btn_nonfocusable_right= Bitmap.getBitmapResource("res/tabRightNoFocus.png");
    final Bitmap btn_nonfocusable_center= Bitmap.getBitmapResource("res/tabMiddleNoFocus.png");
        
    final Bitmap btn_left= Bitmap.getBitmapResource("res/tabLeft.png");
    final Bitmap btn_right= Bitmap.getBitmapResource("res/tabRight.png");
    final Bitmap btn_center= Bitmap.getBitmapResource("res/tabMiddle.png");
    
    Bitmap[] btn_leftarr = new Bitmap[]{btn_left, btn_nonfocusable_left}; 
    Bitmap[] btn_rightarr = new Bitmap[]{btn_right, btn_nonfocusable_right}; 
    Bitmap[] btn_centerarr = new Bitmap[]{btn_center, btn_nonfocusable_center};
    
    private int fieldWidth;
    private int fieldHeight;
    private int imgindex  = 0;
    
    public EmptyTabField  (int fieldwidth, boolean setunfocusimg) {
            super(Field.NON_FOCUSABLE);
            if ( setunfocusimg ) {
                imgindex=1;
            }
            this.fieldWidth = fieldwidth;
            init();            
    }
        
   private void init() {
        try {

            fieldHeight = btn_leftarr[imgindex].getHeight();//defaultFont.getHeight() + padding;
        } catch(Exception e) {
            //System.out.println("ERROR:TabField:"+e);
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

    protected void paint(Graphics graphics) {
        int color = GuiConst.TABCOLOR_NORMAL;
        graphics.setColor(color);
        int x = 0;
        int textwidth = fieldWidth;
        do {
            graphics.drawBitmap(x, 0, btn_centerarr[imgindex].getWidth(), fieldHeight, btn_centerarr[imgindex], 0, 0);
            textwidth=textwidth-btn_centerarr[imgindex].getWidth();
            x=x+btn_centerarr[imgindex].getWidth();
            
        } while(textwidth > 0);
    }
} 
