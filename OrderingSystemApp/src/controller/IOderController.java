package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Food;
import model.Order;

public interface IOderController {
	void generateOrder(String customerName, int[] foodIds, int[] foodNums, double price) throws SQLException; //生成order
//	OrderController orderController = new OrderController();
//	int[] ids = new int[]{1,2};
//	int[] nums = new int[] {2,3};
//	orderController.generateOrder("mdh22", ids, nums, 45.9);
	
	
	
	List<Order> getOrders() throws SQLException; //得到所有的order，每一个order里有两个list分别是foodlist和foodnums
	//输出订单列表页面的信息，一个订单有多个food，每个food有对应number
//	List<Order> ordersList = new ArrayList<>();
//	OrderController oController = new OrderController();
//	ordersList = oController.getOrders();
//	for(Order order: ordersList) {
//		System.out.println(order.getCustomerName() + ", " + order.getCreateTime());
//		List<Food> foodlist = order.getOrderItemsFoods();
//		List<Integer> foodnums = order.getOrderItemsNums();
//
//		int n = foodlist.size();
//		for(int i = 0; i < n; i ++) {
//			System.out.println(foodlist.get(i).getName() + ": " + foodnums.get(i));
//		}
//		System.out.println();
//	}
	
	
	// status 默认是0， 可以修改成 1， 2
	// 0:received   1:done   2:delivered
	void updateOrderStatus(int orderId, int newStatus) throws SQLException;
//	OrderController orderController = new OrderController();
//	int orderId = 1;
//	int newStatus = 2;
//	orderController.updateOrderStatus(orderId, newStatus);
}
