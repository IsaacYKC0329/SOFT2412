import Vending_Machine.PaymentSystem;
import Vending_Machine.VendingMachine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PaymentSystemTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    public PaymentSystem paymentSystem;

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @BeforeEach
    public void setUp() {
        this.paymentSystem = new PaymentSystem();
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
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    @Test
    void TestInitializeCash() {
        paymentSystem.initializeCash("1", 1);
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));

    }

    @Test
    void TestAddCash() {
        paymentSystem.initializeCash("1", 1);
        paymentSystem.addCash("1", 1);
        assertEquals(2, paymentSystem.getCashInMachine().get("1"));
    }

    @Test
    void TestaddCashquantity() {

        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.initializeCash("1", 1);
        paymentSystem.addCash("1", -1);
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));
    }

    @Test
    void TestaddCashdenomination() {

        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.initializeCash("1", 1);
        paymentSystem.addCash("2", 1);
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));
    }

    @Test
    void TestModifyCash() {
        paymentSystem.initializeCash("1", 1);
        paymentSystem.modifyCash("1", 1);
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));
    }

    @Test
    void TestmodifyCashquantity() {
        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.initializeCash("1", 1);
        paymentSystem.modifyCash("1", -1);
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));

    }

    @Test
    void TestmodifyCashdenomination() {

        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.initializeCash("1", 1);
        paymentSystem.modifyCash("2", 1);
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));

    }

    @Test
    void TestGetCashQuantity() {
        paymentSystem.initializeCash("1", 1);
        paymentSystem.getCashQuantity("1");
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));

    }

    @Test
    void getCashQuantityDenomination() {

        PaymentSystem paymentSystem = new PaymentSystem();
        paymentSystem.initializeCash("1", 1);
        paymentSystem.getCashQuantity("2");
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));

    }

    @Test
    void getAllCashQuantity() {
        paymentSystem.initializeCash("1", 1);
        paymentSystem.getAllCashQuantity();
        assertEquals(1, paymentSystem.getCashInMachine().get("1"));


    }

    @Test
    void TestInitializeCard() {
        paymentSystem.initializeCard("Alice", "8888");
        assertEquals("8888", paymentSystem.getCardInMachine().get("Alice"));

    }

    @Test
    void TestCheckCard() {
        paymentSystem.initializeCard("USYD-Sit", "5001899280");
        paymentSystem.checkCard("USYD-Sit", "5001899280");
        assertEquals(true, paymentSystem.checkCard("USYD-Sit", "5001899280"));

    }

    @Test
    void TestCheckCardNameFalse(){
        paymentSystem.initializeCard("USYD-Sit", "5001899280");
        paymentSystem.checkCard("USYD-Sit", "5001899280");
        assertEquals(false, paymentSystem.checkCard("USYD-sit", "5001899281"));


    }

    @Test
    void TestCheckCardNumberFalse(){
        paymentSystem.initializeCard("USYD-Sit", "5001899280");
        paymentSystem.checkCard("USYD-Sit", "5001899280");
        assertEquals(false, paymentSystem.checkCard("USYD-Sit", "5001899281"));


    }

    @Test
    void TestGiveChangeSuccessA() {
//        paymentSystem.initializeCash("2", 5);
//        paymentSystem.initializeCash("0.5", 5);
//        paymentSystem.initializeCash("0.2", 5);
        HashMap<String, Integer> change = paymentSystem.giveChange(45.5, 42.8);
        assertTrue(change.containsKey("2"));
        assertTrue(change.containsKey("0.5"));
        assertTrue(change.containsKey("0.2"));
        assertEquals(change.get("2"), 1);
        assertEquals(change.get("0.5"), 1);
        assertEquals(change.get("0.2"), 1);
    }

    @Test
    void TestGiveChangeSuccessB() {
        HashMap<String, Integer> change = paymentSystem.giveChange(45.5, 45.5);
        assertEquals(change.get("paymentAccept"), 1);
    }

    @Test
    void TestGiveChangeFail() {
        provideInput("-1\n");
        HashMap<String, Integer> change = paymentSystem.giveChange(40.0, 42.8);
        assertEquals(change.get("paymentAccept"), 0);
    }

    @Test
    void getCashInMachine() {
            paymentSystem.initializeCash("1", 1);
            paymentSystem.getCashInMachine();
            assertEquals(1, paymentSystem.getCashInMachine().get("1"));

    }

    @Test
    void inputNextCommand() {
    }

//    @Test
//    void updateJSONFile() {
//
//        PaymentSystem paymentSystem = new PaymentSystem();
//        paymentSystem.initializeCash("1", 1);
//        paymentSystem.updateJSONFile();
//        assertEquals(1, paymentSystem.getCashInMachine().get("1"));
//    }
}