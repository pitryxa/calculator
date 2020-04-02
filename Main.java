import java.math.BigInteger;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Action(
                new AnalyzeInput(
                        new InputLine(
                                new Scanner(System.in)
                        )))
        .action();
    }
}

interface InputType extends Runnable {}

class InputLine {
    private final Scanner scanner;

    InputLine(Scanner scanner) {
        this.scanner = scanner;
    }

    public String input() {
        return scanner.nextLine();
    }
}

class AnalyzeInput {
    private final InputLine inputLine;

    AnalyzeInput(InputLine inputLine) {
        this.inputLine = inputLine;
    }

    public InputType analyze(Map<String, BigInteger> vars) {
        String input = new CondenseString(inputLine.input()).run();
        if (input.matches("\\s*/[a-z]+\\s*")) {
            return new Command(input);
        } else if (input.matches("\\s*[a-zA-Z]+\\s*=\\s*\\d+\\s*")) {
            return new InitVariable(input, vars);
        } else if (input.matches("\\s*[a-zA-Z]+\\s*")) {
            return new PrintVariable(input, vars);
        } else if(input.matches("[\\w\\s-+*/^()]+")){
            return new Expression(input, vars);
        } else if (input.isEmpty()) {
            return new Empty();
        } else {
            return new InvalidExp();
        }
    }
}

class Command implements InputType {
    private final String command;

    Command(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        switch (command) {
            case "/exit":
                exit();
                break;
            case "/help":
                help();
                break;
            default:
                unknown();
                break;
        }
    }

    private void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    private void help() {
        System.out.printf("The program calculates the result of an expression with integers. Including big integers.\n" +
                "Supported parenthesises and the following operations:\n" +
                "- addition ('+');\n" +
                "- subtraction ('-'), including an unary minus;\n" +
                "- multiplication ('*');\n" +
                "- division ('/');\n" +
                "- power ('^').\n" +
                "Supported also latin letters variables.\n" +
                "Available commands:\n" +
                "/help - display help\n" +
                "/exit - exit the program\n");
    }

    private void unknown() {
        System.out.println("Unknown command");
    }
}

class Expression implements InputType {
    private final String expression;
    private final Map<String, BigInteger> vars;

    Expression(String expression, Map<String, BigInteger> vars) {
        this.expression = expression;
        this.vars = vars;
    }

    @Override
    public void run() {
        new Postfix(
                new Infix(
                        expression),
                vars)
                .run();
    }
}

class Postfix {
    private final Infix infix;
    private final Map<String, BigInteger> vars;

    Postfix(Infix infix, Map<String, BigInteger> vars) {
        this.infix = infix;
        this.vars = vars;
    }

    public void run() {
        String postfix = infix.toPostfix();

        if ("Invalid expression".equals(postfix)) {
            System.out.println("Invalid expression");
        } else {
            System.out.println(calculate(postfix));
        }
    }

    private BigInteger calculate(String string) {
        String[] elements = string.split("\\s+");
        Deque<BigInteger> stackNumbers = new ArrayDeque<>();

        for (String e : elements) {
            switch (new Member(e).type()) {
                case NUMBER:
                    stackNumbers.offerLast(new BigInteger(e));
                    break;
                case VARIABLE:
                    stackNumbers.offerLast(vars.get(e));
                    break;
                case OPERATOR:
                    stackNumbers.offerLast(
                            doAction(stackNumbers, e));
            }
        }

        return stackNumbers.pollLast();
    }

    private BigInteger doAction(Deque<BigInteger> stack, String e) {
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

class Member {
    private final String member;

    Member(String member) {
        this.member = member;
    }

    public Type type() {
        if (member.matches("\\d+")) {
            return Type.NUMBER;
        } else if (member.matches("[a-zA-Z]+")) {
            return Type.VARIABLE;
        } else if (member.matches("[-+*/^]")) {
            return Type.OPERATOR;
        } else {
            return Type.UNKNOWN;
        }
    }
}

class CondenseString {
    private final String string;

    CondenseString(String string) {
        this.string = string;
    }

    public String run() {
        return string.replaceAll("\\s+", "");
    }
}

class Infix {
    private final String expression;

    Infix(String expression) {
        this.expression = expression;
    }

    public String toPostfix() {
        StringBuilder postfix = new StringBuilder("");
        Deque<Operator> stackOperators = new ArrayDeque<>();

        char[] chars = new CondenseString(expression).run().toCharArray();

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

class Operator implements Comparable {
    private final Operation operator;

    Operator(String operator) {
        this(operator.trim().charAt(0));
    }

    Operator(char operator) {
        this(new CharOperator(operator).charToOp());
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
}

class Symbol {
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

class InitVariable implements InputType {
    private final String initVar;
    private final Map<String, BigInteger> vars;

    InitVariable(String initVar, Map<String, BigInteger> vars) {
        this.initVar = initVar;
        this.vars = vars;
    }

    @Override
    public void run() {
        String[] e = initVar.split("=");
        if (e.length == 2) {
            String var = e[0];
            //int value = Integer.parseInt(e[1]);
            BigInteger value = new BigInteger(e[1]);
            if (vars.containsKey(var)) {
                vars.replace(var, value);
            } else {
                vars.put(var, value);
            }
        } else {
            System.out.println("Invalid expression");
        }
    }
}

class PrintVariable implements InputType {
    private final String variable;
    private final Map<String, BigInteger> vars;

    PrintVariable(String variable, Map<String, BigInteger> vars) {
        this.variable = variable;
        this.vars = vars;
    }

    @Override
    public void run() {
        System.out.println(
                vars.containsKey(variable)
                ? vars.get(variable)
                : "Unknown variable");
    }
}

class Empty implements InputType {

    @Override
    public void run() {

    }
}

class InvalidExp implements InputType {

    @Override
    public void run() {
        System.out.println("Invalid expression");
    }
}

class Action {
    private final AnalyzeInput analyzeInput;

    Action(AnalyzeInput analyzeInput) {
        this.analyzeInput = analyzeInput;
    }

    public void action() {
        Map<String, BigInteger> vars = new HashMap<>();
        System.out.println("Smart Calculator. Enter an expression or the /help command.");

        while (true) {
            analyzeInput.analyze(vars).run();
        }
    }
}

class CharOperator {
    private final char operator;

    CharOperator(char operator) {
        this.operator = operator;
    }

    public Operation charToOp() {
        for (Operation op : Operation.values()) {
            if (op.charOperator == operator) {
                return op;
            }
        }
        return Operation.UNKNOWN;
    }
}

enum Operation {
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

enum Type {
    NUMBER,
    VARIABLE,
    OPERATOR,
    //PARENTHESIS,
    UNKNOWN
}