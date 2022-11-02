import Vending_Machine.Category;
import Vending_Machine.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void TestGetName() {
        Category category = new Category("instant noodle");
        assertEquals("instant noodle", category.getName());
    }

    @Test
    void TestSetName() {
        Category category = new Category("sausage");
        category.setName("sausage");
        assertEquals("sausage", category.getName());
    }

    @Test
    void TestSetProductsA() {
        Category category = new Category("instant noodle");
        assertEquals(0, category.getProducts().size());
    }


    @Test
    void TestAddProduct() {
        Category category = new Category("self heat hot pot");
        Product product = new Product("2033", "self heat hot pot", category, 1, 1, 1);
        category.addProduct(product);
        assertEquals(1, category.getProducts().size());
    }

    @Test
    void TestSetProductsB() {

        List<Product> products = List.of(new Product("2044", "self heat hot pot", new Category("instant food"), 1, 1, 1));
        Category category = new Category("self heat hot pot");
        category.setProducts(products);
        assertEquals(1, category.getProducts().size());

    }
}