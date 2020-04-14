package processing.ExpressionCalculation;

public class Symbol {
    private final char symbol;

    Symbol(char symbol) {
        this.symbol = symbol;
    }

    public Type type() {
        if ((symbol >= 40 && symbol <= 43) ||
                symbol == 45 ||
                symbol == 47 ||
                symbol == 94) {
            return Type.OPERATOR;
        } else if (symbol >= 48 && symbol <= 57) {
            return Type.NUMBER;
        } else if ((symbol >= 65 && symbol <= 90) ||
                (symbol >= 97 && symbol <= 122)) {
            return Type.VARIABLE;
        } else {
            return Type.UNKNOWN;
        }
    }

    public String toString() {
        return Character.toString(symbol);
    }

    public char toChar() {
        return symbol;
    }
}
