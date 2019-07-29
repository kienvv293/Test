/**
 * 
 */
package common;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import constant.BotConst;

/**
 * @author nguyenhuytan
 *
 */
public class StringCommon {

	public static boolean isNull(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}

	public static String trimSpace(String str) {
		if (isNull(str)) {
			return "";
		}
		return str.trim();
	}

	public static String formatDate(String date) {
		if (isNull(date)) {
			return "";
		}
		date = trimSpace(date);
		date = date.replace(BotConst.HALF_SLASH, BotConst.HALF_MINUS);
		String[] dateArr = date.split(BotConst.HALF_MINUS);
		if (dateArr.length != 3) {
			return "";
		}
		if (dateArr[0].length() != 4) {
			return "";
		}

		StringBuilder dateBuilder = new StringBuilder();
		dateBuilder.append(dateArr[0]);
		dateBuilder.append(BotConst.HALF_MINUS);
		dateBuilder.append(addZero(dateArr[1], 2));
		dateBuilder.append(BotConst.HALF_MINUS);
		dateBuilder.append(addZero(dateArr[2], 2));
		return dateBuilder.toString();
	}

	public static String encodeString(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("UTF-8");
		String encodeString = Base64.encodeBase64String(bytes);
		return encodeString;
	}

	public static String decodeString(String encodeText) throws UnsupportedEncodingException {
		byte[] decodeBytes = Base64.decodeBase64(encodeText);
		String str = new String(decodeBytes, "UTF-8");
		return str;
	}

	private static String addZero(String str, int length) {
		for (int i = 0; i < length; i++) {
			if (str.length() < length) {
				str = "0" + str;
			}
		}
		return str;
	}

	public static boolean checkEnAlphaSymbol(String str) {
		if (isNull(str)) {
			return false;
		}
		char[] chArray = str.toCharArray();
		for (int count = 0; count < chArray.length; count++) {
			if (!checkEnAlphabet(chArray[count]) && !checkSymbol(chArray[count])) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkEnAlphabet(char ch) {
		return (ch >= 0x0041 && ch <= 0x005a) || (ch >= 0x0061 && ch <= 0x007a);
	}

	public static boolean checkSymbol(char ch) {
		return (ch >= 0x0020 && ch <= 0x002f) || (ch >= 0x003a && ch <= 0x0040) || (ch >= 0x005b && ch <= 0x0060)
				|| (ch >= 0x007b && ch <= 0x007e);
	}
	
	/**
	 * Convert integer value to String value
	 * @param intValue
	 * @param defaultValue value return when data input null
	 * @return data converted
	 */
	public static String valueOf(Integer intValue, String defaultValue) {
		if (intValue == null) {
			return defaultValue;
		} else {
			return String.valueOf(intValue);
		}
	}
	// tannh 2019/01/09 add start
	/**
	 * 
	 * @param processDate
	 * @return
	 */
	public static String getNextDate(Date processDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(processDate);
		cal.add(Calendar.DATE, 1);

		int dayOfW = cal.get(Calendar.DAY_OF_WEEK);
		while (!(dayOfW >= 2 && dayOfW <= 6)) {
			cal.add(Calendar.DATE, 1);
			dayOfW = cal.get(Calendar.DAY_OF_WEEK);
		}

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(cal.getTime());
	}
	// tannh 2019/01/09 add end
}
