package com.og.app.facebook.ui;

import blackberry.action.ActionEvent;
import blackberry.action.ActionListener;
import blackberry.facebook.ApplicationSettings;
import com.og.app.facebook.ui.LoginScreen;
import blackberry.net.CookieManager;
import blackberry.ui.BrowserScreen;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;

public class LoginScreen extends BrowserScreen implements ActionListener {
	private static final Logger log = LoggerFactory.getLogger(LoginScreen.class.getName());
	
	public static final String ACTION_SUCCESS = "success";
	public static final String ACTION_LOGGED_IN = "loggedIn";
	public static final String ACTION_ERROR = "error";
	
	private ApplicationSettings settings;
	
	public LoginScreen(ApplicationSettings settings, CookieManager cookieManager) {
		super(new StringBuffer()
			.append("http://m.facebook.com/tos.php?")
			.append("api_key=").append(settings.applicationKey).append('&')
			.append("v=1.0").append('&')
			.append("next=").append(settings.nextUrl)
			.toString(), cookieManager);
		this.settings = settings;
		
		addActionListener(this);
		
		log.debug("URL: " + getUrl());
	}
	
	public void login() {
		browse();
	}
	
	public void actioned(ActionEvent event) {
		if (event.getSource() == this) {
			if (event.getAction().equals(ACTION_SUCCESS)
					&& getUrl().startsWith(settings.nextUrl)) {
				String url = getUrl();
				log.debug("URL: " + url);
				int startIndex = url.indexOf("auth_token");
				
				if (startIndex > -1) {
					int stopIndex = url.length();
					
					if (url.indexOf('&', startIndex) > -1) {
						stopIndex = url.indexOf('&', startIndex);
					} else if (url.indexOf(';', startIndex) > -1) {
						stopIndex = url.indexOf(';', startIndex);
					}
					
					String authToken = url.substring(url.indexOf('=', startIndex)+1, stopIndex);
					fireActioned(ACTION_LOGGED_IN, authToken);
				}
			}
		}
	}
	
}
