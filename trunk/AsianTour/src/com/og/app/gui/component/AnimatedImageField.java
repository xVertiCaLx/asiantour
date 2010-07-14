package com.og.app.gui.component;

import com.og.app.gui.*;
import com.og.app.*;
import com.og.app.util.*;
import com.og.rss.*;
import java.util.Vector; 
import java.io.*;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.*;

import javax.microedition.io.*;

public class AnimatedImageField  extends BitmapField implements Runnable
{  
    private String imgurl = "";
    private byte[] imgbytes = null;
    private Thread thread = null;
    private boolean animate=true;
    private int interval = 0;
    private int index=0;
    private Bitmap bitmap = null;
    private int frameno = 0;
    private int fieldHeight=0;
    private int fieldWidth=0;
    private Bitmap finalbitmap = null;
    private int imgHeight = 0;
    private int imgWidth= 0;
    
    public AnimatedImageField(int fieldwidth, int fieldheight, Bitmap bitmap, int frameno, int interval, long style)
    {
        super(bitmap, style);
        this.interval=interval;
        this.bitmap=bitmap;
        this.frameno=frameno;
        imgHeight = bitmap.getHeight();
        int imgwidth = bitmap.getWidth();
        imgWidth=imgwidth/frameno;
        this.fieldWidth = fieldwidth;
        this.fieldHeight = fieldheight;
    }
        
    public AnimatedImageField(Bitmap bitmap, int frameno, int interval, long style)
    {
        super(bitmap, style);
        this.interval=interval;
        this.bitmap=bitmap;
        this.frameno=frameno;
        imgHeight = bitmap.getHeight();
        int imgwidth = bitmap.getWidth();
        imgWidth=imgwidth/frameno;
        fieldWidth = imgWidth;
        fieldHeight = imgHeight;
    }
    
    protected void paint(Graphics graphics){
        graphics.setColor(GuiConst.FONT_COLOR_WHITE);
        graphics.fillRect(0,0,this.getPreferredWidth(), this.getPreferredHeight());
                
        //System.out.println("animate:"+animate);
        if ( animate )
            graphics.drawBitmap((fieldWidth-imgWidth)/2, (fieldHeight-imgHeight)/2, 
                imgWidth, bitmap.getHeight(), bitmap , imgWidth*index, 0);     
        else
            graphics.drawBitmap((fieldWidth-finalbitmap.getWidth())/2, (fieldHeight-finalbitmap.getHeight())/2, 
                finalbitmap.getWidth(), finalbitmap.getHeight(), finalbitmap , 0, 0);     
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
        
    public void startAnimation(){
         //System.out.println("startAnimation");
        animate=true;
        thread = new Thread(this);
        thread.start();
    }
    
    public void updateLayout(int height, int width){
        //System.out.println("updateLayout:height:"+height);
        this.fieldHeight=height;
        this.fieldWidth=width;
        super.updateLayout();
    }
            
    public void stopAnimation(Bitmap bitmap){
        //System.out.println("stopAnimation");        
        this.finalbitmap=bitmap;
        animate=false;
    }    
    
    public void stopAnimation(){
        //System.out.println("stopAnimation");
        animate=false;
    }    

    public void run(){
            while(animate){ 
                //System.out.println("run:animate:"+animate);
                try{ Thread.sleep(interval);}catch(Exception e){}
                if ( index+1>=frameno )
                    index=0;
                else
                    index++;
                invalidate();
            }
    }
}

