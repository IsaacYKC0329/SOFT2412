import java.util.Objects;

public class Admin extends User{
    protected void addCurrency(String name){
        Currency c1 = new Currency(name);
        currencyList.add(c1);
        System.out.println(name + " has been added!");
    }

    protected void changeRate(String c1, String c2, String date, String rate){
        for (Currency c : currencyList) {
            if (Objects.equals(c.getName(), c1)) {
                if (c.getExchangeRateList().containsKey(c2)) {
                    c.getExchangeRateList().get(c2).add(new String[] {date, rate});
                    sort(c.getExchangeRateList().get(c2));
                } else {
                    System.out.println("No such a currency, please add it into system");
                }
            }
        }
    }

    protected void choosePopular(String c1, String c2, String c3, String c4) {
        String[] c = new String[]{c1, c2, c3, c4};
        for(int i = 0; i < c.length; i++){
            popularCurrencies[i] = findCurrency(c[i]);
        }
    }
}
