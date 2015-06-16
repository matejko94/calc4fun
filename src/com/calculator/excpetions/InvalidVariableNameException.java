package com.calculator.excpetions;


public class InvalidVariableNameException extends Throwable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4944091275744603344L;
	private String message = "";

    public InvalidVariableNameException(String string) {
        this.message = string;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

}
