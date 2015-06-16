package com.Converter.myConverter.Unit;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.calculator.mycalculator.CalculatorGUI;
import com.calculator.mycalculator.R;
import com.google.android.gms.wearable.NodeApi.GetConnectedNodesResult;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleFragment extends Fragment implements
		OnItemSelectedListener, TextWatcher, OnClickListener {
	final static String ARG_POSITION = "position";
	int mCurrentPosition = -1;

	// notri so zapisane vse enote
	private ArrayList<Enota> enote;
	private ArrayList<String> e;
	private ArrayAdapter enoteAdapter;
	
	private ArrayList<String> results;

	// Spiner za vhodne podatke
	private Spinner inputSpinner;

	// Spinner za izhodne podatke
	private Spinner outputSpinner;

	private double inputAmount;
	private boolean inputValid;

	// indeksa ki povesta katera je trenutna stvar izbrana
	private int unitInputIndex1;
	private int unitInputIndex2;

	// normalizirana mera ki je trenutno prikazana
	private double inputRate1;
	private double inputRate2;

	// rabimo tudi ime kot so moč, kot itd..
	String category;

	View v;
	TextView v1;
	ListView listView;
	ArrayList<Model> list;
	
	
	int sdk = android.os.Build.VERSION.SDK_INT;
	String CopyText;

	/** Declaring an ArrayAdapter to set items to ListView */
	//ArrayAdapter<String> adapter;
	 MyAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.unitInputIndex1 = -1;
		this.unitInputIndex2 = -1;
		this.inputRate1 = -1.0;
		this.inputRate2 = -1.0;
		this.inputValid = false;
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			unitInputIndex1 = savedInstanceState.getInt("unitInputIndex1");
			unitInputIndex2 = savedInstanceState.getInt("unitInputIndex2");
			inputAmount = savedInstanceState.getDouble("inputAmount");
			inputValid = savedInstanceState.getBoolean("inputValid");
			inputRate1 = savedInstanceState.getDouble("inputRate1");
			inputRate2 = savedInstanceState.getDouble("inputRate2");
		}
		v = inflater.inflate(R.layout.article_view, container, false);
		e = new ArrayList<String>();

		category = "kot";
		this.enote = EnoteUpravljalec
				.getUnits(this.category, v.getContext(), 1);
		String[] unitNames = new String[this.enote.size()];
		for (int i = 0; i < this.enote.size(); i++)
			unitNames[i] = enote.get(i).getLocalizedName();
		enoteAdapter = new ArrayAdapter(v.getContext(),
				android.R.layout.simple_spinner_item, unitNames);
		enoteAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		inputSpinner = (Spinner) v.findViewById(R.id.from_currency_spinner);
	//	v1 = (TextView) v.findViewById(R.id.unit_conversion_output);
		inputSpinner.setAdapter(enoteAdapter);
		inputSpinner.setOnItemSelectedListener(this);
		EditText edit_text = (EditText) v
				.findViewById(R.id.currency_converter_edit_text);
		edit_text.addTextChangedListener(this);
		///	adapter = new ArrayAdapter<String>(v.getContext(),	R.layout.custom_article_view,R.id.textItem, list);

		listView = (ListView) v.findViewById(R.id.show_currency);
		list = new ArrayList<Model>();
		adapter = new MyAdapter(v.getContext(), list);
		listView.setAdapter(adapter);
		edit_text.setText("1");
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();

		Bundle args = getArguments();

		if (args != null) {
			unitInputIndex1 = EnotaMainHendler.unitInputIndex1;
			unitInputIndex2 = EnotaMainHendler.unitInputIndex2;
			inputAmount = EnotaMainHendler.inputAmount;
			inputValid = EnotaMainHendler.inputValid;
			inputRate1 = EnotaMainHendler.inputRate1;
			inputRate2 = EnotaMainHendler.inputRate2;
			updateArticleView(args.getInt(ARG_POSITION));
		} else if (mCurrentPosition != -1) {
			updateArticleView(mCurrentPosition);
		}
	}

	public void updateArticleView(int position) {

		mCurrentPosition = position;

		switch (position) {
		case 0:
			category = "kot";
			break;
		case 1:
			category="podatki";
			break;
		case 2:
			category = "energija";
			
			break;
			
		case 3:
			category = "povrsina";
			break;
		case 4:

			category = "dolzina";
			break;
		case 5:
			category = "masa";			
			break;
		case 6:
			category = "moc";			
			break;
		case 7:
			category = "hitrost";
			
			break;
		case 8:
			category = "tlak";
			break;
		case 9:
			category = "temperatura";
			break;
		case 10:
			category = "cas";
			break;
		case 11:
			category = "volumen";
			break;

		}

		e = new ArrayList<String>();
		// dobime ime kategorije
		// this.category = getIntent().getStringExtra("category");
		// dobimo enoto

		this.enote = EnoteUpravljalec.getUnits(this.category, getActivity(), 1);
		String[] unitNames = new String[this.enote.size()];
		for (int i = 0; i < this.enote.size(); i++)
			unitNames[i] = enote.get(i).getLocalizedName();
		enoteAdapter = new ArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item, unitNames);
		enoteAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		inputSpinner = (Spinner) v.findViewById(R.id.from_currency_spinner);
		inputSpinner.setAdapter(enoteAdapter);
		inputSpinner.setOnItemSelectedListener(this);
		EditText edit_text = (EditText) v
				.findViewById(R.id.currency_converter_edit_text);
		edit_text.addTextChangedListener(this);
    	listView = (ListView) v.findViewById(R.id.show_currency);
		list = new ArrayList<Model>();
		adapter = new MyAdapter(v.getContext(), list);
		

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adap, View view,
					int position, long arg) {

				list = new ArrayList<Model>();
				adapter = new MyAdapter(v.getContext(), list);
				adapter.notifyDataSetChanged();
				inputSpinner.setSelection(position);
				unitInputIndex1 = position;
				inputRate1 = getUnit(unitInputIndex1).getNormalizedValue();
				results=new ArrayList<>();
				for (int i = 0; i < enote.size(); i++) {
					unitInputIndex2 = i;
					inputRate2 = getUnit(unitInputIndex2).getNormalizedValue();

					inputValid = true;
					NarediPretvorbo();
				}
			}
		});
		
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			//	Double d =Double.valueOf( results.get(position));//CalculatorGUI.history.get(position).getDescription();
			//	DecimalFormat formatter = new DecimalFormat("#,###.00");
				double d = Double.parseDouble(results.get(position)); // Also accepts format like "1.574e10"
				Log.e("test",String.valueOf(d));
				String s1=  String.format("%.25f",d);
				Log.e("test",s1);
				CopyText=s1;
				//CopyText =  String.format("%f",results.get(position));
				//CopyText=formatter.format(d);
				if (CopyText.length() != 0) {
					if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
						android.text.ClipboardManager clipboard = (android.text.ClipboardManager)v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setText(CopyText);
						Toast.makeText(getActivity(),
								"Text Copied to Clipboard", Toast.LENGTH_SHORT)
								.show();
					} else {
						android.content.ClipboardManager clipboard = (android.content.ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
						android.content.ClipData clip = android.content.ClipData
								.newPlainText("Clip", CopyText);
						Toast.makeText(getActivity(),
								"Text Copied to Clipboard", Toast.LENGTH_SHORT)
								.show();
						clipboard.setPrimaryClip(clip);
					}
				} else {
					Toast.makeText(v.getContext(), "Nothing to Copy",
							Toast.LENGTH_SHORT).show();
				}
				return inputValid;
			}

		});
		edit_text.setText("1");

	}

	@Override
	public void afterTextChanged(Editable amount) {

		if (amount.length() == 0) {
			// If no text is entered, the input is invalid.
			this.inputValid = false;
		} else {

			String text = amount.toString();
			try {
			//list = new ArrayList<>();
			//	adapter = new MyAdapter(this, generateData());
			//	adapter = new ArrayAdapter<String>(v.getContext(),	R.layout.custom_article_view,R.id.textItem,,R.id.textItem,1 list);
				list = new ArrayList<Model>();
				adapter = new MyAdapter(v.getContext(), list);
				adapter.notifyDataSetChanged();
				adapter.notifyDataSetChanged();
				results=new ArrayList<>();
				for (int i = 0; i < enote.size(); i++) {
					unitInputIndex2 = i;
					inputRate2 = getUnit(unitInputIndex2).getNormalizedValue();
					this.inputAmount = Double.parseDouble(text);
					this.inputValid = true;

					NarediPretvorbo();
				}
			} catch (NumberFormatException e) {
				this.inputValid = false;

			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	private Enota getUnit(int index) {

		return enote.get(index);

	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.from_currency_spinner:

			list = new ArrayList<Model>();
			adapter = new MyAdapter(v.getContext(), list);
			
			adapter.notifyDataSetChanged();

			unitInputIndex1 = position;
			
			inputRate1 = getUnit(unitInputIndex1).getNormalizedValue();
			results=new ArrayList<>();
			for (int i = 0; i < enote.size(); i++) {
				unitInputIndex2 = i;
				inputRate2 = getUnit(unitInputIndex2).getNormalizedValue();

				inputValid = true;
				NarediPretvorbo();
			}
			break;

		}

	//	NarediPretvorbo();
	}

	public void NarediPretvorbo() {

		if (this.unitInputIndex1 == -1 || this.unitInputIndex2 == -1)
			return;

		double vred;
		if (this.inputValid)
			vred = this.inputAmount;
		else
			vred = 1.0;

		String LokalnaEnota1 = getUnit(unitInputIndex1).getLocalizedName();
		String LokalnaEnota2 = getUnit(unitInputIndex2).getLocalizedName();

		double resultAmount = 0.0;

		if (this.category.equalsIgnoreCase("temperatura")) {

			if (LokalnaEnota1.equalsIgnoreCase(LokalnaEnota2)) {
				resultAmount = vred;
			} else if (LokalnaEnota1.equalsIgnoreCase("fahrenheit")) {
				if (LokalnaEnota2.equalsIgnoreCase("celsius")) {
					resultAmount = (vred - 32) * (5 / 9.0);
				} else if (LokalnaEnota2.equalsIgnoreCase("kelvin")) {
					resultAmount = (vred - 32) * (5 / 9.0) + 273.15;
				}
			} else if (LokalnaEnota1.equalsIgnoreCase("celsius")) {
				if (LokalnaEnota2.equalsIgnoreCase("fahrenheit")) {
					resultAmount = (vred * (9 / 5.0)) + 32;
				} else if (LokalnaEnota2.equalsIgnoreCase("kelvin")) {
					resultAmount = vred + 273.15;
				}
			} else if (LokalnaEnota1.equalsIgnoreCase("kelvin")) {
				if (LokalnaEnota2.equalsIgnoreCase("fahrenheit")) {
					resultAmount = ((vred - 273.15) * 1.8) + 32;
				} else if (LokalnaEnota2.equalsIgnoreCase("celsius")) {
					resultAmount = vred - 273.15;
				}

				e.add(category
						+ " "
						+ vred
						+ " "
						+ getUnit(unitInputIndex1).getLocalizedName()
								.toLowerCase()
						+ " "
						+ resultAmount
						+ " "
						+ getUnit(unitInputIndex2).getLocalizedName()
								.toLowerCase());
			}
		} else {

			resultAmount = vred * (this.inputRate1 / this.inputRate2);
			if (vred > 1.0 && !this.category.equalsIgnoreCase("temperature"))
				e.add(category
						+ " iz "
						+ vred
						+ " "
						+ getUnit(unitInputIndex1).getLocalizedName()
								.toLowerCase()
						+ " v "
						+ resultAmount
						+ " "
						+ getUnit(unitInputIndex2).getLocalizedName()
								.toLowerCase());
		}

		String result = Double.toString(resultAmount);
		DecimalFormat df=null;
		if(result.length()>9)
		{	  
		df = new DecimalFormat("0.#######E0");
		
		adapter.add(new Model(df.format(resultAmount), getUnit(unitInputIndex2).getLocalizedName()+" ("+getUnit(unitInputIndex2).getAbbreviatedName()+")"));
		}	
		else
		adapter.add(new Model(result, getUnit(unitInputIndex2).getLocalizedName()+" ("+getUnit(unitInputIndex2).getAbbreviatedName()+")"));
		listView.setAdapter(adapter);
		
		this.results.add(result);

		// setConversionOutput(result);
	}

/*public void onNothingSelected(AdapterView<?> parent) {
		switch (parent.getId()) {
		case R.id.unitInput1:
			this.unitInputIndex1 = -1;
			this.inputRate1 = -1.0;
			break;
		case R.id.unitInput2:
			this.unitInputIndex2 = -1;
			this.inputRate2 = -1.0;
			break;
		}
	}
	*/

	// Save the activity state
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		EnotaMainHendler.unitInputIndex1 = unitInputIndex1;
		EnotaMainHendler.unitInputIndex2 = unitInputIndex2;
		EnotaMainHendler.inputAmount = inputAmount;
		EnotaMainHendler.inputValid = inputValid;
		EnotaMainHendler.inputRate2 = inputRate2;

		savedInstanceState.putInt("unitInputIndex1", unitInputIndex1);
		savedInstanceState.putInt("unitInputIndex2", unitInputIndex2);
		savedInstanceState.putDouble("inputAmount", inputAmount);
		savedInstanceState.putBoolean("inputValid", inputValid);
		savedInstanceState.putDouble("inputRate1", inputRate1);
		savedInstanceState.putDouble("inputRate2", inputRate2);
		savedInstanceState.putInt(ARG_POSITION, mCurrentPosition);
		super.onSaveInstanceState(savedInstanceState);
		// SaveAllObjects();
	}

@Override
public void onNothingSelected(AdapterView<?> parent) {
	// TODO Auto-generated method stub
	
}

}