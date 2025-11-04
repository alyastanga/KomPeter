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

import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService;
import com.github.ragudos.kompeter.database.sqlite.dao.monitoring.SqliteSalesDao;
import com.github.ragudos.kompeter.database.dto.monitoring.Top10SellingItemsDto;
import com.github.ragudos.kompeter.database.dto.monitoring.RevenueDto;
import com.github.ragudos.kompeter.database.dto.monitoring.ExpensesDto;

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

@SystemForm(name = "Monitoring Sales", description = "Shows all possible information about the sales.", tags = {
        "monitoring", "sales"})
public class FormMonitoringSales extends Form {

    private MonitoringSalesService salesService;

    public FormMonitoringSales() {
        this.salesService = new MonitoringSalesService(new SqliteSalesDao());
        
        setLayout(new MigLayout("fill, insets 0"));

        JPanel top10Panel = createTop10ChartPanel();
        JPanel revenuePanel = createRevenueChartPanel();

        JPanel chartsContainerPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 0"));

        chartsContainerPanel.add(new CollapsibleChartPanel("Top 10 Selling Items", top10Panel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Daily Revenue", revenuePanel), "growx");

        JScrollPane scrollPane = new JScrollPane(chartsContainerPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        add(scrollPane, "grow");
    }

    private JPanel createTop10ChartPanel() {
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        List<Top10SellingItemsDto> data = salesService.getTop10SellingItemsReport(from, to);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        if (data.isEmpty()) {
            dataset.addValue(0, "Total Sold", "No Data");
        } else {
            for (Top10SellingItemsDto item : data) {
                dataset.addValue(item.totalSold(), "Total Sold", item.itemName());
            }
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Top 10 Selling Items (Last 30 Days)", "Product", "Units Sold",
                dataset, PlotOrientation.VERTICAL, true, true, false
        );

        return new ChartPanel(barChart);
    }

    private JPanel createRevenueChartPanel() {
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        
        List<RevenueDto> data = salesService.getRevenueReport(from, to);

        TimeSeries series = new TimeSeries("Daily Revenue");
        
        if (data.isEmpty()) {
            series.add(new Day(), 0.0);
        } else {
            for (RevenueDto revenue : data) {
                series.add(new Day(revenue.date()), revenue.totalRevenue());
            }
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Daily Revenue (Last 30 Days)", "Date", "Revenue (PHP)",
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