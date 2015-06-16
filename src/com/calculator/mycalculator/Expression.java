package com.calculator.mycalculator;

import java.util.ArrayList;
import java.math.BigDecimal;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceView;

import com.calculator.excpetions.InvalidEntryException;
import com.calculator.excpetions.SyntaxErrorException;
import com.calculator.excpetions.UnknownOperatorException;
import com.calculator.grap.GraphSettings;

/**
 * 
 * */
public class Expression {

	// Oklepaji,koreni,eksponenti, fakulteta, pogoste matematične operacije,
	// vseprisotne operacije

	public double replaceNumberX;
	// shranil se bo notri oklepaj
	private ArrayList<Object> expressionArray = null;
	private ArrayList<Object> pomExpressionArray = null;
	private Expression parent = null;
	private Context context;
	public static double minX = -10;
	public static double maxX = 10;
	public static double minY = -10;
	public static double maxY = 10;
	private boolean stopThreads = false;
	SurfaceView view;
	Path polyline;
	int xx, yy, xxx, yyy;
	
	
	public void setMinY(double minY) {
		this.minY = minY;
	}
	

	// ///////////////////////////////

	public Expression(Context conx) {
		this(null, conx);
		context = conx;

	}

	private Expression(Expression parent, Context conx) {
		this.expressionArray = new ArrayList<>();
		this.parent = parent;
		context = conx;
	}

	/**
	 * Ali ima starsa
	 * */
	private boolean hasParent() {
		return this.parent != null;
	}

	/**
	 * funkcija vrne otroka
	 * */

	private Expression getParent() {
		return this.parent;
	}

	// =============================================================================
	// [FUNC] Returns a boolean value indicating whether the passed parameter is
	// an operator.
	private static boolean isOperator(Object obj) {
		byte opr = obj instanceof Byte ? (byte) obj : -1;
		return (opr >= ByteValues.BRO && opr <= ByteValues.FCT) || (opr >= ByteValues.SIN && opr <= ByteValues.NEG)
				|| (opr >= ByteValues.MUL && opr <= ByteValues.SUB);
	}

	// =============================================================================
	// [FUNC] Returns a boolean value indicating whether the passed parameter is
	// an operand (just BigDecimal for now).
	private static boolean isOperand(Object obj) {
		return obj instanceof BigDecimal;
	}

	// =============================================================================
	// [FUNC] Returns a boolean value indicating wheher the passed parameter is
	// an expression.
	private static boolean isExpression(Object obj) {
		return obj instanceof Expression;
	}

	// =============================================================================
	// [FUNC] Computes and returns the factorial of the argument.
	private static BigDecimal factorial(BigDecimal n) {
		BigDecimal r = BigDecimal.ONE;
		while (n.compareTo(BigDecimal.ONE) > 0) {
			r = r.multiply(n);
			n = n.subtract(BigDecimal.ONE);
		}
		return r;
		// return n.compareTo(BigDecimal.ONE) > 0 ?
		// factorial(n.subtract(BigDecimal.ONE)).multiply(n) : n;
	}

	// =============================================================================
	// [FUNC] Returns a boolean value indicating whether there are items on
	// stack.
	public boolean hasItems() {
		return expressionArray.size() > 0;
	}

	// =============================================================================
	// [FUNC] Adds a new item onto the internal list. Think of it like stack?!
	public Expression push(Object... args) {
		for (Object obj : args)
			this.expressionArray.add(obj);
		return this;
	}

	// =============================================================================
	// [FUNC] Removes the last item (if any) from the internal stack.
	public Object pop() {
		int index = expressionArray.size() - 1;
		return (index >= 0) ? expressionArray.remove(index) : null;
	}

	public Object getLast() {
		int index = expressionArray.size() - 1;
		return (index >= 0) ? expressionArray.get(index) : null;

	}

	public Object getFirst() {
		int index = 0;
		return (index >= 0) ? expressionArray.get(index) : null;

	}

	/**
	 * Converts specified x value to it's pixel location.
	 * 
	 * @param x
	 *            - the value of x for which to find the pixel location on the
	 *            graph.
	 * @return - the pixel value.
	 */
	public synchronized int UnitToPixelX(double x) {
		double pixelsPerUnit = getWidth() / (maxX - minX);
		double pos = (x - minX) * pixelsPerUnit;
		return (int) pos;
	}

	/**
	 * Converts specified y value to it's pixel location.
	 * 
	 * @param y
	 *            - the value of y for which to find the pixel location on the
	 *            graph.
	 * @return - the pixel value.
	 */
	public synchronized int UnitToPixelY(double y) {
		double pixelsPerUnit = getHeight() / (maxY - minY);
		double pos = (y - minY) * pixelsPerUnit;
		pos = -pos + getHeight();
		return (int) pos;
	}

	public int getHeight() {

		return CalculatorAdapter.height;
	}

	public int getWidth() {

		return CalculatorAdapter.width;
	}

	public void exprasionCalculatePoints() {
		try {

			double interval, intervalFormula, slope;
			BigDecimal eqVal = null;
			Double eqPrev = 0.0;

			// / polyline.moveTo(unittopixelxmin, unittopixelxeqprev);
			interval = intervalFormula = (maxX - minX) / (getWidth());

			for (double x = minX;; x += interval) {

				if (stopThreads) {
					break;
				}

				int pixValX = UnitToPixelX(x);

				pomExpressionArray = (ArrayList<Object>) expressionArray
						.clone();
				replaceNumberX = x;
				try {
					eqVal = evalPom();
				} catch (Exception ex) {
					Log.e("Eroor", ex.getLocalizedMessage());
					eqVal = null;

				}
				expressionArray = pomExpressionArray;
				int y = 0;
				if (eqVal != null)
					if (!Double.isInfinite(eqVal.doubleValue())
							&& !Double.isNaN(eqVal.doubleValue())) {
						CalculatorAdapter.point.add(new Point(pixValX,
								y = UnitToPixelY(eqVal.doubleValue())));
					}
				Log.e("x-y", new Point(pixValX, y).toString());
				if (eqVal != null) {
					// Set interval.
					slope = Math.abs((eqVal.doubleValue() - eqPrev)
							/ (x - (x - interval)));
					if (slope > GraphSettings.getMinCalcPerPixel()) {
						if (slope > GraphSettings.getMaxCalcPerPixel()) {
							slope = GraphSettings.getMaxCalcPerPixel();
						}
						interval = intervalFormula / slope;
					} else {
						interval = intervalFormula;
					}
					eqPrev = eqVal.doubleValue();
				} else {
					interval = intervalFormula;
					eqPrev = 0.0;
				}

				if (x >= maxX) {
					break;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			Log.e(e.getMessage(), e.getMessage());
		}

	}

	// =============================================================================
	// [FUNC] Evaluates and returns the result of this expression.
	public BigDecimal evalPom() {

		
		Object obj = null;
		Expression curr = this;
		BigDecimal lhs = null, rhs = null;

		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.Xv) /* || obj.equals(CUB) */) {
				expressionArray.set(i, replaceNumberX);
				// expressionArray.add(i, POW);
				i++;
			}
		}
		
		

		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.PI) /* || obj.equals(CUB) */) {
				expressionArray.set(i, Math.PI);
				// expressionArray.add(i, POW);
				i++;
			} else if (obj.equals(ByteValues.E) /* || obj.equals(CUB) */) {
				expressionArray.set(i, Math.E);
				// expressionArray.add(i, POW);
				i++;
			}
		}

		// zdruzimo izraz

		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.BRO)) {

				if (this.equals(curr)) {
					curr = new Expression(curr, context);
					expressionArray.set(i, curr);
					continue;
				} else {
					curr = new Expression(curr, context);
					curr.getParent().push(curr);
				}
			} else if (obj.equals(ByteValues.BRC)) {

				curr = curr.getParent();
				if (curr == null)
					break;
			} else if (this.equals(curr)) {
				if (!(isOperator(obj) || isExpression(obj)))
					expressionArray.set(i, new BigDecimal(obj.toString()));
				continue;
			} else
				curr.push(obj);
			expressionArray.remove(i--);
		}

		if (!this.equals(curr))
			return null;

		// STEP 1: Translate SQR & CUB into POWs
		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.SQR) || obj.equals(ByteValues.CUB)) {
				expressionArray.set(i, new BigDecimal(obj.equals(ByteValues.SQR) ? 2 : 3));
				expressionArray.add(i, ByteValues.POW);
				i++;
			}
		}

		// STEP 2: Roots, powers, reciprocal and factorial.
		for (int i = 0; i < expressionArray.size(); i++) {

			obj = expressionArray.get(i);
			if (isOperator(obj))
				switch ((byte) obj) {
				case ByteValues.SQUARE_ROOT:
				case ByteValues.CUBE_ROOT:
					obj = i + 1 < expressionArray.size() ? expressionArray.get(i + 1) : -1;
					if (obj.equals(ByteValues.SRT) || obj.equals(ByteValues.CRT)) 
					continue;

					else if (isOperand(obj)) rhs = (BigDecimal) obj;
					else if (isExpression(obj)) rhs = ((Expression) obj).evalPom();
				//	else if()
					else return null;
					
					if (rhs.compareTo(BigDecimal.ZERO) < 0)
						if(expressionArray.get(i).equals(ByteValues.SRT))
							return null;
					rhs = new BigDecimal(expressionArray.get(i).equals(ByteValues.SRT) ?
						Math.sqrt(rhs.doubleValue()) : Math.cbrt(rhs.doubleValue()));
					expressionArray.set(i, rhs);
					expressionArray.remove(i + 1);
					i = Math.max(i - 2, -1);
					break;

				case ByteValues.ROOT:
					obj = i + 2 < expressionArray.size() ? expressionArray
							.get(i + 2) : -1;
					if (obj.equals(ByteValues.POW))
						continue;
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).evalPom();
					else
						return null;
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj))
						rhs = (BigDecimal) obj;
					else if (isExpression(obj))
						rhs = ((Expression) obj).evalPom();

					else
						return null;
					if (lhs.compareTo(BigDecimal.ZERO) < 0){
						try{
						lhs = BigDecimal.valueOf(Math.pow(rhs.doubleValue(),1/lhs.doubleValue()));
						}catch (Exception e) {
							// TODO: handle exception
							//throw new SyntaxErrorException("Missing operand");
							return null;
						}	
					}else
						try{
						lhs =lhs.valueOf(Math.pow(rhs.doubleValue(),1/lhs.doubleValue()));
					}catch (Exception e) {
						// TODO: handle exception
						//throw new SyntaxErrorException("Missing operand");
						return null;
					}
					expressionArray.set(i - 1, lhs);
					expressionArray.remove(i);
					expressionArray.remove(i);
					i = Math.max(i - 3, -1);
					break;

				case ByteValues.POWER:
					obj = i + 2 < expressionArray.size() ? expressionArray
							.get(i + 2) : -1;
					if (obj.equals(ByteValues.POW))
						continue;
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).evalPom();
					else
						return null;
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj))
						rhs = (BigDecimal) obj;
					else if (isExpression(obj))
						rhs = ((Expression) obj).evalPom();

					else
						return null;
					
					if (rhs.compareTo(BigDecimal.ZERO) < 0){
						try{
						lhs = BigDecimal.valueOf(Math.pow(lhs.doubleValue(),rhs.doubleValue()));
						}catch (Exception e) {
							// TODO: handle exception
							//throw new SyntaxErrorException("Missing operand");
							return null;
						}	
					}else
						try{
						lhs =lhs.valueOf(Math.pow(lhs.doubleValue(),rhs.doubleValue()));
					}catch (Exception e) {
						// TODO: handle exception
						//throw new SyntaxErrorException("Missing operand");
						return null;
					}
					expressionArray.set(i - 1, lhs);
					expressionArray.remove(i);
					expressionArray.remove(i);
					i = Math.max(i - 3, -1);
					break;

				case ByteValues.RECIPROCAL:
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).evalPom();
					else
						return null;
					expressionArray.set(i - 1, BigDecimal.ONE.divide(lhs, 30,
							BigDecimal.ROUND_DOWN));
					expressionArray.remove(i);
					i -= 1;
					break;

				case ByteValues.FACTORIAL:
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).evalPom();
					else
						return null;
				//	 if (lhs.compareTo(BigDecimal.ZERO) < 0)
			//		throw new
				//	InvalidEntryException("Factorial input less than zero");
				//	else if (lhs.compareTo(new BigDecimal(400)) > 0)
				//	 throw new
				//	 InvalidEntryException("Factorial input too large (>5000)");
					expressionArray.set(i - 1,
							factorial(lhs.setScale(0, BigDecimal.ROUND_DOWN)));
					expressionArray.remove(i);
					i -= 1;
					break;

				}
		}

		// STEP 3: Common mathematical functions.
		for (int i = expressionArray.size() - 1; i >= 0; i--) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.SIN) || obj.equals(ByteValues.COS) || obj.equals(ByteValues.TAN)
					|| obj.equals(ByteValues.ASIN) || obj.equals(ByteValues.ACOS) || obj.equals(ByteValues.ATAN)
					|| obj.equals(ByteValues.LOG) || obj.equals(ByteValues.NLG) || obj.equals(ByteValues.NEG)) {
				obj = i + 1 < expressionArray.size() ? expressionArray
						.get(i + 1) : -1;
				if (isOperand(obj))
					rhs = (BigDecimal) obj;
				else if (isExpression(obj))
					rhs = ((Expression) obj).evalPom();
				else
					return null;
				switch ((byte) expressionArray.get(i)) {
				case ByteValues.SIN:

					if (CalculatorGUI.radian) {
						rhs = new BigDecimal(Math.sin(rhs.doubleValue()));
					} else {
						rhs = new BigDecimal(Math.sin(Math.toRadians(rhs
								.doubleValue())));

					}
					/* Math.toRadians(rhs.doubleValue())) */
					break;
				case ByteValues.COS:
					if (CalculatorGUI.radian) {
						rhs = new BigDecimal(Math.cos(rhs.doubleValue()));
					} else {
						rhs = new BigDecimal(Math.cos(Math.toRadians(rhs
								.doubleValue())));

					}

					break;
				case ByteValues.TAN:
					if (CalculatorGUI.radian) {
						if (rhs.compareTo(new BigDecimal(90)) == 0)
							throw new ArithmeticException("Tangent 90");
						rhs = new BigDecimal(Math.tan(rhs.doubleValue()));
					} else {
						if (rhs.compareTo(new BigDecimal(90)) == 0)
							throw new ArithmeticException("Tangent 90");
						rhs = new BigDecimal(Math.tan(Math.toRadians(rhs
								.doubleValue())));

					}
					break;

				case ByteValues.ASIN:
					if (rhs.doubleValue() >= -1 && rhs.doubleValue() <= 1) {
						if (CalculatorGUI.radian) {
							rhs = new BigDecimal(Math.asin(rhs.doubleValue()));

						} else {
							rhs = new BigDecimal(Math.toDegrees(Math.asin((rhs
									.doubleValue()))));

						}
					} else
						return null;

					break;

				case ByteValues.ACOS:
					if (rhs.doubleValue() >= -1 && rhs.doubleValue() <= 1) {
						if (CalculatorGUI.radian) {
							rhs = new BigDecimal(Math.acos(rhs.doubleValue()));
						} else {
							rhs = new BigDecimal(Math.toDegrees(Math.acos((rhs
									.doubleValue()))));

						}
					} else
						return null;

					break;
				case ByteValues.ATAN:
					if (CalculatorGUI.radian) {
						rhs = new BigDecimal(Math.atan(rhs.doubleValue()));
					} else {
						rhs = new BigDecimal(Math.toDegrees(Math.atan((rhs
								.doubleValue()))));

					}

					break;
				case ByteValues.LOG:
					if (rhs.doubleValue() > 0) {
						rhs = new BigDecimal(Math.log10(rhs.doubleValue()));
					} else
						return null;
					break;
				case ByteValues.NLG:
					if (rhs.doubleValue() > 0) {
						rhs = new BigDecimal(Math.log(rhs.doubleValue()));
					} else
						return null;
					break;

				case ByteValues.NEG:
					rhs = rhs.negate();
					break;
				default:
					continue;
				}
				if (rhs.scale() > 15)
					rhs = rhs.setScale(15, BigDecimal.ROUND_HALF_EVEN);
				expressionArray.set(i, rhs);
				expressionArray.remove(i + 1);
			}
		}

		// STEP 4: Multiplicative and additive operations.
		for (int s = 0; s < 2; s++)
			for (int i = 0; i < expressionArray.size(); i++) {
				obj = expressionArray.get(i);
				if (s == 0
						&& (obj.equals(ByteValues.MUL) || obj.equals(ByteValues.DIV) || obj
								.equals(ByteValues.MOD)) || s == 1
						&& (obj.equals(ByteValues.ADD) || obj.equals(ByteValues.SUB))) {
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).evalPom();
					else
						return null;
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj))
						rhs = (BigDecimal) obj;
					else if (isExpression(obj))
						rhs = ((Expression) obj).evalPom();
					else
						return null;
					switch ((byte) expressionArray.get(i)) {
					case ByteValues.MUL:
					//	lhs=lhs.valueOf(lhs.doubleValue()*rhs.doubleValue());
						lhs = lhs.multiply(rhs);
						break;
					case ByteValues.DIV:
						if (rhs.compareTo(BigDecimal.ZERO) == 0)
							throw new ArithmeticException("Division by zero");
						lhs = lhs.divide(rhs, 30, BigDecimal.ROUND_DOWN);
						break;
					case ByteValues.MOD:
						
						lhs = lhs.remainder(rhs);
						break;
					case ByteValues.ADD:
						lhs = lhs.add(rhs);
						break;
					case ByteValues.SUB:
						lhs = lhs.subtract(rhs);
						break;
					}
					expressionArray.set(i - 1, lhs);
					expressionArray.remove(i);
					expressionArray.remove(i);
					i -= 1;
				} else if (isExpression(obj)) {
					expressionArray.set(i, rhs = ((Expression) obj).evalPom());
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj)) {
						expressionArray.set(i - 1,
								rhs = rhs.multiply((BigDecimal) obj));
						expressionArray.remove(i);
						i -= 1;
					}
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj)) {
						expressionArray.set(i, rhs.multiply((BigDecimal) obj));
						expressionArray.remove(i + 1);
					}
				}
			}

		// STEP 4: Multiply any remaining items. A cheap way to get my math
		// right.
		// E.g. 2 sin 30 == 2 * sin 30
		while (expressionArray.size() > 1) {
			obj = expressionArray.get(0);
			if (isExpression(obj))
				lhs = ((Expression) obj).evalPom();
			else if (isOperand(obj))
				lhs = (BigDecimal) obj;
			else
				return null;
			obj = expressionArray.get(1);
			if (isExpression(obj))
				rhs = ((Expression) obj).evalPom();
			else if (isOperand(obj))
				rhs = (BigDecimal) obj;
			else
				return null;
			expressionArray.set(0, lhs.multiply(rhs));
			expressionArray.remove(1);
		}

		if (expressionArray.size() == 0)
			return null;
		else if (isExpression(expressionArray.get(0)))
			expressionArray
					.set(0, ((Expression) expressionArray.get(0)).evalPom());

		lhs = (BigDecimal) expressionArray.get(0);
		if (lhs.scale() > 30)
			lhs = lhs.setScale(30, BigDecimal.ROUND_HALF_EVEN);
		return lhs.stripTrailingZeros();
	}

		

	// =============================================================================
	// [FUNC] Evaluates and returns the result of this expression.
	
	public BigDecimal eval() throws SyntaxErrorException,
			InvalidEntryException, UnknownOperatorException {

		try{
		Object obj = null;
		Expression curr = this;
		BigDecimal lhs = null, rhs = null;

		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.PI)) {
				expressionArray.set(i, Math.PI);
				// expressionArray.add(i, POW);
				i++;
			} else if (obj.equals(ByteValues.E) ) {
				expressionArray.set(i, Math.E);
				// expressionArray.add(i, POW);
				i++;
			}
		}

		// zdruzimo izraz

		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.BRO)) {

				if (this.equals(curr)) {
					curr = new Expression(curr, context);
					expressionArray.set(i, curr);
					continue;
				} else {
					curr = new Expression(curr, context);
					curr.getParent().push(curr);
				}
			} else if (obj.equals(ByteValues.BRC)) {

				curr = curr.getParent();
				if (curr == null)
					break;
			} else if (this.equals(curr)) {
				if (!(isOperator(obj) || isExpression(obj)))
					expressionArray.set(i, new BigDecimal(obj.toString()));
				continue;
			} else
				curr.push(obj);
			expressionArray.remove(i--);
		}

		if (!this.equals(curr))

			throw new SyntaxErrorException("Unmatched brackets");
		// push(BRC);
		// throw new SyntaxErrorException("Unmatched brackets");

		// STEP 1: Translate SQR & CUB into POWs
		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.SQR) || obj.equals(ByteValues.CUB)) {
				expressionArray.set(i, new BigDecimal(obj.equals(ByteValues.SQR) ? 2 : 3));
				expressionArray.add(i, ByteValues.POW);
				i++;
			}
		}

		// STEP 2: Roots, powers, reciprocal and factorial.
		for (int i = 0; i < expressionArray.size(); i++) {
		/*			obj = expressionArray.get(i);
			if (obj.equals(SIN) || obj.equals(COS) || obj.equals(TAN)
					|| obj.equals(ASIN) || obj.equals(ACOS) || obj.equals(ATAN)
					|| obj.equals(LOG) || obj.equals(NLG) || obj.equals(NEG)) {
				obj = i + 1 < expressionArray.size() ? expressionArray
						.get(i + 1) : -1;
				if (isOperand(obj))
					rhs = (BigDecimal) obj;
				else if (isExpression(obj))
					rhs = ((Expression) obj).eval();
				else
					throw new SyntaxErrorException("Missing operand");*/
			obj = expressionArray.get(i);
			if (isOperator(obj))
				switch ((byte) obj) {
				case ByteValues.SQUARE_ROOT:
				case ByteValues.CUBE_ROOT:
					obj = i + 1 < expressionArray.size() ? expressionArray.get(i + 1) : -1;
					if (obj.equals(ByteValues.SRT) || obj.equals(ByteValues.CRT)) 
					continue;

					else if (isOperand(obj)) rhs = (BigDecimal) obj;
					else if (isExpression(obj)) rhs = ((Expression) obj).eval();
				//	else if()
					else throw new SyntaxErrorException("Missing operand");
					
					if (rhs.compareTo(BigDecimal.ZERO) < 0)
						if(expressionArray.get(i).equals(ByteValues.SRT))
						throw new SyntaxErrorException("Root of negative no.");
					rhs = new BigDecimal(expressionArray.get(i).equals(ByteValues.SRT) ?
						Math.sqrt(rhs.doubleValue()) : Math.cbrt(rhs.doubleValue()));
					expressionArray.set(i, rhs);
					expressionArray.remove(i + 1);
					i = Math.max(i - 2, -1);
					break;

				case ByteValues.ROOT:
					obj = i + 2 < expressionArray.size() ? expressionArray
							.get(i + 2) : -1;
					if (obj.equals(ByteValues.POW))
						continue;
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).eval();
					else
						throw new SyntaxErrorException("Missing operand");
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj))
						rhs = (BigDecimal) obj;
					else if (isExpression(obj))
						rhs = ((Expression) obj).eval();

					else
						throw new SyntaxErrorException("Missing operand");
					if (lhs.compareTo(BigDecimal.ZERO) < 0){
						try{
						lhs = BigDecimal.valueOf(Math.pow(rhs.doubleValue(),1/lhs.doubleValue()));
						}catch (Exception e) {
							// TODO: handle exception
							//throw new SyntaxErrorException("Missing operand");
							throw new SyntaxErrorException(String.valueOf(Double.POSITIVE_INFINITY));
						}	
					}else
						try{
						lhs =lhs.valueOf(Math.pow(rhs.doubleValue(),1/lhs.doubleValue()));
					}catch (Exception e) {
						// TODO: handle exception
						//throw new SyntaxErrorException("Missing operand");
					throw new SyntaxErrorException(String.valueOf(Double.POSITIVE_INFINITY));
					}
					expressionArray.set(i - 1, lhs);
					expressionArray.remove(i);
					expressionArray.remove(i);
					i = Math.max(i - 3, -1);
					break;

				case ByteValues.POWER:
					obj = i + 2 < expressionArray.size() ? expressionArray
							.get(i + 2) : -1;
					if (obj.equals(ByteValues.POW))
						continue;
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).eval();
					else
						throw new SyntaxErrorException("Missing operand");
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj))
						rhs = (BigDecimal) obj;
					else if (isExpression(obj))
						rhs = ((Expression) obj).eval();

					else
						throw new SyntaxErrorException("Missing operand");
					
					if (rhs.compareTo(BigDecimal.ZERO) < 0){
						try{
						lhs = BigDecimal.valueOf(Math.pow(lhs.doubleValue(),rhs.doubleValue()));
						}catch (Exception e) {
							// TODO: handle exception
							//throw new SyntaxErrorException("Missing operand");
							throw new SyntaxErrorException(String.valueOf(Double.POSITIVE_INFINITY));
						}	
					}else
						try{
						lhs =lhs.valueOf(Math.pow(lhs.doubleValue(),rhs.doubleValue()));
					}catch (Exception e) {
						// TODO: handle exception
						//throw new SyntaxErrorException("Missing operand");
					throw new SyntaxErrorException(String.valueOf(Double.POSITIVE_INFINITY));
					}
					expressionArray.set(i - 1, lhs);
					expressionArray.remove(i);
					expressionArray.remove(i);
					i = Math.max(i - 3, -1);
					break;

				case ByteValues.RECIPROCAL:
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).eval();
					else
						throw new SyntaxErrorException("Missing operand");
					expressionArray.set(i - 1, BigDecimal.ONE.divide(lhs, 30,
							BigDecimal.ROUND_DOWN));
					expressionArray.remove(i);
					i -= 1;
					break;

				case ByteValues.FACTORIAL:
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).eval();
					else
						throw new SyntaxErrorException("Missing operand");
					 if (lhs.compareTo(BigDecimal.ZERO) < 0)
							throw new
							InvalidEntryException("Factorial input less than zero");
							else if (lhs.compareTo(new BigDecimal(400)) > 0)
							 throw new
							 InvalidEntryException("Factorial input too large (>5000)");
					// if (lhs.compareTo(BigDecimal.ZERO) < 0)
					// throw new
					// InvalidInputException("Factorial input less than zero");
					// else if (lhs.compareTo(new BigDecimal(5000)) > 0)
					// throw new
					// InvalidInputException("Factorial input too large (>5000)");
					expressionArray.set(i - 1,
							factorial(lhs.setScale(0, BigDecimal.ROUND_DOWN)));
					expressionArray.remove(i);
					i -= 1;
					break;

				}
		}

		// STEP 3: Common mathematical functions.
		for (int i = expressionArray.size() - 1; i >= 0; i--) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.SIN) || obj.equals(ByteValues.COS) || obj.equals(ByteValues.TAN)
					|| obj.equals(ByteValues.ASIN) || obj.equals(ByteValues.ACOS) || obj.equals(ByteValues.ATAN)
					|| obj.equals(ByteValues.LOG) || obj.equals(ByteValues.NLG) || obj.equals(ByteValues.NEG)) {
				obj = i + 1 < expressionArray.size() ? expressionArray
						.get(i + 1) : -1;
				if (isOperand(obj))
					rhs = (BigDecimal) obj;
				else if (isExpression(obj))
					rhs = ((Expression) obj).eval();
				else
					throw new SyntaxErrorException("Missing operand");
				switch ((byte) expressionArray.get(i)) {
				case ByteValues.SIN:

					if (CalculatorGUI.radian) {
						rhs = new BigDecimal(Math.sin(rhs.doubleValue()));
					} else {
						rhs = new BigDecimal(Math.sin(Math.toRadians(rhs
								.doubleValue())));

					}
					/* Math.toRadians(rhs.doubleValue())) */
					break;
				case ByteValues.COS:
					if (CalculatorGUI.radian) {
						rhs = new BigDecimal(Math.cos(rhs.doubleValue()));
					} else {
						rhs = new BigDecimal(Math.cos(Math.toRadians(rhs
								.doubleValue())));

					}

					break;
				case ByteValues.TAN:
					if (CalculatorGUI.radian) {
						if (rhs.compareTo(new BigDecimal(90)) == 0)
							throw new ArithmeticException("Tangent 90");
						rhs = new BigDecimal(Math.tan(rhs.doubleValue()));
					} else {
						if (rhs.compareTo(new BigDecimal(90)) == 0)
							throw new ArithmeticException("Tangent 90");
						rhs = new BigDecimal(Math.tan(Math.toRadians(rhs
								.doubleValue())));

					}
					break;

				case ByteValues.ASIN:
					if (rhs.doubleValue() >= -1 && rhs.doubleValue() <= 1) {
						if (CalculatorGUI.radian) {
							rhs = new BigDecimal(Math.asin(rhs.doubleValue()));

						} else {
							rhs = new BigDecimal(Math.toDegrees(Math.asin((rhs
									.doubleValue()))));

						}
					} else
						throw new InvalidEntryException("NaN");

					break;

				case ByteValues.ACOS:
					if (rhs.doubleValue() >= -1 && rhs.doubleValue() <= 1) {
						if (CalculatorGUI.radian) {
							rhs = new BigDecimal(Math.acos(rhs.doubleValue()));
						} else {
							rhs = new BigDecimal(Math.toDegrees(Math.acos((rhs
									.doubleValue()))));

						}
					} else
						throw new InvalidEntryException("NaN");

					break;
				case ByteValues.ATAN:
					if (CalculatorGUI.radian) {
						rhs = new BigDecimal(Math.atan(rhs.doubleValue()));
					} else {
						rhs = new BigDecimal(Math.toDegrees(Math.atan((rhs
								.doubleValue()))));

					}

					break;
				case ByteValues.LOG:
					if (rhs.doubleValue() > 0) {
						rhs = new BigDecimal(Math.log10(rhs.doubleValue()));
					} else
						throw new InvalidEntryException("NaN");
					break;
				case ByteValues.NLG:
					if (rhs.doubleValue() > 0) {
						rhs = new BigDecimal(Math.log(rhs.doubleValue()));
					} else
						throw new InvalidEntryException("NaN");
					break;

				case ByteValues.NEG:
					rhs = rhs.negate();
					break;
				default:
					continue;
				}
				if (rhs.scale() > 15)
					rhs = rhs.setScale(15, BigDecimal.ROUND_HALF_EVEN);
				expressionArray.set(i, rhs);
				expressionArray.remove(i + 1);
			}
		}

		// STEP 4: Multiplicative and additive operations.
		Byte opr=0;
		for (int s = 0; s < 2; s++)
			for (int i = 0; i < expressionArray.size(); i++) {
				obj = expressionArray.get(i);
				if (s == 0&& (obj.equals(ByteValues.MUL) || obj.equals(ByteValues.DIV) || obj.equals(ByteValues.MOD)) || s == 1&& (obj.equals(ByteValues.ADD) || obj.equals(ByteValues.SUB))) {
					opr=ByteValues.SUB;
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					
					boolean compress = false;
					if (isOperand(obj))
						lhs = (BigDecimal) obj;
					else if (isExpression(obj))
						lhs = ((Expression) obj).eval();

					  
					else
					{	if(!opr.equals(ByteValues.SUB))
						
						
						throw new SyntaxErrorException("Missing operand");
					 lhs=null;
					}
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj))
						rhs = (BigDecimal) obj;
					else if (isExpression(obj))
						rhs = ((Expression) obj).eval();
					else
						throw new SyntaxErrorException("Missing operand");
					switch ((byte) expressionArray.get(i)) {
					case ByteValues.MUL:
						lhs = lhs.multiply(rhs);
						compress=true;
						break;
					case ByteValues.DIV:
						if (rhs.compareTo(BigDecimal.ZERO) == 0)
							throw new ArithmeticException("Division by zero");
						lhs = lhs.divide(rhs, 30, BigDecimal.ROUND_DOWN);
						compress=true;
						break;
					case ByteValues.MOD:
						lhs = lhs.remainder(rhs);
						compress=true;
						break;
					case ByteValues.ADD:
						lhs = lhs.add(rhs);
						compress=true;
						break;
					case ByteValues.SUB:
						if(lhs!=null){
						 lhs = lhs.subtract(rhs);
						 compress=true; 
						}
						else{
							lhs=rhs.negate();
						compress=false;	
						}
						break;
					}
					if(compress==true){
					expressionArray.set(i - 1, lhs);
					expressionArray.remove(i);
					expressionArray.remove(i);
					i -= 1;
					}else{
						expressionArray.set(i, lhs);
					expressionArray.remove(i + 1);}
				} else if (isExpression(obj)) {
					expressionArray.set(i, rhs = ((Expression) obj).eval());
					obj = i > 0 ? expressionArray.get(i - 1) : -1;
					if (isOperand(obj)) {
						expressionArray.set(i - 1,
								rhs = rhs.multiply((BigDecimal) obj));
						expressionArray.remove(i);
						i -= 1;
					}
					obj = i + 1 < expressionArray.size() ? expressionArray
							.get(i + 1) : -1;
					if (isOperand(obj)) {
						expressionArray.set(i, rhs.multiply((BigDecimal) obj));
						expressionArray.remove(i + 1);
					}
				}
			}

		// STEP 4: Multiply any remaining items. A cheap way to get my math
		// right.
		// E.g. 2 sin 30 == 2 * sin 30
		while (expressionArray.size() > 1) {
			obj = expressionArray.get(0);
			if (isExpression(obj))
				lhs = ((Expression) obj).eval();
			else if (isOperand(obj))
				lhs = (BigDecimal) obj;
			else
				throw new UnknownOperatorException();
			obj = expressionArray.get(1);
			if (isExpression(obj))
				rhs = ((Expression) obj).eval();
			else if (isOperand(obj))
				rhs = (BigDecimal) obj;
			else
				throw new UnknownOperatorException();

			expressionArray.set(0, lhs.multiply(rhs));
			expressionArray.remove(1);
		}

		if (expressionArray.size() == 0)
			throw new SyntaxErrorException("Empty "
					+ (this.hasParent() ? "brackets" : "expression"));
		else if (isExpression(expressionArray.get(0)))
			expressionArray
					.set(0, ((Expression) expressionArray.get(0)).eval());

		lhs = (BigDecimal) expressionArray.get(0);
		if (lhs.scale() > 30)
			lhs = lhs.setScale(30, BigDecimal.ROUND_HALF_EVEN);
		return lhs.stripTrailingZeros();
		
		
			}
			catch (Exception e) {
				// TODO: handle exception
			  Log.e(e.getMessage(),e.getLocalizedMessage());
			
			  throw new SyntaxErrorException("Error");
			}
		
	}
	
	

	// =============================================================================
	// [FUNC] Returns the string representation of this expression.
	public String toString() {
		String ret = "";
		Object obj = null;

		for (int i = 0; i < expressionArray.size(); i++) {
			obj = expressionArray.get(i);
			if (obj.equals(ByteValues.BRO))
				ret += "(";
			else if (obj.equals(ByteValues.BRC))
				ret += ")";
			else if (isExpression(obj))
				ret += "[" + obj + "]";
			else if (obj.equals(ByteValues.SRT))
				ret += "\u221A";
			else if (obj.equals(ByteValues.CRT))
				ret += "\u00B3\u221a";
			else if (obj.equals(ByteValues.REC))
				ret += "\u02C9\u00B9";
			else if (obj.equals(ByteValues.SQR))
				ret += "\u00B2";
			else if (obj.equals(ByteValues.CUB))
				ret += "\u00B3";
			else if (obj.equals(ByteValues.POW))
				ret += " ^ ";
			else if (obj.equals(ByteValues.ROOT))
				ret += "^\u221A ";
			else if (obj.equals(ByteValues.FCT))
				ret += "!";
			else if (obj.equals(ByteValues.PI))
				ret += "π";
			else if (obj.equals(ByteValues.EULER))
				ret += "e";
			else if (obj.equals(ByteValues.SIN))
				ret += " sin";
			else if (obj.equals(ByteValues.COS))
				ret += " cos";
			else if (obj.equals(ByteValues.TAN))
				ret += " tan";
			else if (obj.equals(ByteValues.ASIN))
				ret += " asin";
			else if (obj.equals(ByteValues.ACOS))
				ret += " acos";
			else if (obj.equals(ByteValues.ATAN))
				ret += " atan";
			else if (obj.equals(ByteValues.LOG))
				ret += " log";
			else if (obj.equals(ByteValues.NLG))
				ret += " ln";
			else if (obj.equals(ByteValues.NEG))
				ret += "-";
			else if (obj.equals(ByteValues.X))
				ret += " X ";
			else if (obj.equals(ByteValues.MUL))
				ret += " \u00D7 ";
			else if (obj.equals(ByteValues.DIV))
				ret += " \u00F7 ";
			else if (obj.equals(ByteValues.MOD))
				ret += " mod ";
			else if (obj.equals(ByteValues.ADD))
				ret += " \u002B ";
			else if (obj.equals(ByteValues.SUB))
				ret += " - ";

			else if (i > 0	&& (expressionArray.get(i - 1).equals(ByteValues.SRT)
							|| expressionArray.get(i - 1).equals(ByteValues.CRT) || expressionArray
							.get(i - 1).equals(ByteValues.NEG)))
				ret += obj;
			else
				ret += " " + obj;
		}

		ret = ret.replaceAll("\\s\\s+", " ");
		ret = ret.replaceAll("\\(\\s+", "(");
		return ret.trim();

		
	}
	
	
	
	
}
