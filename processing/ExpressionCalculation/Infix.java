package processing.ExpressionCalculation;

import java.util.ArrayDeque;
import java.util.Deque;

public class Infix {
    private final String expression;

    public Infix(String expression) {
        this.expression = expression;
    }

    public String toPostfix() {
        StringBuilder postfix = new StringBuilder("");
        Deque<Operator> stackOperators = new ArrayDeque<>();

        char[] chars = expression.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            switch (new Symbol(chars[i]).type()) {
                case NUMBER:
                case VARIABLE:
                    if (i != 0 &&
                            new Symbol(chars[i - 1]).type() == Type.OPERATOR) {
                        postfix.append(' ');
                    }
                    postfix.append(chars[i]);
                    break;
                case UNKNOWN:
                    return "Invalid expression";
                case OPERATOR:
                    if (!operatorProcessing(postfix, stackOperators, new Operator(chars[i]), chars, i)) {
                        return "Invalid expression";
                    }
                    break;
                default:
                    break;
            }
        }

        while (!stackOperators.isEmpty()) {
            if (stackOperators.peekLast().isLeftPar()) {
                return "Invalid expression";
            }
            postfix.append(' ').append(stackOperators.pollLast().asChar());
        }

        return postfix.toString().trim();
    }

    private boolean operatorProcessing(StringBuilder sb, Deque<Operator> stack, Operator op, char[] chars, int i) {
        Symbol prevSym = null;
        if (i != 0) {
            prevSym = new Symbol(chars[i - 1]);
        }

        switch (op.get()) {
            case ADDITION:
                if (prevSym == null ||
                        prevSym.toString().matches("[-+(*/^]")) {
                    break;
                }
                while (!stack.isEmpty() &&
                        op.compareTo(stack.peekLast()) <= 0) {
                    sb.append(' ').append(stack.pollLast().asChar());
                }
                stack.offerLast(op);
                break;
            case SUBTRACTION:
                if (prevSym != null && prevSym.toChar() == '-') {
                    break;
                }

                int minusCount = 1;
                for (int j = i + 1; j < chars.length; j++) {
                    if (chars[j] == '-') {
                        minusCount++;
                    } else {
                        break;
                    }
                }

                Operator temp = new Operator(minuses(minusCount));
                if (prevSym == null ||
                        prevSym.toString().matches("[+(*/^]")) {
                    if (temp.asChar() == '-') {
                        sb.append(' ').append('0');
                        temp = new Operator(Operation.UNAR_MINUS);
                    } else {
                        break;
                    }
                } else {
                    while (!stack.isEmpty() &&
                            op.compareTo(stack.peekLast()) <= 0) {
                        sb.append(' ').append(stack.pollLast().asChar());
                    }
                }
                stack.offerLast(temp);

                break;
            case DIVISION:
            case POWER:
            case MULTIPLICATION:
                if (prevSym == null ||
                        prevSym.toString().matches("[-+(*/^]")) {
                    return false;
                }
                while (!stack.isEmpty() &&
                        op.compareTo(stack.peekLast()) < 0) {
                    sb.append(' ').append(stack.pollLast().asChar());
                }
                stack.offerLast(op);
                break;
            case LEFT_PARENTHESIS:
                if (prevSym != null &&
                        prevSym.type().ordinal() < 2) {
                    return false;
                }
                stack.offerLast(op);
                break;
            case RIGHT_PARENTHESIS:
                if (stack.isEmpty() ||
                        prevSym == null ||
                        prevSym.type() == Type.OPERATOR) {
                    return false;
                }
                Operator stackOp;
                while (!(stackOp = stack.pollLast()).isLeftPar()) {
                    if (stack.isEmpty()) {
                        return false;
                    }
                    sb.append(' ').append(stackOp.asChar());
                }
                break;
            case UNKNOWN:
                return false;
        }
        return true;
    }

    private char minuses(int cnt) {
        if (cnt % 2 == 0) {
            return '+';
        } else {
            return '-';
        }
    }
}
