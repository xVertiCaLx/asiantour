package com.og.app.gui;
import net.oauth.j2me.BadTokenStateException;
import net.oauth.j2me.OAuthServiceProviderException;
import net.oauth.j2me.token.RequestToken;
import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BorderFactory;

import com.og.app.gui.component.LineField;
import com.og.app.social.TwitterHelper;


public class TwitterOAuthScreen extends MainScreen implements FieldChangeListener {
	
	TextField txtPin = null;
	String contentToPost = null;
	RequestToken requestToken = null;
	public TwitterOAuthScreen(final String authUrl, RequestToken token, String contentToPost){
		this.contentToPost = contentToPost;
		this.requestToken = token;
		ButtonField authorizeButtonField = new ButtonField("Get My Twitter OAuth Pin!" , ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK);
		authorizeButtonField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				Browser.getDefaultSession().displayPage(authUrl);				
			}
		});
		add(authorizeButtonField);
		HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(Field.FIELD_HCENTER);
		LabelField lblPin = new LabelField("Pin: ");
		txtPin = new TextField("", "", 10, 0);
		txtPin.setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1)));
		txtPin.setMargin(0, 200, 0, 0);
		add(new LineField(3,1));
		add(new LineField(10,-1));
		horizontalFieldManager.add(lblPin);
		horizontalFieldManager.add(txtPin);
		add(horizontalFieldManager);
		ButtonField authBtn = new ButtonField("Authorize AsianTour");
		authBtn.setChangeListener(this);
		add(authBtn);
	}

	public void fieldChanged(Field field, int context) {
		String verifier = txtPin.getText();
		try {
			TwitterHelper.FinishAuthRequest(requestToken, verifier, contentToPost);
		} catch (OAuthServiceProviderException e) {
			e.printStackTrace();
		} catch (BadTokenStateException e) {
			e.printStackTrace();
		}
	}
}
