package controller;

import java.sql.SQLException;
import java.util.function.DoubleToLongFunction;

public interface IRestaurantController {
	//这里比较简单，就是根据不同的时间粒度查营业额
	double getSales(int year) throws SQLException;
	double getSales(int year, String month) throws SQLException;
	double getSales(int year, String month, String day) throws SQLException;
//	RestaurantController restaurantController = new RestaurantController();
//	double sales = restaurantController.getSales(2023,"03","10");
//	System.out.println(sales);
}
