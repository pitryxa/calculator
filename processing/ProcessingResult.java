package processing;

public class ProcessingResult {
    private final boolean exit;
    private final String outputLine;

    public ProcessingResult(boolean exit, String outputLine) {
        this.exit = exit;
        this.outputLine = outputLine;
    }

    public boolean isExit() {
        return exit;
    }

    public String getOutputLine() {
        return outputLine;
    }
}
