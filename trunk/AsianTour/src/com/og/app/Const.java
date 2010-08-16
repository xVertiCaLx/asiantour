package com.og.app;

import com.og.app.gui.GuiConst;
//hello
public class Const {
	public static int ERRORCODE_1STTIMEUSERERROR = 1;
	public static int ERRORCODE_LOADIMAGE = 2;
	public static int ERRORCODE_NOCATEGORY = 3;
	public static int ERRORCODE_EXECEPTION = 4;

	public static long TIMEDIFF = 0;

	public static int DEFAULT_FONTSIZE = GuiConst.FONTSIZE_ARR[1];
	static {
		if (GuiConst.SCREENWIDTH > 320)
			DEFAULT_FONTSIZE = GuiConst.FONTSIZE_ARR2[1];
	}
	public final static String VERSION = "CLOSED BETA";
	public final static String DEVICENAME = net.rim.device.api.system.DeviceInfo
			.getDeviceName();

	public final static int DEFAULT_DELETEITEMEXPIREDDAYS = 10;

	public final static int DEFAULT_XDAYSBOOKMARK = 7;
	public final static int DEFAULT_XDAYSCACHE = GuiConst.CACHE_ARR[GuiConst.CACHE_ARR.length - 1];
	public final static String DEFAULT_FONTNAME = GuiConst.FONTNAME_ARR[0];// "BBAlpha Sans";
	public final static int DEFAULT_NEWSIMGPERCENTAGE = GuiConst.PREVIEWIMAGE_PERCENTAGE[1]; // 50%
	public final static String DEFAULT_TWITTER_CONNECT_MSG = "We are now checking authorization for Twitter.";

	public final static int ID_SETTING = -1;
	public final static int ID_SAVED = -2;
	public final static int ID_CATEGORY = -3;
	public final static int ID_WEATHER = -4;
	public final static int ID_ADS = -5;
	public final static int ID_DELETE = -6;
	public final static int ID_PHOTOGALLERY = -7;

	public final static int ID_UNKNOWN = -99;

	public final static String PAGETRACKER_NAME = "Photo";
	public final static String NEWS_SHARE_BASE_URL = "http://www.asiantour.com/news.aspx?sid=";
	public final static String LISTEN_URL = "http://:1337";
	public static final int CHUNK_SIZE = 256;
}
