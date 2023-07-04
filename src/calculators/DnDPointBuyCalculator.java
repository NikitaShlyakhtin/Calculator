//package calculators;
//
//import java.util.Scanner;
//
//public class DnDPointBuyCalculator implements Calculator {
//    @Override
//    public void work(Scanner scanner) {
//        Characteristic[] characteristics = {Characteristic.STRENGTH, Characteristic.CONSTITUTION,
//                Characteristic.DEXTERITY, Characteristic.INTELLIGENCE, Characteristic.WISDOM, Characteristic.CHARISMA};
//        int leftPoints = 0;
//        System.out.println("To exit print \"EXIT\"");
//        String input;
//        do {
//            System.out.println("Current stats:");
//            for (Characteristic characteristic : characteristics) {
//                System.out.println(characteristic);
//            }
//            System.out.println("Left points: " + leftPoints);
//            System.out.println("What would you like to increase or decrease?");
//            System.out.println("0 - strength");
//            System.out.println("1 - dexterity");
//            System.out.println("2 - constitution");
//            System.out.println("3 - intelligence");
//            System.out.println("4 - wisdom");
//            System.out.println("5 - charisma");
//            input = scanner.nextLine();
//            if (!(input.equals("EXIT") || input.equals("\"EXIT\""))) {
//                System.out.println("Increase or decrease?");
//                System.out.println("0 - increase");
//                System.out.println("1 - decrease");
//                switch (scanner.nextInt()) {
//                    case 0: {
//                        leftPoints = characteristics[Integer.parseInt(input)].increase(leftPoints);
//                        break;
//                    }
//                    case 1: {
//                        leftPoints = characteristics[Integer.parseInt(input)].decrease(leftPoints);
//                        break;
//                    }
//                }
//                scanner.nextLine();
//            } else if (leftPoints != 0){
//                System.out.println("You still have " + leftPoints + "points left.");
//                System.out.println("Repeat \"EXIT\" if you really want to exit or press Enter key otherwise");
//                input = scanner.nextLine();
//            }
//        } while (!(input.equals("EXIT") || input.equals("\"EXIT\"")));
//    }
//}
//
//enum Characteristic {
//    STRENGTH("Strength"),
//    CONSTITUTION("Constitution"),
//    DEXTERITY("Dexterity"),
//    INTELLIGENCE("Intelligence"),
//    WISDOM("Wisdom"),
//    CHARISMA("Charisma");
//    private int value;
//    private final String name;
//
//    Characteristic(String newName) {
//        value = 12;
//        name = newName;
//    }
//
//    public int increase(int leftPoints) {
//        if (value >= 13 && leftPoints < 2) {
//            System.out.println("You cannot increase this characteristic: not enough points");
//        } else if (value == 15) {
//            System.out.println("You cannot increase this characteristic: already maximum possible value");
//        } else {
//            value++;
//            leftPoints--;
//            if (value >= 14) {
//                leftPoints--;
//            }
//        }
//        return leftPoints;
//    }
//
//    public int decrease(int leftPoints) {
//        if (value == 8) {
//            System.out.println("You cannot decrease this characteristic: already minimum possible value");
//        } else {
//            value--;
//            leftPoints++;
//            if (value >= 13) {
//                leftPoints++;
//            }
//        }
//        return leftPoints;
//    }
//
//    public String toString() {
//        return name + ": " + value + " (" + (value > 11 ? "+" : "") + (value / 2 - 5) + ")";
//    }
//}
