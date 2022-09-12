import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public abstract class User {
    protected static ArrayList<Currency> currencyList = new ArrayList<>();
    protected Currency[] popularCurrencies = new Currency[4];

    protected void moneyExchange(String c1, String c2, String number) {
        for (Currency c : currencyList) {
            if (Objects.equals(c.getName(), c1)) {
                if (c.getExchangeRateList().containsKey(c2)) {
                    double rate = Double.parseDouble(c.getExchangeRateList().get(c2).get(0)[1]);
                    double result = Double.parseDouble(number)*rate;
                    System.out.println("You have changed "+c2+result);
                } else {
                    System.out.println("No such a currency, please add it into system");
                }
            }
        }
    }

    protected String strRightPadding(String str) {
        return String.format("%1$-" + 8 + "s", str);
    }
    protected String doubleRightPadding(double value) {
        return String.format("%1$-" + 8 + ".2f", value);
    }

    protected void showPopular() {
        if (popularCurrencies.length!=4) {
            System.out.println("Admin should pick 4 currency first");
        } else {
            Currency c1 = popularCurrencies[0];
            Currency c2 = popularCurrencies[1];
            Currency c3 = popularCurrencies[2];
            Currency c4 = popularCurrencies[3];
            String c1str = strRightPadding(c1.getName());
            String c2str = strRightPadding(c2.getName());
            String c3str = strRightPadding(c3.getName());
            String c4str = strRightPadding(c4.getName());
            String c1toC2 = doubleRightPadding(c1.getExchangeRate(c2.getName()));
            String c1toC3 = doubleRightPadding(c1.getExchangeRate(c3.getName()));
            String c1toC4 = doubleRightPadding(c1.getExchangeRate(c4.getName()));
            String c2toC1 = doubleRightPadding(c2.getExchangeRate(c1.getName()));
            String c2toC3 = doubleRightPadding(c2.getExchangeRate(c3.getName()));
            String c2toC4 = doubleRightPadding(c2.getExchangeRate(c4.getName()));
            String c3toC1 = doubleRightPadding(c3.getExchangeRate(c1.getName()));
            String c3toC2 = doubleRightPadding(c3.getExchangeRate(c2.getName()));
            String c3toC4 = doubleRightPadding(c3.getExchangeRate(c4.getName()));
            String c4toC1 = doubleRightPadding(c4.getExchangeRate(c1.getName()));
            String c4toC2 = doubleRightPadding(c4.getExchangeRate(c2.getName()));
            String c4toC3 = doubleRightPadding(c4.getExchangeRate(c3.getName()));

            System.out.printf("""
               ______________________________________________
               |From/To |%s|%s|%s|%s|
               |%s|-       |%s|%s|%s|
               |%s|%s|-       |%s|%s|
               |%s|%s|%s|-       |%s|
               |%s|%s|%s|%s|-       |
               ----------------------------------------------
                """,c1str,c2str,c3str,c4str,c1str,c1toC2,c1toC3,c1toC4,c2str,c2toC1,c2toC3,c2toC4,c3str,c3toC1,c3toC2,c3toC4,c4str,c4toC1,c4toC2,c4toC3);

        }
    }

    protected void printList(String startTime, String endTime, Currency c1, Currency c2) {

        ArrayList<String[]> c2HistoryRates = c1.getExchangeRateList().get(c2.getName());
        int startIndex = 0;
        int endIndex = 0;
        boolean start = false;
        boolean end = false;
        double sum = 0;
        double average;
        double median;
        double standardDeviation;
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        sort(c2HistoryRates);
        for(int i=0; i<c2HistoryRates.size(); i++){
            String Date = c2HistoryRates.get(i)[0];
            double Rate = Double.parseDouble(c2HistoryRates.get(i)[1]);

            // find the index of start date and end date
            if(Date.equals(startTime)){
                startIndex = i;
                start = true;
            }
            else if(Date.equals(endTime)){
                endIndex = i;
                end = true;
            }

            // start printing
            if(start){
                //calculate the sum for average
                sum+=Rate;

                // calculate the max value
                if(Rate > max){
                    max = Rate;
                }
                // calculate the min value
                if(Rate < min){
                    min = Rate;
                }

                //print out every history rate between specific time
                System.out.println(Date + ": " + Rate);

                //stop printing
                if(end){
                    start = false;
                }
            }
        }
        // calculate the average and median
        average = sum / (endIndex+1 - startIndex);
        median = Double.parseDouble(c2HistoryRates.get((endIndex+startIndex)/2)[1]);

        // calculate standard deviation
        double[] values = new double[endIndex+1 - startIndex];
        for(int i = startIndex, j = 0; i < endIndex+1; i++, j++){
            double Rate = Double.parseDouble(c2HistoryRates.get(i)[1]);
            values[j] = Math.pow((Rate - average), 2);
        }
        double averageOfSumOfValue = Arrays.stream(values).sum()/(endIndex+1 - startIndex);
        standardDeviation = Math.sqrt(averageOfSumOfValue);
        System.out.printf("""
                average is: %f
                median is: %f
                maximum is: %f
                minimum is: %f
                standard deviation is: %f
                """, average, median, max, min, standardDeviation);
    }

    protected abstract void addCurrency(String name);
    protected abstract void changeRate(String c1,  String c2, String rate, String date);
    protected abstract void choosePopular(String c1, String c2, String c3, String c4);

    protected Currency findCurrency(String c){
        for (Currency currency : currencyList) {
            if (c.equals(currency.getName())) {
                return currency;
            }
        }
        return null;
    }

    protected void sort(ArrayList<String[]> arr){
        for(int i=0; i<arr.size()-1; i++) {
            for(int j=i+1; j<arr.size(); j++) {
                if(arr.get(i)[0].compareTo(arr.get(j)[0])<0) {
                    Collections.swap(arr, i, j);
                }
            }
        }
    }
}
