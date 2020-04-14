package processing.inputType;

import processing.ProcessingResult;

import java.math.BigInteger;
import java.util.Map;

public class InitVariable implements InputType {
    private final String initVar;
    private final Map<String, BigInteger> vars;

    public InitVariable(String initVar, Map<String, BigInteger> vars) {
        this.initVar = initVar;
        this.vars = vars;
    }

    @Override
    public ProcessingResult execute() {
        ProcessingResult result = null;
        String[] e = initVar.split("=");

        if (e.length == 2) {

            String var = e[0];
            BigInteger value = null;

            if (isNumber(e[1])) {
                value = new BigInteger(e[1]);
            } else {
                if (vars.containsKey(e[1])) {
                    value = vars.get(e[1]);
                } else {
                    result = new ProcessingResult(false, "Unknown variable");
                }
            }

            if (value != null) {
                if (vars.containsKey(var)) {
                    vars.replace(var, value);
                } else {
                    vars.put(var, value);
                }
                result = new ProcessingResult(false, "Variable is initialized");
            }

        } else {
            result = new ProcessingResult(false, "Invalid expression");
        }

        return result;
    }

    private boolean isNumber(String s) {
        return s.matches("\\d+");
    }
}
