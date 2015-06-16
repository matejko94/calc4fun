package com.calculator.mycalculator;

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class DecimalSeparator extends Dialog {
 Context context;
	public DecimalSeparator(Context context) {
		super(context);
		this.context=context;
		// TODO Auto-generated constructor stub
	}
	
	String s;
	
	boolean downRadioDecimalSep=false;
	boolean downRadioSep=false;
	public RadioGroup radioGroup,radioSep;
	static String locilo;
	public String t;
	String loc;
String decimal;
	int lastClick;

	
	private static String groupText(String str, int cnt, String sep) {
		String ret = "";
		int i = 0, a = 0, z = str.lastIndexOf(locilo);
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
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.decimalseparator);
			final TextView tv=(TextView) findViewById(R.id.numbers);
			locilo=".";
			
			s="123456.789";
			tv.setText(s);

			
			
			radioGroup = (RadioGroup) findViewById(R.id.radioDecimalSep);        
		    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		    {
		        public void onCheckedChanged(RadioGroup group, int checkedId) {
		            // checkedId is the RadioButton selected
		        	 switch(checkedId) {
		             case R.id.decimalSepNone:
		            	 
		            	 tv.setText(groupText(s,3,""));
		            	 loc="";
		            	 lastClick=R.id.decimalSepNone;
		            	 
		                 break;
		             case R.id.decimalSepComma:
		            	
		            		// radioSep.check(R.id.PointSepPoint);
		            	 if(radioSep.getCheckedRadioButtonId()!=R.id.PointSepComma) {
			            	 tv.setText( groupText(s,3,","));
			            	 loc=",";
			            	 lastClick=R.id.decimalSepComma;
		            	 // 'Accident' checked
		            	 }
		                 break;
		             case R.id.decimalSepPoint:
		            	
		            	 
		            	// radioSep.check(R.id.PointSepComma);
		            	 
		            		if(radioSep.getCheckedRadioButtonId()!=R.id.PointSepPoint) {
			            	 tv.setText( groupText(s,3,"."));
			            	 loc=".";
			            	 lastClick=R.id.decimalSepPoint;
		            		}
		           

		            	 break;
		             case R.id.DecimalSepSpace:
		            		
		            	 tv.setText(groupText(s,3," "));
		            	 loc=" ";
		            	 lastClick=R.id.DecimalSepSpace;
		                 // 'Concern' checked
		            	
		                 break;
		        	 }
		        }
		    });

		    
			radioSep = (RadioGroup) findViewById(R.id.radioSep);        
			radioSep.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		    {
		        public void onCheckedChanged(RadioGroup group, int checkedId) {
		            // checkedId is the RadioButton selected
		        	 switch(checkedId) {
		             case R.id.PointSepComma:
		      
		            	 tv.setText(s.replace('.',','));
		            	 
		     			locilo=",";
		     			 if(lastClick==R.id.decimalSepComma)
		            		 lastClick=R.id.decimalSepPoint;
		     			radioGroup.check(lastClick);

		            	 break;
		             case R.id.PointSepPoint:
		            	 tv.setText(s.replace(',','.'));
		            	 locilo=".";
		            	 if(lastClick==R.id.decimalSepPoint)
		            		 lastClick=R.id.decimalSepComma;
		            	 radioGroup.check(lastClick); 
		            	 break;
		        	 }
		        }
		    });			
	
			radioGroup.check(R.id.decimalSepNone);
			radioSep.check(R.id.PointSepPoint);
			Button b=(Button) findViewById(R.id.button1);
			b.setOnClickListener(new View.OnClickListener() {
				


				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CalculatorGUI.digit_sep=loc;
					CalculatorGUI.decimal_sep=locilo;
					KeypadButton.DECIMAL_SEP.setText(locilo);
					for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button.entrySet()) {
						Button b = CalculatorGUI.arButton.get(entry.getKey());
						KeypadButton kb = entry.getValue();
						b.setText(CalculatorGUI.button.get(entry.getKey()).getText());
					}
					
					dismiss();
				}
			});
		}
	
	

}
