package visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.DefaultPieDataset;

import dbManager.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//With use of AI

public class FoodGroupPieChart extends ApplicationFrame{
	
	//Constructor
	public FoodGroupPieChart(String title) {
		super(title);
	
	
	DefaultPieDataset dataset = createDataset();
	
	JFreeChart chart = ChartFactory.createPieChart("Food items by Group", dataset, true, true, false);
	
	ChartPanel panel = new ChartPanel(chart);
	setContentPane(panel);
	}
	
	// Define private method inside class
	private DefaultPieDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		String sql = "SELECT fg.food_group_name, COUNT(fi.food_id) AS count "
				+ "FROM food_items fi "
				+ "JOIN food_groups fg ON fi.food_group_id = fg.food_group_id "
				+ "GROUP BY fg.food_group_name";
		
		try (
				Connection conn = DatabaseManager.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				){
			while (rs.next()) {
				String groupName = rs.getString("food_group_name");
				int count = rs.getInt("count");
				dataset.setValue(groupName, count);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataset;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FoodGroupPieChart chart = new FoodGroupPieChart("Food Group Pie Chart");
		chart.setSize(800,600);
		chart.setLocationRelativeTo(null);
		chart.setDefaultCloseOperation(EXIT_ON_CLOSE);
		chart.setVisible(true);

	}

}
