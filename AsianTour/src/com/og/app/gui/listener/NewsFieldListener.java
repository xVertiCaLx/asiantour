package com.og.app.gui.listener;

public interface NewsFieldListener {
        //public abstract void drawBottomPanel();
        
        public abstract void setSelectedNewsTab(int selectedIndex);
        public abstract int getSelectedNewsTab();
        
        public abstract void setNewsTabOnFocus(boolean isfocus);
        public abstract boolean isNewsTabOnFocus();
        
        public abstract void reinitNewsTab();
        //public abstract boolean isSelectedNewsTabHasNews();
        public abstract void repaintNewsTab();
        
} 
