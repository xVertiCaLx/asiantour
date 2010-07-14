package com.og.rss;

public class RssStatus
{
    public int feedid=0;
    public int errorcode=0;
    public String errordesc = "";
 
    public RssStatus(int feedid, int errorcode, String errordesc){
        this.feedid=feedid;        
        this.errorcode=errorcode;
        this.errordesc=errordesc;
    }
} 
