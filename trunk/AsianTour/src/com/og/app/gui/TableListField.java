package com.og.app.gui;

import com.og.app.gui.listener.*;
//import com.og.app.util.Utility;
import com.og.app.util.DataCentre;


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
                
                for (int i =0; i <30; i++) {
                    
                    add(item[i]);
                }
                
            } else if (tableNo == 2) {
                for (int i =0; i <5; i++) {
                    if (i == 0) {
                        item[i] = new DataCentre("January 6-9", "Thailand", "Qualifying School First Stage ", "Creek GR, Kaeng Krachan CCR & Rayong CC", "none", "$0");
                    } else if (i == 1) {
                        item[i] = new DataCentre("January 13-16", "Thailand", "Qualifying School Final Stage", "Creek GR, Kaeng Krachan CCR & Rayong CC", "Artemio MURAKAMI", "$0");
                    } else if (i == 2) {
                        item[i] = new DataCentre("January 13-16", "Thailand", "Qualifying School Final Stage", "Creek GR, Kaeng Krachan CCR & Rayong CC", "Artemio MURAKAMI", "$0");
                    } else if (i == 3) {
                        item[i] = new DataCentre("February 4-7", "Thailand", "Asian Tour International", "Suwan Golf and Country Club", "Gaganjeet BHULLAR", "$300,000");
                    } else if (i == 4) {
                        item[i] = new DataCentre("January 13-16", "Thailand", "Qualifying School Final Stage", "Creek GR, Kaeng Krachan CCR & Rayong CC", "Artemio MURAKAMI", "$0");
                    }
                	
                    add(item[i]);
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
