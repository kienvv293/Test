/**
 * 
 */
package assistant;

import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONObject;

import common.BotProperty;
import common.StringCommon;

/**
 * @author nguyenhuytan
 *
 */
public class SimsimiExecute {

	public static String getMessage(String message) {
		String returnMsg = "";
		try {
			String simSimiUrl = BotProperty.getProperty(BotProperty.SIM_URL) + URLEncoder.encode(message, "UTF-8");
			System.out.println(simSimiUrl);
			String json = getSimiMsg(simSimiUrl);

			System.out.println("Simsimi Json: " + json);
			JSONObject jsonObject = new JSONObject(json);
			String messages = jsonObject.getString("messages");
			if (!StringCommon.isNull(messages)) {
				String textJson = messages.substring(1, messages.length() - 1);
				JSONObject jsonTextObject = new JSONObject(textJson);
				returnMsg = jsonTextObject.getString("text");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			returnMsg = "hở, hỏi em à ?";
		}
		return returnMsg;
	}

	private static String getSimiMsg(String path) {
		GetMethod method = null;
		HttpClient httpClient = new HttpClient();
		try {
			method = new GetMethod(path);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				return "hở, hỏi em à ?";
			}
			return method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
}
