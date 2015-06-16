package com.calculator.position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.calculator.mycalculator.ButtonToTextView;
import com.calculator.mycalculator.CalculatorGUI;
import com.calculator.mycalculator.DefaulButtonsPosition;
import com.calculator.mycalculator.KeypadButton;
import com.calculator.mycalculator.R;
import com.calculator.mycalculator.R.drawable;
import com.calculator.mycalculator.R.id;
import com.calculator.mycalculator.R.layout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PositionButtons extends Activity {

	public Map<Integer, Button> arButton = new HashMap<>();
	public ArrayList<Integer> rLayout = new ArrayList<>();
	//ce smo pritisnilni gumb ali smo le brutalno zaprli dialog (on cancle , back press itd..)
	public static boolean pressButton;
	KeypadAdapter mKeypadAdapter;
	GridView mKeypadGrid;
	boolean dropble;
	public static int click;
	
	//cd dialog velja za on click
	public static ChooseDialog cd;
	//cd1 dialog velja za on long click
	public static ChooseDialog cd1;	
	
	
	private AdView mAdView;

	protected static KeypadButton keypadButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		


		click = -1;

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.editbuttons);
		
		mAdView = (AdView) findViewById(R.id.adView);

		mAdView.loadAd(new AdRequest.Builder().build());

		Button confirm=(Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
finish();
                // Perform action on click
            }});
		Button reset=(Button) findViewById(R.id.reset);
		reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	DefaulButtonsPosition df=new DefaulButtonsPosition();
        		
        			CalculatorGUI.button.putAll(df.onClickGenerateDefault());
        			

        			
        				CalculatorGUI.button2.putAll(df.onLongClickGenerateDefault());
        				
        			for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button2.entrySet()){
        		        Button b=(Button) findViewById((int) entry.getKey());
        		        KeypadButton kb=(KeypadButton) entry.getValue();
        		        TextView t=(TextView) findViewById(ButtonToTextView.mapButtonToTextView.get((int) entry.getKey()));
        		        t.setText(kb.getText());
        		   	        

        		}
        			for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button.entrySet()){
        			        Button b=(Button) findViewById((int) entry.getKey());
        			        KeypadButton kb=(KeypadButton) entry.getValue();
        			        b.setText(kb.getText());
        					switch(kb.getmCategory())
        					{
        					case MEMORYBUFFER:
        						//@style/lighterButtonInRow
        						b.setBackgroundResource(R.drawable.lighter_button_background);
        						break;	
        					case CLEAR:
        						b.setBackgroundResource(R.drawable.lighter_button_background);
        						break;	
        					case NUMBER:
        						b.setBackgroundResource(R.drawable.darker_button_background);
        						break;
        					case OPERATOR:
        						
        						b.setBackgroundResource(R.drawable.lighter_button_background);
        						break;
        					case OTHER:
        						b.setBackgroundResource(R.drawable.lighter_button_background);
        						break;
        					case RESULT:
        						b.setBackgroundResource(R.drawable.red_button_background);
        						break;
        					case DUMMY:
        						b.setBackgroundResource(R.drawable.lighter_button_background);
        						break;
        					case GRAPH:
        						b.setBackgroundResource(R.drawable.wood_button_background_pressed);
        						break;						
        					default:
        						b.setBackgroundResource(R.drawable.lighter_button_background);
        						break;
        					}
        			        arButton.put((int) entry.getKey(),b);
        			        

        			}
                // Perform action on click
            }});		
		rLayout.add(R.id.button);
		rLayout.add(R.id.button1);
		rLayout.add(R.id.button2);
		rLayout.add(R.id.button3);
		rLayout.add(R.id.button4);
		rLayout.add(R.id.button5);
		rLayout.add(R.id.button6);
		rLayout.add(R.id.button7);
		rLayout.add(R.id.button8);
		rLayout.add(R.id.button9);
		rLayout.add(R.id.button10);
		rLayout.add(R.id.button11);
		rLayout.add(R.id.button12);
		rLayout.add(R.id.button13);
		rLayout.add(R.id.button14);
		rLayout.add(R.id.button15);
		rLayout.add(R.id.button16);
		rLayout.add(R.id.button17);
		rLayout.add(R.id.button18);
		rLayout.add(R.id.button19);
		rLayout.add(R.id.button20);
		rLayout.add(R.id.button21);
		rLayout.add(R.id.button22);
		rLayout.add(R.id.button23);
		rLayout.add(R.id.button24);
		rLayout.add(R.id.button25);
		rLayout.add(R.id.button26);
		rLayout.add(R.id.button27);
		rLayout.add(R.id.button28);
		rLayout.add(R.id.button29);
		rLayout.add(R.id.button30);
		rLayout.add(R.id.button31);
		rLayout.add(R.id.button31);
		rLayout.add(R.id.button32);
		rLayout.add(R.id.button33);
		rLayout.add(R.id.button34);
		rLayout.add(R.id.button35);
		rLayout.add(R.id.button36);
		rLayout.add(R.id.button37);
		rLayout.add(R.id.button38);
		DefaulButtonsPosition df=new DefaulButtonsPosition();
		if(CalculatorGUI.button.size()==0){
			CalculatorGUI.button.putAll(df.onClickGenerateDefault());
			}

			if(CalculatorGUI.button2.size()==0){
				CalculatorGUI.button2.putAll(df.onLongClickGenerateDefault());
				}
			for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button2.entrySet()){
		        Button b=(Button) findViewById((int) entry.getKey());
		        KeypadButton kb=(KeypadButton) entry.getValue();
		        TextView t=(TextView) findViewById(ButtonToTextView.mapButtonToTextView.get((int) entry.getKey()));
		        t.setText(kb.getText());
		   	        

		}
			for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button.entrySet()){
			        Button b=(Button) findViewById((int) entry.getKey());
			        KeypadButton kb=(KeypadButton) entry.getValue();
			        b.setText(kb.getText());
			        arButton.put((int) entry.getKey(),b);
			        

			}
/*		for (Integer t : rLayout)

			arButton.put(t, (Button) findViewById(t));*/

		cd = new ChooseDialog(this,false);
        cd.setTitle("Choose button");
		cd.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

				if (click != -1) {
					  if(pressButton!=false){
					Button b = (Button) findViewById(PositionButtons.click);
					b.setText(keypadButton.getText());
					switch(keypadButton.getmCategory())
					{
					case MEMORYBUFFER:
						//@style/lighterButtonInRow
						b.setBackgroundResource(R.drawable.lighter_button_background);
						break;	
					case CLEAR:
						b.setBackgroundResource(R.drawable.lighter_button_background);
						break;	
					case NUMBER:
						b.setBackgroundResource(R.drawable.darker_button_background);
						break;
					case OPERATOR:
						
						b.setBackgroundResource(R.drawable.lighter_button_background);
						break;
					case OTHER:
						b.setBackgroundResource(R.drawable.lighter_button_background);
						break;
					case RESULT:
						b.setBackgroundResource(R.drawable.red_button_background);
						break;
					case DUMMY:
						b.setBackgroundResource(R.drawable.lighter_button_background);
						break;
					case GRAPH:
						b.setBackgroundResource(R.drawable.wood_button_background_pressed);
						break;						
					default:
						b.setBackgroundResource(R.drawable.lighter_button_background);
						break;
					}
					PositionButtons.click = -1;
					pressButton=false;
				}}

			}
		});

		cd1 = new ChooseDialog(this,true);
		cd1.setTitle("Choose button");
		cd1.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

				if (click != -1) {
                   if(pressButton!=false){
					Button b = (Button) findViewById(PositionButtons.click);
					TextView t =  (TextView) findViewById(ButtonToTextView.mapButtonToTextView.get(PositionButtons.click));
					t.setText(keypadButton.getText());
					PositionButtons.click = -1;
					pressButton=false;
				}}

			}
		});		
		OnLongClickListener oclLngBtn = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				
				if (v != null) {
					final int id = v.getId();

					switch (id) {

					case R.id.button:
						click = R.id.button;
						cd1.show();
						break;
					case R.id.button1:
						click = R.id.button1;
						cd1.show();
						break;
					case R.id.button2:
						click = R.id.button2;
						cd1.show();
						break;
					case R.id.button3:
						click = R.id.button3;
						cd1.show();
						break;
					case R.id.button4:
						click = R.id.button4;
						cd1.show();
						break;
					case R.id.button5:
						click = R.id.button5;
						cd1.show();
						break;
					case R.id.button6:
						click = R.id.button6;
						cd1.show();
						break;
					case R.id.button7:
						click = R.id.button7;
						cd1.show();
						break;
					case R.id.button8:
						click = R.id.button8;
						cd1.show();
						break;
					case R.id.button9:
						click = R.id.button9;
						cd1.show();
						break;
					case R.id.button10:
						click = R.id.button10;
						cd1.show();
						
						break;
					case R.id.button11:
						click = R.id.button11;
						cd1.show();
						break;
					case R.id.button12:
						click = R.id.button12;
						cd1.show();
						break;
					case R.id.button13:
						click = R.id.button13;
						cd1.show();
						break;
					case R.id.button14:
						click = R.id.button14;
						cd1.show();
						break;
					case R.id.button15:
						click = R.id.button15;
						cd1.show();
						break;
					case R.id.button16:
						click = R.id.button16;
						cd1.show();
						break;
					case R.id.button17:
						click = R.id.button17;
						cd1.show();
						break;
					case R.id.button18:
						click = R.id.button18;
						cd1.show();
						break;
					case R.id.button19:
						click = R.id.button19;
						cd1.show();
						break;
					case R.id.button20:
						click = R.id.button20;
						cd1.show();
						break;
					case R.id.button21:
						click = R.id.button21;
						cd1.show();
						break;
					case R.id.button22:
						click = R.id.button22;
						cd1.show();
						break;
					case R.id.button23:
						click = R.id.button23;
						cd1.show();
						break;
					case R.id.button24:
						click = R.id.button24;
						cd1.show();
						break;
					case R.id.button25:
						click = R.id.button25;
						cd1.show();
						break;
					case R.id.button26:
						click = R.id.button26;
						cd1.show();
						break;
					case R.id.button27:
						click = R.id.button27;
						cd1.show();
						break;
					case R.id.button28:
						click = R.id.button28;
						cd1.show();
						break;
					case R.id.button29:
						click = R.id.button29;
						cd1.show();
						break;
					case R.id.button30:
						click = R.id.button30;
						cd1.show();
						break;
					case R.id.button31:
						click = R.id.button31;
						cd1.show();
						break;
					case R.id.button32:
						click = R.id.button32;
						cd1.show();
						break;
					case R.id.button33:
						click = R.id.button33;
						cd1.show();
						break;
					case R.id.button34:
						click = R.id.button34;
						cd1.show();
						break;
					case R.id.button35:
						click = R.id.button35;
						cd1.show();
						break;
					case R.id.button36:
						click = R.id.button36;
						cd1.show();
						break;
					case R.id.button37:
						click = R.id.button37;
						cd1.show();
						break;
					case R.id.button38:
						click = R.id.button38;
						cd1.show();
						break;

					}

					return true;
				}
				return false;
			}
		};

		OnClickListener oclBtn = new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				if (v != null) {
					final int id = v.getId();
					switch (id) {

					case R.id.button:
						click = R.id.button;
						cd.show();
						break;
					case R.id.button1:
						click = R.id.button1;
						cd.show();
						break;
					case R.id.button2:
						click = R.id.button2;
						cd.show();
						break;
					case R.id.button3:
						click = R.id.button3;
						cd.show();
						break;
					case R.id.button4:
						click = R.id.button4;
						cd.show();
						break;
					case R.id.button5:
						click = R.id.button5;
						cd.show();
						break;
					case R.id.button6:
						click = R.id.button6;
						cd.show();
						break;
					case R.id.button7:
						click = R.id.button7;
						cd.show();
						break;
					case R.id.button8:
						click = R.id.button8;
						cd.show();
						break;
					case R.id.button9:
						click = R.id.button9;
						cd.show();
						break;
					case R.id.button10:
						click = R.id.button10;
						cd.show();
						
						break;
					case R.id.button11:
						click = R.id.button11;
						cd.show();
						break;
					case R.id.button12:
						click = R.id.button12;
						cd.show();
						break;
					case R.id.button13:
						click = R.id.button13;
						cd.show();
						break;
					case R.id.button14:
						click = R.id.button14;
						cd.show();
						break;
					case R.id.button15:
						click = R.id.button15;
						cd.show();
						break;
					case R.id.button16:
						click = R.id.button16;
						cd.show();
						break;
					case R.id.button17:
						click = R.id.button17;
						cd.show();
						break;
					case R.id.button18:
						click = R.id.button18;
						cd.show();
						break;
					case R.id.button19:
						click = R.id.button19;
						cd.show();
						break;
					case R.id.button20:
						click = R.id.button20;
						cd.show();
						break;
					case R.id.button21:
						click = R.id.button21;
						cd.show();
						break;
					case R.id.button22:
						click = R.id.button22;
						cd.show();
						break;
					case R.id.button23:
						click = R.id.button23;
						cd.show();
						break;
					case R.id.button24:
						click = R.id.button24;
						cd.show();
						break;
					case R.id.button25:
						click = R.id.button25;
						cd.show();
						break;
					case R.id.button26:
						click = R.id.button26;
						cd.show();
						break;
					case R.id.button27:
						click = R.id.button27;
						cd.show();
						break;
					case R.id.button28:
						click = R.id.button28;
						cd.show();
						break;
					case R.id.button29:
						click = R.id.button29;
						break;
					case R.id.button30:
						click = R.id.button30;
						cd.show();
						break;
					case R.id.button31:
						click = R.id.button31;
						cd.show();
						break;
					case R.id.button32:
						click = R.id.button32;
						cd.show();
						break;
					case R.id.button33:
						click = R.id.button33;
						cd.show();
						break;
					case R.id.button34:
						click = R.id.button34;
						cd.show();
						break;
					case R.id.button35:
						click = R.id.button35;
						cd.show();
						break;
					case R.id.button36:
						click = R.id.button36;
						cd.show();
						break;
					case R.id.button37:
						click = R.id.button37;
						cd.show();
						break;
					case R.id.button38:
						click = R.id.button38;
						cd.show();
						break;

					}

					
				}
				
			}}
			
		;

		for (Button t : arButton.values()) {

			// t.setOnTouchListener(new MyTouchListener());
			if (t != null) {
				t.setOnClickListener(oclBtn);
				t.setOnLongClickListener(oclLngBtn);
			}
			;
		}


	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}



}
