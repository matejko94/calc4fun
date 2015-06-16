package com.calculator.position;

import com.calculator.mycalculator.CalculatorAdapter;
import com.calculator.mycalculator.CalculatorGUI;
import com.calculator.mycalculator.KeypadButton;
import com.calculator.mycalculator.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class ChooseDialog extends Dialog {

	boolean longC1;
	KeypadAdapter mKeypadAdapter;
	GridView mKeypadGrid;

	ChooseDialog(Context context, boolean longC) {
		// TODO Auto-generated constructor stub

		super(context);
	    this.longC1 = longC;
		
		setContentView(R.layout.grid);
		mKeypadGrid = (GridView) findViewById(R.id.grdButtons);

		// Create Keypad Adapter
		mKeypadAdapter = new KeypadAdapter(getContext());

		// Set adapter of the keypad grid
		mKeypadGrid.setAdapter(mKeypadAdapter);

		// Set button click listener of the keypad adapter
		mKeypadAdapter.setOnButtonClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btn = (Button) v;
				KeypadButton keypadButton = (KeypadButton) btn.getTag();
				Log.e(keypadButton.getText(), keypadButton.getText());
				PositionButtons.keypadButton = keypadButton;

				if (longC1)
					CalculatorGUI.button2.put(PositionButtons.click,
							keypadButton);
				else
					CalculatorGUI.button.put(PositionButtons.click,
							keypadButton);

				PositionButtons.pressButton=true;
				dismiss();
			}

		});


	}



	
	// TODO Auto-generated constructor stub
}
