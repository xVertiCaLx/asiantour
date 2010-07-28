package com.og.app.facebook.ui;

import blackberry.action.ActionEvent;
import blackberry.action.ActionListener;
import blackberry.facebook.ApplicationSettings;
import blackberry.net.CookieManager;
import blackberry.ui.BrowserScreen;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;

public class PermissionScreen extends BrowserScreen implements ActionListener
{

	// Logger
	private static final Logger log = LoggerFactory.getLogger(PermissionScreen.class.getName());
	
	// List of actions:
	public static final String ACTION_SUCCESS = "success";
	public static final String ACTION_GRANTED = "granted";
	public static final String ACTION_ERROR = "error";
	
	private ApplicationSettings settings;
	private String baseUrl = null;
	

	public PermissionScreen(ApplicationSettings settings, CookieManager cookieManager) {
		super(null, cookieManager);
		this.settings = settings;
		
		StringBuffer urlBuffer = new StringBuffer()
			.append(settings.graphUrl)
			.append("/oauth/authorize?")
			.append("client_id=").append(String.valueOf(settings.applicationId)).append('&')
			.append("redirect_uri=").append(settings.nextUrl).append('&')
			.append("type=user_agent").append('&')
			.append("display=wap").append('&')
			.append("scope=");
		baseUrl = urlBuffer.toString();
		
		addActionListener(this);
	}
	
	public void requestPermissions(String[] permissions) {
		StringBuffer urlBuffer = new StringBuffer(baseUrl);
		
		for (int i = 0; i < permissions.length; i ++) {
			urlBuffer.append(permissions[i]);
			
			if (i < permissions.length-1)
				urlBuffer.append(',');
		}
		
		setUrl(urlBuffer.toString());
		browse();
	}

	public void actioned(ActionEvent event) {
		if (event.getSource() == this) {
			if (event.getAction().equals(ACTION_SUCCESS)
					&& getUrl().startsWith(settings.nextUrl)) {
				String url = getUrl();
				int startIndex = url.indexOf("access_token");
				
				if (startIndex > -1) {
					int stopIndex = url.length();
					
					if (url.indexOf('&', startIndex) > -1) {
						stopIndex = url.indexOf('&', startIndex);
					} else if (url.indexOf(';', startIndex) > -1) {
						stopIndex = url.indexOf(';', startIndex);
					}
					
					String accessToken = url.substring(url.indexOf('=', startIndex)+1, stopIndex);
					fireActioned(ACTION_GRANTED, accessToken);
				}
			}
		}
	}
	
}
