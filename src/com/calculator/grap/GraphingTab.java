package com.calculator.grap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.calculator.mycalculator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Creates the Graphing Tab
 * 
 * @author Matej
 */
public class GraphingTab extends Activity {

	private GraphPanel graph;
	private AdView mAdView;

	/**
	 * Constructor to create the graphing panel
	 */
	public GraphingTab() {
		super();

	}

	public static ProgressDialog ringProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ringProgressDialog = ProgressDialog.show(this, "Please wait ...",
				"Calculating points ...", true);

		setContentView(R.layout.drawsurface);
		mAdView = (AdView) findViewById(R.id.adViewPom);
		mAdView.loadAd(new AdRequest.Builder().build());
		graph = new GraphPanel(getApplicationContext());
		// setContentView(graph);

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
	}

	@Override
	protected void onDestroy() {
		mAdView.destroy();
		super.onDestroy();
	}

}
