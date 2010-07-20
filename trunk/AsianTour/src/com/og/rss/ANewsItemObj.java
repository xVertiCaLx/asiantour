package com.og.rss;

//import net.rim.device.api.system.Bitmap;

public class ANewsItemObj implements net.rim.device.api.util.Persistable {
    public int index = 0;
    public String title = "";
    public String link = "";
    public String author= "";
    public String pubDate = "";
    public String longdate = "";
    public String shortdate = "";
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
    
//    public ANewsItemObj(String title,String link,String author,String pubDate,String description,String guid, String imageurl) {    
//        this.title=title;
//        this.link=link;
//        this.author=author;
//        this.pubDate=pubDate;
//        this.description=description;
//        this.guid=guid;
//        this.imageurl=imageurl;
//    }         
    
    public ANewsItemObj(String title,String author,String pubDate,String description,String guid) {    
        this.title=title;
        this.author=author;
        this.pubDate=pubDate;
        this.description=description;
        this.guid=guid;
    }  
    
    public ANewsItemObj(String guid, String title, String description, String thumbnailUrl, String imageUrl, String author, String longdate, String shortdate){
    	this.guid = guid;
    	this.title = title;
    	this.description = description;
    	this.thumbnailurl = thumbnailUrl;
    	this.imageurl = imageUrl;
    	this.longdate = longdate;
    	this.shortdate = shortdate;
    	this.author = author;
    }
} 
