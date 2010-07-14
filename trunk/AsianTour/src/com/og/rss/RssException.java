package com.og.rss;

public class RssException extends Exception
{
        public static int ERRORCODE_UNKNOWN = 0;
        public static int ERRORCODE_NEWSITEMEXISTS = 1;
        public static int ERRORCODE_NONEWSUPDATE = 2;
        public static int ERRORCODE_DBERROR= 3;
        
        private String[] ERRORCODEARR = new String[]{
                "Unknown Error",
                "News Item exists in database.",
                "No latest news.",
                 "Database error",
        };
        
        private int errorcode=0;
        
        public RssException(int errorcode){
                this.errorcode=errorcode;
        }
        
        public int getErrorCode(){
                return errorcode;
        }
        
        public String toString(){
                return ERRORCODEARR[errorcode];
        }
} 
