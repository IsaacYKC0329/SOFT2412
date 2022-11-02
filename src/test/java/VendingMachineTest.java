import Vending_Machine.User;
import Vending_Machine.VendingMachine;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;




import java.io.*;

import static org.junit.jupiter.api.Assertions.*;


class VendingMachineTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    private String currentUser;
    private User user;




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






    @Test
    void getCurrentUser() {
//        public User getCurrentUser() {
//            return currentUser;
//        }


    }


    @Test
    void userLogin() {
//        if (input.equalsIgnoreCase("L") || input.equalsIgnoreCase("LOGIN")) {
//                if (login()) {
//                    return;
//                }


        provideInput("l");
        VendingMachine vendingMachine = new VendingMachine();

    }






    @Test
    void login() {

        provideInput("seller1");
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.login();
        assertEquals("seller1", vendingMachine.getCurrentUser().getUsername());

    }

    @Test
    void badLogin() {
//        else {
//                if (!checkUserName(username)) {
//                    System.out.println("Wrong password, please try again!");
//                } else {
//                    System.out.println();
//                    System.out.println("Incorrect username or password! If you are a new user, please register first.");
//                    System.out.println();
//                    System.out.println("1. Register");
//                    System.out.println("2. Login as a guest");
//                    System.out.println();
//                    System.out.print("Input your command: ");
//                    String input = scanner.nextLine();
//                    if (input.equalsIgnoreCase("1") || input.equalsIgnoreCase("REGISTER")) {
//                        if (register()) {
//                            return true;
//                        }
//                    } else if (input.equalsIgnoreCase("2") || input.equalsIgnoreCase("LOGIN AS A GUEST")) {
//                        // Login as a guest
//                        this.currentUser = new User("Guest", "Guest", "Customer");
//                        this.currentUser.readLastBought5FromFile();
//                        System.out.println("Login as a guest successfully!");
//
//                        return true;
//                    }




    }


    @Test
    void TestregisterExit() {

        provideInput("EXIT");
        VendingMachine vendingMachine = new VendingMachine();
        boolean result = vendingMachine.register();
        assertFalse(result);

    }


    @Test
    void Testregisternameused() {

//        test if the user enter a name that has been used, the register will return false
        provideInput("seller1\n" + "seller2");
        VendingMachine vendingMachine = new VendingMachine();
        boolean result = vendingMachine.register();
        assertTrue(result);

    }




    @Test
    void checkUserName() {


        VendingMachine vendingMachine = new VendingMachine();
        assertFalse(vendingMachine.checkUserName("seller1"));

    }


    @Test
    void checkUserLogin() {

        VendingMachine vendingMachine = new VendingMachine();
        assertTrue(vendingMachine.checkUserLogin("seller1", "seller1"));
    }


    @Test
    void createUser(){

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.createUser("seller1", "seller1", "seller1");
        assertFalse(vendingMachine.checkUserName("seller1"));


    }

    @Disabled
    @Test
    void showMenu() {
    }



    @Disabled
    @Test
    void transaction() {
    }


    @Test
    void addToCart() {

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addToCart("test", 1);

    }

    @Disabled
    @Test
    void removeFromCart() {
    }


    @Test
    void deleteFromEmptyCart() {

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.deleteFromCart("test");

    }

    @Test
    void deleteFromCart2() {

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addToCart("Pepsi", 1);
        vendingMachine.deleteFromCart("Pepsi");

    }

    @Test
    void deleteFromCart3(){

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addToCart("Coca cola", 1);
        vendingMachine.deleteFromCart("Pepsi");

    }


    @Test
    void viewCart() {
//        public void viewCart() {
//            // Print the cart
//            System.out.println("Cart:");
//            for (Map.Entry<Product, Integer> entry : this.cart.entrySet()) {
//                System.out.println(entry.getKey().getName() + " " + entry.getValue());
//            }
//        }

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.viewCart();

//        assertEquals("Cart:\n", getOutput());



    }


    @Test
    void clearCart() {

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.clearCart();




    }


    @Test
    void checkoutCartempty() {

        VendingMachine vendingMachine = new VendingMachine();
        assertFalse(vendingMachine.checkoutCart());

    }

    @Test
    void checkoutCartitemenough() {


        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addToCart("Pepsi", 1100);




    }

    @Disabled
    @Test
    void saveTransaction() {
    }

    @Disabled
    @Test
    void saveCancelledTransaction() {
    }


    @Test
    void TesthidingEnter() {

        VendingMachine vendingMachine = new VendingMachine();
        String password = vendingMachine.hidingEnter("Enter password: ");
        assertEquals("1", password);


    }


    @Test
    void writeData() {
//        public void writeData(String save) {
//        // write the data into txt file
//        try {
//            FileWriter fileWriter = new FileWriter("src/main/resources/save_data.txt");
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//            // write the username in to txt file
//            bufferedWriter.write(this.currentUser.getUsername() + " " + save +" \n");
//            System.out.println("card saved.");
//            bufferedWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    }

    @Disabled
    @Test
    void cardSaved() {
//            public boolean cardSaved() {
//        // read the data from txt file until the end of the file
//        try {
//            FileReader fileReader = new FileReader("src/main/resources/save_data.txt");
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line = bufferedReader.readLine();
//            while (line != null) {
//                // read the username from txt file
//                String[] data = line.split(" ");
//                // if the username is the same as the username in txt file
//                if (data[0].equals(this.currentUser.getUsername())) {
//                    String saveCard = data[1];
//                    // check if the username and password is correct
//                    if (saveCard.equals("Y")) {
//                        bufferedReader.close();
//                        return true;
//                    }
//                }
//                line = bufferedReader.readLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


    }


    @Test
    void updateCategoriesJson() {


        try {
            VendingMachine vendingMachine = new VendingMachine();
            vendingMachine.updateCategoriesJson();
            File file = new File("src/main/resources/categories.json");
            assertTrue(file.exists());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Disabled
    @Test
    void edit() {
    }

    @Disabled
    @Test
    void report() {
    }
}