/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.go.bpjskesehatan.inspired.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.go.bpjskesehatan.inspired.config.Config;
import id.go.bpjskesehatan.util.Base64;

//import id.go.bpjskesehatan.entitas.Headers;

public class Utils {
	
	public static String GetMaskingName(String nama) {
        String ret = "";
        String[] arrName = nama.split(" ");
        for (int n = 0; n < arrName.length; ++n) {
            if (arrName[n].length() > 2) {
                String mask = "";
                for (int m = 0; m < arrName[n].length() - 2; ++m) {
                    mask = mask + "*";
                }
                if (arrName[n].length() > 1) {
                    ret = ret + " " + arrName[n].substring(0, 2) + mask;
                } else {
                    ret = ret + " " + arrName[n] + mask;
                }
            } else {
                if (arrName[n].length() > 1) {
                    ret = ret + " " + arrName[n].substring(0, 2);
                } else {
                    ret = ret + " " + arrName[n];
                }
            }
        }
        return ret;
    }

	public static String generateHmacSHA256Signature(String data, String key) {
		byte[] hmacData = null;
		byte[] result;

		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(),
				"HmacSHA256");
		Mac mac;
		try {
			mac = Mac.getInstance("HmacSHA256");
			mac.init(secretKey);
			hmacData = mac.doFinal(data.getBytes("UTF-8"));
		} catch (IllegalStateException | UnsupportedEncodingException
				| NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
		}
		result = Base64.encode(hmacData);
		return new String(result);

	}

	public static String MD5(String word) {

		StringBuilder hexString = new StringBuilder();
		try {
			byte[] bytesOfMessage = word.getBytes("UTF-8");

			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				byte[] thedigest = md.digest(bytesOfMessage);
				for (int i = 0; i < thedigest.length; i++) {
					String hex = Integer.toHexString(0xff & thedigest[i]);
					if (hex.length() == 1) {
						hexString.append('0');
					}
					hexString.append(hex);
				}
				return hexString.toString();
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
			}

		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		else
			return hex;
	}
	
	public static int getAngkaTahun(Date date) {

      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      int year = cal.get(Calendar.YEAR);
      return year;
	}
	
	public static Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(getTime());
        } catch (ParseException e) {
            return testDate;

        }
        String strDate = convertDatetoString(testDate);
        return convertStringtoDate(strDate);
    }
	
	public static String getTime() {
        String ret = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        ret = sdf.format(date);
        return ret;
    }
	
	public static String convertDatetoString(Date date) {
        String reportDate = "01/01/1900";
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            reportDate = df.format(date);
        } catch (Exception e) {
            return null;
        }
        return reportDate;
    }
	
	public static Date convertStringtoDate(String tgl) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(tgl);
        } catch (ParseException e) {
            return testDate;
        }
        return testDate;
    }
	
	public static int checkTahunRegulasi(Date tgl, String tglolder) {
		int hasil = 0;
		
		String tglbaru = convertDatetoString(tgl);
		Date tglbarulagi = convertStringtoDate(tglbaru);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(tglolder);
        } catch (ParseException e) {
        }
		
		int hasilPerbandingan = tglbarulagi.compareTo(testDate);
		 
        if (hasilPerbandingan > 0) {
            hasil =  getAngkaTahun(tgl);
        } else if (hasilPerbandingan < 0) {
            hasil = 2014;
        } else if (hasilPerbandingan == 0) {
        	hasil =  getAngkaTahun(tgl);
        }
        return hasil;
    }
	
	public static String BPJSEncode(Object o) 
			throws JsonProcessingException, InvalidKeyException, 
			NoSuchPaddingException, NoSuchAlgorithmException, 
			InvalidAlgorithmParameterException, BadPaddingException, 
			IllegalBlockSizeException  {
		ObjectMapper responMapper = new ObjectMapper();
		String jsonString = responMapper.writeValueAsString(o);
		String lzString = LZString.compressToEncodedURIComponent(jsonString);
		
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddyyyyMMdd");  
	    String iv = formatter.format(date);
		
		SecretKeySpec _key = new SecretKeySpec(Config.STRKEY.getBytes(StandardCharsets.UTF_8), "AES");
	    IvParameterSpec _iv = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
		String encrypted = Enc.encrypt(lzString, _key, _iv);
		return encrypted;
	}
	
	public static String BPJSDecode(String str) 
			throws JsonProcessingException, InvalidKeyException, 
			NoSuchPaddingException, NoSuchAlgorithmException, 
			InvalidAlgorithmParameterException, BadPaddingException, 
			IllegalBlockSizeException  {
		
		Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddyyyyMMdd");  
	    String iv = formatter.format(date);
		
		SecretKeySpec _key = new SecretKeySpec(Config.STRKEY.getBytes(StandardCharsets.UTF_8), "AES");
	    IvParameterSpec _iv = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
		String decrypted = Enc.decrypt(str, _key, _iv);
		
		String lzString = LZString.decompressFromEncodedURIComponent(decrypted);
		return lzString;
	}
}
