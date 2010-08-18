package com.og.app.gui.schedule;

import java.util.Vector;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

import com.og.app.gui.MenuScreen;
import com.og.app.gui.TableDetailsScreen;
import com.og.app.gui.listener.ListFieldListener;
import com.og.app.util.DataCentre;

public class DataListField extends CustomListField {

	private DataPanel dataPanel = null;

	public DataListField(DataPanel dataPanel, ListFieldListener listener,
			int tableNo) {
		super(listener, tableNo);
		this.dataPanel = dataPanel;
	}

	protected synchronized boolean navigationMovement(int dx, int dy,
			int status, int time) {
		
		if (dx > 0) {
			//right
			if ((tableNo == 1) || (tableNo == 2)) {
				MenuScreen.getInstance().showTourScheduleTab();
			} else if (tableNo == 3) {
				MenuScreen.getInstance().showOOMTab();
			}
		} else if (dx < 0) {
			//left
			if ((tableNo == 1) || (tableNo == 2)) {
				MenuScreen.getInstance().showLiveScoreTab();
			} else if (tableNo == 3) {
				MenuScreen.getInstance().showTVScheduleTab();
			}
		}
		
		return false;
	}
	
	public boolean navigationClick(int status, int time) {
		/*
		 * Table No: 1 - TV Schedule 2 - Tour Schedule 3 - Live Score 4 - Order
		 * of Merit
		 */
		if (tableNo == 1) {
			//reload to TV Sched
			try {
				synchronized (Application.getEventLock()) {
					DataCentre item = (DataCentre) MenuScreen.getInstance().countryCollection
					.elementAt(getSelectedIndex()-1);
					selected_country = item.country;
					MenuScreen.getInstance().repainteverything();
					MenuScreen.getInstance().initSchedulePkg(2);
					//MenuScreen.getInstance().add(field)
					MenuScreen.getInstance().addPanels();
				}
			} catch (Exception e) {
				
			}
		} else if (tableNo == 2) {
			System.out.println("Selected row number " + getSelectedIndex()
					+ "of TV Schedule table");
			// here change V
			if (MenuScreen.getInstance().tvTimesCollection.size() > 0) {
				try {
					synchronized (Application.getEventLock()) {
						// here change V
						DataCentre item = (DataCentre) MenuScreen.getInstance().tvTimesCollection
								.elementAt(getSelectedIndex()-1);
						Screen s = UiApplication.getUiApplication()
								.getActiveScreen();
						// here change V
						item.tvIndex = getSelectedIndex();
						UiApplication.getUiApplication().pushScreen(
								new TableDetailsScreen(tableNo, item));
					}
					return true;
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		} else if (tableNo == 3) {
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
								new TableDetailsScreen(tableNo, item));
					}
					return true;
				} catch (Exception e) {
					System.out.println(e);
				}
			}

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

				Vector country = MenuScreen.getInstance().countryCollection;
				for (int i = 0; i < country.size(); i++) {
					add((DataCentre) country.elementAt(i));
				}

			} else if (tableNo == 2) {

				Vector tvTimes = MenuScreen.getInstance().tvTimesCollection;
				for (int i = 0; i < tvTimes.size(); i++) {
					add((DataCentre) tvTimes.elementAt(i));
				}

			} else if (tableNo == 3) {

				Vector tourSchedule = MenuScreen.getInstance().tourScheduleCollection;
				for (int i = 0; i < tourSchedule.size(); i++) {
					add((DataCentre) tourSchedule.elementAt(i));
				}

			} 
		}
	}
}
