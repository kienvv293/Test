/**
 * 
 */
package common.mail.bean;

/**
 * @author nguyenhuytan
 *
 */
public class MailBean {
	private String toAddress;
	private boolean mustCc;
	private String subject;
	private String body;

	/**
	 * @return the toAddress
	 */
	public String getToAddress() {
		return toAddress;
	}

	/**
	 * @param toAddress
	 *            the toAddress to set
	 */
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	/**
	 * @return the mustCc
	 */
	public boolean isMustCc() {
		return mustCc;
	}

	/**
	 * @param mustCc
	 *            the mustCc to set
	 */
	public void setMustCc(boolean mustCc) {
		this.mustCc = mustCc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

}
