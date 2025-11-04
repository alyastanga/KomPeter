/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.app.desktop.forms;

import com.github.ragudos.kompeter.app.desktop.system.Form;
import com.github.ragudos.kompeter.app.desktop.utilities.SystemForm;

import com.github.ragudos.kompeter.monitoring.service.MonitoringInventoryService;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteInventoryDao;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10LowStockItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryValueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.InventoryCountDto;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@SystemForm(name = "Monitoring Inventory", description = "Shows all possible information about the inventory.", tags = {
        "monitoring", "inventory"})
public class FormMonitoringInventory extends Form {

    private MonitoringInventoryService inventoryService;

    public FormMonitoringInventory() {
        this.inventoryService = new MonitoringInventoryService(new SqliteInventoryDao());
        
        setLayout(new MigLayout("fill, insets 0"));

        JPanel inventoryValuePanel = createInventoryValueChart();
        JPanel inventoryCountPanel = createInventoryCountChart();

        JPanel chartsContainerPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 0"));

        chartsContainerPanel.add(new CollapsibleChartPanel("Total Inventory Value", inventoryValuePanel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Total Inventory Count", inventoryCountPanel), "growx");

        JScrollPane scrollPane = new JScrollPane(chartsContainerPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane, "grow");
    }

    private JPanel createInventoryValueChart() {
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        List<InventoryValueDto> data = inventoryService.getInventoryValueReport(from, to);

        TimeSeries series = new TimeSeries("Total Inventory Value");
        
        if (data.isEmpty()) {
            series.add(new Day(), 0.0);
        } else {
            for (InventoryValueDto iv : data) {
                series.add(new Day(iv.date()), iv.totalInventoryValue());
            }
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Total Inventory Value (Last 30 Days)", "Date", "Value (PHP)",
                dataset, true, true, false
        );

        return new ChartPanel(timeChart);
    }
    
    private JPanel createInventoryCountChart() {
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        List<InventoryCountDto> data = inventoryService.getInventoryCountReport(from, to);

        TimeSeries series = new TimeSeries("Total Item Count");
        
        if (data.isEmpty()) {
            series.add(new Day(), 0);
        } else {
            for (InventoryCountDto ic : data) {
                series.add(new Day(ic.date()), ic.totalInventoryCount());
            }
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Total Inventory Count (Last 30 Days)", "Date", "Total Units",
                dataset, true, true, false
        );

        return new ChartPanel(timeChart);
    }
    
    private class CollapsibleChartPanel extends JPanel implements ActionListener {
        private final JPanel contentPanel;
        private final JButton toggleButton;

        public CollapsibleChartPanel(String title, JPanel contentPanel) {
            super(new MigLayout("wrap 1, fillx, insets 0"));
            this.contentPanel = contentPanel;

            JPanel headerPanel = new JPanel(new MigLayout("fillx, insets 2 5 2 5"));
            this.toggleButton = new JButton("-");
            this.toggleButton.addActionListener(this);
            
            headerPanel.add(toggleButton);
            headerPanel.add(new JLabel(title));

            add(headerPanel, "growx");
            add(contentPanel, "grow");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean isVisible = contentPanel.isVisible();
            contentPanel.setVisible(!isVisible);
            toggleButton.setText(isVisible ? "+" : "-");
            revalidate();
            repaint();
        }
    }
}
