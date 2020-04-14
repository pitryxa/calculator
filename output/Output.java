package output;

import processing.ProcessingResult;

public class Output {
    private final boolean exit;
    private final String outputLine;

    public Output(ProcessingResult result) {
        this.exit = result.isExit();
        this.outputLine = result.getOutputLine();
    }

    public boolean isExit() {
        return exit;
    }

    public void print() {
        System.out.println(outputLine);
    }
}
