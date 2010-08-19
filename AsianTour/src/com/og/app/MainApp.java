package com.og.app;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.MDSPushInputStream;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.DataBuffer;

import com.og.app.gui.GuiConst;
import com.og.app.gui.MenuScreen;
import com.og.app.gui.SplashScreen;
import com.og.xml.XmlHelper;


public class MainApp extends UiApplication {

	private ListeningThread _listeningThread;

	public static void main (String [] args) {
		//HomeScreen.setRolloverIcon(Bitmap.getBitmapResource("res/ico_hover.png"));
		MainApp ms = new MainApp();
		ms.enterEventDispatcher();
	}

	public MainApp() {		
		//Listening Thread for Push
		_listeningThread = new ListeningThread();
		_listeningThread.start();
		if ( net.rim.device.api.system.Display.getWidth()>320 ){
			GuiConst.FONTSIZE_ARR = GuiConst.FONTSIZE_ARR2;
		}
		pushScreen(MenuScreen.getInstance());
		//        try {
		//			TwitterHelper.UpdateStatus("test 2 this better work");
		//		} catch (OAuthServiceProviderException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		SplashScreen s = new SplashScreen();
		pushScreen(s);
		enterEventDispatcher();
	}

	public class ListeningThread extends Thread{
		private boolean _stop = false;
		private StreamConnectionNotifier _notify;

		private synchronized void stop()
		{
			_stop = true;
			try 
			{
				// Close the connection so the thread will return.
				_notify.close(); 
			} 
			catch (IOException e) 
			{
				System.err.println(e.toString());
			} 
			catch (NullPointerException e) 
			{
				// The notify object likely failed to open, due to an IOException.
			}
		}
		public void run()
		{
			StreamConnection stream = null;
			InputStream input = null;
			MDSPushInputStream pushInputStream=null;

			while (!_stop)
			{
				try 
				{

					// Synchronize here so that we don't end up creating a connection
					//that is never closed.
					synchronized(this)  
					{
						// Open the connection once (or re-open after an
						//IOException), so we don't end up 
						// in a race condition, where a push is lost if it comes in
						//before the connection 
						// is open again. We open the url with a parameter that
						//indicates that we should 
						// always use MDS when attempting to connect.
						_notify = (StreamConnectionNotifier)Connector.open(Const.LISTEN_URL +
						";deviceside=false");
					}

					while (!_stop){
						// NOTE: the following will block until data is received.
						System.out.println("Listening for data");
						stream = _notify.acceptAndOpen();
						System.out.println("Data received!");
						try 
						{
							input = stream.openInputStream();
							pushInputStream= new
							MDSPushInputStream((HttpServerConnection)stream, input);
							// Extract the data from the input stream.

							DataBuffer db = new DataBuffer();
							byte[] data = new byte[Const.CHUNK_SIZE];
							int chunk = 0;

							while ( -1 != (chunk = input.read(data)) )
							{
								db.write(data, 0, chunk);
							}

							updateMessage(data);

							// This method is called to accept the push.
							pushInputStream.accept();
							input.close();
							stream.close();
							data = db.getArray();

						} 
						catch (IOException e1) 
						{
							// A problem occurred with the input stream , however,
							//the original 
							// StreamConnectionNotifier is still valid.
							System.err.println(e1.toString());
							if ( input != null ) 
							{
								try 
								{
									input.close();
								} 
								catch (IOException e2) 
								{
								}
							}

							if ( stream != null )
							{
								try 
								{
									stream.close();
								} 
								catch (IOException e2) 
								{
								}
							}
						}
					}

					_notify.close();
					_notify = null;   

				} 
				catch (IOException ioe)
				{
					// Likely the stream was closed. Catches the exception thrown by 
					// _notify.acceptAndOpen() when this program exits.

					if ( _notify != null ) 
					{
						try 
						{
							_notify.close();
							_notify = null;
						} 
						catch ( IOException e ) 
						{
						}
					}
				}
			}

		}
	}

	private void updateMessage(final byte[] data)
	{
		UiApplication.getApplication().invokeLater(new Runnable() 
		{
			public void run() 
			{
				//Do Something
				System.out.println("Update Message: " + new String(data));
				if((new String(data).indexOf(("news update!"))>=0)){
					System.out.println("Downloading News Update!");
					XmlHelper.downloadNews();
					//TODO CHANGE ICON
				}
			}
		});

	}

//	protected void onExit()
//	{
//		// Kill the listening thread.
//		_listeningThread.stop();
//
//		try 
//		{
//			_listeningThread.join();
//		} 
//		catch (InterruptedException e) 
//		{
//			System.err.println(e.toString());
//		}
//	}
}

