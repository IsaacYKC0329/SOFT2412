package Vending_Machine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PaymentSystem {
    // the hashmap key is the denomination, the value is the quantity.
    private final HashMap<String, Integer> cashInMachine = new HashMap<>();
    private final HashMap<String, String> cardInMachine = new HashMap<>();

    // initialize the cash amount in vending machine
    public void initializeCash(String denomination, int quantity) {
        cashInMachine.put(denomination, quantity);
    }

    // add cash into vending machine
    public void addCash(String denomination, int quantity) {
        if (cashInMachine.containsKey(denomination)) {
            if(quantity >= 0) {
                cashInMachine.put(denomination, cashInMachine.get(denomination) + quantity);
            }
            else {
                System.out.println("The quantity should be greater or equal than 0.");
            }
        } else {
            System.out.println("The denomination is not in the machine.");
        }
    }

    // modify cash quantity in vending machine
    public void modifyCash(String denomination, int quantity) {
        if (cashInMachine.containsKey(denomination)) {
            if(quantity >= 0) {
                cashInMachine.put(denomination, quantity);
            } else {
                System.out.println("The quantity should be greater or equal than 0.");
            }
        } else {
            System.out.println("The denomination is not in the machine.");
        }
    }

    // print specific cash quantity in vending machine
    public void getCashQuantity(String denomination) {
        if (cashInMachine.containsKey(denomination)) {
            System.out.println("The quantity of " + denomination + " is " + cashInMachine.get(denomination));
        } else {
            System.out.println("The denomination is not in the machine.");
        }
    }

    // print all cash quantity in vending machine
    public void getAllCashQuantity() {
        for (String denomination : cashInMachine.keySet()) {
            System.out.println("The quantity of " + denomination + " is " + cashInMachine.get(denomination));
        }
    }

    // initialize the card information in vending machine
    public void initializeCard(String cardName, String cardNumber) {
        cardInMachine.put(cardName, cardNumber);
    }

    // check if the card owner and card number is in the machine
    public boolean checkCard(String cardName, String cardNumber) {
        if (cardInMachine.containsKey(cardName)) {

            if (cardInMachine.get(cardName).equals(cardNumber)) {
                return true;
            }
            else{
                System.out.println("The card number is not correct.");
                return false;
            }
        }
        System.out.println("The name of the owner is not in the machine.");
        return false;
    }


    // Give customers change by check the payment amount and the price of the product
    public HashMap<String, Integer> giveChange(double doublePaymentAmount, double doublePrice) {

        BigDecimal paymentAmount = BigDecimal.valueOf(doublePaymentAmount);
        BigDecimal price = BigDecimal.valueOf(doublePrice);

        boolean TransactionContinued = true;
        HashMap<String, Integer> actualChanges = new HashMap<>();

        // calculate how much change the customer should get
        while (TransactionContinued){

            HashMap<String, Integer> changes = new HashMap<>();

            double change = paymentAmount.subtract(price).doubleValue();

            // payment amount not enough
            if (change < 0) {

                System.out.println("The payment amount is not enough.");
                System.out.println("The price is "+ price + ", the payment amount is " + paymentAmount);
                double nextCommand = Double.parseDouble(inputNextCommand(1));

                // cancel the transaction
                if(nextCommand<0){
                    System.out.println("Transaction cancelled.");
                    TransactionContinued = false;
                    actualChanges.put("paymentAccept", 0);
                }

                // add amount to the existing payment
                else{
                    paymentAmount = paymentAmount.add(BigDecimal.valueOf(nextCommand));
                }
            }

            // payment amount equals to price, no change to give back
            else if (change == 0) {
                System.out.print("""
                    payment accepted
                    no change to give back.
                    """);
                TransactionContinued = false;
                actualChanges.put("paymentAccept", 1);
            }

            // calculate how would the change be given
            else {
                String[] denominations = {"100", "50", "20", "10", "5", "2", "1", "0.5", "0.2", "0.1", "0.05"};
                for (String denomination : denominations) {
                    int quantity = (int)(change / Double.parseDouble(denomination));
                    if (quantity > 0) {

                        // check if the machine has enough change
                        if (cashInMachine.get(denomination) >= quantity) {

                            // update the cash amount in machine
                            cashInMachine.put(denomination, cashInMachine.get(denomination) - quantity);

                            // add the change that will gives back to customer into a hashmap
                            if(changes.containsKey(denomination)){
                                changes.put(denomination, changes.get(denomination) + quantity);
                            }
                            else{
                                changes.put(denomination, quantity);
                            }
                            change = BigDecimal.valueOf(change).subtract(BigDecimal.valueOf(Double.parseDouble(denomination) * quantity)).doubleValue();
                        }
                    }
                }

                // vending machine do not have enough cash to give back
                if (change > 0) {
                    System.out.println("The machine does not have enough cash to give back");
                    double nextCommand = Double.parseDouble(inputNextCommand(0));

                    // cancel the transaction
                    if (nextCommand<0) {
                        System.out.println("Transaction cancelled.");
                        TransactionContinued = false;
                        actualChanges.put("paymentAccept", 0);

                    }

                    // input new payment amount
                    else {
                        paymentAmount = BigDecimal.valueOf(nextCommand);
                    }
                }

                // output the change to customer
                else {
//                    System.out.println("payment accepted");
////                    System.out.println("The change is:");
////                    for (String denomination : changes.keySet()) {
////                        System.out.println(denomination+ ": " + changes.get(denomination));
//                    }
                    actualChanges = changes;
                    updateJSONFile();
                    TransactionContinued = false;
                }
            }
        }
        return actualChanges;
    }

    public HashMap<String, Integer> getCashInMachine() {
        return cashInMachine;
    }

    // ask user to input the next command
    public String inputNextCommand(int type) {

        // machine have no enough cash to give back
        if(type == 0){
            System.out.print("""
                        Please input the next command:
                        1. input new payment amount
                        2. input negative number to cancel transaction
                        """);
        }

        // payment amount not enough
        else if(type == 1){
            System.out.print("""
                        Please input the next command:
                        1. input addition payment amount
                        2. input negative number to cancel transaction
                        """);
        }
        System.out.print("Input your command: ");
        String nextCommand = new Scanner(System.in).nextLine();

        // if the command is not a number, ask user to input again
        try {
            Double.parseDouble(nextCommand);
            return nextCommand;
        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("invalid input");
            return inputNextCommand(type);
        }

    }

    // update the quantity of cash in JSON file
    public void updateJSONFile() {
        JSONParser parser = new JSONParser();
        try {
            Object cashObj = parser.parse(new FileReader("src/main/resources/cash.json"));
            JSONArray jsonCashArray = (JSONArray) cashObj;
            for (Object cash : jsonCashArray) {
                JSONObject cashObject = (JSONObject) cash;
                String denomination = (String) cashObject.get("denomination");
                cashObject.put("quantity", cashInMachine.get(denomination));
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(new FileWriter("src/main/resources/cash.json"), jsonCashArray);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getCardInMachine() {
        return cardInMachine;
    }
}




