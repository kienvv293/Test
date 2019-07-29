/**
 * 
 */
package common.user;

/**
 * @author nguyenhuytan
 *
 */
public class UserInfoBean {
	private String userName;
	private String email;
	private String chatworkId;
	private boolean isMale = false;
	private boolean mustcc = false;
	private boolean isOk = false;

	/**
	 * <PRE>
	 * userNameを設定する。<BR>
	 * </PRE>
	 * 
	 * @param userName
	 *            userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * <PRE>
	 * userNameを取得する。<BR>
	 * </PRE>
	 * 
	 * @return userName userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * <PRE>
	 * emailを設定する。<BR>
	 * </PRE>
	 * 
	 * @param email
	 *            email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * <PRE>
	 * emailを取得する。<BR>
	 * </PRE>
	 * 
	 * @return email email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * <PRE>
	 * chatworkIdを設定する。<BR>
	 * </PRE>
	 * 
	 * @param chatworkId
	 *            chatworkId
	 */
	public void setChatworkId(String chatworkId) {
		this.chatworkId = chatworkId;
	}

	/**
	 * <PRE>
	 * chatworkIdを取得する。<BR>
	 * </PRE>
	 * 
	 * @return chatworkId chatworkId
	 */
	public String getChatworkId() {
		return chatworkId;
	}

	/**
	 * <PRE>
	 * isMaleを設定する。<BR>
	 * </PRE>
	 * 
	 * @param isMale
	 *            isMale
	 */
	public void setIsMale(boolean isMale) {
		this.isMale = isMale;
	}

	/**
	 * <PRE>
	 * isMaleを取得する。<BR>
	 * </PRE>
	 * 
	 * @return isMale isMale
	 */
	public boolean getIsMale() {
		return isMale;
	}

	/**
	 * <PRE>
	 * mustccを設定する。<BR>
	 * </PRE>
	 * 
	 * @param mustcc
	 *            mustcc
	 */
	public void setMustcc(boolean mustcc) {
		this.mustcc = mustcc;
	}

	/**
	 * <PRE>
	 * mustccを取得する。<BR>
	 * </PRE>
	 * 
	 * @return mustcc mustcc
	 */
	public boolean getMustcc() {
		return mustcc;
	}

	/**
	 * <PRE>
	 * isOkを設定する。<BR>
	 * </PRE>
	 * 
	 * @param isOk
	 *            isOk
	 */
	public void setIsOk(boolean isOk) {
		this.isOk = isOk;
	}

	/**
	 * <PRE>
	 * isOkを取得する。<BR>
	 * </PRE>
	 * 
	 * @return isOk isOk
	 */
	public boolean getIsOk() {
		return isOk;
	}

}
