package ExceptionHandlers;

public class MissedPropertyException extends Exception {

	private String parameterName;
	
	private static final long serialVersionUID = 1;

	public MissedPropertyException(String parameterName) {
		super("Configuration parameter needed: '" + parameterName + "'");
		this.parameterName = parameterName;
	}

	public String getParameterName() {
		return parameterName;
	}
}