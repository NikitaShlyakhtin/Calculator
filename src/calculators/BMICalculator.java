package calculators;
import calculatorApp.CalculatorApp;

import java.io.IOException;

public class BMICalculator implements Calculator {
    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Enter height in cm: ");
        double height = Double.parseDouble(calculatorApp.readLine());
        calculatorApp.println("Enter weight in kg: ");
        double weight = Double.parseDouble(calculatorApp.readLine());
        Human human = new Human(height, weight);
        calculatorApp.println("BMI: " + human.BMI);
        calculatorApp.println("Interpretation: " + human.interpretBMI());
    }
}

class Human {
    public final double BMI;

    public Human(double height, double weight) {
        double heightInMeters = height / 100.0;
        BMI = weight / (heightInMeters * heightInMeters);
    }

    public String interpretBMI() {
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
}
