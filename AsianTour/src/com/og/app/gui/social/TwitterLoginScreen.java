package com.og.app.gui.social;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;

class TwitterLoginScreen extends MainScreen implements FieldChangeListener { 
    BasicEditField musername; 
    BasicEditField mPassword; 
    BasicEditField mStatus; 
    ButtonField mUpdateStatus; 
 
    public TwitterLoginScreen(String message) { 
        add(musername = new BasicEditField("username: ", "")); 
        add(mPassword = new BasicEditField("password: ", "")); 
        add(mStatus = new BasicEditField("status: ", message)); 
        mUpdateStatus = new ButtonField(ButtonField.CONSUME_CLICK); 
        mUpdateStatus.setLabel("update status"); 
        mUpdateStatus.setChangeListener(this); 
        add(mUpdateStatus); 
    } 
 
    public void fieldChanged(Field field, int context) { 
        if (mUpdateStatus == field) { 
            String username = musername.getText().trim(); 
            String password = mPassword.getText().trim(); 
            String status = mStatus.getText().trim(); 
            updateStatus(username, password, status); 
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
        } catch (Exception ex) { 
            System.out.println(ex.getMessage()+" "+response); 
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
} 
