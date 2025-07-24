package observer;

import dao.FoodSwapDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import classes.SwapLoggedEvent;
import classes.SwapSuggestion;

import javax.swing.*;
import java.awt.*;

public class SwapChartObserver implements SwapEventListener {
    private final FoodSwapDAO dao = new FoodSwapDAO();
    private final JFrame frame;
    private final DefaultCategoryDataset dataset;
    private final ChartPanel chartPanel;

    public SwapChartObserver() {
        frame = new JFrame("Nutrient Impact Chart");
        dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Nutrient Comparison", "Type", "Amount", dataset);
        chartPanel = new ChartPanel(chart);
        frame.setContentPane(chartPanel);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    @Override
    public void onSwapLogged(SwapLoggedEvent event) {
        SwingUtilities.invokeLater(() -> {
            dataset.clear();

            SwapSuggestion suggestion = event.getSuggestion();
            String nutrient = event.getNutrientName();

            dataset.addValue(suggestion.getOriginalValue(), "Original", nutrient);
            dataset.addValue(suggestion.getRecommendedValue(), "Swap", nutrient);

            frame.setVisible(true);
        });
    }
}
