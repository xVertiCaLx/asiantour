package com.og.app.gui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.og.app.gui.component.AnimatedImageField;
import com.og.app.gui.component.LineField;
import com.og.app.gui.component.SpaceField;
import com.og.app.gui.component.TitleField;
import com.og.app.gui.component.WebBitmapField;
import com.og.rss.ANewsItemObj;

import blackberry.facebook.ApplicationSettings;
import blackberry.net.CookieManager;
import com.og.app.facebook.ui.*;
import blackberry.action.*;

public class NewsDetailScreen extends MainScreen implements Runnable {// implements
	// ImageButtonListener,
	// Runnable
	// {

	private VerticalFieldManager mainFM = null;
	private HorizontalFieldManager bottomFM = new HorizontalFieldManager(
			Field.USE_ALL_WIDTH);

	private Bitmap imgUp = null;
	private Bitmap imgDown = null;

	private int myIndex = -1;
	private int finalWidth = 0;
	private int finalHeight = 0;
	private int index_btnSaved = 0;
	private int headLineHeight = 0;

	private ArticlePanel vFM = null;
	private ImagePanel childPanel = null;

	private BitmapField webImg = null;
	private BitmapField newsImg = null;
	private TitleField lblTitle = null;
	private SpaceField spaceField = null;
	private CustomListField listField = null;
	private AnimatedImageField animatedImg = null;

	// private ClickableImageField bannerField = null;

	public NewsDetailScreen(CustomListField listField, final ANewsItemObj newsItem) {
		super();
		imgUp = Bitmap.getBitmapResource("res/up.png");
		imgDown = Bitmap.getBitmapResource("res/down.png");
		this.myIndex = newsItem.index;
		this.listField = listField;

		if (newsItem.hasread == false) {
			newsItem.hasread = true;
			// listField.saveChanges(newsItem, myIndex);
		}

		Bitmap settingIcon = Bitmap.getBitmapResource("res/icon_news.png");
		lblTitle = new TitleField("Full Article", settingIcon);
		headLineHeight = lblTitle.getPreferredHeight();

		mainFM = new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Manager.VERTICAL_SCROLLBAR);

		RichTextField lblHeadlineField = new RichTextField(newsItem.title);
		lblHeadlineField.select(false);

		lblHeadlineField.setFont(GuiConst.FONT_BOLD);

		String newsInfo = "";

		if (newsItem.author != null && !newsItem.author.equals("")) {
			newsInfo = "written by " + newsItem.author + " | ";
		}

		newsInfo += newsItem.longdate;

		LabelField lblNewsInfo = new LabelField(newsInfo, Field.FOCUSABLE) {
			public void paint(Graphics g) {
				g.setBackgroundColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				g.clear();
				super.paint(g);
			}
		};

		lblNewsInfo.setFont(GuiConst.FONT_DATE);

		RichTextField lblDesc = new RichTextField(newsItem.description);
		lblDesc.select(false);
		lblDesc.setFont(GuiConst.FONT_PLAIN); // | DrawStyle.HFULL);

		finalWidth = newsItem.imagewidth;
		finalHeight = newsItem.imageheight;

		if (finalHeight != 0 && finalWidth != 0) {
			if (finalWidth > finalHeight) {
				int tmpWidth = (int) ((GuiConst.SCREENWIDTH - 20) * 0.5);
				int tmpHeight = (int) ((double) tmpWidth * finalHeight / finalWidth);
				if (tmpHeight > (GuiConst.SCREENHEIGHT - headLineHeight) * 0.5) {
					tmpHeight = (int) ((GuiConst.SCREENHEIGHT - headLineHeight) * 0.5);
					tmpWidth = (int) ((double) tmpHeight * finalWidth / finalHeight);
				}

				if (tmpWidth <= finalWidth && tmpHeight <= finalHeight) {
					finalWidth = tmpWidth;
					finalHeight = tmpHeight;
				}
			} else {
				int tmpHeight = (int) ((GuiConst.SCREENHEIGHT - headLineHeight) * 0.5);
				int tmpWidth = (int) ((double) tmpHeight * finalWidth / finalHeight);
				if (tmpWidth <= finalWidth && tmpHeight <= finalHeight) {
					finalWidth = tmpWidth;
					finalHeight = tmpHeight;
				}
			}
		}

		if (newsItem.imageurl.length() == 0) {
			// this means NO THUMBNAIL
			if (finalHeight != 0 && finalWidth != 0
					& newsItem.imageurl.equals("")) {
				Bitmap loadingimg = Bitmap.getBitmapResource("res/loading.png");
				animatedImg = new AnimatedImageField(finalWidth, finalHeight,
						loadingimg, 12, 100, Field.FIELD_HCENTER
								| Field.FOCUSABLE);
				animatedImg.startAnimation();
				childPanel = new ImagePanel(finalHeight);
				childPanel.add(animatedImg);
				Thread thread = new Thread(this);
				thread.start();
			}
		} else {
			try {
				// Bitmap tmpbitmap =
				// Bitmap.createBitmapFromBytes(newsItem.image, 0, -1, 1);
				// tmpbitmap=Utility.resizeBitmap(tmpbitmap, finalWidth,
				// finalHeight);
				// webImg = new BitmapField( tmpbitmap,
				// Field.FIELD_HCENTER|Field.FOCUSABLE);
				if (newsItem.image == null || newsItem.image.length < 1) {
					webImg = new WebBitmapField(newsItem.imageurl,
							newsItem.guid);
				} else {
					webImg = new BitmapField(Bitmap.createBitmapFromBytes(
							newsItem.image, 0, newsItem.image.length, 1));
				}

			} catch (Exception e) {
				System.out.println("aloy.NewsDetailScreen.exceptione: " + e);
			}
		}
		// define the vertical field manager's screen details
		vFM = new ArticlePanel(GuiConst.SCREENHEIGHT
				- lblTitle.getPreferredHeight(), GuiConst.SCREENWIDTH - 20);
		// adds a line to space the top, so that the "twitter/fb" banner will
		// show 10 pixel lower
		// vFM.add(newLineField(10));

		// add in twitter/fb banner, but not today
		// adds a headline
		vFM.add(new LineField(5)); /* to remove when twitter banner up */
		vFM.add(lblHeadlineField);
		// adds a <hr>
		vFM.add(new LineField(2, GuiConst.LINE_COLOR_BYLINE));
		// adds the author name(s), published date and other information.
		HorizontalFieldManager hfmNewsLine = new HorizontalFieldManager();
		hfmNewsLine.add(lblNewsInfo);
		ButtonField fbButtonField = new ButtonField("Share", ButtonField.CONSUME_CLICK);
		fbButtonField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				new Facebook("http://www.asiantour.com/news.aspx?sid=" + newsItem.guid);
			}
		});
		hfmNewsLine.add(fbButtonField);
		vFM.add(hfmNewsLine);
		vFM.add(new LineField(5));
		if (webImg != null) {
			vFM.add(webImg);
		}
		if (childPanel != null) {
			System.out.println("i believe this is for the thumbnail");
			vFM.add(childPanel);
			vFM.add(new LineField(5));
		}

		vFM.add(lblDesc);

		spaceField = new SpaceField(10, GuiConst.SCREENHEIGHT
				- lblTitle.getPreferredHeight()) {
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
									- lblTitle.getPreferredHeight()
									- imgDown.getHeight(), imgDown.getWidth(),
							imgDown.getHeight(), imgDown, 0, 0);
				}
			}
		};

		bottomFM.add(new SpaceField(10, vFM.getPreferredHeight()));
		bottomFM.add(vFM);
		bottomFM.add(spaceField);

		mainFM.add(new NullField(Field.FOCUSABLE));
		mainFM.add(lblTitle);
		mainFM.add(bottomFM);

		add(mainFM);

	}

	protected void paint(Graphics graphics) {
		try {
			super.paint(graphics);

			spaceField.repaintField();
		} catch (Exception e) {
		}
	}

	public void run() {
		byte[] imgbytes = null;
		/*
		 * try{ imgbytes = ConnectionMgr.loadImage(newsItem.imageurl); if (
		 * imgbytes!=null ){ newsItem.image=imgbytes; Bitmap tmpbitmap =
		 * Bitmap.createBitmapFromBytes(imgbytes, 0, -1, 1);
		 * newsItem.imageheight=tmpbitmap.getHeight();
		 * newsItem.imagewidth=tmpbitmap.getWidth();
		 * tmpbitmap=Utility.resizeBitmap(tmpbitmap, finalWidth, finalHeight);
		 * listfield.saveChanges(newsitem, myindex);
		 * 
		 * synchronized(Application.getEventLock() ){
		 * animatedimg.stopAnimation(tmpbitmap); } } }catch (Exception e){
		 * e.printStackTrace(); //System.out.println("run error:"+e); }
		 */
		imgbytes = null;
	}

	public void setNewsImage(BitmapField newsimg) {
		this.newsImg = newsimg;
	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		return false;
		// return super.navigationMovement(dx, dy, status, time);
	}

	public boolean keyChar(char key, int status, int time) {
		switch (key) {
		case Characters.ESCAPE:
			try {
				Screen s = UiApplication.getUiApplication().getActiveScreen();
				s.deleteAll();
				UiApplication.getUiApplication().popScreen(s);
				clearResource();
				return true;
			} catch (Exception e) {
				// System.out.println(e);
			}
			break;
		}
		return true;
	}

	public void clearResource() {
		mainFM = null;
		bottomFM = null;
		vFM = null;
		webImg = null;
		newsImg = null;
		listField = null;
		childPanel = null;
		animatedImg = null;
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

		protected void sublayout(int width, int height) {
			super.sublayout(fixwidth, fixheight);
			setExtent(fixwidth, fixheight);
		}
	}

	// ---------------------- FACEBOOK --------------------------
	/*
	 * loginScreen = new LoginScreen(settings, cookieManager);
	 * loginScreen.addActionListener(this);
	 * 
	 * permissionScreen = new PermissionScreen(settings, cookieManager);
	 * permissionScreen.addActionListener(this);
	 * 
	 * loginScreen.login(); pushScreen(loginScreen);
	 */
	
	
	
	// ---------------------- END OF FACEBOOK -------------------

	class ImagePanel extends VerticalFieldManager {
		int fixheight = 0;

		public ImagePanel(int height) {
			super(Field.USE_ALL_WIDTH);
			this.fixheight = height;
		}

		public void updateLayout(int height) {
			this.fixheight = height;
			super.updateLayout();
		}

		public int getPreferredHeight() {
			return fixheight;
		}

		protected void sublayout(int width, int height) {
			super.sublayout(width, fixheight);
			setExtent(width, fixheight);
		}

	}
}
