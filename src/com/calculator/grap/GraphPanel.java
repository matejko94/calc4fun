/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.calculator.grap;

import com.calculator.mycalculator.CalculatorAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Creates the Graphing Panel
 * 
 * @author Matej
 */
public class GraphPanel extends SurfaceView implements SurfaceHolder.Callback {

	
	 ProgressDialog ringProgressDialog;
	public GraphPanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private SurfaceHolder holder;
	private GraphingTread graphingTread;
	private Context context;
	public GraphPanel(Context context) {
		super(context);
		holder = getHolder(); // Holder is now the internal/private
								// mSurfaceHolder inherit
								// from the SurfaceView class, which is from an
								// anonymous
								// class implementing SurfaceHolder interface.
		this.context=context;
		holder.addCallback(this);
	}

	public GraphPanel(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		holder = getHolder(); // Holder is now the internal/private
								// mSurfaceHolder inherit
		this.context=context;
		holder.addCallback(this);
	}
	
	

	
	@Override
	public void surfaceChanged(final SurfaceHolder holder, int format, int width,
			int height) {
		
		CalculatorAdapter.width=width;
		CalculatorAdapter.height=height;
		
	    new Thread(new Runnable() {

	        @Override
	        public void run() {

	            try {

	                CalculatorAdapter.expr[0].exprasionCalculatePoints();
	            } catch (Exception e) {
	                Log.e(e.getMessage(), e.getMessage());

	            }

	            GraphingTab.ringProgressDialog.dismiss();
				graphingTread = new GraphingTread(holder);
				graphingTread.setRunning(true);
				graphingTread.setSurfaceSize(CalculatorAdapter.width, CalculatorAdapter.height);
				graphingTread.drawGraph();


	        }
	    }).start();
		
		

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		if(graphingTread!=null)
		graphingTread.setRunning(false);
		while (retry) {
			try {
				retry = false;
			} catch (Exception e) {
				Log.e(e.getMessage(),e.getMessage());
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
