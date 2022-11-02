import Vending_Machine.User;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void addLastBought5Test1() {
        User user = new User("seller3", "abcdefg", "seller");
        user.addLastBought5("luoshi noodle");


        assertEquals(1, user.getLastBought5().size());
    }

    @Test
    void addLastBought5Test2() {

        User user = new User("seller3", "abcdefg", "seller");
        user.addLastBought5("luoshi noodle");
        user.addLastBought5("luoshi noodle");
        assertEquals(1, user.getLastBought5().size());

    }



    @Test
    void readLastBought5FromFile() {

        User user = new User("seller3", "seller3", "seller");
        user.readLastBought5FromFile();
        assertEquals(1, user.getLastBought5().size());

    }

    @Test
    void writeLastBought5ToFile() throws IOException {


        User user = new User("seller3", "abcdefg", "seller");
        user.addLastBought5("luoshi noodle");
        user.writeLastBought5ToFile();
        File file = new File("seller3LastBought5.txt");
        assertEquals(true, file.exists());

    }


    @Test
    void TestreadfileExist(){


        User user = new User("seller3", "seller3", "seller");
        user.readLastBought5FromFile();
        assertEquals(1, user.getLastBought5().size());


    }

    @Test
    void getLastBought5() {

        User user = new User("seller3", "seller3", "seller");
        user.readLastBought5FromFile();
        assertEquals(1, user.getLastBought5().size());
    }

    @Test
    void getUsername() {

        User user = new User("seller3", "abcdefg", "seller");
        assertEquals("seller3", user.getUsername());

    }

    @Test
    void setUsername() {

        User user = new User("cashier4", "qwerasdf", "cashier");
        user.setUsername("cashier4");
        assertEquals("cashier4", user.getUsername());
    }

    @Test
    void getPassword() {

        User user = new User("cashier4", "qwerasdf", "cashier");
        assertEquals("qwerasdf", user.getPassword());
    }

    @Test
    void setPassword() {

        User user = new User("seller10", "usydsoft2201", "seller");
        user.setPassword("usydsoft2201");
        assertEquals("usydsoft2201", user.getPassword());
    }

    @Test
    void getRole() {

        User user = new User("cashier20", "usydsoft2201", "cashier");
        assertEquals("cashier", user.getRole());
    }

    @Test
    void setRole() {

        User user = new User("seller2", "123", "user");
        user.setRole("seller2");
        assertEquals("seller2", user.getRole());
    }
}