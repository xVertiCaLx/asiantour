package com.og.app.util;

public class FeedException extends Exception {
    public final static int ERRORCODE_UNKNOWNEXCEPTION = 0;
    public final static int ERRORCODE_SAMELASTBUILDDATE = 1;
    public final static int ERRORCODE_INVALIDRESPONSECODE = 2;    
    public final static int ERRORCODE_IOEXCEPTION   = 3;
    public final static int ERRORCODE_SAXEXCEPTION   = 4;
    public final static int ERRORCODE_EXCEPTION   = 5;
   
    private int errorcode = 0;
    
    public FeedException ( int errorcode) {    
        this.errorcode=errorcode;
    }                       
    public int getErrorCode(){
        return errorcode;
    }
} 

