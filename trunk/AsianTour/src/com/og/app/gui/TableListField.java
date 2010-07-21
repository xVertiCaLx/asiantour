package com.og.app.gui;

//import java.io.IOException;

import com.og.app.gui.listener.*;
//import com.og.app.util.Utility;
import com.og.app.util.DataCentre;
//import com.og.app.util.WebDataCallback;
//import com.og.rss.*;

//import net.rim.device.api.system.*;
//import net.rim.device.api.ui.*;

public class TableListField extends CustomTableListField {
    private TablePanel tablePanel = null;
    DataCentre[] item = new DataCentre[500];
    //private int tableNo = 0;

    
    public TableListField(TablePanel tablePanel, ListFieldListener listener, int tableNo, int page) {
        super(listener, tableNo, page);
        this.tablePanel = tablePanel;
    }
    
    protected synchronized boolean navigationMovement(int dx, int dy, int status, int time) {
    	System.out.println("dx: " + dx + " dy: " + dy );
    	
    	//2,2,3,1
    	
    	if ((table == 1)||(table ==2)) {
    		if (dx > 0) {
    			//move right
	    		System.out.println("right");
	    		
	    		if (page == 1) {
	    			page = 2;
	    		}
	    		System.out.println(page);
	    		
	    	} else if (dx < 0) {
	    		System.out.println("left");
	    		if (page == 2) {
	    			page = 1;
	    		}
	    		System.out.println(page);
	    		//move left
	    	}
    	} else if (table == 3) {
    		if (dx > 0) {
    			//move right
	    		System.out.println("right");
	    		
	    		if (page == 1) {
	    			page = 2;
	    		} else if (page == 2) {
	    			page = 3;
	    		}
	    		System.out.println(page);
	    		
	    	} else if (dx < 0) {
	    		System.out.println("left");
		    	if (page == 3) {
		    		page = 2;
		    	}else if (page == 2) {
	    			page = 1;
	    		}
	    		System.out.println(page);
	    		//move left
	    	}
    	}
    	
		System.out.println("BIIIIIGGGGGGGGERRRR ALERT: page : " + page);
    	
    	tablePanel.invalidate();
        System.out.println("this");
        return false;
    }
    
    public boolean navigationClick(int status, int time) {
        System.out.println("print");
        return false;
    }
    
    public void loadNews(int tableNo) {
        //setRowHeight();
        synchronized (lock) {
            /* Table No:    
            1 - TV Schedule
            2 - Tour Schedule
            3 - Live Score*/
            if (tableNo == 1) {
                
                for (int i =0; i <50; i++) {
                    /*if (i == 1) {
                        item[i] = new DataCentre("Asian Tour Highlights", "TBC", "TBC", "Guangdong Golf Channel", "China");
                    } else if (i == 2) {
                        item[i] = new DataCentre("Asian Tour Golf Show", "24 June 2010", "2030 - 2100", "PCCW, Golf Tour Channel #683", "Hong Kong");
                    } else if (i == 3) {
                        item[i] = new DataCentre("Asian Tour Golf Show", "24 June 2010", "2030 - 2100", "SPORTCAST", "Taiwan");
                    } else if (i == 4) {
                        item[i] = new DataCentre("Asian Tour Golf Show", "24 June 2010", "2030 - 2100", "TrueVision", "Thailand");
                    } else {
                        item[i] = new DataCentre("Asian Tour Golf Show", "24 June 2010", "2030 - 2100", "Sky Sports 3 Digital", "UK");
                    }*/
                	
                	item[i] = new DataCentre(i+"i want WWW something that is super logn so that i can see the effects of the truncation "+ i, "date 1 of " + i, "time 1 of " + i, "broadcaster 1 of " + i, "region 1 of " + i );
                	
                    add(item[i]);
                }
                
            } else if (tableNo == 2) {
                for (int i =0; i <5; i++) {
                    /*if (i == 1) {
                        item[i] = new DataCentre("January 6-9", "Thailand", "Qualifying School First Stage ", "Creek GR, Kaeng Krachan CCR & Rayong CC", "none", "$0");
                    } else if (i == 2) {
                        item[i] = new DataCentre("January 13-16", "Thailand", "Qualifying School Final Stage", "Creek GR, Kaeng Krachan CCR & Rayong CC", "Artemio MURAKAMI", "$0");
                    } else if ((i >= 3)||(i ==0)) {
                        item[i] = new DataCentre("February 4-7", "Thailand", "Asian Tour International", "Suwan Golf and Country Club", "Gaganjeet BHULLAR", "$300,000");
                    }*/
                	
                	item[i] = new DataCentre("ab" + i, "bb", "cb"+1, "db", "eb"+i, "fb");
                	
                    add(item[i]);
                }
            }
        }
    
    }
    
} 
