package com.og.app.gui;
import net.oauth.j2me.BadTokenStateException;
import net.oauth.j2me.OAuthServiceProviderException;
import net.oauth.j2me.token.RequestToken;
import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BorderFactory;

import com.og.app.gui.NewsDetailScreen.ArticlePanel;
import com.og.app.gui.component.LineField;
import com.og.app.gui.component.SpaceField;
import com.og.app.gui.component.TitleField;
import com.og.app.social.TwitterHelper;


public class TwitterOAuthScreen extends MainScreen implements FieldChangeListener {
	
	private VerticalFieldManager mainFM = null;
	private HorizontalFieldManager bottomFM = new HorizontalFieldManager(
			Field.USE_ALL_WIDTH);
	private ArticlePanel vFM = null;
	private Bitmap imgUp = null;
	private Bitmap imgDown = null;
	private int headLineHeight = 0;
	private SpaceField spaceField = null;
	private TitleField titleBar = null;
	TextField txtPin = null;
	String contentToPost = null;
	RequestToken requestToken = null;
	ButtonField authBtn = new ButtonField(Lang.TWITTER_AUTHORIZE_LABEL);
	
	
	public TwitterOAuthScreen(final String authUrl, RequestToken token, String contentToPost){
	
		Bitmap settingIcon = Bitmap.getBitmapResource("res/icon_setting.png");
		titleBar = new TitleField("Login to Twitter", settingIcon);
		headLineHeight = titleBar.getPreferredHeight();
		imgUp = Bitmap.getBitmapResource("res/up.png");
		imgDown = Bitmap.getBitmapResource("res/down.png");
		
		mainFM = new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Manager.VERTICAL_SCROLLBAR);
		vFM = new ArticlePanel(GuiConst.SCREENHEIGHT
				- titleBar.getPreferredHeight(), GuiConst.SCREENWIDTH - 20);
		spaceField = new SpaceField(10, GuiConst.SCREENHEIGHT
				- titleBar.getPreferredHeight()) {
			protected void paint(Graphics g) {
				if (vFM.getVerticalScroll() > 0) {
					g
							.drawBitmap(this.getPreferredWidth()
									- imgUp.getWidth(), 0, imgUp.getWidth(),
									imgUp.getHeight(), imgUp, 0, 0);
				}

				if (vFM.getVerticalScroll() + vFM.getPreferredHeight() < vFM
						.getVirtualHeight()) {
					g.drawBitmap(this.getPreferredWidth() - imgDown.getWidth(),
							GuiConst.SCREENHEIGHT
									- titleBar.getPreferredHeight()
									- imgDown.getHeight(), imgDown.getWidth(),
							imgDown.getHeight(), imgDown, 0, 0);
				}
			}
		};
		
		this.contentToPost = contentToPost;
		this.requestToken = token;
		
		RichTextField instrHdrLbl = new RichTextField(Lang.TWITTER_INSTRUCTION_HEADER);
		instrHdrLbl.setFont(GuiConst.FONT_BOLD);
		
		RichTextField instrLbl = new RichTextField(Lang.TWITTER_LOGIN_INSTRUCTION);
		instrLbl.setFont(GuiConst.FONT_PLAIN);
		
		ButtonField authorizeButtonField = new ButtonField(Lang.TWITTER_LOGIN_LABEL , ButtonField.FIELD_HCENTER | ButtonField.CONSUME_CLICK);
		authorizeButtonField.setFont(GuiConst.FONT_PLAIN);
		authorizeButtonField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				Browser.getDefaultSession().displayPage(authUrl);				
			}
		});
		
		HorizontalFieldManager pinField = new HorizontalFieldManager(Field.FIELD_HCENTER);
		LabelField lblPin = new LabelField("Pin: ");
		txtPin = new TextField(Field.FOCUSABLE);//"", "", 10, 0);
		txtPin.setBorder(BorderFactory.createSimpleBorder(new XYEdges(1, 1, 1, 1)));
		txtPin.setMargin(0, 200, 0, 0);
		
		pinField.add(lblPin);
		pinField.add(txtPin);
		
		
		vFM.add(new LineField(3));
		vFM.add(instrHdrLbl);
		vFM.add(new LineField(3));
		vFM.add(new LineField(3,1));
		vFM.add(new LineField(3));
		vFM.add(instrLbl);
		vFM.add(new LineField(3));
		//vFM.add(authorizeButtonField);
		
		//vFM.add(new LineField(10,-1));
		vFM.add(pinField);
		
		HorizontalFieldManager buttonField = new HorizontalFieldManager(Field.FIELD_HCENTER);
		buttonField.add(authorizeButtonField);
		
		authBtn.setFont(GuiConst.FONT_PLAIN);
		authBtn.setChangeListener(this);
		
		buttonField.add(authBtn);
		
		vFM.add(buttonField);
		
		bottomFM.add(new SpaceField(10, vFM.getPreferredHeight()));
		bottomFM.add(vFM);
		bottomFM.add(spaceField);
		
		mainFM.add(titleBar);
		mainFM.add(bottomFM);
		
		add(mainFM);
		

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
	
	class ArticlePanel extends VerticalFieldManager {
		int fixheight = 0;
		int fixwidth = 0;

		public ArticlePanel(int fixheight, int fixwidth) {
			super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
			this.fixheight = fixheight;
			this.fixwidth = fixwidth;
		}

		public int getPreferredWidth() {
			return fixwidth;
		}

		public int getPreferredHeight() {
			return fixheight;
		}
		
		public boolean navigationMovement(int dx, int dy, int status, int time) {
			if (txtPin.getText().equals(null) || txtPin.getText().equals("") ){
				//authBtn.setVisualState(Field.VISUAL_STATE_DISABLED);
				authBtn.select(false);
				invalidate();
			} else {
				authBtn.select(true);
				//authBtn.setVisualState(Field.VISUAL_STATE_NORMAL);
				invalidate();
			}
			return false;
		}

		protected void sublayout(int width, int height) {
			super.sublayout(fixwidth, fixheight);
			setExtent(fixwidth, fixheight);
		}
	}
}
