package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;

public class LongDistanceCalculator implements Calculator {

    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Select base scale");
        calculatorApp.println("0 - Kilometer");
        calculatorApp.println("1 - Mile");
        calculatorApp.println("2 - Yard");
        calculatorApp.println("3 - Feet");
        int oldScaleIndex;
        do {
            try {
                oldScaleIndex = Integer.parseInt(calculatorApp.readLine());
                if (oldScaleIndex < 0 || oldScaleIndex > 3) {
                    throw new Exception();
                }
            } catch (Exception e) {
                calculatorApp.println("This is not a valid index, select a valid one");
                oldScaleIndex = -1;
            }
        } while (oldScaleIndex == -1);
        calculatorApp.println("Select to which scale to convert");
        calculatorApp.println("0 - Kilometer");
        calculatorApp.println("1 - Mile");
        calculatorApp.println("2 - Yard");
        calculatorApp.println("3 - Feet");
        int newScale;
        do {
            try {
                newScale = Integer.parseInt(calculatorApp.readLine());
                if (newScale < 0 || newScale > 3) {
                    throw new Exception();
                }
            } catch (Exception e) {
                calculatorApp.println("This is not a valid index, select a valid one");
                newScale = -1;
            }
        } while (newScale == -1);
        DistanceType newType = DistanceType.KILOMETER;
        switch (newScale) {
            case 1 -> newType = DistanceType.MILE;
            case 2 -> newType = DistanceType.YARD;
            case 3 -> newType = DistanceType.FEET;
        }
        calculatorApp.println("Enter distance");
        double distance;
        boolean trigger = false;
        do {
            try {
                distance = Double.parseDouble(calculatorApp.readLine());
                trigger = true;
            } catch (Exception e) {
                calculatorApp.println("Not a number, try again");
                distance = 0;
            }
        } while (distance == 0 && !trigger);
        ConversionDistance conversion;
        switch (oldScaleIndex) {
            case 0 -> {
                conversion = new ConversionDistance(new Kilometer(distance), newType);
                calculatorApp.println(conversion.convert());
            }
            case 1 -> {
                conversion = new ConversionDistance(new Mile(distance), newType);
                calculatorApp.println(conversion.convert());
            }
            case 2 -> {
                conversion = new ConversionDistance(new Yard(distance), newType);
                calculatorApp.println(conversion.convert());
            }
            case 3 -> {
                conversion = new ConversionDistance(new Feet(distance), newType);
                calculatorApp.println(conversion.convert());
            }
        }
        calculatorApp.readLine();
    }
}

interface Distance {
    double convertToKilometers();
    double convertToMiles();
    double convertToYards();
    double convertToFeet();
}

class Mile implements Distance {
    private final double distance;

    Mile(double distance) {
        this.distance = distance;
    }

    @Override
    public double convertToKilometers() {
        return distance * 1.609;
    }

    @Override
    public double convertToMiles() {
        return distance;
    }

    @Override
    public double convertToYards() {
        return distance * 1760;
    }
    @Override
    public double convertToFeet() {
        return distance*5280;
    }
}

class Kilometer implements Distance {
    private final double distance;

    Kilometer(double distance) {
        this.distance = distance;
    }

    @Override
    public double convertToKilometers() {
        return distance;
    }

    @Override
    public double convertToMiles() {
        return distance * 0.621;
    }

    @Override
    public double convertToYards() {
        return distance * 1093.613;
    }
    @Override
    public double convertToFeet() {
        return distance*3280.8;
    }
}

class Yard implements Distance {
    private final double distance;

    Yard(double distance) {
        this.distance = distance;
    }

    @Override
    public double convertToKilometers() {
        return distance / 1093.613;
    }

    @Override
    public double convertToMiles() {
        return distance / 1760;
    }

    @Override
    public double convertToYards() {
        return distance;
    }
    @Override
    public double convertToFeet() {
        return distance*3;
    }
}

class Feet implements Distance {
    private final double distance;

    Feet(double distance) {
        this.distance = distance;
    }

    @Override
    public double convertToKilometers() {
        return distance /3280.8;
    }

    @Override
    public double convertToMiles() {
        return distance / 5280;
    }

    @Override
    public double convertToYards() {
        return distance/3;
    }
    @Override
    public double convertToFeet() {
        return distance;
    }
}

class ConversionDistance {
    private final Distance oldScale;
    private final DistanceType newScale;

    ConversionDistance(Distance oldScale, DistanceType newScale) {
        this.oldScale = oldScale;
        this.newScale = newScale;
    }

    public String convert() {
        switch (newScale) {
            case KILOMETER -> {
                return "result = " + oldScale.convertToKilometers();
            }
            case MILE -> {
                return "result = " + oldScale.convertToMiles();
            }
            case YARD -> {
                return "result = " + oldScale.convertToYards();
            }
            case FEET -> {
                return "result = " + oldScale.convertToFeet();
            }
            default -> {
                return "Not a valid index";
            }
        }
    }
}

enum DistanceType {
    KILOMETER,
    MILE,
    YARD,
    FEET
}