package com.calculator.mycalculator;

import java.math.BigDecimal;

public class dec2rad {

	public static String dec2rad(String str, BigDecimal rad) {
		BigDecimal bigDecimal = new BigDecimal(str);
		String ret = "";
		int rem = 0;
		bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
		while (bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
			rem = bigDecimal.remainder(rad).intValue();
			if (rem >= 0 && rem <= 9)
				ret = (char) (rem + '0') + ret;
			else if (rem >= 10)
				ret = (char) (rem + 'A' - 10) + ret;
			bigDecimal = bigDecimal.divide(rad, 0, BigDecimal.ROUND_DOWN);
		}
		return ret == "" ? "0" : ret;
	}


	private static String rad2dec(String str, BigDecimal rad) {
		BigDecimal bd = new BigDecimal(0);
		int chr = 0;

		for (int i = str.length() - 1, p = 0; i >= 0; i--, p++) {
			chr = Character.toUpperCase(str.charAt(i));
			if (chr >= '0' && chr <= '9')
				bd = bd.add(rad.pow(p).multiply(new BigDecimal(chr - '0')));
			else if (chr >= 'A' && chr <= 'Z')
				bd = bd.add(rad.pow(p).multiply(new BigDecimal(chr - 'A' + 10)));
			else
				p--; // Ignore other characters as if they weren't there ;)
		}

		return bd.toPlainString();
	}


	public static String dec2rad(String str, KeypadButton mode) {
		if (mode == KeypadButton.BIN)
			return dec2rad(str, new BigDecimal(2));
		else if (mode == KeypadButton.OCT)
			return dec2rad(str, new BigDecimal(8));
		else if (mode == KeypadButton.HEX)
			return dec2rad(str, new BigDecimal(16));
		return str;
	}


	static String rad2dec(String str, KeypadButton mode) {
		if (mode == KeypadButton.BIN)
			return rad2dec(str, new BigDecimal(2));
		else if (mode == KeypadButton.OCT)
			return rad2dec(str, new BigDecimal(8));
		else if (mode == KeypadButton.HEX)
			return rad2dec(str, new BigDecimal(16));
		return str;
	}


}
