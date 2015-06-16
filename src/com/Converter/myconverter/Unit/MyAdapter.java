package com.Converter.myConverter.Unit;

import java.util.ArrayList;

import com.calculator.mycalculator.CalculatorGUI;
import com.calculator.mycalculator.R;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class MyAdapter extends ArrayAdapter<Model> {
 
        private final Context context;
        private final ArrayList<Model> modelsArrayList;
 
        public MyAdapter(Context context, ArrayList<Model> modelsArrayList) {
 
            super(context, R.layout.activity_main, modelsArrayList);
 
            this.context = context;
            this.modelsArrayList = modelsArrayList;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
 
            // 1. Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // 2. Get rowView from inflater
 
            View rowView = null;
           
                rowView = inflater.inflate(R.layout.custom_article_view, parent, false);
 
                // 3. Get icon,title & counter views from the rowView
             //   ImageView imgView = (ImageView) rowView.findViewById(R.id.textview); 
                TextView titleView = (TextView) rowView.findViewById(R.id.textItem);
                TextView counterView = (TextView) rowView.findViewById(R.id.textItem1);
               
    			switch(CalculatorGUI.screenSize) {
    		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
    		    	titleView.setTextSize(14);
    		    	counterView.setTextSize(14);
    		        break;
    		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
    		    	titleView.setTextSize(18);
    		    	counterView.setTextSize(18);
    		    	break;
    		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
    		    	titleView.setTextSize(22);
    		    	counterView.setTextSize(22);
    		    	break;
    		    case Configuration.SCREENLAYOUT_SIZE_XLARGE:
    		    	titleView.setTextSize(26);
    		    	counterView.setTextSize(26);
    		        break;
    		    default:
    		    	titleView.setTextSize(18);
    		    	counterView.setTextSize(18);
    		    	break;
    			}
 
                // 4. Set the text for textView 
              //  imgView.setImageResource(modelsArrayList.get(position).getIcon());
               if(modelsArrayList.get(position) != null){
            	   if(modelsArrayList.get(position).getTitle()!=null)
                titleView.setText(modelsArrayList.get(position).getTitle());
            	   if(modelsArrayList.get(position).getCounter()!=null)
            	   counterView.setText(modelsArrayList.get(position).getCounter());
            	 
                }
         /*  }
            else{
                    rowView = inflater.inflate(R.layout.group_header_item, parent, false);
                    TextView titleView = (TextView) rowView.findViewById(R.id.header);
                    titleView.setText(modelsArrayList.get(position).getTitle());
 
            }*/
 
            // 5. retrn rowView
            return rowView;
        }
}