package processing.inputType;

import processing.ProcessingResult;

import java.math.BigInteger;
import java.util.Map;

public class PrintVariable implements InputType {
    private final String variable;
    private final Map<String, BigInteger> vars;

    public PrintVariable(String variable, Map<String, BigInteger> vars) {
        this.variable = variable;
        this.vars = vars;
    }

    @Override
    public ProcessingResult execute() {
        ProcessingResult result;

        if (vars.containsKey(variable)) {
            result = new ProcessingResult(false, vars.get(variable).toString());
        } else {
            result = new ProcessingResult(false, "Unknown variable");
        }

        return result;
    }
}
