/**
 * 
 */
package assistant;

import java.util.Date;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

import common.StringCommon;
import constant.MessageConst;
import googleAssistant.apiClient.AssistantClient;
import googleAssistant.authentication.AuthenticationHelper;
import googleAssistant.config.AssistantConf;
import googleAssistant.config.AuthenticationConf;
import googleAssistant.config.DeviceRegisterConf;
import googleAssistant.config.IoConf;
import googleAssistant.device.DeviceRegister;
import googleAssistant.exception.AudioException;
import googleAssistant.exception.AuthenticationException;
import googleAssistant.exception.ConverseException;
import googleAssistant.exception.DeviceRegisterException;

/**
 * @author nguyenhuytan
 *
 */
public class GoogleAssistantClient {

	private static AuthenticationHelper authenticationHelper = null;
	private static AssistantClient assistantClient = null;

	public static void init() throws AuthenticationException, DeviceRegisterException {
		Config root = ConfigFactory.load();
		AuthenticationConf authenticationConf = ConfigBeanFactory.create(root.getConfig("authentication"),
				AuthenticationConf.class);
		DeviceRegisterConf deviceRegisterConf = ConfigBeanFactory.create(root.getConfig("deviceRegister"),
				DeviceRegisterConf.class);
		AssistantConf assistantConf = ConfigBeanFactory.create(root.getConfig("assistant"), AssistantConf.class);
		IoConf ioConf = ConfigBeanFactory.create(root.getConfig("io"), IoConf.class);

		// Authentication
		authenticationHelper = new AuthenticationHelper(authenticationConf);
		authenticationHelper.authenticate()
				.orElseThrow(() -> new AuthenticationException("Error during authentication"));

		// Check if we need to refresh the access token to request the api
		if (authenticationHelper.expired()) {
			authenticationHelper.refreshAccessToken()
					.orElseThrow(() -> new AuthenticationException("Error refreshing access token"));
		}

		// Register Device model and device
		DeviceRegister deviceRegister = new DeviceRegister(deviceRegisterConf,
				authenticationHelper.getOAuthCredentials().getAccessToken());
		deviceRegister.register();

		// Build the client (stub)
		assistantClient = new AssistantClient(authenticationHelper.getOAuthCredentials(), assistantConf,
				deviceRegister.getDeviceModel(), deviceRegister.getDevice(), ioConf);
	}

	public static String executeGoogleAssistant(String requestString, boolean isValidate)
			throws AuthenticationException, AudioException, ConverseException, DeviceRegisterException {
		if (assistantClient == null) {
			init();
		}
		if (assistantClient == null) {
			return MessageConst.GA_ERROR;
		}
		// Check if we need to refresh the access token to request the api
		if (authenticationHelper.expired()) {
			authenticationHelper.refreshAccessToken()
					.orElseThrow(() -> new AuthenticationException("Error refreshing access token"));

			// Update the token for the assistant client
			assistantClient.updateCredentials(authenticationHelper.getOAuthCredentials());
		}

		String result = "";
		System.out.println(new Date() + ": Google request: " + requestString);

		if (isValidate && !StringCommon.checkEnAlphaSymbol(requestString)) {
			result = MessageConst.GA_ERROR;
		} else {
			// Get input (text or voice)
			byte[] request = requestString.getBytes();

			// requesting assistant with text query
			assistantClient.requestAssistant(request);

			// process result
			System.out.println(new Date() + ": Google response: " + assistantClient.getTextResponse());
			result = StringCommon.trimSpace(assistantClient.getTextResponse()).replace("Google Assistant",
					"X Assistant");
			if (StringCommon.isNull(result)) {
				result = MessageConst.GA_ERROR;
			}
		}
		return result;
	}

}
