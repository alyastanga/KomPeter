/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/

package com.github.ragudos.kompeter.app.desktop.forms;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.formdev.flatlaf.FlatClientProperties;
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

    }

    private void createTop10ChartPanel() {
        final Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        final Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        final List<Top10SellingItemsDto> data = salesService.getTop10SellingItemsReport(from, to);

        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (data.isEmpty()) {
            dataset.addValue(0, "Total Sold", "No Data");
        } else {
            for (final Top10SellingItemsDto item : data) {
                dataset.addValue(item.totalSold(), "Total Sold", item.itemName());
            }
        }

        final JFreeChart barChart = ChartFactory.createBarChart(
                "Top 10 Selling Items (Last 30 Days)", "Product", "Units Sold",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        new ChartPanel(barChart);
    }

    private void createRevenueChartPanel() {
        final Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        final Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));

        final List<RevenueDto> data = salesService.getRevenueReport(from, to);

        final TimeSeries series = new TimeSeries("Daily Revenue");

        if (data.isEmpty()) {
            series.add(new Day(), 0.0);
        } else {
            for (final RevenueDto revenue : data) {
                series.add(new Day(revenue.date()), revenue.totalRevenue());
            }
        }

        final TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        final JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Daily Revenue (Last 30 Days)", "Date", "Revenue (PHP)",
                dataset, true, true, false);

        new ChartPanel(timeChart);
    }

    private void createRevenuePredictionChart() {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        final TimeSeries actualSeries = new TimeSeries("Actual Revenue");
        final TimeSeries predictedSeries = new TimeSeries("Predicted Trend");

        try {
            final Timestamp to = Timestamp.valueOf(LocalDateTime.now());
            final Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
            final RevenuePredictionReport report = salesService.getRevenuePredictionReport(from, to, 7);

            if (report.actualData().isEmpty()) {
                actualSeries.add(new Day(), 0.0);
            } else {
                for (final RevenueDto revenue : report.actualData()) {
                    actualSeries.add(new Day(revenue.date()), revenue.totalRevenue());
                }
            }

            if (report.predictedData().isEmpty()) {
                predictedSeries.add(new Day(), 0.0);
            } else {
                for (final PredictionPoint pv : report.predictedData()) {
                    predictedSeries.add(new Day(pv.date()), pv.value());
                }
            }

        } catch (final SQLException e) {
            Logger.getLogger(FormMonitoringSales.class.getName()).log(Level.SEVERE,
                    "Failed to load revenue prediction chart", e);
            actualSeries.add(new Day(), 0.0);
            predictedSeries.add(new Day(), 0.0);
        }

        dataset.addSeries(actualSeries);
        dataset.addSeries(predictedSeries);

        final JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Revenue Prediction (Last 30 Days + 7 Day Forecast)", "Date", "Revenue (PHP)",
                dataset, true, true, false);

        new ChartPanel(timeChart);
    }

}
