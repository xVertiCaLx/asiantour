package com.og.app.gui;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;

class AboutScreen extends MainScreen
{
   /*private VerticalFieldManager mainFM = null;
   private ArticlePanel vFM = null;
    private SettingScreen parentscreen = null;

    SettingObj settingobj = null;
    HorizontalFieldManager  bottomFM = new HorizontalFieldManager  (Field.USE_ALL_WIDTH);
    Bitmap imgup = null;
    Bitmap imgdown = null;
    TitleField lbltitle = null;
    SpaceField spacefield = null;
    
    public AboutScreen  (SettingScreen parentscreen)
    {
        this.parentscreen = parentscreen;
        
        settingobj = Utility.loadSetting();
        
        imgup = Bitmap.getBitmapResource("res/up.png");
        imgdown = Bitmap.getBitmapResource("res/down.png");        
        Bitmap settingicon= Bitmap.getBitmapResource("res/icon_about.png");
        lbltitle= new TitleField (Lang.SETTING_ABOUT, settingicon);

        RichTextField lbldescfield = new RichTextField(Lang.TXT_ABOUT ) ;
        lbldescfield .setFont(GUIConst.FONT_PLAIN);
               
         mainFM  = new VerticalFieldManager  ();                 
         vFM = new ArticlePanel(GUIConst.SCREENHEIGHT-lbltitle.getPreferredHeight(), GUIConst.SCREENWIDTH-20);            

        spacefield = new SpaceField(10, GUIConst.SCREENHEIGHT-lbltitle.getPreferredHeight()){
            protected void paint(Graphics graphics)
            {
                if ( vFM.getVerticalScroll()>0 ){
                    graphics.drawBitmap(this.getPreferredWidth()-imgup.getWidth(), 0, imgup.getWidth(), imgup.getHeight(), imgup , 0, 0);  
                }
                if ( vFM.getVerticalScroll()+vFM.getPreferredHeight()< vFM.getVirtualHeight() ){
                    graphics.drawBitmap(this.getPreferredWidth()-imgdown.getWidth(), GUIConst.SCREENHEIGHT-lbltitle.getPreferredHeight()-imgdown.getHeight(), 
                    imgdown.getWidth(), imgdown.getHeight(), imgdown , 0, 0);    
                }
            }            
        };   
        
        vFM.add(new LineField(10));
        vFM.add(lbldescfield);
        
        bottomFM.add(new SpaceField(10, GUIConst.SCREENHEIGHT-lbltitle.getPreferredHeight()));
        bottomFM.add(vFM );        
        bottomFM.add(spacefield);
        
        mainFM.add(lbltitle);        
        mainFM.add(bottomFM);        
        add(mainFM);
    }
    
    protected void paint(Graphics graphics)
    {
        try{
            super.paint(graphics);
    
            spacefield.repaintField();
        }catch(Exception e){}        
    }     
        
    public boolean keyChar(char key, int status, int time) {
        switch (key) {
            case Characters.ESCAPE:
                try{
                    synchronized(Application.getEventLock() ){
                        Screen s = UiApplication.getUiApplication().getActiveScreen();
                        s.deleteAll();
                        UiApplication.getUiApplication().popScreen(s);
                        UiApplication.getUiApplication().pushScreen(parentscreen);              
                    }                            
                    return true;
                }catch(Exception e){
                    //System.out.println(e);
                }
                break;
        }
        return true;
    }  */
    
class ArticlePanel extends VerticalFieldManager{
        int fixheight = 0;
        int fixwidth = 0;
        
        public ArticlePanel(int fixheight, int fixwidth){
            super(Manager.VERTICAL_SCROLL|Manager.VERTICAL_SCROLLBAR);
            this.fixheight=fixheight;
            this.fixwidth=fixwidth;
        }

        public int getPreferredWidth()
        {
            return fixwidth;
        }
        
        public int getPreferredHeight()
        {
            return fixheight;
        }        

        protected void sublayout(int width, int height)
        {
            super.sublayout(fixwidth, fixheight);
            setExtent(fixwidth, fixheight);
        }        
    }        
} 


