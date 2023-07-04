package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;

public class DnDPointBuyCalculator implements Calculator {
    @Override
    public void work(CalculatorApp calculatorApp) throws IOException {
        Characteristic[] characteristics = {Characteristic.STRENGTH, Characteristic.CONSTITUTION,
                Characteristic.DEXTERITY, Characteristic.INTELLIGENCE, Characteristic.WISDOM, Characteristic.CHARISMA};
        int leftPoints = 0;
        String input;
        do {
            calculatorApp.println("To exit print \"EXIT\"");
            calculatorApp.println("Current stats:");
            for (Characteristic characteristic : characteristics) {
                calculatorApp.println(characteristic.toString());
            }
            calculatorApp.println("Left points: " + leftPoints);
            calculatorApp.println("What would you like to increase or decrease?");
            calculatorApp.println("0 - strength");
            calculatorApp.println("1 - dexterity");
            calculatorApp.println("2 - constitution");
            calculatorApp.println("3 - intelligence");
            calculatorApp.println("4 - wisdom");
            calculatorApp.println("5 - charisma");
            input = calculatorApp.readLine();
            if (!(input.equals("EXIT") || input.equals("\"EXIT\""))) {
                calculatorApp.println("Increase or decrease?");
                calculatorApp.println("0 - increase");
                calculatorApp.println("1 - decrease");
                switch (Integer.parseInt(calculatorApp.readLine())) {
                    case 0 -> leftPoints = characteristics[Integer.parseInt(input)].increase(leftPoints, calculatorApp);
                    case 1 -> leftPoints = characteristics[Integer.parseInt(input)].decrease(leftPoints, calculatorApp);
                }
            } else if (leftPoints != 0){
                calculatorApp.println("You still have " + leftPoints + " points left.");
                calculatorApp.println("Repeat \"EXIT\" if you really want to exit or press Enter key otherwise");
                input = calculatorApp.readLine();
            }
        } while (!(input.equals("EXIT") || input.equals("\"EXIT\"")));
        calculatorApp.close();
    }
}

enum Characteristic {
    STRENGTH("Strength"),
    CONSTITUTION("Constitution"),
    DEXTERITY("Dexterity"),
    INTELLIGENCE("Intelligence"),
    WISDOM("Wisdom"),
    CHARISMA("Charisma");
    private int value;
    private final String name;

    Characteristic(String newName) {
        value = 12;
        name = newName;
    }

    public int increase(int leftPoints, CalculatorApp calculatorApp) throws IOException {
        if (value >= 13 && leftPoints < 2) {
            calculatorApp.println("You cannot increase this characteristic: not enough points");
        } else if (value == 15) {
            calculatorApp.println("You cannot increase this characteristic: already maximum possible value");
        } else {
            value++;
            leftPoints--;
            if (value >= 14) {
                leftPoints--;
            }
        }
        return leftPoints;
    }

    public int decrease(int leftPoints, CalculatorApp calculatorApp) throws IOException {
        if (value == 8) {
            calculatorApp.println("You cannot decrease this characteristic: already minimum possible value");
        } else {
            value--;
            leftPoints++;
            if (value >= 13) {
                leftPoints++;
            }
        }
        return leftPoints;
    }

    public String toString() {
        return name + ": " + value + " (" + (value > 11 ? "+" : "") + (value / 2 - 5) + ")";
    }
}
