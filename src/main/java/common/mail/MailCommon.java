/**
 * 
 */
package common.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import common.BotProperty;
import common.StringCommon;
import common.mail.bean.MailBean;
import constant.BotConst;

/**
 * @author nguyenhuytan
 *
 */
public class MailCommon {

	/**
	 * Send mail
	 * 
	 * @param mailBean
	 * @return
	 */
	public static boolean sendMail(MailBean mailBean) {
		if (StringCommon.isNull(mailBean.getBody()) || StringCommon.isNull(mailBean.getToAddress())) {
			return false;
		}
		String toAddress = mailBean.getToAddress();
		String host = BotProperty.getProperty(BotProperty.MAIL_HOST);
		String fromAddress = BotProperty.getProperty(BotProperty.MAIL_FROM);

		// Get the session object
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);

		// compose the message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			if (toAddress.contains(",")) {
				String[] addLst = toAddress.split(",");
				if (addLst != null && addLst.length > 0) {
					for (String addStr : addLst) {
						message.addRecipient(Message.RecipientType.TO, new InternetAddress(addStr));
					}
				}
			} else {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
			}
			message.setSubject(mailBean.getSubject());

			String body = mailBean.getBody() + "----------" + BotConst.LINE_BREAK_MAIL + "Tự động nhắc nhở từ X-Assistant";
			message.setText(body);

			// Send message
			Transport.send(message);
			System.out.println("================");
			System.out.println("mail start");
			System.out.println("to:");
			System.out.println("from:");
			System.out.println("");
			System.out.println(body);
			System.out.println("");
			System.out.println("Mail sent successfully. To: " + mailBean.getToAddress());
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return true;
	}
}
