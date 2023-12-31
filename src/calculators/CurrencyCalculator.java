package calculators;

import calculatorApp.CalculatorApp;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Currency {
    public final String code;
    private final double rate;

    public Currency(String code) {
        this.code = code;
        this.rate = fetchRate();
    }

    public double convertTo(double amount, Currency targetCurrency) {
        double rubAmount = amount / rate;
        return rubAmount * targetCurrency.rate;
    }

    private double fetchRate() {
        Unirest.config().defaultBaseUrl("https://open.er-api.com/v6/latest");
        Object response = Unirest.get("/RUB")
                .asJson()
                .getBody()
                .getObject()
                .getJSONObject("rates")
                .get(code);
        if (response instanceof Double) {
            return (double) response;
        } else if (response instanceof Integer) {
            return ((Integer) response).doubleValue();
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + response.getClass());
        }
    }
}

public class CurrencyCalculator implements Calculator {
    @Override
    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Available operations: <n> rub to usd," +
                " <n> rub to eur, <n> usd to rub, <n> usd to eur, <n> eur to rub, <n> eur to usd.");
        calculatorApp.println("Please, input operands separated by space");
        calculatorApp.println("To stop working, write STOP");

        Map<String, Currency> currencies = new HashMap<>();
        currencies.put("USD", new Currency("USD"));
        currencies.put("EUR", new Currency("EUR"));
        currencies.put("RUB", new Currency("RUB"));

        String input = calculatorApp.readLine();

        while (!input.equals("STOP")) {
            try {
                String[] inputArray = input.split(" ");
                double amount = Double.parseDouble(inputArray[0]);

                Currency sourceCurrency = currencies.get(inputArray[1].toUpperCase());
                Currency targetCurrency = currencies.get(inputArray[3].toUpperCase());

                if (sourceCurrency != null && targetCurrency != null) {
                    String result = String.format("%.02f", sourceCurrency.convertTo(amount, targetCurrency));
                    calculatorApp.print(" | " + result + " " + targetCurrency.code);
                } else {
                    calculatorApp.println("Invalid currency pair.");
                }
            } catch (Exception e) {
                calculatorApp.println("Input is malformed, try again");
            }
            input = calculatorApp.readLine();
        }
    }
}
