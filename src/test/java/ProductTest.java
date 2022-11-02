import Vending_Machine.Category;
import Vending_Machine.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTest {

    @Test
    void getId() {

        Product product = new Product("2033", "self heat hot pot", new Category("instant food"), 1, 1, 1);
        assertEquals("2033", product.getId());
    }

    @Test
    void setId() {
        Product product = new Product("2044", "instant noodle", new Category("instant food"), 1, 1, 1);
        product.setId("2044");
        assertEquals("2044", product.getId());
    }

    @Test
    void getName() {
        Product product = new Product("2055", "sausage", new Category("instant food"), 1, 1, 1);
        assertEquals("sausage", product.getName());
    }

    @Test
    void setName() {
        Product product = new Product("2066", "wuqiong chicken", new Category("instant food"), 1, 1, 1);
        product.setName("wuqiong chicken");
        assertEquals("wuqiong chicken", product.getName());
    }

    @Test
    void getCategory() {
        Product product = new Product("2066", "wuqiong chicken", new Category("instant food"), 1, 1, 1);
        assertEquals("instant food", product.getCategory().getName());
    }

    @Test
    void setCategory() {
        Product product = new Product("2077", "wuqiong duck", new Category("instant food"), 1, 1, 1);
        product.setCategory(new Category("instant food"));
        assertEquals("instant food", product.getCategory().getName());

    }

    @Test
    void getPrice() {
        Product product = new Product("2088", "luoshi noodle", new Category("instant food"), 10, 1, 1);
        assertEquals(10, product.getPrice());
    }

    @Test
    void setPrice() {
        Product product = new Product("2088", "luoshi noodle", new Category("instant noodle"), 10, 1, 1);
        product.setPrice(12);
        assertEquals(12, product.getPrice());

    }

    @Test
    void getQuantity() {
        Product product = new Product("2088", "luoshi noodle", new Category("instant noodle"), 1, 5, 1);
        assertEquals(5, product.getQuantity());
    }

    @Test
    void setQuantity() {
        Product product = new Product("2099", "yuanqi forest", new Category("Drink"), 1, 1, 1);
        product.setQuantity(3);
        assertEquals(3, product.getQuantity());
    }

    @Test
    void getQuantitySold() {
        Product product = new Product("2099", "yuanqi forest", new Category("Drink"), 1, 1, 1);
        assertEquals(1, product.getQuantitySold());
    }

    @Test
    void setQuantitySold() {
        Product product = new Product("2099", "yuanqi forest", new Category("Drink"), 1, 1, 0);
        product.setQuantitySold(3);
        assertEquals(3, product.getQuantitySold());
    }
}