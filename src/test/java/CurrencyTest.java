import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyTest {

    @Test
    void getName() {
        Currency currency = new Currency("AUD");
        assertEquals(currency.getName(), "AUD");

    }

    @Test
    void getExchangeRateList() {
        Currency currency = new Currency("AUD");
        assertEquals(currency.getExchangeRateList().size(), 0);

    }

    @Test
    void getExchangeRate() {
        Currency currency = new Currency("USD");
        currency.addExchangeRate("USD", "0.8", "2019-01-01");

        assertEquals(currency.getExchangeRate("USD"), 0.8);


    }

    @Test
    void addExchangeRate() {
        Currency currency = new Currency("AUD");
        currency.addExchangeRate("USD", "0.8", "2019-01-01");
        assertEquals(currency.getExchangeRateList().size(), 1);
    }
}