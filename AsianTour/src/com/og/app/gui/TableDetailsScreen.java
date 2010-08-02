package com.og.app.gui;

import com.og.app.gui.listener.ImageButtonListener;
import com.og.app.util.DataCentre;
import com.og.app.gui.component.SpaceField;
import com.og.app.gui.component.TitleField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TableDetailsScreen extends MainScreen {

	private VerticalFieldManager mainFM = null;
	private HorizontalFieldManager bottomFM = new HorizontalFieldManager(
			Field.USE_ALL_WIDTH);
	private ArticlePanel vFM = null;
	
	private TitleField title = null;
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
		
		/* Table No:    
        1 - TV Schedule
        2 - Tour Schedule
        3 - Live Score
        4 - Order of Merit */
		
		if (table == 1) {
			tvSchedule();
		} else if (table == 2) {
			tourSchedule();
		} else if (table == 3) {
			liveScore();
		} else if (table == 4) {
			orderOfMerit();
		}
		
		
	}
	
	private void tvSchedule() {
		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("TV Schedule - " + item.tvName, titleBarIcon);
	}
	
	private void tourSchedule() {
		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("Tour Schedule - " + item.tourName, titleBarIcon);
	}
	
	private void liveScore() {
		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("Live Score of " + item.ls_playerFirstName + " " + item.ls_playerLastName, titleBarIcon);
	}
	
	private void orderOfMerit() {
		titleBarIcon = Bitmap.getBitmapResource("res/icon_news.png");
		title = new TitleField("Order of Merit - " + item.merit_player, titleBarIcon);
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
