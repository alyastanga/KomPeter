/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/

package com.github.ragudos.kompeter.app.desktop.forms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;
import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService;
import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService.PredictionPoint;
import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService.RevenuePredictionReport;

import net.miginfocom.swing.MigLayout;

@SystemForm(name = "Monitoring Sales", description = "Shows all possible information about the sales.", tags = {
        "monitoring", "sales" })
public class FormMonitoringSales extends Form {

    private MonitoringSalesService salesService;

    JTabbedPane body;
    JPanel top10Panel;
    JPanel revenuePanel;

    JFreeChart top10Chart;
    JFreeChart revenueChart;

    DefaultCategoryDataset top10Data;

    TimeSeries revenuePredictionSeries;
    TimeSeries revenueSeries;
    TimeSeries amountPaidSeries;
    TimeSeriesCollection revenuSeriesCollection;

    @Override
    public void formInit() {
        this.salesService = new MonitoringSalesService(new SqliteSalesDao());
        body = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        final JLabel title = new JLabel("Sales Inventory");
        final JLabel subtitle = new JLabel("This contains overall statistics about the sales.");

        top10Panel = new JPanel(
                new MigLayout("insets 8 0 0 0, al center center", "[grow, fill, center]", "[grow, fill, center]"));
        revenuePanel = new JPanel(
                new MigLayout("insets 8 0 0 0, al center center", "[grow, fill, center]", "[grow, fill, center]"));
        top10Data = new DefaultCategoryDataset();
        revenuePredictionSeries = new TimeSeries("Revenue (Prediction)");
        revenueSeries = new TimeSeries("Revenue");
        amountPaidSeries = new TimeSeries("Amount Paid by Customer");
        revenuSeriesCollection = new TimeSeriesCollection();

        revenuSeriesCollection.addSeries(revenuePredictionSeries);
        revenuSeriesCollection.addSeries(revenueSeries);
        revenuSeriesCollection.addSeries(amountPaidSeries);

        body.addTab("Top 10 Products Sold", top10Panel);
        body.addTab("Total Revenue", revenuePanel);

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");

        setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]", "[][]16px[grow, fill]"));

        add(title);
        add(subtitle);
        add(body, "grow");

        createTop10ChartPanel();
        createRevenueChartPanel();
    }

    @Override
    public void formRefresh() {
        loadData();
    }

    @Override
    public void formOpen() {
        loadData();
    }

    private void loadData() {
        top10Data.clear();

        final List<Top10SellingItemsDto> data = salesService.getTop10SellingItemsReport();

        if (data.isEmpty()) {
            top10Data.addValue(0, "Total Sold", "No Data");
        } else {
            for (final Top10SellingItemsDto item : data) {
                top10Data.addValue(item.totalSold(), "Total Sold", item.itemName());
            }
        }

        RevenuePredictionReport report = null;

        try {
            report = salesService.getRevenuePredictionReport();
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(KompeterDesktopApp.getRootFrame(),
                    "Cannot get reevnue data because of: \n\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        revenueSeries.clear();
        revenuePredictionSeries.clear();

        if (report.actualData().isEmpty()) {
            revenueSeries.add(new Day(), 0.0);
        } else {
            for (final RevenueDto revenue : report.actualData()) {
                final LocalDateTime day = revenue.date().toLocalDateTime();

                revenueSeries.addOrUpdate(new Day(day.getDayOfMonth(), day.getMonthValue(), day.getYear()),
                        revenue.totalRevenue());

                amountPaidSeries.addOrUpdate(new Day(day.getDayOfMonth(), day.getMonthValue(), day.getYear()),
                        revenue.totalPaid());
            }
        }

        if (report.predictedData().isEmpty()) {
            revenuePredictionSeries.add(new Day(), 0.0);
        } else {
            for (final PredictionPoint pv : report.predictedData()) {
                final LocalDateTime day = pv.date().toLocalDateTime();
                revenuePredictionSeries.addOrUpdate(new Day(day.getDayOfMonth(), day.getMonthValue(), day.getYear()),
                        pv.value());
            }
        }
    }

    private void createTop10ChartPanel() {
        top10Chart = ChartFactory.createBarChart(
                "Top 10 Selling Items (Last 30 Days)", "Product", "Units Sold",
                top10Data, PlotOrientation.VERTICAL, true, true, false);

        final ChartPanel panel = new ChartPanel(top10Chart);

        top10Chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        final CategoryPlot plot = top10Chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0, 0, 0, 0)); // transparent plot area
        plot.setDomainGridlinesVisible(true); // vertical gridlines
        plot.setRangeGridlinesVisible(true); // horizontal gridlines
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Customize renderer for colors, bar width, tooltips
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189)); // custom bar color
        renderer.setBarPainter(new BarRenderer().getBarPainter()); // default painter
        renderer.setDrawBarOutline(true);
        renderer.setMaximumBarWidth(0.1); // adjust bar width
        renderer.setDefaultToolTipGenerator(
                (dataset1, row, column) -> dataset1.getRowKey(row) + ": " + dataset1.getValue(row, column));

        plot.setRenderer(renderer);

        top10Chart.getLegend().setItemFont(new Font("Dialog", Font.BOLD, 14));
        top10Chart.getLegend().setBackgroundPaint(new Color(0, 0, 0, 0));

        panel.setDisplayToolTips(true);

        top10Panel.add(panel, "grow");
    }

    private void createRevenueChartPanel() {
        revenueChart = ChartFactory.createTimeSeriesChart(
                "Revenue w/ Prediction (+7 Day Forecast)", "Date", "Revenue (PHP)",
                revenuSeriesCollection, true, true, false);

        final ChartPanel panel = new ChartPanel(revenueChart);
        final XYPlot plot = revenueChart.getXYPlot();
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("tl", "ph"));

        renderer.setSeriesStroke(0, new BasicStroke(3.5f));
        renderer.setSeriesStroke(1, new BasicStroke(3.5f));
        renderer.setSeriesStroke(2, new BasicStroke(3.5f));
        renderer.setSeriesPaint(2, Color.GREEN.darker());

        rangeAxis.setNumberFormatOverride(currencyFormatter);
        renderer.setDefaultShapesVisible(true);
        renderer
                .setDefaultToolTipGenerator(
                        new StandardXYToolTipGenerator(

                                "{0}: {1} = {2}", new SimpleDateFormat("dd-MM-yyyy"), currencyFormatter));
        revenueChart.getLegend().setItemFont(new Font("Montserrat", Font.PLAIN, 16));

        revenueChart.getLegend().setBackgroundPaint(new Color(0, 0, 0, 0));
        revenueChart.setBackgroundPaint(new Color(0, 0, 0, 0));

        plot.setBackgroundPaint(new Color(0, 0, 0, 0));

        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlineStroke(new BasicStroke(1.0f));
        plot.setRangeGridlineStroke(new BasicStroke(1.0f));

        plot.getDomainAxis().setLabelFont(new Font("Montserrat", Font.BOLD, 14));
        plot.getRangeAxis().setLabelFont(new Font("Montserrat", Font.BOLD, 14));

        panel.setDisplayToolTips(true);

        revenuePanel.add(panel, "grow");
    }

}
