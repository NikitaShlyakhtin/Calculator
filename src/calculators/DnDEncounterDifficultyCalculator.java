package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;

public class DnDEncounterDifficultyCalculator implements Calculator {
    @Override
    public void work(CalculatorApp calculatorApp) throws IOException {
        DifficultyLevel[] thresholds = new DifficultyLevel[]{DifficultyLevel.EASY,
                DifficultyLevel.MEDIUM, DifficultyLevel.HARD, DifficultyLevel.DEADLY};
        calculatorApp.println("How many player characters do you have?");
        int numberOfCharacters = Integer.parseInt(calculatorApp.readLine());
        for (int i = 0; i < numberOfCharacters; i++) {
            calculatorApp.println("Input level for character " + (i + 1));
            int level = Integer.parseInt(calculatorApp.readLine());
            for (DifficultyLevel difficultyLevel : thresholds) {
                difficultyLevel.increaseDifficulty(level);
            }
        }
        calculatorApp.println("How many monsters will they face?");
        Monsters monsters = new Monsters(Integer.parseInt(calculatorApp.readLine()));
        monsters.count(calculatorApp, thresholds, numberOfCharacters);
        calculatorApp.readLine();
        calculatorApp.close();
    }
}

class Monsters {
    private int sumXP;
    private final int numberOfMonsters;

    Monsters(int howMany) {
        numberOfMonsters = howMany;
    }

    public void count(CalculatorApp calculatorApp, DifficultyLevel[] thresholds, int numberOfCharacters) throws IOException {
        for (int i = 0; i < numberOfMonsters; i++) {
            calculatorApp.println("Enter XP for monster " + (i + 1));
            sumXP += Integer.parseInt(calculatorApp.readLine());
        }
        if (numberOfMonsters >= 15) {
            sumXP *= 4;
        } else if (numberOfMonsters >= 11) {
            sumXP *= 3;
        } else if (numberOfMonsters >= 7) {
            sumXP *= 2.5;
        } else if (numberOfMonsters >= 3) {
            sumXP *= 2;
        } else if (numberOfMonsters == 2) {
            sumXP *=1.5;
        }
        int minDifference = Integer.MAX_VALUE;
        DifficultyLevel realDifficulty = DifficultyLevel.EASY;
        for (DifficultyLevel probableDifficulty : thresholds) {
            if (minDifference > probableDifficulty.difference(sumXP)) {
                minDifference = probableDifficulty.difference(sumXP);
                realDifficulty = probableDifficulty;
            }
        }
        calculatorApp.println(realDifficulty.toString(numberOfCharacters, sumXP));
    }
}

enum DifficultyLevel {
    EASY(new int[]{25, 50, 75, 125, 250, 300, 350, 450, 550, 600, 800,
            1000, 1100, 1250, 1400, 1600, 2000, 2100, 2400, 2800}, "Easy"),
    MEDIUM(new int[]{50, 100, 150, 250, 500, 600, 750, 900, 1100, 1200,
            1600, 2000, 2200, 2500, 2800, 3200, 3900, 4200, 4900, 5700}, "Medium"),
    HARD(new int[]{75, 150, 225, 375, 750, 900, 1100, 1400, 1600, 1900,
            2400, 3000, 3400, 3800, 4300, 4800, 5900, 6300, 7300, 8500}, "Hard"),
    DEADLY(new int[]{100, 200, 400, 500, 1100, 1400, 1700, 2100, 2400,
            2800, 3600, 4500, 5100, 5700, 6400, 7200, 8800, 9500, 10900, 12700}, "Deadly");

    private final int[] XPThresholds;
    private int XPCount;
    private final String name;

    DifficultyLevel(int[] newXPThresholds, String newName) {
        XPThresholds = newXPThresholds;
        XPCount = 0;
        name = newName;
    }

    public void increaseDifficulty(int level) {
        XPCount += XPThresholds[level - 1];
    }

    public int difference(int monsterXP) {
        return Math.abs(XPCount - monsterXP);
    }

    public String toString(int numberOfCharacters, int monsterXP) {
        return "The encounter is " + name + ". Each character will get " + monsterXP / numberOfCharacters;
    }
}