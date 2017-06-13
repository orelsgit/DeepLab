package entities;

public class Error extends GeneralMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String classError, functionError;
	private int numberError;
	
	
	
	public Error(String classError, String functionError, int numberError) {
		super();
		this.classError = classError;
		this.functionError = functionError;
		this.numberError = numberError;
	}


	public String getClassError() {
		return classError;
	}

	public void setClassError(String classError) {
		this.classError = classError;
	}

	public String getFunctionError() {
		return functionError;
	}

	public void setFunctionError(String functionError) {
		this.functionError = functionError;
	}

	public int getNumberError() {
		return numberError;
	}

	public void setNumberError(int numberError) {
		this.numberError = numberError;
	}
}
