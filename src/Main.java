import calculatorApp.CalculatorApp;
import calculators.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

enum CalculatorTypes {
    BASIC,
    CURRENCY,
    DND_ENCOUNTER,
    DND_POINT_BUY
}

public class Main {
    public static void main(String[] args) throws IOException {
        CalculatorApp calculatorApp = new CalculatorApp();

        List<String> welcomeMessage = Arrays.asList("Welcome to Calculator!",
                "Please, select calculator:",
                "0 - Basic calculator",
                "1 - Currency calculator",
                "2 - DnD Encounter Difficulty calculator",
                "3 - DnD Point-buy calculator");

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
            case DND_ENCOUNTER -> {
                calculator = new DnDEncounterDifficultyCalculator();
                calculator.work(calculatorApp);
            }
            case DND_POINT_BUY -> {
                calculator = new DnDPointBuyCalculator();
                calculator.work(calculatorApp);
            }
            default -> calculatorApp.println("Not a valid type of calculator");
        }

    }
}