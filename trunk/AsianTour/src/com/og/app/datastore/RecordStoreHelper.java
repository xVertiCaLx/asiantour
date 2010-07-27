package com.og.app.datastore;

import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import com.og.rss.ANewsItemObj;

public class RecordStoreHelper {
	
	//Hash of "com.og.app.datastore_test2"
	public static final long DATASTORE_KEY = 0xe62c9c2775dc9fc5L;//0x8d0ed7fadc56615cL;

	private static PersistentObject getRecordStore(){
		return PersistentStore.getPersistentObject(DATASTORE_KEY);
	}

	//returns Vector<ANewsItemObj>
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

	//Vector<ANewsItemObj>
	public static void setNewsCollection(Vector aNewsItemObj){
		//Ensures only latest 20 news is stored.
		while(aNewsItemObj.size() > 20){
			aNewsItemObj.removeElementAt(0);
		}
		PersistentObject recordStore = getRecordStore();
		recordStore.setContents(aNewsItemObj);
		recordStore.commit();
	}

	public static int getNewsCount(){
		return getNewsCollection().size();
	}

	public static boolean isNewsExist(String newsID){
		int newsCount = getNewsCount();
		if(newsCount==0){
			return false;
		}
		Vector newsCollection = getNewsCollection();
		for(int i=0 ;i< newsCount; i++){
			ANewsItemObj news = (ANewsItemObj)newsCollection.elementAt(i);
			if(newsID.equals(news.guid)){
				return true;
			}
		}
		return false;
	}


}