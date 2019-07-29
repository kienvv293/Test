/**
 * 
 */
package common.chatwork;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import common.BotProperty;
import constant.BotConst;

/**
 * @author nguyenhuytan
 *
 */
public class ChatworkCommon {
	private static String cw_api_token;
	private static HttpClient httpClient;

	public static void init() {
		cw_api_token = BotProperty.getProperty(BotProperty.CW_API_TOKEN);
		httpClient = new HttpClient();

		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setConnectionManagerTimeout(60 * 1000);
		httpClientParams.setSoTimeout(60 * 1000);
		httpClient.setParams(httpClientParams);
	}

	/**
	 * Send message to room.
	 *
	 * @param roomId
	 * @param message
	 * @throws IOException
	 */
	public static void sendMessage(String roomId, String message) {
		if (httpClient == null) {
			init();
		}

		PostMethod method = null;
		try {
			method = new PostMethod(BotConst.CW_API_URL.concat("/rooms/").concat(roomId).concat("/messages"));
			method.addRequestHeader("X-ChatWorkToken", cw_api_token);
			method.addRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			method.setParameter("body", message);

			int statusCode = httpClient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				throw new Exception("Response is not valid. Check your API Key or ChatWork API status. response_code = "
						+ statusCode + ", message =" + method.getResponseBodyAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

}
