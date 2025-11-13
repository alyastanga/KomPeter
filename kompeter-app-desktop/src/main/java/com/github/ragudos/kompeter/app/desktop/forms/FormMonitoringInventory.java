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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.formdev.flatlaf.FlatClientProperties;
import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteInventoryDao;
import com.github.ragudos.kompeter.monitoring.service.MonitoringInventoryService;

import net.miginfocom.swing.MigLayout;

@SystemForm(name = "Monitoring Inventory", description = "Shows all possible information about the inventory.", tags = {
        "monitoring", "inventory" })
public class FormMonitoringInventory extends Form {
    private MonitoringInventoryService inventoryService;

    JTabbedPane body;
    JPanel inventoryCount;
    JFreeChart inventoryCountChart;

    TimeSeries beforeQty;
    TimeSeries addedQty;
    TimeSeries currentQty;
    TimeSeriesCollection inventoryCountSeriesCollection;

    @Override
    public void formInit() {
        this.inventoryService = new MonitoringInventoryService(new SqliteInventoryDao());
        body = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        final JLabel title = new JLabel("Monitor Inventory");
        final JLabel subtitle = new JLabel("This contains overall statistics about the inventory.");

        beforeQty = new TimeSeries("Total Quantity Before Stock Change");
        addedQty = new TimeSeries("Total Quantity Changed");
        currentQty = new TimeSeries("Total Quantity");
        inventoryCountSeriesCollection = new TimeSeriesCollection();
        inventoryCount = new JPanel(
                new MigLayout("insets 8 0 0 0, al center center", "[grow, fill, center]", "[grow, fill, center]"));

        inventoryCountSeriesCollection.addSeries(beforeQty);
        inventoryCountSeriesCollection.addSeries(addedQty);
        inventoryCountSeriesCollection.addSeries(currentQty);

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");

        setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]", "[][]16px[grow, fill]"));

        body.addTab("Inventory Stock Change History", inventoryCount);

        add(title);
        add(subtitle);
        add(body, "grow");

        createInventoryCountChart();
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
        final List<InventoryCountDto> data = inventoryService.getInventoryCountReport();

        addedQty.clear();
        beforeQty.clear();
        currentQty.clear();

        for (final InventoryCountDto d : data) {
            final LocalDateTime day = d.day().toLocalDateTime();

            addedQty.addOrUpdate(new Day(day.getDayOfMonth(), day.getMonthValue(), day.getYear()),
                    d.totalQuantityAdded());
            beforeQty.addOrUpdate(new Day(day.getDayOfMonth(), day.getMonthValue(), day.getYear()),
                    d.totalQuantityBefore());
            currentQty.addOrUpdate(new Day(day.getDayOfMonth(), day.getMonthValue(), day.getYear()), d.totalQuantity());
        }
    }

    private void createInventoryCountChart() {
        inventoryCountChart = ChartFactory.createTimeSeriesChart(
                "Inventory Stock Change History (Last 30 Days)", "Date", "Total Units",
                inventoryCountSeriesCollection, true, true, false);

        final XYPlot plot = inventoryCountChart.getXYPlot();
        final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesPaint(2, Color.GREEN.darker());
        renderer.setSeriesStroke(0, new BasicStroke(3.5f));
        renderer.setSeriesStroke(1, new BasicStroke(3.5f));
        renderer.setSeriesStroke(2, new BasicStroke(3.5f));
        renderer.setDefaultShapesVisible(true);
        renderer
                .setDefaultToolTipGenerator(
                        new StandardXYToolTipGenerator(

                                "{0}: {1} = {2}", new SimpleDateFormat("dd-MM-yyyy"), new DecimalFormat("0")));
        inventoryCountChart.getLegend().setItemFont(new Font("Montserrat", Font.PLAIN, 16));

        inventoryCountChart.getLegend().setBackgroundPaint(new Color(0, 0, 0, 0));
        inventoryCountChart.setBackgroundPaint(new Color(0, 0, 0, 0));
        plot.setBackgroundPaint(new Color(0, 0, 0, 0));

        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlineStroke(new BasicStroke(1.0f));
        plot.setRangeGridlineStroke(new BasicStroke(1.0f));

        plot.getDomainAxis().setLabelFont(new Font("Montserrat", Font.BOLD, 14));
        plot.getRangeAxis().setLabelFont(new Font("Montserrat", Font.BOLD, 14));

        final ChartPanel panel = new ChartPanel(inventoryCountChart);

        panel.setDisplayToolTips(true);

        inventoryCount.add(panel, "grow");
    }
}
