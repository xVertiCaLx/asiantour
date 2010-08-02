package com.og.app.gui;

import java.util.Vector;

import com.og.app.gui.listener.ListFieldListener;
import com.og.app.util.DataCentre;

//
public class TableListField extends CustomTableListField {
	
    private TablePanel tablePanel = null;
    DataCentre[] item = new DataCentre[arraySize];

    
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
    	tablePanel.invalidate();
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
            3 - Live Score
            4 - Order of Merit */
            if (tableNo == 1) {
            	
                Vector tvTimes = MenuScreen.getInstance().tvTimesCollection;
                for(int i=0; i<tvTimes.size(); i++){
                	add((DataCentre)tvTimes.elementAt(i));
                }               
                
            } else if (tableNo == 2) {
            	
            	Vector tourSchedule = MenuScreen.getInstance().tourScheduleCollection;
                for(int i=0; i<tourSchedule.size(); i++){
                	add((DataCentre)tourSchedule.elementAt(i));
                } 
                
            } else if (tableNo == 4) {
                for (int i =0; i <5; i++) {
                    if (i == 0) {
                        item[i] = new DataCentre("NOH Seung-yul (KOR)", "1", "$503,916.20");
                    } else if (i == 1) {
                        item[i] = new DataCentre("Marcus FRASER (AUS)", "2", "$496,970.25");
                    } else if ((i == 2)) {
                        item[i] = new DataCentre("Andrew DODT (AUS)", "3", "$382,199.50");
                    } else if ((i == 3)) {
                        item[i] = new DataCentre("K.J. CHOI (KOR)", "4", "$235,838.00");
                    }else if ((i == 4)) {
                        item[i] = new DataCentre("Tetsuji HIRATSUKA (JPN)", "5", "$212,463.86");
                    }
                	
                    add(item[i]);
                }
            }
        }
    
    }
    
} 
