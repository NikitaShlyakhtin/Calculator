public class InvalidOperationException extends Exception {
    String expression;
    InvalidOperationException(String expression) {
        this.expression = expression;
    }

    @Override
    public String getMessage() {
        return "Operation" + expression + "is not a valid operation";
    }
}
