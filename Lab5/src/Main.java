import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.readGrammarFromFile("src/g1err.txt");
        showMenu();

        Scanner scanner = new Scanner(System.in);
        int opt = -1; // Initialize opt to a default value
        while (opt != 0) {
            try {
                opt = scanner.nextInt(); // Attempt to read an integer
                switch (opt) {
                    case 1:
                        grammar.printNonterminals();
                        break;
                    case 2:
                        grammar.printTerminals();
                        break;
                    case 3:
                        grammar.printProductions();
                        break;
                    case 4:
                        System.out.println("Enter the given nonterminal: ");
                        Scanner scanner1 = new Scanner(System.in);
                        String nonterminal = scanner1.nextLine();
                        printProductionsForNonterminal(grammar, nonterminal);
                        break;
                    case 5:
                        System.out.println("Check if CFG: ");
                        System.out.println(grammar.checkIfCFG());
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.next(); // Consume the invalid input to prevent an infinite loop
            }

            System.out.println();
            showMenu();
        }
    }

    private static void showMenu() {
        System.out.println("Choose one of the following options: ");
        System.out.println("1. The set of nonterminals: ");
        System.out.println("2. The set of terminals: ");
        System.out.println("3. The productions: ");
        System.out.println("4. Productions for a given nonterminal: ");
        System.out.println("5. Check if CFG: ");
        System.out.println("0. Exit");
        System.out.println("\nEnter option:");
    }

    private static void printProductionsForNonterminal(Grammar grammar, String nonterminal) {
        Set<String> productions = grammar.productionsForNonterminal(nonterminal);
        if(productions.isEmpty()) {
            System.out.println("No productions found for nonterminal: " + nonterminal);
            return;
        }
        System.out.println("Productions for " + nonterminal + ": " + String.join(" | ", productions));
    }
}
