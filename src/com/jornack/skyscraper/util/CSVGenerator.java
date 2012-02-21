/**
 * @author Jornack
 * copyright 2012 Jornack Hupkens
 *
 */
package com.jornack.skyscraper.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jornack
 *
 */
public class CSVGenerator extends JSONFileReader {
	
	public CSVGenerator(File source) {
		super(source);
		// TODO Auto-generated constructor stub
	}
	
	public void generate(File target) throws IOException{
		FileWriter writer = new FileWriter(target);
		
		writer.append("TIMESTAMP,POOR_SIGNAL_LEVEL,BLINKSTRENGTH,ATTENTION,MEDITATION," + 
				"DELTA,THETA,LOW_ALPHA,HIGH_ALPHA,LOW_BETA,HIGH_BETA," + 
				"LOW_GAMMA,HIGH_GAMA\n");

		String data = null;
		while (isDataAvailable()){
			data = getData();
			try {
				
				JSONObject json = new JSONObject(data);
				
				/*
				 * JH: check for existence of ms. If not available, assume 0									 * 
				 */
				if (!json.isNull("ms")){
					writer.append(Integer.toString(json.getInt("ms")) + ',');
				}else{
					writer.append("0,"); 
				}
				/*
				 * JH: check for existence of poorSignalLevel. If not available, assume 0									 * 
				 */
				if (!json.isNull("poorSignalLevel")){
					writer.append(Integer.toString(json.getInt("poorSignalLevel")) + ',');
				}else{
					writer.append("0,"); 
				}
				/*
				 * JH: check for existence of blinkStrength. If not available, assume 0									 * 
				 */
				if (!json.isNull("blinkStrength")){
					writer.append(Integer.toString(json.getInt("blinkStrength")) + ',');
				}else{
					writer.append("0,"); 
				}
				/*
				 * JH: check for existence of eSense. 
				 * I noticed it's possible to get eegPower without eSense when poorSignallevel >0
				 */
				if (!json.isNull("eSense")){

					JSONObject esense = json.getJSONObject("eSense");

					 
					writer.append(Integer.toString(esense.getInt("attention")) + ',');
					writer.append(Integer.toString(esense.getInt("meditation")) + ',');
					
				
				} else{
					writer.append("0,"); 
					writer.append("0,"); 
				}
				// JH: check just in case it's not there due to poorSignallevel
				if (!json.isNull("eegPower")){
					
					JSONObject eegPower = json.getJSONObject("eegPower");
					
					writer.append(Integer.toString(eegPower.getInt("delta")) + ',');
					writer.append(Integer.toString(eegPower.getInt("theta")) + ',');
					writer.append(Integer.toString(eegPower.getInt("lowAlpha")) + ',');
					writer.append(Integer.toString(eegPower.getInt("highAlpha")) + ',');
					writer.append(Integer.toString(eegPower.getInt("lowBeta")) + ',');
					writer.append(Integer.toString(eegPower.getInt("highBeta")) + ',');
					writer.append(Integer.toString(eegPower.getInt("lowGamma")) + ',');
					writer.append(Integer.toString(eegPower.getInt("highGamma")));
					
				} else{
					writer.append("0,"); 
					writer.append("0,"); 
					writer.append("0,"); 
					writer.append("0,"); 
					writer.append("0,"); 
					writer.append("0,"); 
					writer.append("0,"); 
					writer.append("0"); 
				}
				
				// JH: make sure all lines end with \n
				writer.append('\n'); 

				writer.flush();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}// end while 
		
		writer.flush();
		writer.close();
		
	}

}
