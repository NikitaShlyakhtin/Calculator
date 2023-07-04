package calculators;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

class Currency {
    public final String code;
    public final double rate;

    public Currency(String code, double rate) {
        this.code = code;
        this.rate = rate;
    }

    public double convertTo(double amount, Currency targetCurrency) {
        double rubAmount = amount / rate;
        return rubAmount * targetCurrency.rate;
    }
}

public class CurrencyCalculator implements Calculator{
    @Override
    public void work(Scanner scanner) {
        System.out.println("Available operations: <n> rub to usd," +
                " <n> rub to eur, <n> usd to rub, <n> usd to eur, <n> eur to rub, <n> eur to usd.");
        System.out.println("Please, input operands separated by space");
        System.out.println("To stop working, write STOP");

        Map<String, Currency> currencies = new HashMap<>();
        currencies.put("USD", new Currency("USD", 1.0));
        currencies.put("EUR", new Currency("EUR", 0.85));
        currencies.put("RUB", new Currency("RUB", 74.0));

        String input = scanner.nextLine();

        while (!input.equals("STOP")) {
            String[] inputArray = input.split(" ");
            double amount = Double.parseDouble(inputArray[0]);

            Currency sourceCurrency = currencies.get(inputArray[1].toUpperCase());
            Currency targetCurrency = currencies.get(inputArray[3].toUpperCase());

            if (sourceCurrency != null && targetCurrency != null) {
                double result = sourceCurrency.convertTo(amount, targetCurrency);
                System.out.print(amount + " " + sourceCurrency.code + " = " + result + " " + targetCurrency.code);
            } else {
                System.out.println("Invalid currency pair.");
            }

            input = scanner.nextLine();
        }
    }
}
