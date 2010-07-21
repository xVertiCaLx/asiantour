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
    protected Bitmap prev_icon = null;
    protected Bitmap next_icon = null;
    protected Bitmap border = null;
    
    protected Object lock = new Object();
    protected ListFieldListener listener = null;
    
    public int table = 1;
    public int row = 0;
    public int padding = 2;
    public int bg_y = 0;
    public int text_y = padding;
    public int text_x = padding;
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
    
    static final String [] meritLabel = {"Player", "Pos", " Earnings"};
    static final int[] meritWidth = {};
    
    //this is for live score
    public CustomTableListField (ListFieldListener listener, int table, int page) {
        super();
        this.listener = listener;
        this.table = table;
        this.page = page;
        this.row = 0;
        
        setEmptyString("", DrawStyle.HCENTER);
        setCallback(this);
        
        header_bg = Utility.resizeBitmap(Bitmap.getBitmapResource("res/table_header_bg.png"), this.getPreferredWidth(), 22);//Bitmap.getBitmapResource("res/table_header_bg.png");
        even_bg = Utility.resizeBitmap(Bitmap.getBitmapResource("res/table_even_bg.png"), this.getPreferredWidth(), 34);
        odd_bg = Utility.resizeBitmap(Bitmap.getBitmapResource("res/table_odd_bg.png"), this.getPreferredWidth(), 34);
        border = Utility.resizeBitmap(Bitmap.getBitmapResource("res/table_border.png"), this.getPreferredWidth(), 1);
        
        header_separator = Bitmap.getBitmapResource("res/table_header_separator.png");
        even_separator = Bitmap.getBitmapResource("res/table_even_separator.png");
        odd_separator = Bitmap.getBitmapResource("res/table_odd_separator.png");
        prev_icon = Bitmap.getBitmapResource("res/table_prev_page.png");
        next_icon = Bitmap.getBitmapResource("res/table_next_page.png");
        
        setRowHeight(header_bg.getHeight() + border.getHeight());
        
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
    	
    	System.out.println("BIG ALERT!!!!!! aloy.drawList.index:"+ index);
        Font textFont = GuiConst.FONT_TABLE_HEADER;
        
        /* Table No:    
        1 - TV Schedule
        2 - Tour Schedule
        3 - Live Score
        4 - Order of Merit*/
        System.out.println("SWITCH TABLE NUMBER IS WHAT NUMBER?: " + table);
        switch (table) {
            
            /*TV Schedule*/
            case 1: 
                if (page == 1) {
                	
                    if (index == 0) {
                    	
                    	setupBackground(g, index, y);
                    	System.out.println("aloy.enter case 1 and row 0");
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        text_y = (header_bg.getHeight() - textFont.getHeight())/2;
                        for (int i = 0; i < 3; i++) { 
                        	if ((tvWidth[i]-textFont.getAdvance(tvLabel[i])) > 0){
                        		this_x = ((tvWidth[i]-textFont.getAdvance(tvLabel[i]))/2)+1;
                        	} else {
                        		this_x = 1;
                        	}
                            
                        	//text_y = y+2;
                        	System.out.println(text_y + " "+ y);
                            prev_x += this_x; //+ padding;
                            
                            g.drawText(tvLabel[i], prev_x, text_y);
                            
                            prev_x += textFont.getAdvance(tvLabel[i]) + this_x;
                            
                            System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
                            
                            //need to check page
//                            if (i != 2) {
//                                //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
//                                g.drawBitmap(prev_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
//                                prev_x += header_separator.getWidth();//+ padding;
//                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
//                                //temp_x = tvWidth[i];//text_x;
//                            } else {
//                            	g.drawBitmap((GuiConst.SCREENWIDTH - next_icon.getWidth()), text_y-2, next_icon.getWidth(), next_icon.getHeight(), next_icon, 0,0);
//                            }
                            
                            if (i == 2) {
                            	g.drawBitmap((GuiConst.SCREENWIDTH - next_icon.getWidth()), text_y-2, next_icon.getWidth(), next_icon.getHeight(), next_icon, 0,0);
                            }
                            
                        }
                        //next row
                        //text_y += header_separator.getHeight();
                        if (getRowHeight() == (header_bg.getHeight() + border.getHeight())) {
                    		setRowHeight(odd_bg.getHeight() + border.getHeight());
                    	}
                        text_x = padding;
                        temp_x = 0;
                        prev_x = 0;
                        row ++;
                    } else {
                    	
                    	setupBackground(g, index, y);
                    	
                        //get content in array and draw list
                    	//setupBackground(g);
                    	text_y = y+padding;
                    	text_x = padding;
                    	temp_x = padding;
                    	
                    	System.out.println("this is row index : " + index);
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        
                        String printText = item.tvName;
                        Vector vText = Utility.breakIntoWords(printText);
                        int lineNo = 1;
                        
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_BLACK);
                        
                        if (textFont.getAdvance(printText) <= tvWidth[0]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[0]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[0]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tvWidth[0]+ padding + header_separator.getWidth() + padding;//+tvWidth[1];
                        text_x = temp_x;
                        lineNo = 1;
                        
                        printText = item.tvDate;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tvWidth[1]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[1]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[1]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tvWidth[0]+ padding + header_separator.getWidth() + padding + tvWidth[1] + padding;
                        text_x = temp_x;
                        lineNo = 1;
                        
                        printText = item.tvBroadcastTime;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tvWidth[2]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[2]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[2]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        row ++;
                        if (row > index) {
                        	row = 0;
                        	temp_x = padding;
                        	text_x = padding;
                        	prev_x = 0;
                        	bg_y = y;
                        }
                    }
                } else if (page == 2) {
                    if (index == 0) {
                    	setupBackground(g, index, y);
                    	System.out.println("aloy.enter case 1 and row 0");
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        text_y = (header_bg.getHeight() - textFont.getHeight())/2;
                        for (int i = 0; i < tvLabel.length; i++) { 
	                    	if ((i == 0)||(i == 3) || (i == 4)){
	                        	if ((tvWidth[i]-textFont.getAdvance(tvLabel[i])) > 0){
	                        		this_x = ((tvWidth[i]-textFont.getAdvance(tvLabel[i]))/2)+1;
	                        	} else {
	                        		this_x = 1;
	                        	}
	                            
	                        	//text_y = y+2;
	                        	System.out.println(text_y + " "+ y);
	                            prev_x += this_x; //+ padding;
	                            
	                            g.drawText(tvLabel[i], prev_x, text_y);
	                            
	                            prev_x += textFont.getAdvance(tvLabel[i]) + this_x;
	                            
	                            System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
	                            
	                            //need to check page
//	                            if (i < tvLabel.length) {
//	                                //if i equals tvLabel.length, it is already the last colomn, no need to have another separator
//	                                g.drawBitmap(prev_x, text_y, header_separator.getWidth(), header_separator.getHeight(), header_separator, 0,0);
//	                                prev_x += header_separator.getWidth();//+ padding;
//	                                System.out.println("aloy.CustomTableListField.drawList.switch1: text_x: "+text_x);
//	                                //temp_x = tvWidth[i];//text_x;
//	                            } else {
//	                            	g.drawBitmap((GuiConst.SCREENWIDTH - next_icon.getWidth()), text_y-2, next_icon.getWidth(), next_icon.getHeight(), next_icon, 0,0);
//	                            }
	                        }
                        }
                        //next row
                        //text_y += header_separator.getHeight();
                        if (getRowHeight() == (header_bg.getHeight() + border.getHeight())) {
                    		setRowHeight(odd_bg.getHeight() + border.getHeight());
                    	}
                        text_x = padding;
                        temp_x = 0;
                        prev_x = 0;
                        row ++;
                    } else {
                    	
                    	setupBackground(g, index, y);
                    	
                        //get content in array and draw list
                    	//setupBackground(g);
                    	text_y = y+padding;
                    	text_x = padding;
                    	temp_x = padding;
                    	
                    	System.out.println("this is row index : " + index);
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        
                        String printText = item.tvName;
                        Vector vText = Utility.breakIntoWords(printText);
                        int lineNo = 1;
                        
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_BLACK);
                        
                        if (textFont.getAdvance(printText) <= tvWidth[0]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[0]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[0]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tvWidth[0]+ padding + header_separator.getWidth() + padding;//+tvWidth[1];
                        text_x = temp_x;
                        lineNo = 1;
                        
                        printText = item.tvBroadcaster;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tvWidth[3]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[3]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[3]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tvWidth[0]+ padding + header_separator.getWidth() + padding + tvWidth[1] + padding;
                        text_x = temp_x;
                        lineNo = 1;
                        
                        printText = item.tvRegion;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tvWidth[2]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[4]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[4]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        row ++;
                        if (row > index) {
                        	row = 0;
                        	temp_x = padding;
                        	text_x = padding;
                        	prev_x = 0;
                        	bg_y = y;
                        }
                    }
                }
                break;
                
            /*Tour Schedule*/
            case 2: 

                System.out.println("i'm printing second table");
        		if (page == 1) {
                	
                    if (index == 0) {
                    	
                    	setupBackground(g, index, y);
                    	
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        text_y = (header_bg.getHeight() - textFont.getHeight())/2;
                        
                        for (int i = 0; i < 2; i++) { 
                        	if ((tourWidth[i]-textFont.getAdvance(tourLabel[i])) > 0){
                        		this_x = ((tourWidth[i]-textFont.getAdvance(tourLabel[i]))/2)+1;
                        	} else {
                        		this_x = 1;
                        	}
                            
                        	//text_y = y+2;
                        	System.out.println(text_y + " "+ y);
                            prev_x += this_x; //+ padding;
                            
                            g.drawText(tourLabel[i], prev_x, text_y);
                            
                            prev_x += textFont.getAdvance(tourLabel[i]) + this_x;

                            if (i == 2) {
                            	g.drawBitmap((GuiConst.SCREENWIDTH - next_icon.getWidth()), text_y-2, next_icon.getWidth(), next_icon.getHeight(), next_icon, 0,0);
                            }
                            
                        }
                        //next row
                        //text_y += header_separator.getHeight();
                        if (getRowHeight() == (header_bg.getHeight() + border.getHeight())) {
                    		setRowHeight(odd_bg.getHeight() + border.getHeight());
                    	}
                        text_x = padding;
                        temp_x = 0;
                        prev_x = 0;
                        row ++;
                    } else {
                    	
                    	setupBackground(g, index, y);
                    	
                        //get content in array and draw list
                    	//setupBackground(g);
                    	text_y = y+padding;
                    	text_x = padding;
                    	temp_x = padding;
                    	
                    	System.out.println("this is row index : " + index);
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        //"Tour Date", "Country", "Tour Name", "Golf Club", "Def Champion", "Prize Money"
                        String printText = item.tourDate;
                        Vector vText = Utility.breakIntoWords(printText);
                        int lineNo = 1;
                        
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_BLACK);
                        
                        if (textFont.getAdvance(printText) <= tourWidth[0]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tourWidth[0]) || ((lineNo == 2) && (text_x+wordWidth >= ((tourWidth[0]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tourWidth[0]+ padding + header_separator.getWidth() + padding;//+tvWidth[1];
                        text_x = temp_x;
                        lineNo = 1;
                        //"Tour Date", "Country", "Tour Name", "Golf Club", "Def Champion", "Prize Money"
                        printText = item.tourCountry;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tourWidth[1]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tourWidth[1]) || ((lineNo == 2) && (text_x+wordWidth >= ((tourWidth[1]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tvWidth[0]+ padding + header_separator.getWidth() + padding + tvWidth[1] + padding;
                        text_x = temp_x;
                        lineNo = 1;
                        
                        printText = item.tvBroadcastTime;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tvWidth[2]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[2]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[2]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        row ++;
                        if (row > index) {
                        	row = 0;
                        	temp_x = padding;
                        	text_x = padding;
                        	prev_x = 0;
                        	bg_y = y;
                        }
                    }
                } else if (page == 2) {
                    if (index == 0) {
                    	setupBackground(g, index, y);
                    	System.out.println("aloy.enter case 1 and row 0");
                        //if row is zero, it is a header row, set up Table Header
                        g.setColor(GuiConst.FONT_COLOR_WHITE);
                        g.setFont(textFont);
                        text_y = (header_bg.getHeight() - textFont.getHeight())/2;
                        for (int i = 0; i < tvLabel.length; i++) { 
	                    	if ((i == 0)||(i == 3) || (i == 4)){
	                        	if ((tvWidth[i]-textFont.getAdvance(tvLabel[i])) > 0){
	                        		this_x = ((tvWidth[i]-textFont.getAdvance(tvLabel[i]))/2)+1;
	                        	} else {
	                        		this_x = 1;
	                        	}
	                            
	                        	//text_y = y+2;
	                        	System.out.println(text_y + " "+ y);
	                            prev_x += this_x; //+ padding;
	                            
	                            g.drawText(tvLabel[i], prev_x, text_y);
	                            
	                            prev_x += textFont.getAdvance(tvLabel[i]) + this_x;
	                        }
                        }
                        //next row
                        if (getRowHeight() == (header_bg.getHeight() + border.getHeight())) {
                    		setRowHeight(odd_bg.getHeight() + border.getHeight());
                    	}
                        text_x = padding;
                        temp_x = 0;
                        prev_x = 0;
                        row ++;
                    } else {
                    	
                    	setupBackground(g, index, y);
                    	
                        //get content in array and draw list
                    	//setupBackground(g);
                    	text_y = y+padding;
                    	text_x = padding;
                    	temp_x = padding;
                    	
                    	System.out.println("this is row index : " + index);
                        DataCentre item = (DataCentre)_elements.elementAt(index);
                        
                        String printText = item.tvName;
                        Vector vText = Utility.breakIntoWords(printText);
                        int lineNo = 1;
                        
                        textFont = GuiConst.FONT_TABLE;
                        g.setFont(textFont);
                        g.setColor(GuiConst.FONT_COLOR_BLACK);
                        
                        if (textFont.getAdvance(printText) <= tvWidth[0]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[0]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[0]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tvWidth[0]+ padding + header_separator.getWidth() + padding;//+tvWidth[1];
                        text_x = temp_x;
                        lineNo = 1;
                        
                        printText = item.tvBroadcaster;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tvWidth[3]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[3]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[3]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        text_y = y + padding;
                        temp_x = tvWidth[0]+ padding + header_separator.getWidth() + padding + tvWidth[1] + padding;
                        text_x = temp_x;
                        lineNo = 1;
                        
                        printText = item.tvRegion;
                        vText = Utility.breakIntoWords(printText);
                        if (textFont.getAdvance(printText) <= tvWidth[2]) {
                        	text_y = y + ((getRowHeight() - textFont.getHeight())/2);
                        	g.drawText(printText + " ", text_x, text_y);
                        } else {
	                        for (int word = 0; word < vText.size(); word ++) {
	                        	if (lineNo > 2) {
	                        		break;
	                        	}
	                        	
	                        	String tempString = (String)vText.elementAt(word);
	                        	int wordWidth = GuiConst.FONT_TABLE.getAdvance(tempString+ " ");
	                        	if ((text_x + wordWidth >= tvWidth[4]) || ((lineNo == 2) && (text_x+wordWidth >= ((tvWidth[4]*75)/100)))) {
	                        		if (lineNo == 2) {
	                        			tempString = "...";
	                        		} else {
	                        			text_y +=  GuiConst.FONT_TABLE.getHeight() + padding;
	                        			text_x = temp_x;
	                        		}
	                        		lineNo ++;
	                        	}
	
	                        	g.drawText(tempString + " ", text_x, text_y);
	                        	text_x += wordWidth;
	                        }
                        }
                        
                        row ++;
                        if (row > index) {
                        	row = 0;
                        	temp_x = padding;
                        	text_x = padding;
                        	prev_x = 0;
                        	bg_y = y;
                        }
                    }
                }            	
            	
                break;
                
            /*Live Score*/
            case 3: 
                
                break;
                
            /*Order of Merit*/
            case 4:
            	
            	break;
        }
    //192.168.1.48 HPJET 3030
    }
    
    public void setupBackground(Graphics g, int index, int bg_y) {
        //this.bg_y = bg_y;
        
//    	if (index == 0) {
//            /* header */
//            g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg.getHeight(), header_bg, 0,0);
//            System.out.println("1background = row"  + row + " height:" + header_bg.getHeight() + " bg_y: " + bg_y); 
//        } else if ((index != 0) && ((index % 2) == 0)) {
//            /* even row */
//            g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg.getHeight(), even_bg, 0,0);
//            System.out.println("2background = row"  + row + " height:" + header_bg.getHeight() + " bg_y: " + bg_y); 
//        } else if ((index != 0) && ((index % 2) == 1)) {
//            /* odd row */
//            g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg.getHeight(), odd_bg, 0,0);
//            System.out.println("3background = row"  + row + " height:" + header_bg.getHeight() + " bg_y: " + bg_y); 
//        }
        
        if (index == 0) {
        	g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg.getHeight(), header_bg, 0,0);
        	bg_y += header_bg.getHeight();
        	g.drawBitmap(0, bg_y, this.getPreferredWidth(), border.getHeight(), border, 0,0);
        	System.out.println("header background = row"  + row + " height:" + header_bg.getHeight() + " bg_y: " + bg_y + " and index: " + index);
        } else {
        	if (index == this.getSelectedIndex() && listener.isListFieldFocus()) {
        		g.drawBitmap(0, bg_y, this.getPreferredWidth(), odd_bg.getHeight(), odd_bg, 0,0);
        		System.out.println("this is a selected row, so use ODD bg, header background = row"  + row + " height:" + odd_bg.getHeight() + " bg_y: " + bg_y + " and index: " + index);
        		bg_y += odd_bg.getHeight();
            	g.drawBitmap(0, bg_y, this.getPreferredWidth(), border.getHeight(), border, 0,0);
        	} else {
        		
        		g.drawBitmap(0, bg_y, this.getPreferredWidth(), even_bg.getHeight(), even_bg, 0,0);
        		System.out.println("this is NOT a selected row, so use EVEN bg, header background = row"  + row + " height:" + even_bg.getHeight() + " bg_y: " + bg_y + " and index: " + index);
        		bg_y += even_bg.getHeight();
            	g.drawBitmap(0, bg_y, this.getPreferredWidth(), border.getHeight(), border, 0,0);
        	}
        }
        
        //bg_y += header_bg.getHeight()+1;
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
