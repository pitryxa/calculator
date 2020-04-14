package processing;

import processing.inputType.*;

import java.math.BigInteger;
import java.util.Map;

public class Controller {
    private final String inputLine;

    public Controller(String inputLine) {
        this.inputLine = inputLine;
    }

    public InputType analyzeInput(Map<String, BigInteger> vars) {

        if (inputLine.matches("\\s*/[a-z]+\\s*")) {
            return new Command(inputLine);
        } else if (inputLine.matches("\\s*[a-zA-Z]+\\s*=\\s*(\\d+|[a-zA-Z]+)\\s*")) {
            return new InitVariable(inputLine, vars);
        } else if (inputLine.matches("\\s*[a-zA-Z]+\\s*")) {
            return new PrintVariable(inputLine, vars);
        } else if(inputLine.matches("[\\w\\s-+*/^()]+")){
            return new Expression(inputLine, vars);
        } else if (inputLine.isEmpty()) {
            return new Empty();
        } else {
            return new InvalidExp();
        }
    }
}
