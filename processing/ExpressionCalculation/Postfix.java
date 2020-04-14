package processing.ExpressionCalculation;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Postfix {
    private final String expression;
    private final Map<String, BigInteger> vars;

    public Postfix(String expression, Map<String, BigInteger> vars) {
        this.expression = expression;
        this.vars = vars;
    }

    private Type type(String s) {
        if (s.matches("\\d+")) {
            return Type.NUMBER;
        } else if (s.matches("[a-zA-Z]+")) {
            return Type.VARIABLE;
        } else if (s.matches("[-+*/^]")) {
            return Type.OPERATOR;
        } else {
            return Type.UNKNOWN;
        }
    }

    public String execute() {
        String result;

        if ("Invalid expression".equals(expression)) {
            result = expression;
        } else {
            result = calculate(expression);
        }

        return result;
    }

    private String calculate(String string) {
        String[] elements = string.split("\\s+");
        Deque<BigInteger> stackNumbers = new ArrayDeque<>();

        for (String e : elements) {
            switch (type(e)) {
                case NUMBER:
                    stackNumbers.offerLast(new BigInteger(e));
                    break;
                case VARIABLE:
                    if (vars.containsKey(e)) {
                        stackNumbers.offerLast(vars.get(e));
                    } else {
                        return "Unknown variable";
                    }
                    break;
                case OPERATOR:
                    stackNumbers.offerLast(doOperation(stackNumbers, e));
            }
        }

        return stackNumbers.pollLast().toString();
    }

    private BigInteger doOperation(Deque<BigInteger> stack, String e) {
        BigInteger result = BigInteger.ONE;
        BigInteger a, b;

        switch (new Operator(e).get()) {
            case ADDITION:
                result = stack.pollLast().add(stack.pollLast());
                break;
            case SUBTRACTION:
                b = stack.pollLast();
                a = stack.pollLast();
                result = a.subtract(b);
                break;
            case MULTIPLICATION:
                result = stack.pollLast().multiply(stack.pollLast());
                break;
            case DIVISION:
                b = stack.pollLast();
                a = stack.pollLast();
                result = a.divide(b);
                break;
            case POWER:
                b = stack.pollLast();
                a = stack.pollLast();
                result = a.pow(b.intValue());
                break;
            default:
                break;
        }

        return result;
    }
}
