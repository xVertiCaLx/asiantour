package com.og.app.gui.schedule;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

import com.og.app.gui.GuiConst;
import com.og.app.gui.Lang;
import com.og.app.gui.listener.ListFieldListener;
import com.og.app.util.DataCentre;
import com.og.app.util.Utility;

public class CustomListField extends ListField implements ListFieldCallback {

	protected Vector _elements = new Vector();

	protected Bitmap instruction_bg = null;
	protected Bitmap unselected_bg = null;
	protected Bitmap selected_bg = null;
	protected Bitmap prev_icon = null;
	protected Bitmap next_icon = null;
	protected Bitmap border = null;
	protected Bitmap more_details = null;

	protected Object lock = new Object();
	protected ListFieldListener listener = null;
	protected Font textFont = GuiConst.FONT_BOLD;
	protected DataCentre item;
	protected String selected_country;

	protected int padding = 2;
	protected int bg_y = 0;
	protected int tableNo = 1;
	protected int x;
	protected int text_y = padding;
	protected int more_details_y;
	protected int text_x = padding;
	protected int writable_width;
	protected int fixRowHeight;

	public CustomListField(ListFieldListener listener, int tableNo) {
		super();
		this.listener = listener;
		this.tableNo = tableNo;
		border = Utility.resizeBitmap(Bitmap
				.getBitmapResource("res/news_border.png"),
				GuiConst.SCREENWIDTH, 2);
		if (tableNo == 1) {
			fixRowHeight = GuiConst.HEADER_ROW_HEIGHT + border.getHeight();
		} else {
			fixRowHeight = (GuiConst.FONT_BOLD.getHeight() * 2) + (padding * 4)
					+ GuiConst.FONT_DATE.getHeight() + border.getHeight();
		}

		setEmptyString("", DrawStyle.HCENTER);
		setCallback(this);

		selected_bg = Utility.resizeBitmap(Bitmap
				.getBitmapResource("res/news_list_selected.png"),
				GuiConst.SCREENWIDTH, fixRowHeight);
		unselected_bg = Utility.resizeBitmap(Bitmap
				.getBitmapResource("res/news_list.png"), GuiConst.SCREENWIDTH,
				fixRowHeight);

		more_details = Bitmap.getBitmapResource("res/moreDetails.png");
		setRowHeight(fixRowHeight);

	}

	public void drawListRow(ListField listField, Graphics g, int index, int y,
			int width) {

		/*
		 * Table Numbers: 1 - List Country 2 - TV Schedule 3 - Tour Schedule
		 */
		writable_width = GuiConst.SCREENWIDTH - (padding * 2)
				- more_details.getWidth();

		switch (tableNo) {

		case 1:
			/* List Country */
			background(g, index, y);

			item = (DataCentre) _elements.elementAt(index);

			insertData(g, item.country, y, "tvC");

//			g.drawBitmap(writable_width + padding, more_details_y, more_details
//					.getWidth(), more_details.getHeight(), more_details, 0, 0);

			more_details_y = y + (getRowHeight() - more_details.getHeight())
					/ 2;
			g.drawBitmap(writable_width + padding, more_details_y, more_details
					.getWidth(), more_details.getHeight(), more_details, 0, 0);
			break;

		case 2:
			/* TV Schedule */
			background(g, index, y);
			item = (DataCentre) _elements.elementAt(index);
			insertData(g, item.tvName, y, "tv");

			textFont = GuiConst.FONT_DATE;
			g.setFont(textFont);
			g.drawText(item.tvDate, text_x, text_y);

			more_details_y = y + (getRowHeight() - more_details.getHeight())
					/ 2;
			g.drawBitmap(writable_width + padding, more_details_y, more_details
					.getWidth(), more_details.getHeight(), more_details, 0, 0);

			break;
		case 3:
			/* Tour Schedule */
			background(g, index, y);

			item = (DataCentre) _elements.elementAt(index);

			insertData(g, item.tourName, y, "tour");

			textFont = GuiConst.FONT_DATE;
			g.setFont(textFont);
			g.drawText(item.tourDate + ", " + item.tourCountry, text_x, text_y);
			more_details_y = y + (getRowHeight() - more_details.getHeight())
					/ 2;
			g.drawBitmap(writable_width + padding, more_details_y, more_details
					.getWidth(), more_details.getHeight(), more_details, 0, 0);
			break;
		}

	}

	public void background(Graphics g, int index, int y) {
		if (index == this.getSelectedIndex() && listener.isListFieldFocus()) {
			g.drawBitmap(0, y, selected_bg.getWidth(), selected_bg.getHeight(),
					selected_bg, 0, 0);
			y += getRowHeight() - border.getHeight();
			g.drawBitmap(0, y, border.getWidth(), border.getHeight(), border,
					0, 0);
		} else {
			g.drawBitmap(0, y, unselected_bg.getWidth(), unselected_bg
					.getHeight(), unselected_bg, 0, 0);
			y += getRowHeight() - border.getHeight();
			g.drawBitmap(0, y, border.getWidth(), border.getHeight(), border,
					0, 0);
		}
	}

	private void insertData(Graphics g, String printText, int y, String type) {
		Vector vText = Utility.breakIntoWords(printText);
		int lineNo = 1;
		text_y = padding;
		textFont = GuiConst.FONT_BOLD;
		g.setFont(textFont);
		g.setColor(GuiConst.FONT_COLOR_BLACK);

		if (textFont.getAdvance(printText) <= writable_width) {
			if (type != "tvC")
				text_y = y
						+ ((getRowHeight() - (textFont.getHeight() + GuiConst.FONT_DATE
								.getHeight())) / 2);
			else
				text_y = y
				+ ((getRowHeight() - textFont.getHeight()) / 2);
			
				g.drawText(printText + " ", text_x, text_y);
			text_y += textFont.getHeight() + padding;
		} else {
			text_y += y;
			for (int word = 0; word < vText.size(); word++) {
				if (lineNo > 2) {
					break;
				}

				String tempString = (String) vText.elementAt(word);
				int wordWidth = GuiConst.FONT_TABLE_HEADER
						.getAdvance(tempString + " ");
				if ((text_x + wordWidth >= writable_width)
						|| ((lineNo == 2) && (text_x + wordWidth >= ((writable_width * 75) / 100)))) {
					if (lineNo == 2) {
						tempString = "...";
					} else {
						if (word != 0) {
							text_y += GuiConst.FONT_TABLE_HEADER.getHeight()
									+ padding;
							text_x = padding;
						} else {
							text_y = y
									+ ((getRowHeight() - textFont.getHeight()) / 2);
						}
					}
					lineNo++;
				}

				g.drawText(tempString + " ", text_x, text_y);
				text_x += wordWidth;

			}
			text_y += textFont.getHeight() + padding;
		}
		text_x = padding;

	}

	public void onUnfocus() {
		super.onUnfocus();
	}

	public Vector getAllElements() {
		return _elements;
	}

	public int getSize() {
		return _elements.size();
	}

	public void addElements(DataCentre element) {
		add(element);
	}

	protected void add(DataCentre element) {
		synchronized (lock) {
			_elements.addElement(element);
			setSize(getSize());
		}
	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
		invalidate();
		return false;
	}

	public Object get(ListField listField, int index) {
		if ((index >= 0) && (index < getSize())) {
			return _elements.elementAt(index);
		}
		return null;
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

	public void remove(int index) {
		synchronized (lock) {
			_elements.removeElementAt(index);
			setSize(getSize());
		}
		invalidate();

	}

	protected void removeAll() {
		synchronized (lock) {
			_elements.removeAllElements();
			setSize(0);
		}
		invalidate();
	}

}
