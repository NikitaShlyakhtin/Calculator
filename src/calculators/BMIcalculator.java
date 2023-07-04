//package calculators;
//
//import java.util.Scanner;
//
//public class BMIcalculator {
//    private double height;
//    private double weight;
//
//    public BMIcalculator(double height, double weight) {
//        this.height = height;
//        this.weight = weight;
//
//    }
//    public BMIcalculator() {}
//
//    public double calculateBMI() {
//        double heightInMeters = height / 100.0;
//        return weight / (heightInMeters * heightInMeters);
//    }
//
//    public String interpretBMI() {
//        double bmi = calculateBMI();
//        if (bmi < 18.5) {
//            return "Underweight";
//        } else if (bmi >= 18.5 && bmi < 25) {
//            return "Normal weight";
//        } else if (bmi >= 25 && bmi < 30) {
//            return "Overweight";
//        } else {
//            return "Obese";
//        }
//    }
//    void work(Scanner scanner) {
//        System.out.println("Enter height in cm: ");
//        height = scanner.nextDouble();
//        System.out.println("Enter weight in kg: ");
//        weight = scanner.nextDouble();
//        BMIcalculator calculator = new BMIcalculator(height, weight);
//        double bmi = calculator.calculateBMI();
//        String interpretation = calculator.interpretBMI();
//
//        System.out.println("BMI: " + bmi);
//        System.out.println("Interpretation: " + interpretation);
//    }
//}
