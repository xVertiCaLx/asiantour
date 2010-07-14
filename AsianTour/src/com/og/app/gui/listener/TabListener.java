package com.og.app.gui.listener;

public interface TabListener
{
        public abstract void drawBottomPanel();
        
        public abstract void setSelectedTab(int selectedIndex);
        public abstract int getSelectedTab();
        
        public abstract void setTabOnFocus(boolean isfocus);
        public abstract boolean isTabOnFocus();
        
        public abstract void reinitTab();
        public abstract boolean isSelectedTabHasNews();
        public abstract void repaintTab();
        
} 
