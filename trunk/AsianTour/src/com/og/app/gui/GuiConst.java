package com.og.app.gui;
//import com.og.app.util.*;
//import com.og.app.object.SettingObj;

import com.og.app.*;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
 
public class GuiConst{ 
        
    public final static int SCREENWIDTH = net.rim.device.api.system.Display.getWidth();
    public final static int SCREENHEIGHT= net.rim.device.api.system.Display.getHeight();

    public final static int MAX_PHOTO_PERCAT= 40;
    public final static int MAX_ARTICLE_PERCAT= 500;
      
    public final static int VERSION_TYPE_B= 0;//bold
    public final static int VERSION_TYPE_G= 1;//gemini/curve
    public final static int VERSION_TYPE_S= 2; //storm
 
    //update this variable for different version
    public final static int VERSION_TYPE = VERSION_TYPE_G;
         
    private final static String[] VERSION_TYPE_ARR = new String[]{
        "typeB", "typeG", "typeS"
    }; 

    private final static boolean[] SHOWTHUMBNAIL_ARR = new boolean[]{
        true, false,true
    };
    private final static boolean[] SHOWTABICON_ARR = new boolean[]{
        false,false,true
    };        
    private final static int[] YESNOSPACE_ARR = new int[]{
        15,15,30
    };        
    private final static int[] LISTTOPDOWNSPACE_ARR = new int[]{
        4, 4, 10
    };        
    public final static int DEFAULT_LISTTOPDOWNSPACE = LISTTOPDOWNSPACE_ARR[VERSION_TYPE];              
    public final static int DEFAULT_YESNOSPACE= YESNOSPACE_ARR[VERSION_TYPE];            
    public final static boolean DEFAULT_SHOWTHUMBNAIL = SHOWTHUMBNAIL_ARR[VERSION_TYPE];
    public final static boolean DEFAULT_SHOWTABICON = SHOWTABICON_ARR[VERSION_TYPE];
    public final static String DEFAULT_VERSION_TYPE = VERSION_TYPE_ARR[VERSION_TYPE];    

    public final static String[] PREVIEWIMAGETEXT_ARR= new String[]{ 
        "Full Screen ",                       
        "Normal",          
     };
     
     public final static int[] PREVIEWIMAGE_PERCENTAGE= new int[]{ 
        100,              
        50,            
     };

    public final static String[] FONTNAME_ARR = new String[]{ 
        "BBAlpha Sans", 
        "BBAlpha Serif", 
        "BBCapitals", 
        "BBCasual", 
        "BBClarity",  
        "BBCondensed", 
        "BBJapanese", 
        "BBKorean", 
        "BBMillbank", 
        "BBMillbankTall", 
        "BBSansSerif",
        "BBSansSerifSquare",
        "BBSerif",
        "BBSerifFixed",
        "BBSimpChinese",
        "BBTradChinese",
    };
        
    public final static String[] CACHETEXT_ARR = new String[]{ "1","2","3", "4","5","6","7"};
    public final static int[] CACHE_ARR = new int[]{ 1,2,3,4,5,6,7};
        
    public final static String[] FONTSIZETEXT_ARR = new String[]{ "Small","Medium", "Large"};
        
    public static int[] FONTSIZE_ARR = new int[]{10, 12, 14};
    public static int[] FONTSIZE_ARR2 = new int[]{14, 17, 20};

    public static Font FONT_PLAIN = null;
    public static Font FONT_LINK= null;
    public static Font FONT_BOLD = null;
    public static Font FONT_DATE = null;
    public static Font FONT_VERSION = null;
    public static Font FONT_BOLD_UNDERLINED = null;
    public static Font FONT_TABLE_HEADER = null;
    public static Font FONT_TABLE = null;
    
    /*public static Font FONT_PLAIN = Font.getDefault().getFontFamily().getFont(Font.PLAIN, 10);
    public static Font FONT_LINK= Font.getDefault().getFontFamily().getFont(Font.UNDERLINED, 10);
    public static Font FONT_BOLD = Font.getDefault().getFontFamily().getFont(Font.BOLD, 10);
    public static Font FONT_DATE = Font.getDefault().getFontFamily().getFont(Font.UNDERLINED, 5);
    public static Font FONT_VERSION = Font.getDefault().getFontFamily().getFont(Font.UNDERLINED, 5);
    public static Font FONT_BOLD_UNDERLINED = Font.getDefault().getFontFamily().getFont(Font.UNDERLINED|Font.BOLD, 10);
    public static Font TABTYPEFONT_PLAIN = Font.getDefault().getFontFamily().getFont(Font.BOLD, 12);
    public static Font TABTYPEFONT_UNDERLINED = Font.getDefault().getFontFamily().getFont(Font.UNDERLINED|Font.BOLD, 12);*/
    
    public static void reinitFont(){
        //SettingObj settingobj = Utility.loadSetting();
        int font_size = Const.DEFAULT_FONTSIZE;//12;//settingobj.fontsize;
        try {
            FontFamily ff = null;
            ff = FontFamily.forName(Const.DEFAULT_FONTNAME);
            
            if (SCREENWIDTH > 320 )
                FONT_VERSION  = ff.getFont(Font.PLAIN, FONTSIZE_ARR2[2]);
            else
                FONT_VERSION  = ff.getFont(Font.PLAIN, FONTSIZE_ARR[2]);        
                            
            do{
                if ( ff.isHeightSupported(font_size))
                    break;
                font_size++;
            }while(true);
            
            int datefont_size = font_size-1;
            /*do{
                if ( ff.isHeightSupported(datefont_size))
                    break;
                datefont_size++;
            }while(true);*/   

            FONT_PLAIN = ff.getFont(Font.PLAIN, font_size);
            FONT_LINK = ff.getFont(Font.UNDERLINED, font_size);
            FONT_BOLD = ff.getFont(Font.BOLD, font_size);
            FONT_BOLD_UNDERLINED = ff.getFont(Font.BOLD|Font.UNDERLINED, font_size);
            FONT_DATE = ff.getFont(Font.ITALIC, datefont_size);
            if (SCREENWIDTH > 320) {
            	FONT_TABLE = ff.getFont(Font.PLAIN, 14);
            	FONT_TABLE_HEADER = ff.getFont(Font.BOLD, 17);
            } else {
            	FONT_TABLE = FONT_PLAIN;
            	FONT_TABLE_HEADER = FONT_BOLD;
            }
            
            return;
        } catch (Exception e) {}

        //font_size = settingobj.fontsize;
        Font defaultFont =Font.getDefault();
        do{
            if ( defaultFont.getFontFamily().isHeightSupported(font_size))
                break;
            font_size++; 
        }while(true);
        
         int datefont_size = font_size-1;
        do{
            if ( defaultFont.getFontFamily().isHeightSupported(datefont_size))
                break;
            datefont_size++;
        }while(true);         

            if ( net.rim.device.api.system.Display.getWidth()>320 )
                FONT_VERSION  = defaultFont.getFontFamily().getFont(Font.PLAIN, FONTSIZE_ARR2[2]);
            else
                FONT_VERSION  = defaultFont.getFontFamily().getFont(Font.PLAIN, FONTSIZE_ARR[2]);        
                        
        FONT_PLAIN = defaultFont.getFontFamily().getFont(Font.PLAIN, font_size);
        FONT_LINK = defaultFont.getFontFamily().getFont( Font.UNDERLINED, font_size);
        FONT_BOLD = defaultFont.getFontFamily().getFont( Font.BOLD, font_size);
        FONT_BOLD_UNDERLINED = defaultFont.getFontFamily().getFont(Font.BOLD|Font.UNDERLINED, font_size);
        FONT_DATE = defaultFont.getFontFamily().getFont(Font.PLAIN, datefont_size);  
        FONT_TABLE = FONT_PLAIN;//ff.getFont(Font.PLAIN, 14);
        FONT_TABLE_HEADER = FONT_BOLD;//ff.getFont(Font.BOLD, 17);
    }
    
    public final static int PHOTO_WIDTH=70;  
    public final static int PHOTO_HEIGHT =70;
        
    public static final int FONT_COLOR_DROPDOWN = 0x00fed1d3;    
    public static final int FONT_COLOR_WHITE= 0x00FFFFFF;    
    public static final int FONT_COLOR_BLACK  = 0x00000000;
    public static final int FONT_COLOR_TITLE= 0x00FFFFFF;    
    public static final int FONT_COLOR_NONEWS   = 0x007c7c7c; 
    public static final int FONT_COLOR_NEWSREAD= 0x007c7c7c;
    public static final int FONT_COLOR_NEWSSELECTED = 0x00000000;
    public static final int FONT_COLOR_PSI = 0x00339900;
    public static final int FONT_COLOR_DEGREE= 0x000099CC;
    public static final int FONT_COLOR_IMAGEBORDER= 0x00FF0000;
    public static final int FONT_COLOR_PHOTOGALLERYBG= 0x00c0c0c0;    
    
    public static final int LINE_COLOR_LIST= 0x00cfcdcd;
    public static final int LINE_COLOR_BYLINE = 0x002c407b;
    
    public static final int TABCOLOR_NORMAL = 0x00FFFFFF;
    public static final int TABCOLOR_FOCUS = 0x00FFFFFF;
    public static final int TABCOLOR_HIGHLIGHT= 0x00FFFFFF;
    //public static final int TABCOLOR_FOCUS = 0x007c7c7c;
    //public static final int TABCOLOR_HIGHLIGHT= 0x007c7c7c;    
    public static int LOGOPANEL_HEIGHT=0;
    public static int TABPANEL_HEIGHT=0;
    public static int NEWSPANEL_HEIGHT=0;
    

}
