package com.example.mycalc;

import java.util.ArrayList;

import com.calculator.mycalculator.CalculatorGUI;
import com.calculator.mycalculator.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListviewActivity extends Activity {
	private ListView mainListView;
	private MyAdapter listAdapter;
	int sdk = android.os.Build.VERSION.SDK_INT;
	String CopyText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		ArrayList<Item> arItem = new ArrayList<>();

		int start=CalculatorGUI.history.size()-CalculatorGUI.histSize;
		if(start<0){
				start=0;		
		}
		for (int i = start; i < start+CalculatorGUI.histSize; i++) {
			if (CalculatorGUI.history.size() > i)
				arItem.add(CalculatorGUI.history.get(i));

		}

		listAdapter = new MyAdapter(this, arItem);
		mainListView = (ListView) findViewById(R.id.mainListView);
		mainListView.setAdapter(listAdapter);
		mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				CopyText = CalculatorGUI.history.get(position).getDescription();
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
			}

		});

	}

}
