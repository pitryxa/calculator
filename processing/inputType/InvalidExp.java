package processing.inputType;

import processing.ProcessingResult;

public class InvalidExp implements InputType {
    @Override
    public ProcessingResult execute() {
        return new ProcessingResult(false,"Invalid expression");
    }
}
