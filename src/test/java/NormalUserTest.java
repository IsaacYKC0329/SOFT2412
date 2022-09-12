import org.junit.jupiter.api.Test;

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