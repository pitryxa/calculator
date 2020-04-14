package processing.inputType;

import processing.ProcessingResult;

public class Command implements InputType {
    private final String command;

    public Command(String command) {
        this.command = command;
    }

    @Override
    public ProcessingResult execute() {
        ProcessingResult result;
        switch (command) {
            case "/exit":
                result = exit();
                break;
            case "/help":
                result = help();
                break;
            default:
                result = unknown();
                break;
        }
        return result;
    }

    private ProcessingResult exit() {
        return new ProcessingResult(true, "Bye!");
    }

    private ProcessingResult help() {
        return new ProcessingResult(false, "The program calculates the result of an expression with integers. Including big integers.\n" +
                "Supported parenthesises and the following operations:\n" +
                "- addition ('+');\n" +
                "- subtraction ('-'), including an unary minus;\n" +
                "- multiplication ('*');\n" +
                "- division ('/');\n" +
                "- power ('^').\n" +
                "Supported also latin letters variables.\n" +
                "Available commands:\n" +
                "/help - display help\n" +
                "/exit - exit the program\n");
    }

    private ProcessingResult unknown() {
        return new ProcessingResult(false, "Unknown command");
    }
}
