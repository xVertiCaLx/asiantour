package com.og.app.datastore;

import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import com.og.rss.ANewsItemObj;

public class RecordStoreHelper {
	public static final long DATASTORE_KEY = 0x8d0ed7fadc56615cL;
	
	private static PersistentObject getRecordStore(){
		return PersistentStore.getPersistentObject(DATASTORE_KEY);
	}
	
	//Vector<ANewsItemObj>
	public static Vector getNewsCollection(){
		PersistentObject recordStore = getRecordStore();
		Vector newsCollection = (Vector)recordStore.getContents();
		if(newsCollection == null){
			newsCollection =  new Vector();
			recordStore.setContents(newsCollection);
			recordStore.commit();
		}
		return newsCollection;
	}
}
