package com.jornack.skyscraper.util;

import java.util.prefs.Preferences;

/**
 * <p>Title:		PreferencesManager</p><br>
 * <p>Description:	Preference Manager to read/write system tray app settings</p><br>
 * @author		    <a href="http://eric-blue.com">Eric Blue</a><br>
 *
 * $Date: 2011-07-24 17:54:27 $ 
 * $Author: ericblue76 $
 * $Revision: 1.2 $
 *
 */


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
