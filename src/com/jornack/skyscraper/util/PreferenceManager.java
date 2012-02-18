package com.jornack.skyscraper.util;

import java.util.prefs.Preferences;

public class PreferenceManager {
	
	private static Preferences prefs = null;;
	public static final String SAVE = "skyskraper.save";
	public static final String SAVE_FILENAME = "skyskraper.save.filename";
	
	private PreferenceManager(){}
	public static Preferences getPreferences() {
		if (prefs == null){
			prefs = Preferences.userRoot().node(PreferenceManager.class.getName());
		}
		return prefs;
		
	}
	
	
}
