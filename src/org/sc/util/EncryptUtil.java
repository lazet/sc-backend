package org.sc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {
	
	/**
     * Convert a object into a Base64 string (as used in mime formats)
     */
	public static String objectToBase64String(Object object) {
		byte[] bytes = null;
		ObjectOutputStream output = null;
		try {
			ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
			output = new ObjectOutputStream(arrayOutput);
			output.writeObject(object);
			output.flush();
			bytes = arrayOutput.toByteArray();
			output.close();
		} catch (IOException e) {
			try {
				output.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		return toBase64(bytes);
	}
	
	/**
     * Convert a Base64 string into a object.
     */
	public static Object base64StringToObject(String str) {
		Object object = null;
		ObjectInputStream input = null;
		try {
			byte[] baseByte = Base64.decodeBase64(str.getBytes());
			ByteArrayInputStream arrayInput = new ByteArrayInputStream(baseByte); 
			
			input = new ObjectInputStream(arrayInput);
			object = (Object) input.readObject();
			input.close();
		} catch (Exception e) {
			try {
				input.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			object = null;
		}
		return object;
	}
	
	/**
     * Convert a byte array into a Base64 string (as used in mime formats)
     */
    public static String toBase64(byte[] aValue) {
        
        final String m_strBase64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        
        int byte1;
        int byte2;
        int byte3;
        int iByteLen = aValue.length;
        StringBuffer tt = new StringBuffer();
        
        for (int i = 0; i < iByteLen; i += 3) {
            boolean bByte2 = (i + 1) < iByteLen;
            boolean bByte3 = (i + 2) < iByteLen;
            byte1 = aValue[i] & 0xFF;
            byte2 = (bByte2) ? (aValue[i + 1] & 0xFF) : 0;
            byte3 = (bByte3) ? (aValue[i + 2] & 0xFF) : 0;
            
            tt.append(m_strBase64Chars.charAt(byte1 / 4));
            tt.append(m_strBase64Chars.charAt((byte2 / 16) + ((byte1 & 0x3) * 16)));
            tt.append(((bByte2) ? m_strBase64Chars.charAt((byte3 / 64) + ((byte2 & 0xF) * 4)) : '='));
            tt.append(((bByte3) ? m_strBase64Chars.charAt(byte3 & 0x3F) : '='));
        }
        return tt.toString();
    }
    
    /**
	 * Encode a string using specified algorithm and return the
	 * resulting encrypted string. If exception, the plain credentials string
	 * is returned
	 * 
	 * @return encypted string based on the algorithm.
	 */
    public static String encode(String id, String algorithm) {
    	try{
	        byte[] unencodedPassword = id.getBytes("utf8");
	        
	        MessageDigest md = null;
	        
	        try {
	            // first create an instance, given the provider
	            md = MessageDigest.getInstance(algorithm);
	        } catch (Exception e) {
	            return id;
	        }
	        
	        md.reset();
	        
	        // call the update method one or more times
	        // (useful when you don't know the size of your data, eg. stream)
	        md.update(unencodedPassword);
	        
	        // now calculate the hash
	        byte[] encodedPassword = md.digest();
	        
	        StringBuffer buf = new StringBuffer();
	        
	        for (int i = 0; i < encodedPassword.length; i++) {
	            if ((encodedPassword[i] & 0xff) < 0x10) {
	                buf.append("0");
	            }
	            
	            buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
	        }
	        
	        return buf.toString();
	    	}
    	catch(Exception e){
    		return null;
    	}
    }
    public static String encodeWithMD5(String text) {
    	return encode(text,MD5);
    }
    public final static String MD5 = "MD5";
}
