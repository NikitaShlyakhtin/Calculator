package calculators;

import java.util.Scanner;

public interface Calculator {
    void work(Scanner scanner) throws InvalidOperationException;
}
