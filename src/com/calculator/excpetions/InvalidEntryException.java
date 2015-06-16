
package com.calculator.excpetions;

public class InvalidEntryException extends Exception
{
	
/**
 * @author senozetnik
 *  napaka za napaèen vnos 
	 * 
	 */
	private static final long serialVersionUID = 1L;


public InvalidEntryException()
{
	super();
}

//=============================================================================
// [FUNC] Constructor accepting exception message as parameter.
public InvalidEntryException(String msg)
{
	super(msg);
}
}
