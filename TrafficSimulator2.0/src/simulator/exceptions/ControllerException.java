package simulator.exceptions;

@SuppressWarnings("serial")
public class ControllerException extends Exception {

	public ControllerException() {
		super("No se pudo crear el controller, argumentos invalidos");
	}

	public ControllerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ControllerException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ControllerException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
