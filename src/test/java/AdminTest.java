import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    User admin;

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        this.admin = new Admin();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        this.admin = null;
    }

    @Test
    void addCurrencyTest() {

        // Test the length of the currency list before adding
        int currencyListLengthA = admin.currencyList.size();
        assertEquals(0, currencyListLengthA);

        // Add two currencies to the list
        admin.addCurrency("AUD");
        admin.addCurrency("USD");

        // Test the length of the currency list after adding
        int currencyListLengthB = admin.currencyList.size();
        assertEquals(currencyListLengthB, 2);
    }

    @Test
    void changeRateTest() {
        // Test invalid change
        admin.changeRate("AUD", "XYZ", "50", "2022/12/25");
        assertEquals("No such a currency, please add it into system", outputStreamCaptor.toString()
                .trim());

        // Test after changing the rate
        admin.addCurrency("CNY");
        admin.addCurrency("AUD");
        admin.changeRate("CNY", "AUD", "1", "2022/11/11");

        Currency cny = new Currency("CNY");
        assertEquals(cny.getExchangeRate("AUD"), 1);
    }

    @Test
    void choosePopularTest() {
        admin.addCurrency("CNY");
        admin.addCurrency("AUD");
        admin.addCurrency("USD");
        admin.addCurrency("EUR");
        admin.choosePopular("CNY", "AUD", "USD", "EUR");
        assertEquals("CNY", admin.popularCurrencies[0].getName());

        // tear down
        admin.currencyList = new ArrayList<>();
    }
}
