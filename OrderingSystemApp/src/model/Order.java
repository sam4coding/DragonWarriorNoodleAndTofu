package model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import application.Main;
import controller.FoodController;
import controller.OrderController;

public class Order {
	/*********************************************Variables****************************************************/
	// But it will be shown in the 7 digit formatted form: formattedorderID
	private int orderID;
	private String customerName;
	private String createDate;
	private String createTime;
	private int status; 
	private String statusText;
	private double price;
	private List<Food> orderItemsFoods;
	private List<Integer> orderItemsNums;
	private FoodController fController = new FoodController();
	private OrderController fOrderController = new OrderController();
	// This default constructor is for generating sample Order for testing
	// It does not write any record to the sql database. Need to call fOrderController.generateOrder() to save it
	public Order() throws SQLException { 
		this.orderID = fOrderController.getOrders().size()+1;// Add one new record, id is 1 more than existing orders
		this.customerName = "Master Shifu";
		int[] defaultMeal =  {1,2,3,4};
		int[] defaultNum =  {2,2,1,3};
		this.orderItemsFoods = new ArrayList<Food>();
		this.orderItemsNums = new ArrayList<Integer>();
		for (int i=0; i<defaultMeal.length; i++) {
			this.orderItemsFoods.add(fController.getFoodbyId(defaultMeal[i]));
			this.orderItemsNums.add(defaultNum[i]);
		}
		setCreateDate();
		setCreateTime();
		this.status = 0;
		//this.price = price;
		this.price=this.calculatePrice();
	}
	public Order(String customerName,double price, List<Food> orderItemsFoods) {
//		this.formattedOrderID = formattedOrderID;
		this.orderID=0;
		this.customerName = customerName;
		setCreateDate();
		setCreateTime();
		this.status = 0;
		this.price = price;
		this.orderItemsFoods = orderItemsFoods;
	}

	public Order(String customerName,
					double price,
					List<Food> orderItemsFoods, 
					List<Integer> orderItemsNums) {
		try {
			this.orderID=this.fOrderController.getOrders().size()+1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.price=price;
		this.customerName = customerName;
		setCreateDate();
		setCreateTime();
		this.status = 0;
		//this.price = price;
		//this.orderItemsFoods = orderItemsFoods;
		//this.orderItemsNums = orderItemsNums;
		this.orderItemsFoods = new ArrayList<Food>();
		this.orderItemsNums = new ArrayList<Integer>();
		for (int i=0; i<orderItemsFoods.size(); i++) {
			this.orderItemsFoods.add(orderItemsFoods.get(i));
			this.orderItemsNums.add(orderItemsNums.get(i));
			System.out.println(orderItemsNums.get(i)+ " servings of " + orderItemsFoods.get(i).getName() + "is added in Order constructor");
		}
	}
	public Order(int orderId, String customerName, String createDate, String createTime, int status,
			double price, List<Food> orderItemsFoods, List<Integer> orderItemsNums) {
		this.orderID = orderId;
//		this.formattedOrderID = formattedOrderID;
		this.customerName = customerName;
		this.createDate = createDate;
		this.createTime = createTime;
		this.status = status;
		this.price = price;
		this.orderItemsFoods = orderItemsFoods;
		this.orderItemsNums = orderItemsNums;
	}
	

	public String getFormattedOrderID() {

        String formattedPid = String.format("%04d", this.orderID);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMdd");
        String formattedDate = currentDate.format(dateFormatter);


        String formattedId =  formattedDate + 'A' + formattedPid;

		return formattedId;
	}
	
	
	public List<Food> getOrderItemsFoods() {
		return orderItemsFoods;
	}
	public void setOrderItemsFoods(List<Food> orderItemsFoods) {
		this.orderItemsFoods = orderItemsFoods;
	}
	public List<Integer> getOrderItemsNums() {
		return orderItemsNums;
	}
	public void setOrderItemsNums(List<Integer> orderItemsNums) {
		this.orderItemsNums = orderItemsNums;
	}
	// getter of orderID
	public int getOrderID() {
		return this.orderID;
	}
	
	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", customerName=" + customerName
				+ ", createTime=" + createTime + ", status=" + status + ", price=" + price + "]";
	}
	// getter and setter of String customer_Name;
	public String getCustomerName() {
		return this.customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	// getter and setter of String createTtime;
	public String getCreateTime() {
		return this.createTime;
	}
	
	public String getCreateDate() {
		return this.createDate;
	}
	private void setCreateTime() {
		// get the current time
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String timeString = formatter.format(date);
		this.createTime = timeString;
	}

	private void setCreateDate() {
		Date date = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = formatter.format(date);
	    this.createDate = dateString;
	}

	// getter and setter of String status;
	public int getStatus() {
		return this.status;
	}
	
	// getter and setter of price
	public double getPrice() {
		//calculatePrice();
		return this.price;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	// A private method to calculate the total price 
	public double calculatePrice() {
		double result = 0;
		for (int i = 0; i < this.orderItemsFoods.size(); i++) {
			result += this.orderItemsFoods.get(i).getPrice()*this.orderItemsNums.get(i);
		}
		System.out.println("This order costs: "+ result);
		this.price=result;
		return result;
	}
	
	// Helper method to convert status int value to string for display 
	public String getStatusString(int status) { 
		switch (status) { 
			case 0: return "received";
			case 1: return "preparing";
			case 2: return "ready";
			case 3: return "delivered";
			default: return "error";
		}
	}
	// convert status number to status text
	public String getStatusText() {
		this.statusText=getStatusString(getStatus());
		return this.statusText;
	}
}
