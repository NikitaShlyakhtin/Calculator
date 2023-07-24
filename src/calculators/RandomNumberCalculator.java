package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;
import java.util.Random;

public class RandomNumberCalculator implements Calculator{
    @Override
    public void work(CalculatorApp calculatorApp) throws IOException {
        Dice dice = new Dice(calculatorApp);
        calculatorApp.println("Press ENTER to generate, input EXIT to exit");
        while (!calculatorApp.readLine().trim().equals("EXIT")) {
            dice.roll(calculatorApp);
        }
    }
}

class Dice {
    private int lowBorder;
    private int highBorder;

    public Dice(CalculatorApp calculatorApp) throws IOException {
        do {
            calculatorApp.println("Input minimum value");
            int value;
            do {
                try {
                    value = Integer.parseInt(calculatorApp.readLine());
                } catch (Exception e) {
                    value = Integer.MAX_VALUE;
                    calculatorApp.println("Malformed input, try again");
                }
            } while (value == Integer.MAX_VALUE);
            lowBorder = value;
            calculatorApp.println("Input maximum value");
            do {
                try {
                    value = Integer.parseInt(calculatorApp.readLine());
                } catch (Exception e) {
                    value = Integer.MIN_VALUE;
                    calculatorApp.println("Malformed input, try again");
                }
            } while (value == Integer.MIN_VALUE);
            highBorder = value;
            if (highBorder < lowBorder) {
                calculatorApp.println("The maximum value cannot be smaller than minimum, try again");
            }
        } while (highBorder < lowBorder);
    }

    public void roll(CalculatorApp calculatorApp) throws IOException {
        Random random = new Random();
        calculatorApp.println("Result = " + (random.nextInt(highBorder - lowBorder + 1) + lowBorder));
    }
}