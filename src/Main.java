import calculatorApp.CalculatorApp;
import calculators.BasicCalculator;
import calculators.Calculator;
import calculators.CurrencyCalculator;
import calculators.InvalidOperationException;
import com.googlecode.lanterna.TextColor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

enum CalculatorTypes {
    BASIC,
    CURRENCY
}

public class Main {
    public static void main(String[] args) throws IOException, InvalidOperationException {
        CalculatorApp calculatorApp = new CalculatorApp();

        List<String> welcomeMessage = Arrays.asList("Welcome to Calculator!",
                "Please, select calculator:",
                "0 - Basic calculator",
                "1 - Currency calculator");

        for (String line : welcomeMessage) {
            calculatorApp.println(line);
        }

        CalculatorTypes calculatorType = CalculatorTypes.values()[Integer.parseInt(calculatorApp.readLine())];
        Calculator calculator;

        switch (calculatorType) {
            case BASIC -> {
                calculator = new BasicCalculator();
                calculator.work(calculatorApp);
            }
            case CURRENCY -> {
                calculator = new CurrencyCalculator();
                calculator.work(calculatorApp);
            }
            default -> System.out.println("Not a valid type of calculator");
        }

    }
}