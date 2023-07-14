package calculators;

import calculatorApp.CalculatorApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;


public class BasicCalculator implements Calculator {

    @Override
    public void work(CalculatorApp calculatorApp) throws IOException {
        calculatorApp.println("Available operations:");
        calculatorApp.println("Basic arithmetic operations: \"+\", \"-\", \"*\", \"/\", \"pow ( a , b )\"");
        calculatorApp.println("Comparison operations: \"min ( a , b )\", \"max ( a , b )\"");
        calculatorApp.println("Please, input operands separated by space");
        calculatorApp.println("Double numbers should be input via \".\"");
        calculatorApp.println("To stop working, write STOP");
        String input = calculatorApp.readLine();
        while (!input.equals("STOP")) {
            PerformedExpression expression = new PerformedExpression(new Shunt(new ParsedTokens(input)));
            try {
                calculatorApp.print(" = " + expression.perform());
            } catch (NotPerformableTokenException | NotParsableTokenException | DivisionByZeroException |
                     InvalidOperationException | UndefinedBehaviourException e) {
                calculatorApp.println(e.getMessage());
            }
            input = calculatorApp.readLine();
        }
    }
}

class PerformedExpression {
    private final Shunt shunt;

    PerformedExpression(Shunt reorganisedExpression) {
        this.shunt = reorganisedExpression;
    }

    public String perform() throws NotPerformableTokenException,
            NotParsableTokenException, DivisionByZeroException, InvalidOperationException, UndefinedBehaviourException {
        ArrayList<Token> reorganisedExpression = shunt.reorganise();
        Stack<Token> royalty = new Stack<>();
        for (Token token : reorganisedExpression) {
            if (token.type == TokenType.NUMBER) {
                royalty.push(token);
            } else if (token.type == TokenType.DIVIDE || token.type == TokenType.DIV || token.type == TokenType.MIN
                    || token.type == TokenType.MOD || token.type == TokenType.MAX || token.type == TokenType.POWER
                    || token.type == TokenType.MULTIPLY || token.type == TokenType.MINUS
                    || token.type == TokenType.PLUS) {
                Token operand2 = royalty.pop();
                Token operand1 = royalty.pop();
                royalty.push(token.perform(operand1, operand2));
            }
        }
        return royalty.pop().value;
    }
}

class Shunt {
    private final ParsedTokens parsedTokens;

    Shunt(ParsedTokens parsedInput) {
        this.parsedTokens = parsedInput;
    }

    public ArrayList<Token> reorganise() throws InvalidOperationException {
        ArrayList<Token> parsedInput = parsedTokens.parse();
        ArrayList<Token> reorganisedInput = new ArrayList<>();
        Stack<Token> shunt = new Stack<>();
        for (Token token : parsedInput) {
            switch (token.type) {
                case NUMBER -> reorganisedInput.add(token);
                case COMMA -> {
                    while (!(shunt.peek().type == TokenType.OPEN_BRACKET)) {
                        reorganisedInput.add(shunt.pop());
                    }
                }
                case CLOSE_BRACKET -> {
                    while (!(shunt.peek().type == TokenType.OPEN_BRACKET)) {
                        reorganisedInput.add(shunt.pop());
                    }
                    shunt.pop();
                    if (!shunt.isEmpty()) {
                        if (shunt.peek().type == TokenType.MAX || shunt.peek().type == TokenType.MIN
                                || shunt.peek().type == TokenType.POWER || shunt.peek().type == TokenType.MOD
                                || shunt.peek().type == TokenType.DIV) {
                            reorganisedInput.add(shunt.pop());
                        }
                    }
                }
                case OPEN_BRACKET, MIN, MAX, POWER, MOD, DIV -> shunt.push(token);
                default -> {
                    if (shunt.isEmpty() || token.isBigger(shunt.peek())) {
                        shunt.push(token);
                    } else {
                        while (!(shunt.isEmpty() || token.isBigger(shunt.peek()))) {
                            reorganisedInput.add(shunt.pop());
                        }
                        shunt.push(token);
                    }
                }
            }
        }
        while (!shunt.isEmpty()) {
            reorganisedInput.add(shunt.pop());
        }
        return reorganisedInput;
    }
}

class ParsedTokens {
    private final String input;

    ParsedTokens(String newInput) {
        input = newInput.trim();
    }

    public ArrayList<Token> parse() throws InvalidOperationException {
        ArrayList<Token> tokens = new ArrayList<>();
        int index = 0;
        String token = "";
        while (index < input.length()) {
            if (input.charAt(index) == ' ') {
                if (token.matches("-?\\d+(\\.\\d+)?")) {
                    tokens.add(new Token(TokenType.NUMBER, token));
                } else {
                    switch (token) {
                        case "+" -> tokens.add(new Token(TokenType.PLUS, token));
                        case "-" -> tokens.add(new Token(TokenType.MINUS, token));
                        case "*" -> tokens.add(new Token(TokenType.MULTIPLY, token));
                        case "/" -> tokens.add(new Token(TokenType.DIVIDE, token));
                        case "min" -> tokens.add(new Token(TokenType.MIN, token));
                        case "max" -> tokens.add(new Token(TokenType.MAX, token));
                        case "(" -> tokens.add(new Token(TokenType.OPEN_BRACKET, token));
                        case ")" -> tokens.add(new Token(TokenType.CLOSE_BRACKET, token));
                        case "," -> tokens.add(new Token(TokenType.COMMA, token));
                        case "pow" -> tokens.add(new Token(TokenType.POWER, token));
                        case "mod" -> tokens.add(new Token(TokenType.MOD, token));
                        case "div" -> tokens.add(new Token(TokenType.DIV, token));
                        default -> throw new InvalidOperationException(token);
                    }
                }
                token = "";
            } else {
                token = token + input.charAt(index);
            }
            index++;
        }
        if (token.matches("\\d*(\\.\\d*)?")) {
            tokens.add(new Token(TokenType.NUMBER, token));
        } else {
            switch (token) {
                case "+" -> tokens.add(new Token(TokenType.PLUS, token));
                case "-" -> tokens.add(new Token(TokenType.MINUS, token));
                case "*" -> tokens.add(new Token(TokenType.MULTIPLY, token));
                case "/" -> tokens.add(new Token(TokenType.DIVIDE, token));
                case "min" -> tokens.add(new Token(TokenType.MIN, token));
                case "max" -> tokens.add(new Token(TokenType.MAX, token));
                case "(" -> tokens.add(new Token(TokenType.OPEN_BRACKET, token));
                case ")" -> tokens.add(new Token(TokenType.CLOSE_BRACKET, token));
                case "," -> tokens.add(new Token(TokenType.COMMA, token));
                case "pow" -> tokens.add(new Token(TokenType.POWER, token));
                case "mod" -> tokens.add(new Token(TokenType.MOD, token));
                case "div" -> tokens.add(new Token(TokenType.DIV, token));
                default -> throw new InvalidOperationException(token);
            }
        }
        return tokens;
    }
}

class Token {
    public final TokenType type;
    public final String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Token calculate(Token token) throws NotCalculateTokenException {
        switch (type) {
            default -> throw new NotCalculateTokenException(value);
        }
    }

    public Token perform(Token firstToken, Token secondToken)
            throws NotPerformableTokenException, NotParsableTokenException, DivisionByZeroException,
            UndefinedBehaviourException {
        switch (type) {
            case MAX -> {
                return new Token(TokenType.NUMBER,
                        Double.toString(Math.max(firstToken.parseDouble(), secondToken.parseDouble())));
            }
            case MIN -> {
                return new Token(TokenType.NUMBER,
                        Double.toString(Math.min(firstToken.parseDouble(), secondToken.parseDouble())));
            }
            case PLUS -> {
                return new Token(TokenType.NUMBER,
                        Double.toString(firstToken.parseDouble() + secondToken.parseDouble()));
            }
            case MINUS -> {
                return new Token(TokenType.NUMBER,
                        Double.toString(firstToken.parseDouble() - secondToken.parseDouble()));
            }
            case DIVIDE -> {
                if (secondToken.parseDouble() == 0) {
                    throw new DivisionByZeroException();
                }
                return new Token(TokenType.NUMBER,
                        Double.toString(firstToken.parseDouble() / secondToken.parseDouble()));
            }
            case MULTIPLY -> {
                return new Token(TokenType.NUMBER,
                        Double.toString(firstToken.parseDouble() * secondToken.parseDouble()));
            }
            case POWER -> {
                if (firstToken.parseDouble() == 0 && secondToken.parseDouble() <= 0
                        || firstToken.parseDouble() < 0 && secondToken.parseDouble() % 1 != 0) {
                    throw new UndefinedBehaviourException();
                }
                return new Token(TokenType.NUMBER,
                        Double.toString(Math.pow(firstToken.parseDouble(), secondToken.parseDouble())));
            }
            case MOD -> {
                if (secondToken.parseDouble() == 0) {
                    throw new DivisionByZeroException();
                }
                return new Token(TokenType.NUMBER,
                        Double.toString(firstToken.parseDouble() % secondToken.parseDouble()));
            }
            case DIV -> {
                if (secondToken.parseDouble() == 0) {
                    throw new DivisionByZeroException();
                }
                return new Token(TokenType.NUMBER,
                        Double.toString((firstToken.parseDouble() / secondToken.parseDouble())
                                - firstToken.parseDouble() / secondToken.parseDouble() % 1));
            }
            default -> throw new NotPerformableTokenException(value);
        }
    }

    public double parseDouble() throws NotParsableTokenException {
        try {
        return Double.parseDouble(value);
        } catch (Exception e) {
            throw new NotParsableTokenException(value);
        }
    }

    public boolean isBigger(Token token) {
        if ((type == TokenType.PLUS || type == TokenType.MINUS)
                && (token.type == TokenType.MINUS || token.type == TokenType.PLUS
                || token.type == TokenType.MULTIPLY || token.type == TokenType.DIVIDE)) {
            return false;
        } else return (!(type == TokenType.MULTIPLY) && !(type == TokenType.DIVIDE))
                || (!(token.type == TokenType.MULTIPLY) && !(token.type == TokenType.DIVIDE));
    }
}

enum TokenType {
    NUMBER,
    MINUS,
    PLUS,
    DIVIDE,
    MULTIPLY,
    MIN,
    MAX,
    OPEN_BRACKET,
    CLOSE_BRACKET,
    COMMA,
    POWER,
    MOD,
    DIV,
    ABS,
    SINUS,
    COS,
    TANGENT,
    COTANGENT,
    SECANT,
    CSC,
    ASIN,
    ACOS,
    ATAN,
    ACOT,
    ASEC,
    ACSC,
    SINH,
    COSH,
    TANH,
    COTH,
    SECH,
    CSCH,
    CEIL,
    FLOOR,
    ROUND,
    SIGN
}

class NotCalculateTokenException extends Exception {
    String expression;
    NotCalculateTokenException(String expression) {
        this.expression = expression;
    }
    @Override
    public String getMessage() {
        return "The token" + expression + "cannot be calculated, error in parsing the input";
    }
}

class NotPerformableTokenException extends Exception {
    String expression;
    NotPerformableTokenException(String expression) {
        this.expression = expression;
    }
    @Override
    public String getMessage() {
        return "The token" + expression + "cannot be performed, error in parsing the input";
    }
}

class NotParsableTokenException extends Exception {
    String expression;
    NotParsableTokenException(String expression) {
        this.expression = expression;
    }
    @Override
    public String getMessage() {
        return "The token " + expression + " cannot be parsed, error in parsing the input";
    }
}

class DivisionByZeroException extends Exception {
    @Override
    public String getMessage() {
        return "Cannot divide by zero";
    }
}

class InvalidOperationException extends Exception {
    String expression;
    InvalidOperationException(String expression) {
        this.expression = expression;
    }

    @Override
    public String getMessage() {
        return "Operation " + expression + " is not a valid operation, error in the input";
    }
}

class UndefinedBehaviourException extends Exception {
    @Override
    public String getMessage() {
        return "The behaviour of the expression is undefined";
    }
}