package application;
	
import java.io.IOException;
import java.lang.foreign.ValueLayout.OfChar;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

import controller.FoodController;
import controller.OrderController;
import controller.RestaurantController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Food;
import model.Order;
import tools.DatabaseConnector;
import tools.Dbinitializer;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;



public class Main extends Application {
	public static Connection connection;




	@Override
	public void start(Stage primaryStage) throws IOException, SQLException {
		
		
		
		Parent root = FXMLLoader.load(getClass().getResource("/view/Welcome.fxml"));
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("Welcome to Dragon Warrior Noodle and Tofu!");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Dbinitializer.initDatabase();
		connection = DatabaseConnector.getConnection();
		

		//这是管理员添加food
//		Food food = new Food("food2", 10, "food2 des");
//		food.addFood(food);
		
//		这是管理员查看menu，获得所有的food
//		List<Food> foodList = new ArrayList<>();
//		FoodController fController = new FoodController();
//		foodList = fController.getFood();
//
//		for(Food food: foodList) {
//			System.out.println(food.toString());
//		}
		

		
		

		

		
		
		//这是从menu中删除food， 需要传入要删除的id数组
//		FoodController fController = new FoodController();
//		int[] arr = {11, 12};
//		fController.deleteFood(arr);
		
		
		//根据foodname前缀查找food，需要判断返回值是否为空
//		FoodController fController = new FoodController();
//		List<Food> foodList = new ArrayList<>();
//		foodList = fController.getFood("Egg");
//		if (foodList != null ){
//			for(Food food: foodList) {
//			System.out.println(food.toString());
//			}
//		}else {
//			System.out.println("null");
//		}
		
	}
	//List<Food> foodList = new ArrayList<Food>();                                                                                                           
	//FoodController fController = new FoodController();
	//foodList = fController.getFood();
	//public ArrayList<Food> getFoodList() {
	//	return (ArrayList<Food>) this.foodList;
	//}
	
	
	public static void main(String[] args) {

		launch(args);
	}
}

