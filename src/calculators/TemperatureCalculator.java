package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;

public class TemperatureCalculator implements Calculator {

    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Select base scale");
        calculatorApp.println("0 - Kelvin");
        calculatorApp.println("1 - Celsius");
        calculatorApp.println("2 - Fahrenheit");
        int oldScaleIndex;
        do {
            try {
                oldScaleIndex = Integer.parseInt(calculatorApp.readLine());
                if (oldScaleIndex < 0 || oldScaleIndex > 2) {
                    throw new Exception();
                }
            } catch (Exception e) {
                calculatorApp.println("This is not a valid index, select a valid one");
                oldScaleIndex = -1;
            }
        } while (oldScaleIndex == -1);
        calculatorApp.println("Select to which scale to convert");
        calculatorApp.println("0 - Kelvin");
        calculatorApp.println("1 - Celsius");
        calculatorApp.println("2 - Fahrenheit");
        int newScale;
        do {
            try {
                newScale = Integer.parseInt(calculatorApp.readLine());
                if (newScale < 0 || newScale > 2) {
                    throw new Exception();
                }
            } catch (Exception e) {
                calculatorApp.println("This is not a valid index, select a valid one");
                newScale = -1;
            }
        } while (newScale == -1);
        ScaleType newType = ScaleType.KELVIN;
        switch (newScale) {
            case 1 -> newType = ScaleType.CELSIUS;
            case 2 -> newType = ScaleType.FAHRENHEIT;
        }
        calculatorApp.println("Enter temperature");
        double temperature;
        boolean trigger = false;
        do {
            try {
                temperature = Double.parseDouble(calculatorApp.readLine());
                trigger = true;
            } catch (Exception e) {
                calculatorApp.println("Not a number, try again");
                temperature = 0;
            }
        } while (temperature == 0 && !trigger);
        Conversion conversion;
        switch (oldScaleIndex) {
            case 0 -> {
                conversion = new Conversion(new Kelvin(temperature), newType);
                calculatorApp.println(conversion.convert());
            }
            case 1 -> {
                conversion = new Conversion(new Celsius(temperature), newType);
                calculatorApp.println(conversion.convert());
            }
            case 2 -> {
                conversion = new Conversion(new Fahrenheit(temperature), newType);
                calculatorApp.println(conversion.convert());
            }
        }
        calculatorApp.readLine();
    }
}

interface Scale {
    double convertToFahrenheit();
    double convertToCelsius();
    double convertToKelvin();
}

class Celsius implements Scale {
    private final double temperature;

    Celsius(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public double convertToFahrenheit() {
        return temperature * 1.8 + 32;
    }

    @Override
    public double convertToCelsius() {
        return temperature;
    }

    @Override
    public double convertToKelvin() {
        return temperature + 273.15;
    }
}

class Fahrenheit implements Scale {
    private final double temperature;

    Fahrenheit(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public double convertToFahrenheit() {
        return temperature;
    }

    @Override
    public double convertToCelsius() {
        return (temperature - 32) / 1.8;
    }

    @Override
    public double convertToKelvin() {
        return (temperature + 459.67) / 1.8;
    }
}

class Kelvin implements Scale {
    private final double temperature;

    Kelvin(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public double convertToFahrenheit() {
        return temperature * 1.8 - 459.67;
    }

    @Override
    public double convertToCelsius() {
        return temperature - 273.15;
    }

    @Override
    public double convertToKelvin() {
        return temperature;
    }
}

class Conversion {
    private final Scale oldScale;
    private final ScaleType newScale;

    Conversion(Scale oldScale, ScaleType newScale) {
        this.oldScale = oldScale;
        this.newScale = newScale;
    }

    public String convert() {
        switch (newScale) {
            case KELVIN -> {
                return "result = " + oldScale.convertToKelvin();
            }
            case CELSIUS -> {
                return "result = " + oldScale.convertToCelsius();
            }
            case FAHRENHEIT -> {
                return "result = " + oldScale.convertToFahrenheit();
            }
            default -> {
                return "Not a valid index";
            }
        }
    }
}

enum ScaleType {
    KELVIN,
    CELSIUS,
    FAHRENHEIT
}