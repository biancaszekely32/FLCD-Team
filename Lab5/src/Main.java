import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.readGrammarFromFile("Lab5/src/G2.txt");
        showMenu();

        Scanner scanner = new Scanner(System.in);
        int opt = -1;
        while (opt != 0) {
            try {
                opt = scanner.nextInt();
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
                    case 6:
                        LR lrAlg = new LR(grammar);
                        System.out.println(lrAlg.canonicalCollection().getStates());
                        break;
                    case 7:
                        PrintWriter writer = new PrintWriter("Lab5/src/out1.txt");
                        writer.print("");
                        writer.close();

                        LR lrAlg1 = new LR(grammar);
                        CanonicalCollection canonicalCollection = lrAlg1.canonicalCollection();

                        System.out.println("\nStates");
                        writeToFile("Lab5/src/out1.txt", "States");
                        for(int i = 0; i < canonicalCollection.getStates().size(); i++){
                            System.out.println(i + " " + canonicalCollection.getStates().get(i));
                            writeToFile("Lab5/src/out1.txt", i + " " + canonicalCollection.getStates().get(i));
                        }



                        System.out.println("\nTransitions");
                        writeToFile("Lab5/src/out1.txt", "\nTransitions");
                        for(Map.Entry<Pair<Integer, String>, Integer> entry: canonicalCollection.getGoToPairs().entrySet()){
                            System.out.println(entry.getKey() + " -> " + entry.getValue());
                            writeToFile("Lab5/src/out1.txt", entry.getKey() + " -> " + entry.getValue());
                        }

                        System.out.println();

                        ParsingTable parsingTable = lrAlg1.getParsingTable(canonicalCollection);
                        if(parsingTable.entries.size() == 0){
                            System.out.println("CONFLICTS IN THE PARSING TABLE");
                            writeToFile("Lab5/src/out1.txt", "CONFLICTS IN THE PARSING TABLE");
                        }
                        else {
                            System.out.println(parsingTable);
                        writeToFile("Lab5/src/out1.txt", parsingTable.toString());
                        }

                        Stack<String> word = readSequence("Lab5/src/Sequence.txt");

                        lrAlg1.parse(word, parsingTable, "Lab5/src/out1.txt");

                        break;

                    case 8:

                        LR lrAlg2 = new LR(grammar);
                        CanonicalCollection canonicalCollection2 = lrAlg2.canonicalCollection();

                        System.out.println("\nStates");
                        for(int i = 0; i < canonicalCollection2.getStates().size(); i++){
                            System.out.println(i + " " + canonicalCollection2.getStates().get(i));
                        }

                        System.out.println("\nTransitions");
                        for(Map.Entry<Pair<Integer, String>, Integer> entry: canonicalCollection2.getGoToPairs().entrySet()){
                            System.out.println(entry.getKey() + " -> " + entry.getValue());
                        }
                        System.out.println();

                        ParsingTable parsingTable2 = lrAlg2.getParsingTable(canonicalCollection2);
                        if(parsingTable2.entries.size() == 0){
                            System.out.println("CONFLICTS IN THE PARSING TABLE");
                            writeToFile("Lab5/src/out2.txt", "CONFLICTS IN THE PARSING TABLE");
                        }
                        else {
                            System.out.println(parsingTable2);
                        }

                        PrintWriter writer2 = new PrintWriter("Lab5/src/out2.txt");
                        writer2.print("");
                        writer2.close();

                        Analyzer scanner2 = new Analyzer("Lab5/src/P1.txt");
                        scanner2.analyze();

                        Stack<String> word2 = readFirstElemFromFile("Lab5/src/ProgramInternalForm.txt");
                        lrAlg2.parse(word2, parsingTable2, "Lab5/src/out2.txt");
                        break;

                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option.");
                scanner.next();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
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
        System.out.println("6. LR Algorithm: ");
        System.out.println("7. Run LR for GS.txt and parse Sequence.txt");
        System.out.println("8. Run LR for G2.txt");
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


    public static Stack<String> readSequence(String filename){
        BufferedReader reader;
        Stack<String> wordStack = new Stack<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            if(line != null){
                Arrays.stream(new StringBuilder(line).reverse().toString().split("")).forEach(wordStack::push);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return wordStack;
    }

    //
    public static Stack<String> readFirstElemFromFile(String filename) {
        BufferedReader reader;
        Stack<String> wordStack = new Stack<String>();
        ArrayList<String> normal = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while(line != null) {
                String[] split = line.split("\\s+");
                normal.add(split[0]);
                line = reader.readLine();
            }
            for(int i = normal.size() -1; i>=0 ; i--) {
                wordStack.add(normal.get(i));
            }
            return wordStack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }

}
