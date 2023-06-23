import java.util.Scanner;
import java.util.Stack;

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

interface Calculator {
    void work(Scanner scanner) throws InvalidOperationException;
}

class BasicCalculator implements Calculator {

    @Override
    public void work(Scanner scanner) throws InvalidOperationException {
        System.out.println("Available operations: \"+\", \"-\", \"*\", \"/\", \"min ( a , b )\", \"max ( a , b )\"");
        System.out.println("Please, input operands separated by space");
        System.out.println("To stop working, write STOP");
        String input = scanner.nextLine();
        while (!input.equals("STOP")) {
            Stack<String> shunt = new Stack<>();
            String reorganisedExpression = "";
            Scanner expression = new Scanner(input);
            while (expression.hasNext()) {
                String elem = expression.next();
                if (elem.matches("\\d")) {
                    reorganisedExpression = reorganisedExpression.concat(elem + " ");
                }
                else {
                    if (elem.equals(",")) {
                        while (!shunt.peek().equals("(")) {
                            reorganisedExpression = reorganisedExpression.concat(shunt.pop() + " ");
                        }
                    }
                    if (elem.equals(")")) {
                        while (!shunt.peek().equals("(")) {
                            reorganisedExpression = reorganisedExpression.concat(shunt.pop() + " ");
                        }
                        shunt.pop();
                        if (!shunt.isEmpty()) {
                            if (shunt.peek().equals("max") || shunt.peek().equals("min")) {
                                reorganisedExpression = reorganisedExpression.concat(shunt.pop() + " ");
                            }
                        }
                    }
                    if (elem.equals("(") || elem.equals("min") || elem.equals("max")) {
                        shunt.push(elem);
                    }
                    if (elem.equals("*") || elem.equals("/") || elem.equals("+") || elem.equals("-")) {
                        if (shunt.isEmpty() || isBigger(elem, shunt.peek())) {
                            shunt.push(elem);
                        }
                        else {
                            while (!(shunt.isEmpty() || isBigger(elem, shunt.peek()))) {
                                reorganisedExpression = reorganisedExpression.concat(shunt.pop() + " ");
                            }
                            shunt.push(elem);
                        }
                    }
                }
            }
            while (!shunt.isEmpty()) {
                reorganisedExpression = reorganisedExpression.concat(" " + shunt.pop());
            }
            Scanner royalty = new Scanner(reorganisedExpression);
            while (royalty.hasNext()) {
                String elem = royalty.next();
                if (elem.matches("\\d")) {
                    shunt.push(elem);
                }
                else {
                    String operand2 = shunt.pop();
                    String operand1 = shunt.pop();
                    shunt.push(perform(elem, operand1, operand2));
                }
            }
            System.out.println(shunt.pop());
            input = scanner.nextLine();
        }
    }

    private boolean isBigger (String a, String b) {
        if (b == null) {
            return true;
        }
        if ((a.equals("+") || a.equals("-")) && (b.equals("+") || b.equals("-") || b.equals("*") || b.equals("/"))) {
            return false;
        }
        else return (!a.equals("*") && !a.equals("/")) || (!b.equals("*") && !b.equals("/"));
    }

    private String perform (String operation, String operand1, String operand2) throws InvalidOperationException {
        int op1 = Integer.parseInt(operand1);
        int op2 = Integer.parseInt(operand2);
        switch (operation) {
            case "min": return (Integer.toString(Math.min(op1, op2)));
            case "max": return (Integer.toString(Math.max(op1, op2)));
            case "-": return (Integer.toString(op1 - op2));
            case "+": return (Integer.toString(op1 + op2));
            case "*": return (Integer.toString(op1 * op2));
            case "/": return (Integer.toString(op1 / op2));
            default: throw new InvalidOperationException(operation);
        }
    }
}

class InvalidOperationException extends Exception {
    String expression;
    InvalidOperationException(String expression) {
        this.expression = expression;
    }

    @Override
    public String getMessage() {
        return "Operation" + expression + "is not a valid operation";
    }
}