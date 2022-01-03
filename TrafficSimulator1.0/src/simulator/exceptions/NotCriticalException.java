package simulator.exceptions;

@SuppressWarnings("serial")
public class NotCriticalException extends Exception {

	private String event;
	
	public NotCriticalException() {
		// TODO Auto-generated constructor stub
	}

	public NotCriticalException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public NotCriticalException(String message, String event) {
		super(message);
		this.event = event; 
		// TODO Auto-generated constructor stub
	}

	public NotCriticalException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NotCriticalException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotCriticalException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public String getEvent() {
		return event;
	}
	
	

}
