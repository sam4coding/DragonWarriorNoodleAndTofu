package model;

import java.beans.Customizer;

import controller.FoodController;

public class Food {
	// id of the cuisine, or kind of food, not specific to food item, alternative to Food.name
	private int id;
	// name of the cuisine, or kind of food, not specific to food item, alternative to Food.cusineID
	private String name;
	// text description of the cuisine, or kind of food, not specific to food item
	private String description;
	// file name of the image of the cuisine, or kind of food, not specific to food item
	private String imageName;
	// price of the cuisine
	private double price; 
	// constructors
	public Food() {
		this.id=0;
		this.name = "Mr.Ping's special treat";
		this.description = "Ask Mr.Ping to find out more.";
		this.imageName="questionMark.png";
		this.price=9.99;
	}
	public Food(int id, String name, String description, String imageName,double price) {
		this.id=id;
		this.name = name;
		this.description = description;
		this.imageName=imageName;
		this.price=price;
	}
	// add food to the data base, only Mr. Ping can do that
	public void addFood(Food food) {
		FoodController fController = new FoodController();
		fController.addFood(food);
	}
	// getter and setter of id
	public int getid() {
		return this.id;
	}
	public void setid(int id) {
		this.id=id;
	}
	// getter and setter of cuisineName
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	// getter and setter of description
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	// getter and setter of imageName
	public String getImageName() {
		return this.imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	// getter and setter of price
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price=price;
	}
}
