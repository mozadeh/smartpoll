package com.smartiky.smartpoll;

import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class PollAnalyticsActivity extends Activity {
	
	
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.pollanalytics);
	Date dd1= new Date(113, 11, 5);
	Date dd2= new Date(113, 11, 6);
	Date dd3= new Date(113, 11, 7);
	Date dd4= new Date(113, 11, 8);
	Date dd5= new Date(113, 11, 10);
	
	long d1= dd1.getTime();
	long d2= dd2.getTime();
	long d3= dd3.getTime();
	long d4= dd4.getTime();
	long d5= dd5.getTime();
	final java.text.DateFormat dateTimeFormatter = DateFormat.getMediumDateFormat(getBaseContext());
	GraphViewData[] Data1 = new GraphViewData[] {  
			
		      new GraphViewData(d1, 2.0d)  
		      , new GraphViewData(d2, 1.5d)  
		      , new GraphViewData(d3, 2.5d)  
		      , new GraphViewData(d4, 1.0d)
		      , new GraphViewData(d5, 3.0d)
		};
	
	GraphViewData[] Data2 = new GraphViewData[] { 
			
				new GraphViewData(d1, 3.0d)  
		      , new GraphViewData(d2, 3.5d)  
		      , new GraphViewData(d3, 5.5d)  
		      , new GraphViewData(d4, 0.5d)
				, new GraphViewData(d5, 1.5d)
		};
	
	GraphViewSeriesStyle GS_forData1=new GraphViewSeriesStyle(Color.BLUE, 10);
	GraphViewSeriesStyle GS_forData2=new GraphViewSeriesStyle(Color.RED, 10);
	// init example series data  
	GraphViewSeries exampleSeries = new GraphViewSeries("Series1", GS_forData1, Data1);
	
	GraphViewSeries exampleSeries2 = new GraphViewSeries("Series1", GS_forData2, Data2);
	
	  
	GraphView graphView = new LineGraphView(  
	      this // context  
	      , "GraphViewDemo" // heading  
	){
	    @Override
	    protected String formatLabel(double value, boolean isValueX) {
	        if (isValueX) {
	            // transform number to time
	            return dateTimeFormatter.format(new Date((long) value));
	        } else {
	            return super.formatLabel(value, isValueX);
	        }
	    }
	};  
	graphView.addSeries(exampleSeries); // data
	graphView.addSeries(exampleSeries2);
	//((LineGraphView)graphView).setDrawBackground(true);
	
	// set legend  
	graphView.setShowLegend(true);  
	graphView.setLegendAlign(LegendAlign.BOTTOM);  
	graphView.setLegendWidth(400);
	graphView.setScalable(true);
	
	
	
	  
	LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout);  
	layout.addView(graphView);

	
}
}
