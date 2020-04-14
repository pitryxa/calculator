package input;

public class Input {
    private final String inputLine;

    public Input(String inputLine) {
        this.inputLine = inputLine;
    }

    public String condense() {
        return inputLine.replaceAll("\\s+", "");
    }
}
