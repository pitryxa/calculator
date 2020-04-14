package processing.inputType;

import processing.ProcessingResult;

public class Empty implements InputType {
    @Override
    public ProcessingResult execute() {
        return new ProcessingResult(false,"");
    }
}
