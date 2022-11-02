import java.io.*;

import Vending_Machine.App;
import Vending_Machine.VendingMachine;
import org.junit.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AppTest {

    App app = new App();

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }


    @Disabled
    @Test
    void TestMain() {

//        input = g, help, buy, add Pepsi 2, cancel, exit

        provideInput("g\nhelp\nbuy\nadd Pepsi 2\ncancel\nexit\n");
        app.main(new String[0]);
        assertEquals("Welcome to the vending machine! Please enter a command or type \"help\" for a list of commands.\n" +
                "g: Get a list of all items in the vending machine.\n" +
                "buy: Buy an item from the vending machine.\n" +
                "add: Add an item to the vending machine.\n" +
                "cancel: Cancel the current transaction.\n" +
                "exit: Exit the program.\n" +
                "Please enter the name of the item you would like to buy.\n" +
                "Available commands: Pepsi, Coke, Sprite, Fanta, Water, Juice\n" +
                "Please enter the name of the item you would like to add.\n" +
                "Available commands: Pepsi, Coke, Sprite, Fanta, Water, Juice\n" +
                "Please enter the quantity of the item you would like to add.\n" +
                "Available commands: Pepsi, Coke, Sprite, Fanta, Water, Juice\n" +
                "Transaction cancelled.\n" +
                "Thank you for using the vending machine. Have a nice day!\n", getOutput());
    }

    @Disabled
    @Test
    void TestMain2() {

// public static void main(String[] args) {
//        VendingMachine vendingMachine = new VendingMachine();
//        vendingMachine.showMenu();
//        // User Login Interface
//        vendingMachine.userLogIn();
//        // Main Menu
//        vendingMachine.showMenu();
//        while(true){
//            Scanner sc = new Scanner(System.in);
//
//            System.out.print("Input your command: ");
//            String command = sc.nextLine();
//            String [] parameter = command.split(" ");
//            String firstCommand = parameter[0];
//            if (firstCommand.equalsIgnoreCase("HELP")) {
//                System.out.println();
//                System.out.println("BUY: buy some items");
//                System.out.println("EDIT: fill or modify something in the vending machine");
//                System.out.println("REPORT: show information or records of vending machines");
//                System.out.println("EXIT: exit vending machine");
//                System.out.println("HELP: print this manual");
//                System.out.println();
//            } else if (firstCommand.equalsIgnoreCase("BUY")) {
//                //vendingMachine.idleTimer();
//                vendingMachine.transaction();
//            } else if (firstCommand.equalsIgnoreCase("EDIT")) {
//                // Check if user has admin role
//                if (vendingMachine.getCurrentUser().getRole().equals("Seller")||vendingMachine.getCurrentUser().getRole().equals("Cashier")||vendingMachine.getCurrentUser().getRole().equals("Owner")){
//                    vendingMachine.edit();
//                } else {
//                    System.out.println("Your do not have access to edit anything");
//                }
//                vendingMachine.edit();
//            } else if (firstCommand.equalsIgnoreCase("REPORT")) {
//                // Check if user has admin role
//                if (vendingMachine.getCurrentUser().getRole().equals("Seller")||vendingMachine.getCurrentUser().getRole().equals("Cashier")||vendingMachine.getCurrentUser().getRole().equals("Owner")){
//                    vendingMachine.report();
//                } else {
//                    System.out.println("Your do not have access to generate any reports");
//                }
//            } else if (firstCommand.equalsIgnoreCase("EXIT")) {
//                System.exit(0);
//            } else {
//                System.out.println("Invalid command, please enter again.");
//            }
//        }
//    }

        provideInput("help\nbuy\nedit\nreport\nexit\n");
        app.main(new String[0]);
        assertEquals("Welcome to the vending machine! Please enter a command or type \"help\" for a list of commands.\n" +
                "g: Get a list of all items in the vending machine.\n" +
                "buy: Buy an item from the vending machine.\n" +
                "add: Add an item to the vending machine.\n" +
                "cancel: Cancel the current transaction.\n" +
                "exit: Exit the program.\n" +
                "Please enter the name of the item you would like to buy.\n" +
                "Available commands: Pepsi, Coke, Sprite, Fanta, Water, Juice\n" +
                "Please enter the name of the item you would like to add.\n" +
                "Available commands: Pepsi, Coke, Sprite, Fanta, Water, Juice\n" +
                "Please enter the quantity of the item you would like to add.\n" +
                "Available commands: Pepsi, Coke, Sprite, Fanta, Water, Juice\n" +
                "Transaction cancelled.\n" +
                "Thank you for using the vending machine. Have a nice day!\n", getOutput());


    }

//    @Disabled
//    @Test
//    void TestMain3() {
//
//
//    }


}