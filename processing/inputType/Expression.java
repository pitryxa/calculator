package processing.inputType;

import processing.ExpressionCalculation.Infix;
import processing.ExpressionCalculation.Postfix;
import processing.ProcessingResult;

import java.math.BigInteger;
import java.util.Map;

public class Expression implements InputType {
    private final String expression;
    private final Map<String, BigInteger> vars;

    public Expression(String expression, Map<String, BigInteger> vars) {
        this.expression = expression;
        this.vars = vars;
    }

    @Override
    public ProcessingResult execute() {
        Infix infix = new Infix(expression);
        Postfix postfix = new Postfix(infix.toPostfix(), vars);

        return new ProcessingResult(false, postfix.execute());
    }
}
