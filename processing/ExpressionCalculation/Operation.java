package processing.ExpressionCalculation;

public enum Operation {
    UNKNOWN(-1, ' '),
    LEFT_PARENTHESIS(0, '('),
    RIGHT_PARENTHESIS(0, ')'),
    ADDITION(1, '+'),
    SUBTRACTION(1, '-'),
    MULTIPLICATION(2, '*'),
    DIVISION(2, '/'),
    POWER(3, '^'),
    UNAR_MINUS(4,'-');

    int priority;
    char charOperator;

    Operation(int priority, char operator) {
        this.priority = priority;
        this.charOperator = operator;
    }
}
