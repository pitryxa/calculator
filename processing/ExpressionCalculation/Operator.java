package processing.ExpressionCalculation;

import java.util.Objects;

public class Operator implements Comparable {
    private final Operation operator;

    Operator(String operator) {
        this(operator.trim().charAt(0));
    }

    Operator(char operator) {
        this.operator = charToOperation(operator);
    }

    Operator(Operation operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object obj) {
        if (    !super.equals(obj) ||
                !(obj instanceof Operator) ||
                !Objects.equals(this.operator, ((Operator) obj).get())) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object o) {
        return Integer.compare(this.priority(), ((Operator) o).priority());
    }

    public Operation get() {
        return operator;
    }

    public int priority() {
        return operator.priority;
    }

    public char asChar() {
        return operator.charOperator;
    }

    public boolean isLeftPar() {
        return this.get() == Operation.LEFT_PARENTHESIS;
    }

    public Operation charToOperation(char operator) {
        for (Operation op : Operation.values()) {
            if (op.charOperator == operator) {
                return op;
            }
        }
        return Operation.UNKNOWN;
    }
}
