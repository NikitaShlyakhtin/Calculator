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
        calculatorApp.println(
                "Trigonometric functions: \"sin ( a )\", \"cos ( a )\", \"tan ( a )\", \"cot ( a )\", \"sec ( a )\", \"csc ( a )\""
        );
        calculatorApp.println(
                "Inverse trigonometric functions: \"arcsin ( a )\", \"arccos ( a )\", \"arctan ( a )\", \"arccot ( a )\""
        );
        calculatorApp.println("Hyperbolic functions: \"sinh ( a )\", \"cosh ( a )\", \"tanh ( a )\", \"coth ( a )\"");
        calculatorApp.println(
                "Number theory: \"mod ( a , b )\", \"div ( a , b )\", \"ceil ( a )\", \"floor ( a )\", \"abs ( a )\", \"round ( a )\", \"sign ( a )\""
        );
        calculatorApp.println("Please, input operands separated by space");
        calculatorApp.println("Double numbers should be input via \".\"");
        calculatorApp.println("To stop working, write STOP");
        String input = calculatorApp.readLine();
        while (!input.equals("STOP")) {
            PerformedExpression expression = new PerformedExpression(new Shunt(new ParsedTokens(input)));
            try {
                calculatorApp.print(" = " + expression.perform());
            } catch (NotPerformableTokenException | NotParsableTokenException | DivisionByZeroException |
                     InvalidOperationException | UndefinedBehaviourException | FunctionBreakException |
                     NotCalculateTokenException e) {
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

    public String perform() throws NotPerformableTokenException, NotParsableTokenException, DivisionByZeroException,
            InvalidOperationException, UndefinedBehaviourException, FunctionBreakException, NotCalculateTokenException {
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
            } else {
                royalty.push(token.calculate(royalty.pop()));
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
                                || shunt.peek().type == TokenType.DIV || shunt.peek().type == TokenType.ABS
                                || shunt.peek().type == TokenType.SINUS || shunt.peek().type == TokenType.COS
                                || shunt.peek().type == TokenType.TANGENT || shunt.peek().type == TokenType.COTANGENT
                                || shunt.peek().type == TokenType.SECANT || shunt.peek().type == TokenType.CSC
                                || shunt.peek().type == TokenType.ASIN || shunt.peek().type == TokenType.ACOS
                                || shunt.peek().type == TokenType.ATAN || shunt.peek().type == TokenType.ACOT
                                || shunt.peek().type == TokenType.SINH || shunt.peek().type == TokenType.COSH
                                || shunt.peek().type == TokenType.TANH || shunt.peek().type == TokenType.COTH
                                || shunt.peek().type == TokenType.CEIL || shunt.peek().type == TokenType.FLOOR
                                || shunt.peek().type == TokenType.ROUND || shunt.peek().type == TokenType.SIGN) {
                            reorganisedInput.add(shunt.pop());
                        }
                    }
                }
                case OPEN_BRACKET, MIN, MAX, POWER, MOD, DIV, ABS, SINUS, COS, TANGENT, COTANGENT, SECANT, CSC, ASIN,
                        ACOS, ATAN, ACOT, SINH, COSH, TANH, COTH, CEIL, FLOOR, ROUND, SIGN-> shunt.push(token);
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
                        case "abs" -> tokens.add(new Token(TokenType.ABS, token));
                        case "sin" -> tokens.add(new Token(TokenType.SINUS, token));
                        case "cos" -> tokens.add(new Token(TokenType.COS, token));
                        case "tan" -> tokens.add(new Token(TokenType.TANGENT, token));
                        case "cot" -> tokens.add(new Token(TokenType.COTANGENT, token));
                        case "sec" -> tokens.add(new Token(TokenType.SECANT, token));
                        case "csc" ->  tokens.add(new Token(TokenType.CSC, token));
                        case "arcsin" -> tokens.add(new Token(TokenType.ASIN, token));
                        case "arccos" -> tokens.add(new Token(TokenType.ACOS, token));
                        case "arctan" -> tokens.add(new Token(TokenType.ATAN, token));
                        case "arccot" -> tokens.add(new Token(TokenType.ACOT, token));
                        case "sinh" -> tokens.add(new Token(TokenType.SINH, token));
                        case "cosh" -> tokens.add(new Token(TokenType.COSH, token));
                        case "tanh" -> tokens.add(new Token(TokenType.TANH, token));
                        case "coth" -> tokens.add(new Token(TokenType.COTH, token));
                        case "ceil" -> tokens.add(new Token(TokenType.CEIL, token));
                        case "floor" -> tokens.add(new Token(TokenType.FLOOR, token));
                        case "round" -> tokens.add(new Token(TokenType.ROUND, token));
                        case "sign" -> tokens.add(new Token(TokenType.SIGN, token));
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
                case "abs" -> tokens.add(new Token(TokenType.ABS, token));
                case "sin" -> tokens.add(new Token(TokenType.SINUS, token));
                case "cos" -> tokens.add(new Token(TokenType.COS, token));
                case "tan" -> tokens.add(new Token(TokenType.TANGENT, token));
                case "cot" -> tokens.add(new Token(TokenType.COTANGENT, token));
                case "sec" -> tokens.add(new Token(TokenType.SECANT, token));
                case "csc" ->  tokens.add(new Token(TokenType.CSC, token));
                case "arcsin" -> tokens.add(new Token(TokenType.ASIN, token));
                case "arccos" -> tokens.add(new Token(TokenType.ACOS, token));
                case "arctan" -> tokens.add(new Token(TokenType.ATAN, token));
                case "arccot" -> tokens.add(new Token(TokenType.ACOT, token));
                case "sinh" -> tokens.add(new Token(TokenType.SINH, token));
                case "cosh" -> tokens.add(new Token(TokenType.COSH, token));
                case "tanh" -> tokens.add(new Token(TokenType.TANH, token));
                case "coth" -> tokens.add(new Token(TokenType.COTH, token));
                case "ceil" -> tokens.add(new Token(TokenType.CEIL, token));
                case "floor" -> tokens.add(new Token(TokenType.FLOOR, token));
                case "round" -> tokens.add(new Token(TokenType.ROUND, token));
                case "sign" -> tokens.add(new Token(TokenType.SIGN, token));
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

    public Token calculate(Token token) throws NotCalculateTokenException, FunctionBreakException {
        switch (type) {
            case ABS -> {
                if (Double.parseDouble(token.value) < 0) {
                    return new Token(TokenType.NUMBER, Double.toString(Double.parseDouble(token.value) * (-1)));
                } else {
                    return token;
                }
            }
            case COS -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.cos(Double.parseDouble(token.value))));
            }
            case SINUS -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.sin(Double.parseDouble(token.value))));
            }
            case TANGENT -> {
                if (Math.cos(Double.parseDouble(token.value)) == 0) {
                    throw new FunctionBreakException(value);
                }
                return new Token(TokenType.NUMBER, Double.toString(Math.tan(Double.parseDouble(token.value))));
            }
            case COTANGENT -> {
                if (Math.sin(Double.parseDouble(token.value)) == 0) {
                    throw new FunctionBreakException(value);
                }
                return new Token(TokenType.NUMBER, Double.toString(1.0 / Math.tan(Double.parseDouble(token.value))));
            }
            case SECANT -> {
                if (Math.cos(Double.parseDouble(token.value)) == 0) {
                    throw new FunctionBreakException(value);
                }
                return new Token(TokenType.NUMBER, Double.toString(1.0 / Math.cos(Double.parseDouble(token.value))));
            }
            case CSC -> {
                if (Math.sin(Double.parseDouble(token.value)) == 0) {
                    throw new FunctionBreakException(value);
                }
                return new Token(TokenType.NUMBER, Double.toString(1.0 / Math.sin(Double.parseDouble(token.value))));
            }
            case ASIN -> {
                if (Double.parseDouble(token.value) < -1 || Double.parseDouble(token.value) > 1) {
                    throw new FunctionBreakException(value);
                }
                return new Token(TokenType.NUMBER, Double.toString(Math.asin(Double.parseDouble(token.value))));
            }
            case ACOS -> {
                if (Double.parseDouble(token.value) < -1 || Double.parseDouble(token.value) > 1) {
                    throw new FunctionBreakException(value);
                }
                return new Token(TokenType.NUMBER, Double.toString(Math.acos(Double.parseDouble(token.value))));
            }
            case ATAN -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.atan(Double.parseDouble(token.value))));
            }
            case ACOT -> {
                if (Double.parseDouble(token.value) == 0) {
                    return new Token(TokenType.NUMBER, Double.toString(Math.PI / 2));
                }
                return new Token(TokenType.NUMBER, Double.toString(Math.atan(1.0 / Double.parseDouble(token.value))));
            }
            case COSH -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.cosh(Double.parseDouble(token.value))));
            }
            case SINH -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.sinh(Double.parseDouble(token.value))));
            }
            case TANH -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.tanh(Double.parseDouble(token.value))));
            }
            case COTH -> {
                if (Double.parseDouble(token.value) == 0) {
                    throw new FunctionBreakException(value);
                }
                return new Token(TokenType.NUMBER, Double.toString(1.0 / Math.tanh(Double.parseDouble(token.value))));
            }
            case CEIL -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.ceil(Double.parseDouble(token.value))));
            }
            case SIGN -> {
                if (Double.parseDouble(token.value) == 0) {
                    return new Token(TokenType.NUMBER, Double.toString(0));
                } else if (Double.parseDouble(token.value) < 0) {
                    return new Token(TokenType.NUMBER, Double.toString(-1));
                }
                return new Token(TokenType.NUMBER, Double.toString(1));
            }
            case FLOOR -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.floor(Double.parseDouble(token.value))));
            }
            case ROUND -> {
                return new Token(TokenType.NUMBER, Double.toString(Math.round(Double.parseDouble(token.value))));
            }
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
    SINH,
    COSH,
    TANH,
    COTH,
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

class FunctionBreakException extends Exception {
    String expression;
    FunctionBreakException(String expression) {
        this.expression = expression;
    }

    @Override
    public String getMessage() {
        return "Function " + expression + " cannot be calculated at a given point";
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