package Vending_Machine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class VendingMachine {

    public User User;
    private List<Category> categories = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private PaymentSystem paymentSystem = new PaymentSystem();
    private HashMap<Product, Integer> cart = new HashMap<>();
    private User currentUser;

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
                    Long quantitySold = (Long) jsonProduct.get("quantitySold");
                    // Create a new product by using above configurations
                    Product product1 = new Product(id, productName, category1, price, quantity.intValue(), quantitySold.intValue());
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
                // Create a new user by using above configurations
                User user1 = new User(username, password, role);
                // Add the user into the vending machine users list
                users.add(user1);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }



    public User getCurrentUser() {
        return currentUser;
    }

    @ExcludeFromJacocoGeneratedReport
    public void userLogIn() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println();
            System.out.print("Login or Register or login as a guest? (L/R/G)");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("L") || input.equalsIgnoreCase("LOGIN")) {
                if (login()) {
                    return;
                }
            } else if (input.equalsIgnoreCase("R") || input.equalsIgnoreCase("REGISTER")) {
                if (register()) {
                    return;
                }
            } else if (input.equalsIgnoreCase("G") || input.equalsIgnoreCase("GUEST")) {
                currentUser = new User("Guest", "Guest", "Guest");
                return;
            } else {
                System.out.println("Invalid input! Please try again.");
            }
        }
    }

    // Login Interface
    @ExcludeFromJacocoGeneratedReport
    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter your username:");
            String username = scanner.nextLine();
            if (username.equalsIgnoreCase("EXIT")) {
                return false;
            }
            System.out.print("Please enter your password:");
            String password = hidingEnter("Password");
            if (password.equalsIgnoreCase("EXIT")) {
                return false;
            }
            // Check if the username and password are correct
            if (this.checkUserLogin(username, password)) {
                System.out.println("Login successfully!");
                this.currentUser = this.getUserByUsername(username);
                this.currentUser.readLastBought5FromFile();
                return true;
            } else {
                if (!checkUserName(username)) {
                    System.out.println("Wrong password, please try again!");
                } else {
                    System.out.println();
                    System.out.println("Incorrect username or password! If you are a new user, please register first.");
                    System.out.println();
                    System.out.println("1. Register");
                    System.out.println("2. Login as a guest");
                    System.out.println();
                    System.out.print("Input your command: ");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("1") || input.equalsIgnoreCase("REGISTER")) {
                        if (register()) {
                            return true;
                        }
                    } else if (input.equalsIgnoreCase("2") || input.equalsIgnoreCase("LOGIN AS A GUEST")) {
                        // Login as a guest
                        this.currentUser = new User("Guest", "Guest", "Customer");
                        this.currentUser.readLastBought5FromFile();
                        System.out.println("Login as a guest successfully!");

                        return true;
                    }
                }
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
                String password = hidingEnter("Password");
                if (password.equalsIgnoreCase("EXIT")) {
                    return false;
                }
                // Create a new customer by using above configurations
                createUser(username, password, "Customer");
                System.out.println("Register successfully!");
                this.currentUser = this.getUserByUsername(username);
                this.currentUser.readLastBought5FromFile();
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
    public void createUser(String username, String password, String role) {
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
            thisUserObject.put("role", user.getRole());
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
    @ExcludeFromJacocoGeneratedReport
    public void showMenu() {
        System.out.println();
        System.out.println("Welcome to the vending machine!");
        System.out.println("");
        System.out.println("This is the main menu, here are all the items we have:");
        for (Category category: this.categories) {
            System.out.print(category.getName() + ": ");
            for (Product product : category.getProducts()) {
                if (product.equals(category.getProducts().get(category.getProducts().size() - 1))) {
                    System.out.println(product.getName() + " " + product.getQuantity() + " " + product.getPrice() + ".");
                } else {
                    System.out.print(product.getName() + " " + product.getQuantity() + " " + product.getPrice() + ", ");
                }
            }
        }
        System.out.println();
        if (currentUser != null) {
            if (currentUser.getLastBought5() != null) {
                if(currentUser.getLastBought5().size() != 0) {
                    System.out.print("Last 5 bought items: ");
                    for (String productName : this.currentUser.getLastBought5()){
                        if (productName.equals(this.currentUser.getLastBought5().get(this.currentUser.getLastBought5().size() - 1))) {
                            System.out.println(productName + ".");
                        } else {
                            System.out.print(productName + ", ");
                        }
                    }
                }
            }
        }
        System.out.println();
        System.out.println("Enter help command to get more information.");
        System.out.println();
    }

    // Find the product with same id and return it
    @ExcludeFromJacocoGeneratedReport
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

    // Find the product with same name and return it
    private Product findProductByName(String productName) {
        for (Category category : categories) {
            for (Product product : category.getProducts()) {
                if (Objects.equals(product.getName(), productName)) {
                    return product;
                }
            }
        }
        return null;
    }
    @ExcludeFromJacocoGeneratedReport
    public void transaction() {
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
                System.out.println("REMOVE <item> <quantity>: remove some of this item from the cart");
                System.out.println("DELETE <item>: delete this item from the cart");
                System.out.println("VIEW: view the items in the cart");
                System.out.println("CLEAR: clear the cart");
                System.out.println("CHECKOUT: checkout");
                System.out.println("CANCEL: cancel the transaction");
                System.out.println("HELP: print this manual");
                System.out.println();
            } else if (buyFirstCommand.equalsIgnoreCase("CANCEL")) {
                // Cancel transaction and show main menu
                saveCancelledTransaction("user cancelled");
                this.showMenu();
                return;
            } else if (buyFirstCommand.equalsIgnoreCase("VIEW")) {
                this.viewCart();
            } else if (buyFirstCommand.equalsIgnoreCase("CLEAR")) {
                this.clearCart();
            } else if (buyFirstCommand.equalsIgnoreCase("CHECKOUT")) {
                if (this.checkoutCart()) {
                    this.showMenu();
                    return;
                }
            } else if (buyFirstCommand.equalsIgnoreCase("ADD")) {
                // Read the second to second last parameter as the product name
                StringBuilder productName = new StringBuilder();
                int buyQuantity = 0;
                for (int i = 1; i < buyParameter.length - 1; i++) {
                    productName.append(buyParameter[i]).append(" ");
                }
                // Read the last parameter as quantity
                buyQuantity = Integer.parseInt(buyParameter[buyParameter.length - 1]);
                this.addToCart(productName.toString().trim(), buyQuantity);
            } else if (buyFirstCommand.equalsIgnoreCase("REMOVE")) {
                // Read the second to second last parameter as the product name
                StringBuilder productName = new StringBuilder();
                int buyQuantity = 0;
                for (int i = 1; i < buyParameter.length - 1; i++) {
                    productName.append(buyParameter[i]).append(" ");
                }
                // Read the last parameter as quantity
                buyQuantity = Integer.parseInt(buyParameter[buyParameter.length - 1]);
                this.removeFromCart(productName.toString().trim(), buyQuantity);
            } else if (buyFirstCommand.equalsIgnoreCase("DELETE")) {
                // Read the second to second last parameter as the product name
                StringBuilder productName = new StringBuilder();
                for (int i = 1; i < buyParameter.length - 1; i++) {
                    productName.append(buyParameter[i]).append(" ");
                }
                this.deleteFromCart(productName.toString().trim());
            } else {
                System.out.println("Invalid command, please enter help command to get more information.");
            }
        }
    }

    // Add the product into the cart
    public void addToCart(String productName, int quantity) {
        // Find the product by using its id
        Product product = findProductByName(productName);
        // if product is not found
        if (product == null) {
            System.out.println("Product not found");
            return;
        }
        // Check if the product has enough quantity
        if (product.getQuantity() >= quantity) {
            // Add this product into cart
            this.cart.put(product, quantity);
            System.out.println("Added " + quantity + " " + product.getName() + " to the cart.");
        } else {
            // If the product does not have enough quantity, print the error message
            System.out.println("The product " + product.getName() + " does not have enough quantity");
        }
    }

    // Remove some of this item from the cart
    @ExcludeFromJacocoGeneratedReport
    public void removeFromCart(String productName, int quantity) {
        // Check if the cart is empty
        if (this.cart.isEmpty()) {
            System.out.println("The cart is empty");
            return;
        }
        // Find the product by using its id
        Product product = findProductByName(productName);
        // Check if the product is in the cart
        if (this.cart.containsKey(product)) {
            // Check if the quantity is less than the quantity in the cart
            if (this.cart.get(product) <= quantity) {
                // Remove the product from the cart
                this.cart.remove(product);
                System.out.println("Removed All " + productName + " from the cart.");
            } else {
                // Update the quantity in the cart
                this.cart.put(product, this.cart.get(product) - quantity);
                System.out.println("Removed " + quantity + " " + productName + " from the cart.");
            }
        } else {
            // If the product is not in the cart, print the error message
            System.out.println("The product " + productName + " is not in the cart");
        }

    }

    // Delete this item from the cart
    public void deleteFromCart(String productName) {
        // Check if the cart is empty
        if (this.cart.isEmpty()) {
            System.out.println("The cart is empty");
            return;
        }
        // Find the product by using its id
        Product product = findProductByName(productName);
        // Check if the product is in the cart
        if (this.cart.containsKey(product)) {
            // Remove the product from the cart
            this.cart.remove(product);
            System.out.println("Deleted " + productName + " from the cart");
        } else {
            // If the product is not in the cart, print the error message
            System.out.println("The product " + productName + " is not in the cart");
        }
    }

    // View the cart
    @ExcludeFromJacocoGeneratedReport
    public void viewCart() {
        // Print the cart
        System.out.println("Cart:");
        for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
            System.out.println(entry.getKey().getName() + " " + entry.getValue());
        }
    }
    // Clear the cart
    public void clearCart() {
        this.cart.clear();
    }

    // Checkout the cart
    @ExcludeFromJacocoGeneratedReport
    public boolean checkoutCart() {

        boolean paymentSuccess = false;

        // Check if the cart is empty
        if (this.cart.isEmpty()) {
            System.out.println("Your cart is empty, please add some items into your cart");
            return false;
        }
        // Check if the items has enough quantity
        for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
            if (entry.getKey().getQuantity() < entry.getValue()) {
                System.out.println("The product " + entry.getKey().getName() + " does not have enough quantity");
                return false;
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
        System.out.print("Please choose payment method: 1. Cash 2. Card: ");
        // Get the payment method
        Scanner paymentMethodScanner = new Scanner(System.in);
        while (true) {
            String paymentMethod = paymentMethodScanner.nextLine();
            HashMap<String, Integer> change = new HashMap<>();
            // if user use card
            if (paymentMethod.equalsIgnoreCase("card")) {
                // user saved the card
                if (cardSaved()) {
                    System.out.println("Payment success.");
                    paymentSuccess = true;
                }

                // user do not save the card
                else {
                    // Ask the user to enter the card name
                    System.out.print("Please enter your card name: ");
                    Scanner cardNameScanner = new Scanner(System.in);
                    String cardName = cardNameScanner.nextLine();

                    // Ask the user to enter the card number
                    System.out.println("Please enter your card number in the popped up windows. ");
                    String cardNumber = hidingEnter("Card Number");

                    // Check if the card number and password is valid (see issue "save function")
                    if (paymentSystem.checkCard(cardName, cardNumber)) {

                        // If the card is valid, print the success message
                        System.out.println("Payment success.");
                        paymentSuccess = true;

                        // ask user if they want to save the account
                        while (true) {
                            System.out.print("Do you want to save this card? (Y/N): ");
                            Scanner saveCardScanner = new Scanner(System.in);
                            String saveCard = saveCardScanner.next();
                            if (saveCard.equalsIgnoreCase("Y")) {
                                // save the card
                                writeData(saveCard);
                                break;
                            } else if (saveCard.equalsIgnoreCase("N")) {
                                break;
                            } else {
                                System.out.println("Invalid input, please enter Y or N");
                            }
                        }
                    }

                    // If the card is not valid, print the error message
                    else {
                        System.out.println("card do not exist");
                    }
                }
            }
            // if it is cash
            else if (paymentMethod.equalsIgnoreCase("cash")) {

                // Ask the user to enter the cash amount**********************
                System.out.print("Please enter cash: ");
                Scanner cashScanner = new Scanner(System.in);
                double cash = cashScanner.nextDouble();
                change = paymentSystem.giveChange(cash, totalPrice);

                if (change.containsKey("paymentAccept")) {

                    // If the payment is accepted, and no change
                    if (change.get("paymentAccept") == 1) {
                        System.out.println("Payment success.. no change.");
                        paymentSuccess = true;
                    }

                    // if the payment is cancelled
                    else if (change.get("paymentAccept") == 0) {
                        System.out.println("Payment cancelled.");
                    }
                }

                // if there are changes
                else {
                    System.out.println("Payment success.. Change:");
                    paymentSuccess = true;
                    for (Map.Entry<String, Integer> entry : change.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                }
                System.out.println();
            }

            // If the payment method is not valid, print the error message
            else {
                System.out.print("Invalid payment method, input card or cash: ");
            }

            if (paymentSuccess) {

                // update the quantity of the products
                for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
                    // add it to the user history list
                    currentUser.addLastBought5(entry.getKey().getName());
                    // update the quantity sold of the product
                    entry.getKey().setQuantitySold(entry.getValue());
                    // update the quantity of the product
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
                updateCategoriesJson();
                saveTransaction(totalPrice, paymentMethod, change);
                // Clear the cart
                this.clearCart();
                return true;
            }
        }
    }

    // save the transaction history to a .txt file
    @ExcludeFromJacocoGeneratedReport
    public void saveTransaction(double totalPrice, String paymentMethod, HashMap<String, Integer> change) {
        // read system date and current time
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        // get all the products with quantity in the cart
        StringBuilder products = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
            products.append(entry.getKey().getName()).append(":").append(entry.getValue()).append(" ");
        }
        // write to transactionHistory.txt file
        try {
            File file = new File("transactionHistory.txt");
            // if no such a txt file, create a new transactionHistory.txt
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (paymentMethod.equalsIgnoreCase("cash")) {
                // get the change
                StringBuilder changes = new StringBuilder();
                for (Map.Entry<String, Integer> entry : change.entrySet()) {
                    changes.append(entry.getKey()).append(":").append(entry.getValue()).append(" ");
                }
                // write to the file
                bufferedWriter.write("Date:" + date + "  Time:" + time + "  Items: " + products + "  Total Price:" + totalPrice + "  Payment:" + paymentMethod + " " + changes);
            } else {
                bufferedWriter.write("Date:" + date + "  Time:" + time + "  Items: " + products + "  Total Price:" + totalPrice + "  Payment:" + paymentMethod);
            }
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // save cancelled transaction history to a .txt file
    @ExcludeFromJacocoGeneratedReport
    public void saveCancelledTransaction(String reason) {
        // read system date and current time
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        // get the username
        String username = currentUser.getUsername();
        // write to cancelledTransactionHistory.txt file
        try {
            File file = new File("cancelledTransactionHistory.txt");
            // if no such a txt file, create a new cancelledTransactionHistory.txt
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("Date:" + date + "  Time:" + time + "  Username:" + username + "  Reason:" + reason);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // provide a windows for user to enter password
    public String hidingEnter(String message) {
        JPasswordField passwordField;
        JFrame frame = new JFrame();
        frame.setVisible(true);
        passwordField = new JPasswordField();

        JDialog dialog = new JDialog(frame,true);

        JButton button = new JButton("enter");
        button.addActionListener(e -> frame.dispose());

        dialog.getContentPane().add(new JLabel(message),BorderLayout.NORTH);
        dialog.getContentPane().add(passwordField);
        dialog.getContentPane().add(button, BorderLayout.SOUTH);
        dialog.pack();

        // set the frame and dialog in the middle of the screen
        frame.setLocationRelativeTo(null);
        dialog.setLocationRelativeTo(null);

        // close the frame after the dialog is closed
        dialog.setVisible(true);
        frame.setVisible(false);
        return ((JTextField)passwordField).getText();
    }

    // write the data into txt file
    @ExcludeFromJacocoGeneratedReport
    public void writeData(String save) {
        // write the data into txt file
        try {
            FileWriter fileWriter = new FileWriter("src/main/resources/save_data.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            // write the username in to txt file
            bufferedWriter.write(this.currentUser.getUsername() + " " + save +" \n");
            System.out.println("card saved.");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // check if the user has saved the card
    @ExcludeFromJacocoGeneratedReport
    public boolean cardSaved() {
        // read the data from txt file until the end of the file
        try {
            FileReader fileReader = new FileReader("src/main/resources/save_data.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                // read the username from txt file
                String[] data = line.split(" ");
                // if the username is the same as the username in txt file
                if (data[0].equals(this.currentUser.getUsername())) {
                    String saveCard = data[1];
                    // check if the username and password is correct
                    if (saveCard.equals("Y")) {
                        bufferedReader.close();
                        return true;
                    }
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update the whole categories.json file
    public void updateCategoriesJson() {
        JSONParser parser = new JSONParser();
        try {
            // Create a JSONArray to store all the categories
            JSONArray jsonCategoryArray = new JSONArray();
            // convert the categories to JSONArray
            for (Category category : this.categories) {
                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put("name", category.getName());
                // Create a JSONArray to store all the products
                JSONArray jsonProductArray = new JSONArray();
                // convert the products to JSONArray
                for (Product product : category.getProducts()) {
                    JSONObject jsonProduct = new JSONObject();
                    jsonProduct.put("id", product.getId());
                    jsonProduct.put("name", product.getName());
                    jsonProduct.put("price", product.getPrice());
                    jsonProduct.put("quantity", product.getQuantity());
                    jsonProduct.put("quantitySold", product.getQuantitySold());
                    jsonProductArray.add(jsonProduct);
                }
                jsonCategory.put("products", jsonProductArray);
                jsonCategoryArray.add(jsonCategory);
            }
            // create a new ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // enable pretty print
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            // write the categories json array to the categories.json file
            mapper.writeValue(new FileWriter("src/main/resources/categories.json"), jsonCategoryArray);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Update the whole cash.json file
    @ExcludeFromJacocoGeneratedReport
    private void updateCashJson() {
        JSONParser parser = new JSONParser();
        try {
            // Create a JSONArray to store all the cash
            JSONArray jsonCashArray = new JSONArray();
            // convert the cash to JSONArray
            for (String denomination : this.paymentSystem.getCashInMachine().keySet()) {
                JSONObject jsonCash = new JSONObject();
                jsonCash.put("denomination", denomination);
                jsonCash.put("quantity", this.paymentSystem.getCashInMachine().get(denomination));
                jsonCashArray.add(jsonCash);
            }
            // create a new ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // enable pretty print
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            // write the categories json array to the categories.json file
            mapper.writeValue(new FileWriter("src/main/resources/cash.json"), jsonCashArray);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Update the whole users.json file
    @ExcludeFromJacocoGeneratedReport
    private void updateUsersJson() {
        JSONParser parser = new JSONParser();
        try {
            // Create a JSONArray to store all the users
            JSONArray jsonUserArray = new JSONArray();
            // convert the users to JSONArray
            for (User user : this.users) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("username", user.getUsername());
                jsonUser.put("password", user.getPassword());
                jsonUser.put("role", user.getRole());
                jsonUserArray.add(jsonUser);
            }
            // create a new ObjectMapper
            ObjectMapper mapper = new ObjectMapper();
            // enable pretty print
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            // write the categories json array to the categories.json file
            mapper.writeValue(new FileWriter("src/main/resources/users.json"), jsonUserArray);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @ExcludeFromJacocoGeneratedReport
    // cashier, seller and owner interface
    public void edit() {
        System.out.println();
        System.out.println("What do you want to edit?");
        while (true) {
            System.out.print("Input your command: ");
            Scanner sc = new Scanner(System.in);
            String editCommand = sc.nextLine();
            if (editCommand.equalsIgnoreCase("HELP")) {
                if (currentUser.getRole().equals("Seller")) {
                    System.out.println("ITEM: fill/modify the item details.");
                    System.out.println("EXIT: to exit from the edit mode.");
                    System.out.println("HELP: display information about the available commands.");
                } else if (currentUser.getRole().equals("Cashier")) {
                    System.out.println("CASH: fill/modify the available change or notes/coins available in the vending machine.");
                    System.out.println("EXIT: to exit from the edit mode.");
                    System.out.println("HELP: display information about the available commands.");
                } else if (currentUser.getRole().equals("Owner")) {
                    System.out.println("ITEM: fill/modify the item details.");
                    System.out.println("CASH: fill/modify the available change or notes/coins available in the vending machine.");
                    System.out.println("USER: add/remove Seller or Cashier or Owner user(s).");
                    System.out.println("EXIT: to exit from the edit mode.");
                    System.out.println("HELP: display information about the available commands.");
                }
            } else if (editCommand.equalsIgnoreCase("ITEM")) {
                // check the user's role
                if (currentUser.getRole().equals("Seller") || currentUser.getRole().equals("Owner")) {
                    // fill/modify item
                    this.editItems();
                } else {
                    System.out.println("You do not have access to modify the item details.");
                    System.out.println("Only Seller or Owner has access to this.");
                }

            } else if (editCommand.equalsIgnoreCase("CASH")) {
                // check the user's role
                if(currentUser.getRole().equals("Cashier") || currentUser.getRole().equals("Owner")) {
                    // fill/modify cash
                    this.editCash();
                } else {
                    System.out.println("You do not have access to modify the cash");
                    System.out.println("Only Cashier or Owner has access to this.");
                }
            } else if (editCommand.equalsIgnoreCase("USER")) {
                // check the user's role
                if(currentUser.getRole().equals("Owner")) {
                    // add/remove user
                    this.editUser();
                } else {
                    System.out.println("You do not have access to modify the user");
                    System.out.println("Only Owner has access to this.");
                }

            } else if (editCommand.equalsIgnoreCase("EXIT")) {
                break;
            } else {
                System.out.println("Invalid command, please enter your valid command.");
            }
        }
    }
    @ExcludeFromJacocoGeneratedReport
    private boolean checkProductName(String productName) {
        for (Category category : this.categories) {
            for (Product product : category.getProducts()) {
                if (product.getName().equals(productName)) {
                    return true;
                }
            }
        }
        return false;
    }
    @ExcludeFromJacocoGeneratedReport
    private boolean checkProductId(String productId) {
        for (Category category : this.categories) {
            for (Product product : category.getProducts()) {
                if (product.getId().equals(productId)) {
                    return true;
                }
            }
        }
        return false;
    }
    @ExcludeFromJacocoGeneratedReport
    private void editItems() {
        while(true) {
            System.out.println();
            System.out.print("Input the item name you want to edit: ");
            Scanner sc = new Scanner(System.in);
            String itemName = sc.nextLine();
            boolean isItemExist = false;
            // check if the item is in the vending machine
            for (Category category : this.categories) {
                for (Product product : category.getProducts()) {
                    if (product.getName().equals(itemName)) {
                        isItemExist = true;
                        while (true) {
                            System.out.println("What do you want to modify?");
                            System.out.println("1. item name");
                            System.out.println("2. item code");
                            System.out.println("3. item category");
                            System.out.println("4. item quantity");
                            System.out.println("5. item price");
                            System.out.println("6. Exit");
                            System.out.print("Input your command: ");
                            String itemCommand = sc.nextLine();
                            if (itemCommand.equals("1")) {
                                System.out.print("Input the new item name: ");
                                String newItemName = sc.nextLine();
                                if (newItemName.equals(product.getName())){
                                    System.out.println("The new item name is the same as the old item name.");
                                } else if (this.checkProductName(newItemName)) {
                                    System.out.println("The new item name is already in the vending machine.");
                                } else {
                                    product.setName(newItemName);
                                    System.out.println("The item name has been changed to " + newItemName);
                                    updateCategoriesJson();
                                    break;
                                }
                            } else if (itemCommand.equals("2")) {
                                System.out.print("Input the new item code: ");
                                String newItemCode = sc.nextLine();
                                if (newItemCode.equals(product.getId())){
                                    System.out.println("The new item code is the same as the old item code.");
                                } else if (this.checkProductId(newItemCode)) {
                                    System.out.println("The new item code is already in the vending machine.");
                                } else {
                                    product.setId(newItemCode);
                                    System.out.println("The item code has been changed to " + newItemCode);
                                    updateCategoriesJson();
                                    break;
                                }
                            } else if (itemCommand.equals("3")) {
                                System.out.print("Input the new item category: ");
                                String newItemCategory = sc.nextLine();
                                if (newItemCategory.equals(product.getCategory().getName())){
                                    System.out.println("The new item category is the same as the old item category.");
                                } else {
                                    for (Category category1 : this.categories) {
                                        if (category1.getName().equals(newItemCategory)) {

                                            category1.getProducts().add(product);
                                            category.getProducts().remove(product);
                                            System.out.println("Item category has been updated.");
                                            updateCategoriesJson();
                                            break;
                                        }
                                    }

                                    Category newCategory = new Category(newItemCategory);
                                    category.getProducts().remove(product);
                                    newCategory.getProducts().add(product);
                                    System.out.println("Item category has been updated.");
                                    updateCategoriesJson();
                                    break;
                                }
                            } else if (itemCommand.equals("4")) {
                                System.out.print("Input the new item quantity: ");
                                int newItemQuantity = sc.nextInt();
                                if (newItemQuantity < 0 || newItemQuantity > 15) {
                                    System.out.println("Invalid quantity, please enter a valid quantity.");
                                } else {
                                    product.setQuantity(newItemQuantity);
                                    System.out.println("Item quantity has been updated.");
                                    updateCategoriesJson();
                                    break;
                                }
                            } else if (itemCommand.equals("5")) {
                                System.out.print("Input the new item price: ");
                                double newItemPrice = sc.nextDouble();
                                if (newItemPrice < 0) {
                                    System.out.println("Invalid price, please enter a valid price.");
                                } else {
                                    product.setPrice(newItemPrice);
                                    System.out.println("Item price has been updated.");
                                    updateCategoriesJson();
                                    break;
                                }
                            } else if (itemCommand.equals("6")) {
                                break;
                            } else {
                                System.out.println("Invalid command, please enter your valid command.");
                            }
                        }
                    } else if (itemName.equalsIgnoreCase("EXIT")) {
                        return;
                    }
                }
            }
            if (!isItemExist) {
                System.out.println("The item is not in the vending machine.");
            }
        }

    }
    @ExcludeFromJacocoGeneratedReport
    private void editCash() {
        while(true) {
            System.out.println();
            System.out.print("Input the cash you want to edit: ");
            Scanner sc = new Scanner(System.in);
            String cash = sc.nextLine();
            boolean isCashExist = false;
            // check if the cash is in the vending machine
            for (String denomination : this.paymentSystem.getCashInMachine().keySet()) {
                if (denomination.equals(cash)) {
                    isCashExist = true;
                    while (true) {
                        System.out.println("What do you want to modify?");
                        System.out.println("1. cash quantity");
                        System.out.println("2. Exit");
                        System.out.print("Input your command: ");
                        String cashCommand = sc.nextLine();
                        if (cashCommand.equals("1")) {
                            System.out.print("Input the new cash quantity: ");
                            int newCashQuantity = sc.nextInt();
                            if (newCashQuantity < 0) {
                                System.out.println("Invalid quantity, please enter a valid quantity.");
                            } else {
                                this.paymentSystem.getCashInMachine().put(denomination, newCashQuantity);
                                System.out.println("Cash quantity has been updated.");
                                updateCashJson();
                                break;
                            }
                        } else if (cashCommand.equals("2")) {
                            break;
                        } else {
                            System.out.println("Invalid command, please enter your valid command.");
                        }
                    }
                } else if (cash.equalsIgnoreCase("EXIT")) {
                    return;
                }
            }
            if (!isCashExist) {
                System.out.println("The cash is not in the vending machine.");
            }
        }
    }
    @ExcludeFromJacocoGeneratedReport
    private void editUser() {
        while(true) {
            System.out.println("What do you want to do?");
            System.out.println("1. Add user");
            System.out.println("2. Delete user");
            System.out.println("3. Edit user");
            System.out.println("4. Exit");
            System.out.print("Input your command: ");
            Scanner sc = new Scanner(System.in);
            String userCommand = sc.nextLine();
            if (userCommand.equals("1")) {
                System.out.print("Input the new user name: ");
                String newUserName = sc.nextLine();
                if (this.checkUserName(newUserName)) {
                    System.out.println("The new user name is already in the vending machine.");
                } else {
                    System.out.print("Input the new user password: ");
                    String newUserPassword = sc.nextLine();
                    String role = "Customer";
                    while (true) {
                        System.out.println("Choose a role for the new user: ");
                        System.out.println("1. Customer");
                        System.out.println("2. Seller");
                        System.out.println("3. Cashier");
                        System.out.println("4. Owner");
                        System.out.println("5. Exit");
                        System.out.print("Input your command: ");
                        String newUserRole = sc.nextLine();
                        if (newUserRole.equals("1")) {
                            role = "Customer";
                            break;
                        } else if (newUserRole.equals("2")) {
                            role = "Seller";
                            break;
                        } else if (newUserRole.equals("3")) {
                            role = "Cashier";
                            break;
                        } else if (newUserRole.equals("4")) {
                            role = "Owner";
                            break;
                        } else if (newUserRole.equals("5")) {
                            return;
                        } else {
                            System.out.println("Invalid command, please enter your valid command.");
                        }
                    }
                    User newUser = new User(newUserName, newUserPassword, role);
                    this.users.add(newUser);
                    System.out.println("User has been added.");
                    updateUsersJson();
                    break;
                }
            } else if (userCommand.equals("2")) {
                System.out.print("Input the user name you want to delete: ");
                String deleteUserName = sc.nextLine();
                boolean isUserExist = false;
                for (User user : this.users) {
                    if (user.getUsername().equals(deleteUserName)) {
                        isUserExist = true;
                        this.users.remove(user);
                        System.out.println("User has been deleted.");
                        updateUsersJson();
                        break;
                    } else if (deleteUserName.equalsIgnoreCase("EXIT")) {
                        return;
                    }
                }
                if (!isUserExist) {
                    System.out.println("The user is not in the vending machine.");
                }
            } else if (userCommand.equals("3")) {
                System.out.print("Input the user name you want to edit: ");
                String editUserName = sc.nextLine();
                boolean isUserExist = false;
                for (User user : this.users) {
                    if (user.getUsername().equals(editUserName)) {
                        isUserExist = true;
                        System.out.println("Current Role: " + user.getRole());
                        while (true) {
                            System.out.println("What role you want to modify?");
                            System.out.println("1. Customer");
                            System.out.println("2. Seller");
                            System.out.println("3. Cashier");
                            System.out.println("4. Owner");
                            System.out.println("5. Exit");
                            System.out.print("Input your command: ");
                            String editUserRole = sc.nextLine();
                            if (editUserRole.equals("1")) {
                                if (user.getRole().equals("Customer")) {
                                    System.out.println("The user is already a customer.");
                                } else {
                                    user.setRole("Customer");
                                    System.out.println("User role has been updated.");
                                    updateUsersJson();
                                    break;
                                }
                            } else if (editUserRole.equals("2")) {
                                if (user.getRole().equals("Seller")) {
                                    System.out.println("The user is already a seller.");
                                } else {
                                    user.setRole("Seller");
                                    System.out.println("User role has been updated.");
                                    updateUsersJson();
                                    break;
                                }
                            } else if (editUserRole.equals("3")) {
                                if (user.getRole().equals("Cashier")) {
                                    System.out.println("The user is already a cashier.");
                                } else {
                                    user.setRole("Cashier");
                                    System.out.println("User role has been updated.");
                                    updateUsersJson();
                                    break;
                                }
                            } else if (editUserRole.equals("4")) {
                                if (user.getRole().equals("Owner")) {
                                    System.out.println("The user is already an owner.");
                                } else {
                                    user.setRole("Owner");
                                    System.out.println("User role has been updated.");
                                    updateUsersJson();
                                    break;
                                }
                            } else if (editUserRole.equals("5")) {
                                break;
                            }
                        }
                    }
                }
                if (!isUserExist) {
                    System.out.println("The user is not in the vending machine.");
                }
            }
        }
    }

    // Report generation interface
    @ExcludeFromJacocoGeneratedReport
    public void report() {
        System.out.println();
        System.out.println("What kind of report do you want to generate?");
        while(true) {
            System.out.print("Input your command: ");
            Scanner sc = new Scanner(System.in);
            String reportCommand = sc.nextLine();
            if (reportCommand.equalsIgnoreCase("HELP")) {
                if (currentUser.getRole().equals("Owner")) {
                    System.out.println("SELLER REPORT: generate details report that Seller can read.");
                    System.out.println("CASHIER REPORT: generate details report that Cashier can read.");
                    System.out.println("OWNER REPORT: generate details report that Owner can read.");
                    System.out.println("EXIT: to exit from the generate report mode.");
                    System.out.println("HELP: display information about the available commands.");
                } else if (currentUser.getRole().equals("Cashier")) {
                    System.out.println("CASHIER REPORT: generate details report that Cashier can read.");
                    System.out.println("EXIT: to exit from the generate report mode.");
                    System.out.println("HELP: display information about the available commands.");
                } else if (currentUser.getRole().equals("Seller")) {
                    System.out.println("SELLER REPORT: generate details report that Seller can read.");
                    System.out.println("EXIT: to exit from the generate report mode.");
                    System.out.println("HELP: display information about the available commands.");
                }
            } else if (reportCommand.equalsIgnoreCase("SELLER REPORT")) {
                // check the user's role
                if (currentUser.getRole().equals("Seller") || currentUser.getRole().equals("Owner")) {
                    // Generating Seller's report
                    this.generateSellerReport();
                } else {
                    System.out.println("You do not have access to generate the seller report.");
                    System.out.println("Only Seller or Owner has access to this.");
                }
            } else if (reportCommand.equalsIgnoreCase("CASHIER REPORT")) {
                // check the user's role
                if (currentUser.getRole().equals("Cashier") || currentUser.getRole().equals("Owner")) {
                    // Generating Cashier's report
                    this.generateCashierReport();
                } else {
                    System.out.println("You do not have access to generate the cashier report.");
                    System.out.println("Only Cashier or Owner has access to this.");
                }
            } else if (reportCommand.equalsIgnoreCase("OWNER REPORT")) {
                // check the user's role
                if (currentUser.getRole().equals("Owner")) {
                    // Generating Owner's report
                    this.generateOwnerReport();
                } else {
                    System.out.println("You do not have access to generate the owner report.");
                    System.out.println("Only Owner has access to this.");
                }
            } else if (reportCommand.equalsIgnoreCase("EXIT")) {
                break;
            } else {
                System.out.println("Invalid command, please enter your valid command.");
            }
        }
    }
    @ExcludeFromJacocoGeneratedReport
    private void generateSellerReport() {
        System.out.println();
        System.out.println("Which report do you want to generate? List or Summary");
        while(true) {
            System.out.print("Input your command: ");
            Scanner sc = new Scanner(System.in);
            String reportCommand = sc.nextLine();
            if (reportCommand.equalsIgnoreCase("LIST")) {
                itemList();
            } else if (reportCommand.equalsIgnoreCase("SUMMARY")) {
                itemSummary();
            } else if (reportCommand.equalsIgnoreCase("HELP")) {
                System.out.println("LIST: generate a list of the current available items that include the item details.");
                System.out.println("SUMMARY: generate a summary that includes items codes, item names and the total number of quantity sold for each item");
                System.out.println("HELP: display information about the available commands.");
                System.out.println("BACK: go back to previous page");
            } else if (reportCommand.equalsIgnoreCase("BACK")) {
                break;
            } else {
                System.out.println("Invalid command, please enter your valid command.");
            }
        }
    }

    // generate a item list report
    @ExcludeFromJacocoGeneratedReport
    private void itemList(){
        try {
            File file = new File("itemList.txt");
            // if no such a txt file, create a new itemList.txt
            if (!file.exists()) {
                file.createNewFile();
            }
            // if the file exists, write the change list to the file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Category category: this.categories) {
                bw.write(category.getName() + ": ");
                bw.newLine();
                for (Product product : category.getProducts()) {
                    bw.write(product.getName() + ": Price " + product.getPrice() + " Quantity " + product.getQuantity());
                    bw.newLine();
                }
            }
            bw.close();
            System.out.println("The item list report has been generated.");
            System.out.println("File path: " + file.getAbsolutePath());
            System.out.println("File name: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // generate items summary report
    @ExcludeFromJacocoGeneratedReport
    private void itemSummary() {
        try {
            File file = new File("itemSummary.txt");
            // if no such a txt file, create a new itemList.txt
            if (!file.exists()) {
                file.createNewFile();
            }
            // if the file exists, write the change list to the file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (Category category: this.categories) {
                for (Product product : category.getProducts()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(product.getId()).append(";").append(product.getName()).append(";").append(product.getQuantitySold());
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }
            bw.close();
            System.out.println("The item summary report has been generated.");
            System.out.println("File path: " + file.getAbsolutePath());
            System.out.println("File name: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ExcludeFromJacocoGeneratedReport
    private void generateCashierReport() {
        System.out.println();
        System.out.println("Which report do you want to generate? List or Summary");
        while(true) {
            System.out.print("Input your command: ");
            Scanner sc = new Scanner(System.in);
            String reportCommand = sc.nextLine();
            if (reportCommand.equalsIgnoreCase("LIST")) {
                changeList();
            } else if (reportCommand.equalsIgnoreCase("SUMMARY")) {
                transactionSummary();
            } else if (reportCommand.equalsIgnoreCase("HELP")) {
                System.out.println("LIST: generate a list of the current available change.");
                System.out.println("SUMMARY: generate a summary of transactions that includes transaction date and time, item sold, amount of money paid, returned change and payment method.");
                System.out.println("HELP: display information about the available commands.");
                System.out.println("BACK: go back to previous page");
            } else if (reportCommand.equalsIgnoreCase("BACK")) {
                break;
            } else {
                System.out.println("Invalid command, please enter your valid command.");
            }
        }
    }

    // generate a change list report
    @ExcludeFromJacocoGeneratedReport
    private void changeList(){
        try {
            File file = new File("changeList.txt");
            // if no such a txt file, create a new changeList.txt
            if (!file.exists()) {
                file.createNewFile();
            }
            // if the file exists, write the change list to the file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String denomination : paymentSystem.getCashInMachine().keySet()) {
                bw.write("The quantity of " + denomination + " is " + paymentSystem.getCashInMachine().get(denomination));
                bw.newLine();
            }
            bw.close();
            System.out.println("The change list report has been generated.");
            System.out.println("File path: " + file.getAbsolutePath());
            System.out.println("File name: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  generate a transaction summary report
    @ExcludeFromJacocoGeneratedReport
    private void transactionSummary() {
        try {
            File file = new File("transactionHistory.txt");
            // if no such a txt file, create a new transactionHistory.txt
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("No transaction history founded.");
                return;
            } else {
                System.out.println("The transaction summary report has been generated.");
                System.out.println("File path: " + file.getAbsolutePath());
                System.out.println("File name: " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ExcludeFromJacocoGeneratedReport
    private void generateOwnerReport() {
        System.out.println();
        System.out.println("Which report do you want to generate? List or Summary");
        while(true) {
            System.out.print("Input your command: ");
            Scanner sc = new Scanner(System.in);
            String reportCommand = sc.nextLine();
            if (reportCommand.equalsIgnoreCase("LIST")) {
                userList();
            } else if (reportCommand.equalsIgnoreCase("SUMMARY")) {
                cancelledSummary();
            } else if (reportCommand.equalsIgnoreCase("HELP")) {
                System.out.println("LIST: generate a list of usernames in the vending machine with the associated role.");
                System.out.println("SUMMARY: generate a summary of cancelled transaction. This summary only includes date and time of the cancelled, the user.");
                System.out.println("HELP: display information about the available commands.");
                System.out.println("BACK: go back to previous page");
            } else if (reportCommand.equalsIgnoreCase("BACK")) {
                break;
            } else {
                System.out.println("Invalid command, please enter your valid command.");
            }
        }
    }

    // generate a user list report
    @ExcludeFromJacocoGeneratedReport
    private void userList(){
        try {
            File file = new File("userList.txt");
            // if no such a txt file, create a new userList.txt
            if (!file.exists()) {
                file.createNewFile();
            }
            // if the file exists, write the change list to the file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (User user : this.users) {
                bw.write(user.getUsername() + " " + user.getRole());
                bw.newLine();
            }
            bw.close();
            System.out.println("The user list report has been generated.");
            System.out.println("File path: " + file.getAbsolutePath());
            System.out.println("File name: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // generate a cancelled transaction summary report
    @ExcludeFromJacocoGeneratedReport
    private void cancelledSummary(){
        try {
            File file = new File("cancelledTransactionHistory.txt");
            // if no such a txt file, create a new cancelledTransactionHistory.txt
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("No cancelled transaction founded.");
                return;
            } else {
                System.out.println("The cancelled transaction summary report has been generated.");
                System.out.println("File path: " + file.getAbsolutePath());
                System.out.println("File name: " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
