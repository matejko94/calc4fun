package com.calculator.mycalculator;

import java.util.HashMap;
import java.util.Map;

public class DefaulButtonsPosition {
	public Map<Integer, KeypadButton> onClicDefault = new HashMap<>();
	public Map<Integer, KeypadButton> onLongClickDefault = new HashMap<>();

	public Map<Integer, KeypadButton> onClickGenerateDefault() {

		onClicDefault.put(R.id.button, KeypadButton.MC);
		onClicDefault.put(R.id.button1, KeypadButton.MR);
		onClicDefault.put(R.id.button2, KeypadButton.M_ADD);
		onClicDefault.put(R.id.button3, KeypadButton.M_REMOVE);
		onClicDefault.put(R.id.button4, KeypadButton.BACKSPACE);
		onClicDefault.put(R.id.button5, KeypadButton.CLEAR);
		onClicDefault.put(R.id.button6, KeypadButton.SQRT_ROOT);
		onClicDefault.put(R.id.button7, KeypadButton.SQUARED);
		onClicDefault.put(R.id.button8, KeypadButton.POWER);
		onClicDefault.put(R.id.button9, KeypadButton.RECIPROC);
		onClicDefault.put(R.id.button10, KeypadButton.FACULTY);
		onClicDefault.put(R.id.button11, KeypadButton.DEC);
		onClicDefault.put(R.id.button12, KeypadButton.SIN);
		onClicDefault.put(R.id.button13, KeypadButton.COS);
		onClicDefault.put(R.id.button14, KeypadButton.TAN);
		onClicDefault.put(R.id.button15, KeypadButton.LOG);
		onClicDefault.put(R.id.button16, KeypadButton.LN);
		onClicDefault.put(R.id.button17, KeypadButton.MODULUS);
		onClicDefault.put(R.id.button18, KeypadButton.SEVEN);
		onClicDefault.put(R.id.button19, KeypadButton.EIGHT);
		onClicDefault.put(R.id.button20, KeypadButton.NINE);
		onClicDefault.put(R.id.button21, KeypadButton.BRACKETOPEN);
		onClicDefault.put(R.id.button22, KeypadButton.BRACKETCLOSE);
		onClicDefault.put(R.id.button23, KeypadButton.FOUR);
		onClicDefault.put(R.id.button24, KeypadButton.FIVE);
		onClicDefault.put(R.id.button25, KeypadButton.SIX);
		onClicDefault.put(R.id.button26, KeypadButton.MULTIPLY);
		onClicDefault.put(R.id.button27, KeypadButton.DIV);
		onClicDefault.put(R.id.button28, KeypadButton.ONE);
		onClicDefault.put(R.id.button29, KeypadButton.TWO);
		onClicDefault.put(R.id.button30, KeypadButton.THREE);
		onClicDefault.put(R.id.button31, KeypadButton.PLUS);
		onClicDefault.put(R.id.button32, KeypadButton.MINUS);
		onClicDefault.put(R.id.button33, KeypadButton.ZERO);
		onClicDefault.put(R.id.button34, KeypadButton.DECIMAL_SEP);
		onClicDefault.put(R.id.button35, KeypadButton.SIGN);
		onClicDefault.put(R.id.button36, KeypadButton.X);
		onClicDefault.put(R.id.button37, KeypadButton.CALCULATE);
		onClicDefault.put(R.id.button38, KeypadButton.SETTINGS);

		return onClicDefault;

	}
	
	public Map<Integer, KeypadButton> onLongClickGenerateDefault() {
		onLongClickDefault.put(R.id.button, KeypadButton.MS);
		onLongClickDefault.put(R.id.button1, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button2, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button3, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button4, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button5, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button6, KeypadButton.CUBE_ROOT);
		onLongClickDefault.put(R.id.button7, KeypadButton.CUBING);
		onLongClickDefault.put(R.id.button8, KeypadButton.ROOT);
		onLongClickDefault.put(R.id.button9, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button10, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button11, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button12, KeypadButton.ASIN);
		onLongClickDefault.put(R.id.button13, KeypadButton.ACOS);
		onLongClickDefault.put(R.id.button14, KeypadButton.ATAN);
		onLongClickDefault.put(R.id.button15, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button16, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button17, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button18, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button19, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button20, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button21, KeypadButton.A);
		onLongClickDefault.put(R.id.button22, KeypadButton.B);
		onLongClickDefault.put(R.id.button23, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button24, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button25, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button26, KeypadButton.C);
		onLongClickDefault.put(R.id.button27, KeypadButton.D);
		onLongClickDefault.put(R.id.button28, KeypadButton.PI);
		onLongClickDefault.put(R.id.button29, KeypadButton.EULER);
		onLongClickDefault.put(R.id.button30, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button31, KeypadButton.E);
		onLongClickDefault.put(R.id.button32, KeypadButton.F);
		onLongClickDefault.put(R.id.button33, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button34, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button35, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button36, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button37, KeypadButton.DUMMY);
		onLongClickDefault.put(R.id.button38, KeypadButton.CONVERT);

		
		return onLongClickDefault;
	}
}
