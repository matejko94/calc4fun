package com.calculator.mycalculator;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;

import com.calculator.excpetions.InvalidEntryException;
import com.calculator.excpetions.SyntaxErrorException;
import com.calculator.excpetions.UnknownOperatorException;
import com.calculator.grap.GraphingTab;
import com.example.mycalc.Item;

public class CalculatorAdapter {
	

	public static Expression[] expr = null;
	private String memValue = null;
	private String prmScreenText = null;
	private boolean clearPrmScreen = false;
	private boolean groupDigits = false;
	private boolean hasError = false;
	private KeypadButton numMode = null;
	private KeypadButton lastKey = null;
	public static ArrayList<Point> point;
	public static boolean isXInput;
	public static int width;
	public static int height;
	public static int bro, brc;
	private Context context;


	public CalculatorAdapter(Context context) {
		expr = new Expression[2];
		for (int i = 0; i < expr.length; i++)
			expr[i] = new Expression(context);
		memValue = "0";
		isXInput = false;
		initFields();
		this.context = context;
	
	}


	private void initFields() {
		clear();
		point = new ArrayList<>();
		bro = 0;
		brc = 0;
		prmScreenText = "0";
		clearPrmScreen = true;
		isXInput = false;
		hasError = false;
		lastKey = null;
	}


	private boolean isNumber(KeypadButton val) {
		return val == KeypadButton.ZERO || val == KeypadButton.ONE
				|| val == KeypadButton.TWO || val == KeypadButton.THREE
				|| val == KeypadButton.FOUR || val == KeypadButton.FIVE
				|| val == KeypadButton.SIX || val == KeypadButton.SEVEN
				|| val == KeypadButton.EIGHT || val == KeypadButton.NINE
				|| val == KeypadButton.A || val == KeypadButton.B
				|| val == KeypadButton.C || val == KeypadButton.D
				|| val == KeypadButton.E || val == KeypadButton.F;
	}


	public boolean hasMemValue() {
		return memValue != "0";
	}


	public String getSecScreenText() {
		return expr[1] + (lastKey == KeypadButton.CALCULATE ? " =" : "");
	}


	public String getPrmScreenText() {
		if (!hasError) {
			if (groupDigits) {
				if (numMode == KeypadButton.BIN || numMode == KeypadButton.HEX)
					return groupText(prmScreenText, 4, " ");
				else if (numMode == KeypadButton.OCT)
					return groupText(prmScreenText, 3, " ");
				else
					return groupText(prmScreenText, 3, CalculatorGUI.digit_sep)/*.replace(oldChar, newChar)*/;
			}}
		return prmScreenText;
	}


	private String groupText(String str, int cnt, String sep) {
		String ret = "";
		int i = 0, a = 0, z = str.lastIndexOf(CalculatorGUI.decimal_sep);
		if (z <= 0)
			z = str.length();
		else
			ret = str.substring(z);
		if (str.length() > 0 && str.charAt(0) == '-')
			a++;

		for (i = z - cnt; i > a; i -= cnt)
			ret = sep + str.substring(i, i + cnt) + ret;

		return str.substring(0, i + cnt) + ret;
	}


	private void clear() {
		for (int i = 0; i < expr.length; i++)
			while (expr[i].hasItems())
				expr[i].pop();
	}


	private void push(Object obj) {
		expr[1].push(obj);
		if (obj instanceof String) {
			expr[0].push(dec2rad.rad2dec((String) obj, numMode));
		} else
			expr[0].push(obj);
	}


	private void pop() {
		Object o = expr[0].pop();
		if (o instanceof Byte) {
			Byte b = (Byte) o;
			if (b.equals(ByteValues.X)) {
				isXInput = false;
			} else if (b.equals(ByteValues.BRACKET_OPEN))
				bro = bro - 1;
			else if (b.equals(ByteValues.BRACKET_CLOSE))
				brc = brc - 1;

		}
		o = expr[1].pop();
		if (o instanceof Byte) {
			Byte b = (Byte) o;
			if (b.equals(ByteValues.X)) {
				isXInput = false;
			}

		}
	}

	private Object getLast() {
		return expr[0].getLast();

	}

	private Object getFirst() {
		return expr[0].getFirst();

	}


	private String stripZeros(String s) {
		if (s.indexOf(".") >= 0)
			while (s.length() > 1 && (s.endsWith("0") || s.endsWith(".")))
				s = s.substring(0, s.length() - 1);
		return s;
	}


	private void throwError(String msg) {
		prmScreenText = msg;
		hasError = true;

	}

	public void inputKey(KeypadButton key) {
		if (hasError) {
			if (key == KeypadButton.CLEAR || key == KeypadButton.GROUP_DIGIT) {
			} else if (key == KeypadButton.HEX || key == KeypadButton.DEC
					|| key == KeypadButton.OCT || key == KeypadButton.BIN)
				initFields();
			else { 
			}
		} else if (lastKey == KeypadButton.CALCULATE
				&& key != KeypadButton.BACKSPACE) {
			clear();
		}
		Byte b = null;
		Object o = null;
		;

		switch (key) {
		case GROUP_DIGIT:
			groupDigits = !groupDigits;
			if (hasError)
				return;
			break;

		case BIN:
		case OCT:
		case DEC:
		case HEX:
			prmScreenText = dec2rad.dec2rad(dec2rad.rad2dec(prmScreenText, numMode), key);
			numMode = key;
			clearPrmScreen = true;
			break;
		case MC:
			memValue = "0";
			clearPrmScreen = true;
			break;
		case MR:
			if (memValue != "0")
				prmScreenText = dec2rad.dec2rad(memValue, numMode);
			else {
				prmScreenText = "0";
				lastKey = KeypadButton.ZERO;
				return;
			}
			clearPrmScreen = true;
			break;
		case MS:
			memValue = dec2rad.rad2dec(prmScreenText, numMode);
			clearPrmScreen = true;
			break;
		case M_ADD:
			memValue = new BigDecimal(dec2rad.rad2dec(prmScreenText, numMode)).add(
					new BigDecimal(memValue)).toPlainString();
			clearPrmScreen = true;
			break;
		case M_REMOVE:
			memValue = new BigDecimal(dec2rad.rad2dec(memValue, numMode)).subtract(
					new BigDecimal(prmScreenText)).toPlainString();
			clearPrmScreen = true;
			break;
		case BACKSPACE:
			if (clearPrmScreen) {
				// beep(); praznoo
				break;
			} else if (prmScreenText.length() > 1)
				prmScreenText = prmScreenText.substring(0,
						prmScreenText.length() - 1);
			else if (prmScreenText != "0")
				prmScreenText = "0";
			else if (expr[0].hasItems()) {

				pop();
			}

			break;
		case CE:
			prmScreenText = "0";
			break;
		case CLEAR:
			this.initFields();
			break;

		case ZERO:
			if (clearPrmScreen) {
				prmScreenText = "0";
				clearPrmScreen = false;
			} else if (!prmScreenText.equals("0"))
				prmScreenText += key.getText();

			break;
		case ONE:
		case TWO:
		case THREE:
		case FOUR:
		case FIVE:
		case SIX:
		case SEVEN:
		case EIGHT:
		case NINE:
		case A:
		case B:
		case C:
		case D:
		case E:
		case F:

			if (clearPrmScreen || prmScreenText == KeypadButton.ZERO.getText()) {
				prmScreenText = (String) key.getText();
				clearPrmScreen = false;
			} else
				prmScreenText += key.getText();
			break;
		case DECIMAL_SEP:
			if (clearPrmScreen || prmScreenText == KeypadButton.ZERO.getText()) {
				prmScreenText = (String) KeypadButton.ZERO.getText()
						+ (String) KeypadButton.DECIMAL_SEP.getText();
				clearPrmScreen = false;
			} else if (prmScreenText.indexOf((String) KeypadButton.DECIMAL_SEP
					.getText()) < 0)
				prmScreenText += key.getText();

			break;
		case SIGN:
			if (!(prmScreenText.equals("0") || prmScreenText.equals("")))
				if (!prmScreenText.contains("-"))
					prmScreenText = "-" + prmScreenText;
				else
					prmScreenText = prmScreenText.substring(1);
			// push(ByteValues.NEG);
			clearPrmScreen = false;
			// najebaš da se jebeš

			break;

		case BRACKETOPEN:
		case BRACKETCLOSE:
		case SQRT_ROOT:
		case CUBE_ROOT:
		case RECIPROC:
		case SQUARED:
		case CUBING:
		case ROOT:
		case FACULTY:
		case SIN:
		case COS:
		case TAN:
		case LOG:
		case ASIN:
		case ACOS:
		case ATAN:
		case LN:
		case POWER:
		case MULTIPLY:
		case DIV:
		case MODULUS:
		case PLUS:

		case MINUS:
		case EULER:
		case PI:
		case X:
			if (prmScreenText != "0" || isNumber(lastKey))
				push(prmScreenText);
			if (key.equals(KeypadButton.BRACKETOPEN)) {
				bro = bro + 1;
				push(ByteValues.BRO);
			} else if (key.equals(KeypadButton.BRACKETCLOSE)) {
				brc = brc + 1;
				push(ByteValues.BRC);
			} else if (key.equals(KeypadButton.SQRT_ROOT)) {
				push(ByteValues.SRT);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.CUBE_ROOT)) {
				push(ByteValues.CRT);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.RECIPROC)) {
				push(ByteValues.REC);
				
				
			} else if (key.equals(KeypadButton.SQUARED))
				push(ByteValues.SQR);

			else if (key.equals(KeypadButton.CUBING))
				push(ByteValues.CUB);
			else if (key.equals(KeypadButton.ROOT)) {
				push(ByteValues.ROOT);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.FACULTY))
				push(ByteValues.FCT);
			else if (key.equals(KeypadButton.SIN)) {
				push(ByteValues.SIN);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.COS)) {
				push(ByteValues.COS);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;

			} else if (key.equals(KeypadButton.TAN)) {
				push(ByteValues.TAN);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;

			} else if (key.equals(KeypadButton.ASIN)) {
				push(ByteValues.ASIN);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.ACOS)) {
				push(ByteValues.ACOS);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.ATAN)) {
				push(ByteValues.ATAN);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.LOG)) {
				push(ByteValues.LOG);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.LN)) {
				push(ByteValues.NLG);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			}
			/*
			 * else if (key.equals(KeypadButton.SIGN)) {
			 * push(ByteValues.BRACKET_OPEN); bro=bro+1; push(ByteValues.NEG);
			 * // push(ByteValues.BRACKET_OPEN); // bro=bro+1; }
			 */
			else if (key.equals(KeypadButton.POWER)) {
				push(ByteValues.POW);
				push(ByteValues.BRACKET_OPEN);
				bro = bro + 1;
			} else if (key.equals(KeypadButton.MULTIPLY)) // 32
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(ByteValues.MULTIPLY))
						pop();
					if (b.equals(ByteValues.DIV))
						pop();
					if (b.equals(ByteValues.ADD))
						pop();
					if (b.equals(ByteValues.SUB))
						pop();
				}
				push(ByteValues.MUL);
			} else if (key.equals(KeypadButton.DIV)) // 33
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(ByteValues.MULTIPLY))
						pop();
					if (b.equals(ByteValues.DIV))
						pop();
					if (b.equals(ByteValues.ADD))
						pop();
					if (b.equals(ByteValues.SUB))
						pop();
				}
				push(ByteValues.DIV);
			} else if (key.equals(KeypadButton.MODULUS))
				push(ByteValues.MOD);
			else if (key.equals(KeypadButton.PLUS)) // 35
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(ByteValues.MULTIPLY))
						pop();
					if (b.equals(ByteValues.DIV))
						pop();
					if (b.equals(ByteValues.ADD))
						pop();
					if (b.equals(ByteValues.SUB))
						pop();
				}
				push(ByteValues.ADD);
			} else if (key.equals(KeypadButton.MINUS)) // 36
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(ByteValues.MULTIPLY))
						pop();
					if (b.equals(ByteValues.DIV))
						pop();
					if (b.equals(ByteValues.ADD))
						pop();
					if (b.equals(ByteValues.SUB))
						pop();
				}

				push(ByteValues.SUB);
			} else if (key.equals(KeypadButton.PI))
				push(ByteValues.PI);
			else if (key.equals(KeypadButton.EULER))
				push(ByteValues.E);
			else if (key.equals(KeypadButton.X)) {
				push(ByteValues.X);
				isXInput = true;
			}
			prmScreenText = "0";
			clearPrmScreen = false;
			break;
		case CALCULATE:
			if (prmScreenText != "0" || isNumber(lastKey) || !expr[0].hasItems())
				push(prmScreenText);

			// we check and try correct ByteValues
			if (bro > brc) {
				int size = bro - brc;
				for (int i = 0; i < size; i++) {
					push(ByteValues.BRACKET_CLOSE);

				}

			}
			bro = 0;
			brc = 0;

			if (!isXInput) {
				try {
					prmScreenText = stripZeros(expr[0].eval().toPlainString());

					CalculatorGUI.history.add(new Item(CalculatorGUI.history
							.size(), getSecScreenText() + "=", prmScreenText));
					if (CalculatorGUI.histSize == 50)
						if (CalculatorGUI.history.size() > CalculatorGUI.histSize) {
							CalculatorGUI.history.remove(0);
						}
					if (numMode == KeypadButton.BIN)
						prmScreenText = dec2rad.dec2rad(prmScreenText, KeypadButton.BIN);
					else if (numMode == KeypadButton.OCT)
						prmScreenText = dec2rad.dec2rad(prmScreenText, KeypadButton.OCT);
					else if (numMode == KeypadButton.HEX)
						prmScreenText = dec2rad.dec2rad(prmScreenText, KeypadButton.HEX);
					clearPrmScreen = true;
				} catch (SyntaxErrorException | InvalidEntryException
						| UnknownOperatorException e) {
					// TODO Auto-generated catch block
					throwError("Syntax Error: " + e.getMessage());
					Log.e(e.getMessage(), e.getMessage());
					e.printStackTrace();
				}
			} else {

				Intent intent = new Intent(context, GraphingTab.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				context.startActivity(intent);

			}
			break;


		default:
			break;
		}

		lastKey = key;
	}
}
