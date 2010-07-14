package com.og.app.object;

import com.og.app.Const;
import com.og.rss.ANewsFeed;

public class Settings implements net.rim.device.api.util.Persistable {
    public ANewsFeed[] rssfeeds= null;
    public int xdayscache = 0;
    public boolean imgautoload = false;
    public int fontsize = 0;
    public String fontname = "";
    public int newsimgpercentage= 0;
    public String lastbuilddate = "";
    public int xdaysbookmark= 0;
            
    public Settings () {    
    }                       
} 
