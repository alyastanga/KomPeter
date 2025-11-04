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
import com.github.ragudos.kompeter.database.dto.monitoring.ProfitDto; 
import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService.PredictionPoint;
import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService.RevenuePredictionReport;
import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService.ExpensePredictionReport;
import com.github.ragudos.kompeter.monitoring.service.MonitoringSalesService.ProfitPredictionReport; 

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SystemForm(name = "Monitoring Sales", description = "Shows all possible information about the sales.", tags = {
        "monitoring", "sales"})
public class FormMonitoringSales extends Form {

    private MonitoringSalesService salesService;

    public FormMonitoringSales() {
        this.salesService = new MonitoringSalesService(new SqliteSalesDao());
        
        setLayout(new MigLayout("fill, insets 0"));

        JPanel revenuePredictionPanel = createRevenuePredictionChart();
        JPanel expensePredictionPanel = createExpensePredictionChart();
        JPanel profitPredictionPanel = createProfitPredictionChart();
        JPanel top10Panel = createTop10ChartPanel();
        JPanel revenuePanel = createRevenueChartPanel();
        JPanel expensePanel = createExpenseChartPanel();
        JPanel profitPanel = createProfitChartPanel(); 

        JPanel chartsContainerPanel = new JPanel(new MigLayout("wrap 1, fillx, insets 0"));

        chartsContainerPanel.add(new CollapsibleChartPanel("Revenue Prediction", revenuePredictionPanel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Expense Prediction", expensePredictionPanel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Profit Prediction", profitPredictionPanel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Top 10 Selling Items", top10Panel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Daily Revenue", revenuePanel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Daily Expenses", expensePanel), "growx");
        chartsContainerPanel.add(new CollapsibleChartPanel("Daily Profit", profitPanel), "growx"); 

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
    
    private JPanel createExpenseChartPanel() {
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        
        List<ExpensesDto> data = salesService.getExpensesReport(from, to);

        TimeSeries series = new TimeSeries("Daily Expenses");
        
        if (data.isEmpty()) {
            series.add(new Day(), 0.0);
        } else {
            for (ExpensesDto expense : data) {
                series.add(new Day(expense.date()), expense.totalExpenses());
            }
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Daily Expenses (Last 30 Days)", "Date", "Expenses (PHP)",
                dataset, true, true, false
        );

        return new ChartPanel(timeChart);
    }

    private JPanel createProfitChartPanel() {
        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
        
        List<ProfitDto> data = salesService.getProfitReport(from, to);

        TimeSeries series = new TimeSeries("Daily Profit");
        
        if (data.isEmpty()) {
            series.add(new Day(), 0.0);
        } else {
            for (ProfitDto profit : data) {
                series.add(new Day(profit.date()), profit.totalProfit());
            }
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Daily Profit (Last 30 Days)", "Date", "Profit (PHP)",
                dataset, true, true, false
        );

        return new ChartPanel(timeChart);
    }

    private JPanel createRevenuePredictionChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries actualSeries = new TimeSeries("Actual Revenue");
        TimeSeries predictedSeries = new TimeSeries("Predicted Trend");

        try {
            Timestamp to = Timestamp.valueOf(LocalDateTime.now());
            Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
            RevenuePredictionReport report = salesService.getRevenuePredictionReport(from, to, 7);

            if (report.actualData().isEmpty()) {
                actualSeries.add(new Day(), 0.0);
            } else {
                for (RevenueDto revenue : report.actualData()) {
                    actualSeries.add(new Day(revenue.date()), revenue.totalRevenue());
                }
            }

            if (report.predictedData().isEmpty()) {
                predictedSeries.add(new Day(), 0.0);
            } else {
                for (PredictionPoint pv : report.predictedData()) {
                    predictedSeries.add(new Day(pv.date()), pv.value());
                }
            }
            
        } catch (SQLException e) {
            Logger.getLogger(FormMonitoringSales.class.getName()).log(Level.SEVERE, "Failed to load revenue prediction chart", e);
            actualSeries.add(new Day(), 0.0);
            predictedSeries.add(new Day(), 0.0);
        }

        dataset.addSeries(actualSeries);
        dataset.addSeries(predictedSeries);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Revenue Prediction (Last 30 Days + 7 Day Forecast)", "Date", "Revenue (PHP)",
                dataset, true, true, false
        );
        
        stylePredictionChart(timeChart, new Color(0, 102, 204), Color.RED);
        return new ChartPanel(timeChart);
    }
    
    private JPanel createExpensePredictionChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries actualSeries = new TimeSeries("Actual Expenses");
        TimeSeries predictedSeries = new TimeSeries("Predicted Trend");

        try {
            Timestamp to = Timestamp.valueOf(LocalDateTime.now());
            Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
            ExpensePredictionReport report = salesService.getExpensePredictionReport(from, to, 7);

            if (report.actualData().isEmpty()) {
                actualSeries.add(new Day(), 0.0);
            } else {
                for (ExpensesDto expense : report.actualData()) {
                    actualSeries.add(new Day(expense.date()), expense.totalExpenses());
                }
            }

            if (report.predictedData().isEmpty()) {
                predictedSeries.add(new Day(), 0.0);
            } else {
                for (PredictionPoint pv : report.predictedData()) {
                    predictedSeries.add(new Day(pv.date()), pv.value());
                }
            }
            
        } catch (SQLException e) {
            Logger.getLogger(FormMonitoringSales.class.getName()).log(Level.SEVERE, "Failed to load expense prediction chart", e);
            actualSeries.add(new Day(), 0.0);
            predictedSeries.add(new Day(), 0.0);
        }

        dataset.addSeries(actualSeries);
        dataset.addSeries(predictedSeries);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Expense Prediction (Last 30 Days + 7 Day Forecast)", "Date", "Expenses (PHP)",
                dataset, true, true, false
        );
        
        stylePredictionChart(timeChart, new Color(204, 102, 0), Color.RED);
        return new ChartPanel(timeChart);
    }
    
    private JPanel createProfitPredictionChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries actualSeries = new TimeSeries("Actual Profit");
        TimeSeries predictedSeries = new TimeSeries("Predicted Trend");

        try {
            Timestamp to = Timestamp.valueOf(LocalDateTime.now());
            Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusDays(30));
            ProfitPredictionReport report = salesService.getProfitPredictionReport(from, to, 7);

            if (report.actualData().isEmpty()) {
                actualSeries.add(new Day(), 0.0);
            } else {
                for (ProfitDto profit : report.actualData()) {
                    actualSeries.add(new Day(profit.date()), profit.totalProfit());
                }
            }

            if (report.predictedData().isEmpty()) {
                predictedSeries.add(new Day(), 0.0);
            } else {
                for (PredictionPoint pv : report.predictedData()) {
                    predictedSeries.add(new Day(pv.date()), pv.value());
                }
            }
            
        } catch (SQLException e) {
            Logger.getLogger(FormMonitoringSales.class.getName()).log(Level.SEVERE, "Failed to load profit prediction chart", e);
            actualSeries.add(new Day(), 0.0);
            predictedSeries.add(new Day(), 0.0);
        }

        dataset.addSeries(actualSeries);
        dataset.addSeries(predictedSeries);

        JFreeChart timeChart = ChartFactory.createTimeSeriesChart(
                "Profit Prediction (Last 30 Days + 7 Day Forecast)", "Date", "Profit (PHP)",
                dataset, true, true, false
        );
        
        stylePredictionChart(timeChart, new Color(0, 153, 51), Color.RED);
        return new ChartPanel(timeChart);
    }
    
    private void stylePredictionChart(JFreeChart timeChart, Color actualColor, Color predictedColor) {
        XYPlot plot = (XYPlot) timeChart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        renderer.setSeriesPaint(0, actualColor);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);

        renderer.setSeriesPaint(1, predictedColor);
        renderer.setSeriesStroke(1, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_ROUND, 1.0f, new float[]{6.0f, 6.0f}, 0.0f));
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        
        plot.setRenderer(renderer);
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