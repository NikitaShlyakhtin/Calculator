package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;
import java.util.ArrayList;

public class MatrixCalculator implements Calculator{
    @Override
    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Input matrix A");
        Matrix first = new Matrix(calculatorApp);
        calculatorApp.println("Input matrix B");
        Matrix second = new Matrix(calculatorApp);
        int operation;
        do {
            calculatorApp.println("Select operation:");
            calculatorApp.println("1: A + B");
            calculatorApp.println("2: A - B");
            calculatorApp.println("3: B - A");
            calculatorApp.println("4: A * B");
            calculatorApp.println("5: B * A");
            calculatorApp.println("6: Transpose A");
            calculatorApp.println("7: Transpose B");
            calculatorApp.println("0: exit");
            operation = Integer.parseInt(calculatorApp.readLine());
            try {
                switch (operation) {
                    case 1 -> first.add(second).output(calculatorApp);
                    case 2 -> first.subtract(second).output(calculatorApp);
                    case 3 -> second.subtract(first).output(calculatorApp);
                    case 4 -> first.multiply(second).output(calculatorApp);
                    case 5 -> second.multiply(first).output(calculatorApp);
                    case 6 -> first.transpose().output(calculatorApp);
                    case 7 -> second.transpose().output(calculatorApp);
                    case 0 -> calculatorApp.close();
                    default -> calculatorApp.println("Not a valid operation");
                }
            } catch (IncompatibleSizeException e) {
                calculatorApp.println(e.getMessage());
            }
        } while (operation != 0);
    }
}

class Matrix {
    public final int rows;
    public final int columns;
    public final Double[][] values;
    Matrix(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Input rows");
        rows = Integer.parseInt(calculatorApp.readLine());
        calculatorApp.println("Input columns");
        columns = Integer.parseInt(calculatorApp.readLine());
        calculatorApp.println("Input values row by row");
        ArrayList<Double[]> newValues = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            ArrayList<Double> row = new ArrayList<>();
            String[] input = calculatorApp.readLine().split(" ");
            for (String value : input) {
                row.add(Double.parseDouble(value));
            }
            Double[] costyl = new Double[columns];
            newValues.add(row.toArray(costyl));
        }
        Double[][] secondCostyl= new Double[rows][columns];
        values = newValues.toArray(secondCostyl);
    }

    Matrix(int rows, int columns, Double[][] values) {
        this.rows = rows;
        this.columns = columns;
        this.values = values;
    }

    public void output(CalculatorApp calculatorApp) throws IOException {
        for (int i = 0; i < rows; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < columns; j++) {
                row.append(values[i][j].toString()).append(" ");
            }
            calculatorApp.println(row.toString());
        }
    }

    public Matrix add(Matrix matrix) throws IncompatibleSizeException {
        if (rows != matrix.rows || columns != matrix.columns) {
            throw new IncompatibleSizeException();
        }
        Double[][] newValues = values;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newValues[i][j] += matrix.values[i][j];
            }
        }
        return new Matrix(rows, columns, newValues);
    }

    public Matrix subtract(Matrix matrix) throws IncompatibleSizeException {
        if (rows != matrix.rows || columns != matrix.columns) {
            throw new IncompatibleSizeException();
        }
        Double[][] newValues = values;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newValues[i][j] -= matrix.values[i][j];
            }
        }
        return new Matrix(rows, columns, newValues);
    }

    public Matrix transpose() {
        Double[][] newValues = new Double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                newValues[i][j] = values[j][i];
            }
        }
        return new Matrix(columns, rows, newValues);
    }

    public Matrix multiply(Matrix matrix) throws IncompatibleSizeException {
        if (columns != matrix.rows) {
            throw new IncompatibleSizeException();
        }
        Double[][] newValues = new Double[rows][matrix.columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                newValues[i][j] = 0.0;
                for (int k = 0; k < columns; k++) {
                    newValues[i][j] += values[i][k] * matrix.values[k][j];
                }
            }
        }
        return new Matrix(rows, matrix.columns, newValues);
    }
}

class IncompatibleSizeException extends Exception {
    @Override
    public String toString() {
        return "The operation cannot be performed, the matrices sizes are incompatible";
    }
}