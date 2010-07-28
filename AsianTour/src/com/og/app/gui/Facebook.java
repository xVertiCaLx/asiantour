package com.og.app.gui;

import com.og.app.facebook.ui.LoginScreen;
import com.og.app.facebook.ui.PermissionScreen;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import blackberry.action.ActionEvent;
import blackberry.action.ActionListener;
import blackberry.facebook.ApplicationSettings;
import blackberry.facebook.ExtendedPermission;
import blackberry.facebook.FacebookContextImpl;
import blackberry.net.ConnectionFactory;
import blackberry.net.ConnectionType;
import blackberry.net.CookieManager;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;
import facebook.FacebookContext;
import facebook.FacebookException;

public class Facebook extends UiApplication implements ActionListener {
	
	private static final String REST_URL = "http://api.facebook.com/restserver.php";				// As per Facebook.
	private static final String GRAPH_URL = "https://graph.facebook.com";							// As per Facebook.
	private static final String NEXT_URL = "http://www.facebook.com/connect/login_success.html";	// Your successful URL.
	private static final String APPLICATION_KEY = "51d0972badc2bba08637c4a306d60851";				// Your Facebook Application Key. 
	private static final String APPLICATION_SECRET = "cb2fa41fbf56ba415a2cff0037377cff";			// Your Facebook Application Secret.
	private static final long APPLICATION_ID = 140944745935626L;
	private static PersistentObject store;
	private static ApplicationSettings settings;
	
	private String URL_TO_SHARE = "";
	
	private static Facebook fb = null;
	
	public synchronized static Facebook getInstance(String URL_TO_SHARE) {
		if (fb == null) {
			fb = new Facebook(URL_TO_SHARE);
			ConnectionFactory.getInstance().setConnectionType(ConnectionType.DIRECT_TCP);
		}
		return fb;
	}
	
	//facebook
    static {
		//long of com_og_asiantour_fbsession_test1 == 0xd2f5116b41798d8eL
		store = PersistentStore.getPersistentObject(0xd2f5116b41798d8eL);
		
		synchronized (store) {
			if (store.getContents() == null) {
				store.setContents(new ApplicationSettings(REST_URL, GRAPH_URL, NEXT_URL, APPLICATION_KEY, APPLICATION_SECRET, APPLICATION_ID));
				store.commit();
			}
		}
		
		settings = (ApplicationSettings)store.getContents();
	}
    
    private static final Logger log = LoggerFactory.getLogger(NewsDetailScreen.class.getName());
	
	private CookieManager cookieManager = new CookieManager();
	
	public Facebook (String URL_TO_SHARE) {
		super();
		fb = this;
		this.URL_TO_SHARE = URL_TO_SHARE;
		log.debug("====== START =======");
		try {
			new FacebookContextImpl(settings);
		} catch (FacebookException e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
			System.exit(0);
		}
		
		if (!FacebookContext.getInstance().hasSession()) {
			//no session found, user not logged in. 
			fbLogin(settings, cookieManager, this);
		} else {
			//session found, so user is logged in. post in wall
			try {
				FacebookContext.getInstance().getLoggedInUser().setStatus(URL_TO_SHARE);
				Dialog.inform("News has been shared on " + FacebookContext.getInstance().getLoggedInUser().getFirstName() + "\'s wall.");
			} catch (Exception e) {
				System.out.println( e.getMessage());
			}
		}
	}
	
	private PermissionScreen permissionScreen = null;
	private LoginScreen loginScreen = null;
	
	public void fbLogin(ApplicationSettings settings, CookieManager cookieManager, ActionListener actionListener) {
		loginScreen = new LoginScreen(settings, cookieManager);
		loginScreen.addActionListener(actionListener);
		
		permissionScreen = new PermissionScreen(settings, cookieManager);
		permissionScreen.addActionListener(actionListener);
		loginScreen.login();
		pushScreen(loginScreen);
	}
	
	public void actioned(ActionEvent event) {
		if (event.getSource() == loginScreen) {
			if (event.getAction().equals(LoginScreen.ACTION_LOGGED_IN)) {
				try { popScreen(loginScreen); }
				catch (IllegalArgumentException e) {}
				
				try {
					FacebookContext.getInstance().getSession((String)event.getData());
					FacebookContext.getInstance().upgradeSession();
					
					permissionScreen.requestPermissions(new String[] {
						ExtendedPermission.OFFLINE_ACCESS,
						ExtendedPermission.PUBLISH_STREAM
					});
					pushScreen(permissionScreen);
				} catch (Exception e) {
					Dialog.alert("Error: " + e.getMessage());
				}
			} else if (event.getAction().equals(LoginScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		} else if (event.getSource() == permissionScreen) {
			if (event.getAction().equals(PermissionScreen.ACTION_GRANTED)) {
				try { popScreen(permissionScreen); }
				catch (IllegalArgumentException e) {}
				
				try {
					synchronized (store) {
						store.setContents(settings);
						store.commit();
					}
					
					//pushScreen(homeScreen);
					//instead of pushing, post news
					try {
						FacebookContext.getInstance().getLoggedInUser().setStatus(URL_TO_SHARE);
						Dialog.inform("News has been shared on " + FacebookContext.getInstance().getLoggedInUser().getFirstName() + "\'s wall.");
					} catch (Exception e) {
						System.out.println( e.getMessage());
					}
					
					
				} catch (Exception e) {
					Dialog.alert("Error: " + e.getMessage());
				}
			} else if (event.getAction().equals(PermissionScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		}
	}
	
}
