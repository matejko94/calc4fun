package com.calculator.position;


import com.calculator.mycalculator.CalculatorGUI;
import com.calculator.mycalculator.KeypadButton;
import com.calculator.mycalculator.R;
import com.google.android.gms.wallet.fragment.Dimension;

import android.widget.*;
import android.app.Dialog;
import android.content.*;
import android.content.res.Configuration;
import android.text.AndroidCharacter;
import android.view.*;
import android.view.View.OnClickListener;

public class KeypadAdapter extends BaseAdapter {
	private Context mContext;
	
	// Declare button click listener variable
	private OnClickListener mOnButtonClick;
	
	public KeypadAdapter(Context c) {
		mContext = c;
	}

	// Method to set button click listener variable
	
	public void setOnButtonClickListener(OnClickListener listener) {
		mOnButtonClick = listener;
	}

	public int getCount() {
		return mButtons.length;
	}

	public Object getItem(int position) {
		return mButtons[position];
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ButtonView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		Button btn;


			btn = new Button(mContext);
			KeypadButton keypadButton = mButtons[position];
			

			switch(CalculatorGUI.screenSize) {
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		    	btn.setTextSize(12);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		    	btn.setTextSize(14);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
		    	btn.setTextSize(22);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
		    	btn.setTextSize(26);
		        break;
		    default:
		    	btn.setTextSize(18);
		       
		}
		
			
			
		
			if(keypadButton != KeypadButton.DUMMY)
				btn.setOnClickListener(mOnButtonClick);
			else
				btn.setClickable(false);
			// Set CalculatorButton enumeration as tag of the button so that we
			// will use this information from our main view to identify what to do
			btn.setTag(keypadButton);
	
		btn.setText(mButtons[position].getText());
		return btn;
	}

	// Create and populate keypad buttons array with CalculatorButton enum
	// values
	

	private KeypadButton[] mButtons = { KeypadButton.MC, KeypadButton.MR,KeypadButton.MS, KeypadButton.M_ADD, KeypadButton.M_REMOVE,
			KeypadButton.ZERO,KeypadButton.ONE,KeypadButton.TWO,KeypadButton.THREE,KeypadButton.FOUR,KeypadButton.FIVE,
			KeypadButton.SIX,KeypadButton.SEVEN,KeypadButton.EIGHT,KeypadButton.NINE,KeypadButton.A,KeypadButton.B,
			KeypadButton.C,KeypadButton.D,KeypadButton.E,KeypadButton.F,KeypadButton.BRACKETOPEN,KeypadButton.BRACKETCLOSE,
			KeypadButton.BACKSPACE,KeypadButton.CE,KeypadButton.CLEAR,KeypadButton.PLUS,KeypadButton.MINUS,KeypadButton.MULTIPLY,
			KeypadButton.DIV,KeypadButton.RECIPROC,KeypadButton.DECIMAL_SEP,KeypadButton.SIGN,KeypadButton.SQRT_ROOT,
			KeypadButton.CUBE_ROOT,KeypadButton.ROOT,KeypadButton.SQUARED,KeypadButton.CUBING,KeypadButton.FACULTY,
			KeypadButton.POWER,KeypadButton.MODULUS,KeypadButton.SIN,KeypadButton.COS,KeypadButton.TAN,
			KeypadButton.ASIN,KeypadButton.ACOS,KeypadButton.ATAN,KeypadButton.LOG,KeypadButton.LN,KeypadButton.EULER,KeypadButton.PI,
			KeypadButton.CALCULATE,KeypadButton.DEC,KeypadButton.X, KeypadButton.SETTINGS,KeypadButton.CONVERT
			 };

	

}
