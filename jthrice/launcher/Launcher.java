package jthrice.launcher;

/** Launches the compiler. */
public class Launcher {
    public static void main(String[] arguments) {
        System.out.println("Thrice Java Compiler v.0.0.1");
        System.out.println("Running with arguments:");
        for (int i = 0; i < arguments.length; i++) {
            System.out.printf("[%d] %s\n", i, arguments[i]);
        }
    }
}
