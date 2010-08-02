package com.og.app.gui;

import com.og.app.gui.listener.ImageButtonListener;
import com.og.app.util.DataCentre;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class TableDetailsScreen extends MainScreen implements
		ImageButtonListener {

	private VerticalFieldManager mainFM = null;
	private HorizontalFieldManager bottomFM = new HorizontalFieldManager(
			Field.USE_ALL_WIDTH);
	private ArticlePanel vFM = null;

	private int table = 0;

	private DataCentre item = null;

	private Bitmap imgUp = null;
	private Bitmap imgDown = null;

	public TableDetailsScreen(int table, final DataCentre item) {
		super();
		this.table = table;
		this.item = item;

		imgUp = Bitmap.getBitmapResource("res/up.png");
		imgDown = Bitmap.getBitmapResource("res/down.png");
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
