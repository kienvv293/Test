/**
 * 
 */
package constant;

/**
 * @author nguyenhuytan
 *
 */
public interface BotConst {
	String ROLE_PUBLIC = "0";
	String ROLE_ADVANCED = "1";
	String ROLE_ADMIN = "2";

	String HALF_SPACE = " ";
	String HALF_MINUS = "-";
	String HALF_DASH = "_";
	String HALF_SLASH = "/";

	String LINE_BREAK_MAIL = "\r\n";
	String LINE_BREAK = "\n";
	String BLANK = "";
	String NO_SHARP = "#";

	String DEFAULT_ST_TIME = "1";
	String OF_ON = "on";

	String FILE_TOKEN = "UserRedmineApiToken.properties";
	String FILE_BOT_CONFIG = "BotChatConfig.properties";
	// tannh 2019/01/09 add start
	String USER_CONFIG = "RedmineUserInfo.properties";
	// tannh 2019/01/09 add end
	String FILE_ROOM = "ListRoom.txt";
	String FILE_DB_SQLITE = "botchat.db";

	// Chatwork API URL:
	// http://download.chatwork.com/ChatWork_API_Documentation.pdf
	String CW_HEADER_NAME = "X-ChatWorkToken";
	String CW_API_URL = "https://api.chatwork.com/v2";
}
