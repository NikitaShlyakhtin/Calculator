import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Select calculator");
            System.out.println("0 - Basic Calculator (arithmetic calculations)");
            Calculator calculator;
            int selectedType = scanner.nextInt();
            scanner.nextLine();
            switch (selectedType) {
                case 0:
                    calculator = new BasicCalculator();
                    calculator.work(scanner);
                    break;
                default: System.out.println("Not a valid type of calculator");
            }
        }
        catch (InvalidOperationException e) {
            e.getMessage();
        }
    }
}