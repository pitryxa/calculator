import input.Input;
import output.Output;
import processing.Processing;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        System.out.println("Smart Calculator. Enter an expression or the '/help' command.");
        Map<String, BigInteger> vars = new HashMap<>();
        boolean exit = false;

        do {
            Input input = new Input(
                    new Scanner(System.in)
                            .nextLine());

            Processing processing = new Processing(
                    input.condense(),
                    vars);

            Output output = new Output(
                    processing.process());

            output.print();
            exit = output.isExit();

        } while (!exit);
    }
}