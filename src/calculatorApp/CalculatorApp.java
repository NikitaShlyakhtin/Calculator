package calculatorApp;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * Object representing calculator application in terminal window
 */
public class CalculatorApp {
    private final Terminal terminal;
    private final TextGraphics textGraphics;
    private int yCounter; // Y position of cursor
    private int xCounter; // X position of cursor

    public CalculatorApp() throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        this.terminal = defaultTerminalFactory.createTerminal();
        this.textGraphics = terminal.newTextGraphics();
        this.yCounter = 0;
        this.xCounter = 0;
    }

    /**
     * Method to print line in calculator application window.
     * <p>
     * Moves cursor at the beginning of a new line after printing.
     * Clears x-position of the cursor.
     *
     * @param line String line to print.
     */
    public void println(String line) throws IOException {
        xCounter = 0;

        textGraphics.putString(xCounter, yCounter, line);
        terminal.setCursorPosition(0, yCounter + 1);
        terminal.flush();

        yCounter++;
    }

    /**
     * Method to print some string in calculator application window.
     * <p>
     * Prints string on line before cursor after all strings.
     * After that returns cursor to previous position.
     * Clears x-position of the cursor.
     *
     * @param string String to print.
     */
    public void print(String string) throws IOException {
        textGraphics.putString(xCounter, yCounter - 1, string);
        xCounter = 0;

        terminal.setCursorPosition(0, yCounter);
        terminal.flush();
    }

    /**
     * Method to read line from user.
     * <p>
     * Line ends on "Enter" button press.
     * Moves cursor at the beginning of a new line after printing.
     * Doesn't clear x-position of the cursor.
     *
     * @return Returns string from user input.
     */
    public String readLine() throws IOException {
        KeyStroke keyStroke = terminal.readInput();

        StringBuilder stringBuilder = new StringBuilder();
        while (keyStroke.getKeyType() != KeyType.Enter) {
            if (keyStroke.getKeyType() == KeyType.Backspace) {
                textGraphics.setCharacter(stringBuilder.length() - 1, yCounter, ' ');
                terminal.setCursorPosition(stringBuilder.length() - 1, yCounter);
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            } else {
                stringBuilder.append(keyStroke.getCharacter());
                textGraphics.putString(stringBuilder.length() - 1, yCounter, keyStroke.getCharacter().toString());
            }

            terminal.flush();
            keyStroke = terminal.readInput();
        }

        xCounter += stringBuilder.length();
        yCounter++;

        terminal.setCursorPosition(0, yCounter);

        terminal.flush();

        return stringBuilder.toString();
    }

    public void clear() throws IOException {
        xCounter = 0;
        yCounter = 0;
        terminal.clearScreen();
    }

    public void close() throws IOException {
        terminal.close();
        System.exit(0);
    }
}
