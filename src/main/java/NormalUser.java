
public class NormalUser extends User{

    @Override
    protected void addCurrency(String name) {
        Currency c1 = new Currency(name);
        currencyList.add(c1);

    }

    @Override
    protected void changeRate(String c1,  String c2, String rate, String date) {
        for(Currency currency : currencyList){
            if(currency.getName().equals(c1)){
                currency.addExchangeRate(c2, rate, date);
            }
        }
    }

    @Override
    protected void choosePopular(String c1, String c2, String c3, String c4) {
        for(Currency currency : currencyList){
            if(currency.getName().equals(c1)){
                currencyList.remove(currency);
                currencyList.add(0, currency);
            }
            if(currency.getName().equals(c2)){
                currencyList.remove(currency);
                currencyList.add(1, currency);
            }
            if(currency.getName().equals(c3)){
                currencyList.remove(currency);
                currencyList.add(2, currency);
            }
            if(currency.getName().equals(c4)){
                currencyList.remove(currency);
                currencyList.add(3, currency);
            }

        }

    }
}

