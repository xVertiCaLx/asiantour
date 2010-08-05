package com.og.app.gui;

import com.og.app.util.DataCentre;
import com.og.app.gui.component.LineField;
import com.og.app.gui.component.SpaceField;
import com.og.app.gui.component.TitleField;
import com.og.app.gui.component.ShareButtonField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TableDetailsScreen extends MainScreen {

	private VerticalFieldManager mainFM = null;
	private HorizontalFieldManager bottomFM = new HorizontalFieldManager(
			Field.USE_ALL_WIDTH);
	private ArticlePanel vFM = null;

	private TitleField title = null;
	private LineField horizontalBreak = new LineField(2,
			GuiConst.LINE_COLOR_BYLINE);
	private LineField paraPadding = new LineField(1);
	private SpaceField spaceField = null;

	private DataCentre item = null;

	private Bitmap imgUp = null;
	private Bitmap imgDown = null;
	private Bitmap titleBarIcon = null;

	public TableDetailsScreen(int table, final DataCentre item) {
		super();
		this.item = item;

		imgUp = Bitmap.getBitmapResource("res/up.png");
		imgDown = Bitmap.getBitmapResource("res/down.png");

		mainFM = new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Manager.VERTICAL_SCROLLBAR);

		/*
		 * Table No: 1 - TV Schedule 2 - Tour Schedule 3 - Live Score 4 - Order
		 * of Merit
		 */

		if (table == 1) {
			tvSchedule();
		} else if (table == 2) {
			tourSchedule();
		} else if (table == 3) {
			liveScore();
		} else if (table == 4) {
			orderOfMerit();
		}

		add(mainFM);
	}

	private void tvSchedule() {
		String dateTime = "";
		String tourName = item.tvName;

		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("TV Schedule - " + tourName, titleBarIcon);
		RichTextField lblTitle = new RichTextField(tourName);
		lblTitle.select(false);
		lblTitle.setFont(GuiConst.FONT_BOLD);

		vFM = new ArticlePanel(GuiConst.SCREENHEIGHT
				- title.getPreferredHeight(), GuiConst.SCREENWIDTH - 20);

		if ((item.tvDate == "TBC") || (item.tvBroadcastTime.startsWith("TBC"))) {
			dateTime = "To be confirmed";
		} else {
			dateTime = item.tvDate + " (" + item.tvBroadcastTime + ")";
		}

		RichTextField lblCountry = new RichTextField("Available in: "
				+ item.tvRegion);
		lblCountry.select(false);
		lblCountry.setFont(GuiConst.FONT_PLAIN);

		RichTextField lblDate = new RichTextField("Broadcast Date: " + dateTime);
		lblDate.select(false);
		lblDate.setFont(GuiConst.FONT_PLAIN);

		RichTextField lblChannel = new RichTextField("Official Broadcaster: "
				+ item.tvBroadcaster);
		lblChannel.select(false);
		lblChannel.setFont(GuiConst.FONT_PLAIN);
		
		RichTextField lblSharing = new RichTextField("Share this article!");
		lblSharing.select(false);
		lblSharing.setFont(GuiConst.FONT_DATE);

		// Content Segment; Content Field
		vFM.add(new LineField(1));
		vFM.add(lblTitle);
		vFM.add(horizontalBreak);
		vFM.add(paraPadding);
		vFM.add(lblSharing);
		vFM.add(new ShareButtonField("tw", "TV", item, null));
		vFM.add(lblCountry);
		vFM.add(new LineField(1));
		vFM.add(lblDate);
		vFM.add(new LineField(1));
		vFM.add(lblChannel);
		
		buildLayout(title.getPreferredHeight());

		System.out.println("Tour Name: " + item.tvName);
		System.out.println("Tour tvBroadcaster: " + item.tvBroadcaster);
		System.out.println("Tour tvBroadcastTime: " + item.tvBroadcastTime);
		System.out.println("Tour tvDate: " + item.tvDate);
		System.out.println("Tour tvRegion: " + item.tvRegion);
		System.out.println("Tour tvIndex: " + item.tvIndex);
	}

	private void tourSchedule() {
		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("Tour Schedule - " + item.tourName, titleBarIcon);
		RichTextField lblTitle = new RichTextField(item.tourName);
		lblTitle.select(false);
		lblTitle.setFont(GuiConst.FONT_BOLD);

		buildLayout(title.getPreferredHeight());
	}

	private void liveScore() {
		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("Live Score of " + item.ls_playerFirstName + " "
				+ item.ls_playerLastName, titleBarIcon);

		buildLayout(title.getPreferredHeight());
	}

	private void orderOfMerit() {
		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("Order of Merit - " + item.merit_player,
				titleBarIcon);

		buildLayout(title.getPreferredHeight());
	}

	private void buildLayout(final int titleHeight) {
		// Middle Segment; Content Field Manager
		spaceField = new SpaceField(10, GuiConst.SCREENHEIGHT - titleHeight) {
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
							(GuiConst.SCREENHEIGHT - titleHeight)
									- imgDown.getHeight(), imgDown.getWidth(),
							imgDown.getHeight(), imgDown, 0, 0);
				}
			}
		};

		bottomFM.add(new SpaceField(10, vFM.getPreferredHeight()));
		bottomFM.add(vFM);
		bottomFM.add(spaceField);

		mainFM.add(new NullField(Field.FOCUSABLE));

		// Overall Segment; Main Field Manager
		mainFM.add(title);
		mainFM.add(bottomFM);
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

}
