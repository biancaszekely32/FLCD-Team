import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Grammar {
    private Set<String> nonterminals, terminals;
    private final Map<String, Set<String>> productions;
    String initialState;
    public static final String EPSILON = "epsilon";



    public Grammar() {
        this.nonterminals = new HashSet<>();
        this.terminals = new HashSet<>();
        this.productions = new HashMap<>();
        this.initialState = "";
    }


    public Set<String> getNonterminals() {
        return nonterminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Map<String, Set<String>> getProductions() {
        return productions;
    }

    public String getInitialState() {
        return initialState;
    }

    public void readGrammarFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);

            processNonterminals(reader);
            processTerminals(reader);
            processInitialState(reader);
            processProductions(reader);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processNonterminals(Scanner reader) {
        String nonterminalsLine = reader.nextLine();
        nonterminals = new HashSet<>(Arrays.asList(nonterminalsLine.split(" ")));
    }

    private void processTerminals(Scanner reader) {
        String terminalsLine = reader.nextLine();
        terminals = new HashSet<>(Arrays.asList(terminalsLine.split(" ")));
    }

    private void processProductions(Scanner reader) {
        while (reader.hasNextLine()) {
            String productionLine = reader.nextLine();
            String[] productionElements = productionLine.split("->");

            if (productionElements.length == 2) {
                String nonterminal = productionElements[0].trim();
                String[] productionSymbols = productionElements[1].trim().split("\\|");

                if (nonterminals.isEmpty()) {
                    initialState = nonterminal;
                }

                nonterminals.add(nonterminal);

                for (String production : productionSymbols) {
                    production = production.trim();
                    productions.computeIfAbsent(nonterminal, k -> new HashSet<>()).add(production);

                    String[] symbols = production.split(" ");
                    for (String symbol : symbols) {
                        if (!nonterminals.contains(symbol) && !symbol.equals(EPSILON)) {
                            terminals.add(symbol);
                        }
                    }
                }
            }
        }
    }

    private void processInitialState(Scanner reader) {
        String initialStateLine = reader.nextLine();
        initialState = initialStateLine.trim();
    }

    public void printNonterminals() {
        System.out.println("Nonterminals: " + String.join(", ", nonterminals));
    }

    public void printTerminals() {
        System.out.println("Terminals: " + String.join(", ", terminals));
    }

    public void printProductions() {
        System.out.println("Productions:");
        for (Map.Entry<String, Set<String>> entry : productions.entrySet()) {
            System.out.println(entry.getKey() + " -> " + String.join(" | ", entry.getValue()));
        }
    }

    public Set<String> productionsForNonterminal(String nonterminal) {
        if (!nonterminals.contains(nonterminal)) {
            return Collections.emptySet();
        }

        return productions.getOrDefault(nonterminal, Collections.emptySet());
    }


    public boolean checkIfCFG() {
        if (!this.nonterminals.contains(this.initialState)) {
            return false;
        }

        for (Map.Entry<String, Set<String>> entry : this.productions.entrySet()) {
            String leftHandSide = entry.getKey();
            Set<String> productions = entry.getValue();

            String[] nonterminalsOnLeft = leftHandSide.split(" ");

            // Check if there is exactly one nonterminal on the left-hand side
            if (nonterminalsOnLeft.length != 1) {
                System.out.println("Invalid number of nonterminals on left-hand side: " + leftHandSide);
                return false;
            }

            String nonterminal = nonterminalsOnLeft[0];

            if (!this.nonterminals.contains(nonterminal)) {
                System.out.println("Invalid nonterminal in left-hand side: " + nonterminal);
                return false;
            }

            for (String production : productions) {
                // Check each symbol in the production
                String[] symbols = production.split(" ");
                for (String symbol : symbols) {
                    if (!this.nonterminals.contains(symbol) && !this.terminals.contains(symbol) && !symbol.equals(EPSILON)) {
                        System.out.println("Invalid symbol in production: " + symbol);
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
