package com.og.app.gui.component;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;

import com.og.app.gui.MenuScreen;
import com.og.app.util.Utility;
import com.og.app.util.WebDataCallback;
import com.og.xml.ANewsItemObj;

public class WebBitmapField extends BitmapField implements WebDataCallback
{
	private EncodedImage bitmap = null;
	private String guid = null;
	public WebBitmapField(String url, String newsItemGuid)
	{
		try
		{
			Utility.getWebData(url, this);
			System.out.println("Downloading image...");
			this.guid = newsItemGuid;
		}
		catch (Exception e) {e.printStackTrace();}
	}

	public Bitmap getBitmap()
	{
		if (bitmap == null) return null;
		return bitmap.getBitmap();
	}

	public void callback(final byte[] data)
	{
		if (data == null || data.length < 1){ 
			return;
		}

		try
		{
			System.out.println("Image downloaded");
			bitmap = EncodedImage.createEncodedImage(data, 0,
					data.length);
//			if(bitmap.getBitmap().getWidth() > getScreen().getWidth()){
//				int ht = bitmap.getBitmap().getWidth() / getScreen().getWidth() * bitmap.getBitmap().getHeight();
//				bitmap = Utility.resizeBitmap(bitmap.getBitmap(), getScreen().getWidth(), ht);
//			}
			for(int i=0; i<MenuScreen.getInstance().newsCollection.size(); i++){
				ANewsItemObj newsItem = (ANewsItemObj)MenuScreen.getInstance().newsCollection.elementAt(i);
				if(newsItem.guid.equals(guid)){
					newsItem.image = data;
					break;
				}
			}
			setImage(bitmap);
		}
		catch (final Exception e){e.printStackTrace();}
	}
}