package com.calculator.grap;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.calculator.excpetions.InvalidBoundsException;
import com.calculator.mycalculator.CalculatorAdapter;
import com.calculator.mycalculator.CalculatorGUI;
import com.calculator.mycalculator.Expression;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


/**
 * Creates the Graphing Tread
 * 
 * @author Matej
 */

public class GraphingTread implements Runnable {

	public static int mCanvasWidth;
	public static int mCanvasHeight;

	public int getHeight() {
		return mCanvasHeight;
	}

	public int getWidth() {
		return mCanvasWidth;
	}



	private double xInterval;
	private double yInterval;
	private int xAxis = 0;
	private int yAxis = 0;
	private Vector<Thread> threads = new Vector<Thread>();
	private boolean stopThreads = false;
	private boolean painting = false;
	private int currentEq = 0;
	private static HashMap<String, PointF> points = new HashMap<String, PointF>();
	SurfaceView view;
	private Canvas g2;
	private Paint p;
	Path polyline;

	private ArrayList<Point> pointA;
	int xx, yy, xxx, yyy;

	private SurfaceHolder holder;
	private boolean running = false;
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final int refresh_rate = 100; // How often we update the screen, in
											// ms

	public GraphingTread(SurfaceHolder holder) {
		this.holder = holder;

	}

	public void setRunning(boolean b) {
		running = b;

	}

	public void setSurfaceSize(int width, int height) {
		synchronized (holder) {
			mCanvasWidth = width;
			mCanvasHeight = height;
		}
	}

	public void paintComponent(Canvas g2) {
		painting = true;
		// g2 = new Canvas(bitmap);
		// imageview.setImageBitmap(bitmap);
		p = new Paint();
		p.setColor(Color.GRAY);

		
		yAxis = UnitToPixelX(0);
		xAxis = UnitToPixelY(0);
		// GraphSettings.setDrawGrid(true);
		// Draw Grid
		if (GraphSettings.isDrawGrid()) {
			// g2.setColor(Color.GRAY);
			xInterval = Math.pow(10, String.valueOf((int) (Expression.maxX - Expression.minX) / 4)
					.length() - 1);
			yInterval = Math.pow(10, String.valueOf((int) (Expression.maxY - Expression.minY) / 4)
					.length() - 1);

			xInterval = yInterval = Math.min(xInterval, yInterval);

			for (double i = 0 + xInterval; i <= Expression.maxX; i += xInterval) {

				g2.drawLine(UnitToPixelX(i), 0, UnitToPixelX(i),
						g2.getHeight(), p);
			}
			for (double i = 0 - xInterval; i >= Expression.minX; i -= xInterval) {
				g2.drawLine(UnitToPixelX(i), 0, UnitToPixelX(i),
						g2.getHeight(), p);
			}
			for (double i = 0 + yInterval; i <= Expression.maxY; i += yInterval) {
				g2.drawLine(0, UnitToPixelY(i), g2.getWidth(), UnitToPixelY(i),
						p);
			}
			for (double i = 0 - yInterval; i >= Expression.minY; i -= yInterval) {
				g2.drawLine(0, UnitToPixelY(i), g2.getWidth(), UnitToPixelY(i),
						p);
			}
		}

		// Draw crossheir
		g2.drawLine(g2.getWidth() / 2 - 5, g2.getHeight() / 2,
				g2.getWidth() / 2 + 5, g2.getHeight() / 2, p);
		g2.drawLine(g2.getWidth() / 2, g2.getHeight() / 2 - 5,
				g2.getWidth() / 2, g2.getHeight() / 2 + 5, p);



		
		
		painting = false;
	}

	
	public void paintPoint(Canvas g2){
		p = new Paint();
		p.setColor(Color.BLACK);

		int start=0,end=0;
		switch(CalculatorGUI.screenSize) {
	    case Configuration.SCREENLAYOUT_SIZE_SMALL:
	    	p.setTextSize(14);
	    	start=15;
	    	end=7;
	        break;
	    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
	    	p.setTextSize(16);
	    	start=20;
	    	end=10;
	    	break;
	    case Configuration.SCREENLAYOUT_SIZE_LARGE:
	    	p.setTextSize(22);
	    	start=25;
	    	end=13;
	    	break;
	    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
	    	p.setTextSize(26);
	    	start=30;
	    	end=16;
	    	break;
	    default:
	    	p.setTextSize(16);
	       
	}
	
		

		// Draw x and y axis
		g2.drawLine(0, xAxis, g2.getWidth(), xAxis, p);
		g2.drawLine(yAxis, 0, yAxis, g2.getHeight(), p);
		// Write Numbers
		g2.drawText("0", yAxis + 2, xAxis - 1, p);
		g2.drawText(String.valueOf(Expression.minX), 5, xAxis - 1, p);
		
		g2.drawText(String.valueOf(Expression.maxX), this.getWidth()-String.valueOf(Expression.maxX).length()*end, xAxis - 1, p);
		g2.drawText(String.valueOf(Expression.minY),yAxis + 2, this.getHeight() - 5 , p);	
		g2.drawText(String.valueOf(Expression.maxY), yAxis + 2, start, p);			
		g2.drawText(String.valueOf("You entered: "+CalculatorAdapter.expr[1].toString()),2,  start , p);	
		
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
		double pixelsPerUnit = getWidth() / (Expression.maxX - Expression.minX);
		double pos = (x - Expression.minX) * pixelsPerUnit;
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
		double pixelsPerUnit = getHeight() / (Expression.maxY - Expression.minY);
		double pos = (y - Expression.minY) * pixelsPerUnit;
		pos = -pos + getHeight();
		return (int) pos;
	}


	/**
	 * Draws the graph with the equations
	 * 
	 * @param eq
	 */
	void drawGraph() {

		 startDrawing();
	}
	
	public void startDrawing() {
		stopThreads = true;
		for (Thread t : threads) {
			t.stop(); //TODO: Terbile design. This should be changed later. But it works for testing.
		}
		threads.clear();
		stopThreads = false;
			threads.add(new Thread(this));
			threads.lastElement().start();

	}




	@Override
	public void run() {
		
		
	//	Calculator.expr[0].exprasionCalculatePoints();
		
		Log.v("tocke",Expression.minX+" "+Expression.maxX+" "+Expression.minY+" "+Expression.maxY);
		CalculatorAdapter.isXInput=false;
		Canvas canvas = null;
		try {
			canvas = holder.lockCanvas();
			synchronized (holder) {
				canvas.drawColor(Color.WHITE);
				draw(canvas, polyline);
				CalculatorAdapter.point=new ArrayList<>();
				
			}
		} finally {
			if (canvas != null) {
				holder.unlockCanvasAndPost(canvas);
				CalculatorAdapter.point=new ArrayList<>();
			}
		}

	}

	private void draw(Canvas canvas, Path path) {
		// TODO Auto-generated method stub

		paintComponent(canvas);
		p = new Paint();
		p.setColor(Color.BLUE);
		/*
		switch(CalculatorGUI.screenSize) {
	    case Configuration.SCREENLAYOUT_SIZE_SMALL:
	    	p.setStrokeWidth(14);
	        break;
	    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
	    	p.setStrokeWidth(16);
	        break;
	    case Configuration.SCREENLAYOUT_SIZE_LARGE:
	    	p.setStrokeWidth(22);
	        break;
	    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
	    	p.setTextSize(26);
	        break;
	    default:
	    	p.setTextSize(16);
	       
	}
	*/
		

		Log.e("size",String.valueOf(CalculatorAdapter.point.size()));
		for (int i = 1; i <CalculatorAdapter.point.size(); i = i + 1) {
			int beginx = CalculatorAdapter.point.get(i - 1).x, beginy = CalculatorAdapter.point.get(i - 1).y, endx = CalculatorAdapter.point
					.get(i).x, endy = CalculatorAdapter.point.get(i).y;
			if(!(beginx<getHeight()&&endx>getHeight())||
			!(beginy<getWidth()&&endy>getWidth())
			||!(endx>getHeight()&&beginx<getHeight())||
			!(endy>getWidth()&&beginy<getWidth())
					)
			  canvas.drawLine(beginx, beginy, endx, endy, p);
		
		}
		
		paintPoint(canvas);


	}

}
