package com.og.app.gui.schedule;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.og.app.gui.GuiConst;
import com.og.app.gui.Lang;
import com.og.app.gui.listener.ListFieldListener;

public class DataPanel extends VerticalFieldManager {
	private static DataPanel dataPanel = null;
	private DataListField dataList = null;
	private int fixHeight = 0;
	private ChildNewsPanel childNewsPanel = null;

	Bitmap imgUp = null;
	Bitmap imgDown = null;

	private DataPanel(ListFieldListener listener, int fixHeight, int tableNo) {
		super();
		this.fixHeight = fixHeight;
		imgUp = Bitmap.getBitmapResource("res/up.png");
		imgDown = Bitmap.getBitmapResource("res/down.png");

		dataList = new DataListField(this, listener, tableNo);
		childNewsPanel = new ChildNewsPanel(fixHeight);
		childNewsPanel.add(dataList);
		add(childNewsPanel);
		loadRows(tableNo);
	}

	public void reinitThisThing() {
		dataPanel = null;
	}

	public synchronized void loadRows(int tableNo) {
		dataList.loadTableData(tableNo);

		Field field = childNewsPanel.getField(0);

		if (dataList.getSize() == 0) {
			childNewsPanel.deleteAll();
			String txtNoNews = Lang.DEFAULT_NO_NEWS;
			LabelField displayLabel = new LabelField(txtNoNews,
					Field.FIELD_HCENTER) {
				protected void paintBackground(net.rim.device.api.ui.Graphics g) {
					g.clear();
					g.setColor(GuiConst.FONT_COLOR_NONEWS);
				}
			};

			displayLabel.setFont(GuiConst.FONT_PLAIN);
			HorizontalFieldManager hfm = new HorizontalFieldManager();
			hfm.add(displayLabel);
			childNewsPanel.add(hfm);

		} else {
			if (field instanceof ListField) {
			} else {
				childNewsPanel.deleteAll();
				childNewsPanel.add(dataList);

			}

		}

	}

	protected void paint(Graphics g) {

		super.paint(g);

		if (childNewsPanel.getVerticalScroll() > 0) {
			g.drawBitmap(Display.getWidth() - imgUp.getWidth(), 0, imgUp
					.getWidth(), imgUp.getHeight(), imgUp, 0, 0);
		}
		if ((childNewsPanel.getVerticalScroll() + fixHeight) < (childNewsPanel
				.getVirtualHeight())) {
			g.drawBitmap(Display.getWidth() - imgDown.getWidth(), fixHeight
					- imgDown.getHeight(), imgDown.getWidth(), imgDown
					.getHeight(), imgDown, 0, 0);
		}
	}

	public synchronized static DataPanel getInstance(
			ListFieldListener listener, int fixHeight, int tableNo) {
		if (dataPanel == null) {
			dataPanel = new DataPanel(listener, fixHeight, tableNo);
		}
		return dataPanel;
	}

	public void updateLayout(int height) {
		super.updateLayout();
		childNewsPanel.updateLayout(height);
	}

	public void setFocus() {
		dataList.setFocus();
	}

	class ChildNewsPanel extends VerticalFieldManager {
		int fixHeight = 0;

		public ChildNewsPanel(int fixHeight) {
			super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
			this.fixHeight = fixHeight;
			return;
		}

		public int getPreferredHeight() {
			return fixHeight;
		}

		protected void sublayout(int width, int height) {
			super.sublayout(width, fixHeight);
			setExtent(width, fixHeight);
		}

		public void updateLayout(int height) {
			this.fixHeight = height;
			super.updateLayout();
		}
	}
}
