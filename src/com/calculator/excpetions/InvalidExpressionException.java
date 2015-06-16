package com.calculator.excpetions;

public class InvalidExpressionException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3157576367852769255L;
	String message;
	
	public InvalidExpressionException(String _message)
	{
		message = _message;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	

}
