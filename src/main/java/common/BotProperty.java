/**
 * 
 */
package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author nguyenhuytan
 *
 */
public class BotProperty {

	public static final String CW_API_TOKEN = "CW_API_TOKEN";
	public static final String BOT_ID = "BOT_ID";
	public static final String ROOM_ID_1 = "ROOM_ID_1";
	public static final String ROOM_ID_2 = "ROOM_ID_2";
	public static final String ROOM_ID_3 = "ROOM_ID_3";
	public static final String SIM_URL = "SIM_URL";
	public static final String BOT_DELAY_TIME = "BOT_DELAY_TIME";
	// tannh 2019/01/09 add start
	public static final String MAIL_HOST = "MAIL_HOST";
	public static final String MAIL_FROM = "MAIL_FROM";
	public static final String MAIL_DAILY_TO = "MAIL_DAILY_TO";
	// tannh 2019/01/09 add end
	// tannh 2019/01/09 add start
	public static final String MAIL_LEADER = "MAIL_LEADER";
	// tannh 2019/01/09 add end

	private static Properties botchatProperties = null;

	public static void init(String fileName) {
		if (botchatProperties == null) {
			initProperties(fileName);
		}
	}

	public static String getProperty(String property) {
		if (botchatProperties == null) {
			return "";
		}
		return botchatProperties.getProperty(property, "");
	}

	private static void initProperties(String propertyFile) {
		System.out.println("bot chat properties file： " + propertyFile);
		try {
			botchatProperties = new Properties();
			FileInputStream fs = new FileInputStream(propertyFile);
			botchatProperties.load(fs);
			fs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
