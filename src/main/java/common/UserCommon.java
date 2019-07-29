/**
 * 
 */
package common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import constant.ActionEnum;
import constant.BotConst;

/**
 * @author nguyenhuytan
 *
 */
public class UserCommon {
	private static Properties userApiTokenProperties = null;

	public static void init(String fileName) {
		if (userApiTokenProperties == null) {
			initProperties(fileName);
		}
	}

	public static String checkUserRole(String cwUserId, String actionId) throws UnsupportedEncodingException {
		String rmUserToken = "";
		if (ActionEnum.getActionList().containsKey(actionId)) {
			ActionEnum actionRole = ActionEnum.getActionList().get(actionId);
			if (BotConst.ROLE_PUBLIC.equals(actionRole.getRoleId())) {
				rmUserToken = BotConst.HALF_MINUS;
			} else {
				if (userApiTokenProperties != null) {
					rmUserToken = userApiTokenProperties.getProperty(cwUserId);
					if (!StringCommon.isNull(rmUserToken)) {
						rmUserToken = StringCommon.decodeString(rmUserToken);
					}
				}
			}
		}
		System.out.println("User token: " + rmUserToken);
		return rmUserToken;
	}

	private static void initProperties(String propertyFile) {
		System.out.println("properties file：" + propertyFile);
		// プロパティファイルから設定値を取得
		try {
			// プロパティファイルオープン
			userApiTokenProperties = new Properties();
			// プロパティファイルロード
			FileInputStream fs = new FileInputStream(propertyFile);
			userApiTokenProperties.load(fs);
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
