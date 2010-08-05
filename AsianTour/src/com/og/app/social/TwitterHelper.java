package com.og.app.social;

import java.io.IOException;
import java.util.Hashtable;

import net.oauth.j2me.BadTokenStateException;
import net.oauth.j2me.Consumer;
import net.oauth.j2me.OAuthMessage;
import net.oauth.j2me.OAuthServiceProviderException;
import net.oauth.j2me.token.AccessToken;
import net.oauth.j2me.token.RequestToken;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.og.app.gui.TwitterOAuthScreen;

public class TwitterHelper {
	private static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	private static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize?oauth_token=";
	private static final String CONSUMER_KEY = "qnBc5vuVeTfsSklSUm7ymQ";
	private static final String CONSUMER_SECRET = "RGdOTxmHEG7VV9oEjU5aLHyCUbgfp0xzMZS8S3lpxus";
	private static final String SIGNATURE_METHOD = "HMAC-SHA1";
	private static final long DATASTORE_KEY = 0x358c8f7e3e76d79eL;
	private static final String STATUS_UPDATE_URL = "http://api.twitter.com/statuses/update.xml";
	
	public static void UpdateStatus(String contentToPost) throws OAuthServiceProviderException{
		AccessToken accessToken = GetAccessToken();
		if(accessToken==null){
			SetupAuthorizationRequest(contentToPost);
		}else{
			Consumer c = new Consumer(CONSUMER_KEY, CONSUMER_SECRET);
			c.setSignatureMethod(SIGNATURE_METHOD);
			Hashtable params =  new Hashtable(1);
			params.put("status", "AsianTour: " + contentToPost);
			Dialog.alert("Article posted on Twitter.");
			try {
				c.accessProtectedResource2(STATUS_UPDATE_URL, accessToken, params, OAuthMessage.METHOD_POST);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void SetupAuthorizationRequest(String contentToPost){
		UiApplication mainApp = UiApplication.getUiApplication();
		Consumer c = new Consumer(CONSUMER_KEY, CONSUMER_SECRET);
		c.setSignatureMethod(SIGNATURE_METHOD);
		RequestToken requestToken = null;
		try {
			requestToken = c.getRequestToken(REQUEST_URL);
		} catch (OAuthServiceProviderException e) {
			e.printStackTrace();
		}
		String token = requestToken.getToken();
		String authorizeUrl = AUTHORIZE_URL + token;
		System.out.println(authorizeUrl);
		TwitterOAuthScreen twitterOAuthScreen = new TwitterOAuthScreen(authorizeUrl, requestToken, contentToPost);
		mainApp.pushScreen(twitterOAuthScreen);
	}
	
	public static void FinishAuthRequest(RequestToken requestToken, String verifier, String contentToPost) throws OAuthServiceProviderException, BadTokenStateException{
		Consumer c = new Consumer(CONSUMER_KEY, CONSUMER_SECRET);
		c.setSignatureMethod(SIGNATURE_METHOD);
		AccessToken accessToken = c.getAccessToken(ACCESS_URL, requestToken, verifier);
		SetAccessToken(accessToken);
		Screen s = UiApplication.getUiApplication().getActiveScreen();
		UiApplication.getUiApplication().popScreen(s);
		Hashtable params =  new Hashtable(1);
		params.put("status", "AsianTour: " + contentToPost);
		try {
			c.accessProtectedResource2(STATUS_UPDATE_URL, accessToken, params, OAuthMessage.METHOD_POST);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Dialog.alert("Posting news on twitter!");
	}
	
	public static AccessToken GetAccessToken(){
		PersistentObject pObj = PersistentStore.getPersistentObject(DATASTORE_KEY);
		Object obj = pObj.getContents();
		if(obj==null){
			return null;
		}
		return (AccessToken)obj;
	}
	
	private static void SetAccessToken(AccessToken accessToken){
		PersistentObject pObj = PersistentStore.getPersistentObject(DATASTORE_KEY);
		pObj.setContents(accessToken);
		pObj.commit();
	}
	
	
}
