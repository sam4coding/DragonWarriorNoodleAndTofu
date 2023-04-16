package view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import application.Main;
import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.text.Text;

import application.Main;
import controller.FoodController;
import controller.OrderController;
import controller.RestaurantController;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Food;
import model.Helper;
import model.Order;
import tools.Dbinitializer;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.property.SimpleStringProperty;

public class OwnerController {
    // those on the top button bar
	@FXML
	private Button Get_an_order;// get a default order, this is for testing
    @FXML
    private Button Refresh_the_page;// refresh the page to see whether there is new order coming in
	@FXML
	private Button Owner_exit;
	// Widgets on the Tab "Orders"
	@FXML
	private TableView<Order> ordersTableView; // Point to the whole table, and below are each colomn
	@FXML
	private TableColumn<Order,String> Order_ID;
    @FXML
    private TableColumn<Order, String> Updated_time;
	@FXML
	private TableColumn<Order, String> Customer_Name;
	@FXML
	private TableColumn<Order, String> Order_Dishes;
    @FXML
    private TableColumn<Order, String> Servings;
	@FXML
	private TableColumn<Order, String> Status;
	// Widgets on the Tab "Dishes"
    @FXML
    private GridPane dishGallery; // showing the image and name buttons of the dish
    @FXML
    private TextField dishName; // showing the name of the selected dish
    @FXML
    private TextField price;// showing the price of the selected dish
    @FXML
    private ImageView dishImage;// showing the image of the selected dish
    @FXML
    private TextArea dishDescription;// showing the description of the selected dish
    @FXML
    private Button addDish;// add a new dish to the menu. Only when the dish name is not in the current list, this button is activated
    @FXML
    private Button removeDish;// removed a new dish from the menu. Only when the dish name is in the current list, this button is activated
    @FXML
    private Button editDish;// edit a dish in menu. Only when the dish name is in the current list, and one field is different, this button is activated
    
    // those on the Finance tab
    @FXML
    private TextField TaxRate; //Show the current Tax rate
    @FXML
    private TextField TotalOrders; // Show the current total order numbers
    @FXML
    private TextField TurnOver;// show the total income
    @FXML
    private TextField MaterialCost;// show the total cost of materials
    @FXML
    private TextField NetProfit;// show the net profits
    @FXML
    private TextField SalaryCost;// show the salary cost
    // those on the helpers tab
    @FXML
    private TableView<Helper> HelpersTableView;// table to show available helpers. Only Po for Mr. Ping
    @FXML
    private TableColumn<Helper, Integer> helperIDTableColumn;// id column
    @FXML
    private TableColumn<Helper, String> helperNameTableColumn;//name column
    
    

	
	// These 2 list are for receiving orderList in order to populate the order TableView
	private ObservableList<Order> orderShowList = FXCollections.observableArrayList();// this is for showing only

	private List<Order> orderList = new ArrayList<Order>(); // this is for operating
	
	// Load in the database controller for connection
	//private FoodController fController = new FoodController();
	private OrderController fOrderController = new OrderController();
	// popularizing the table view with existing orders upon initializing


	public void initialize() throws SQLException {
		
		
		
		/***************************Populating the order tab page, order management**********************************************/
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
		// 6th column, the status of the order, need to toggle such as 0:"received"-> 1:"preparing" -> 2:"ready", 
		Status.setCellFactory(column -> new TableCell<Order, String>() {//Fill the column with status
			private final Button button = new Button(); // on buttons
			@Override
	        protected void updateItem(String item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	            } else {
	                Order order = getTableView().getItems().get(getIndex());
	                button.setText(order.getStatusString(order.getStatus()));
	                int status = order.getStatus();
	                if (status==2 || status==3) {// Owner only dear with 0:"received"-> 1:"preparing" -> 2:"ready", 
	                	button.setDisable(true);
	                }
	                button.setOnAction(e -> {
	                	int new_status = order.getStatus();
			    		if (new_status==0 || new_status==1) {
			    			order.setStatus(++new_status);
			    			if(new_status==2) {
			    				button.setDisable(true);// Owner only dear with 0:"received"-> 1:"preparing" -> 2:"ready", 
			    			}
			    			try {
								fOrderController.updateOrderStatus(order.getOrderID(), new_status);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
			    		}
	                    button.setText(order.getStatusString(new_status));
	                });
	                setGraphic(button);
	            }
	        }
		});
		orderList = fOrderController.getOrders();
	    orderShowList.addAll(orderList);
	    FXCollections.reverse(orderShowList);
	    ordersTableView.setItems(orderShowList);
	    
	    /***************************Populating the dishes tab page, showing the menu **********************************************/
	    // Clean up the gridPane dishGallery first, lest there is any remaining parts on it from before
	    Node node = dishGallery.getChildren().get(0);
	    dishGallery.getChildren().clear();
	    dishGallery.getChildren().add(0,node);
	    // get the foodList
		List<Food> foodList = new ArrayList<>();
		FoodController fController = new FoodController();
		foodList = fController.getFood();
		int columnIndex=0, rowIndex=0;
		//System.out.println("There are "+foodList.size() + " items in the Dish menu" );
	    for (Food food : foodList) {
	    	// add the Dish image to dishGallery
	    	Image image = new Image("/images/"+food.getImageName());
	    	ImageView imageView = new ImageView(image);
	    	// add a button at the bottom of the cell with dish name
	    	Button button = new Button(food.getName());
	    	button.setWrapText(true);
	    	button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
	        StackPane stackPane = new StackPane();
	        //borderPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	        // place the image and the button on the stackPane
	        stackPane.setMaxSize(100, 110);
	        stackPane.getChildren().addAll(imageView, button);
	        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
	        StackPane.setAlignment(imageView, Pos.CENTER);
	        imageView.fitWidthProperty().bind(stackPane.widthProperty());
	        imageView.fitHeightProperty().bind(stackPane.heightProperty());
	        // set the function of the button,upon click, it will populate the textFields
	        button.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	dishName.setText(food.getName());
	            	price.setText(food.getPrice()+"");
	            	dishImage.setImage(image);
	            	dishDescription.setText(food.getDescription());
	            }
	        });
	    	imageView.setPreserveRatio(true);
	        dishGallery.add(stackPane, columnIndex, rowIndex);
	        // update columnIndex and rowIndex for next image
	        if (columnIndex>=5) {
	        	columnIndex=0;
	        	rowIndex++;
	        } else {
	        	columnIndex++;
	        }
	    }
	    /****Setting the menu page buttons' availability******/
	    //This is done by adding textfield listeners
	    dishName.textProperty().addListener((observable, oldValue, newValue) -> {
	    	checkAndUpdateButton();
	    });

	    dishDescription.textProperty().addListener((observable, oldValue, newValue) -> {
	    	checkAndUpdateButton();
	    });

	    price.textProperty().addListener((observable, oldValue, newValue) -> {
	    	checkAndUpdateButton();
	    });

	    dishImage.imageProperty().addListener((observable, oldValue, newValue) -> {
	    	checkAndUpdateButton();
	    });
	    /***************************Populating the helper tab page, employee management**********************************************/
	    // clear out the table order TableView for populating
		// set up temporal helper list
		List<Helper> helpersList = new ArrayList<Helper>();
		helpersList.add(new Helper(1,"Po"));
	 	HelpersTableView.getItems().clear();
 		// 1st column, the index of the helpers. 
	 	helperIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
 		// 2nd column, the name of the helpers. 
	 	helperNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
	 	HelpersTableView.getColumns().clear();
	    HelpersTableView.getColumns().addAll(helperIDTableColumn, helperNameTableColumn);
	    HelpersTableView.setItems(FXCollections.observableArrayList(helpersList));
	    /***************************Fill up the finance tab page, restaurant general management***************************************/
	    RestaurantController restaurantController = new RestaurantController();
	    Double taxRate=0.01; //1% tax rate
	    Integer totalOrders=fOrderController.getOrders().size();//
	    int year = 2023;
	    double turnover = restaurantController.getSales(year);// The total sale of the given year
	    double tax = turnover*0.01; //tax is 1 percent of the sale
	    double salary = turnover*0.1;//Helper salary is 10 percent of the sale
	    double materialCost = turnover*0.5;//material cost is half of the sale
	    double netProfit = turnover-tax-salary-materialCost;
	    TaxRate.setText(String.format("%.2f",taxRate));
	    TotalOrders.setText(String.format("%8d",totalOrders));
	    TurnOver.setText(String.format("%.2f",turnover));
	    MaterialCost.setText(String.format("%.2f",materialCost));
	    SalaryCost.setText(String.format("%.2f",salary));
	    NetProfit.setText(String.format("%.2f",netProfit));

	}

	

	
	// Event Listener on Button[#Get_an_order].onAction
	@FXML
	public void get_an_order(ActionEvent event) throws SQLException {
		// generate a default order
		Order order = new Order();
		// add it to orderList
		orderList.add(order);
		// save it in the database

		
		int foodNum = order.getOrderItemsFoods().size();
		int[] defaultMeal =  new int[foodNum];
		int[] defaultNum =  new int[foodNum];
		double price = order.calculatePrice();;
		for (int i=0; i<foodNum; i++) {
			defaultMeal[i]=order.getOrderItemsFoods().get(i).getid();
			defaultNum[i] =order.getOrderItemsNums().get(i);
			System.out.println("dish ID:"+defaultMeal[i]+ " , dish num: "+defaultNum[i]);
		}
		fOrderController.generateOrder(
				order.getCustomerName(),
				defaultMeal, 
				defaultNum, 
				price);
		initialize();
	}
	// Event Listener on Button[#Call_helper].onAction
	@FXML
	public void call_helper(ActionEvent event) {
	}
	
    @FXML
    void setImage(MouseEvent event) {//Allowing ImageView on the Dish page read in new image file and display
    	    FileChooser fileChooser = new FileChooser();
    	    fileChooser.setInitialDirectory(new File("D:\\library\\JC\\NEU\\Semester_1\\20221210ObjectOrientedDesign\\FinalProject\\OrderingSystemApp\\src\\images"));
    	    fileChooser.setTitle("Select Image File");
    	    fileChooser.getExtensionFilters().addAll(
    	            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif","*.jpeg"));
    	    File selectedFile = fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow());
    	    if (selectedFile != null) {
    	        // handle selected file
    	        String url;
				try {
					url = selectedFile.toURI().toURL().toString();
					Image image = new Image(url);
	    	    	dishImage.setImage(image);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
    	  }
    }
	// Event Listener on Button[#Owner_exit].onAction
	@FXML	
	public void owner_exit(ActionEvent event) {
	   	Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    	stage.close();
	}
	// Event Listener on Button[#addDish].onAction
	@FXML
	public void addDish(ActionEvent event) throws SQLException {
		// Step 1 connect to database, get food data
		List<Food> foodList = new ArrayList<>();
		FoodController fController = new FoodController();
		foodList = fController.getFood();
		// Step 2 check whether the current dishName is already in the list
		boolean found = false;
		for (Food food:foodList) {
			if (food.getName().equals(dishName.getText())) {
				found = true;
				break;
			} 
		}
		// Step 3 if not in the current food list, add the food
		if (found==false) {
			// get image Name of the dish from dishImage textField
			String[] parts = dishImage.getImage().getUrl().split("/");
			String fileName = parts[parts.length - 1];
			//System.out.println(fileName);
			// created a new food object
			Food food = new Food(foodList.size()+1, 
					dishName.getText(), 
					dishDescription.getText(), 
					fileName,  //!! Need to add a test on whether there is a image loaded to dishImage ImageView
					Double.parseDouble(price.getText()));
			// add the food object to the database
			fController.addFood(food);
		}
		// Step 4 Refresh the page to show new food 
		initialize();

				
	}
	// Event Listener on Button[#removeDish].onAction
	@FXML
	public void removeDish(ActionEvent event) throws SQLException {

		// Step 1 connect to database, get food data
		Dbinitializer.initDatabase(); //Create an instance of database class 
		List<Food> foodList = new ArrayList<>();
		FoodController fController = new FoodController();
		foodList = fController.getFood();
		// Step 2 check whether the current dishName is already in the list
		int foodID=1;
		for (Food food:foodList) {
			if (food.getName().equals(dishName.getText())) {
		// Step 3 if in the current food list, remove the food
				int[] foodIDs = {foodID};
				fController.deleteFood(foodIDs);
				break;
			} 
			foodID++;
		}
		// Step 4 Refresh the page to show new food 
		initialize();
		
	}
	// Event Listener on Button[#editDish].onAction
	@FXML
	public void editDish(ActionEvent event) throws SQLException {
		// Edit Dish is available when the current dishName field has an existing 
		// Step 1 read from the text boxes
        String name = dishName.getText();
        String description = dishDescription.getText();
        double priceValue = Double.parseDouble(price.getText());
    	String[] parts = dishImage.getImage().getUrl().split("/");
    	String imageName = parts[parts.length - 1];
		
		// Step 2 connect to database, get food by name
		
		Dbinitializer.initDatabase(); //Create an instance of database class 
		FoodController fController = new FoodController();
		Food food = fController.getFoodbyName(name);
		
		// Step 3 update food property
		food.setDescription(description);
		food.setImageName(imageName);
		food.setPrice(priceValue);
		
		// Step 4 save change to data base
		fController.editFood(food);
		
		// Step 5 Refresh the page to show new food 
		initialize();
	}
	
	
	/***************************************************Button Availability Checking Methods*******************************************************/	
    
	
    private void checkAndUpdateButton() {
    	// method to check whether editDish button shall be disabled
    	
        String name = dishName.getText();
        String description = dishDescription.getText();
        double priceValue = Double.parseDouble(price.getText());
    	String[] parts = dishImage.getImage().getUrl().split("/");
    	String imageName = parts[parts.length - 1];
               
        List<Food> foodList = new ArrayList<>();
		FoodController fController = new FoodController();
		foodList = fController.getFood();
		Food foodFound = new Food();
		
		// condition for editable:
		// 1. dishName, dishDescription, price and dishImage all occupied
		boolean allFilled = false;
		if (!(name.isEmpty())
				&& !(description.isEmpty()) 
				&& !(imageName.isEmpty())
				&& !(price.getText().isEmpty())
				) {
			allFilled = true;
		}
		// 2. dishName exists
		boolean nameExist = false;
		for (Food food : foodList) {
            if (food.getName().equals(name)) {
            	nameExist = true;
            	foodFound = food;
                break;
            }
        }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
		// 3. dishDescription, price and dishImage at least one is different from existing value
		boolean oneDifference = false;
		if (!(foodFound.getDescription().equals(description))
        || !(foodFound.getPrice() == priceValue) 
        || !(foodFound.getImageName().equals(imageName))) {
			oneDifference = true;
		}
        // Now combine 1, 2, 3 
		// If 1, 2, 3 all true, editable
		if (allFilled == true 
				&& nameExist == true 
				&& oneDifference == true) {  
				editDish.setDisable(false);
			} else {
				editDish.setDisable(true);
			}
		// If 1 true, 2 false, addable
		if (allFilled == true 
				&& nameExist == false) {  
				addDish.setDisable(false);
			} else {
				addDish.setDisable(true);
			}
		// If 1, 2 true, 3 false deletable
		if (allFilled == true 
				&& nameExist == true 
				&& oneDifference == false) {  
				removeDish.setDisable(false);
			} else {
				removeDish.setDisable(true);
			}
	}
    
    @FXML
    void Refresh_the_page(ActionEvent event) throws SQLException {
    	initialize();
    }        		
	
}
