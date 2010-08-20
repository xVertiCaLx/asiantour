package com.og.app.gui.social;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.og.app.datastore.RecordStoreHelper;
import com.og.app.gui.GuiConst;
import com.og.app.gui.Lang;
import com.og.app.gui.component.LineField;
import com.og.app.gui.component.SpaceField;
import com.og.app.gui.component.TitleField;

public class TwitterLoginScreen extends MainScreen implements FieldChangeListener { 
    private BasicEditField musername; 
    private BasicEditField mPassword;
    private SpaceField spaceField = null;
	private ButtonField mUpdateStatus;
	private TitleField titleBar = null;
    
    private VerticalFieldManager mainFM = null;
	private HorizontalFieldManager bottomFM = new HorizontalFieldManager(
			Field.USE_ALL_WIDTH);
	private ArticlePanel vFM = null;
	
	private Bitmap imgUp = null;
	private Bitmap imgDown = null;
	
	//private int headLineHeight = 0;
	
	String message = null;
	
 
    public TwitterLoginScreen(String message) { 
    	super();
    	this.message = message;

        mUpdateStatus = new ButtonField(ButtonField.CONSUME_CLICK); 
        mUpdateStatus.setLabel(Lang.TWITTER_LOGIN_LABEL); 
        mUpdateStatus.setChangeListener(this); 
        mUpdateStatus.setFont(GuiConst.FONT_PLAIN);
    	musername = new BasicEditField(Lang.SOCIAL_NETWORK_USERNAME_LABEL, "");
		mPassword = new BasicEditField(Lang.SOCIAL_NETWORK_PASSWORD_LABEL, "");
    	
		musername.setFont(GuiConst.FONT_PLAIN);
		mPassword.setFont(GuiConst.FONT_PLAIN);
		
    	titleBar = new TitleField("Login to Twitter", Bitmap.getBitmapResource("res/icon_setting.png"));
		//headLineHeight = titleBar.getPreferredHeight();
		imgUp = Bitmap.getBitmapResource("res/up.png");
		imgDown = Bitmap.getBitmapResource("res/down.png");
		
		mainFM = new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Manager.VERTICAL_SCROLLBAR);
		vFM = new ArticlePanel(GuiConst.SCREENHEIGHT
				- titleBar.getPreferredHeight(), GuiConst.SCREENWIDTH - 20);
		spaceField = new SpaceField(10, GuiConst.SCREENHEIGHT
				- titleBar.getPreferredHeight()) {
			protected void paint(Graphics g) {
				if (vFM.getVerticalScroll() > 0) {
					g
							.drawBitmap(this.getPreferredWidth()
									- imgUp.getWidth(), 0, imgUp.getWidth(),
									imgUp.getHeight(), imgUp, 0, 0);
				}

				if (vFM.getVerticalScroll() + vFM.getPreferredHeight() < vFM
						.getVirtualHeight()) {
					g.drawBitmap(this.getPreferredWidth() - imgDown.getWidth(),
							GuiConst.SCREENHEIGHT
									- titleBar.getPreferredHeight()
									- imgDown.getHeight(), imgDown.getWidth(),
							imgDown.getHeight(), imgDown, 0, 0);
				}
			}
		};
		
		RichTextField instrHdrLbl = new RichTextField(Lang.TWITTER_INSTRUCTION_HEADER);
		instrHdrLbl.setFont(GuiConst.FONT_BOLD);
		
		RichTextField instrLbl = new RichTextField(Lang.TWITTER_LOGIN_INSTRUCTION);
		instrLbl.setFont(GuiConst.FONT_PLAIN);		
		
		vFM.add(new LineField(3));
		vFM.add(instrHdrLbl);
		vFM.add(new LineField(3));
		vFM.add(new LineField(3,1));
		vFM.add(new LineField(3));
		vFM.add(instrLbl);
		vFM.add(new LineField(10));
		vFM.add(new LineField(3,1));
		vFM.add(musername);
		vFM.add(mPassword);
		vFM.add(mUpdateStatus);
		
		bottomFM.add(new SpaceField(10, vFM.getPreferredHeight()));
		bottomFM.add(vFM);
		bottomFM.add(spaceField);
		
		mainFM.add(titleBar);
		mainFM.add(bottomFM);
		
		add(mainFM);
    	
    	
    	
    } 
 
    public void fieldChanged(Field field, int context) { 
        if (mUpdateStatus == field) { 
            String username = musername.getText().trim(); 
            String password = mPassword.getText().trim();
            updateStatus(username, password, message); 
        } else { 
 
        } 
    } 
 
    void updateStatus(String username, String password, String status) { 
        String response = ""; 
        try { 
            String query = "status=" + urlEncode(status); 
            String len = String.valueOf(query.length()); 
            SocketConnection hc = (SocketConnection) Connector 
                    .open("socket://twitter.com:80"); 
            DataOutputStream dout =  
                new DataOutputStream(hc.openOutputStream()); 
            DataInputStream din = new DataInputStream(hc.openInputStream()); 
            String userPass = username + ":" + password; 
            byte[] encoded = Base64OutputStream.encode(userPass.getBytes(), 0, 
                    userPass.length(), false, false); 
            ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
            String request = "POST /statuses/update.json HTTP/1.1\r\n" 
                    + "Host: twitter.com:80\r\n" 
                    + "User-Agent: curl/7.18.0 (i486-pc-linux-gnu) " + 
                            "libcurl/7.18.0 OpenSSL/0.9.8g zlib/1.2.3.3 " + 
                            "libidn/1.1\r\n" 
                    + "Accept: */*\r\n" 
                    + "Content-Type: application/x-www-form-urlencoded\r\n" 
                    + "Content-Length: " + len + "\r\nAuthorization: Basic " 
                    + new String(encoded) + "\r\n\r\n"; 
            bos.write(request.getBytes()); 
            bos.write(query.getBytes()); 
            dout.write(bos.toByteArray()); 
            dout.flush(); 
            dout.close(); 
            byte[] bs = new byte[900]; 
            din.readFully(bs); 
            bos = new ByteArrayOutputStream(); 
            bos.write(bs); 
            din.close(); 
            hc.close(); 
            response = bos.toString();
            if (Dialog.ask(Dialog.D_OK, Lang.TWITTER_SUCCESS) == Dialog.D_OK) {
    			UiApplication.getUiApplication().popScreen(this);
    		}
        } catch (Exception ex) { 
            System.out.println(ex.getMessage()+" "+response); 
            Dialog.alert(Lang.TWITTER_ERROR + ex.getMessage());
        } 
    } 
 
    public static String urlEncode(String s) { 
        if (s != null) { 
            try { 
                s = new String(s.getBytes("UTF-8"), "ISO-8859-1"); 
            } catch (UnsupportedEncodingException e) { 
            } 
            StringBuffer tmp = new StringBuffer(); 
            try { 
                for (int i = 0; i < s.length(); i++) { 
                    int b = (int) s.charAt(i); 
                    if ((b >= 0x30 && b <= 0x39) || (b >= 0x41 && b <= 0x5A) 
                            || (b >= 0x61 && b <= 0x7A)) { 
                        tmp.append((char) b); 
                    } else if (b == 0x20) { 
                        tmp.append("+"); 
                    } else { 
                        tmp.append("%"); 
                        if (b <= 0xf) { 
                            tmp.append("0"); 
                        } 
                        tmp.append(Integer.toHexString(b)); 
                    } 
                } 
            } catch (Exception e) { 
            } 
            return tmp.toString(); 
        } 
        return null; 
    } 
    
    class ArticlePanel extends VerticalFieldManager {
		int fixheight = 0;
		int fixwidth = 0;

		public ArticlePanel(int fixheight, int fixwidth) {
			super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
			this.fixheight = fixheight;
			this.fixwidth = fixwidth;
		}

		public int getPreferredWidth() {
			return fixwidth;
		}

		public int getPreferredHeight() {
			return fixheight;
		}
		
		public boolean navigationMovement(int dx, int dy, int status, int time) {
			return false;
		}

		protected void sublayout(int width, int height) {
			super.sublayout(fixwidth, fixheight);
			setExtent(fixwidth, fixheight);
		}
	}
} 
