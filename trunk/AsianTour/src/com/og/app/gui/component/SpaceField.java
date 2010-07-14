package com.og.app.gui.component;
import net.rim.device.api.ui.*;

public class SpaceField extends Field
{
    private int fieldHeight = 0;
    private int fieldWidth = 0;

     public SpaceField  (int width)
     {
            super(Field.NON_FOCUSABLE);
            fieldHeight = 1;
            fieldWidth = width;
   }
     public SpaceField  (int width, int height)
     {
            super(Field.NON_FOCUSABLE);
            fieldHeight = height;
            fieldWidth = width;
   }   

    public int getPreferredWidth()
    {
            return fieldWidth;
    }
    
    public int getPreferredHeight()
    {
            return fieldHeight;
    }
    
    protected void layout(int arg0, int arg1)
    {
            setExtent(getPreferredWidth(), getPreferredHeight());
    }

    public void repaintField(){
        invalidate();
    }
     protected void paint(Graphics graphics)
     {
     }
}
