package controller;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Main;
import model.Food;
import model.Order;
import tools.DatabaseConnector;

public class OrderController implements IOderController{
	FoodController fController = new FoodController();
	public List<Order> getOrders() throws SQLException{
		List<Order> ordersList = new ArrayList<>();	
		try (Connection connection = DatabaseConnector.getConnection()) {
			String queryString = "SELECT * FROM orders";
	        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
	
	        ResultSet resultSet = preparedStatement.executeQuery();
	        while (resultSet.next()) {
	        	Map<Integer, Integer> map = new HashMap<>();
	        	List<Food> foodlist = new ArrayList<>();
	        	List<Integer> foodnums = new ArrayList<>();
	        	int id = resultSet.getInt("id");
	            String customerName = resultSet.getString("customer_name");
	            String createDate = resultSet.getString("create_date");
	            String createTime = resultSet.getString("create_time");
	            int status = resultSet.getInt("status");
	            double price = resultSet.getDouble("price");
	            
	            OrderController orderController = new OrderController();
	            map = orderController.getFoodIdsbyOrderid(id);
	            FoodController foodController = new FoodController();
	            
	            Food food = new Food();
	            for(Integer foodId : map.keySet()) {
	            	food = foodController.getFoodbyId(foodId);
	            	foodlist.add(food);
	            	foodnums.add(map.get(foodId));
	            }
	            	
	            Order order = new Order(id, customerName, createDate, createTime, status, price, foodlist, foodnums);
	            ordersList.add(order);
	        }
		}  catch (SQLException e) {
            e.printStackTrace();
        }
		
		return ordersList;
		
	}
	
	public Map<Integer, Integer> getFoodIdsbyOrderid(int order_id) throws SQLException {
		HashMap<Integer, Integer> map = new HashMap<>();
		String queryString = "SELECT food_id, nums FROM order_food WHERE order_id = ?";
		PreparedStatement preparedStatement = Main.connection.prepareStatement(queryString);
		preparedStatement.setInt(1, order_id); 
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
        	int foodId = resultSet.getInt("food_id");
        	int foodNum = resultSet.getInt("nums");
        	map.put(foodId, foodNum);
        }
		return map;
	}
	
	/*public void generateOrder(String customerName, int[] foodIds, int[] foodNums, double price) throws SQLException {
    	List<Food> foodlist = new ArrayList<>();
    	List<Integer> foodnumsIntegers = new ArrayList<>();
        //if (orderId != -1) {
        	for(int i = 0; i < foodIds.length; i ++) {
        		foodlist.add(fController.getFoodbyId(foodIds[i]));
        		foodnumsIntegers.add(foodNums[i]);
        		//System.out.println(foodnumsIntegers.get(i)+ " servings of " + foodlist.get(i).getName() + "is added in Order constructor");
            }
        //}
    	Order order = new Order(customerName, price, foodlist, foodnumsIntegers);
		String queryString = "INSERT INTO orders (customer_name, create_date, create_time,status,price) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Main.connection.prepareStatement(queryString);
        preparedStatement.setString(1, order.getCustomerName());
        preparedStatement.setString(2, order.getCreateDate());
        preparedStatement.setString(3, order.getCreateTime());
        preparedStatement.setInt(4, order.getStatus()); 
        preparedStatement.setDouble(5, order.getPrice());
        
        int affectedRows = preparedStatement.executeUpdate();
        int orderId = 0;
        if (affectedRows > 0) {
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }
        }
        if (orderId != -1) {
        	for(int i = 0; i < foodIds.length; i ++) {
        		InsertIntoOrder_food(orderId, foodIds[i], foodNums[i]);
            }
        }

	}*/
	public void generateOrder(String customerName, int[] foodIds, int[] foodNums, double price) throws SQLException {
    	List<Food> foodlist = new ArrayList<>();
    	List<Integer> foodnumsIntegers = new ArrayList<>();
    	Order order = new Order(customerName, price, foodlist, foodnumsIntegers);
		String queryString = "INSERT INTO orders (customer_name, create_date, create_time,status,price) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = Main.connection.prepareStatement(queryString);
        preparedStatement.setString(1, order.getCustomerName());
        preparedStatement.setString(2, order.getCreateDate());
        preparedStatement.setString(3, order.getCreateTime());
        preparedStatement.setInt(4, order.getStatus()); 
        preparedStatement.setDouble(5, order.getPrice());
        
        int affectedRows = preparedStatement.executeUpdate();
        int orderId = -1;

        if (affectedRows > 0) {
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }
        }
        
        if (orderId != -1) {
        	for(int i = 0; i < foodIds.length; i ++) {
        		InsertIntoOrder_food(orderId, foodIds[i], foodNums[i]);
            }
        }
	}


	public void InsertIntoOrder_food(int orderId, int foodId, int nums) throws SQLException {
		String queryString = "INSERT INTO order_food (order_id, food_id, nums) VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = Main.connection.prepareStatement(queryString);
		preparedStatement.setInt(1, orderId);
		preparedStatement.setInt(2, foodId);
		preparedStatement.setInt(3, nums);
		preparedStatement.executeUpdate();
		
	}

	@Override
	public void updateOrderStatus(int orderId, int newStatus) throws SQLException {
		String updateString = "UPDATE orders SET status = ? WHERE orders.id = ?";
        PreparedStatement preparedStatement = Main.connection.prepareStatement(updateString);
		preparedStatement.setInt(1, newStatus);
		preparedStatement.setInt(2, orderId);
		preparedStatement.executeUpdate();
	}
	
	
}
