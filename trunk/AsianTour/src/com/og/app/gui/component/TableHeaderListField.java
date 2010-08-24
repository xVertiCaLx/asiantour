package com.og.app.gui.component;

import com.og.app.gui.GuiConst;
import com.og.app.gui.Lang;
import com.og.app.gui.MenuScreen;
import com.og.app.gui.TableListField;
import com.og.app.util.Utility;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

public class TableHeaderListField extends ListField implements ListFieldCallback {

	protected Bitmap header_bg = null;
	protected Bitmap header_separator = null;

	Font textFont = GuiConst.FONT_TABLE_HEADER;

	String tableType;
	
	private int page;

	int x;
	int y;
	int padding = 2;

	static final String[] meritLabel = { "Pos", "Player", " Earnings", "Played" };

	public TableHeaderListField(String tableType) {
		setEmptyString("", DrawStyle.HCENTER);
		setCallback(this);
		insert(0);
		setSize(1);
		this.tableType = tableType;

		if (header_bg == null)
			header_bg = Utility.resizeBitmap(Bitmap
					.getBitmapResource("res/table_header_bg.png"), GuiConst.SCREENWIDTH, GuiConst.HEADER_ROW_HEIGHT);
		if (header_separator == null)
			header_separator = Bitmap.getBitmapResource("res/table_header_separator.png");

		setRowHeight(header_bg.getHeight());

	}

	public void drawListRow(ListField listField, final Graphics g, int index, int y,
			int width) {
		g.setColor(GuiConst.FONT_COLOR_WHITE);
		g.setFont(textFont);
		x = padding;
		y = y + (getRowHeight() - textFont.getHeight()) / 2;

		if (tableType == "merit") {
			g.drawBitmap(0, 0, header_bg.getWidth(), header_bg.getHeight(),
					header_bg, 0, 0);

			for (int i = 0; i < (meritLabel.length - 1); i++) {
				g.drawText(meritLabel[i], x, y);
				x += TableListField.meritWidth[i] + padding;
				if (i != meritLabel.length) {
					g.drawBitmap(x, y, header_separator.getWidth(),
							header_separator.getHeight(), header_separator, 0,
							0);
					x += header_separator.getWidth() + padding;
				}
			}
		} else if (tableType == "tv") {
			g.drawBitmap(0, 0, header_bg.getWidth(), header_bg.getHeight(),
					header_bg, 0, 0);
			
			g.drawText(Lang.SELECT_COUNTRY_INSTRUCTION, padding, y);
		} else {
			g.drawBitmap(0, 0, header_bg.getWidth(), header_bg.getHeight(),
					header_bg, 0, 0);
			
			g.drawText(Lang.SELECT_ROW_INSTRUCTION, padding, y);
		}
	}
	
	public Object get(ListField listField, int index) {
		if ((index >= 0) && (index < MenuScreen.getInstance().newsCollection.size())) {
			return MenuScreen.getInstance().newsCollection.elementAt(index);
		}
		return null;
	}
	
	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		invalidate();
		return false;
	}
	
	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}
	
	

	public int indexOfList(ListField listField, String prefix, int start) {
		return listField.indexOfList(prefix, start);
	}

	public boolean keyChar(char key, int status, int time) {
		if (getSize() > 0 && key == Characters.SPACE) {
			getScreen().scroll(Manager.DOWNWARD);
			return true;
		}
		return super.keyChar(key, status, time);
	}

	public boolean navigationClick(int status, int time) {
		// System.out.println("aloy.CustomListField.navigationClick: got enter or not?");

		return false;
	}
}
