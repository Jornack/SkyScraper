/**
 * 
 */
package com.jornack.skyscraper.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jornack.skyscraper.window.PreferencesWindow;

/**
 * @author Jornack
 *
 */
public class Logger {
	private static JTextArea area;
	private static JScrollPane scrollpane;
	private Logger(){}
	
	public static void log(String text){
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
			
		area.append(fmt.format(new Date()).toString());
		area.append(" - " );
		area.append(text);
		area.append("\n");
		//area.setCaretPosition(pos-1);
		scrollpane.getVerticalScrollBar().setValue( scrollpane.getVerticalScrollBar().getMaximum()); 
	}
	public static void log(Object o){
		log(o.toString());
	}
	public static void debug(Object o){
		debug(o.toString());
	}
	public static void debug(String text){
		if (PreferenceManager.getPreferences().getBoolean(PreferenceManager.PREFERENCES_DEBUG, false)){
			log(text);
		}
		
	}
	public static void setScrollpane(JScrollPane scrollpane) {
		Logger.scrollpane = scrollpane;
	}

	
	public static void setArea(JTextArea area) {
		Logger.area = area;
	}

}
