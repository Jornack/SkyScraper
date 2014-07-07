package com.jornack.skyscraper.util;

import java.util.prefs.Preferences;

public class PreferenceManager
{

    private static Preferences prefs = null;
    ;
	public static final String SAVE = "skyskraper.save";
    public static final String SAVE_FILENAME = "skyskraper.save.filename";
    public static final String CHART_GENERATION_LINETHICKNESS
            = "skyskraper.chart.generation.linethickness";
    public static final String CHART_GENERATION_FORMAT_JPG = "JPG";
    public static final String CHART_GENERATION_FORMAT_PNG = "PNG";
    public static final String CHART_GENERATION_FORMAT
            = "skyskraper.chart.generation.format";
    public static final String CHART_GENERATION_WIDTH
            = "skyskraper.chart.generation.width";
    public static final String CHART_GENERATION_HEIGHT
            = "skyskraper.chart.generation.height";
    public static final String CHART_GENERATION_IGNORE_PSL
            = "skyskraper.chart.generation.ignore.psl";
    public static final String CHART_GENERATION_IGNORE_ESENSE
            = "skyskraper.chart.generation.ignore.esense";
    public static final String CHART_GENERATION_IGNORE_EEGPWR
            = "skyskraper.chart.generation.ignore.eegpwr";
    public static final String THINKGEAR_DEFAULT_HOST
            = "skyskraper.thinkgear.default.host";
    public static final String THINKGEAR_DEFAULT_PORT
            = "skyskraper.thinkgear.default.port";
    public static final String THINKGEAR_RAW_OUTPUT
            = "skyskraper.thinkgear.rawoutput";
    public static final String PREFERENCES_DEBUG
            = "skyskraper.preferences.debug";
    public static final String FORMAT_PNG = "PNG";
    public static final String FORMAT_JPG = "JPG";
    public static final String FORMAT_EXCEL = "Excel";
    public static final String FORMAT_CSV = "CSV";

    private PreferenceManager()
    {
    }

    public static Preferences getPreferences()
    {
        if (prefs == null)
        {
            prefs = Preferences.userRoot().node(PreferenceManager.class.
                    getName());
        }
        return prefs;

    }

}
