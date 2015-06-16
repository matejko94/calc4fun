package com.calculator.excpetions;

public class SyntaxErrorException extends Exception
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//=============================================================================
// [FUNC] Primary class constructor with no parameters.
public SyntaxErrorException()
{
	super();
}

//=============================================================================
// [FUNC] Constructor accepting exception message as parameter.
public SyntaxErrorException(String msg)
{
	super(msg);
}
}
