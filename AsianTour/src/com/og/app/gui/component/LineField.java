package com.og.app.gui.component;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.*;

public class LineField extends Field
{
    private int fieldHeight = 0;
    private int fieldWidth = 0;
    private int color=-1;
    
     public LineField  (int height, int color)
     {
            super(Field.NON_FOCUSABLE);
            fieldHeight = height;
            fieldWidth = Display.getWidth();
            this.color=color;
   }
       
     public LineField  (int height)
     {
            super(Field.NON_FOCUSABLE);
            fieldHeight = height;
            fieldWidth = Display.getWidth();
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
    
    protected void drawFocus(Graphics graphics, boolean on)
    {
    
    }
     protected void paint(Graphics graphics)
     {
            if ( color!=-1 ){
                graphics.setColor(color);
                graphics.fillRect(0,0,fieldWidth,fieldHeight);
            }
     }
        
     protected void fieldChangeNotify(int context)
    {
              try
            {
                      this.getChangeListener().fieldChanged(this, context);
          }
              catch (Exception e)
            {}
     }
}
