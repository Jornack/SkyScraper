package com.jornack.skyscraper.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.json.JSONException;
import org.json.JSONObject;

import com.jornack.skyscraper.window.PreferencesWindow;

public class LineChartGenerator {
	
	private final static float LINE_THINKNESS 				  = 2f;
	

	
	public JFreeChart generate(File file){
		
		TimeSeries poorSignalLevelSeries     = new TimeSeries ("Poor Signal Level"); 
		TimeSeries attentionSeries           = new TimeSeries ("Attention");        
		TimeSeries meditationSeries          = new TimeSeries ("Meditation");       
		TimeSeries deltaSeries               = new TimeSeries ("Delta");            
		TimeSeries thetaSeries               = new TimeSeries ("Theta ");           
		TimeSeries lowAlphaSeries            = new TimeSeries ("Low Alpha");        
		TimeSeries highAlphaSeries           = new TimeSeries ("High Alpha");       
		TimeSeries lowBetaSeries             = new TimeSeries ("Low Beta");         
		TimeSeries highBetaSeries            = new TimeSeries ("High Beta");        
		TimeSeries lowGammaSeries            = new TimeSeries ("Low Gamma");        
		TimeSeries highGamaSeries            = new TimeSeries ("High Gama");        
		

	    TimeSeriesCollection dataset = new TimeSeriesCollection();
	    boolean ignorePSl =PreferenceManager.getPreferences().getBoolean(PreferencesWindow.CHART_GENERATION_IGNORE_PSL, false);
		boolean ignoreESense =PreferenceManager.getPreferences().getBoolean(PreferencesWindow.CHART_GENERATION_IGNORE_ESENSE, false);
		boolean ignoreEEGPwr =PreferenceManager.getPreferences().getBoolean(PreferencesWindow.CHART_GENERATION_IGNORE_EEGPWR, false);
		if (!ignorePSl){
			dataset.addSeries(poorSignalLevelSeries		 );
		}
		if (!ignoreESense){
			 dataset.addSeries(attentionSeries           );
			dataset.addSeries(meditationSeries          );
				
		}
		if(!ignoreEEGPwr){
			dataset.addSeries(deltaSeries               );
			dataset.addSeries(thetaSeries               );
			dataset.addSeries(lowAlphaSeries            );
			dataset.addSeries(highAlphaSeries           );
			dataset.addSeries(lowBetaSeries             );
			dataset.addSeries(highBetaSeries            );
			dataset.addSeries(lowGammaSeries            );
			dataset.addSeries(highGamaSeries            );
		}
		
		JFreeChart chart = createChart(dataset);
		

		
		JSONFileReader reader = new JSONFileReader(file);
		chart.setTitle(file.getAbsoluteFile().toString());
		
		Date first = null;
		Date last = null;
		while(reader.isDataAvailable()){
			String data = reader.getData();
			//System.out.println( new Millisecond() +": " +data);
			try {
				JSONObject json = new JSONObject(data);
				Millisecond ms = null;
				if (!json.isNull("ms")){
					ms = new Millisecond(new Date(json.getInt("ms")));
					if (first == null){
						first = new Date(json.getInt("ms"));
					}else{
						last = new Date(json.getInt("ms"));
					}
				}
				Logger.debug("Date: " + new Date(json.getInt("ms")));
				//System.out.println("ms: " +  ms);
				if (!json.isNull("poorSignalLevel")){

					poorSignalLevelSeries.addOrUpdate(ms, (double) json.getInt("poorSignalLevel"));
				}
				/*
				 * JH: check for existence of eSense. 
				 * I noticed it's possible to get eegPower without eSense when poorSignallevel >0
				 */
				if (!json.isNull("eSense")){

					JSONObject esense = json.getJSONObject("eSense");

					attentionSeries.addOrUpdate(ms,(double)esense.getInt("attention"));
					meditationSeries.addOrUpdate(ms,(double)esense.getInt("meditation"));
					
				} 
				// JH: check just in case it's not there due to poorSignallevel
				if (!json.isNull("eegPower")){
					
					JSONObject eegPower = json.getJSONObject("eegPower");
					deltaSeries.addOrUpdate(ms,(double)eegPower.getInt("delta"));
					thetaSeries.addOrUpdate(ms,(double)eegPower.getInt("theta"));
					lowAlphaSeries.addOrUpdate(ms,(double)eegPower.getInt("lowAlpha"));
					highAlphaSeries.addOrUpdate(ms,(double)eegPower.getInt("highAlpha"));
					lowBetaSeries.addOrUpdate(ms,(double)eegPower.getInt("lowBeta"));
					highBetaSeries.addOrUpdate(ms,(double)eegPower.getInt("highBeta"));
					lowGammaSeries.addOrUpdate(ms,(double)eegPower.getInt("lowGamma"));
					highGamaSeries.addOrUpdate(ms,(double)eegPower.getInt("highGamma"));
					
				} 

				Thread.currentThread();
				Thread.sleep(1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		try {
			
			//System.getProperty("user.home"));
			int width = PreferenceManager.getPreferences().getInt(PreferencesWindow.CHART_GENERATION_WIDTH, 4096);
			int height = PreferenceManager.getPreferences().getInt(PreferencesWindow.CHART_GENERATION_HEIGHT, 512);
			String format = PreferenceManager.getPreferences().get(PreferencesWindow.CHART_GENERATION_FORMAT, "PNG");
			resizeChart(chart, first, last);
			if (format.equals("PNG")){
				ChartUtilities.saveChartAsPNG(new File(file.getAbsoluteFile() +".png"), chart, width, height);
			}else{
				ChartUtilities.saveChartAsJPEG(new File(file.getAbsoluteFile() +".jpg"), 1.0f, chart, width, height);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chart;
	}
	private void resizeChart(JFreeChart chart, Date first, Date last){
		XYPlot plot = chart.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        //axis.setAutoRange(true);
        Logger.debug(axis.getRange().toString());
        axis.setRange(new Range(first.getTime(), last.getTime()));
        Logger.debug(axis.getRange().toString());
        
//        /axis.setFixedAutoRange(60000.0);  // 60 seconds
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAutoRange(true);
        rangeAxis.setRange(rangeAxis.getRange()); 
//        /Logger.debug(rangeAxis.getRange().toString());
	}
	private JFreeChart createChart(XYDataset dataset) {
        JFreeChart result = ChartFactory.createTimeSeriesChart(
            "", //Title
            "Time", //timeAxisLabel
            "Value", //valueAxislabel
            dataset, //dataset
            true, //legend
            true, //tooltips
            false //uurls
        );
        XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        
        return result;
    }

}
