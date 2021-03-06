import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MultipleLinesChart extends JFrame {

    public MultipleLinesChart(String applicationTitle, String chartTitle, List<long[]> data) {
        super(applicationTitle);
        /*
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Number of threads", "Time to complete (ms)",
                createDataset(data),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
        */
        JPanel chartPanel = createChartPanel(chartTitle,data);
        add(chartPanel, BorderLayout.CENTER);

        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    private JPanel createChartPanel(String chartTitle, List<long[]> data) { // this method will create the chart panel containin the graph
        String chTitle = chartTitle;
        String xAxisLabel = "Number of threads";
        String yAxisLabel = "Time to compute (ns)";

        XYDataset dataset = createDataset(data);

        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                xAxisLabel, yAxisLabel, dataset);

        customizeChart(chart);

        // saves the chart as an image files
        File imageFile = new File("XYLineChart.png");
        int width = 640;
        int height = 480;

        try {
            ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        return new ChartPanel(chart);
    }

    private void customizeChart(JFreeChart chart) {   // here we make some customization
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // sets paint color for each series
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.GREEN);
        //renderer.setSeriesPaint(2, Color.YELLOW);

        // sets thickness for series (using strokes)
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        // renderer.setSeriesStroke(2, new BasicStroke(2.0f));

        // sets paint color for plot outlines
        plot.setOutlinePaint(Color.BLUE);
        plot.setOutlineStroke(new BasicStroke(2.0f));

        // sets renderer for lines
        plot.setRenderer(renderer);

        // sets plot background
        plot.setBackgroundPaint(Color.DARK_GRAY);

        // sets paint color for the grid lines
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

    }

    private XYDataset createDataset(List<long[]> data) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries LFS = new XYSeries("LFS");
        XYSeries EBS = new XYSeries("EBS");

        for (long[] datum : data) {

            // 0 for elimination backoff stack
            // 1 for Lock free stack
            if (datum[1] == 0 ){
                EBS.add(datum[2],datum[0]);
            }else{
                LFS.add(datum[2],datum[0]);
            }
            // time to complete operations , "Time to complete" , Number of threads used
            //dataset.addValue(datum[0], rowKey, Long.toString(datum[1]));

        }
        dataset.addSeries(LFS);
        dataset.addSeries(EBS);
        return dataset;
    }
}