package com.og.rss;

public interface RssListener
{
        public final static short STATUS_PENDING=0;
        public final static short STATUS_COMPLETED=1;
        public final static short STATUS_ERROR=2;
        
        public abstract int getXDaysCache(int feedid);
        public abstract void updateRssFeedLastBuildDate(int feedid, String lastbuilddate);
        public abstract void updateStatus(RssStatus status);
        public abstract void addDeleteItem(String guid, String pubDate);
        public abstract int getMaxItemNo(int feedid);
        

} 
