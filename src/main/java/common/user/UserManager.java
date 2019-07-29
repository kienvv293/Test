/**
 * 
 */
package common.user;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.google.gson.Gson;

import common.StringCommon;
import constant.BotConst;

/**
 * @author nguyenhuytan
 *
 */
public class UserManager {

	private static HashMap<String, UserInfoBean> userInfoMap = new HashMap<>();

	public static void initUserManager(String propertyFile) {
		System.out.println("redmine user properties file： " + propertyFile);
		try {
			Properties redmineUserProperties = new Properties();
			FileInputStream fs = new FileInputStream(propertyFile);
			redmineUserProperties.load(fs);
			fs.close();

			for (Object keyValObj : redmineUserProperties.keySet()) {
				String keyVal = String.valueOf(keyValObj);

				String jsonUserStr = redmineUserProperties.getProperty(keyVal);
				Gson g = new Gson();
				UserInfoBean userinfo = g.fromJson(jsonUserStr, UserInfoBean.class);
				// OK
				userinfo.setIsOk(true);
				// Username
				userinfo.setUserName(keyVal);
				userInfoMap.put(keyVal.toLowerCase(), userinfo);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static UserInfoBean getUserInfoByName(String username) {
		if (StringCommon.isNull(username)) {
			return new UserInfoBean();
		}
		String userCnv = username.toLowerCase().replace(BotConst.HALF_SPACE, BotConst.BLANK);
		UserInfoBean result = userInfoMap.get(userCnv);
		if (result == null) {
			result = new UserInfoBean();
		}
		return result;
	}

}
