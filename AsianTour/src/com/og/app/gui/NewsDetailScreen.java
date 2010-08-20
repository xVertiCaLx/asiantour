package com.og.app.gui;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYPoint;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
//import net.rim.device.api.ui.TouchEvent;

import com.og.app.gui.component.AnimatedImageField;
import com.og.app.gui.component.LineField;
import com.og.app.gui.component.ShareButtonField;
import com.og.app.gui.component.SpaceField;
import com.og.app.gui.component.TabField;
import com.og.app.gui.component.TitleField;
import com.og.app.gui.component.WebBitmapField;
import com.og.app.gui.social.TwitterLoginScreen;
import com.og.app.util.ConnectionMgr;
import com.og.app.util.Utility;
import com.og.xml.ANewsItemObj;

public class NewsDetailScreen extends MainScreen implements Runnable {

	private VerticalFieldManager mainFM = null;
	private HorizontalFieldManager bottomFM = new HorizontalFieldManager(
			Field.USE_ALL_WIDTH);

	private Bitmap imgUp = null;
	private Bitmap imgDown = null;

	private int myIndex = -1;
	private int finalWidth = 0;
	private int finalHeight = 0;

	private ArticlePanel vFM = null;
	private ImagePanel childPanel = null;

	private BitmapField webImg = null;
	private TitleField lblTitle = null;
	private SpaceField spaceField = null;
	private CustomListField listField = null;
	private AnimatedImageField animatedImg = null;
	private ANewsItemObj newsItem = null;

	public NewsDetailScreen(CustomListField listField,
			final ANewsItemObj newsItem) {
		super();
		this.newsItem = newsItem;
		imgUp = Bitmap.getBitmapResource("res/up.png");
		imgDown = Bitmap.getBitmapResource("res/down.png");
		this.myIndex = newsItem.index;
		this.listField = listField;

		if (newsItem.hasread == false) {
			newsItem.hasread = true;
		}

		lblTitle = new TitleField("Full Article", Bitmap
				.getBitmapResource("res/icon_news.png"));

		mainFM = new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Manager.VERTICAL_SCROLLBAR);

		drawScreen();
	}

	private void drawScreen() {
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

		if (newsItem.imageurl.length() != 0) {
			loadImage();
		}

		vFM = new ArticlePanel(GuiConst.SCREENHEIGHT
				- lblTitle.getPreferredHeight(), GuiConst.SCREENWIDTH - 20);
		vFM.add(lblHeadlineField);
		// adds a <hr>
		vFM.add(new LineField(2, GuiConst.LINE_COLOR_BYLINE));
		// adds the author name(s), published date and other information.

		vFM.add(lblNewsInfo);
		vFM.add(new LineField(1));
		ButtonPanel buttonPanel = new ButtonPanel();
		// buttonPanel.add(new ShareButtonField("fb", newsItem));
		buttonPanel.add(new ShareButtonField("tw", "News", null, newsItem));

		HorizontalFieldManager hFM = new HorizontalFieldManager();
		hFM.add(buttonPanel);

		vFM.add(hFM);
		vFM.add(new LineField(2));

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

	private void loadImage() {
		try {

			finalWidth = (GuiConst.SCREENWIDTH / 100) * 85;
			finalHeight = 300;

			if (newsItem.image == null || newsItem.image.length < 1) {
				webImg = new WebBitmapField(newsItem.imageurl, newsItem.guid);
				Bitmap loadingimg = Bitmap.getBitmapResource("res/loading.png");
				animatedImg = new AnimatedImageField(finalWidth, finalHeight,
						loadingimg, 12, 100, Field.FIELD_HCENTER
								| Field.FOCUSABLE);
				animatedImg.startAnimation();
				childPanel = new ImagePanel(finalHeight);
				childPanel.add(animatedImg);
				Thread thread = new Thread(this);
				thread.start();
			} else {
				Bitmap bmp = Bitmap.createBitmapFromBytes(newsItem.image, 0,
						newsItem.image.length, 1);
				bmp = Utility.resizeBitmap(bmp, finalWidth, (finalWidth / bmp
						.getWidth())
						* bmp.getHeight());
				webImg = new BitmapField(bmp);
				childPanel = new ImagePanel(webImg.getHeight());
				childPanel.add(webImg);
			}

		} catch (Exception e) {
			System.out
					.println("aloy.NewsDetailScreen.whiledrawing.exceptione: "
							+ e);
		}
	}

	public void run() {
		byte[] imgbytes = null;
		try {
			imgbytes = ConnectionMgr.loadImage(newsItem.imageurl);
			if (imgbytes != null) {
				newsItem.image = imgbytes;
				Bitmap tmpbitmap = Bitmap.createBitmapFromBytes(imgbytes, 0,
						-1, 1);
				newsItem.imageheight = tmpbitmap.getHeight();
				
				tmpbitmap = Utility.resizeBitmap(tmpbitmap, finalWidth,
						finalHeight);
				listField.saveChanges(newsItem, myIndex);

				synchronized (Application.getEventLock()) {
					animatedImg.stopAnimation(tmpbitmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		imgbytes = null;
	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		System.out.println("i moved " + dx + " " + dy);
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

	class ButtonPanel extends HorizontalFieldManager {
		int fixHeight = new ShareButtonField("fb", "News", null, newsItem)
				.getPreferredHeight() + 4;

		public ButtonPanel() {
			super(Manager.USE_ALL_WIDTH | Manager.HORIZONTAL_SCROLL
					| Manager.HORIZONTAL_SCROLLBAR | Field.FOCUSABLE);
		}

		public void updateLayout(int height) {
			this.fixHeight = new ShareButtonField("tw", "News", null, newsItem)
					.getPreferredHeight() + 4;
			super.updateLayout();
		}

		public void sublayout(int width, int height) {
			super.sublayout(width, fixHeight);
			setExtent(width, fixHeight);
		}

	}

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
	
//	protected boolean touchEvent(TouchEvent te) {
//		if(te.getEvent() == TouchEvent.CLICK){
//			int gX = te.getX(1);
//			int gY = te.getY(1);
//			System.out.println("TE: " + gX + ", " + gY);
//			TwitterLoginScreen twitter = new TwitterLoginScreen("AT News");;
//			UiApplication.getUiApplication().pushScreen(twitter);
//		}
//		return super.touchEvent(te);
//	}

	public void imageButtonClicked(int id) {
		// TODO Auto-generated method stub

	}

	public void imageButtonOnFocus(int id) {
		// TODO Auto-generated method stub

	}

	public void imageButtonOnUnfocus(int id) {
		// TODO Auto-generated method stub

	}
}
