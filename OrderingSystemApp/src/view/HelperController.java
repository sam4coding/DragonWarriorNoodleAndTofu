package view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.FoodController;
import controller.OrderController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Food;
import model.Order;

public class HelperController {
	private ObservableList<Food> addedDishesShow= FXCollections.observableArrayList();// For showing all dishes
	private ObservableList<Order> allOrderShow= FXCollections.observableArrayList();// For showing all orders
	private List<Food> addedDishes = new ArrayList<Food>(); // For manipulating all dishes, need clearing after each save
	private List<Order> allOrder = new ArrayList<Order>(); // For manipulating all orders, not for show
	private double thetotalprice=0.0; 
	
	// widgets on general
    @FXML
    private Button reloadTheDatabaseButton; // On top of the page, for refreshing the page to fetch data
    @FXML
    private Button Helper_exit; // On top of the page, exit the page elegantly
    // widgets on menu page
    @FXML
    private GridPane dishGallery; // Showing the menu with pictures
    @FXML
    private Text dishID; // showing the dishID upon clicking on the dish picture
    @FXML
    private Text dishName;// showing the dishName upon clicking on the dish picture
    @FXML
    private Text price;// showing the dish price upon clicking on the dish picture
    @FXML
    private Label dishDescription;// showing the dish description upon clicking on the dish picture
    @FXML
    private ImageView dishImage;// showing the dish image upon clicking on the dish picture
    @FXML
    private Button addDish;  // adding the selected dish to addedDishes
    // widgets on order page
	@FXML
    private TextField cusName; // text field for input customer name. Will give warning when clicking on saveBtn if it's left empty
    @FXML
    private Button removeDish;  // this is the button to remove a dish from selected ones
    @FXML
    private VBox selectedDishes; // this is the vbox to show the dishes already been selected
	@FXML
    private TableColumn<Food, String> foodName; // Table column to show food name in addedDishes list
    @FXML
    private TableColumn<Food, Double> foodPrice;// Table column to show food price in addedDishes list
    @FXML
    private TableView<Food> foodTable;// TableView to show addedDishes
    @FXML
    private TextField totalPrice;// Textbox to show calculated total price of current addedDishes
    @FXML
    private Button saveBtn;// Generate an order in addedDishes and save it in database, and then refresh addedDishes list for next order
	// Those on the Tab "All Orders"
	@FXML
	private TableView<Order> ordersTableView; //The table view to show all orders in the database
	@FXML
	private TableColumn<Order,String> Order_ID; //The table column to show ID of all orders in the reversed order, newer ones comes on top
    @FXML
    private TableColumn<Order, String> Updated_time;//The table column to show Updated_time of all orders 
	@FXML
	private TableColumn<Order, String> Customer_Name;//The table column to show customer name of all orders 
	@FXML
	private TableColumn<Order, String> Order_Dishes;//The table column to show a list of all dish names of all orders
    @FXML
    private TableColumn<Order, String> Servings;//The table column to show all dish servings of all orders
	@FXML
	private TableColumn<Order, String> Status;//The table column to show status of all orders
	// database controllers 
	FoodController fController = new FoodController();
	OrderController fOrderController = new OrderController();
	List<Food> foodList = new ArrayList<>();//
    public void initialize() throws SQLException {
		/***************************Populating the dishes tab page**********************************************/
	    // Step 1. create a Food list and load it to the foodGallery
    	// This is for accessing all dishes in the data base
		foodList = fController.getFood();
		int columnIndex=0, rowIndex=0;
		/*** Load images of the food, name on bottom (button) unto the dishGallery ***/
	    for (Food food : foodList) {
	    	//add the Dish image to dishGallery
	    	Image image = new Image("/images/"+food.getImageName());//
	    	ImageView imageView = new ImageView(image);
	    	Button button = new Button(food.getName());
	    	button.setWrapText(true);//wrap the text to fit the text box
	    	button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");//设置背景板透明色
	        StackPane stackPane = new StackPane();
	        stackPane.setMaxSize(110, 110);
	        stackPane.getChildren().addAll(imageView, button);
	        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
	        StackPane.setAlignment(imageView, Pos.CENTER);
	        imageView.fitWidthProperty().bind(stackPane.widthProperty());
	        imageView.fitHeightProperty().bind(stackPane.heightProperty());
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	dishName.setText(food.getName());
	            	dishID.setText(food.getid()+"");
	            	price.setText(food.getPrice()+"");
	            	dishImage.setImage(image);
	            	dishDescription.setText(food.getDescription());
	            	//dishIngradient.setText(food.getIngradient());//Will update in the future
	            }
	        });
	    	//imageView.setPreserveRatio(true);
	        dishGallery.add(stackPane, columnIndex, rowIndex);
	        // update columnIndex and rowIndex for next image
	        if (columnIndex==5) {
	        	columnIndex=0;
	        	rowIndex++;
	        } else {
	        	columnIndex++;
	        }
	    }
	    /**************************** The Order Tab *************************************/
        foodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        FXCollections.reverse(addedDishesShow);
        foodTable.setItems(addedDishesShow);

        /**************************** The All Order Tab *************************************/
		/***************************Populating the order tab page, order management**********************************************/
        //getting the current list of all ordersFXCollections.reverse(
		allOrder = fOrderController.getOrders();
		// clear out the table order TableView for populating
		ordersTableView.getItems().clear();
		// 1st column, the index of the order. 
		// There is a formattedOrderID field and a getFormattedOrderID() method to call
		Order_ID.setCellValueFactory(new PropertyValueFactory<>("formattedOrderID"));
		// set the "formattedOrderID" to auto wrap. Somehow, this can be done in javaFX code only. 
		// scene builder cannot do auto wrap for text in the table view. 
		Order_ID.setCellFactory(tc -> {
		    TableCell<Order, String> cell = new TableCell<Order, String>() {
		        private Text text;
		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);
		            if (empty) {
		                setGraphic(null);
		            } else {
		                text = new Text(item);
		                text.wrappingWidthProperty().bind(Order_ID.widthProperty());
		                setGraphic(text);
		            }
		        }
		    };
		    return cell;
		});
		// 2nd column, the date and time when the order is generated. 
		// This includes 2 fields in an order object, need to implement a callback, instead of using PropertyValueFactory API.
		Updated_time.setCellValueFactory(new Callback<CellDataFeatures<Order,String>, ObservableValue<String>>() { 
			@Override 
			public ObservableValue<String> call(CellDataFeatures<Order, String> data) { 
				Order currentOrder = data.getValue(); 
				String text = currentOrder.getCreateTime()+ " " + currentOrder.getCreateDate();
				return new SimpleStringProperty(text); 
			} 
		});
		// 3rd column, the customer Name
		Customer_Name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
		//Order_Dishes.setCellValueFactory(new PropertyValueFactory<>("foodNameString"));
		// 4th column, all the dishes' names
		Order_Dishes.setCellValueFactory(new Callback<CellDataFeatures<Order,String>, ObservableValue<String>>() { 
			@Override 
			public ObservableValue<String> call(CellDataFeatures<Order, String> data) { 
				Order currentOrder = data.getValue(); 
				String foodNameString = "";
				for (Food food: currentOrder.getOrderItemsFoods()) {
					foodNameString += food.getid() + ": " + food.getName()+"\n";
				}
				return new SimpleStringProperty(foodNameString); 
			} 
		});
		// 5th column, all the dishes' servings
		Servings.setCellValueFactory(new Callback<CellDataFeatures<Order,String>, ObservableValue<String>>() { 
			@Override 
			public ObservableValue<String> call(CellDataFeatures<Order, String> data) { 
				Order currentOrder = data.getValue(); 
				String servingString = "";
				for (Integer num: currentOrder.getOrderItemsNums()) {
					servingString += num+"\n";
				}
				return new SimpleStringProperty(servingString); 
			} 
		});
		// 6th column, the status of the order, need to toggle such as 2:"ready"-3:"delivered", 
		Status.setCellFactory(column -> new TableCell<Order, String>() {//Using setCellFactory instead of setCellValueFactory
			private final Button button = new Button(); // because we use button
			@Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	            } else {
	                Order order = getTableView().getItems().get(getIndex());
	                int status = order.getStatus();
	                button.setText(order.getStatusString(status));
		    		if (status!=2 ) {// Helpers only do 2:ready -> 3:delivered conversion
		    			button.setDisable(true);//If the status is not 2, the button will not be available
		    		} 
	                button.setOnAction(e -> {
                		if (order.getStatus() == 2) { 
                			int new_status = order.getStatus()+1;//do 2:ready -> 3:delivered
                			button.setDisable(true);// and then disable the button
                			order.setStatus(new_status);
		    			try {
							fOrderController.updateOrderStatus(order.getOrderID(), new_status);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
	                    button.setText(order.getStatusString(new_status));
                		}
	                });
	                setGraphic(button);
	            }
	        }
		});
	    allOrderShow.addAll(allOrder);
	    FXCollections.reverse(allOrderShow);
	    ordersTableView.setItems(allOrderShow);
    }
	// The function of the bottom right "Add" button on the menu tab, 
    // this is to add the selected dish into the selectedDish ArrayList    
    @FXML
    public void addDish(ActionEvent event) {
    	// create a dish instance from the database record
        Food food = fController.getFoodbyId(Integer.valueOf(dishID.getText()));
        // add the dish instance to addedDishes list
        addedDishes.add(food);
        // update the current thetotalprice value
        thetotalprice+=food.getPrice();
        // update "totalPrice" text field in "Order" tab
        totalPrice.setText(String.format("%.2f", this.thetotalprice));
        // show this added dish to the showSelectedDish vbox
        showSelectedDishes(addedDishes);
        // update the "foodTable"
        addedDishesShow.clear();
        addedDishesShow.addAll(addedDishes);
        FXCollections.reverse(addedDishesShow);
        foodTable.setItems(addedDishesShow);
    }
    // Define a method to show the current selected dishes to the "selectedDish" vbox
    private void showSelectedDishes(List<Food> foodItems) {
    	selectedDishes.getChildren().clear();//清空
    	int idInCurrentList = 1; // the id of dish in the current selected list. Needed for deleting dish.
    	for (Food food : foodItems) {
    		CheckBox checkbox = new CheckBox(idInCurrentList+": "+food.getName());
    	    //Button button = new Button(idInCurrentList+": "+food.getName());
    	    idInCurrentList++;
    	    selectedDishes.getChildren().add(checkbox);
    	    checkbox.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	dishName.setText(food.getName());
	            	dishID.setText(food.getid()+"");
	            	price.setText(food.getPrice()+"");
	            	Image image = new Image("/images/"+food.getImageName());
	            	dishImage.setImage(image);
	            	dishDescription.setText(food.getDescription());
	            	//dishIngradient.setText(food.getIngradient());//Will update in the future
	            }
	        });
    	}
    }
    // exit elegantly
    @FXML
    void helper_exit(ActionEvent event) {
    	Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	stage.close();

    }
    // remove selected dish with a check box
    @FXML
    void removeDish(ActionEvent event) {
    	// read all the dishes in the selectedDishes vbox
    	List<CheckBox> checkboxes = new ArrayList<CheckBox>(); 
    	for(Node node : selectedDishes.getChildren()) { 
    		if(node instanceof CheckBox) { 
    			checkboxes.add((CheckBox)node); 
    		} 
    	}
    	// find which ones are marked to delete, put them in toDeleteDishes list
    	ArrayList<Food> toDeleteDishes = new ArrayList<Food>();
    	for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).isSelected() == true) {
                	toDeleteDishes.add(addedDishes.get(i));
                }
            }
    	addedDishes.removeAll(toDeleteDishes);
        // refresh the vbox with new foodItems
        showSelectedDishes(addedDishes);
         for(Food food: toDeleteDishes) {
        	thetotalprice=thetotalprice-food.getPrice();
        }
       
        totalPrice.setText(String.format("%.2f", this.thetotalprice));
        addedDishesShow.clear();
        addedDishesShow.addAll(addedDishes);
        FXCollections.reverse(addedDishesShow);
        foodTable.setItems(addedDishesShow);
        
    }
    @FXML // the function of the save button in the Order tab
    void createOrder(ActionEvent event) throws SQLException {
    	
    	List<Food> orderItemsFoods=this.addedDishes;
    	String name = null;
    	double price=this.thetotalprice;
    	//if the textfield "cusName" has no text input, show a pop up window 
    	//saying "Please input the customer name!" 
    	if (cusName.getText().isEmpty()) {
    	    Alert alert = new Alert(Alert.AlertType.WARNING);
    	    alert.setTitle("Warning");
    	    alert.setHeaderText(null);
    	    alert.setContentText("Please input the customer name!");
    	    alert.showAndWait();
    	} else {
    	    name = cusName.getText();
	    	//////// Make food count list /////////////
	        ///////////// The code below is for saving the order to the database ///////////
			/////Given the addedDishes list, this is to get the count of food items to feed the database API generateOrder
			Map<Integer, Integer> dishCount = new HashMap<>(); 
			for (Food food:addedDishes) {
				int id = food.getid();
				dishCount.put(id,dishCount.getOrDefault(id, 0)+1);// a histogram of addedDishes is built as a Map
			}
			//This is to get the num list of food items to feed the database API generateOrder
			int[] dishNameList =  new int[dishCount.size()];
			int[] dishCountList =  new int[dishCount.size()];
			int foodNum = 0;
			for (Map.Entry<Integer, Integer> entry:dishCount.entrySet()) {
				dishNameList[foodNum]=(int)entry.getKey();
				dishCountList[foodNum]=(int)entry.getValue();
				foodNum++;
			}
			// create a new order
			Order order=new Order(name,price,orderItemsFoods); 
			// add this order to Helper accessible order list allOrder
	        allOrder.add(order);
	        // save this order to database
			fOrderController.generateOrder(
					order.getCustomerName(),
					dishNameList, 
					dishCountList, 
					price);
			// Finally, clear out for next order and refresh the page
	        addedDishesShow.clear();
	        FXCollections.reverse(addedDishesShow);
	        foodTable.setItems(addedDishesShow);
			selectedDishes.getChildren().clear();
			cusName.clear();
			totalPrice.clear();
			addedDishes.clear();
			this.thetotalprice=0.0;
			initialize();
    	}
    }
    // refresh the page, read from the database
    @FXML
    void reloadTheDatabase(ActionEvent event) throws SQLException {
    	initialize();
    }
}



