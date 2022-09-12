import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class NormalUserTest {


    @Test
    void addCurrency() {
        NormalUser normalUser = new NormalUser();
        normalUser.addCurrency("AUD");
        normalUser.addCurrency("USD");
        assertEquals(normalUser.currencyList.size(), 2);
    }

    @Test
    void changeRate() {
        NormalUser normalUser = new NormalUser();
        normalUser.addCurrency("AUD");
        normalUser.addCurrency("USD");
        normalUser.changeRate("AUD", "USD", "0.8", "2019-01-01");
        assertEquals(normalUser.currencyList.get(0).getExchangeRate("USD"), 0.8);
    }

    @Test
    void choosePopular() {
        User normalUser = new NormalUser();
        normalUser.addCurrency("AUD");
        normalUser.addCurrency("USD");
        normalUser.addCurrency("CAD");
        normalUser.addCurrency("EUR");
        normalUser.choosePopular("AUD", "USD", "CAD", "EUR");
        assertEquals(normalUser.currencyList.get(0).getName(), "AUD");
        assertEquals(normalUser.currencyList.get(1).getName(), "USD");
        assertEquals(normalUser.currencyList.get(2).getName(), "CAD");
        assertEquals(normalUser.currencyList.get(3).getName(), "EUR");
    }
}



//import org.junit.Before;
//        import org.junit.jupiter.api.AfterEach;
//        import org.junit.jupiter.api.BeforeEach;
//        import org.junit.jupiter.api.Test;
//        import java.io.ByteArrayOutputStream;
//        import java.io.PrintStream;
//        import java.util.ArrayList;
//
//        import java.io.PrintStream;
//
//        import static org.junit.jupiter.api.Assertions.*;
//
//class NormalUserTest {
//
//    User NormalUser;
//
//    private final PrintStream standardOut = System.out;
//    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
//
//    @BeforeEach
//    void setUp() {
//        this.NormalUser = new NormalUser();
//        System.setOut(new PrintStream(outputStreamCaptor));
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        this.NormalUser = null;
//    }
//
//    @Test
//    void addCurrency() {
//        User normalUser = new NormalUser();
//        int currencyListLengthA = User.currencyList.size();
//        assertEquals(currencyListLengthA, 0);
//
//
//    }
//
//    @Test
//    void changeRateTest() {
//        // Test invalid change
//        User normalUser = new NormalUser();
//        normalUser.changeRate("AUD", "XYZ", "50", "2022/12/25");
//        assertEquals("No such a currency, please add it into system", outputStreamCaptor.toString()
//                .trim());
//
//    }
//
////    @Test
////    void choosePopularTest() {
////        User normalUser = new NormalUser();
////        normalUser.choosePopular("AUD", "USD","40" ,"2022/12/25");
////        assertEquals("AUD", outputStreamCaptor.toString()
////                .trim());
////    }
//
//
//
//    @Test
//    void choosePopularTest() {
//        User NormalUser = new NormalUser();
//        NormalUser.addCurrency("CNY");
//        NormalUser.addCurrency("AUD");
//        NormalUser.addCurrency("USD");
//        NormalUser.addCurrency("EUR");
//        NormalUser.choosePopular("CNY", "AUD", "USD", "EUR");
//        assertEquals("CNY", NormalUser.popularCurrencies[0].getName());
//
//        // tear down
//        NormalUser.currencyList = new ArrayList<>();
//        NormalUser.popularCurrencies = new Currency[4];
//    }
//}
