package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Main;

public class RestaurantController implements IRestaurantController {

	@Override
	public double getSales(int year) throws SQLException {
		double sales = 0.0;
		String queryString = "SELECT SUM(price) AS total_price FROM "
				+ "orders WHERE strftime('%Y', create_date) = ? ";
		PreparedStatement preparedStatement = Main.connection.prepareStatement(queryString);
		preparedStatement.setString(1, Integer.toString(year)); 
        ResultSet resultSet = preparedStatement.executeQuery();
        sales = resultSet.getDouble("total_price");
		return sales;
	}

	@Override
	public double getSales(int year, String month) throws SQLException {
		double sales = 0.0;
		String queryString = "SELECT SUM(price) AS total_price FROM "
				+ "orders WHERE strftime('%Y-%m', create_date) = ? ";
		PreparedStatement preparedStatement = Main.connection.prepareStatement(queryString);
		String str = Integer.toString(year)+"-"+month;
		System.out.println(str);
		preparedStatement.setString(1, str); 
        ResultSet resultSet = preparedStatement.executeQuery();
        sales = resultSet.getDouble("total_price");
		return sales;
	}

	@Override
	public double getSales(int year, String month, String day) throws SQLException {
		double sales = 0.0;
		String queryString = "SELECT SUM(price) AS total_price FROM "
				+ "orders WHERE create_date = ? ";
		PreparedStatement preparedStatement = Main.connection.prepareStatement(queryString);
		String str = Integer.toString(year)+"-"+month+"-"+day;
		System.out.println(str);
		preparedStatement.setString(1, str); 
        ResultSet resultSet = preparedStatement.executeQuery();
        sales = resultSet.getDouble("total_price");
		return sales;
	}

}
