package com.calculator.excpetions;
public class UnknownOperatorException extends Exception
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//=============================================================================
// [FUNC] Primary class constructor with no parameters.
public UnknownOperatorException()
{
	super();
}

//=============================================================================
// [FUNC] Constructor accepting exception message as parameter.
public UnknownOperatorException(String msg)
{
	super(msg);
}
}
