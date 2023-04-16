package controller;

import java.sql.SQLException;
import java.util.List;

import model.Food;

public interface IFoodController {

	void addFood(Food food);
	List<Food> getFood();
	void deleteFood(int[] ids);
	List<Food> getFood(String name);
	void deleteFood(int cuisineId);
	Food getFoodbyId(int id);
	void editFood(Food food) throws SQLException;
	Food getFoodbyName(String foodName);
}
