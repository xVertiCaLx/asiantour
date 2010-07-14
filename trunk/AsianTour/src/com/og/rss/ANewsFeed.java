package com.og.rss;

public class ANewsFeed implements net.rim.device.api.util.Persistable {
        public final static short FEEDTYPE_NEWS = 0;
        public final static short FEEDTYPE_PHOTO = 1;
    
        public String url;
        public String lastBuildDate;
        public int id;
        public String name;
        public String iconImageUrl = null;        
        public byte[] iconImage = null;
        public short feedType = 0;
        public String imagePath = null;
        
        public ANewsFeed (int id, String name, String url, String imagePath, String lastBuildDate, short feedType) {
                this.id=id;
                this.url=url;
                this.name=name;
                this.lastBuildDate=lastBuildDate;
                this.iconImage=iconImage;
                this.feedType=feedType;
                this.imagePath=imagePath;
        }
         
        public ANewsFeed (int id,String name, String url, byte[] iconImage, String lastBuildDate) {
                this.id=id;
                this.url=url;
                this.name=name;
                this.lastBuildDate=lastBuildDate;
                this.iconImage=iconImage;
        }
} 
