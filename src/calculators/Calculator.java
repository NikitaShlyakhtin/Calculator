package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;

public interface Calculator {
    void work(CalculatorApp calculatorApp) throws IOException;
}
