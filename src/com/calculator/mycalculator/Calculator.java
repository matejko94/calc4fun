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

public class Calculator {
	// =============================================================================

	// [DATA] Class instance data fields.
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

	// =============================================================================
	// [FUNC] Primary class constructor.
	public Calculator(Context context) {
		expr = new Expression[2];
		for (int i = 0; i < expr.length; i++)
			expr[i] = new Expression(context);
		// numMode = KeypadButton.DEC;
		memValue = "0";
		isXInput = false;
		initFields();
		this.context = context;

		bro = 0;
		brc = 0;
		point = new ArrayList<>();
	}

	// =============================================================================
	// [FUNC] Re-initializes various fields.
	private void initFields() {
		clear();
		bro = 0;
		brc = 0;
		prmScreenText = "0";
		clearPrmScreen = true;
		isXInput = false;
		hasError = false;
		lastKey = null;
	}

	// =============================================================================
	// [FUNC] Returns a boolean value indicating whether the parameter is a
	// digit.
	private boolean isDigit(KeypadButton val) {
		return val == KeypadButton.ZERO || val == KeypadButton.ONE
				|| val == KeypadButton.TWO || val == KeypadButton.THREE
				|| val == KeypadButton.FOUR || val == KeypadButton.FIVE
				|| val == KeypadButton.SIX || val == KeypadButton.SEVEN
				|| val == KeypadButton.EIGHT || val == KeypadButton.NINE
				|| val == KeypadButton.A || val == KeypadButton.B
				|| val == KeypadButton.C || val == KeypadButton.D
				|| val == KeypadButton.E || val == KeypadButton.F;
	}

	// =============================================================================
	// [FUNC] Returns a boolean value indicating whether this calculator has
	// a stored memory value.
	public boolean hasMemValue() {
		return memValue != "0";
	}

	// =============================================================================
	// [FUNC] Returns the text for the secondary screen.
	public String getSecScreenText() {
		return expr[1] + (lastKey == KeypadButton.CALCULATE ? " =" : "");
	}

	// =============================================================================
	// [FUNC] Returns the text for the primary screen.
	public String getPrmScreenText() {
		if (!hasError) {
			if (groupDigits) {
				if (numMode == KeypadButton.BIN || numMode == KeypadButton.HEX)
					return groupText(prmScreenText, 4, " ");
				else if (numMode == KeypadButton.OCT)
					return groupText(prmScreenText, 3, " ");
				else
					return groupText(prmScreenText, 3, ",");
			}
		}
		return prmScreenText;
	}

	// =============================================================================
	// [FUNC] Converts BigDecimal to a radix (No shit!)
	private static String dec2rad(String str, BigDecimal rad) {
		BigDecimal bd = new BigDecimal(str);
		String ret = "";
		int rem = 0;
		bd = bd.setScale(0, BigDecimal.ROUND_DOWN);
		while (bd.compareTo(BigDecimal.ZERO) > 0) {
			rem = bd.remainder(rad).intValue();
			if (rem >= 0 && rem <= 9)
				ret = (char) (rem + '0') + ret;
			else if (rem >= 10)
				ret = (char) (rem + 'A' - 10) + ret;
			bd = bd.divide(rad, 0, BigDecimal.ROUND_DOWN);
		}
		return ret == "" ? "0" : ret;
	}

	// =============================================================================
	// [FUNC] Converts a radical value to BigDecimal.
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

	// =============================================================================
	// [FUNC] Convert a decimal value to radical value.
	private static String dec2rad(String str, KeypadButton mode) {
		if (mode == KeypadButton.BIN)
			return dec2rad(str, new BigDecimal(2));
		else if (mode == KeypadButton.OCT)
			return dec2rad(str, new BigDecimal(8));
		else if (mode == KeypadButton.HEX)
			return dec2rad(str, new BigDecimal(16));
		return str;
	}

	// =============================================================================
	// [FUNC] Convert a radical value to decimal.
	private static String rad2dec(String str, KeypadButton mode) {
		if (mode == KeypadButton.BIN)
			return rad2dec(str, new BigDecimal(2));
		else if (mode == KeypadButton.OCT)
			return rad2dec(str, new BigDecimal(8));
		else if (mode == KeypadButton.HEX)
			return rad2dec(str, new BigDecimal(16));
		return str;
	}

	// =============================================================================
	// [FUNC] Returns a grouped text.
	private static String groupText(String str, int cnt, String sep) {
		String ret = "";
		int i = 0, a = 0, z = str.lastIndexOf(".");
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

	// =============================================================================
	// [FUNC] Clears all items from the expression stacks.
	private void clear() {
		for (int i = 0; i < expr.length; i++)
			while (expr[i].hasItems())
				expr[i].pop();
	}

	// =============================================================================
	// [FUNC] Pushed a new item onto the expression stack.
	private void push(Object obj) {
		expr[1].push(obj);
		if (obj instanceof String) {
			expr[0].push(rad2dec((String) obj, numMode));
		} else
			expr[0].push(obj);
	}

	// =============================================================================
	// [FUNC] Pops an item off the expression stack.
	private void pop() {
		Object o = expr[0].pop();
		if (o instanceof Byte) {
			Byte b = (Byte) o;
			if (b.equals(Expression.X)) {
				isXInput = false;
			} else if (b.equals(Expression.BRACKET_OPEN))
				bro = bro - 1;
			else if (b.equals(Expression.BRACKET_CLOSE))
				brc = brc - 1;

		}
		o = expr[1].pop();
		if (o instanceof Byte) {
			Byte b = (Byte) o;
			if (b.equals(Expression.X)) {
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

	// =============================================================================
	// [FUNC] Strips trailing zeros from a decimal string.
	private String stripZeros(String s) {
		if (s.indexOf(".") >= 0)
			while (s.length() > 1 && (s.endsWith("0") || s.endsWith(".")))
				s = s.substring(0, s.length() - 1);
		return s;
	}

	// =============================================================================
	// [FUNC] Throws an error message to the user.
	private void throwError(String msg) {
		prmScreenText = msg;
		hasError = true;

	}

	// =============================================================================
	// [FUNC] Accepts input key from the user.
	public void inputKey(KeypadButton key) {
		if (hasError) {
			if (key == KeypadButton.CLEAR || key == KeypadButton.GROUP_DIGIT) {
			} else if (key == KeypadButton.HEX || key == KeypadButton.DEC
					|| key == KeypadButton.OCT || key == KeypadButton.BIN)
				initFields();
			else { // beep();Invalid input :)
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
			prmScreenText = dec2rad(rad2dec(prmScreenText, numMode), key);
			numMode = key;
			clearPrmScreen = true;
			break;
		case MC:
			memValue = "0";
			clearPrmScreen = true;
			break;
		case MR:
			if (memValue != "0")
				prmScreenText = dec2rad(memValue, numMode);
			else {
				prmScreenText = "0";
				lastKey = KeypadButton.ZERO;
				return;
			}
			clearPrmScreen = true;
			break;
		case MS:
			memValue = rad2dec(prmScreenText, numMode);
			clearPrmScreen = true;
			break;
		case M_ADD:
			memValue = new BigDecimal(rad2dec(prmScreenText, numMode)).add(
					new BigDecimal(memValue)).toPlainString();
			clearPrmScreen = true;
			break;
		case M_REMOVE:
			memValue = new BigDecimal(rad2dec(memValue, numMode)).subtract(
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
			// / else
			// beep();
			break;
		case CE:
			prmScreenText = "0";
			break;
		case CLEAR:
			this.initFields();
			break;

		/*
		 * case AVG: if (nset.size() > 0) { BigDecimal bd = BigDecimal.ZERO; for
		 * (int i = 0; i < nset.size(); i++) bd = bd.add((BigDecimal)
		 * nset.get(i)); prmScreenText = dec2rad(bd.divide(new
		 * BigDecimal(nset.size()), 32,
		 * BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toPlainString(),
		 * numMode); } else throwError("Invalid Operation: Empty set");
		 * clearPrmScreen = true; break; case SUM: BigDecimal bd =
		 * BigDecimal.ZERO; for (int i = 0; i < nset.size(); i++) bd =
		 * bd.add((BigDecimal) nset.get(i)); prmScreenText =
		 * dec2rad(bd.toPlainString(), numMode); clearPrmScreen = true; if
		 * (prmScreenText == "0") { lastKey = KeypadButton.ZERO; return; }
		 * break; case LST: if (prmScreenText != "0") nset.add(new
		 * BigDecimal(rad2dec(prmScreenText, numMode))); else beep();
		 * clearPrmScreen = true; break; case CLS: nset.clear(); break;
		 */
		case ZERO:
			if (clearPrmScreen) {
				prmScreenText = "0";
				clearPrmScreen = false;
			} else if (!prmScreenText.equals("0"))
				prmScreenText += key.getText();
			// else if (lastKey.equals( KeypadButton.ZERO))

			// beep();
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
			// else
			// beep();
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
		case SIGN:
		case MINUS:
		case EULER:
		case PI:
		case X:
			if (prmScreenText != "0" || isDigit(lastKey))
				push(prmScreenText);
			if (key.equals(KeypadButton.BRACKETOPEN)) {
				bro = bro + 1;
				push(Expression.BRO);
			} else if (key.equals(KeypadButton.BRACKETCLOSE)) {
				brc = brc + 1;
				push(Expression.BRC);
			} else if (key.equals(KeypadButton.SQRT_ROOT))
				push(Expression.SRT);
			else if (key.equals(KeypadButton.CUBE_ROOT))
				push(Expression.CRT);
			else if (key.equals(KeypadButton.RECIPROC))
				push(Expression.REC);
			else if (key.equals(KeypadButton.SQUARED))
				push(Expression.SQR);
			else if (key.equals(KeypadButton.CUBING))
				push(Expression.CUB);
			else if (key.equals(KeypadButton.ROOT))
				push(Expression.ROOT);
			else if (key.equals(KeypadButton.FACULTY))
				push(Expression.FCT);
			else if (key.equals(KeypadButton.SIN))
				push(Expression.SIN);
			else if (key.equals(KeypadButton.COS))
				push(Expression.COS);
			else if (key.equals(KeypadButton.TAN))
				push(Expression.TAN);
			else if (key.equals(KeypadButton.ASIN))
				push(Expression.ASIN);
			else if (key.equals(KeypadButton.ACOS))
				push(Expression.ACOS);
			else if (key.equals(KeypadButton.ATAN))
				push(Expression.ATAN);
			else if (key.equals(KeypadButton.LOG))
				push(Expression.LOG);
			else if (key.equals(KeypadButton.LN))
				push(Expression.NLG);
			else if (key.equals(KeypadButton.SIGN))
				push(Expression.NEG);
			else if (key.equals(KeypadButton.POWER))
				push(Expression.POW);
			else if (key.equals(KeypadButton.MULTIPLY)) // 32
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(Expression.MULTIPLY))
						pop();
					if (b.equals(Expression.DIV))
						pop();
					if (b.equals(Expression.ADD))
						pop();
					if (b.equals(Expression.SUB))
						pop();
				}
				push(Expression.MUL);
			} else if (key.equals(KeypadButton.DIV)) // 33
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(Expression.MULTIPLY))
						pop();
					if (b.equals(Expression.DIV))
						pop();
					if (b.equals(Expression.ADD))
						pop();
					if (b.equals(Expression.SUB))
						pop();
				}
				push(Expression.DIV);
			} else if (key.equals(KeypadButton.MODULUS))
				push(Expression.MOD);
			else if (key.equals(KeypadButton.PLUS)) // 35
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(Expression.MULTIPLY))
						pop();
					if (b.equals(Expression.DIV))
						pop();
					if (b.equals(Expression.ADD))
						pop();
					if (b.equals(Expression.SUB))
						pop();
				}
				push(Expression.ADD);
			} else if (key.equals(KeypadButton.MINUS)) // 36
			{
				o = getLast();
				if (o instanceof Byte) {
					b = (Byte) o;
					if (b.equals(Expression.MULTIPLY))
						pop();
					if (b.equals(Expression.DIV))
						pop();
					if (b.equals(Expression.ADD))
						pop();
					if (b.equals(Expression.SUB))
						pop();
				}

				push(Expression.SUB);
			} else if (key.equals(KeypadButton.PI))
				push(Expression.PI);
			else if (key.equals(KeypadButton.EULER))
				push(Expression.E);
			else if (key.equals(KeypadButton.X)) {
				push(Expression.X);
				isXInput = true;
			}
			prmScreenText = "0";
			clearPrmScreen = false;
			break;
		case CALCULATE:
			if (prmScreenText != "0" || isDigit(lastKey) || !expr[0].hasItems())
				push(prmScreenText);

			// we check and try correct expression
			if (bro > brc) {
				int size = bro - brc;
				for (int i = 0; i < size; i++) {
					push(Expression.BRACKET_CLOSE);

				}

			}
			bro = 0;
			brc = 0;

			if (!isXInput) {
				try {
					prmScreenText = stripZeros(expr[0].eval().toPlainString());
				} catch (SyntaxErrorException | InvalidEntryException
						| UnknownOperatorException e) {
					// TODO Auto-generated catch block
					throwError("Syntax Error: " + e.getMessage());
					Log.e(e.getMessage(), e.getMessage());
					e.printStackTrace();
				}
				CalculatorGUI.history.add(getSecScreenText() + " = "
						+ prmScreenText);
				if (numMode == KeypadButton.BIN)
					prmScreenText = dec2rad(prmScreenText, KeypadButton.BIN);
				else if (numMode == KeypadButton.OCT)
					prmScreenText = dec2rad(prmScreenText, KeypadButton.OCT);
				else if (numMode == KeypadButton.HEX)
					prmScreenText = dec2rad(prmScreenText, KeypadButton.HEX);
				clearPrmScreen = true;
			} else {

				Intent intent = new Intent(context, GraphingTab.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				context.startActivity(intent);

			}
			break;

		/*
		 * 
		 * catch (SyntaxErrorException e) { throwError("Syntax Error: " +
		 * e.getMessage()); Log.e(e.getMessage(),e.getMessage()); } catch
		 * (InvalidEntryException e) { throwError("Input Error: " +
		 * e.getMessage()); Log.e(e.getMessage(),e.getMessage()); } catch
		 * (UnknownOperatorException e) { throwError("Unknown Operator: " +
		 * e.getMessage()); Log.e(e.getMessage(),e.getMessage()); } catch
		 * (ArithmeticException e) { throwError("Math Error: " +
		 * e.getMessage()); Log.e(e.getMessage(),e.getMessage()); } catch
		 * (Exception e) { throwError("Application Error: " + e.getMessage());
		 * Log.e(e.getMessage(),e.getMessage()); }
		 * 
		 * 
		 * 
		 * break;
		 */
		default:
			break;
		}

		lastKey = key;
	}
}
