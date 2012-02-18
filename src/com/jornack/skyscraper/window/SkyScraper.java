package com.jornack.skyscraper.window;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.json.JSONException;
import org.json.JSONObject;

//import com.ericblue.mindstream.client.ThinkGearSocketClient;

import com.jornack.skyscraper.util.Logger;
import com.jornack.skyscraper.util.PreferenceManager;
import com.jornack.skyscraper.util.ThinkGearConnector;

/**
 * <p>Title:		SkyScraper</p><br>
 * <p>Description:	Displays NeuroSky EEG data in a realtime BarChart with the ability to save and generate LineCharts to JPG/PNG</p><br>
 * 
 * @author		    Jornack
 *
 * $Author: Jornack $
 */
public class SkyScraper extends ApplicationFrame implements ActionListener, MouseListener{

	private static final String UNABLED_TO_BROWSE = "I'm not abled to open the PayPal webpage in you browser. Please visit the following URL manually to proceed:\n\n\thttp://bit.ly/zZisRg";
	private final static String APP_NAME = "SkyScraper";
	private final static String APP_VERSION = "0.1a";
	
	
	private static final boolean CHART_TYPE_LINE = false;
	private static final boolean CHART_TYPE_BAR = true;
	private static final String ACTION_CMD_LINECHART = "ACTION_LINECHART";
	private static final String ACTION_CMD_BARCHART = "ACTION_BARCHART";
	private static final String ACTION_CMD_CONNECT = "CONNECT";
	private static final String ACTION_CMD_DISCONNECT =  "DISCONNECT";
	private static final String ACTION_CMD_SAVE = "SAVE";
	private static final String ACTION_CMD_STOP_SAVE = "STOP_SAVE";
	private static final String ACTION_CMD_GENERATE_CHARTS = "GENERATE_CHARTS";
	private static final String ACTION_CMD_STATS = "STATS";
	private static final String ACTION_CMD_PREFERENCES = "PREFERENCES";
	
	private DefaultCategoryDataset altDataSet = null;
	private DefaultCategoryDataset wavesDataSet = null;
	private ThinkGearHandlerSwingWorker thinkGearHandlerSwingWorker = null;
	private JTextField signalStrengthText;
	
	private JButton saveBtn;
	private PreferencesWindow pw = null;
	private final static int INDEX_ATTENTIONSERIES            = 0;
	private final static int INDEX_MEDITATIONSERIES           = 1;
	private final static int INDEX_DELTASERIES                = 0;
	private final static int INDEX_THETASERIES                = 1;
	private final static int INDEX_LOWALPHASERIES             = 2;
	private final static int INDEX_HIGHALPHASERIES            = 3;
	private final static int INDEX_LOWBETASERIES              = 4;
	private final static int INDEX_HIGHBETASERIES             = 5;
	private final static int INDEX_LOWGAMMASERIES             = 6;
	private final static int INDEX_HIGHGAMASERIES             = 7;
	private final static float LINE_THINKNESS 				  = 2f;
	
	
	private TimeSeriesCollection eegDataset = null;
	private TimeSeriesCollection eSenseDataset = null;
	
	private static TimeSeries poorSignalLevelSeries      ;
	private static TimeSeries attentionSeries            ;
	private static TimeSeries meditationSeries           ;
	private static TimeSeries deltaSeries                ;
	private static TimeSeries thetaSeries                ;
	private static TimeSeries lowAlphaSeries             ;
	private static TimeSeries highAlphaSeries            ;
	private static TimeSeries lowBetaSeries              ;
	private static TimeSeries highBetaSeries             ;
	private static TimeSeries lowGammaSeries             ;
	private static TimeSeries highGamaSeries             ;
	private ChartPanel eSenseChartPanel;
	private ChartPanel eegPowerChartPanel;
	private static JTextArea debugArea;
	private static JScrollPane debugScrollPane;
	private boolean charType = true;
	private final static String[] buttonlabels = {"Connect",
		"", // 
		"Save",
		"Generate charts",
		"Preferences"
		};
	
	private final static String[] buttonActions = {ACTION_CMD_CONNECT,
		"", 
		ACTION_CMD_SAVE,
		ACTION_CMD_GENERATE_CHARTS,
		ACTION_CMD_STATS,//"", // 
		ACTION_CMD_PREFERENCES 
		};
	
	public SkyScraper(String title) {
		super(title);
		JPanel content = new JPanel();
        SpringLayout spring = new SpringLayout();
        content.setLayout(spring);
        
        JScrollPane debugPanel = createdebugPanel();
        
        debugPanel.setPreferredSize(new Dimension(175, 270));;
        spring.putConstraint(SpringLayout.NORTH, debugPanel, -100, SpringLayout.SOUTH, content);
        spring.putConstraint(SpringLayout.EAST, debugPanel, 0, SpringLayout.EAST, content);
        spring.putConstraint(SpringLayout.WEST, debugPanel, 0, SpringLayout.WEST, content);
        spring.putConstraint(SpringLayout.SOUTH, debugPanel, 0, SpringLayout.SOUTH, content);
       
        
        Logger.log(APP_NAME + " " + APP_VERSION);
		
		Logger.log("Creating datasets");
        //dataset = new TimeSeriesCollection();
		altDataSet = new DefaultCategoryDataset();
		wavesDataSet = new DefaultCategoryDataset();
		Logger.log("Creating Collections");
        this.eegDataset = new TimeSeriesCollection();
        this.eSenseDataset = new TimeSeriesCollection();
        
        this.poorSignalLevelSeries     = new TimeSeries ("Poor Signal Level"); 
		this.attentionSeries           = new TimeSeries ("Attention");        
		this.meditationSeries          = new TimeSeries ("Meditation");       
		this.deltaSeries               = new TimeSeries ("Delta");            
		this.thetaSeries               = new TimeSeries ("Theta ");           
		this.lowAlphaSeries            = new TimeSeries ("Low Alpha");        
		this.highAlphaSeries           = new TimeSeries ("High Alpha");       
		this.lowBetaSeries             = new TimeSeries ("Low Beta");         
		this.highBetaSeries            = new TimeSeries ("High Beta");        
		this.lowGammaSeries            = new TimeSeries ("Low Gamma");        
		this.highGamaSeries            = new TimeSeries ("High Gama");        
		
		this.eSenseDataset.addSeries(this.attentionSeries           );
		this.eSenseDataset.addSeries(this.meditationSeries          );
		
        this.eegDataset.addSeries(this.deltaSeries               );
		this.eegDataset.addSeries(this.thetaSeries               );
		this.eegDataset.addSeries(this.lowAlphaSeries            );
		this.eegDataset.addSeries(this.highAlphaSeries           );
		this.eegDataset.addSeries(this.lowBetaSeries             );
		this.eegDataset.addSeries(this.highBetaSeries            );
		this.eegDataset.addSeries(this.lowGammaSeries            );
		this.eegDataset.addSeries(this.highGamaSeries            );
		
        
		Logger.log("Creating Panels");
        JPanel buttonPanel = createButtonPanel();
		spring.putConstraint(SpringLayout.NORTH, buttonPanel, 15, SpringLayout.NORTH, content);
		spring.putConstraint(SpringLayout.EAST, buttonPanel, -5, SpringLayout.EAST, content);
		spring.putConstraint(SpringLayout.WEST, buttonPanel, -125, SpringLayout.EAST, buttonPanel);
		
		JFreeChart eSenseBarChart = createESenseBarChart(altDataSet,"", "eSense");
		JFreeChart eegPowerBarChart = createEEGPowerBarChart(wavesDataSet,"", "eegPower");
		JFreeChart eSenseLineChart = createESenseLineChart(eSenseDataset);
		JFreeChart eegPowerLineChart = createEEGPowerLineChart(eegDataset);
		
		eSenseChartPanel = new ChartPanel(eSenseBarChart);
		spring.putConstraint(SpringLayout.NORTH, eSenseChartPanel, 5, SpringLayout.NORTH, content);
		spring.putConstraint(SpringLayout.WEST, eSenseChartPanel, 5, SpringLayout.WEST, content);
		spring.putConstraint(SpringLayout.EAST, eSenseChartPanel, 275, SpringLayout.WEST, eSenseChartPanel);
		spring.putConstraint(SpringLayout.SOUTH, eSenseChartPanel, -5, SpringLayout.NORTH, debugPanel);
	       
        
		eegPowerChartPanel = new ChartPanel(eegPowerBarChart);
		spring.putConstraint(SpringLayout.NORTH, eegPowerChartPanel, 5, SpringLayout.NORTH, content);
        spring.putConstraint(SpringLayout.WEST, eegPowerChartPanel, 5, SpringLayout.EAST, eSenseChartPanel);
        spring.putConstraint(SpringLayout.EAST, eegPowerChartPanel, -5, SpringLayout.WEST, buttonPanel);
        spring.putConstraint(SpringLayout.SOUTH, eegPowerChartPanel, -5, SpringLayout.NORTH, debugPanel);
        
        JPanel donatePanel = createDonatePanel();
        spring.putConstraint(SpringLayout.NORTH, donatePanel, 35, SpringLayout.SOUTH, buttonPanel);
        spring.putConstraint(SpringLayout.EAST, donatePanel, 0, SpringLayout.EAST, buttonPanel);
        spring.putConstraint(SpringLayout.WEST, donatePanel, 0, SpringLayout.WEST, buttonPanel);
        content.add(eegPowerChartPanel);
        content.add(eSenseChartPanel);
        
        content.add(buttonPanel );
        
        content.add (donatePanel);
        content.add(debugPanel);
        //content.add(infoJPanel,BorderLayout.SOUTH );
        //content.add(createInfoPanel(),BorderLayout.SOUTH );
        //eSenseChartPanel.setPreferredSize(new Dimension(175, 270));;
        //eegPowerChartPanel.setPreferredSize(new Dimension(1024, 512));;
        Logger.log("Setting sizes");
        buttonPanel.setPreferredSize(new Dimension(200, 250));;
        content.setPreferredSize(new Dimension(1124, 512));
        donatePanel.setPreferredSize(new Dimension(74,21));
        
        setContentPane(content);
        Logger.log("Making up stuff to test this TextArea");
        pw = new PreferencesWindow();
        pw.setVisible(false);
        Logger.log("Initialization done.");
        
        
	}
	
	
	
	private JScrollPane createdebugPanel(){
		JPanel panel = new JPanel();
        SpringLayout spring = new SpringLayout();
        panel.setLayout(spring);
        
        debugArea = new JTextArea();
        debugArea.setAutoscrolls(true);
        debugArea.setEnabled(true);
        debugArea.setForeground(Color.black);
        spring.putConstraint(SpringLayout.NORTH, debugArea, 5, SpringLayout.NORTH, panel);
        spring.putConstraint(SpringLayout.WEST, debugArea, 5, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, debugArea, -5, SpringLayout.EAST, panel);
        spring.putConstraint(SpringLayout.SOUTH, debugArea, -5, SpringLayout.SOUTH, panel);
        panel.add(debugArea);
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        debugScrollPane = new JScrollPane(debugArea);
        
        Logger.setArea(debugArea);
        Logger.setScrollpane(debugScrollPane);
        
        return debugScrollPane;
	}
	
	private JPanel createDonatePanel() {
		JPanel panel = new JPanel();
        SpringLayout spring = new SpringLayout();
        panel.setLayout(spring);
        
                 
        JLabel imageLbl = null;
        //try {
			//BufferedImage image = ImageIO.read(new URL("https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif"));
			imageLbl = new JLabel(new ImageIcon(ClassLoader.getSystemResource("images/btn_donate_SM.gif"))){
			      public JToolTip createToolTip() {
			          JToolTip tip = super.createToolTip();
			          tip.setBackground(Color.white);
			          tip.setForeground(Color.black);
			          return tip;
			        }

			        public boolean contains(int x, int y) {
			          
			            setToolTipText("Please donate if you like what I do :)");
			          
			          return super.contains(x, y);
			        }
			      };
			imageLbl.addMouseListener(this);
			spring.putConstraint(SpringLayout.EAST, imageLbl, -25, SpringLayout.EAST, panel);
//			spring.putConstraint(SpringLayout.SOUTH, imageLbl, 0, SpringLayout.SOUTH, panel);
//			spring.putConstraint(SpringLayout.NORTH, imageLbl, 47, SpringLayout.SOUTH, imageLbl);
			
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//} 
        
        panel.add(imageLbl);
        
        panel.setPreferredSize(new Dimension(74,21));
		return panel;
	}

	private JPanel createButtonPanel(){
		JPanel panel = new JPanel();
        SpringLayout spring = new SpringLayout();
        panel.setLayout(spring);
        
        JButton connectBtn = new JButton("Connect");
        connectBtn.setActionCommand(ACTION_CMD_CONNECT);
        connectBtn.addActionListener(this);
        spring.putConstraint(SpringLayout.NORTH, connectBtn, 10, SpringLayout.NORTH, panel);
        spring.putConstraint(SpringLayout.WEST, connectBtn, 5, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, connectBtn, -10, SpringLayout.EAST, panel);
        panel.add(connectBtn);
       
        saveBtn = new JButton("Save");
        saveBtn.setActionCommand(ACTION_CMD_SAVE);
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(this);
        saveBtn.setSize(100, 10);
        spring.putConstraint(SpringLayout.NORTH, saveBtn, 10, SpringLayout.SOUTH, connectBtn);
        spring.putConstraint(SpringLayout.WEST, saveBtn, 5, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, saveBtn, -10, SpringLayout.EAST, panel);
        panel.add(saveBtn);
        
        JButton generateBtn = new JButton("Generate");
        generateBtn.setActionCommand(ACTION_CMD_GENERATE_CHARTS);
        generateBtn.addActionListener(this);
        generateBtn.setSize(40, 10);
        spring.putConstraint(SpringLayout.NORTH, generateBtn, 10, SpringLayout.SOUTH, saveBtn);
        spring.putConstraint(SpringLayout.WEST, generateBtn, 5, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, generateBtn, -10, SpringLayout.EAST, panel);
        panel.add(generateBtn);
        
        JButton prefsbtn = new JButton("Preferences");
        prefsbtn.setActionCommand(ACTION_CMD_PREFERENCES);
        prefsbtn.addActionListener(this);
        prefsbtn.setSize(40, 10);
        spring.putConstraint(SpringLayout.NORTH, prefsbtn, 10, SpringLayout.SOUTH, generateBtn);
        spring.putConstraint(SpringLayout.WEST, prefsbtn, 5, SpringLayout.WEST, panel);
        spring.putConstraint(SpringLayout.EAST, prefsbtn, -10, SpringLayout.EAST, panel);
        panel.add(prefsbtn);
        
        
        JLabel signalLabel = new JLabel("Signal");
		spring.putConstraint(SpringLayout.NORTH, signalLabel, 10, SpringLayout.SOUTH, prefsbtn);
		spring.putConstraint(SpringLayout.WEST, signalLabel, 5, SpringLayout.WEST, panel);
		panel.add(signalLabel);
		
		signalStrengthText = new JTextField("0%");
        signalStrengthText.setBackground(Color.WHITE );
        signalStrengthText. setHorizontalAlignment(JTextField.CENTER);
        signalStrengthText.setSize(1, getHeight());
        signalStrengthText.setColumns(3);
        spring.putConstraint(SpringLayout.NORTH, signalStrengthText, 0, SpringLayout.NORTH, signalLabel);
		spring.putConstraint(SpringLayout.WEST, signalStrengthText, 5, SpringLayout.EAST, signalLabel);
		panel.add(signalStrengthText);
		
		JRadioButton barBtn = new JRadioButton("BarChart");
		barBtn.setSelected(true);
		barBtn.setActionCommand(ACTION_CMD_BARCHART);
		barBtn.addActionListener(this);
		JRadioButton lineBtn = new JRadioButton("LineChart");
		lineBtn.setActionCommand(ACTION_CMD_LINECHART);
		lineBtn.addActionListener(this);
		
		final ButtonGroup group  = new ButtonGroup();
		group.add(barBtn);
		group.add(lineBtn);
		spring.putConstraint(SpringLayout.WEST, barBtn, 0, SpringLayout.WEST, signalLabel);
		spring.putConstraint(SpringLayout.NORTH, barBtn, 5, SpringLayout.SOUTH, signalLabel);
		spring.putConstraint(SpringLayout.WEST, lineBtn, 0, SpringLayout.WEST, barBtn);
		spring.putConstraint(SpringLayout.NORTH, lineBtn, 5, SpringLayout.SOUTH, barBtn);
		
		
		panel.add(barBtn);
		panel.add(lineBtn);
		
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
                
		return panel;
	}

	/**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createESenseBarChart(final CategoryDataset dataset, String title, String domain) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
        		title,         // chart title
            domain,               // domain axis label
            "",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRange(false);
        rangeAxis.setRange(0.0, 100.0); 

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setItemMargin(0d);
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.GRAY, 
            0.0f, 0.0f, Color.GRAY
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.BLACK, 
            0.0f, 0.0f, Color.BLACK
        );
//        final GradientPaint gp2 = new GradientPaint(
//            0.0f, 0.0f, Color.red, 
//            0.0f, 0.0f, Color.lightGray
//        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
//        renderer.setSeriesPaint(2, gp2);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
       
        return chart;
        
    }
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createEEGPowerBarChart(final CategoryDataset dataset, String title, String domain) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
        		title,         // chart title
            domain,               // domain axis label
            "",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setItemMargin(0d);
        
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f,new Color(219,211,42), 
            0.0f, 0.0f,new Color(219,211,42)
        );
        final GradientPaint gp1 = new GradientPaint(
        		 0.0f, 0.0f,new Color(245,80,71), 
        		 0.0f, 0.0f,new Color(245,80,71)
        );
        final GradientPaint gp2 = new GradientPaint(
        		 0.0f, 0.0f,new Color(237,0,119), 
                0.0f, 0.0f, new Color(237,0,119)
            );
        final GradientPaint gp3 = new GradientPaint(
                0.0f, 0.0f,new Color(212,0,149), 
                0.0f, 0.0f,new Color(212,0,149)
            );
        final GradientPaint gp4 = new GradientPaint(
                0.0f, 0.0f,new Color(158,18,188), 
                0.0f, 0.0f, new Color(158,18,188)
            );
        final GradientPaint gp5 = new GradientPaint(
                0.0f, 0.0f,new Color(116,23,190), 
                0.0f, 0.0f, new Color(116,23,190)
            );
        final GradientPaint gp6 = new GradientPaint(
                0.0f, 0.0f,new Color(39,35,159), 
                0.0f, 0.0f, new Color(39,35,159)
            );
        final GradientPaint gp7 = new GradientPaint(
                0.0f, 0.0f,new Color(23,26,153), 
                0.0f, 0.0f, new Color(23,26,153)
            );
        
        
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        renderer.setSeriesPaint(4, gp4);
        renderer.setSeriesPaint(5, gp5);
        renderer.setSeriesPaint(6, gp6);
        renderer.setSeriesPaint(7, gp7);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
    
    private JFreeChart createEEGPowerLineChart(XYDataset dataset) {
        JFreeChart result = ChartFactory.createTimeSeriesChart(
            "eegPower", 
            "", 
            "",
            eegDataset, 
            true, 
            true, 
            false
        );
        XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 200.0); 
        
  		ValueAxis yAxis =plot.getRangeAxis();
  		yAxis.setAutoRange(true);
        		
        
      
  		XYItemRenderer renderer = plot.getRenderer();
  		renderer.setSeriesStroke(INDEX_DELTASERIES     , new BasicStroke(LINE_THINKNESS));
  		renderer.setSeriesStroke(INDEX_THETASERIES     , new BasicStroke(LINE_THINKNESS));
  		renderer.setSeriesStroke(INDEX_LOWALPHASERIES  , new BasicStroke(LINE_THINKNESS));
  		renderer.setSeriesStroke(INDEX_HIGHALPHASERIES , new BasicStroke(LINE_THINKNESS));
  		renderer.setSeriesStroke(INDEX_LOWBETASERIES   , new BasicStroke(LINE_THINKNESS));
  		renderer.setSeriesStroke(INDEX_HIGHBETASERIES  , new BasicStroke(LINE_THINKNESS));
  		renderer.setSeriesStroke(INDEX_LOWGAMMASERIES  , new BasicStroke(LINE_THINKNESS));
  		renderer.setSeriesStroke(INDEX_HIGHGAMASERIES  , new BasicStroke(LINE_THINKNESS));
  		
        
        return result;
    }
    private JFreeChart createESenseLineChart(XYDataset dataset) {
        JFreeChart result = ChartFactory.createTimeSeriesChart(
            "eSense", 
            "", 
            "",
            eSenseDataset, 
            true, 
            true, 
            false
        );
        XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 200.0); 
        
  		ValueAxis yAxis =plot.getRangeAxis();
  		yAxis.setAutoRange(true);
        		
        
      
  		XYItemRenderer renderer = plot.getRenderer();
  		renderer.setSeriesStroke(this.INDEX_ATTENTIONSERIES , new BasicStroke(LINE_THINKNESS)); 
  		renderer.setSeriesStroke(this.INDEX_MEDITATIONSERIES, new BasicStroke(LINE_THINKNESS));
  		
  		
        
        return result;
    }
	
    
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Logger.debug("Someone pressed a button");
		 if (arg0.getActionCommand().equals(ACTION_CMD_CONNECT)) {
			 
			 Logger.debug(ACTION_CMD_CONNECT);
	        	        	
	        	JButton j = (JButton)(arg0.getSource());
	        	j.setActionCommand(ACTION_CMD_DISCONNECT);
	        	
		
				this.thinkGearHandlerSwingWorker = new ThinkGearHandlerSwingWorker();
				this.thinkGearHandlerSwingWorker.setAltDataSet(altDataSet);
				this.thinkGearHandlerSwingWorker.setWavesDataSet(wavesDataSet);
				
				this.thinkGearHandlerSwingWorker.setSignalStrengthJTextField(this.signalStrengthText);
				
				
				j.setText("Disconnect");
				saveBtn.setEnabled(true);
        		
        		this.thinkGearHandlerSwingWorker.execute();
          	
        }else if (arg0.getActionCommand().equals(ACTION_CMD_DISCONNECT)){
        	
        	Logger.debug(ACTION_CMD_SAVE);
        	JButton j = (JButton)(arg0.getSource());
        	
        	j.setActionCommand(ACTION_CMD_CONNECT);
        	if (this.thinkGearHandlerSwingWorker!= null){
				try {
					this.thinkGearHandlerSwingWorker.getConnector().close();
				} catch (IOException e) {
					Logger.log(e.toString());
					e.printStackTrace();
				}
				j.setText("Connect");
				
				((SwingWorker)this.thinkGearHandlerSwingWorker).cancel(true);
				//((SwingWorker)this.timer).cancel(true);
				this.thinkGearHandlerSwingWorker = null;
				saveBtn.setEnabled(false);
        	}
        }else if (arg0.getActionCommand().equals(ACTION_CMD_SAVE)){
            	Logger.debug(ACTION_CMD_SAVE);
            	JButton j = (JButton)(arg0.getSource());
            	
      		
      			// JH: Added date to csvFile
  				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd HH.mm.ssyyy");
  				String filename = "skyskraper."+fmt.format(new Date()).toString() +".json" ; 
  				SaveJDialog sjd = new SaveJDialog(this, filename);
  				sjd.setVisible(true);
  				RefineryUtilities.centerFrameOnScreen(sjd);
  				
  				// if property is set to true we have a new name
  				if (PreferenceManager.getPreferences().get(PreferenceManager.SAVE, "false").equals("true")){
  					this.thinkGearHandlerSwingWorker.setFilename(PreferenceManager.getPreferences().get("skyskraper.save.filename", filename));
	  					// tell the handler to start saving
	  				this.thinkGearHandlerSwingWorker.setSave(true);
	  				
	  				j.setText("Stop Saving");
	  				j.setActionCommand(ACTION_CMD_STOP_SAVE);
   				}
  				
      		
        	
        }else if (arg0.getActionCommand().equals(ACTION_CMD_STOP_SAVE)){
        	JButton j = (JButton)(arg0.getSource());
        	this.thinkGearHandlerSwingWorker.setSave(false);
        	j.setActionCommand(ACTION_CMD_SAVE);
        	j.setText("Save");
        	
            
        }else if (arg0.getActionCommand().equals(ACTION_CMD_GENERATE_CHARTS)){
        	pw.setVisible(true);
        	pw.activateChartGenerationTab();
        	pw.getContentPane().requestFocus();
        	RefineryUtilities.centerFrameOnScreen(pw);
        	
        }else if (arg0.getActionCommand().equals(ACTION_CMD_STATS)){
        	pw.setVisible(true);
        	pw.activatePrefsTab();
        	pw.getContentPane().requestFocus();
        	RefineryUtilities.centerFrameOnScreen(pw);
        	
        }else if (arg0.getActionCommand().equals(ACTION_CMD_PREFERENCES)){
        	pw.setVisible(true);
			pw.getContentPane().requestFocus();
			RefineryUtilities.centerFrameOnScreen(pw);

        }else if (arg0.getActionCommand().equals(ACTION_CMD_BARCHART)){
        	JRadioButton j = (JRadioButton) arg0.getSource();
        	if (j.isSelected()){
        		switchChart(CHART_TYPE_BAR);
        	}

        }else if (arg0.getActionCommand().equals(ACTION_CMD_LINECHART)){
        	JRadioButton j = (JRadioButton) arg0.getSource();
        	if (j.isSelected()){
        		switchChart(CHART_TYPE_LINE);
        	}
        }
	
	}
	
	

	private void switchChart(boolean type) {
		if (type != charType){
			if (type == CHART_TYPE_LINE ){
				Logger.debug ("Switching to LineChart");
				eSenseChartPanel.setChart(createESenseLineChart(eSenseDataset));
				eegPowerChartPanel.setChart(createEEGPowerLineChart(eegDataset));

			}if (type == CHART_TYPE_BAR ){
				Logger.debug ("Switching to BarChart");
				eSenseChartPanel.setChart(createESenseBarChart(altDataSet, "","eSense"));
				eegPowerChartPanel.setChart(createEEGPowerBarChart(wavesDataSet, "","eegPower"));

			}
			this.charType = !this.charType;
		}
		
		
	}

	public static void main(String[] args) {
		SkyScraper me = new SkyScraper("SkyScraper");
		me.pack();
        RefineryUtilities.centerFrameOnScreen(me);
        me.setVisible(true);
	}

	

  
	/**
	 * <p>Title:		ThinkGearHandlerSwingWorker</p><br>
	 * <p>Description:	Handles JSON data coming back from ThinkGearSocketClient</p><br>
	 * 
	 * @author		    Jornack
	 *
	 * $Author: Jornack $
	 */
private class ThinkGearHandlerSwingWorker extends SwingWorker<Void, Void> {
    	private ThinkGearConnector connector = null;
    	private DefaultCategoryDataset altDataSet = null;
    	private DefaultCategoryDataset wavesDataSet = null;
    	private final static int CONNECT_INTERVAL = 3000;
    	private final static int MAX_CONNECT_RETRIES = 5;
    	private boolean save = false;
    	private JTextField signalStrengthJTextField = null;
    	private FileWriter writer = null;
    	private String  filename = null;
    	public ThinkGearHandlerSwingWorker(){
			setConnector(new ThinkGearConnector());
		}
    	@Override
        protected void done() {
    		
    		try {
    			if (writer != null){
    				writer.close();
    			}
				
			} catch (IOException e) {
				Logger.log(e.toString());
				e.printStackTrace();
			}
    		try {
				getConnector().close();
			} catch (IOException e) {
				Logger.log(e.toString());
				e.printStackTrace();
			}
    		writer = null;
    		filename = null;
    		getSignalStrengthJTextField().setText("0%");
    		getSignalStrengthJTextField().setForeground(Color.red);
           
    		//((SwingWorker)this.timer).cancel(true);
    		//this.timer = null;
    		
    		Logger.log("Stopped");
    		Logger.debug("Connected: " + getConnector().isConnected());
    		
        }
    	
    	@Override
	    protected Void doInBackground() {
    		
    		// keep on trying to connect
    		int counter = 0;
    		
    		while (!getConnector().isConnected() && counter < MAX_CONNECT_RETRIES){
    			Logger.log("Connecting..." );
	    		
	    		try {
		    			getConnector().connect();
		    			
    					
		    		
		    	} catch (IOException e) {
		    		counter++;
					Logger.log("Connection failed. Count : " + counter);
		    		// TODO Auto-generated catch block
					Logger.log(e.toString());
					e.printStackTrace();
					Thread.currentThread();
					try {
						Thread.sleep(CONNECT_INTERVAL);
					} catch (InterruptedException e1) {
							Logger.log(e1.toString());
					}
				}
	    	}
    		Logger.log("Connected:" + getConnector().isConnected());
    		if (!getConnector().isConnected()){
    			Logger.log("Failed to connect.");
    			
    		}else{
    			Logger.debug("Start processing data");
	    			while(getConnector().isDataAvailable()){
    					try {
	    					String clientData = getConnector().getData();
	    					Millisecond now = new Millisecond();
	    	                Logger.debug( "clientdata: " +clientData);
	    	                
	    					JSONObject json = new JSONObject(clientData);
	    					json.put("ms", new Date().getTime());
	    					Logger.debug("JSON: " + json);
	    					
	    					if (!json.isNull("poorSignalLevel")){
	    						int strength = 200 - json.getInt("poorSignalLevel");
	    						strength = (int)(((double)strength/(double)200)*100);
	    						getSignalStrengthJTextField().setText(Integer.toString(strength) + "%");
	    						if (strength <25){
	    							getSignalStrengthJTextField().setForeground(Color.red);
	    						}else if  (strength <50){
	    							getSignalStrengthJTextField().setForeground(Color.orange);
	    						}else if  (strength <75){
	    							getSignalStrengthJTextField().setForeground(Color.yellow);
	    						}else {
	    							getSignalStrengthJTextField().setForeground(Color.green);
	    						}
	    					}
	    					/*
	    					 * JH: check for existence of eSense. 
	    					 * I noticed it's possible to get eegPower without eSense when poorSignallevel >0
	    					 */
	    					if (!json.isNull("eSense")){
	
	    						JSONObject esense = json.getJSONObject("eSense");
	    						
	    						getAltDataSet().setValue(esense.getInt("attention"), "Attention", "");
	    						getAltDataSet().setValue(esense.getInt("meditation"), "Meditation", "");
	    						getAttentionSeries().add(now,(double)esense.getInt("attention"));
	    						getMeditationSeries().add(now,(double)esense.getInt("meditation"));
	    					} 
	    					// JH: check just in case it's not there due to poorSignallevel
	    					if (!json.isNull("eegPower")){
	    						
	    						JSONObject eegPower = json.getJSONObject("eegPower");
	    						getWavesDataSet().setValue(eegPower.getInt("delta"), "delta", "");
	    						getWavesDataSet().setValue(eegPower.getInt("theta"), "theta", "");
	    						getWavesDataSet().setValue(eegPower.getInt("lowAlpha"), "lowAlpha", "");
	    						getWavesDataSet().setValue(eegPower.getInt("highAlpha"), "highAlpha", "");
	    						getWavesDataSet().setValue(eegPower.getInt("lowBeta"), "lowBeta", "");
	    						getWavesDataSet().setValue(eegPower.getInt("highBeta"), "highBeta", "");
	    						getWavesDataSet().setValue(eegPower.getInt("lowGamma"), "lowGamma", "");
	    						getWavesDataSet().setValue(eegPower.getInt("highGamma"), "highGamma", "");
	    						
	    						getDeltaSeries().add(now,(double)eegPower.getInt("delta"));
	    						getThetaSeries().add(now,(double)eegPower.getInt("theta"));
	    						getLowAlphaSeries().add(now,(double)eegPower.getInt("lowAlpha"));
	    						getHighAlphaSeries().add(now,(double)eegPower.getInt("highAlpha"));
	    						getLowBetaSeries().add(now,(double)eegPower.getInt("lowBeta"));
	    						getHighBetaSeries().add(now,(double)eegPower.getInt("highBeta"));
	    						getLowGammaSeries().add(now,(double)eegPower.getInt("lowGamma"));
	    						getHighGamaSeries().add(now,(double)eegPower.getInt("highGamma"));
//	    												
	    					} 
	
	    					if (save){
	    						if (writer == null){
		    						Logger.debug("Filename : " + filename);
		        					
		    						try {
		    							writer = new FileWriter(filename);
		    							
		    						
		    						writer.append(json.toString());
		        					
			    					writer.append('\n'); 
									writer.flush();
		    						} catch (IOException e1) {
		    							Logger.log(e1.toString());
		    							e1.printStackTrace();
		    						}
		    						Logger.log("Started saving" );
	    						}else{
	    							try {
										writer.append(json.toString());
										writer.append('\n'); 
		    							writer.flush();
		    							
									} catch (IOException e) {
										Logger.log(e.toString());
										e.printStackTrace();
									}
		        					
	    	    					
	    						}
		    					
	    					}else if (writer != null){
	    						try {
	    							writer.close();
	    							writer = null;
	    							filename = null;
	    						} catch (IOException e) {
	    							Logger.log(e.toString());
	    							e.printStackTrace();
	    						}
	    						Logger.log("Stopped saving");
	    					}
    					}catch (JSONException e) {
		    				// TODO Auto-generated catch block
    						Logger.log(e.toString());
		    				e.printStackTrace();
		    			
		    			}
    				}// end while
    				Logger.debug("No more data available");
    			
    			
    		}// end if (!getConnector().isConnected()){
    		return null;
    	}
		public JTextField getSignalStrengthJTextField() {
			return signalStrengthJTextField;
		}
		public void setSignalStrengthJTextField(JTextField signalStrengthJTextField) {
			this.signalStrengthJTextField = signalStrengthJTextField;
		}
		
		
		public DefaultCategoryDataset getWavesDataSet() {
			return wavesDataSet;
		}
		public void setWavesDataSet(DefaultCategoryDataset wavesDataSet) {
			this.wavesDataSet = wavesDataSet;
		}

		public DefaultCategoryDataset getAltDataSet() {
			return altDataSet;
		}
		public void setAltDataSet(DefaultCategoryDataset altDataSet) {
			this.altDataSet = altDataSet;
		}

		public boolean isSave() {
			return save;
		}
		public void setSave(boolean save) {
			this.save = save;
		}
		
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
		public ThinkGearConnector getConnector() {
			return connector;
		}
		public void setConnector(ThinkGearConnector connector) {
			this.connector = connector;
		}
    }// ThinkgearHandlerSwingWorker




	@Override
	public void mouseClicked(MouseEvent arg0) {
		JOptionPane.showMessageDialog(null, "Thank you for considering a donation!\n\n");
		if(Desktop.isDesktopSupported()){
			String htmlFilePath = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=jornack%40gmail%2ecom&lc=US&item_name=SkyScraper&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest"; // path to your new file
			URL url;
			try {
				url = new URL(htmlFilePath);
				// open the default web browser for the HTML page
				try {
					Desktop.getDesktop().browse(url.toURI());
				} catch (IOException e) {
					
					try {
						Desktop.getDesktop().open(new File(htmlFilePath));
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, UNABLED_TO_BROWSE);
						Logger.log(UNABLED_TO_BROWSE);
					}
				} catch (URISyntaxException e) {
					Logger.log(e.toString());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e2) {
				Logger.log(e2.toString());
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public static TimeSeries getPoorSignalLevelSeries() {
		return poorSignalLevelSeries;
	}

	public static void setPoorSignalLevelSeries(TimeSeries poorSignalLevelSeries) {
		SkyScraper.poorSignalLevelSeries = poorSignalLevelSeries;
	}

	public static TimeSeries getAttentionSeries() {
		return attentionSeries;
	}

	public static void setAttentionSeries(TimeSeries attentionSeries) {
		SkyScraper.attentionSeries = attentionSeries;
	}

	public static TimeSeries getMeditationSeries() {
		return meditationSeries;
	}

	public static void setMeditationSeries(TimeSeries meditationSeries) {
		SkyScraper.meditationSeries = meditationSeries;
	}

	public static TimeSeries getDeltaSeries() {
		return deltaSeries;
	}

	public static void setDeltaSeries(TimeSeries deltaSeries) {
		SkyScraper.deltaSeries = deltaSeries;
	}

	public static TimeSeries getThetaSeries() {
		return thetaSeries;
	}

	public static void setThetaSeries(TimeSeries thetaSeries) {
		SkyScraper.thetaSeries = thetaSeries;
	}

	public static TimeSeries getLowAlphaSeries() {
		return lowAlphaSeries;
	}

	public static void setLowAlphaSeries(TimeSeries lowAlphaSeries) {
		SkyScraper.lowAlphaSeries = lowAlphaSeries;
	}

	public static TimeSeries getHighAlphaSeries() {
		return highAlphaSeries;
	}

	public static void setHighAlphaSeries(TimeSeries highAlphaSeries) {
		SkyScraper.highAlphaSeries = highAlphaSeries;
	}

	public static TimeSeries getLowBetaSeries() {
		return lowBetaSeries;
	}

	public static void setLowBetaSeries(TimeSeries lowBetaSeries) {
		SkyScraper.lowBetaSeries = lowBetaSeries;
	}

	public static TimeSeries getHighBetaSeries() {
		return highBetaSeries;
	}

	public static void setHighBetaSeries(TimeSeries highBetaSeries) {
		SkyScraper.highBetaSeries = highBetaSeries;
	}

	public static TimeSeries getLowGammaSeries() {
		return lowGammaSeries;
	}

	public static void setLowGammaSeries(TimeSeries lowGammaSeries) {
		SkyScraper.lowGammaSeries = lowGammaSeries;
	}

	public static TimeSeries getHighGamaSeries() {
		return highGamaSeries;
	}

	public static void setHighGamaSeries(TimeSeries highGamaSeries) {
		SkyScraper.highGamaSeries = highGamaSeries;
	}




	
}
