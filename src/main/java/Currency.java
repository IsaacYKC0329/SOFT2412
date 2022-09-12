import java.util.*;

public class Currency {
    private String name;

    private Map<String, ArrayList<String[]>> exchangeRateList = new HashMap<>();

    public Currency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, ArrayList<String[]>> getExchangeRateList() {
        return exchangeRateList;
    }

    public double getExchangeRate(String name) {
        double exchangeRate = 1;
        for (String key : exchangeRateList.keySet()) {
            if (Objects.equals(key, name)) {
                exchangeRate = Double.parseDouble(exchangeRateList.get(name).get(0)[1]);
            }
        }
        return exchangeRate;
    }

    public void addExchangeRate(String name, String rate, String date) {
        ArrayList<String[]> dateAndRateList = new ArrayList<>();
        dateAndRateList.add(new String[]{date, rate});
        exchangeRateList.put(name, dateAndRateList);
    }
}
