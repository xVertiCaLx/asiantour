package com.og.app.gui;

import java.util.Vector;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

import com.og.app.gui.listener.ListFieldListener;
import com.og.app.util.DataCentre;

//
public class TableListField extends MeritTableListField {

	private TablePanel tablePanel = null;
	DataCentre[] item = new DataCentre[arraySize];

	public TableListField(TablePanel tablePanel, ListFieldListener listener,
			int tableNo, int page) {
		super(listener, tableNo, page);
		this.tablePanel = tablePanel;
	}

	protected synchronized boolean navigationMovement(int dx, int dy,
			int status, int time) {
		System.out.println("dx: " + dx + " dy: " + dy);

		// 2,2,3,1

		if ((table == 1) || (table == 2)) {
			if (dx > 0) {
				// move right
				System.out.println("right");

				if (page == 1) {
					page = 2;
				}
				System.out.println(page);

			} else if (dx < 0) {
				System.out.println("left");
				if (page == 2) {
					page = 1;
				}
				System.out.println(page);
				// move left
			}
		} else if (table == 3) {
			if (dx > 0) {
				// move right
				System.out.println("right");

				if (page == 1) {
					page = 2;
				} else if (page == 2) {
					page = 3;
				}
				System.out.println(page);

			} else if (dx < 0) {
				System.out.println("left");
				if (page == 3) {
					page = 2;
				} else if (page == 2) {
					page = 1;
				}
				System.out.println(page);
				// move left
			}
		}
		tablePanel.invalidate();
		return false;
	}

	public boolean navigationClick(int status, int time) {
		/*
		 * Table No: 1 - TV Schedule 2 - Tour Schedule 3 - Live Score 4 - Order
		 * of Merit
		 */
		if (table == 1) {
			System.out.println("Selected row number "
					+ (getSelectedIndex() - 1) + "of TV Schedule table");
			if (MenuScreen.getInstance().tvTimesCollection.size() > 0) {
				try {
					synchronized (Application.getEventLock()) {
						DataCentre item = (DataCentre) MenuScreen.getInstance().tvTimesCollection
								.elementAt(getSelectedIndex() - 1);
						Screen s = UiApplication.getUiApplication()
								.getActiveScreen();
						item.tvIndex = getSelectedIndex();
						UiApplication.getUiApplication().pushScreen(
								new TableDetailsScreen(table, item));
					}
					return true;
				} catch (Exception e) {
					System.out.println("this is an error:" + e);
				}
			}

		} else if (table == 2) {
			System.out.println("Selected row number " + getSelectedIndex()
					+ "of Tour Schedule table");
			// here change V
			if (MenuScreen.getInstance().tourScheduleCollection.size() > 0) {
				try {
					synchronized (Application.getEventLock()) {
						// here change V
						DataCentre item = (DataCentre) MenuScreen.getInstance().tourScheduleCollection
								.elementAt(getSelectedIndex());
						Screen s = UiApplication.getUiApplication()
								.getActiveScreen();
						// here change V
						item.tourIndex = getSelectedIndex();
						UiApplication.getUiApplication().pushScreen(
								new TableDetailsScreen(table, item));
					}
					return true;
				} catch (Exception e) {
					System.out.println(e);
				}
			}

		} else if (table == 3) {

		} else if (table == 4) {
			// do nothing.. unless got other stats
		}
		return false;
	}

	public void loadTableData(int tableNo) {
		// setRowHeight();
		synchronized (lock) {
			/*
			 * Table No: 1 - TV Schedule 2 - Tour Schedule 3 - Live Score 4 -
			 * Order of Merit
			 */
			if (tableNo == 1) {

				Vector tvTimes = MenuScreen.getInstance().tvTimesCollection;
				for (int i = 0; i < tvTimes.size(); i++) {
					add((DataCentre) tvTimes.elementAt(i));
				}

			} else if (tableNo == 2) {

				Vector tourSchedule = MenuScreen.getInstance().tourScheduleCollection;
				for (int i = 0; i < tourSchedule.size(); i++) {
					add((DataCentre) tourSchedule.elementAt(i));
				}

			} else if (tableNo == 4) {
				Vector merit = MenuScreen.getInstance().meritCollection;
				for (int i = 0; i < merit.size(); i++) {
					add((DataCentre) merit.elementAt(i));
				}
			}
		}
	}
}
