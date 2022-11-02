package Vending_Machine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    private String username;
    private String password;
    private String role;

    private List<String> lastBought5 = new ArrayList<>();

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // add last bought 5 products to user
    public void addLastBought5(String productName){
        // read the last bought 5 products history from file first
        for (String s : lastBought5) {
            if (s.equals(productName)) {
                return;
            }
        }
        if (lastBought5.size() < 5){
            lastBought5.add(productName);
        } else {
            lastBought5.remove(0);
            lastBought5.add(productName);
        }
        writeLastBought5ToFile();
    }

    public void writeLastBought5ToFile() {
        try {
            File file = new File(this.username+"LastBought5.txt");
            // if no such a txt file, create a new transactionHistory.txt
            if (!file.exists()) {
                file.createNewFile();
            }
            // overwrite the txt file
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String product : lastBought5) {
                bufferedWriter.write(product);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read last bought 5 products from file
    public void readLastBought5FromFile() {
        try {
            File file = new File(this.username+"LastBought5.txt");
            // if no such a txt file, create a new LastBought5.txt
            if (!file.exists()) {
                file.createNewFile();
                return;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String item = scanner.nextLine();
                if (item.equals("")) {
                    break;
                } else {
                    lastBought5.add(item);
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLastBought5() {
        return lastBought5;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
