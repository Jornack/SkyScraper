/**
 * @author Jornack copyright 2012 Jornack Hupkens
 *
 */
package com.jornack.skyscraper.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONException;
import org.json.JSONObject;

public class ExcelGenerator extends JSONFileReader
{

    private static final int COLUMN_MS = 0;
    private static final int COLUMN_PSL = 1;
    private static final int COLUMN_ATT = 2;
    private static final int COLUMN_MED = 3;
    private static final int COLUMN_DELTA = 4;
    private static final int COLUMN_THETA = 5;
    private static final int COLUMN_LOALPHA = 6;
    private static final int COLUMN_HIALPHA = 7;
    private static final int COLUMN_LOBETA = 8;
    private static final int COLUMN_HIBETA = 9;
    private static final int COLUMN_LOGAMMA = 10;
    private static final int COLUMN_HIGAMMA = 11;

    private String sheetName = "Sheet";
    private File target = new File("workbook.xls");

    public ExcelGenerator(File source)
    {
        super(source);

    }

    public ExcelGenerator(File source, File target, String sheetName)
    {
        super(source);
        if (target != null)
        {
            this.target = target;
        }
        if (sheetName != null)
        {
            this.sheetName = sheetName;
        }

    }

    private void writeHeader(WritableSheet sheet) throws RowsExceededException,
            WriteException
    {
        Label ms = new Label(COLUMN_MS, 0, "ms");
        Label pss = new Label(COLUMN_PSL, 0, "PoorSignalLevel");
        Label att = new Label(COLUMN_ATT, 0, "Attention");
        Label med = new Label(COLUMN_MED, 0, "Meditation");
        Label delta = new Label(COLUMN_DELTA, 0, "Delta");
        Label theta = new Label(COLUMN_THETA, 0, "Theta");
        Label loAlpha = new Label(COLUMN_LOALPHA, 0, "Low Alpha");
        Label hiAlpha = new Label(COLUMN_HIALPHA, 0, "High Alpha");
        Label loBeta = new Label(COLUMN_LOBETA, 0, "Low Beta");
        Label hiBeta = new Label(COLUMN_HIBETA, 0, "High Beta");
        Label loGamma = new Label(COLUMN_LOGAMMA, 0, "Low Gamma");
        Label hiGamma = new Label(COLUMN_HIGAMMA, 0, "High Gamma");

        sheet.addCell(ms);
        sheet.addCell(pss);
        sheet.addCell(att);
        sheet.addCell(med);
        sheet.addCell(delta);
        sheet.addCell(theta);
        sheet.addCell(loAlpha);
        sheet.addCell(hiAlpha);
        sheet.addCell(loBeta);
        sheet.addCell(hiBeta);
        sheet.addCell(loGamma);
        sheet.addCell(hiGamma);
    }

    public WritableWorkbook getWorkbook() throws IOException
    {
        WritableWorkbook workbook = Workbook.createWorkbook(this.target);
        WritableSheet sheet = workbook.createSheet(this.sheetName, 0);

        //JSONFileReader reader = new JSONFileReader(source);
        String data = null;

        try
        {
            writeHeader(sheet);
            while (isDataAvailable())
            {
                data = getData();
                Logger.debug(data);

                int row = sheet.getRows();
                JSONObject json = new JSONObject(data);
                Millisecond ms = null;
                if (!json.isNull("ms"))
                {
                    Number numMs = new Number(COLUMN_MS, row, json.getInt("ms"));
                    sheet.addCell(numMs);
                } // end if
                if (!json.isNull("poorSignalLevel"))
                {
                    Number numPsl = new Number(COLUMN_PSL, row, json.getInt(
                            "poorSignalLevel"));
                    sheet.addCell(numPsl);

                } // end if

                if (!json.isNull("eSense"))
                {

                    JSONObject esense = json.getJSONObject("eSense");
                    Number numAtt = new Number(COLUMN_ATT, row, esense.getInt(
                            "attention"));
                    Number numMed = new Number(COLUMN_MED, row, esense.getInt(
                            "meditation"));

                    sheet.addCell(numAtt);
                    sheet.addCell(numMed);

                } // end if

                if (!json.isNull("eegPower"))
                {

                    JSONObject eegPower = json.getJSONObject("eegPower");

                    Number numDelta = new Number(COLUMN_DELTA, row, eegPower.
                            getInt("delta"));
                    Number numTheta = new Number(COLUMN_THETA, row, eegPower.
                            getInt("theta"));
                    Number numLowAlpha = new Number(COLUMN_LOALPHA, row,
                            eegPower.getInt("lowAlpha"));
                    Number numHiAlpha = new Number(COLUMN_HIALPHA, row,
                            eegPower.getInt("highAlpha"));
                    Number numLowBeta = new Number(COLUMN_LOBETA, row, eegPower.
                            getInt("lowBeta"));
                    Number numHiBeta = new Number(COLUMN_HIBETA, row, eegPower.
                            getInt("highBeta"));
                    Number numLoGamma = new Number(COLUMN_LOGAMMA, row,
                            eegPower.getInt("lowGamma"));
                    Number numHiGamma = new Number(COLUMN_HIGAMMA, row,
                            eegPower.getInt("highGamma"));

                    sheet.addCell(numDelta);
                    sheet.addCell(numTheta);
                    sheet.addCell(numLowAlpha);
                    sheet.addCell(numHiAlpha);
                    sheet.addCell(numLowBeta);
                    sheet.addCell(numHiBeta);
                    sheet.addCell(numLoGamma);
                    sheet.addCell(numHiGamma);

                } // end if

            }//end while
        } catch (JSONException e)
        {
            Logger.log(e);
            e.printStackTrace();
        } catch (RowsExceededException e)
        {
            Logger.log(e);
            e.printStackTrace();
        } catch (WriteException e)
        {
            Logger.log(e);
            e.printStackTrace();
        } finally
        {
            workbook.write();
            try
            {
                workbook.close();
            } catch (WriteException e)
            {
                Logger.log(e);
                e.printStackTrace();
            }
        } // end finally
        return workbook;
    }

    public WritableWorkbook getWorkbook(TimeSeriesCollection eSense,
            TimeSeriesCollection eegPower) throws IOException
    {
        WritableWorkbook workbook = Workbook.createWorkbook(this.target);
        WritableSheet sheet = workbook.createSheet(this.sheetName, 0);

        try
        {
            writeHeader(sheet);

            if (eegPower.getSeriesCount() > 0)
            {

                for (int i = 0; i < eegPower.getSeries(0).getTimePeriods().
                        size(); i++)
                {
                    Millisecond period = (Millisecond) eegPower.getSeries(0).
                            getTimePeriod(i);
                    Number numMs = new Number(COLUMN_MS, i + 1, period.
                            getFirstMillisecond());
                    System.out.println("number0=:" + numMs.getValue());
                    Number numAtt = null;
                    Number numMed = null;
                    if (eSense.getSeries(0).getValue(period) != null)
                    {
                        numAtt = new Number(COLUMN_ATT, i + 1, (Double) eSense.
                                getSeries(0).getValue(period));
                        numMed = new Number(COLUMN_MED, i + 1, (Double) eSense.
                                getSeries(1).getValue(period));

                    } else
                    {
                        numAtt = new Number(COLUMN_ATT, i + 1, 0);
                        numMed = new Number(COLUMN_MED, i + 1, 0);

                    }
                    Number numDelta = new Number(COLUMN_DELTA, i + 1,
                            (Double) eegPower.getSeries(0).getValue(period));
                    Number numTheta = new Number(COLUMN_THETA, i + 1,
                            (Double) eegPower.getSeries(1).getValue(period));
                    Number numLowAlpha = new Number(COLUMN_LOALPHA, i + 1,
                            (Double) eegPower.getSeries(2).getValue(period));
                    Number numHiAlpha = new Number(COLUMN_HIALPHA, i + 1,
                            (Double) eegPower.getSeries(3).getValue(period));
                    Number numLowBeta = new Number(COLUMN_LOBETA, i + 1,
                            (Double) eegPower.getSeries(4).getValue(period));
                    Number numHiBeta = new Number(COLUMN_HIBETA, i + 1,
                            (Double) eegPower.getSeries(5).getValue(period));
                    Number numLoGamma = new Number(COLUMN_LOGAMMA, i + 1,
                            (Double) eegPower.getSeries(6).getValue(period));
                    Number numHiGamma = new Number(COLUMN_HIGAMMA, i + 1,
                            (Double) eegPower.getSeries(7).getValue(period));

                    sheet.addCell(numMs);
                    sheet.addCell(numAtt);
                    sheet.addCell(numMed);
                    sheet.addCell(numDelta);
                    sheet.addCell(numTheta);
                    sheet.addCell(numLowAlpha);
                    sheet.addCell(numHiAlpha);
                    sheet.addCell(numLowBeta);
                    sheet.addCell(numHiBeta);
                    sheet.addCell(numLoGamma);
                    sheet.addCell(numHiGamma);
                } // end for

            } // end if 

        } catch (RowsExceededException e)
        {
            Logger.log(e);
            e.printStackTrace();
        } catch (WriteException e)
        {
            Logger.log(e);
            e.printStackTrace();
        } finally
        {
            workbook.write();
            try
            {
                workbook.close();
            } catch (WriteException e)
            {
                Logger.log(e);
                e.printStackTrace();
            }
        } // end finally

        return workbook;
    }

    /**
     * For debugging only
     *
     * @param args
     */
    public static void main(String[] args)
    {

        TimeSeries poorSignalLevelSeries = new TimeSeries("Poor Signal Level");
        TimeSeries attentionSeries = new TimeSeries("Attention");
        TimeSeries meditationSeries = new TimeSeries("Meditation");
        TimeSeries deltaSeries = new TimeSeries("Delta");
        TimeSeries thetaSeries = new TimeSeries("Theta ");
        TimeSeries lowAlphaSeries = new TimeSeries("Low Alpha");
        TimeSeries highAlphaSeries = new TimeSeries("High Alpha");
        TimeSeries lowBetaSeries = new TimeSeries("Low Beta");
        TimeSeries highBetaSeries = new TimeSeries("High Beta");
        TimeSeries lowGammaSeries = new TimeSeries("Low Gamma");
        TimeSeries highGamaSeries = new TimeSeries("High Gama");
        Millisecond ms = new Millisecond(new Date());
        System.out.println(" ms=:" + ms.getFirstMillisecond());
        System.out.println(" ms=:" + ms.getLastMillisecond());

//		poorSignalLevelSeries.add(ms,1); 
//		attentionSeries      .add(ms,2); 
        meditationSeries.add(ms, 3);
        deltaSeries.add(ms, 4);
        thetaSeries.add(ms, 5);
        lowAlphaSeries.add(ms, 6);
        highAlphaSeries.add(ms, 7);
        lowBetaSeries.add(ms, 8);
        highBetaSeries.add(ms, 9);
        lowGammaSeries.add(ms, 10);
        highGamaSeries.add(ms, 11);
        ms = new Millisecond(new Date());

        poorSignalLevelSeries.add(ms, 10);
        attentionSeries.add(ms, 20);
        meditationSeries.add(ms, 30);
        deltaSeries.add(ms, 40);
        thetaSeries.add(ms, 50);
        lowAlphaSeries.add(ms, 60);
        highAlphaSeries.add(ms, 70);
        lowBetaSeries.add(ms, 80);
        highBetaSeries.add(ms, 90);
        lowGammaSeries.add(ms, 100);
        highGamaSeries.add(ms, 110);

        TimeSeriesCollection esense = new TimeSeriesCollection();
        TimeSeriesCollection eegpower = new TimeSeriesCollection();
        esense.addSeries(poorSignalLevelSeries);
        esense.addSeries(attentionSeries);
        eegpower.addSeries(meditationSeries);
        eegpower.addSeries(deltaSeries);
        eegpower.addSeries(thetaSeries);
        eegpower.addSeries(lowAlphaSeries);
        eegpower.addSeries(highAlphaSeries);
        eegpower.addSeries(lowBetaSeries);
        eegpower.addSeries(highBetaSeries);
        eegpower.addSeries(lowGammaSeries);;
        eegpower.addSeries(highGamaSeries);
        ExcelGenerator me = new ExcelGenerator(null, null, "Foo Session");
        File source = new File(
                "d:\\Program Files (x86)\\Eclipse IDE for Java Developers\\eclipse\\workspace\\SkyScraper.JH\\skyskraper.20120211 09.50.5312.json");
        ExcelGenerator me2 = new ExcelGenerator(source, new File("me2.xls"),
                "Foo2 Session");

        try
        {
            me.getWorkbook(esense, eegpower);
            me2.getWorkbook();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
