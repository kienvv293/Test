package common;

/**
 * 
 * @author nguyenvandang
 *
 */
public class IntegerCommon {
	/**
	 * Parse String to Int value.
	 * 
	 * @param stringValue
	 *            data to parse
	 * @param defaultValue
	 *            default value to return if String data is null, empty or can
	 *            not parse
	 * @return value parsed
	 */
	public static Integer parseInt(String stringValue, Integer defaultValue) {
		Integer intValue;
		try {
			if (StringCommon.isNull(stringValue)) {
				intValue = defaultValue;
			} else {
				intValue = Integer.parseInt(stringValue);
			}
		} catch (Exception e) {
			intValue = defaultValue;
		}
		return intValue;
	}
}
