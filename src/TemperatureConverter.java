import java.util.Scanner;

public class TemperatureConverter {
    private double temperature;
    private Scale scale;

    public TemperatureConverter(double temperature, Scale scale) {
        this.temperature = temperature;
        this.scale = scale;
    }

    public TemperatureConverter() {
    }

    public double convert() {
        return scale.convert(temperature);
    }

    void work(Scanner scanner) {
        System.out.println("Type 1 for Celsius to Fahrenheit or 2 for Fahrenheit to Celsius.");
        int scale = scanner.nextInt();
        System.out.println("Enter temperature: ");
        temperature = scanner.nextDouble();
        switch (scale){
            case 1:
                TemperatureConverter celsiusToFahrenheit = new TemperatureConverter(temperature, new Celsius());
                double convertedToFahrenheit = celsiusToFahrenheit.convert();
                System.out.println(temperature + " degrees Celsius is equal to " + convertedToFahrenheit + " degrees Fahrenheit");
                break;
            case 2:
                TemperatureConverter fahrenheitToCelsius = new TemperatureConverter(temperature, new Fahrenheit());
                double convertedToCelsius = fahrenheitToCelsius.convert();
                System.out.println(temperature + " degrees Fahrenheit is equal to " + convertedToCelsius + " degrees Celsius");
                break;
            default:
                System.out.println("Invalid input!");
        }
    }
}

interface Scale {
    double convert(double temperature);
}

class Celsius implements Scale {
    @Override
    public double convert(double temperature) {
        return (temperature * 9 / 5) + 32; // Celsius to Fahrenheit
    }
}

class Fahrenheit implements Scale {
    @Override
    public double convert(double temperature) {
        return (temperature - 32) * 5 / 9; // Fahrenheit to Celsius
    }
}

