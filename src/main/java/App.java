
import java.util.Scanner;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class App {
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader("src/main/resources/config.json"));
            // Convert Object to JSONObject
            JSONObject jsonObject = (JSONObject) object;

            // Reading the currencies array
            JSONArray jsonCurrencyList = (JSONArray) jsonObject.get("Currencies");
            for (Object currency : jsonCurrencyList) {
                JSONObject jsonCurrency = (JSONObject) currency;
                // Reading the currency id
                String currencyName = (String) jsonCurrency.get("id");
                // Creating a new currency by using currency name read above
                Currency c = new Currency(currencyName);
                User.currencyList.add(c);
                // Reading the exchange rate array of this currency
                JSONArray exchangeRateList = (JSONArray) jsonCurrency.get("ExchangeRate");
                for (Object exchangeRate : exchangeRateList) {
                    JSONObject jsonExchangeRate = (JSONObject) exchangeRate;
                    // Reading the exchange currency name
                    String name = (String) jsonExchangeRate.get("name");
                    // Reading the exchange rate
                    String rate = (String) jsonExchangeRate.get("rate");
                    // Reading the date of this rate
                    String date = (String) jsonExchangeRate.get("date");
                    // add these argument into currency's exchange rate list
                    c.addExchangeRate(name, rate, date);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        Admin admin = new Admin();
        NormalUser normalUser = new NormalUser();
        User user = null;

        // print command manual
        String manual = """
                   COMMAND(with uppercase):
                       Permission:
                         ADMIN: can use common and admin function
                         NORMAL: can only use common function
                       
                       Common function(for admin and normal user):
                         EXCHANGE: View the exchange rate of a specified currency
                         SHOWPOPULAR: Showing the four most popular currency rates
                         PRINTLIST <start date> <end date> <currency1> <currency2>: Print a summary of exchange rates for selected 2 currencies for a specific duration (start and end date)
                         
                       Admin function:
                         ADDCURRENCY <name>: add currency
                         CHANGERATE <name> <name> <date> <rate>: change the rate of a currency to another one
                         CHOOSEPOPULAR <name> <name> <name> <name>: choose four currency to be in the popular list, using SHOWPOPULAR command to show list
                    
                       HELP: print this manual
                       
                       EXIT: exit program
                    """;
        System.out.println(manual);

        // choose user
        Scanner sc = new Scanner(System.in);
        boolean chosen = false;
        while(!chosen){
            System.out.print("choose user(admin or normal user): "); // 打印提示
            String permission = sc.nextLine().toUpperCase();
            switch (permission) {
                case "ADMIN" -> {user = admin;
                    chosen = true;}
                case "NORMAL" -> {user = normalUser;
                    chosen = true;}
                case "HELP" -> System.out.println(manual);
                case "EXIT" -> System.exit(0);
                default -> System.out.println("choose user first!!! (or EXIT/HELP)");
            }
        }

        // main body
        while(true){

            System.out.print("Input your command: "); // 打印提示
            String COMMAND = sc.nextLine().toUpperCase();
            String [] parameter = COMMAND.split(" ");
            COMMAND = parameter[0];
            
            if(COMMAND.equals("ADMIN")){
                user = admin;
                System.out.println("admin mode");
            }else if (COMMAND.equals("NORMAL")){
                user = normalUser;
                System.out.println("normal mode");
            }else if (COMMAND.equals("EXIT")){
                System.exit(0);
            }else if (COMMAND.equals("HELP")){
                System.out.println(manual);
            }else if (COMMAND.equals("EXCHANGE")){
                user.moneyExchange(parameter[1], parameter[2], parameter[3]);
            }else if (COMMAND.equals("SHOWPOPULAR")){
                user.showPopular();
            }else if (COMMAND.equals("PRINTLIST")){
                Currency c1 = user.findCurrency(parameter[3]);
                Currency c2 = user.findCurrency(parameter[4]);
                // take start date and end date and two currency as argument
                user.printList(parameter[1], parameter[2], c1, c2);
            }
            else if(user == admin) {
                switch (COMMAND) {
                    //take currency name as argument
                    case "ADDCURRENCY" -> user.addCurrency(parameter[1]);
                    //take currency name and rate as argument
                    case "CHANGERATE" -> user.changeRate(parameter[1], parameter[2], parameter[3], parameter[4]);
                    // take four currencies name as arguments
                    case "CHOOSEPOPULAR" -> user.choosePopular(parameter[1], parameter[2], parameter[3], parameter[4]);
                }
            }
            else {
                   System.out.println("No permission");
            }
        }
    }
}
