package com.og.app.gui;

import java.io.IOException;
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

import com.og.app.gui.listener.ListFieldListener;
import com.og.app.util.DataCentre;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;

public class TableListField extends ListField implements ListFieldCallback/*
																		 * ,
																		 * Runnable
																		 */{

	// screen sizes: 320, 360, 480

	protected Vector _elements = new Vector();

	protected Bitmap header_bg = null;
	protected Bitmap header_separator = null;
	protected Bitmap even_bg = null;
	protected Bitmap even_separator = null;
	protected Bitmap odd_bg = null;
	protected Bitmap odd_separator = null;
	protected Bitmap prev_icon = null;
	protected Bitmap next_icon = null;
	protected Bitmap border = null;

	protected Object lock = new Object();
	protected ListFieldListener listener = null;

	Font textFont = GuiConst.FONT_TABLE_HEADER;

	public int table = 1;
	public int row = 0;
	public int padding = 2;
	public int bg_y = 0;
	public int text_y = padding;
	public int text_x = padding;
	public int page = 1;
	public int temp_x = 0;

	public int arraySize = 50;

	public int this_x = 0;
	public int prev_x = padding;
	public int total_x = 0;

	public int rowHeight = 69;

	static final int constantColWidth = (GuiConst.SCREENWIDTH / 20);

	// header for Live Score table
	static final String[] liveLabel = { "Player", "Mark", "Country", "Pos",
			"To Par", "Hole", "Today", "R1", "R2", "R3", "R4", "Total" };
	static final int[] liveWidth = { constantColWidth * 9,
			constantColWidth * 3, constantColWidth * 6, constantColWidth * 3,
			constantColWidth * 3, constantColWidth * 3, constantColWidth * 2,
			constantColWidth, constantColWidth, constantColWidth,
			constantColWidth, constantColWidth * 3 };

	static final String[] meritLabel = { "Pos", "Player", " Earnings", "Played" };
	static final int[] meritWidth = { constantColWidth * 2,
			constantColWidth * 10, constantColWidth * 4, 
			constantColWidth * 2};

	// this is for live score
	public TableListField(ListFieldListener listener, int table, int page) {
		super();
		this.listener = listener;
		this.table = table;
		this.page = page;
		this.row = 0;

		setEmptyString("", DrawStyle.HCENTER);
		setCallback(this);

		header_bg = Utility.resizeBitmap(Bitmap
				.getBitmapResource("res/table_header_bg.png"), this
				.getPreferredWidth(), GuiConst.HEADER_ROW_HEIGHT);// Bitmap.getBitmapResource("res/table_header_bg.png");
//		even_bg = Utility.resizeBitmap(Bitmap
//				.getBitmapResource("res/table_even_bg.png"), this
//				.getPreferredWidth(), 34);
//		odd_bg = Utility.resizeBitmap(Bitmap
//				.getBitmapResource("res/table_odd_bg.png"), this
//				.getPreferredWidth(), 34);
		even_bg = Utility.resizeBitmap(Bitmap
				.getBitmapResource("res/table_even_bg.png"), this
				.getPreferredWidth(), rowHeight);
		odd_bg = Utility.resizeBitmap(Bitmap
				.getBitmapResource("res/table_odd_bg.png"), this
				.getPreferredWidth(), rowHeight);
		border = Utility.resizeBitmap(Bitmap
				.getBitmapResource("res/table_border.png"), this
				.getPreferredWidth(), 1);

		header_separator = Bitmap
				.getBitmapResource("res/table_header_separator.png");
		even_separator = Bitmap
				.getBitmapResource("res/table_even_separator.png");
		odd_separator = Bitmap.getBitmapResource("res/table_odd_separator.png");
		prev_icon = Bitmap.getBitmapResource("res/table_prev_page.png");
		next_icon = Bitmap.getBitmapResource("res/table_next_page.png");

		setRowHeight(header_bg.getHeight());

	}

	public void onUnfocus() {
		super.onUnfocus();
	}

	public int getSize() {
		return _elements.size();
	}

	protected void add(DataCentre element) {
		synchronized (lock) {
			_elements.addElement(element);
			setSize(getSize());
		}
	}

	public void drawListRow(ListField listField, Graphics g, int index, int y,
			int width) {
		/*
		 * Table No: 1 - TV Schedule 2 - Tour Schedule 3 - Live Score 4 - Order
		 * of Merit
		 */

		if (table == 3) {
			if (page == 1) {
				if (index == 0) {
					textFont = GuiConst.FONT_TABLE_HEADER;
					setupBackground(g, index, y);
					// if row is zero, it is a header row, set up Table Header
					g.setColor(GuiConst.FONT_COLOR_WHITE);
					g.setFont(textFont);
					text_y = (header_bg.getHeight() - textFont.getHeight()) / 2;
					for (int i = 0; i < 3; i++) {

						g.drawText(liveLabel[i], prev_x, text_y);

						prev_x += liveWidth[i] + padding; // +

						if (i == 2) {
							g.drawBitmap((GuiConst.SCREENWIDTH - next_icon
									.getWidth()), text_y - 2, next_icon
									.getWidth(), next_icon.getHeight(),
									next_icon, 0, 0);
						} else {
							g.drawBitmap(prev_x, 2,
									header_separator.getWidth(),
									header_separator.getHeight(),
									header_separator, 0, 0);
						}

						prev_x += header_separator.getWidth() + padding;

					}
					// next row
					if (getRowHeight() == header_bg.getHeight()) {
						setRowHeight(odd_bg.getHeight() + border.getHeight());
					}
					text_x = padding;
					temp_x = 0;
					prev_x = padding;
					row++;
				} else {

					setupBackground(g, index, y);

					// get content in array and draw list
					text_y = y + padding;
					text_x = padding;
					temp_x = padding;

					// //System.out.println("this is row index : " + index);
					DataCentre item = (DataCentre) _elements
							.elementAt(index - 1);

					setupCell(g, item.ls_playerFirstName + " "
							+ item.ls_playerLastName, y, liveWidth[0], true);

					setupCell(g, item.ls_mark, y, liveWidth[1], true);

					setupCell(g, item.tvBroadcastTime, y, liveWidth[2], true);
					row = 0;
					temp_x = padding;
					text_x = padding;
					prev_x = padding;
					bg_y = y;
				}
			}
		} else if (table == 4) {
			if (index == 0) {
				textFont = GuiConst.FONT_TABLE_HEADER;
				setupBackground(g, index, y);
				// if row is zero, it is a header row, set up Table Header
				g.setColor(GuiConst.FONT_COLOR_WHITE);
				g.setFont(textFont);
				text_y = (header_bg.getHeight() - textFont.getHeight()) / 2;
				for (int i = 0; i < meritLabel.length; i++) {
					g.drawText(meritLabel[i], prev_x, text_y);

					prev_x += meritWidth[i] + padding;

					if (i != (meritLabel.length - 1)) {
						g.drawBitmap(prev_x, 2, header_separator.getWidth(),
								header_separator.getHeight(), header_separator,
								0, 0);
					}

					prev_x += header_separator.getWidth() + padding;

				}
				// next row
				if (getRowHeight() == header_bg.getHeight()) {
					//noted,ver:comment out this code segment
					//setRowHeight(odd_bg.getHeight() + border.getHeight());
					setRowHeight(rowHeight);
				}
				text_x = padding;
				temp_x = 0;
				prev_x = padding;
				row++;
			} else {

				setupBackground(g, index, y);

				// get content in array and draw list
				text_y = y + padding;
				text_x = padding;
				temp_x = padding;

				DataCentre item = (DataCentre) _elements.elementAt(index - 1);

				setupCell(g, item.merit_pos, y, meritWidth[0], true);
				
				int _merit_pos = 0;
				try
				{
					_merit_pos = Integer.parseInt(item.merit_pos.trim());
					if (_merit_pos > 0 && _merit_pos <= 5) {
						if(item.merit_playerphotoURL.length() > 0){
							//fetch bytes
							final int _index = index;
							try {
								Utility.getWebData(item.merit_playerphotoURL, new WebDataCallback() {
									public void callback(byte[] data) {
										if(data!=null){
											((DataCentre)_elements.elementAt(_index - 1)).merit_playerphoto = data;
										}
									}
								});
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						setupProfile(g, item.merit_playerphoto, item.merit_player, y, meritWidth[1], true);
					} else {
						setupCell(g, item.merit_player, y, meritWidth[1], true);
					}
				}catch(Exception ex) {
				}
				setupCell(g, item.merit_prize, y, meritWidth[2], true);
				setupCell(g, item.merit_played, y, meritWidth[3], true);

				row = 0;
				temp_x = padding;
				text_x = padding;
				prev_x = padding;
				bg_y = y;
			}
		}
		// 192.168.1.48 HPJET 3030
	}

	private void setupProfile(Graphics g, byte[] playerPhoto, String playerName, int y, int compareWidth, boolean standalone) {
		int desiredWidth = 0;
		int desiredHeight = 0;
		
		//drawing the PlayerPhoto
		if (playerPhoto != null && playerPhoto.length > 2){
			Bitmap playerPhoto_bmp = null;
			try{
				playerPhoto_bmp = Bitmap.createBitmapFromBytes(playerPhoto, 0, playerPhoto.length, 1);
				desiredWidth = 46;
				desiredHeight = (desiredWidth * playerPhoto_bmp.getHeight())/playerPhoto_bmp.getWidth();
				playerPhoto_bmp = Utility.resizeBitmap(playerPhoto_bmp, desiredWidth, desiredHeight);
				g.drawBitmap(text_x, text_y, desiredWidth, desiredHeight, playerPhoto_bmp, 0, 0);
			}catch(Exception e){
			}
		}
		
		//drawing the PlayerName
		text_x += desiredWidth + padding;//added for the space between playerPhoto and playerName
		Vector vText = Utility.breakIntoWords(playerName);
		int lineNo = 1;

		textFont = GuiConst.FONT_TABLE;
		g.setFont(textFont);
		g.setColor(GuiConst.FONT_COLOR_BLACK);

		if (textFont.getAdvance(playerName) <= compareWidth) {
			text_y = y + ((getRowHeight() - textFont.getHeight()) / 2);
			g.drawText(playerName + " ", text_x, text_y);
		} else {
			for (int word = 0; word < vText.size(); word++) {
				if (lineNo > 2) {
					break;
				}

				String tempString = (String) vText.elementAt(word);
				int wordWidth = GuiConst.FONT_TABLE
						.getAdvance(tempString + " ");
				if ((text_x + wordWidth >= compareWidth)
						|| ((lineNo == 2) && (text_x + wordWidth >= ((compareWidth * 75) / 100)))) {
					if (lineNo == 2) {
						tempString = "...";
					} else {
						if (word != 0) {
							text_y += GuiConst.FONT_TABLE.getHeight() + padding;
							text_x = temp_x;
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
		}
		
		//Calculating...
		if (standalone) {
			calculateCellsInbetween(y, compareWidth);
		}
	}
	
	private void setupCell(Graphics g, String printText, int y, int compareWidth, boolean standalone) {
		Vector vText = Utility.breakIntoWords(printText);
		int lineNo = 1;

		textFont = GuiConst.FONT_TABLE;
		g.setFont(textFont);
		g.setColor(GuiConst.FONT_COLOR_BLACK);

		if (textFont.getAdvance(printText) <= compareWidth) {
			text_y = y + ((getRowHeight() - textFont.getHeight()) / 2);
			g.drawText(printText + " ", text_x, text_y);
		} else {
			for (int word = 0; word < vText.size(); word++) {
				if (lineNo > 2) {
					break;
				}

				String tempString = (String) vText.elementAt(word);
				int wordWidth = GuiConst.FONT_TABLE
						.getAdvance(tempString + " ");
				if ((text_x + wordWidth >= compareWidth)
						|| ((lineNo == 2) && (text_x + wordWidth >= ((compareWidth * 75) / 100)))) {
					if (lineNo == 2) {
						tempString = "...";
					} else {
						if (word != 0) {
							text_y += GuiConst.FONT_TABLE.getHeight() + padding;
							text_x = temp_x;
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
		}
		if (standalone) {
			calculateCellsInbetween(y, compareWidth);
		}
	}

	private void calculateCellsInbetween(int y, int compareWidth) {
		text_y = y + padding;
		temp_x += compareWidth + padding + header_separator.getWidth()
				+ padding;
		text_x = temp_x;
	}
	
	public void setupBackground(Graphics g, int index, int bg_y) {

		if (index == 0) {
			g.drawBitmap(0, bg_y, this.getPreferredWidth(), header_bg
					.getHeight(), header_bg, 0, 0);
		} else {
			if (index == this.getSelectedIndex() && listener.isListFieldFocus()) {
				g.drawBitmap(0, bg_y, this.getPreferredWidth(), odd_bg
						.getHeight(), odd_bg, 0, 0);
				bg_y += odd_bg.getHeight();
				g.drawBitmap(0, bg_y, this.getPreferredWidth(), border
						.getHeight(), border, 0, 0);
			} else {
				bg_y += even_bg.getHeight();
				g.drawBitmap(0, bg_y, this.getPreferredWidth(), border
						.getHeight(), border, 0, 0);
			}
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

	// public synchronized void saveChanges(DataCentre ni, int index) {
	// try {
	// // to save news as loaded/read
	// // System.out.println("saveeeededdd");
	// } catch (Exception e) {
	// //
	// System.out.println("aloy.CustomListField.exceptione.saveChanges(ni, index):"+e);
	// }
	// }

	public boolean navigationClick(int status, int time) {
		// System.out.println("aloy.CustomListField.navigationClick: got enter or not?");

		return false;
	}
}
