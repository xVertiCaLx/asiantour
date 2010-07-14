package com.og.app.gui;

import com.og.app.asiantour.*;
import com.og.app.gui.component.*;
import com.og.app.gui.listener.*;
import com.og.app.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.system.*;
import net.rim.blackberry.api.browser.*;

public class VersionScreen extends MainScreen implements ImageButtonListener
{
    private HorizontalFieldManager buttonFM = new HorizontalFieldManager ( );
    private VerticalFieldManager verticalFM = new VerticalFieldManager  ();
    private ImageButtonField btnyes= null;
    private ImageButtonField btnno= null;
    private StatusObj statusobj = null;
    private LabelField lblfield = null;
    
    FieldChangeListener yeslistener = new FieldChangeListener() {
        
         public void fieldChanged(Field field, int context) {
             ImageButtonField buttonField = (ImageButtonField) field;
             //System.out.println("VersionScreen::yeslistener::"+buttonField.getLabel());
            //Browser.getDefaultSession().displayPage(Const.DOWNLOAD_URL);  

            BrowserSession session = Browser.getDefaultSession();
            session.displayPage(Const.DOWNLOAD_URL);
            session.showBrowser();          
                                                                
            try{Thread.sleep(10000);}catch(Exception e){}
            System.exit(0);
            return;          
         }
     };
         
    FieldChangeListener nolistener = new FieldChangeListener() {
        
         public void fieldChanged(Field field, int context) {
             ImageButtonField buttonField = (ImageButtonField) field;
             //loadMenuScreen();
             System.exit(0);
             return;
         }
     };
              
    public VersionScreen  (StatusObj statusobj)
    {
        this.statusobj=statusobj;
        lblfield = new LabelField(Lang.TXT_NEWVERSIONAVAILABLE);
        lblfield.setFont(GuiConst.FONT_PLAIN);
        btnyes = new ImageButtonField(Lang.BTN_YES);
        btnno = new ImageButtonField(Lang.BTN_NO);
        btnyes.setChangeListener(yeslistener);
        btnno.setChangeListener(nolistener);
        
        buttonFM.add(new SpaceField(3));
        buttonFM.add(btnyes);
        buttonFM.add(new SpaceField(GuiConst.DEFAULT_YESNOSPACE));
        buttonFM.add(btnno);
        
        HorizontalFieldManager hfm = new HorizontalFieldManager ( );
        hfm.add(new SpaceField(3));
        hfm.add(lblfield);
        hfm.add(new SpaceField(3));     
        
        verticalFM.add(hfm);
        verticalFM.add(new LineField(10));
        verticalFM.add(buttonFM);
        add(verticalFM);
        //System.out.println("btnyes:"+btnyes.getIndex()+", btnno:"+btnno.getIndex());        
    }     
    /*
    protected boolean touchEvent(TouchEvent message){
            int x = message.getGlobalX(1);
            int y = message.getGlobalY(1);
            
            int btny = lblfield.getPreferredHeight()+10;
            if ( y>=btny && y<=btny+btnyes.getPreferredHeight() ){
                if ( x<=btnyes.getPreferredWidth() )
                    btnyes.setFocus();
                else if (x>=btnyes.getPreferredWidth()+GUIConst.DEFAULT_YESNOSPACE && 
                            x<=btnyes.getPreferredWidth()+GUIConst.DEFAULT_YESNOSPACE+btnno.getPreferredWidth()) {
                        btnno.setFocus();
                }
            }
            return false;
    }    
    */
    private void loadMenuScreen(){
        synchronized(Application.getEventLock() ){
            try{
                Screen s = UiApplication.getUiApplication().getActiveScreen();
                UiApplication.getUiApplication().popScreen(s);
                s.deleteAll();
                MenuScreen screen = MenuScreen.getInstance();
                //screen.refreshAllCategories(statusobj);
                screen.reinitTab();
                UiApplication.getUiApplication().pushScreen(screen); 
            }catch(Exception e){
                //System.out.println("ERROR:loadMenuScreen:"+e);
            }
        }        
    }  
    
    public void imageButtonClicked(int id){    
            if ( btnyes.getIndex()==id ){
                    Browser.getDefaultSession().displayPage(Const.DOWNLOAD_URL);          
                    try{Thread.sleep(10000);}catch(Exception e){}
                    System.exit(0);
                    return;          
            }else if ( btnno.getIndex()==id ){
                    loadMenuScreen();
            }
    }
    public void imageButtonOnUnfocus(int id){
    }    
    public void imageButtonOnFocus(int id){
    }
    public boolean keyChar(char key, int status, int time) {
        switch (key) {
            case Characters.ESCAPE:
               loadMenuScreen();
               break;
        }
        return true;
    }  
} 


