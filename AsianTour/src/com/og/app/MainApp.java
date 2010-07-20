package com.og.app;

import net.rim.device.api.ui.UiApplication;

import com.og.app.gui.*;


public class MainApp extends UiApplication{
    
    public static void main (String [] args) {
        MainApp ms = new MainApp();
        ms.enterEventDispatcher();
    }
    
    public MainApp() {
        if ( net.rim.device.api.system.Display.getWidth()>320 )
                GuiConst.FONTSIZE_ARR = GuiConst.FONTSIZE_ARR2;
        pushScreen(MenuScreen.getInstance());
        SplashScreen s = new SplashScreen();
        pushScreen(s);
        enterEventDispatcher();
    }
} 