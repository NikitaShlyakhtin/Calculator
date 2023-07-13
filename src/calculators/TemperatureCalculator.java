package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;

public class TemperatureCalculator implements Calculator {

    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Select base scale");
        calculatorApp.println("0 - Kelvin");
        calculatorApp.println("1 - Celsius");
        calculatorApp.println("2 - Fahrenheit");
        int oldScaleIndex = Integer.parseInt(calculatorApp.readLine());
        while (oldScaleIndex < 0 || oldScaleIndex > 2) {
            calculatorApp.println("This is not a valid index, select a valid one");
            oldScaleIndex = Integer.parseInt(calculatorApp.readLine());
        }
        calculatorApp.println("Select to which scale to convert");
        calculatorApp.println("0 - Kelvin");
        calculatorApp.println("1 - Celsius");
        calculatorApp.println("2 - Fahrenheit");
        int newScale = Integer.parseInt(calculatorApp.readLine());
        ScaleType newType = ScaleType.KELVIN;
        while (newScale < 0 || newScale > 2) {
            calculatorApp.println("This is not a valid index, select a valid one");
            newScale = Integer.parseInt(calculatorApp.readLine());
        }
        switch (newScale) {
            case 1 -> newType = ScaleType.CELSIUS;
            case 2 -> newType = ScaleType.FAHRENHEIT;
        }
        calculatorApp.println("Enter temperature");
        Conversion conversion;
        switch (oldScaleIndex) {
            case 0 -> {
                conversion = new Conversion(new Kelvin(Double.parseDouble(calculatorApp.readLine())), newType);
                calculatorApp.println(conversion.convert());
            }
            case 1 -> {
                conversion = new Conversion(new Celsius(Double.parseDouble(calculatorApp.readLine())), newType);
                calculatorApp.println(conversion.convert());
            }
            case 2 -> {
                conversion = new Conversion(new Fahrenheit(Double.parseDouble(calculatorApp.readLine())), newType);
                calculatorApp.println(conversion.convert());
            }
        }
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