package com.og.app.gui;

import com.og.app.gui.GuiConst;
import com.og.app.gui.listener.*;
import com.og.app.util.DataCentre;
import com.og.app.util.Utility;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import java.util.*;

public class CustomTableListField extends ListField implements ListFieldCallback, Runnable {
    
    //screen sizes: 320, 360, 480
    
    protected Vector _elements = new Vector();
    
    protected Bitmap header_bg = null;
    protected Bitmap header_separator = null;
    protected Bitmap even_bg = null;
    protected Bitmap even_separator = null;
    protected Bitmap odd_bg = null;
    protected Bitmap odd_separator = null;
    
    protected Object lock = new Object();
    protected ListFieldListener listener = null;
    
    public int table = 1;
    public int row = 0;
    public int padding = 2;
    public int bg_y = 0;
    public int text_y = padding;
    public int text_x = padding;
    public int tmpx = 0;
    public int tmpy = 0;
    public int page = 1;
    public int temp_x = 0;
    
    
    public int this_x = 0;
    public int prev_x = 0;
    public int total_x = 0;
    
    static final int constantColWidth = (GuiConst.SCREENWIDTH / 20);
    
    //header for TV Schedule table
    static final String[] tvLabel = {"Tour Name", "Broadcast Date", "Broadcast Time", "Broadcaster", "Region"};
    //                                  1/2               1               1                  2          2
    static final int[] tvWidth = {constantColWidth*7,constantColWidth*7,constantColWidth*5,constantColWidth*6,constantColWidth*6};
    
    //header for Tour Schedule table
    static final String[] tourLabel = {"Tour Date", "Country", "Tour Name", "Golf Club", "Def Champion", "Prize Money"};
    //                                   1                1        1 /2          2                 2           2
    static final int[] tourWidth = {constantColWidth*5,constantColWidth*5,constantColWidth*9,constantColWidth*3,constantColWidth*4,constantColWidth*4};
    
    //header for Live Score table    
    static final String[] liveLabel = {"Player","Mark", "Country",  "Pos", "To Par", "Hole",  "Today", "R1", "R2", "R3", "R4", "Total"};
    //                                  1/2/3     1        1          2       2        2         3      3     3      3     3      3
    static final int[] liveWidth = {constantColWidth*9,constantColWidth*3,constantColWidth*6, constantColWidth*3,constantColWidth*3,constantColWidth*3, constantColWidth*2,constantColWidth,constantColWidth,constantColWidth,constantColWidth,constantColWidth*3};
    
    //this is for live score
    public CustomTableListField (ListFieldListener listener, int table, int page) {
        super();
        this.listener = listener;
        this.table = table;
        this.page = page;
        this.row = 0;
        //this.text_x = padding;
        //this.text_y = padding;
        setEmptyString("", DrawStyle.HCENTER);
        setCallback(this);
        header_bg = Utility.resizeBitmap(Bitmap.getBitmapResource("res/table_header_bg.png"), this.getPreferredWidth(), 14);//Bitmap.getBitmapResource("res/table_header_bg.png");
        even_bg = Utility.resizeBitmap(Bitmap.getBitmapResource("res/table_even_bg.png"), this.getPreferredWidth(), 14);
        odd_bg = Utility.resizeBitmap(Bitmap.getBitmapResource("res/table_odd_bg.png"), this.getPreferredWidth(), 14);
        
        header_separator = Bitmap.getBitmapResource("res/table_header_separator.png");
        even_separator = Bitmap.getBitmapResource("res/table_even_separator.png");
        odd_separator = Bitmap.getBitmapResource("res/table_odd_separator.png");
        
        
        setRowHeight(header_bg.getHeight());
    }
    
    public void onUnfocus() {
    	super.onUnfocus();
    }
    
    public Vector getAllElements() {
        return _elements;
    }
    
    public int getSize() {
        return _elements.size();
    }
    
    public void addElements(DataCentre element) {
        add(element);
    }
    
    protected void add(DataCentre element) {
        synchronized(lock) {
            _elements.addElement(element);
            setSize(getSize());
        }
    }

    public void run() {}

    public void drawListRow(ListField listField, Graphics g, int index, int y, int width) {
    
        //int rowHeight = getRowHeight();
    
        Font textFont = GuiConst.FONT_TABLE_HEADER;
        
        /* Table No:    
        1 - TV Schedule
        2 - Tour Schedule
        3 - Live Score*/

        switch (table) {
            
            /*TV Schedule*/
            case 1: 
                
                if (page == 1) {
                	
                    if (index == 0) {
                    	setupBackground(g, index);
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        
                        for (int i = 0; i < 3; i++) { 
                        	if ((tvWidth[i]-textFont.getAdvance(tvLabel[i])) > 0){
                        		this_x = ((tvWidth[i]-textFont.getAdvance(tvLabel[i]))/2)+1;
                        	} else {
                        		this_x = 1;
                        	}
                            
                            prev_x += this_x; //+ padding;
                            
                            g.drawText(tvLabel[i], prev_x, text_y);
                            
                            prev_x += textFont.getAdvance(tvLabel[i]) + this_x;
                            
                            System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                            
                            //need to check page
                            if (i != 2) {
                                //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
                                g.drawBitmap(prev_x, text_y-2, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
                                prev_x += header_separator.getWidth();//+ padding;
                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                //temp_x = tvWidth[i];//text_x;
                            }
                        }
                        //next row
                        text_y += header_separator.getHeight();
                        text_x = padding;
                        temp_x = padding;
                        row ++;
                    } else {
                    	setupBackground(g, index);
                        //get content in array and draw list
                    	//setupBackground(g);
                    	System.out.println("this is row index : " + index);
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_BLACK);
                        
                        g.drawText(item.tvName, text_x, text_y);
                        text_x += tvWidth[0];
                        if ((index % 2) == 0) {
                            g.drawBitmap(text_x, text_y-2, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y-2, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth()+ padding;
                        
                        g.drawText(item.tvDate, text_x, text_y);
                        text_x += tvWidth[1];
                        if ((index % 2) == 0) {
                            g.drawBitmap(text_x, text_y-2, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y-2, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth() + padding;
                        
                        g.drawText(item.tvBroadcastTime, text_x, text_y);
                        text_x += tvWidth[2];
                        
                        
                        //this is to set it to the next row
                        text_y += header_separator.getHeight();
                        //reset value
                        text_x = padding;
                        row ++;
                        if (row > index) {
                        	row = 0;
                        	temp_x = 0;
                        	text_x = 0;
                        	prev_x = 0;
                        	bg_y = 0;
                        }
                    }
                } else if (page == 2) {
                    if (row == 0) {
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        
                        for (int i = 0; i < tvLabel.length; i++) { 
                            if ((i == 0) || (i == 3) || (i == 4)) {
                                temp_x += ((tvWidth[i]-textFont.getAdvance(tvLabel[i]))/2);
                                g.drawText(tvLabel[i], temp_x, text_y);
                                text_x += tvWidth[i]; //+ padding;
                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                
                                //need to check page
                                if (i != (tvLabel.length - 1)) {
                                    //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
                                    g.drawBitmap(text_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
                                    text_x += header_separator.getWidth(); //+ padding;
                                    System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                    temp_x += text_x;
                                }
                            }
                        }
                        //next row
                        text_y += header_separator.getHeight() + padding;
                        text_x = padding;
                        temp_x = padding;
                        row ++;
                    } else {
                        //get content in array and draw list
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_TITLE);
                        
                        g.drawText(item.tvName, text_x, text_y);
                        text_x += tvWidth[0];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.tvBroadcaster, text_x, text_y);
                        text_x += tvWidth[3];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.tvRegion, text_x, text_y);
                        text_x += tvWidth[4];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        
                        //this is to set it to the next row
                        text_y += header_separator.getHeight() + padding;
                        //reset value
                        text_x = padding;
                        row ++;
                        
                    }
                }
                break;
                
            /*Tour Schedule*/
            case 2: 
                //setupBackground(g);
                if (page == 1) {
                    if (row == 0) {
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        
                        for (int i = 0; i <= 2; i++) { 
                            temp_x += ((tourWidth[i]-textFont.getAdvance(tourLabel[i]))/2);
                            g.drawText(tourLabel[i], temp_x, text_y);
                            text_x += tourWidth[i]; //+ padding;
                            System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                            
                            //need to check page
                            if (i != 2) {
                                //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
                                g.drawBitmap(text_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
                                text_x += header_separator.getWidth(); //+ padding;
                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                temp_x += text_x;
                            }
                        }
                        //next row
                        text_y += header_separator.getHeight() + padding;
                        text_x = padding;
                        temp_x = padding;
                        row ++;
                    } else {
                        //get content in array and draw list
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_TITLE);
                        //"Tour Date", "Country", "Tour Name", 
                        
                        g.drawText(item.tourDate, text_x, text_y);
                        text_x += tourWidth[0];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.tourCountry, text_x, text_y);
                        text_x += tourWidth[1];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.tourName, text_x, text_y);
                        text_x += tourWidth[2];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        
                        //this is to set it to the next row
                        text_y += header_separator.getHeight() + padding;
                        //reset value
                        text_x = padding;
                        row ++;
                    }
                } else if (page == 2) {
                    if (row == 0) {
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        
                        for (int i = 0; i < tourLabel.length; i++) { 
                            if ((i == 2) || (i == 3) || (i == 4) || (i == 5)) {
                                temp_x += ((tourWidth[i]-textFont.getAdvance(tourLabel[i]))/2);
                                g.drawText(tourLabel[i], temp_x, text_y);
                                text_x += tourWidth[i]; //+ padding;
                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                
                                //need to check page
                                if (i != (tourLabel.length - 1)) {
                                    //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
                                    g.drawBitmap(text_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
                                    text_x += header_separator.getWidth(); //+ padding;
                                    System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                    temp_x += text_x;
                                }
                            }
                        }
                        //next row
                        text_y += header_separator.getHeight() + padding;
                        text_x = padding;
                        temp_x = padding;
                        row ++;
                    } else {
                        //get content in array and draw list
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_TITLE);
                        //"Golf Club", "Def Champion", "Prize Money"
                        g.drawText(item.tourName, text_x, text_y);
                        text_x += tourWidth[2];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.tourGClub, text_x, text_y);
                        text_x += tourWidth[3];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.tourDefChampion, text_x, text_y);
                        text_x += tourWidth[4];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.tourPrize, text_x, text_y);
                        text_x += tourWidth[5];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        //this is to set it to the next row
                        text_y += header_separator.getHeight() + padding;
                        //reset value
                        text_x = padding;
                        row ++;
                    }
                }
                break;
                
            /*Live Score*/
            case 3: 
                //setupBackground(g);
                if (page == 1) {
                    if (row == 0) {
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        
                        for (int i = 0; i <= 2; i++) { 
                            temp_x += ((liveWidth[i]-textFont.getAdvance(liveLabel[i]))/2);
                            g.drawText(liveLabel[i], temp_x, text_y);
                            text_x += liveWidth[i]; //+ padding;
                            System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                            
                            //need to check page
                            if (i != 2) {
                                //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
                                g.drawBitmap(text_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
                                text_x += header_separator.getWidth(); //+ padding;
                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                temp_x += text_x;
                            }
                        }
                        //next row
                        text_y += header_separator.getHeight() + padding;
                        text_x = padding;
                        temp_x = padding;
                        row ++;
                    } else {
                        //get content in array and draw list
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_TITLE);
                        //"Player","Mark", "Country",  
                        
                        //truncation
                        g.drawText(item.ls_playerFirstName, text_x, text_y);
                        text_x += liveWidth[0];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_mark, text_x, text_y);
                        text_x += liveWidth[1];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_country, text_x, text_y);
                        text_x += liveWidth[2];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        
                        //this is to set it to the next row
                        text_y += header_separator.getHeight() + padding;
                        //reset value
                        text_x = padding;
                        row ++;
                    }
                } else if (page == 2) {
                    if (row == 0) {
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        
                        for (int i = 0; i <= 5; i++) { 
                            if ((i == 0) || (i == 3) || (i == 4) || (i == 5)) {
                                temp_x += ((liveWidth[i]-textFont.getAdvance(liveLabel[i]))/2);
                                g.drawText(liveLabel[i], temp_x, text_y);
                                text_x += liveWidth[i]; //+ padding;
                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                
                                //need to check page
                                if (i != 5) {
                                    //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
                                    g.drawBitmap(text_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
                                    text_x += header_separator.getWidth(); //+ padding;
                                    System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                    temp_x += text_x;
                                }
                            }
                        }
                        //next row
                        text_y += header_separator.getHeight() + padding;
                        text_x = padding;
                        temp_x = padding;
                        row ++;
                    } else {
                    	System.out.println("the element's index is: "+ index);
                        //get content in array and draw list
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_TITLE);
                        //"Pos", "To Par", "Hole",  
                        g.drawText(item.ls_playerFirstName, text_x, text_y);
                        text_x += liveWidth[0];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_pos, text_x, text_y);
                        text_x += liveWidth[3];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_toPar, text_x, text_y);
                        text_x += tourWidth[4];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_hole, text_x, text_y);
                        text_x += liveWidth[5];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        //this is to set it to the next row
                        text_y += header_separator.getHeight() + padding;
                        //reset value
                        text_x = padding;
                        row ++;
                    }
                } else if (page == 3) { ///NOT YET DONE!!!
                    if (row == 0) {
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        
                        for (int i = 0; i <= 5; i++) { 
                            if ((i == 0) || (i == 6) || (i == 7) || (i == 8) || (i == 9) || (i == 10) || (i == 11)) {
                                temp_x += ((liveWidth[i]-textFont.getAdvance(liveLabel[i]))/2);
                                g.drawText(liveLabel[i], temp_x, text_y);
                                text_x += liveWidth[i]; //+ padding;
                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                
                                //need to check page
                                if (i != 5) {
                                    //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
                                    g.drawBitmap(text_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
                                    text_x += header_separator.getWidth(); //+ padding;
                                    System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                                    temp_x += text_x;
                                }
                            }
                        }
                        //next row
                        text_y += header_separator.getHeight() + padding;
                        text_x = padding;
                        temp_x = padding;
                        row ++;
                    } else {
                        //get content in array and draw list
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_TITLE);
                        //"Today", "R1", "R2", "R3", "R4", "Total"
                        g.drawText(item.ls_playerFirstName, text_x, text_y);
                        text_x += liveWidth[0];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_today, text_x, text_y);
                        text_x += liveWidth[6];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_r1, text_x, text_y);
                        text_x += tourWidth[7];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_r2, text_x, text_y);
                        text_x += liveWidth[8];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_r3, text_x, text_y);
                        text_x += liveWidth[9];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_r4, text_x, text_y);
                        text_x += liveWidth[10];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        g.drawText(item.ls_r2, text_x, text_y);
                        text_x += liveWidth[11];
                        if ((row % 2) == 0) {
                            g.drawBitmap(text_x, text_y, even_separator.getWidth(), even_separator.getHeight(), even_separator, 0,0);
                        } else {
                            g.drawBitmap(text_x, text_y, odd_separator.getWidth(), odd_separator.getHeight(), odd_separator, 0,0);
                        }
                        text_x += header_separator.getWidth();
                        
                        //this is to set it to the next row
                        text_y += header_separator.getHeight() + padding;
                        //reset value
                        text_x = padding;
                        row ++;
                    }
                }
                break;
        }
    //192.168.1.48 HPJET 3030
    }
    
    public void setupBackground(Graphics g, int index) {
        if (index == 0) {
            /* header */
            g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg.getHeight(), header_bg, 0,0);
            System.out.println("1background = row"  + row + " height:" + header_bg.getHeight() + " bg_y: " + bg_y); 
        } else if ((index != 0) && ((index % 2) == 0)) {
            /* even row */
            g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg.getHeight(), even_bg, 0,0);
            System.out.println("2background = row"  + row + " height:" + header_bg.getHeight() + " bg_y: " + bg_y); 
        } else if ((index != 0) && ((index % 2) == 1)) {
            /* odd row */
            g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg.getHeight(), odd_bg, 0,0);
            System.out.println("3background = row"  + row + " height:" + header_bg.getHeight() + " bg_y: " + bg_y); 
        }
        bg_y += header_bg.getHeight();
        //row+=1;
    }
    
    protected boolean navigationMovement(int dx,
                                     int dy,
                                     int status,
                                     int time) {

         //System.out.println("navigationMovement:"+dx+","+dy+","+status+","+time);        
         //invalidate();
         return false;
    }
    
    public Object get(ListField listField, int index) {
        if ((index >= 0) && (index < getSize())) {
            return _elements.elementAt(index);
        }
        return null;
    }
    
    public int getPreferredWidth(ListField listField) {
        return Display.getWidth();
    }
    
    public int indexOfList(ListField listField, String prefix, int start) {
        return listField.indexOfList(prefix, start);
    }
    
    public boolean keyChar(char key, int status, int time) {
        if (getSize() > 0 && key == Characters.SPACE) {
            getScreen().scroll(Manager.DOWNWARD);
            return true;
        }
        return super.keyChar(key, status, time);
    }
    
    public synchronized void saveChanges(DataCentre ni, int index) {
        try {
            //to save news as loaded/read
            System.out.println("saveeeededdd");
        } catch (Exception e) {
            System.out.println("aloy.CustomListField.exceptione.saveChanges(ni, index):"+e);
        }
    }
    
    public boolean navigationClick(int status, int time) {
        System.out.println("aloy.CustomListField.navigationClick: got enter or not?");
        
        
        return false;
    }
    
    public void remove(int index) {
        synchronized(lock){
            _elements.removeElementAt(index);
            //_pelements.removeElementAt(index);
            setSize(getSize());
        }
        invalidate();
        
    }
        
    protected void removeAll() {
        synchronized(lock){
            _elements.removeAllElements();
            //_pelements.removeAllElements();
            setSize(0);
        }
        invalidate();
    }
} 
