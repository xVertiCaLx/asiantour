package com.og.app.datastore;

import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import com.og.rss.ANewsItemObj;

public class RecordStoreHelper {
	
	//Hash of "com.og.app.datastore_test3"0x9fe7c2eeb62519b6L;
	//hash of "com.og.aloy.test" = 
	public static final long DATASTORE_KEY = 0x1e726fd5eeb438b9L; //0x9fe7c2eeb62519b6L;//0x8d0ed7fadc56615cL;

	private static PersistentObject getRecordStore(){
//		try{
//		PersistentStore.destroyPersistentObject(0xe62c9c2775dc9fc5L);
//		PersistentStore.destroyPersistentObject(0x3a8759a83845f2fbL);
//		PersistentStore.destroyPersistentObject(0x50b5c5546870f428L);
//		}catch(Exception e){
//			
//		}
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
			aNewsItemObj.removeElementAt(aNewsItemObj.size()-1);
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
