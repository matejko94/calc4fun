package com.calculator.mycalculator;

public enum KeypadButton {
	//memory function
	MC("MC",KeypadButtonCategory.MEMORYBUFFER)
	, MR("MR",KeypadButtonCategory.MEMORYBUFFER)
	, MS("MS",KeypadButtonCategory.MEMORYBUFFER)
	, M_ADD("M+",KeypadButtonCategory.MEMORYBUFFER)
	, M_REMOVE("M-",KeypadButtonCategory.MEMORYBUFFER)
	//numbers	
	, ZERO("0",KeypadButtonCategory.NUMBER)
	, ONE("1",KeypadButtonCategory.NUMBER)
	, TWO("2",KeypadButtonCategory.NUMBER)
	, THREE("3",KeypadButtonCategory.NUMBER)
	, FOUR("4",KeypadButtonCategory.NUMBER)
	, FIVE("5",KeypadButtonCategory.NUMBER)
	, SIX("6",KeypadButtonCategory.NUMBER)
	, SEVEN("7",KeypadButtonCategory.NUMBER)
	, EIGHT("8",KeypadButtonCategory.NUMBER)
	, NINE("9",KeypadButtonCategory.NUMBER)
	, A("A",KeypadButtonCategory.NUMBER)
	, B("B",KeypadButtonCategory.NUMBER)
	, C("C",KeypadButtonCategory.NUMBER)
	, D("D",KeypadButtonCategory.NUMBER)
	, E("E",KeypadButtonCategory.NUMBER)
	, F("F",KeypadButtonCategory.NUMBER)	
	, BRACKETOPEN("(",KeypadButtonCategory.OPERATOR)
	, BRACKETCLOSE(")",KeypadButtonCategory.OPERATOR)
	, BACKSPACE("<-",KeypadButtonCategory.OPERATOR)
	, CE("CE",KeypadButtonCategory.OPERATOR)
	, CLEAR("C",KeypadButtonCategory.OPERATOR)
	, PLUS(" + ",KeypadButtonCategory.OPERATOR)
	, MINUS(" - ",KeypadButtonCategory.OPERATOR)
	, MULTIPLY(" * ",KeypadButtonCategory.OPERATOR)
	, DIV(" / ",KeypadButtonCategory.OPERATOR)
	, RECIPROC("1/x",KeypadButtonCategory.OPERATOR)
	, DECIMAL_SEP(".",KeypadButtonCategory.OPERATOR)
	, SIGN("±",KeypadButtonCategory.OPERATOR)
	, SQRT_ROOT("\u221a",KeypadButtonCategory.OPERATOR)
	, CUBE_ROOT("\u00B3\u221a",KeypadButtonCategory.OPERATOR)
	,ROOT("\u207F\u221ay",KeypadButtonCategory.OPERATOR)
	, SQUARED("x\u00b2",KeypadButtonCategory.OPERATOR)
    , CUBING("x\u00B3",KeypadButtonCategory.OPERATOR)
    , FACULTY("n!",KeypadButtonCategory.OPERATOR)
    , POWER("y\u207F",KeypadButtonCategory.OPERATOR)
   // , PERCENT("%",KeypadButtonCategory.OPERATOR)
    , MODULUS("MOD",KeypadButtonCategory.OPERATOR)	
	, SIN("sin",KeypadButtonCategory.OPERATOR)
	, COS("cos",KeypadButtonCategory.OPERATOR)
	, TAN("tan",KeypadButtonCategory.OPERATOR)
	, ASIN("asin",KeypadButtonCategory.OPERATOR)
	, ACOS("acos",KeypadButtonCategory.OPERATOR)
	, ATAN("atan",KeypadButtonCategory.OPERATOR)	
	, LOG("log",KeypadButtonCategory.OPERATOR)
	, LN("ln",KeypadButtonCategory.OPERATOR)
	, EULER("e",KeypadButtonCategory.OPERATOR)
	, PI("π",KeypadButtonCategory.OPERATOR)
	, INTEGRAL("int",KeypadButtonCategory.OPERATOR)
	, CALCULATE("=",KeypadButtonCategory.RESULT)
	, DUMMY("",KeypadButtonCategory.DUMMY)
	, GROUP_DIGIT("Group digit",KeypadButtonCategory.OPERATOR)	
	, BIN("BIN",KeypadButtonCategory.OPERATOR)	
	, OCT("OCT",KeypadButtonCategory.OPERATOR)
	, DEC("DEC",KeypadButtonCategory.OPERATOR)
	, HEX("HEX",KeypadButtonCategory.OPERATOR)
	, X("X",KeypadButtonCategory.GRAPH)
	,RADIAN("RAD",KeypadButtonCategory.OPERATOR)
	,DEGRE("DEG",KeypadButtonCategory.OPERATOR),	
	 EDIT("Edit",KeypadButtonCategory.OPERATOR),
	 HELP("Help",KeypadButtonCategory.OPERATOR),
	 HISTORY("Hist",KeypadButtonCategory.OPERATOR),
	SETTINGS("SET",KeypadButtonCategory.OPERATOR),
	CONVERT("Con",KeypadButtonCategory.OPERATOR),
	;
	

	String mText; // Display Text
	KeypadButtonCategory mCategory;
	
	KeypadButton(String text,KeypadButtonCategory category) {
		mText = text;
		mCategory = category;
	}
	
	public void setText(String s) {
		 mText=s;
	}	

	public KeypadButtonCategory getmCategory() {
		return mCategory;
	}
	public String getText() {
		return mText;
	}
}
