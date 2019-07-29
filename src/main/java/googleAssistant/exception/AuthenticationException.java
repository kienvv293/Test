package googleAssistant.exception;

public class AuthenticationException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 350710573509212871L;

	public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
