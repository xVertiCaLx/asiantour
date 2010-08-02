package com.og.app.util;

import net.rim.device.api.xml.parsers.*;
import net.rim.device.api.system.WLANInfo;
import net.rim.device.api.util.DataBuffer;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.synchronization.ConverterUtilities;

import java.io.*;


import javax.microedition.io.*;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class ConnectionMgr{
	static byte TELCO = 0;
	final static byte STARHUB = 1;
	final static byte SINGTEL = 2;
	final static byte M1 = 3;
	final static int CONFIG_TYPE_WAP  = 0;
	final static int CONFIG_TYPE_BES  = 1;
	public static int CONNECTIONTYPE=0;
	public static String CONNECTIONSTR = "";

	static  ServiceRecord srBISB, srBES, srWAP, srWAP2, srWIFI;
	static{
		if(GPRSInfo.getHomeMCC() == 1317)
		{
			switch(GPRSInfo.getHomeMNC())
			{
			case 1:
			case 2:
				TELCO = SINGTEL;
				break;
			case 3:
				TELCO = M1;
				break;
			case 5:
				TELCO = STARHUB;
				break;
			}
		}          

		//ServiceBook sb = ServiceBook.getSB();
		//ServiceRecord[] records = sb.findRecordsByCid("IPPP");
		ServiceBook sb = ServiceBook.getSB();
		ServiceRecord[] records = sb.getRecords();

		if(records != null)
		{
			for(int i = records.length-1; i >= 0; i--)
			{
				ServiceRecord sr = records[i];

				if(sr.isValid() && !sr.isDisabled())
				{
					String cid = sr.getCid().toLowerCase();
					String uid = sr.getUid().toLowerCase();   

					//bis-b  & bes               
					if (cid.indexOf("ippp") != -1 && uid.indexOf("gpmds") != -1) {
						srBISB=sr;
						srBES=sr;
					}      
					//wifi                                  
					if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") != -1) {
						srWIFI=sr;    
					}
					// Wap2.0
					if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1 && uid.indexOf("mms") == -1) {
						srWAP2=sr;
					}                        
					// Wap1.0
					if ( cid.equalsIgnoreCase("wap")) {
						//if (getConfigType(sr)==CONFIG_TYPE_WAP && cid.equalsIgnoreCase("wap")) {
						srWAP=sr;
					}                 
				}
			}
		}       
		setConnectionStr();        
	}

	private static void setConnectionStr(){

		if (net.rim.device.api.system.DeviceInfo.isSimulator()){
			CONNECTIONTYPE=0;
			CONNECTIONSTR=";deviceside=false";
			return;
		}

		if ( isWIFICoverage()==true ){         
			//if ( isWIFICoverage()==true && srWIFI!=null ){ 
			CONNECTIONTYPE=2;
			CONNECTIONSTR=";interface=wifi";        
			return;
		}              
		if ( isBISBCoverage()==true && srBISB!=null ){
			CONNECTIONTYPE=1;
			CONNECTIONSTR=";deviceside=false;ConnectionType=mds-public";            
			return;
		}           
		if ( isDIRECTCoverage()==true  && srWAP2!=null){
			CONNECTIONTYPE=4;
			CONNECTIONSTR=";deviceside=true" + ";ConnectionUID=" + srWAP2.getUid();
			return;
		}                   
		if ( isDIRECTCoverage()==true && srWAP!=null ){
			switch(TELCO)
			{
			case STARHUB:
				CONNECTIONTYPE=5;
				CONNECTIONSTR=";deviceside=true;apn=shwap";
				return;
			case SINGTEL:
				CONNECTIONTYPE=6;
				CONNECTIONSTR=";deviceside=true;apn=e-ideas";
				return;
			case M1:
				CONNECTIONTYPE=7;
				CONNECTIONSTR=";deviceside=true;apn=sunsurf";
				return;
			}
		}                  
		if ( isBESCoverage()==true && srBES!=null ){
			CONNECTIONTYPE=3;
			CONNECTIONSTR=";deviceside=false";   
			return;
		}               
		CONNECTIONTYPE=8; 
		CONNECTIONSTR=";deviceside=true"; 
	}

	public static byte[] loadImage(String imgurl){

		StreamConnection c = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		byte[] byarr = null;
		try{ 
			System.out.println("loading image:"+ConnectionMgr.getFinalHttpURL(imgurl));
			c = (StreamConnection)Connector.open(ConnectionMgr.getFinalHttpURL(imgurl));
			System.out.println(System.currentTimeMillis()+">>Successfully established.");

			is = c.openInputStream();
			int ch = 0;
			baos = new ByteArrayOutputStream();
			while ((ch = is.read()) != -1){
				baos.write(ch);
			}

			if ( baos!=null ){
				byarr = baos.toByteArray();
			}                  
			System.out.println(System.currentTimeMillis()+">>Image loaded.");                      
		}catch (Exception e){
			System.out.println(System.currentTimeMillis()+">>loadImage."+e);             
		}finally {
			if(baos != null){
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
			if (is != null){ 
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (c != null){
				try {
					c.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
		Utility.connCount--;
		return byarr;
	}

	public static String wgetData(String url) 
	{       
		HttpConnection httpConnection = null;
		String data = null;
		InputStream is = null;
		try
		{
			// connect to feed's URL
			String feedurl = ConnectionMgr.getFinalHttpURL(url);
			System.out.println(System.currentTimeMillis()+">>open connection:"+feedurl);                 
			httpConnection = (HttpConnection)Connector.open(feedurl);
			httpConnection.setRequestMethod(HttpConnection.GET);
			is = httpConnection.openInputStream();
			if(httpConnection.getResponseCode() == HttpConnection.HTTP_OK){
				data = read(is);
			}else{
				System.out.println("Response Code: " + httpConnection.getResponseCode());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			if(httpConnection!=null){
				try {
					httpConnection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			data = null;
			is=null;
			httpConnection=null;
		}

		return data;
	}    


	public static boolean wget(String url) 
	{                  
		HttpConnection httpConnection = null;
		boolean status=false;
		try
		{
			// connect to feed's URL
			String feedurl = ConnectionMgr.getFinalHttpURL(url);
			//System.out.println(System.currentTimeMillis()+">>open connection:"+feedurl);                 
			httpConnection = (HttpConnection)Connector.open(feedurl);
			httpConnection.setRequestMethod(HttpConnection.GET);

			if(httpConnection.getResponseCode() == HttpConnection.HTTP_OK)
				status = true;
		}
		catch (Exception e)
		{
		}
		httpConnection=null;
		return status;
	}    

	public static DefaultHandler requestFeed(String url, DefaultHandler myHandler) throws FeedException
	{                      
		InputStream inputStream = null;
		HttpConnection httpConnection = null;
		try
		{
			// connect to feed's URL
			System.out.println("requestFeed:"+ConnectionMgr.getFinalHttpURL(url));                
			String feedurl = ConnectionMgr.getFinalHttpURL(url);
			httpConnection = (HttpConnection)Connector.open(feedurl);     
			inputStream = httpConnection.openInputStream();
			if(httpConnection.getResponseCode() == HttpConnection.HTTP_OK)
			{
				String desiredEncoding = "UTF-8";  //iso-8859-1
				String contenttype = httpConnection.getHeaderField("Content-Type");
				if (contenttype != null)
				{
					contenttype = contenttype.toUpperCase();
					if (contenttype.indexOf("ISO-8859-1") != -1)
					{
						desiredEncoding = "ISO-8859-1";
					}
				}
				// we need an input source for the sax parser
				InputSource is = new InputSource(inputStream);
				is.setEncoding(desiredEncoding);
				// create the factory
				SAXParserFactory factory = SAXParserFactory.newInstance();
				// create a parser
				SAXParser parser = factory.newSAXParser();
				// perform the synchronous parse       

				parser.parse(is,myHandler);
				return myHandler;
			}else{
				//System.out.println("Error: ResponseCode: " + httpConnection.getResponseCode());
				throw new FeedException(FeedException.ERRORCODE_INVALIDRESPONSECODE);
			}
		}
		catch (IOException ioe)
		{
			System.out.println("IO Exception : " + ioe);
			throw new FeedException(FeedException.ERRORCODE_IOEXCEPTION);
		}
		catch (SAXException saxe)
		{
			System.out.println("SAX Exception : " + saxe);
			if ( saxe.getMessage()!=null && !saxe.getMessage().equals("") ){
				throw new FeedException(FeedException.ERRORCODE_SAXEXCEPTION);
			}else{
				throw new FeedException(FeedException.ERRORCODE_SAMELASTBUILDDATE);
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception : " + e);            
			throw new FeedException(FeedException.ERRORCODE_EXCEPTION);
		}
	}    

	private static String read(InputStream is) throws Exception{
		String result = "";
		byte[] buffer = new byte[100];
		long cursor = 0;
		int offset = 0;

		while ((offset = is.read(buffer)) != -1) {
			result += new String(buffer, 0, offset);
			cursor += offset;
		}
		is.close();

		return result;
	}


	public static String getFinalHttpURL(String url){
		setConnectionStr();            
		return url+CONNECTIONSTR;
	}

	//----     
	private static boolean isBESCoverage(){    
		if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS))
			return true;
		return false;
	}
	private static boolean isDIRECTCoverage(){    
		if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT))
			return true;
		return false;
	}

	private static boolean isBISBCoverage(){
		if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B))
			return true;
		return false;
	}
	private static boolean isWIFICoverage(){
		if(WLANInfo.getWLANState()==WLANInfo.WLAN_STATE_CONNECTED)
			return true;
		return false;
	}
	//----     
	private static int getConfigType(ServiceRecord record) {
		return getDataInt(record, 12);
	}    
	private static int getDataInt(ServiceRecord record, int type)
	{
		DataBuffer buffer = null;
		buffer = getDataBuffer(record, type);

		if (buffer != null){
			try {
				return ConverterUtilities.readInt(buffer);
			} catch (EOFException e) {
				return -1;
			}
		}
		return -1;
	}  
	private static DataBuffer getDataBuffer(ServiceRecord record, int type) {
		byte[] data = record.getApplicationData();
		if (data != null) {
			DataBuffer buffer = new DataBuffer(data, 0, data.length, true);
			try {
				buffer.readByte();
			} catch (EOFException e1) {
				return null;
			}
			if (ConverterUtilities.findType(buffer, type)) {
				return buffer;
			}
		}
		return null;
	}         
}
