package com.og.rss;

public class ANewsItemObj implements net.rim.device.api.util.Persistable {
    public int index = 0;
    public String title = "";
    public String link = "";
    public String author= "";
    public String pubDate = "";    
    public String description = "";    
    public String guid= "";
    public String imageurl= "";
    public String thumbnailurl= "";
    public boolean hasread= false;
    public byte[] thumbnail= null;
    public byte[] image= null;
    public int feedid = 0;
    public String status = null;
    public long longpubDate = 0;
    public int imageheight = 0;
    public int imagewidth = 0;
    public String caption= "";    
    
    public ANewsItemObj() {    
    }
    
    public ANewsItemObj(String title,String link,String author,String pubDate,String description,String guid, String imageurl) {    
        this.title=title;
        this.link=link;
        this.author=author;
        this.pubDate=pubDate;
        this.description=description;
        this.guid=guid;
        this.imageurl=imageurl;
    }         
    
    public ANewsItemObj(String title,String author,String pubDate,String description,String guid) {    
        this.title=title;
        this.author=author;
        this.pubDate=pubDate;
        this.description=description;
        this.guid=guid;
    }    
} 
