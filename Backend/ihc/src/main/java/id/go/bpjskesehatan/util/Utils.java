/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.go.bpjskesehatan.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Bambang Purwanto
 */
public class Utils {

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
			// TODO Auto-generated catch block
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
	
	public static java.sql.Date StringDateToSqlDate(String sdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date parsed = null;
		try {
			parsed = format.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        return sql;
	}
	
	public static String SqlDateToSqlString(java.sql.Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date testDate = null;
      try {
          testDate = sdf.parse(tgl);
      } catch (ParseException e) {
          return testDate;
      }
      return testDate;
  }
}
