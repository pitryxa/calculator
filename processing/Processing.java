package processing;

import processing.inputType.InputType;

import java.math.BigInteger;
import java.util.Map;

public class Processing {
    private String input;
    private Map<String, BigInteger> vars;

    public Processing(String input, Map<String, BigInteger> vars) {
        this.input = input;
        this.vars = vars;
    }

    public ProcessingResult process() {
        InputType inputType = new Controller(input).analyzeInput(vars);
        return inputType.execute();
    }
}
