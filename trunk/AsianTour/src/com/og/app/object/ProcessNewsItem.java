package com.og.app.object;

import com.og.app.util.*;

public class ProcessNewsItem {
   private String displaydate = "";
   
   public ProcessNewsItem(com.og.xml.ANewsItemObj ni){
       String tempdate = ni.pubDate;
       if ( tempdate==null || tempdate.equals("") || tempdate.length()<25 ){
            displaydate="-";
            return;
        }
        int onehour = 1000*60*60;
        int oneday= onehour*24;
        long publishdateinmillis = Utility.getTimeInMillis(tempdate);

        long currenttime = Utility.getCurrentTimeInMillis();

        
        if ( publishdateinmillis>=currenttime ){
            displaydate="1 minute ago";
            return;
        }
        
        long timediff = currenttime-publishdateinmillis;
        if ( timediff<onehour ){
            timediff=timediff/1000/60;
            if ( timediff<=1 )
                displaydate="1 minute ago";
            else
                displaydate=timediff+" minutes ago";
            return;            
        }
        
        if ( timediff<oneday ){
            timediff=timediff/1000/60/60;
            if ( timediff<=1 )
                displaydate="1 hour ago";
            else
                displaydate=timediff+" hours ago";
            return;            
        }
        timediff=timediff/1000/60/60/24;
        if ( timediff<=1 )
            displaydate="1 day ago";
        else
            displaydate=timediff+" days ago";
        return;                    
    }
   
/*
 public ProcessNewsItem(com.og.rssreader.NewsItemObj ni){
       String tempdate = ni.getPubDate();
       if ( tempdate==null || tempdate.equals("") || tempdate.length()<25 ){
            displaydate="-";
            return;
        }
        
       displaydate = tempdate.substring(5, 16);
        Calendar rightnow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        Date tmpdate = rightnow.getTime();
        String tmpstr = tmpdate.toString();
        String date = ""+rightnow.get(Calendar.DATE);
        if ( date.length()==1 )
            date="0"+date;
        String today = date+" "+tmpstr.substring(4, 7)+" "+rightnow.get(Calendar.YEAR);
        //System.out.println("today:"+today);
        if ( today.equals(displaydate) ){
            displaydate= "Today";      
            long currenttime =(rightnow.getTime()).getTime();
            rightnow.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tempdate.substring(17,19)));
            rightnow.set(Calendar.MINUTE, Integer.parseInt(tempdate.substring(20,22)));
            rightnow.set(Calendar.SECOND, Integer.parseInt(tempdate.substring(23,25)));
            long timeInMillis=(rightnow.getTime()).getTime() ;     
            if ( currenttime < timeInMillis+(1000*60*60) ){//less than 1 hr
                    long min = (currenttime-timeInMillis)/60000;
                    if ( min<=1 )
                        displaydate=min+" Minute ago";
                    else
                        displaydate=min+" Minutes ago";
            } else if ( currenttime < timeInMillis+(1000*60*120) ){//less than 1 hr{
                displaydate="1 Hour ago";
            }  
        }else{
            rightnow.set(Calendar.DATE, rightnow.get(Calendar.DATE)-1);    
            tmpdate = rightnow.getTime();
            tmpstr = tmpdate.toString();
            date = ""+rightnow.get(Calendar.DATE);
            if ( date.length()==1 )
                date="0"+date;                
            String yesterday = date+" "+tmpstr.substring(4, 7)+" "+rightnow.get(Calendar.YEAR);
            if ( displaydate.equals(yesterday) )
                displaydate="Yesterday";     
        }
   }
*/   

   public String getDisplayDate(){
        return displaydate;
    }

} 
