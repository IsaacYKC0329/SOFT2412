package Vending_Machine;

import Vending_Machine.Role.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.Console;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VendingMachine {
    List<Category> categories = new ArrayList<>();
    List<User> users = new ArrayList<>();
    PaymentSystem paymentSystem = new PaymentSystem();
    private HashMap<Product, Integer> cart = new HashMap<>();
    private User currentUser;
    private double startTime;
    private double endTime;

    public VendingMachine() {
        JSONParser parser = new JSONParser();
        try {
            // Read the cash.json file
            Object cashObject = parser.parse(new FileReader("src/main/resources/cash.json"));
            // Convert Object to JSONArray
            JSONArray jsonCashArray = (JSONArray) cashObject;
            for (Object cash : jsonCashArray) {
                JSONObject jsonCash = (JSONObject) cash;
                // Read the cash denomination and quantity
                String denomination = (String) jsonCash.get("denomination");
                Long quantity = (Long) jsonCash.get("quantity");
                paymentSystem.initializeCash(denomination, quantity.intValue());

            }
            // Read the credit_card.json file
            Object cardObject = parser.parse(new FileReader("src/main/resources/credit_cards.json"));
            // Convert Object to JSONArray
            JSONArray jsonCardArray = (JSONArray) cardObject;
            for (Object card : jsonCardArray) {
                JSONObject jsonCard = (JSONObject) card;
                // Read the card name and number
                String name = (String) jsonCard.get("name");
                String number = (String) jsonCard.get("number");
                paymentSystem.initializeCard(name, number);
            }

            // Read the categories.json file
            Object categoryObject = parser.parse(new FileReader("src/main/resources/categories.json"));
            // Convert Object to JSONArray
            JSONArray jsonCategoryArray = (JSONArray) categoryObject;
            for (Object category : jsonCategoryArray) {
                JSONObject jsonCategory = (JSONObject) category;
                // Read the category name
                String name = (String) jsonCategory.get("name");
                // Create a new category by using above configurations
                Category category1 = new Category(name);
                // Read the category products
                JSONArray jsonProductArray = (JSONArray) jsonCategory.get("products");
                for (Object product : jsonProductArray) {
                    JSONObject jsonProduct = (JSONObject) product;
                    // Read the product id, name, price, quantity
                    String id = (String) jsonProduct.get("id");
                    String productName = (String) jsonProduct.get("name");
                    double price = (double) jsonProduct.get("price");
                    Long quantity = (Long) jsonProduct.get("quantity");
                    // Create a new product by using above configurations
                    Product product1 = new Product(id, productName, category1, price, quantity.intValue());
                    // Add the product into its category
                    category1.addProduct(product1);
                }
                // Add the category into the vending machine categories list
                categories.add(category1);
            }
            // Read the users.json file
            Object userObject = parser.parse(new FileReader("src/main/resources/users.json"));
            // Convert Object to JSONArray
            JSONArray jsonUserArray = (JSONArray) userObject;
            for (Object user : jsonUserArray) {
                JSONObject jsonUser = (JSONObject) user;
                // Read the username, password, and role
                String username = (String) jsonUser.get("username");
                String password = (String) jsonUser.get("password");
                String role = (String) jsonUser.get("role");
                // Check which role this user is
                Role role1;
                if (Objects.equals(role, "Customer")) {
                    role1 = new Customer();
                } else if (Objects.equals(role, "Seller")) {
                    role1 = new Seller();
                } else if (Objects.equals(role, "Cashier")) {
                    role1 = new Cashier();
                } else if (Objects.equals(role, "Owner")) {
                    role1 = new Owner();
                } else {
                    // invalid role please check the users.json file
                    throw new RuntimeException("Invalid role");
                }
                // Create a new user by using above configurations
                User user1 = new User(username, password, role1);
                // Add the user into the vending machine users list
                users.add(user1);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<User> getUsers() {
        return users;
    }

    public PaymentSystem getPaymentSystem() {
        return paymentSystem;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void userLogIn() {
        System.out.println();
        System.out.println("This is vending machine!");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println();
            System.out.print("Login or Register? (L/R)");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("L") || input.equalsIgnoreCase("LOGIN")) {
                if (login()) {
                    return;
                }
            } else if (input.equalsIgnoreCase("R") || input.equalsIgnoreCase("REGISTER")) {
                if (register()) {
                    return;
                }
            } else {
                System.out.println("Invalid input! Please choose Login or Register (L/R)");
            }
        }
    }
    // Login Interface
    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your username:");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("EXIT")) {
                return false;
            }
            System.out.print("Please enter your password:");
            String password = scanner.nextLine();
            if (password.equalsIgnoreCase("EXIT")) {
                return false;
            }
            // Check if the username and password are correct
            if (this.checkUserLogin(username, password)) {
                System.out.println("Login successfully!");
                this.currentUser = this.getUserByUsername(username);
                return true;
            } else {
                System.out.println("Incorrect username or password! If you are a new user, please register first.");
            }
        }
    }


    // Register Interface
    public boolean register() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your username:");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("EXIT")) {
                return false;
            }
            // Check if the username is already used
            // If the username is already used, ask the user to enter a new username
            // If the username is not used, ask the user to enter a password
            if (!this.checkUserName(username)) {
                System.out.println("The username has been used! Please try another one.");
            } else {
                System.out.print("Please enter your password:");
                String password = scanner.nextLine();
                if (password.equalsIgnoreCase("EXIT")) {
                    return false;
                }
                // Create a new customer by using above configurations
                createUser(username, password, new Customer());
                System.out.println("Register successfully!");
                this.currentUser = this.getUserByUsername(username);
                return true;
            }
        }
    }
    // Get a user by using username
    private User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    // Check username
    public boolean checkUserName(String username) {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return false;
            }
        }
        return true;
    }
    // Check login
    public boolean checkUserLogin(String username, String password) {
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(), password)) {
                return true;
            }
        }
        return false;
    }
    // Create a user
    public void createUser(String username, String password, Role role) {
        // Create a new user
        User user = new User(username, password, role);
        // Add the new user into the vending machine users list
        users.add(user);
        // Write the new user into the users.json file
        JSONParser parser = new JSONParser();
        try {
            // Read the users.json file
            Object userObject = parser.parse(new FileReader("src/main/resources/users.json"));
            // Convert Object to JSONArray
            JSONArray jsonUserArray = (JSONArray) userObject;
            // create a new user
            JSONObject thisUserObject = new JSONObject();
            // add the username, password, and role to this user json object
            thisUserObject.put("username", user.getUsername());
            thisUserObject.put("password", user.getPassword());
            thisUserObject.put("role", user.getRole().getName());
            // add this user to the users json array
            jsonUserArray.add(thisUserObject);
            // create a new ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // enable pretty print
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            // write the users json array to the users.json file
            mapper.writeValue(new FileWriter("src/main/resources/users.json"), jsonUserArray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Show the menu
    public void showMenu() {
        System.out.println();
        System.out.println("Welcome to the vending machine!");
        System.out.println("");
        System.out.println("This is the main menu, here are all the items we have:");
        for (Category category: this.categories) {
            System.out.print(category.getName() + ": ");
            for (Product product : category.getProducts()) {
                if (product.equals(category.getProducts().get(category.getProducts().size() - 1))) {
                    System.out.println(product.getName() + ".");
                } else {
                    System.out.print(product.getName() + ", ");
                }
            }
        }
        System.out.println("");
        System.out.println("Enter help command to get more information.");
        System.out.println();
    }

    // Find the product with same id and return it
    private Product findProductById(String productId) {
        for (Category category : categories) {
            for (Product product : category.getProducts()) {
                if (Objects.equals(product.getId(), productId)) {
                    return product;
                }
            }
        }
        return null;
    }
    public void transaction() {
        // Transaction start
        System.out.println();
        System.out.println("What do you want to buy?");
        while(true){
            System.out.print("Input your command: ");
            Scanner sc = new Scanner(System.in);
            String buyCommand = sc.nextLine();
            String [] buyParameter = buyCommand.split(" ");
            String buyFirstCommand = buyParameter[0];
            if (buyFirstCommand.equalsIgnoreCase("HELP")) {
                System.out.println();
                System.out.println("ADD <item> <quantity>: add item to the cart");
                System.out.println("REMOVE <item>: remove item from the cart");
                System.out.println("REMOVE <item> <quantity>: remove some item from the cart");
                System.out.println("VIEW: view the items in the cart");
                System.out.println("CLEAR: clear the cart");
                System.out.println("CHECKOUT: checkout");
                System.out.println("CANCEL: cancel the transaction");
                System.out.println("HELP: print this manual");
                System.out.println();
            } else if (buyFirstCommand.equalsIgnoreCase("CANCEL")) {
                // Cancel transaction and show main menu
                this.showMenu();
                return;
            } else if (buyFirstCommand.equalsIgnoreCase("VIEW")) {
                this.viewCart();
            } else if (buyFirstCommand.equalsIgnoreCase("CLEAR")) {
                this.clearCart();
            } else if (buyFirstCommand.equalsIgnoreCase("CHECKOUT")) {
                this.checkoutCart();
            } else if (buyFirstCommand.equalsIgnoreCase("ADD")) {
                if (buyParameter.length == 3) {
                    this.addToCart(buyParameter[1], Integer.parseInt(buyParameter[2]));
                } else {
                    System.out.println("Invalid command, please enter help command to get more information.");
                }
            } else if (buyFirstCommand.equalsIgnoreCase("REMOVE")) {
                if (buyParameter.length == 2) {
                    this.removeAllFromCart(buyParameter[1]);
                } else if (buyParameter.length == 3) {
                    this.removeFromCart(buyParameter[1], Integer.parseInt(buyParameter[2]));
                } else {
                    System.out.println("Invalid command, please enter help command to get more information.");
                }
            } else {
                System.out.println("Invalid command, please enter help command to get more information.");
            }
        }
    }

    // Add the product into the cart
    public void addToCart(String productId, int quantity) {
        // set startTime
        this.startTime = System.currentTimeMillis();
        // Find the product by using its id
        Product product = findProductById(productId);
        // if product is not found
        if (product == null) {
            System.out.println("Product not found");
            return;
        }
        // Check if the product has enough quantity
        if (product.getQuantity() >= quantity) {
            // Add this product into cart
            this.cart.put(product, quantity);
        } else {
            // If the product does not have enough quantity, print the error message
            System.out.println("The product " + product.getName() + " does not have enough quantity");
        }
    }

    // Remove some of this product from the cart
    public void removeFromCart(String productId, int quantity) {
        // set startTime
        this.startTime = System.currentTimeMillis();
        // Check if the cart is empty
        if (this.cart.isEmpty()) {
            System.out.println("The cart is empty");
            return;
        }
        // Find the product by using its id
        Product product = findProductById(productId);
        // Check if the product is in the cart
        if (this.cart.containsKey(product)) {
            // Check if the quantity is less than the quantity in the cart
            if (this.cart.get(product) <= quantity) {
                // Remove the product from the cart
                this.cart.remove(product);
            } else {
                // Update the quantity in the cart
                this.cart.put(product, this.cart.get(product) - quantity);
            }
        } else {
            // If the product is not in the cart, print the error message
            System.out.println("The product " + product.getName() + " is not in the cart");
        }

    }

    // Remove the product from the cart
    public void removeAllFromCart(String productId) {
        // set startTime
        this.startTime = System.currentTimeMillis();
        // Check if the cart is empty
        if (this.cart.isEmpty()) {
            System.out.println("The cart is empty");
            return;
        }
        // Find the product by using its id
        Product product = findProductById(productId);
        // Check if the product is in the cart
        if (this.cart.containsKey(product)) {
            // Remove the product from the cart
            this.cart.remove(product);
        } else {
            // If the product is not in the cart, print the error message
            System.out.println("The product " + product.getName() + " is not in the cart");
        }
    }

    // View the cart
    public void viewCart() {
        //set startTime
        this.startTime = System.currentTimeMillis();
        // Print the cart
        System.out.println("Cart:");
        for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
            System.out.println(entry.getKey().getName() + " " + entry.getValue());
        }
    }
    // Clear the cart
    public void clearCart() {
        // set startTime
        this.startTime = System.currentTimeMillis();
        this.cart.clear();
    }

    // Checkout the cart
    public void checkoutCart() {
        // Check if the cart is empty
        if (this.cart.isEmpty()) {
            System.out.println("Your cart is empty, please add some items into your cart");
            return;
        }
        // Check if the items has enough quantity
        for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
            if (entry.getKey().getQuantity() < entry.getValue()) {
                System.out.println("The product " + entry.getKey().getName() + " does not have enough quantity");
                return;
            }
        }
        // Calculate the total price
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        // Print the total price
        System.out.println("Total price: " + totalPrice);
        // Ask the user to pay
        System.out.println("Please choose payment method: 1. Cash 2. Card");
        // Get the payment method
        Scanner paymentMethodScanner = new Scanner(System.in);
        while (true) {
            String paymentMethod = paymentMethodScanner.nextLine();

            // if user use card
            if (paymentMethod.equalsIgnoreCase("card")) {

                // Ask the user to enter the card name
                System.out.println("Please enter your card name:");
                Scanner cardNameScanner = new Scanner(System.in);
                String cardName = cardNameScanner.nextLine();

                // Ask the user to enter the card number
                System.out.println("Please enter your card number:");
                Console console = System.console();
                char[] passwordCharArray = console.readPassword();
                String cardNumber = new String(passwordCharArray);

                // Check if the card number and password is valid (see issue "save function")********************************
                if (paymentSystem.checkCard(false, cardName, cardNumber)) {

                    // If the card is valid, print the success message
                    System.out.println("Payment success");
                }

                // If the card is not valid, print the error message
                else {
                    System.out.println("Payment failed");
                }
            }

            // if it is cash
            else if (paymentMethod.equalsIgnoreCase("cash")) {

                // Ask the user to enter the cash amount**********************
                System.out.println("Please enter cash:");
                Scanner cashScanner = new Scanner(System.in);
                double cash = cashScanner.nextDouble();
                HashMap<String, Integer> change = paymentSystem.giveChange(cash, totalPrice);

                if(change.containsKey("paymentAccept")){

                    // If the payment is accepted, and no change
                    if(change.get("paymentAccept")==1){
                        System.out.println("Payment success. no change.");
                    }

                    // if the payment is cancelled
                    else if (change.get("paymentAccept")==0){
                        System.out.println("Payment cancelled.");
                    }
                }

                // if there are changes
                else{
                    System.out.println("Payment success. Change:");
                    for (Map.Entry<String, Integer> entry : change.entrySet()) {
                        System.out.println(entry.getKey() + " " + entry.getValue());
                    }
                }
                System.out.println();
            }

            // If the payment method is not valid, print the error message
            else {
                System.out.println("Invalid payment method");
            }
            for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
                entry.getKey().setQuantity(entry.getKey().getQuantity() - entry.getValue());
                // If the quantity is 0, remove the product from the product list
                if (entry.getKey().getQuantity() <= 0) {
                    for (Category category : this.categories) {
                        for (Product product : category.getProducts()) {
                            if (product.getId().equals(entry.getKey().getId())) {
                                category.getProducts().remove(product);
                                break;
                            }
                        }
                    }
                }
            }
            // Update the quantity of the products in the categories.json file
            updateProductQuantityJson();
            // Clear the cart
            this.clearCart();
        }

    }
    // Update the quantity of the products in the categories.json file
    public void updateProductQuantityJson() {
        JSONParser parser = new JSONParser();
        try {
            // Read the categories.json file
            Object categoryObject = parser.parse(new FileReader("src/main/resources/categories.json"));
            // Convert Object to JSONArray
            JSONArray jsonCategoryArray = (JSONArray) categoryObject;
            for (Object c : jsonCategoryArray) {
                JSONObject jsonCategory = (JSONObject) c;
                // Read the category name
                String name = (String) jsonCategory.get("name");
                for (Category category : this.categories) {
                    if (category.getName().equals(name)) {
                        // Read the category products
                        JSONArray jsonProductArray = (JSONArray) jsonCategory.get("products");
                        for (Object p : jsonProductArray) {
                            JSONObject jsonProduct = (JSONObject) p;
                            // Read the product id
                            String id = (String) jsonProduct.get("id");
                            for (Product product : category.getProducts()) {
                                if (product.getId().equals(id) ) {
                                    Long quantity = (Long) jsonProduct.get("quantity");
                                    // Only update the quantity if the quantity in json file is different
                                    if (product.getQuantity() != quantity.intValue()) {
                                        // Update the quantity in jsonObject
                                        jsonProduct.put("quantity", product.getQuantity());
                                    }

                                }
                            }
                        }
                    }
                }
            }
            // create a new ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // enable pretty print
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            // write the categories json array to the categories.json file
            mapper.writeValue(new FileWriter("src/main/resources/categories.json"), jsonCategoryArray);
        } catch(IOException | ParseException e){
            e.printStackTrace();
        }
    }
}
