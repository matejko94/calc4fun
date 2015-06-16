package com.calculator.mycalculator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import com.Converter.myConverter.Unit.EnotaMainHendler;
import com.calculator.grap.GraphSettings;
import com.calculator.position.PositionButtons;
import com.example.mycalc.Item;
import com.example.mycalc.ListviewActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorGUI extends Activity {

	private CalculatorAdapter calc;
	public static ArrayList<Item> history;
	public static String decimal_sep,digit_sep; 

	// private AdView add;

	TextView userInputText;
	TextView memoryStack;

	// KeypadAdapter mKeypadAdapter;
	TextView mStackText;
	boolean resetInput = false;
	boolean hasFinalResult = false;
	// parameter clickable zaklene da se lahko zgodi samo longclic ali pa click,
	// pomembno je da se ne moreta zgoditi oba
	boolean clickable;
	// stevilski sistem
	int system;
	/*
	 * 0-bin 1-oct 2-decimal 3-hex
	 */

	int sdk = android.os.Build.VERSION.SDK_INT;
	boolean digit_grop;
	/*
	 * true pomeni da digitalno grupiramo, false pa ne ::.
	 */
	public static boolean radian;
	/*
	 * radian = true -> so radiani radian=false -> stopinje
	 */

	public static int histSize;
	public static int screenSize;
	String mDecimalSeperator;
	double memoryValue = Double.NaN;

	// seznam gumbov in seznam idjev do gumbov
	public static Map<Integer, Button> arButton = new HashMap<>();
	public ArrayList<Integer> rLayout = new ArrayList<>();

	public Map<Integer, KeypadButton> onClicDefault = new HashMap<>();

	public static Map<Integer, KeypadButton> button = new HashMap<>();
	public static Map<Integer, KeypadButton> button2 = new HashMap<>();
	// nastavitve kalkulatorja
	SharedPreferences prefs;
	Point p;
	private PreferenceChangeListener mPreferenceListener = null;

	private AdView mAdView;

	public static final String PREFS_NAME = "MyPrefsFile";
	Vibrator vibe ;

	/**
	 * Called when the activity is first created.
	 * 
	 * @author senozetnik
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		decimal_sep=".";
		digit_sep="";

		screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;

		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			Toast.makeText(this, "Normal screen", Toast.LENGTH_LONG).show();
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			Toast.makeText(this, "Small screen", Toast.LENGTH_LONG).show();
			break;
		case Configuration.SCREENLAYOUT_SIZE_XLARGE:
			Toast.makeText(this, "Xlarg screen", Toast.LENGTH_LONG).show();
			break;
		default:
			Toast.makeText(this,
					"Screen size is neither large, normal or small",
					Toast.LENGTH_LONG).show();
		}
		
		

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		vibe= (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE) ;

		calc = new CalculatorAdapter(getApplicationContext());
		history = new ArrayList<>();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		Set<String> s = new HashSet<>();
		s = settings.getStringSet("history", s);
		history = new ArrayList<>();

		for (String str : s) {
			
			if (str != null) {
				String[] sArray = str.split(Pattern.quote("**"));
				history.add(new Item(Integer.parseInt(sArray[0]), sArray[1],
						sArray[2]));
			}
			// s.add(i+"**"+history.get(i));
		}

		Collections.sort(history, new Comparator<Item>() {

			@Override
			public int compare(Item lhs, Item rhs) {
				// TODO Auto-generated method stub
				return lhs.getId() - rhs.getId();
			}

		});

		system = 2;
		clickable = true;

		setContentView(R.layout.customview);

		mAdView = (AdView) findViewById(R.id.adView);

		mAdView.loadAd(new AdRequest.Builder().build());

		DecimalFormat currencyFormatter = (DecimalFormat) NumberFormat
				.getInstance();
		char decimalSeperator = currencyFormatter.getDecimalFormatSymbols()
				.getDecimalSeparator();
		mDecimalSeperator = Character.toString(decimalSeperator);

		memoryStack = (TextView) findViewById(R.id.txtMemory);
		userInputText = (TextView) findViewById(R.id.txtInput);
		userInputText.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					p = new Point();
					p.x = (int) event.getX();
					p.y = (int) event.getY();
				}

				else if (event.getAction() == MotionEvent.ACTION_UP) {

					if (p != null)
						showPopup(CalculatorGUI.this, p);

				}

				return true;
			}
		});

		userInputText.setText("0");
		mStackText = (TextView) findViewById(R.id.txtStack);
		DefaulButtonsPosition df = new DefaulButtonsPosition();

		
		Set<String> sArray = new HashSet<>();
		for(int i=0;i<39;i++){
			String a=settings.getString("b"+String.valueOf(i), null);
			if(a!=null)
		 	sArray.add(a);
			
		}
	//	sArray = settings.getStringSet("ButtonOnClick", s);
	//	int s009=sArray.size();
		
		if(sArray.size()!=0){
			
        for(String str:sArray){
        	Log.e(str,str);
        	String[] pom=str.split(" ");
        	button.put(Integer.parseInt(pom[0]),KeypadButton.valueOf(pom[1]));
        	
        }}
	//	sArray = settings.getStringSet("OnClickLong", s);
		sArray = new HashSet<>();
		for(int i=0;i<39;i++){
			String a=settings.getString("t"+String.valueOf(i), null);
			if(a!=null)
		 	sArray.add(a);
			
		}
		
		if(sArray.size()!=0){
			
        for(String str:sArray){
        	Log.e(str,str);
        	String[] pom=str.split(" ");
        	button2.put(Integer.parseInt(pom[0]),KeypadButton.valueOf(pom[1]));
        	
        }	
		}
        
		if (button.size() == 0) {
			button.putAll(df.onClickGenerateDefault());
		}

		if (button2.size() == 0) {
			button2.putAll(df.onLongClickGenerateDefault());
		}
		
		
		/*
		 * 		editor.putStringSet("historyOnClick",buttonOnClick);
		Set<String> buttonOnClickLong = new HashSet<>();
		for (Map.Entry<Integer, KeypadButton> entry : button2.entrySet())
		{	MapToSSet m=new MapToSSet(entry.getKey(),entry.getValue());
			buttonOnClick.add(m.toString());
		}
		editor.putStringSet("historyOnClickLong", buttonOnClickLong);
		 * */
		///dobimo podatke
	
		
		for (Map.Entry<Integer, KeypadButton> entry : button2.entrySet()) {
			Button b = (Button) findViewById((int) entry.getKey());
			KeypadButton kb = (KeypadButton) entry.getValue();
			TextView t = (TextView) findViewById(ButtonToTextView.mapButtonToTextView
					.get((int) entry.getKey()));
			t.setText(kb.getText());

		}
		for (Map.Entry<Integer, KeypadButton> entry : button.entrySet()) {
			Button b = (Button) findViewById((int) entry.getKey());
			KeypadButton kb = (KeypadButton) entry.getValue();
			b.setText(kb.getText());
			arButton.put((int) entry.getKey(), b);

		}

		OnLongClickListener oclLngBtn = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				vibe.vibrate(75);
				if (v != null) {
					final int id = v.getId();
					KeypadButton inputstring = null;

					switch (id) {
					case R.id.button:
						inputstring = button2.get(R.id.button);

						clickable = false;
						break;
					case R.id.button1:
						inputstring = button2.get(R.id.button1);

						clickable = false;
						break;
					case R.id.button2:
						inputstring = button2.get(R.id.button2);

						clickable = false;
						break;
					case R.id.button3:
						inputstring = button2.get(R.id.button3);

						clickable = false;
						break;
					case R.id.button4:
						inputstring = button2.get(R.id.button4);

						clickable = false;
						break;
					case R.id.button5:
						inputstring = button2.get(R.id.button5);

						clickable = false;
						break;
					case R.id.button6:
						inputstring = button2.get(R.id.button6);

						clickable = false;
						break;
					case R.id.button7:
						inputstring = button2.get(R.id.button7);

						clickable = false;
						break;
					case R.id.button8:
						inputstring = button2.get(R.id.button8);

						clickable = false;
						break;
					case R.id.button9:
						inputstring = button2.get(R.id.button9);

						clickable = false;
						break;
					case R.id.button10:
						inputstring = button2.get(R.id.button10);

						clickable = false;
						break;
					case R.id.button11:
						inputstring = button2.get(R.id.button11);
						ProcessKeypadInput(inputstring);
						clickable = false;
						break;
					case R.id.button12:
						inputstring = button2.get(R.id.button12);

						clickable = false;
						break;
					case R.id.button13:
						inputstring = button2.get(R.id.button13);

						clickable = false;
						break;
					case R.id.button14:
						inputstring = button2.get(R.id.button14);

						clickable = false;
						break;
					case R.id.button15:
						inputstring = button2.get(R.id.button15);

						clickable = false;
						break;
					case R.id.button16:
						inputstring = button2.get(R.id.button16);

						clickable = false;
						break;
					case R.id.button17:
						inputstring = button2.get(R.id.button17);

						clickable = false;
						break;
					case R.id.button18:
						inputstring = button2.get(R.id.button18);

						clickable = false;
						break;
					case R.id.button19:
						inputstring = button2.get(R.id.button19);
						;
						clickable = false;
						break;
					case R.id.button20:
						inputstring = button2.get(R.id.button20);

						clickable = false;
						break;
					case R.id.button21:
						inputstring = button2.get(R.id.button21);

						clickable = false;
						break;
					case R.id.button22:
						inputstring = button2.get(R.id.button22);

						clickable = false;
						break;
					case R.id.button23:
						inputstring = button2.get(R.id.button23);

						clickable = false;
						break;
					case R.id.button24:
						inputstring = button2.get(R.id.button24);

						clickable = false;
						break;
					case R.id.button25:
						inputstring = button2.get(R.id.button25);

						clickable = false;
						break;
					case R.id.button26:
						inputstring = button2.get(R.id.button26);

						clickable = false;
						break;
					case R.id.button27:
						inputstring = button2.get(R.id.button27);

						clickable = false;
						break;
					case R.id.button28:
						inputstring = button2.get(R.id.button28);

						clickable = false;
						break;
					case R.id.button29:
						inputstring = button2.get(R.id.button29);

						clickable = false;
						break;
					case R.id.button30:
						inputstring = button2.get(R.id.button30);

						clickable = false;
						break;
					case R.id.button31:
						inputstring = button2.get(R.id.button31);

						clickable = false;
						break;
					case R.id.button32:
						inputstring = button2.get(R.id.button32);

						clickable = false;
						break;
					case R.id.button33:
						inputstring = button2.get(R.id.button33);

						clickable = false;
						break;
					case R.id.button34:
						inputstring = button2.get(R.id.button34);

						clickable = false;
						break;
					case R.id.button35:
						inputstring = button2.get(R.id.button35);

						clickable = false;
						break;
					case R.id.button36:
						inputstring = button2.get(R.id.button36);

						clickable = false;
						break;
					case R.id.button37:
						inputstring = button2.get(R.id.button37);

						clickable = false;
						break;
					case R.id.button38:
						inputstring = button2.get(R.id.button38);

						clickable = false;
						break;

					}

					if (inputstring == KeypadButton.CONVERT) {
						Intent i = new Intent(CalculatorGUI.this,
								EnotaMainHendler.class);
						startActivity(i);
					} else if (inputstring == KeypadButton.SETTINGS) {
                         createAlretDialog();
					} else if (inputstring == KeypadButton.A) {
						if (system == 3) {
							inputstring = KeypadButton.A;
							ProcessKeypadInput(inputstring);
							// clickable = false;
						}
					} else if (inputstring == KeypadButton.B) {

						if (system == 3) {
							inputstring = KeypadButton.B;
							ProcessKeypadInput(inputstring);
							// clickable = false;
						}
					} else if (inputstring == KeypadButton.C) {
						if (system == 3) {
							inputstring = KeypadButton.C;
							ProcessKeypadInput(inputstring);
							// clickable = false;
						}
					} else if (inputstring == KeypadButton.D) {

						if (system == 3) {
							inputstring = KeypadButton.D;
							ProcessKeypadInput(inputstring);
							// clickable = false;
						}
					} else if (inputstring == KeypadButton.E) {
						if (system == 3) {
							inputstring = KeypadButton.E;
							ProcessKeypadInput(inputstring);
							// clickable = false;
						}
					} else if (inputstring == KeypadButton.F) {
						if (system == 3) {
							inputstring = KeypadButton.F;
							ProcessKeypadInput(inputstring);
							// clickable = false;
						}
					} else if (inputstring == KeypadButton.DEC
							|| inputstring == KeypadButton.OCT
							|| inputstring == KeypadButton.BIN
							|| inputstring == KeypadButton.HEX) {
						TextView pom = null;
						Integer idButton = -1;
						int[] idArray = new int[16];

						for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button2
								.entrySet()) {

							KeypadButton kb = (KeypadButton) entry.getValue();
							if (kb == inputstring) {
								pom = (TextView) findViewById(ButtonToTextView.mapButtonToTextView
										.get((int) entry.getKey()));
								idButton = (int) entry.getKey();
							} else if (KeypadButton.ZERO == kb) {
								idArray[0] = (int) entry.getKey();
							} else if (KeypadButton.ONE == kb) {
								idArray[1] = (int) entry.getKey();
							} else if (KeypadButton.TWO == kb) {
								idArray[2] = (int) entry.getKey();
							} else if (KeypadButton.THREE == kb) {
								idArray[3] = (int) entry.getKey();
							} else if (KeypadButton.FOUR == kb) {
								idArray[4] = (int) entry.getKey();
							} else if (KeypadButton.FIVE == kb) {
								idArray[5] = (int) entry.getKey();
							} else if (KeypadButton.FIVE == kb) {
								idArray[5] = (int) entry.getKey();
							} else if (KeypadButton.SIX == kb) {
								idArray[6] = (int) entry.getKey();
							} else if (KeypadButton.SEVEN == kb) {
								idArray[7] = (int) entry.getKey();
							} else if (KeypadButton.EIGHT == kb) {
								idArray[8] = (int) entry.getKey();
							} else if (KeypadButton.NINE == kb) {
								idArray[9] = (int) entry.getKey();
							} else if (KeypadButton.A == kb) {
								idArray[10] = (int) entry.getKey();
							} else if (KeypadButton.B == kb) {
								idArray[11] = (int) entry.getKey();
							} else if (KeypadButton.C == kb) {
								idArray[12] = (int) entry.getKey();
							} else if (KeypadButton.D == kb) {
								idArray[13] = (int) entry.getKey();
							} else if (KeypadButton.E == kb) {
								idArray[14] = (int) entry.getKey();
							} else if (KeypadButton.F == kb) {
								idArray[15] = (int) entry.getKey();
							}

						}

						for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button
								.entrySet()) {

							KeypadButton kb = (KeypadButton) entry.getValue();
							if (KeypadButton.ZERO == kb) {
								idArray[0] = (int) entry.getKey();
							} else if (KeypadButton.ONE == kb) {
								idArray[1] = (int) entry.getKey();
							} else if (KeypadButton.TWO == kb) {
								idArray[2] = (int) entry.getKey();
							} else if (KeypadButton.THREE == kb) {
								idArray[3] = (int) entry.getKey();
							} else if (KeypadButton.FOUR == kb) {
								idArray[4] = (int) entry.getKey();
							} else if (KeypadButton.FIVE == kb) {
								idArray[5] = (int) entry.getKey();
							} else if (KeypadButton.FIVE == kb) {
								idArray[5] = (int) entry.getKey();
							} else if (KeypadButton.SIX == kb) {
								idArray[6] = (int) entry.getKey();
							} else if (KeypadButton.SEVEN == kb) {
								idArray[7] = (int) entry.getKey();
							} else if (KeypadButton.EIGHT == kb) {
								idArray[8] = (int) entry.getKey();
							} else if (KeypadButton.NINE == kb) {
								idArray[9] = (int) entry.getKey();
							} else if (KeypadButton.A == kb) {
								idArray[10] = (int) entry.getKey();
							} else if (KeypadButton.B == kb) {
								idArray[11] = (int) entry.getKey();
							} else if (KeypadButton.C == kb) {
								idArray[12] = (int) entry.getKey();
							} else if (KeypadButton.D == kb) {
								idArray[13] = (int) entry.getKey();
							} else if (KeypadButton.E == kb) {
								idArray[14] = (int) entry.getKey();
							} else if (KeypadButton.F == kb) {
								idArray[15] = (int) entry.getKey();
							}

						}

						// pom = arButton.get(idButton);

						// arButton.remove(idButton);

						// arButton.remove(idButton);

						system = system + 1;
						if (system > 3)
							system = 0;

						if (system == 0) {
							inputstring = KeypadButton.BIN;
							pom.setText("BIN");

							// arButton.put(idButton, pom);

							Button b = disableButton(arButton
									.remove(idArray[2]));
							arButton.put(idArray[2], b);
							b = disableButton(arButton.remove(idArray[3]));
							arButton.put(idArray[3], b);
							b = disableButton(arButton.remove(idArray[4]));
							arButton.put(idArray[4], b);
							b = disableButton(arButton.remove(idArray[5]));
							arButton.put(idArray[5], b);
							b = disableButton(arButton.remove(idArray[6]));
							arButton.put(idArray[6], b);
							b = disableButton(arButton.remove(idArray[7]));
							arButton.put(idArray[7], b);
							b = disableButton(arButton.remove(idArray[8]));
							arButton.put(idArray[8], b);
							b = disableButton(arButton.remove(idArray[9]));
							arButton.put(idArray[9], b);
							b = disableButton(arButton.remove(idArray[10]));
							arButton.put(idArray[10], b);
							b = disableButton(arButton.remove(idArray[11]));
							arButton.put(idArray[11], b);
							b = disableButton(arButton.remove(idArray[12]));
							arButton.put(idArray[12], b);
							b = disableButton(arButton.remove(idArray[13]));
							arButton.put(idArray[13], b);
							b = disableButton(arButton.remove(idArray[14]));
							arButton.put(idArray[14], b);
							b = disableButton(arButton.remove(idArray[15]));
							arButton.put(idArray[15], b);

						} else if (system == 1) {
							inputstring = KeypadButton.OCT;
							pom.setText("OCT");
							// arButton.put(idButton, pom);
							Button b = enableButton(idArray[2],
									arButton.remove(idArray[2]));
							arButton.put(idArray[2], b);
							b = enableButton(idArray[3],
									arButton.remove(idArray[3]));
							arButton.put(idArray[3], b);
							b = enableButton(R.id.four,
									arButton.remove(idArray[4]));
							arButton.put(idArray[4], b);
							b = enableButton(idArray[5],
									arButton.remove(idArray[5]));
							arButton.put(idArray[5], b);
							b = enableButton(idArray[6],
									arButton.remove(idArray[6]));
							arButton.put(idArray[6], b);
							b = enableButton(idArray[7],
									arButton.remove(idArray[7]));
							arButton.put(idArray[7], b);
							b = disableButton(arButton.remove(idArray[8]));
							arButton.put(idArray[8], b);
							b = disableButton(arButton.remove(idArray[9]));
							arButton.put(idArray[9], b);
							b = disableButton(arButton.remove(idArray[10]));
							arButton.put(idArray[10], b);
							b = disableButton(arButton.remove(idArray[11]));
							arButton.put(idArray[11], b);
							b = disableButton(arButton.remove(idArray[12]));
							arButton.put(idArray[12], b);
							b = disableButton(arButton.remove(idArray[13]));
							arButton.put(idArray[13], b);
							b = disableButton(arButton.remove(idArray[14]));
							arButton.put(idArray[14], b);
							b = disableButton(arButton.remove(idArray[15]));
							arButton.put(idArray[15], b);

						} else if (system == 2) {
							inputstring = KeypadButton.DEC;
							pom.setText("DEC");
							// arButton.put(idButton, pom);

							Button b = enableButton(idArray[2],
									arButton.remove(idArray[2]));
							arButton.put(idArray[2], b);
							b = enableButton(idArray[3],
									arButton.remove(idArray[3]));
							arButton.put(idArray[3], b);
							b = enableButton(idArray[4],
									arButton.remove(idArray[4]));
							arButton.put(idArray[4], b);
							b = enableButton(idArray[5],
									arButton.remove(idArray[5]));
							arButton.put(idArray[5], b);
							b = enableButton(idArray[6],
									arButton.remove(idArray[6]));
							arButton.put(idArray[6], b);
							b = enableButton(idArray[7],
									arButton.remove(idArray[7]));
							arButton.put(idArray[7], b);
							b = enableButton(idArray[8],
									arButton.remove(idArray[8]));
							arButton.put(idArray[8], b);
							b = enableButton(idArray[9],
									arButton.remove(idArray[9]));
							arButton.put(idArray[9], b);
							b = disableButton(arButton.remove(idArray[10]));
							arButton.put(idArray[10], b);
							b = disableButton(arButton.remove(idArray[11]));
							arButton.put(idArray[11], b);
							b = disableButton(arButton.remove(idArray[12]));
							arButton.put(idArray[12], b);
							b = disableButton(arButton.remove(idArray[13]));
							arButton.put(idArray[13], b);
							b = disableButton(arButton.remove(idArray[14]));
							arButton.put(idArray[14], b);
							b = disableButton(arButton.remove(idArray[15]));
							arButton.put(idArray[15], b);

						} else if (system == 3) {
							inputstring = KeypadButton.HEX;
							pom.setText("HEX");
							// arButton.put(idButton, pom);
							Button b = enableButton(idArray[2],
									arButton.remove(idArray[2]));
							arButton.put(idArray[2], b);
							b = enableButton(idArray[3],
									arButton.remove(idArray[3]));
							arButton.put(idArray[3], b);
							b = enableButton(idArray[4],
									arButton.remove(idArray[4]));
							arButton.put(idArray[4], b);
							b = enableButton(idArray[5],
									arButton.remove(idArray[5]));
							arButton.put(idArray[5], b);
							b = enableButton(idArray[6],
									arButton.remove(idArray[6]));
							arButton.put(idArray[6], b);
							b = enableButton(idArray[7],
									arButton.remove(idArray[7]));
							arButton.put(idArray[7], b);
							b = enableButton(idArray[8],
									arButton.remove(idArray[8]));
							arButton.put(idArray[8], b);
							b = enableButton(idArray[9],
									arButton.remove(idArray[9]));
							arButton.put(idArray[9], b);
							b = enableButton(idArray[10],
									arButton.remove(idArray[10]));
							arButton.put(idArray[10], b);
							b = enableButton(idArray[11],
									arButton.remove(idArray[11]));
							arButton.put(idArray[11], b);
							b = enableButton(idArray[12],
									arButton.remove(idArray[12]));
							arButton.put(idArray[12], b);
							b = enableButton(idArray[13],
									arButton.remove(idArray[13]));
							arButton.put(idArray[13], b);
							b = enableButton(idArray[14],
									arButton.remove(idArray[14]));
							arButton.put(idArray[14], b);
							b = enableButton(idArray[15],
									arButton.remove(idArray[15]));
							arButton.put(idArray[15], b);
						}

						ProcessKeypadInput(inputstring);
					}

					else

						ProcessKeypadInput(inputstring);

				}
				return false;
			}

			private void showAlertDialog() {
				// TODO Auto-generated method stub
				
			}
		};

		OnClickListener oclBtn = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (clickable) {
					vibe.vibrate(50);
					// TODO Auto-generated method stub
					if (v != null) {
						final int id = v.getId();
						KeypadButton inputstring = null;

						switch (id) {
						case R.id.button:
							inputstring = button.get(R.id.button);
							break;
						case R.id.button1:
							inputstring = button.get(R.id.button1);
							break;
						case R.id.button2:
							inputstring = button.get(R.id.button2);
							break;
						case R.id.button3:
							inputstring = button.get(R.id.button3);
							break;
						case R.id.button4:
							inputstring = button.get(R.id.button4);
							break;
						case R.id.button5:
							inputstring = button.get(R.id.button5);
							break;
						case R.id.button6:
							inputstring = button.get(R.id.button6);
							break;
						case R.id.button7:
							inputstring = button.get(R.id.button7);
							break;
						case R.id.button8:
							inputstring = button.get(R.id.button8);
							break;
						case R.id.button9:
							inputstring = button.get(R.id.button9);
							break;
						case R.id.button10:
							inputstring = button.get(R.id.button10);
							break;
						case R.id.button11:
							inputstring = button.get(R.id.button11);
							break;
						case R.id.button12:
							inputstring = button.get(R.id.button12);
							break;
						case R.id.button13:
							inputstring = button.get(R.id.button13);
							break;
						case R.id.button14:
							inputstring = button.get(R.id.button14);
							break;
						case R.id.button15:
							inputstring = button.get(R.id.button15);
							break;
						case R.id.button16:
							inputstring = button.get(R.id.button16);
							break;
						case R.id.button17:
							inputstring = button.get(R.id.button17);
							break;
						case R.id.button18:
							inputstring = button.get(R.id.button18);
							break;
						case R.id.button19:
							inputstring = button.get(R.id.button19);
							break;
						case R.id.button20:
							inputstring = button.get(R.id.button20);
							break;
						case R.id.button21:
							inputstring = button.get(R.id.button21);
							break;
						case R.id.button22:
							inputstring = button.get(R.id.button22);
							break;
						case R.id.button23:
							inputstring = button.get(R.id.button23);
							break;
						case R.id.button24:
							inputstring = button.get(R.id.button24);
							break;
						case R.id.button25:
							inputstring = button.get(R.id.button25);
							break;
						case R.id.button26:
							inputstring = button.get(R.id.button26);
							break;
						case R.id.button27:
							inputstring = button.get(R.id.button27);
							break;
						case R.id.button28:
							inputstring = button.get(R.id.button28);
							break;
						case R.id.button29:
							inputstring = button.get(R.id.button29);
							break;
						case R.id.button30:
							inputstring = button.get(R.id.button30);
							break;
						case R.id.button31:
							inputstring = button.get(R.id.button31);
							break;
						case R.id.button32:
							inputstring = button.get(R.id.button32);
							break;
						case R.id.button33:
							inputstring = button.get(R.id.button33);
							break;
						case R.id.button34:
							inputstring = button.get(R.id.button34);
							break;
						case R.id.button35:
							inputstring = button.get(R.id.button35);
							break;
						case R.id.button36:
							inputstring = button.get(R.id.button36);
							break;
						case R.id.button37:
							inputstring = button.get(R.id.button37);
							break;
						case R.id.button38:
							inputstring = button.get(R.id.button38);
							break;

						}
						if (inputstring == KeypadButton.CONVERT) {
							Intent i = new Intent(CalculatorGUI.this,
									EnotaMainHendler.class);
							startActivity(i);
						} else if (inputstring == KeypadButton.SETTINGS) {
						


							createAlretDialog();
						} else if (inputstring == KeypadButton.A) {
							if (system == 3) {
								inputstring = KeypadButton.A;
								ProcessKeypadInput(inputstring);
								// clickable = false;
							}
						} else if (inputstring == KeypadButton.B) {

							if (system == 3) {
								inputstring = KeypadButton.B;
								ProcessKeypadInput(inputstring);
								// clickable = false;
							}
						} else if (inputstring == KeypadButton.C) {
							if (system == 3) {
								inputstring = KeypadButton.C;
								ProcessKeypadInput(inputstring);
								// clickable = false;
							}
						} else if (inputstring == KeypadButton.D) {

							if (system == 3) {
								inputstring = KeypadButton.D;
								ProcessKeypadInput(inputstring);
								// clickable = false;
							}
						} else if (inputstring == KeypadButton.E) {
							if (system == 3) {
								inputstring = KeypadButton.E;
								ProcessKeypadInput(inputstring);
								// clickable = false;
							}
						} else if (inputstring == KeypadButton.F) {
							if (system == 3) {
								inputstring = KeypadButton.F;
								ProcessKeypadInput(inputstring);
								// clickable = false;
							}
						} else if (inputstring == KeypadButton.DEC
								|| inputstring == KeypadButton.OCT
								|| inputstring == KeypadButton.BIN
								|| inputstring == KeypadButton.HEX) {
							Button pom = null;
							Integer idButton = -1;
							int[] idArray = new int[16];

							for (Map.Entry<Integer, KeypadButton> entry : CalculatorGUI.button
									.entrySet()) {

								KeypadButton kb = (KeypadButton) entry
										.getValue();
								if (kb == inputstring) {
									pom = (Button) findViewById((int) entry
											.getKey());
									idButton = (int) entry.getKey();
								} else if (KeypadButton.ZERO == kb) {
									idArray[0] = (int) entry.getKey();
								} else if (KeypadButton.ONE == kb) {
									idArray[1] = (int) entry.getKey();
								} else if (KeypadButton.TWO == kb) {
									idArray[2] = (int) entry.getKey();
								} else if (KeypadButton.THREE == kb) {
									idArray[3] = (int) entry.getKey();
								} else if (KeypadButton.FOUR == kb) {
									idArray[4] = (int) entry.getKey();
								} else if (KeypadButton.FIVE == kb) {
									idArray[5] = (int) entry.getKey();
								} else if (KeypadButton.FIVE == kb) {
									idArray[5] = (int) entry.getKey();
								} else if (KeypadButton.SIX == kb) {
									idArray[6] = (int) entry.getKey();
								} else if (KeypadButton.SEVEN == kb) {
									idArray[7] = (int) entry.getKey();
								} else if (KeypadButton.EIGHT == kb) {
									idArray[8] = (int) entry.getKey();
								} else if (KeypadButton.NINE == kb) {
									idArray[9] = (int) entry.getKey();
								} else if (KeypadButton.A == kb) {
									idArray[10] = (int) entry.getKey();
								} else if (KeypadButton.B == kb) {
									idArray[11] = (int) entry.getKey();
								} else if (KeypadButton.C == kb) {
									idArray[12] = (int) entry.getKey();
								} else if (KeypadButton.D == kb) {
									idArray[13] = (int) entry.getKey();
								} else if (KeypadButton.E == kb) {
									idArray[14] = (int) entry.getKey();
								} else if (KeypadButton.F == kb) {
									idArray[15] = (int) entry.getKey();
								}

							}

							// pom = arButton.get(idButton);

							arButton.remove(idButton);

							system = system + 1;
							if (system > 3)
								system = 0;

							if (system == 0) {
								inputstring = KeypadButton.BIN;
								pom.setText("BIN");

								arButton.put(idButton, pom);

								Button b = disableButton(arButton
										.remove(idArray[2]));
								arButton.put(idArray[2], b);
								b = disableButton(arButton.remove(idArray[3]));
								arButton.put(idArray[3], b);
								b = disableButton(arButton.remove(idArray[4]));
								arButton.put(idArray[4], b);
								b = disableButton(arButton.remove(idArray[5]));
								arButton.put(idArray[5], b);
								b = disableButton(arButton.remove(idArray[6]));
								arButton.put(idArray[6], b);
								b = disableButton(arButton.remove(idArray[7]));
								arButton.put(idArray[7], b);
								b = disableButton(arButton.remove(idArray[8]));
								arButton.put(idArray[8], b);
								b = disableButton(arButton.remove(idArray[9]));
								arButton.put(idArray[9], b);
								b = disableButton(arButton.remove(idArray[10]));
								arButton.put(idArray[10], b);
								b = disableButton(arButton.remove(idArray[11]));
								arButton.put(idArray[11], b);
								b = disableButton(arButton.remove(idArray[12]));
								arButton.put(idArray[12], b);
								b = disableButton(arButton.remove(idArray[13]));
								arButton.put(idArray[13], b);
								b = disableButton(arButton.remove(idArray[14]));
								arButton.put(idArray[14], b);
								b = disableButton(arButton.remove(idArray[15]));
								arButton.put(idArray[15], b);

							} else if (system == 1) {
								inputstring = KeypadButton.OCT;
								pom.setText("OCT");
								arButton.put(idButton, pom);
								Button b = enableButton(idArray[2],
										arButton.remove(idArray[2]));
								arButton.put(idArray[2], b);
								b = enableButton(idArray[3],
										arButton.remove(idArray[3]));
								arButton.put(idArray[3], b);
								b = enableButton(R.id.four,
										arButton.remove(idArray[4]));
								arButton.put(idArray[4], b);
								b = enableButton(idArray[5],
										arButton.remove(idArray[5]));
								arButton.put(idArray[5], b);
								b = enableButton(idArray[6],
										arButton.remove(idArray[6]));
								arButton.put(idArray[6], b);
								b = enableButton(idArray[7],
										arButton.remove(idArray[7]));
								arButton.put(idArray[7], b);
								b = disableButton(arButton.remove(idArray[8]));
								arButton.put(idArray[8], b);
								b = disableButton(arButton.remove(idArray[9]));
								arButton.put(idArray[9], b);
								b = disableButton(arButton.remove(idArray[10]));
								arButton.put(idArray[10], b);
								b = disableButton(arButton.remove(idArray[11]));
								arButton.put(idArray[11], b);
								b = disableButton(arButton.remove(idArray[12]));
								arButton.put(idArray[12], b);
								b = disableButton(arButton.remove(idArray[13]));
								arButton.put(idArray[13], b);
								b = disableButton(arButton.remove(idArray[14]));
								arButton.put(idArray[14], b);
								b = disableButton(arButton.remove(idArray[15]));
								arButton.put(idArray[15], b);

							} else if (system == 2) {
								inputstring = KeypadButton.DEC;
								pom.setText("DEC");
								arButton.put(idButton, pom);

								Button b = enableButton(idArray[2],
										arButton.remove(idArray[2]));
								arButton.put(idArray[2], b);
								b = enableButton(idArray[3],
										arButton.remove(idArray[3]));
								arButton.put(idArray[3], b);
								b = enableButton(idArray[4],
										arButton.remove(idArray[4]));
								arButton.put(idArray[4], b);
								b = enableButton(idArray[5],
										arButton.remove(idArray[5]));
								arButton.put(idArray[5], b);
								b = enableButton(idArray[6],
										arButton.remove(idArray[6]));
								arButton.put(idArray[6], b);
								b = enableButton(idArray[7],
										arButton.remove(idArray[7]));
								arButton.put(idArray[7], b);
								b = enableButton(idArray[8],
										arButton.remove(idArray[8]));
								arButton.put(idArray[8], b);
								b = enableButton(idArray[9],
										arButton.remove(idArray[9]));
								arButton.put(idArray[9], b);
								b = disableButton(arButton.remove(idArray[10]));
								arButton.put(idArray[10], b);
								b = disableButton(arButton.remove(idArray[11]));
								arButton.put(idArray[11], b);
								b = disableButton(arButton.remove(idArray[12]));
								arButton.put(idArray[12], b);
								b = disableButton(arButton.remove(idArray[13]));
								arButton.put(idArray[13], b);
								b = disableButton(arButton.remove(idArray[14]));
								arButton.put(idArray[14], b);
								b = disableButton(arButton.remove(idArray[15]));
								arButton.put(idArray[15], b);

							} else if (system == 3) {
								inputstring = KeypadButton.HEX;
								pom.setText("HEX");
								arButton.put(idButton, pom);
								Button b = enableButton(idArray[2],
										arButton.remove(idArray[2]));
								arButton.put(idArray[2], b);
								b = enableButton(idArray[3],
										arButton.remove(idArray[3]));
								arButton.put(idArray[3], b);
								b = enableButton(idArray[4],
										arButton.remove(idArray[4]));
								arButton.put(idArray[4], b);
								b = enableButton(idArray[5],
										arButton.remove(idArray[5]));
								arButton.put(idArray[5], b);
								b = enableButton(idArray[6],
										arButton.remove(idArray[6]));
								arButton.put(idArray[6], b);
								b = enableButton(idArray[7],
										arButton.remove(idArray[7]));
								arButton.put(idArray[7], b);
								b = enableButton(idArray[8],
										arButton.remove(idArray[8]));
								arButton.put(idArray[8], b);
								b = enableButton(idArray[9],
										arButton.remove(idArray[9]));
								arButton.put(idArray[9], b);
								b = enableButton(idArray[10],
										arButton.remove(idArray[10]));
								arButton.put(idArray[10], b);
								b = enableButton(idArray[11],
										arButton.remove(idArray[11]));
								arButton.put(idArray[11], b);
								b = enableButton(idArray[12],
										arButton.remove(idArray[12]));
								arButton.put(idArray[12], b);
								b = enableButton(idArray[13],
										arButton.remove(idArray[13]));
								arButton.put(idArray[13], b);
								b = enableButton(idArray[14],
										arButton.remove(idArray[14]));
								arButton.put(idArray[14], b);
								b = enableButton(idArray[15],
										arButton.remove(idArray[15]));
								arButton.put(idArray[15], b);
							}

							ProcessKeypadInput(inputstring);
						}

						else

							ProcessKeypadInput(inputstring);

					}
				} else {
					clickable = true;
				}

			}
		};

		for (Button t : arButton.values()) {
			t.setOnClickListener(oclBtn);
			t.setOnLongClickListener(oclLngBtn);
			// t.setOnTouchListener(new MyTouchListener());;
		}

		// /////////////////////////////////////////////////////
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		applySettings();

		mPreferenceListener = new PreferenceChangeListener();
		prefs.registerOnSharedPreferenceChangeListener(mPreferenceListener);
		// ///////////////////////////////////////////////

	}

	// Handle preferences changes
	private class PreferenceChangeListener implements
			OnSharedPreferenceChangeListener {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs1,
				String key) {
			applySettings();

		}
	}

	protected void createAlretDialog() {
		// TODO Auto-generated method stub
		final CharSequence[] items = {
		getString(R.string.settings)
		,getString(R.string.history)
		,getString(R.string.help)
		,getString(R.string.edit)
		,getString(R.string.unit_converter_title)
	//	,getString(R.string.decimal_separator)
	//	,getString(R.string.currency)
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorGUI.this);
		builder.setTitle("Choose item");
		builder.setCancelable(true);
		//builder.setIcon(R.drawable.icon);
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
                Intent i;
                switch(item) {
                    case 0:
                    	i = new Intent(CalculatorGUI.this, SettingsActivity.class);
            		//	i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    	startActivity(i);
                        
                        break;
                    case 1:
                    	 i = new Intent(CalculatorGUI.this, ListviewActivity.class);
            			startActivity(i);
            			break;
                       
                    case 2:
                    	AlertDialog.Builder bu = new AlertDialog.Builder(CalculatorGUI.this);
                    	bu.setMessage(Html.fromHtml(Help.s));
                    	bu.setCancelable(true);
                    	bu.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    	        @Override
                    	        public void onClick(DialogInterface dialog, int id) {
                    	        // TODO Auto-generated method stub
                    	                    dialog.cancel();
                    	                        }
                    	                        });
                    	                bu.create().show();

            			break;
                       
                    case 3:
           			 i = new Intent(CalculatorGUI.this, PositionButtons.class);
           			startActivity(i);                    	
                    	break;
                    case 4:
                    	 i = new Intent(CalculatorGUI.this,
                    			 EnotaMainHendler .class);
						startActivity(i);
						break;
						
                /*    case 5:
                    	DecimalSeparator ds=new DecimalSeparator(CalculatorGUI.this);
                    	ds.show();
                    	break;*/
                    /*    Intent photosIntent = new Intent(CalculatorGUI.this, CurrencyConverterActivity.class);
                        startActivity(photosIntent);
                      */ 
                   


            }
		    }
		});
		AlertDialog alert = builder.create();

		alert.show();
		
	}

	private void ProcessKeypadInput(KeypadButton keypadButton) {

		calc.inputKey(keypadButton);
		if (keypadButton != KeypadButton.GROUP_DIGIT) {
			memoryStack.setText(calc.hasMemValue() ? "M" : "");
			if (keypadButton != KeypadButton.MS)

				mStackText.setText(calc.getSecScreenText());
		}
		userInputText.setText(calc.getPrmScreenText());
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public void applySettings() {
		// TODO Auto-generated method stub
		String rad = prefs.getString("angle", "1");
		if (rad.equals("1")) {
			radian = false;
		} else
			radian = true;

		boolean digit = prefs.getBoolean("separator", false);
		if (digit != digit_grop) {
			ProcessKeypadInput(KeypadButton.GROUP_DIGIT);
			digit_grop = digit;
		}

		String minx = prefs.getString("minx", "-10");
		if (isNumeric(minx))
			Expression.minX = Integer.parseInt(minx);

		String maxx = prefs.getString("maxx", "10");
		if (isNumeric(maxx))
			Expression.maxX = Integer.parseInt(maxx);
		String miny = prefs.getString("miny", "-10");
		if (isNumeric(miny))
			Expression.minY = Integer.parseInt(miny);
		String maxy = prefs.getString("maxy", "10");
		if (isNumeric(maxy))
			Expression.maxY = Integer.parseInt(maxy);

		if (Expression.minX >= Expression.maxX) {
			Expression.minX = Expression.maxX - 10;

			Toast.makeText(
					getApplicationContext(),
					"Invalid coordinat input. We set" + " MinX:"
							+ Expression.minX + " MaxX:" + Expression.maxX
							+ " MinY:" + Expression.minY + " MaxY:"
							+ Expression.maxY, 1000).show();

		}
		if (Expression.minY >= Expression.maxY) {
			Expression.minY = Expression.maxY - 10;
			Toast.makeText(
					getApplicationContext(),
					"Invalid coordinat input. We set" + " MinX:"
							+ Expression.minX + " MaxX:" + Expression.maxX
							+ " MinY:" + Expression.minY + " MaxY:"
							+ Expression.maxY, 1000).show();
		}
		boolean grid = prefs.getBoolean("grid", false);
		GraphSettings.setDrawGrid(grid);

		String hist = prefs.getString("historysize", "30");
		if (hist.equals("10")) {
			histSize = 10;
		} else if (hist.equals("20")) {
			histSize = 20;
		} else if (hist.equals("30")) {
			histSize = 30;
		} else if (hist.equals("40")) {
			histSize = 40;
		} else if (hist.equals("50")) {
			histSize = 50;
		}
	}

	private void showPopup(final Activity context, Point p) {
		int popupWidth = 200;
		int popupHeight = 150;

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) context
				.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.popuplayout, viewGroup);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);

		popup.setContentView(layout);
		popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		// popup.setWidth(popupWidth);
		// popup.setHeight(popupHeight);
		popup.setFocusable(true);

		// Some offset to align the popup a bit to the right, and a bit down,
		// relative to button's position.
		int OFFSET_X = 30;
		int OFFSET_Y = 30;

		// Clear the default translucent background
		popup.setBackgroundDrawable(new BitmapDrawable());

		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x /* + OFFSET_X */,
				p.y /* + OFFSET_Y */);

		// Getting a reference to Close button, and close the popup when
		// clicked.
		Button copy = (Button) layout.findViewById(R.id.copy);
		copy.setOnClickListener(new OnClickListener() {
			String CopyText;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CopyText = userInputText.getText().toString();
				if (CopyText.length() != 0) {
					if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
						android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(CopyText);
						Toast.makeText(getApplicationContext(),
								"Text Copied to Clipboard", Toast.LENGTH_SHORT)
								.show();
					} else {
						android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						android.content.ClipData clip = android.content.ClipData
								.newPlainText("Clip", CopyText);
						Toast.makeText(getApplicationContext(),
								"Text Copied to Clipboard", Toast.LENGTH_SHORT)
								.show();
						clipboard.setPrimaryClip(clip);
					}
				} else {
					Toast.makeText(getApplicationContext(), "Nothing to Copy",
							Toast.LENGTH_SHORT).show();
				}
				popup.dismiss();
			}
		});
		Button paste = (Button) layout.findViewById(R.id.paste);
		paste.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pasteText;
				// TODO Auto-generated method stub
				if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
					android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					pasteText = clipboard.getText().toString();
					userInputText.setText(pasteText);
				} else {
					ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					if (clipboard.hasPrimaryClip() == true) {
						ClipData.Item item = clipboard.getPrimaryClip()
								.getItemAt(0);
						pasteText = item.getText().toString();
						// userInputText.setText(pasteText);
						copyInputToExpression(pasteText);
					} else {
						Toast.makeText(getApplicationContext(),
								"Nothing to Paste", Toast.LENGTH_SHORT).show();
					}
				}
				popup.dismiss();

			}

			private void copyInputToExpression(String pasteText) {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder(pasteText);
				for (int i = 0; i < pasteText.length(); i++)
					switch (sb.charAt(i)) {
					case ',':
					case '.':
						ProcessKeypadInput(KeypadButton.DECIMAL_SEP);
						break;
					case '0':
						ProcessKeypadInput(KeypadButton.ZERO);
						break;
					case '1':
						ProcessKeypadInput(KeypadButton.ONE);
						break;
					case '2':
						ProcessKeypadInput(KeypadButton.TWO);
						break;
					case '3':
						ProcessKeypadInput(KeypadButton.THREE);
						break;
					case '4':
						ProcessKeypadInput(KeypadButton.FOUR);
						break;
					case '5':
						ProcessKeypadInput(KeypadButton.FIVE);
						break;
					case '6':
						ProcessKeypadInput(KeypadButton.SIX);
						break;
					case '7':
						ProcessKeypadInput(KeypadButton.SEVEN);
						break;
					case '8':
						ProcessKeypadInput(KeypadButton.EIGHT);
						break;
					case '9':
						ProcessKeypadInput(KeypadButton.NINE);
						break;
					case '-':
						ProcessKeypadInput(KeypadButton.SIGN);
						break;
					default:
						Toast.makeText(getApplicationContext(), "Wrong syntax",
								Toast.LENGTH_SHORT).show();
					}

			}

		});

	}

	/**
	 * Funkcija ki izklopi gumb, in ga ponastavi na temno ozadje
	 * 
	 * @return vrne gumb
	 * @author senozetnik
	 * 
	 * */
	private Button disableButton(Button b) {

		try {
			b.setEnabled(false);
			b.setClickable(false);
			b.setBackgroundDrawable(getApplicationContext().getResources()
					.getDrawable(R.drawable.darker_button_background_pressed));
			return b;
		} catch (Exception ex) {
			return b;

		}

	}

	/**
	 * Funkcija ki aktivira gumb, in ga ponastavi na delujoèo temo ozadje
	 * 
	 * @return vrne gumb
	 * @author senozetnik
	 * 
	 * */
	private Button enableButton(int id, Button b) {
		try {
			b.setEnabled(true);
			b.setClickable(true);
			b.setBackgroundDrawable(getApplicationContext().getResources()
					.getDrawable(R.drawable.darker_button_background));

			return b;

		} catch (Exception ex) {

			return b;
		}

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		AppRater.app_launched(this);
	}

	@Override
	protected void onPause() {
		mAdView.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.resume();
		try {

			/*
			 * Button b=arButton.get(R.id.button17);
			 * b.setText(button.get(R.id.button17).getText());
			 * arButton.remove(R.id.button17); arButton.put(R.id.button17,b);
			 */

			for (Map.Entry<Integer, KeypadButton> entry : button.entrySet()) {
				Button b = arButton.get(entry.getKey());
				KeypadButton kb = entry.getValue();
				b.setText(button.get(entry.getKey()).getText());
				switch (kb.getmCategory()) {
				case MEMORYBUFFER:
					// @style/lighterButtonInRow
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
				arButton.remove(entry.getKey());
				arButton.put(entry.getKey(), b);

			}
			for (Map.Entry<Integer, KeypadButton> entry : button2.entrySet()) {
				Button b = (Button) findViewById((int) entry.getKey());
				KeypadButton kb = (KeypadButton) entry.getValue();
				TextView t = (TextView) findViewById(ButtonToTextView.mapButtonToTextView
						.get((int) entry.getKey()));
				t.setText(kb.getText());

			}

		} catch (Exception ex) {
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		SharedPreferences.Editor editor = settings.edit();
		Set<String> s = new HashSet<>();
		for (int i = 0; i < history.size(); i++) {
			s.add(history.get(i).toString());
		}
		editor.putStringSet("history", s);
		
		Set<String> buttonOnClick = new HashSet<>();
		int i=0;
		for (Map.Entry<Integer, KeypadButton> entry : button.entrySet())
		{//	MapToSSet m=new MapToSSet();
		//	buttonOnClick.add(entry.getKey()+" "+entry.getValue());
			editor.putString("b"+String.valueOf(i),entry.getKey()+" "+entry.getValue());
			i=i+1;
		}	
		
		
	
		
		editor.putStringSet("ButtonOnClick",buttonOnClick);
	
		Set<String> buttonOnClickLong = new HashSet<>();
		i=0;
		for (Map.Entry<Integer, KeypadButton> entry : button2.entrySet())
		{	//MapToSSet m=new MapToSSet(entry.getKey(),entry.getValue());
			//buttonOnClick.add(entry.getKey()+" "+entry.getValue());
			editor.putString("t"+String.valueOf(i),entry.getKey()+" "+entry.getValue());
			i=i+1;
		}
		//editor.putStringSet("OnClickLong", buttonOnClickLong);

		// Commit the edits!
		editor.commit();
		editor.clear(); 
	}

	@Override
	protected void onDestroy() {
		mAdView.destroy();
		super.onDestroy();

	}

	/**
	 * Kreiranje menuja
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.calculator_gui, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Kreiranje menuja
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;

		} else if (id == R.id.action_history) {
			Intent i = new Intent(this, ListviewActivity.class);
			startActivity(i);
			return true;
		} else if (id == R.id.action_edit) {
			Intent i = new Intent(this, PositionButtons.class);
			startActivity(i);
			return true;
		}else if(id==R.id.action_help){
			AlertDialog.Builder bu = new AlertDialog.Builder(CalculatorGUI.this);
        	bu.setMessage(Html.fromHtml(Help.s));
        	bu.setCancelable(true);
        	bu.setPositiveButton("OK",new DialogInterface.OnClickListener() {
        	        @Override
        	        public void onClick(DialogInterface dialog, int id) {
        	        // TODO Auto-generated method stub
        	                    dialog.cancel();
        	                        }
        	                        });
        	                bu.create().show();

			
		}
		return super.onOptionsItemSelected(item);

	}

}
