package com.jornack.skyscraper.window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.ui.RefineryUtilities;

import com.jornack.skyscraper.util.LineChartGenerator;
import com.jornack.skyscraper.util.Logger;
import com.jornack.skyscraper.util.PreferenceManager;

/**
 * <p>Title:		ChartGeneratorWindow</p><br>
 * <p>Description:	ChartGeneratorWindow Window</p><br>
 * @author		    Jornack
 *
 * $Author: Jornack $
 */


public class PreferencesWindow extends JFrame {

	public static final String PREFERENCES_DEBUG = "skyskraper.preferences.debug";
	/**
	 * 
	 */
	private static final long serialVersionUID = 6072199621342421682L;
	private static final String ACTION_ADD = "ACTION_ADD";
	public static final String CHART_GENERATION_LINETHICKNESS = "skyskraper.chart.generation.linethickness";
	private static final String FORMAT_JPG = "JPG";
	private static final String FORMAT_PNG = "PNG";
	public static final String CHART_GENERATION_FORMAT = "skyskraper.chart.generation.format";
	public static final String CHART_GENERATION_WIDTH = "skyskraper.chart.generation.width";
	public static final String CHART_GENERATION_HEIGHT = "skyskraper.chart.generation.height";
	public static final String CHART_GENERATION_IGNORE_PSL = "skyskraper.chart.generation.ignore.psl";
	public static final String CHART_GENERATION_IGNORE_ESENSE = "skyskraper.chart.generation.ignore.esense";
	public static final String CHART_GENERATION_IGNORE_EEGPWR = "skyskraper.chart.generation.ignore.eegpwr";
	public static final String THINKGEAR_DEFAULT_HOST = "skyskraper.thinkgear.default.host";
	public static final String THINKGEAR_DEFAULT_PORT = "skyskraper.thinkgear.default.port";
	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final int DEFAULT_PORT = 13854;
	public  static final String THINKGEAR_RAW_OUTPUT = "skyskraper.thinkgear.rawoutput";
	private JTextField textChartWidth;
	private JTextField textChartHeight;
	private JTextField textChartLineThickness;
	private JPanel statsPanel= null;
	private JTextField textFilename = null;
	private JTabbedPane contentPane;
	private JCheckBox debugChk;
	private JTextField hostTxt;
	private JLabel portLbl;
	private JTextField portTxt;
	private JCheckBox rawOutputChk;
	
	

	/**
	 * Launch the application. For testing purposes only. 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreferencesWindow frame = new PreferencesWindow();
					frame.setVisible(true);
					RefineryUtilities.centerFrameOnScreen(frame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void activateChartGenerationTab(){
		contentPane.setSelectedIndex(1);
	}
	public void activatePrefsTab(){
		contentPane.setSelectedIndex(0);
	}
	/**
	 * Create the frame.
	 */
	public PreferencesWindow() {

		setTitle("Preferences");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 1024, 512);
		contentPane = new JTabbedPane();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JComponent panel1 = makePreferencesPanel();
		JComponent panel2 = makeChartGenerationPanel();
		//JComponent panel2 = makeSkySkraperPanel02("Panel #1");
		
		contentPane.addTab("Preferences",  panel1 );
		contentPane.addTab("Chart Generation",  panel2 );
		//contentPane.addTab("Statistics", null, panel2, "Does nothing");
		
		setContentPane(contentPane);

	}
	protected JComponent makePreferencesPanel(){
		Logger.debug("Making PreferencesPanel");
		JPanel panel = new JPanel(false);
		SpringLayout spring = new SpringLayout();
		panel.setLayout(spring);
		
		
		JLabel debugLbl = new JLabel("Enable debug logging");
		spring.putConstraint(SpringLayout.NORTH, debugLbl, 5, SpringLayout.NORTH, panel);
		spring.putConstraint(SpringLayout.WEST, debugLbl, 5, SpringLayout.WEST, panel);
		panel.add(debugLbl);
		debugChk = new JCheckBox();
		debugChk.setSelected(PreferenceManager.getPreferences().getBoolean(PREFERENCES_DEBUG, false)	);
		spring.putConstraint(SpringLayout.NORTH, debugChk, 5, SpringLayout.NORTH, panel);
		spring.putConstraint(SpringLayout.WEST, debugChk, 10, SpringLayout.EAST, debugLbl);
		panel.add(debugChk);
		
		JLabel hostLbl = new JLabel("Thinkgear host");
		spring.putConstraint(SpringLayout.NORTH, hostLbl, 5, SpringLayout.SOUTH, debugLbl);
		spring.putConstraint(SpringLayout.WEST, hostLbl, 5, SpringLayout.WEST, panel);
		panel.add(hostLbl);
		
		hostTxt = new JTextField();
		hostTxt.setText(PreferenceManager.getPreferences().get(PreferencesWindow.THINKGEAR_DEFAULT_HOST, DEFAULT_HOST));
		spring.putConstraint(SpringLayout.NORTH, hostTxt, 0, SpringLayout.NORTH, hostLbl);
		spring.putConstraint(SpringLayout.WEST, hostTxt, 0, SpringLayout.WEST, debugChk);
		hostTxt.setColumns(15);
		panel.add(hostTxt);
		
		portLbl = new JLabel("Thinkgear host");
		spring.putConstraint(SpringLayout.NORTH, portLbl, 5, SpringLayout.SOUTH, hostLbl);
		spring.putConstraint(SpringLayout.WEST, portLbl, 5, SpringLayout.WEST, panel);
		panel.add(portLbl);
		
		portTxt = new JTextField();
		portTxt.setText(Integer.toString(PreferenceManager.getPreferences().getInt(PreferencesWindow.THINKGEAR_DEFAULT_PORT, DEFAULT_PORT)));
		debugChk.setSelected(PreferenceManager.getPreferences().getBoolean(PREFERENCES_DEBUG, false)	);
		spring.putConstraint(SpringLayout.NORTH, portTxt, 0, SpringLayout.NORTH, portLbl);
		spring.putConstraint(SpringLayout.WEST, portTxt, 0, SpringLayout.WEST, hostTxt);
		portTxt.setColumns(5);
		panel.add(portTxt);
		
		JLabel rawOutputLbl = new JLabel("Thinkgear raw output");
		spring.putConstraint(SpringLayout.NORTH, rawOutputLbl, 5, SpringLayout.SOUTH, portLbl);
		spring.putConstraint(SpringLayout.WEST, rawOutputLbl, 5, SpringLayout.WEST, panel);
		panel.add(rawOutputLbl);
		
		rawOutputChk = new JCheckBox();
		rawOutputChk.setSelected(PreferenceManager.getPreferences().getBoolean(THINKGEAR_RAW_OUTPUT, false)	);
		spring.putConstraint(SpringLayout.NORTH, rawOutputChk, 5, SpringLayout.NORTH, rawOutputLbl);
		spring.putConstraint(SpringLayout.WEST, rawOutputChk, 0, SpringLayout.WEST, hostTxt);
		panel.add(rawOutputChk);
		
		JButton btnCancel = new JButton("Cancel");

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});
		spring.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, panel);
		spring.putConstraint(SpringLayout.EAST, btnCancel, -104, SpringLayout.EAST, panel);

		panel.add(btnCancel);

		JButton btnGenerate = new JButton("Ok");
		spring.putConstraint(SpringLayout.NORTH, btnGenerate, 0, SpringLayout.NORTH, btnCancel);
		spring.putConstraint(SpringLayout.WEST, btnGenerate, 5, SpringLayout.EAST, btnCancel);
		spring.putConstraint(SpringLayout.EAST, btnGenerate, -5, SpringLayout.EAST, panel);
		
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PreferenceManager.getPreferences().putBoolean(PREFERENCES_DEBUG, debugChk.isSelected());
				PreferenceManager.getPreferences().putInt(PreferencesWindow.THINKGEAR_DEFAULT_PORT, DEFAULT_PORT);
				PreferenceManager.getPreferences().put(PreferencesWindow.THINKGEAR_DEFAULT_HOST , DEFAULT_HOST);
				PreferenceManager.getPreferences().putBoolean(THINKGEAR_RAW_OUTPUT, rawOutputChk.isSelected());
				dispose();
				

			}
		});
		spring.putConstraint(SpringLayout.NORTH, btnGenerate, 0, SpringLayout.NORTH, btnCancel);
		spring.putConstraint(SpringLayout.WEST, btnGenerate, 4, SpringLayout.EAST, btnCancel);
		panel.add(btnGenerate);
		return panel;
	}
	protected JComponent makeStatisticsPanel(String text) {
		Logger.log("Making StatsPanel");
		final JPanel panel = new JPanel(false);
		SpringLayout spring = new SpringLayout();
		panel.setLayout(spring);

		JButton addbtn = new JButton("Add");
		addbtn.setActionCommand(ACTION_ADD);
		spring.putConstraint(SpringLayout.NORTH, addbtn, 5, SpringLayout.NORTH, panel);
		spring.putConstraint(SpringLayout.EAST, addbtn, -5, SpringLayout.EAST, panel);
		addbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createColumn(statsPanel);
				
			}
		});
		panel.add(addbtn);
		
		statsPanel = new JPanel();
		statsPanel.setBorder(BorderFactory.createLineBorder (Color.lightGray, 2));
		spring.putConstraint(SpringLayout.NORTH, statsPanel, 5, SpringLayout.SOUTH, addbtn);
		spring.putConstraint(SpringLayout.EAST, statsPanel, -5, SpringLayout.EAST, panel);
		spring.putConstraint(SpringLayout.SOUTH, statsPanel, 5, SpringLayout.SOUTH, panel);
		spring.putConstraint(SpringLayout.WEST, statsPanel, 5, SpringLayout.WEST, panel);
		
		panel.add(statsPanel);
		return panel;
	}
	private JComponent createColumn(JComponent parent){
		JPanel panel = new JPanel();
		SpringLayout spring = new SpringLayout();
		panel.setLayout(spring);
		panel.setBorder(BorderFactory.createLineBorder (Color.lightGray, 2));
		String[] waves = {"Delta    ",
				"Theta    ",
				"LowAlpha ",
				"HighAlpha",
				"LowBeta  ",
				"HighBeta ",
				"LowGamma ",
				"HighGamma"};
		JComponent last = parent;
		for (int i = 0; i < waves.length; i++) {
			String string = waves[i];
			JLabel label = new JLabel(string);
			spring.putConstraint(SpringLayout.NORTH, label, 50, SpringLayout.NORTH, last);
			spring.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, panel);
			spring.putConstraint(SpringLayout.EAST, label, 100, SpringLayout.WEST, label);
			last = label;
			panel.add(label);
			
		}
		panel.setPreferredSize(new Dimension(120,400));
		
		parent.add(panel);
		
		return panel;
	}
	
	
	
	
	protected JComponent makeChartGenerationPanel() {
		Logger.debug("Making ChartGeneratorPanel");
		final JPanel panel = new JPanel(false);
		SpringLayout spring = new SpringLayout();
		panel.setLayout(spring);

		
		JLabel lblChartWidth = new JLabel("Image size");
		spring.putConstraint(SpringLayout.NORTH, lblChartWidth, 5, SpringLayout.NORTH, panel);
		spring.putConstraint(SpringLayout.WEST, lblChartWidth, 5, SpringLayout.WEST, panel);
		
		textChartWidth = new JTextField();
		textChartWidth.setHorizontalAlignment(JTextField.RIGHT );
		textChartWidth.setText(PreferenceManager.getPreferences().get(CHART_GENERATION_WIDTH, "4096"));
		spring.putConstraint(SpringLayout.NORTH, textChartWidth, 5, SpringLayout.NORTH, panel);
		spring.putConstraint(SpringLayout.WEST, textChartWidth, 5, SpringLayout.EAST, lblChartWidth);
		panel.add(textChartWidth);
		textChartWidth.setColumns(10);

		panel.add(lblChartWidth);
		
		JLabel lblChartHeight = new JLabel("X");
		spring.putConstraint(SpringLayout.NORTH, lblChartHeight, 5, SpringLayout.NORTH, panel);
		spring.putConstraint(SpringLayout.WEST, lblChartHeight, 5, SpringLayout.EAST, textChartWidth);
		panel.add(lblChartHeight);
		
		textChartHeight = new JTextField();
		textChartHeight.setText(PreferenceManager.getPreferences().get(CHART_GENERATION_HEIGHT, "768"));
		spring.putConstraint(SpringLayout.NORTH, textChartHeight, 5, SpringLayout.NORTH, panel);
		spring.putConstraint(SpringLayout.WEST, textChartHeight, 5, SpringLayout.EAST, lblChartHeight);
		textChartHeight.setColumns(10);
		panel.add(textChartHeight);

		//
		
		JRadioButton radioPNG = new JRadioButton(FORMAT_PNG);
		
		radioPNG.setActionCommand(FORMAT_PNG);
		JRadioButton radioJPG = new JRadioButton(FORMAT_JPG); 
		radioJPG.setActionCommand(FORMAT_JPG);
		if (PreferenceManager.getPreferences().get(CHART_GENERATION_FORMAT, FORMAT_PNG).equals(FORMAT_PNG)){
			radioPNG.setSelected(true);
		}else{
			radioJPG.setSelected(true);
		}
		final ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(radioJPG); 
		buttonGroup.add(radioPNG);
		
		JPanel radioPanel = new JPanel(new GridLayout(0, 2));
        radioPanel.add(radioPNG);
        radioPanel.add(radioJPG);
        spring.putConstraint(SpringLayout.WEST, radioPanel, 0, SpringLayout.WEST, textChartHeight);
		spring.putConstraint(SpringLayout.NORTH, radioPanel, 50, SpringLayout.NORTH, textChartHeight);
		panel.add(radioPanel);
		
		JLabel lblChartFormat = new JLabel("Format");
		spring.putConstraint(SpringLayout.WEST, lblChartFormat, 0, SpringLayout.WEST, lblChartWidth);
		spring.putConstraint(SpringLayout.NORTH, lblChartFormat, 50, SpringLayout.NORTH, lblChartHeight);
		
		panel.add(lblChartFormat);
		
		textChartLineThickness = new JTextField();
		textChartLineThickness.setText(PreferenceManager.getPreferences().get(CHART_GENERATION_LINETHICKNESS, "2"));
		textChartLineThickness.setColumns(2);
		spring.putConstraint(SpringLayout.WEST, textChartLineThickness, 0, SpringLayout.WEST, radioPanel);
		spring.putConstraint(SpringLayout.NORTH, textChartLineThickness, 50, SpringLayout.NORTH, radioPanel);
		panel.add(textChartLineThickness);
		
		JLabel lblChartLineThickness = new JLabel("Line thickness");
		spring.putConstraint(SpringLayout.WEST, lblChartLineThickness, 0, SpringLayout.WEST, lblChartFormat);
		spring.putConstraint(SpringLayout.NORTH, lblChartLineThickness, 50, SpringLayout.NORTH, lblChartFormat);
		panel.add(lblChartLineThickness);
		
		JLabel lblIgnorePoorSignalLevel = new JLabel("Ignore Poor Signal Level");
		spring.putConstraint(SpringLayout.WEST, lblIgnorePoorSignalLevel, 0, SpringLayout.WEST, lblChartLineThickness);
		spring.putConstraint(SpringLayout.NORTH, lblIgnorePoorSignalLevel, 50, SpringLayout.NORTH, lblChartLineThickness);
		panel.add(lblIgnorePoorSignalLevel);
		
		final JCheckBox chkPoorSignalLevel = new JCheckBox();
		chkPoorSignalLevel.setActionCommand("IGNORE_PSL");
		spring.putConstraint(SpringLayout.WEST, chkPoorSignalLevel,0 , SpringLayout.WEST, textChartLineThickness);
		spring.putConstraint(SpringLayout.NORTH, chkPoorSignalLevel, 0, SpringLayout.NORTH, lblIgnorePoorSignalLevel);
		panel.add(chkPoorSignalLevel);
		
		JLabel lblIgnoreESence = new JLabel("Ignore eSense");
		spring.putConstraint(SpringLayout.WEST, lblIgnoreESence, 0, SpringLayout.WEST, lblIgnorePoorSignalLevel);
		spring.putConstraint(SpringLayout.NORTH, lblIgnoreESence, 50, SpringLayout.NORTH, lblIgnorePoorSignalLevel);
		panel.add(lblIgnoreESence);
		
		final JCheckBox chkESense = new JCheckBox();
		chkESense.setActionCommand("IGNORE_ESENSE");
		spring.putConstraint(SpringLayout.WEST, chkESense, 0, SpringLayout.WEST, chkPoorSignalLevel);
		spring.putConstraint(SpringLayout.NORTH, chkESense, 50, SpringLayout.NORTH, chkPoorSignalLevel);
		panel.add(chkESense);
		
		JLabel lblIgnoreEEGPower = new JLabel("Ignore eegPower");
		spring.putConstraint(SpringLayout.WEST, lblIgnoreEEGPower, 0, SpringLayout.WEST, lblIgnoreESence);
		spring.putConstraint(SpringLayout.NORTH, lblIgnoreEEGPower, 50, SpringLayout.NORTH, lblIgnoreESence);
		panel.add(lblIgnoreEEGPower);
		
		final JCheckBox chkEEGPower = new JCheckBox();
		chkPoorSignalLevel.setActionCommand("IGNORE_EEGPWR");
		spring.putConstraint(SpringLayout.WEST, chkEEGPower, 0, SpringLayout.WEST, chkESense);
		spring.putConstraint(SpringLayout.NORTH, chkEEGPower, 50, SpringLayout.NORTH, chkESense);
		panel.add(chkEEGPower);
		
		JLabel lblFilename = new JLabel("Filename");
		spring.putConstraint(SpringLayout.WEST, lblFilename, 0, SpringLayout.WEST, lblIgnoreEEGPower);
		spring.putConstraint(SpringLayout.NORTH, lblFilename, 50, SpringLayout.NORTH, lblIgnoreEEGPower);
		panel.add(lblFilename);
		
		textFilename = new JTextField();
		spring.putConstraint(SpringLayout.WEST, textFilename, 0, SpringLayout.WEST, textChartLineThickness);
		spring.putConstraint(SpringLayout.NORTH, textFilename, 0, SpringLayout.NORTH, lblFilename);
		spring.putConstraint(SpringLayout.EAST, textFilename, -100, SpringLayout.EAST, panel);
		panel.add(textFilename);
		JButton btnbrowse = new JButton("Browse");
		
		btnbrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".json files", "json");
				fc.setFileFilter(filter);
				fc.showOpenDialog(panel);
				File selFile = fc.getSelectedFile();
				if (selFile != null) {
					
					textFilename.setText((selFile.getAbsolutePath()));
				}


				
			}
		});
		spring.putConstraint(SpringLayout.WEST, btnbrowse, 5, SpringLayout.EAST, textFilename);
		spring.putConstraint(SpringLayout.NORTH, btnbrowse, -4, SpringLayout.NORTH, textFilename);
		spring.putConstraint(SpringLayout.EAST, btnbrowse, -5, SpringLayout.EAST, panel);
		panel.add(btnbrowse);
		
		
		JButton btnCancel = new JButton("Cancel");

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});
		spring.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, panel);
		spring.putConstraint(SpringLayout.EAST, btnCancel, -104, SpringLayout.EAST, panel);

		panel.add(btnCancel);

		JButton btnGenerate = new JButton("Generate");
		spring.putConstraint(SpringLayout.NORTH, btnGenerate, 0, SpringLayout.NORTH, btnCancel);
		spring.putConstraint(SpringLayout.WEST, btnGenerate, 5, SpringLayout.EAST, btnCancel);
		spring.putConstraint(SpringLayout.EAST, btnGenerate, -5, SpringLayout.EAST, panel);
		
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try{
					Integer.parseInt(textChartWidth.getText());
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(null, "Invalid Chart generation width.");
					return;
				}if (textChartWidth.getText().length() == 0 || new Integer(textChartWidth.getText())== 0 ) {
					JOptionPane.showMessageDialog(null, "Invalid Chart generation width.");
					return;
				}
				
				PreferenceManager.getPreferences().put(CHART_GENERATION_WIDTH, textChartWidth.getText());
				
				try{
					Integer.parseInt(textChartHeight.getText());
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(null, "Invalid Chart generation height.");
					return;
				}if (textChartHeight.getText().length() == 0|| new Integer(textChartHeight.getText())== 0) {
					JOptionPane.showMessageDialog(null, "Invalid Chart generation height.");
					return;
				}
				
				PreferenceManager.getPreferences().put(CHART_GENERATION_FORMAT , buttonGroup.getSelection().getActionCommand());
				try{
					Integer.parseInt(textChartLineThickness.getText());
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(null, "Invalid Line thickness.");
					return;
				}if (textChartLineThickness.getText().length() == 0|| new Integer(textChartLineThickness.getText())== 0) {
					JOptionPane.showMessageDialog(null, "Invalid Line thickness.");
					return;
				}
				if (textFilename.getText() != null && new File(textFilename.getText()).exists()==false){
					JOptionPane.showMessageDialog(null, "File does not exist.");
					return;
				}
				PreferenceManager.getPreferences().putBoolean(CHART_GENERATION_IGNORE_PSL , chkPoorSignalLevel.isSelected());
				PreferenceManager.getPreferences().putBoolean(CHART_GENERATION_IGNORE_ESENSE , chkESense.isSelected());
				PreferenceManager.getPreferences().putBoolean(CHART_GENERATION_IGNORE_EEGPWR , chkEEGPower.isSelected());
				
				PreferenceManager.getPreferences().put(CHART_GENERATION_LINETHICKNESS , textChartLineThickness.getText());

				File file = new File(textFilename.getText());
						
				if (file != null) {
					LineChartGenerator generator = new LineChartGenerator();
					generator.generate(file);
				}
				
				JOptionPane.showMessageDialog(null, "Generation complete.");
				
				

			}
		});
		spring.putConstraint(SpringLayout.NORTH, btnGenerate, 0, SpringLayout.NORTH, btnCancel);
		spring.putConstraint(SpringLayout.WEST, btnGenerate, 4, SpringLayout.EAST, btnCancel);
		panel.add(btnGenerate);
	
		
		return panel;
	}

	
}
