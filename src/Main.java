import calculators.BasicCalculator;
import calculators.Calculator;
import calculators.CurrencyCalculator;
import calculators.InvalidOperationException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Select calculator");
            System.out.println("0 - Basic calculators.Calculator (arithmetic calculations)");
            System.out.println("1 - Currency calculator");

            Calculator calculator;

            int selectedType = scanner.nextInt();
            scanner.nextLine();

            switch (selectedType) {
                case 0 -> {
                    calculator = new BasicCalculator();
                    calculator.work(scanner);
                }
                case 1 -> {
                    calculator = new CurrencyCalculator();
                    calculator.work(scanner);
                }
                default -> System.out.println("Not a valid type of calculator");
            }
        }
        catch (InvalidOperationException e) {
            e.getMessage();
        }
    }
}