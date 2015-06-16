package com.calculator.excpetions;


public class UnsetVariableException extends Exception{

		/**
	 * 
	 */
	private static final long serialVersionUID = -738450447401979940L;
		String message;
		
		public UnsetVariableException(String _message)
		{
			message = _message;
		}
		
		public String getMessage()
		{
			return message;
		}
	

}
