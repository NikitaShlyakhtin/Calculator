package calculators;
import calculatorApp.CalculatorApp;

import java.io.IOException;

public class BMICalculator implements Calculator {
    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Enter height in cm: ");
        double height = 0;
        do {
            try {
                height = Double.parseDouble(calculatorApp.readLine());
            } catch (Exception e) {
                calculatorApp.println("Not a number, try again");
            }
        } while (height == 0);
        calculatorApp.println("Enter weight in kg: ");
        double weight = 0;
        do {
            try {
                weight = Double.parseDouble(calculatorApp.readLine());
            } catch (Exception e) {
                calculatorApp.println("Not a number, try again");
            }
        } while (weight == 0);
        Human human = new Human(height, weight);
        human.interpret(calculatorApp);
        calculatorApp.readLine();
    }
}

class Human {
    private final double BMI;

    public Human(double height, double weight) {
        double heightInMeters = height / 100.0;
        BMI = weight / (heightInMeters * heightInMeters);
    }

    private String interpretBMI() {
        if (BMI < 18.5) {
            return "Underweight";
        } else if (BMI >= 18.5 && BMI < 25) {
            return "Normal weight";
        } else if (BMI >= 25 && BMI < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public void interpret(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("BMI = " + BMI);
        calculatorApp.println("Interpretation = " + interpretBMI());
    }
}
