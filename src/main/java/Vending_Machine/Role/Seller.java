package Vending_Machine.Role;

import Vending_Machine.Category;
import Vending_Machine.Product;
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

public class Seller implements Role {
    List<Category> categories = new ArrayList<>();

//    Seller: this role is able to fill/modify the item details.
//    To fill and modify the items, this role is able to select and modify item details such as item name,
//    item code, item category, item quantity and item price.
//    Appropriate error message must be shown if the quantity added will be over than the limit (15 of each item/product)
//    or there is conflicting item code/name/category.


    public String getName() {
        return "Seller";
    }
    Role role1;
    //    check if the role is seller
    public boolean checkRole(User user) {
        if(user.getRole().getName().equals("Seller")) {
            return true;
        }
        return false;
    }



    private void viewProduct() {
//        print the list of the product in categories.json

        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader("src/main/resources/categories.json"));
            JSONArray categories = (JSONArray) obj;
            for (Object category : categories) {
                JSONObject categoryObj = (JSONObject) category;
                System.out.println("Category: " + categoryObj.get("name"));
                JSONArray products = (JSONArray) categoryObj.get("products");
                for (Object product : products) {
                    JSONObject productObj = (JSONObject) product;
                    System.out.println("Product: " + productObj.get("name"));
                    System.out.println("Product ID: " + productObj.get("id"));
                    System.out.println("Product Price: " + productObj.get("price"));
                    System.out.println("Product Quantity: " + productObj.get("quantity"));
                    System.out.println();
                }
            }
        }catch (IOException | ParseException e) {
            e.printStackTrace();

        }
    }




    public void addProduct() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("src/main/java/Vending_Machine/Category.json"));
            JSONArray jsonObject = (JSONArray) obj;
            for (Object seller : jsonObject) {
                JSONObject sellerObject = (JSONObject) seller;
                String name = (String) sellerObject.get("name");
                Category category = new Category(name);

                JSONArray products = (JSONArray) sellerObject.get("products");
                for (Object product : products) {
                    JSONObject productObject = (JSONObject) product;
                    String id = (String) productObject.get("id");
                    String productName = (String) productObject.get("name");
                    double price = (double) productObject.get("price");
                    int quantity = (int) productObject.get("quantity");
                    Product product1 = new Product(id, productName, category, price, quantity);
                    category.addProduct(product1);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        //        add a new product to the vending machine also add the product to categories.json
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product name");
        String name = scanner.nextLine();
        System.out.println("Enter the product id");
        String id = scanner.nextLine();
        System.out.println("Enter the product price");
        double price = scanner.nextDouble();
        System.out.println("Enter the product quantity");
        int quantity = scanner.nextInt();
        System.out.println("Enter the product category");
        String categoryName = scanner.next();
        Category category = new Category(categoryName);
        Product product = new Product(id, name, category, price, quantity);
        category.addProduct(product);

        if (quantity > 15) {
            System.out.println("The quantity added will be over than the limit (15 of each item/product)");
        }
        if (id.equals(product.getId())) {
            System.out.println("There is conflicting item code");
        }
        if (name.equals(product.getName())) {
            System.out.println("There is conflicting item name");
        }
        if (categoryName.equals(product.getCategory().getName())) {
            System.out.println("There is conflicting item category");
        } else {
            System.out.println("Product added successfully");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new FileWriter("src/main/java/Vending_Machine/Category.json"), category);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void modifyProduct() {
//        change the product name, id, price, and quantity
        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader("src/main/java/Vending_Machine/Category.json"));
            JSONArray jsonObject = (JSONArray) obj;
            for (Object seller : jsonObject) {
                JSONObject sellerObject = (JSONObject) seller;
                String name = (String) sellerObject.get("name");
                Category category = new Category(name);

                JSONArray products = (JSONArray) sellerObject.get("products");
                for (Object product : products) {
                    JSONObject productObject = (JSONObject) product;
                    String id = (String) productObject.get("id");
                    String productName = (String) productObject.get("name");
                    double price = (double) productObject.get("price");
                    int quantity = (int) productObject.get("quantity");
                    Product product1 = new Product(id, productName, category, price, quantity);
                    category.addProduct(product1);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //        change the product name, id, price, and quantity

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product name");
        String name = scanner.nextLine();
        System.out.println("Enter the product id");
        String id = scanner.nextLine();
        System.out.println("Enter the product price");
        double price = scanner.nextDouble();
        System.out.println("Enter the product quantity");
        int quantity = scanner.nextInt();
        System.out.println("Enter the product category");
        String categoryName = scanner.next();
        Category category = new Category(categoryName);
        Product product = new Product(id, name, category, price, quantity);
        category.addProduct(product);

        if (quantity > 15) {
            System.out.println("The quantity added will be over than the limit (15 of each item/product)");
        }
        if (id.equals(product.getId())) {
            System.out.println("There is conflicting item code");
        }
        if (name.equals(product.getName())) {
            System.out.println("There is conflicting item name");
        }
        if (categoryName.equals(product.getCategory().getName())) {
            System.out.println("There is conflicting item category");
        } else {
            System.out.println("Product info edit successfully");
        }

    }


}



