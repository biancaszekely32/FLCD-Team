import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LR {

    private final Grammar grammar;
    private final Grammar enhancedGrammar;

    private List<Pair<String, List<String>>> orderedProductions;


    public LR(Grammar grammar) {
        this.grammar = grammar;
        this.enhancedGrammar = enhanceGrammar();
        this.orderedProductions = grammar.getOrderedProductions();
    }

    public Grammar enhanceGrammar() {
        Grammar enhancedGrammar = new Grammar();

        String newStartSymbol = "S'";
        enhancedGrammar.getNonterminals().add(newStartSymbol);

        Set<String> newStartProductions = new HashSet<>();
        newStartProductions.add(grammar.getInitialState());
        enhancedGrammar.getProductions().put(newStartSymbol, newStartProductions);

        enhancedGrammar.getNonterminals().addAll(grammar.getNonterminals());
        enhancedGrammar.getTerminals().addAll(grammar.getTerminals());
        enhancedGrammar.getProductions().putAll(grammar.getProductions());

        enhancedGrammar.initialState = newStartSymbol;

        return enhancedGrammar;
    }

    public String getNonterminalAfterDot(Item item) {
        try {
            String term = item.getRhs().get(item.getDotPosition());
            if (!grammar.getNonterminals().contains(term)) {
                return null;
            }
            return term;
        } catch (Exception e) {
            return null;
        }
    }

    //whenever we have a nonterminal after the dot, we add the productions for that nonterminal to the closure
    //we do this until we have no more nonterminals after the dot
    //
public State closure(Item item) {

    Set<Item> closureItems = new LinkedHashSet<>(Set.of(item));

    boolean updated;
    do {
        updated = false;

        Set<Item> itemsToAdd = new LinkedHashSet<>();

        for (Item currentItem : closureItems) {
            String nonTerminal = getNonterminalAfterDot(currentItem);

            if (nonTerminal != null) {
                for (String production : grammar.productionsForNonterminal(nonTerminal)) {
                    Item newItem = new Item(nonTerminal, Arrays.asList(production.trim().split(" ")), 0);
                   // System.out.println("new item is " + newItem);
                    //if we add a new item, we set updated to true; if it already exists we don't add it
                    if (closureItems.add(newItem)) {
                    //    System.out.println("-----added item " + newItem);
                        itemsToAdd.add(newItem);
                        updated = true;
                    }
                }
            }
        }

        closureItems.addAll(itemsToAdd);

    } while (updated);
    System.out.println("Closure items: " + closureItems);
    return new State(closureItems);
}

//we go through all the items in the state, and we look for the nonterminal after the dot
public State goTo(State state, String symbol) {
  //  System.out.println("------we are performing goTo on state: " + state + " and symbol:"+symbol);
    Set<Item> goToItems = new LinkedHashSet<>();

    for (Item item : state.getItems()) {
        String nonTerminal = item.getRhs().get(item.getDotPosition());

        if (nonTerminal != null && nonTerminal.equals(symbol)) {
            Item nextItem = new Item(item.getLhs(), item.getRhs(), item.getDotPosition() + 1);
            State newState = closure(nextItem);
            goToItems.addAll(newState.getItems());
        }
    }
    return new State(goToItems);
}
    public CanonicalCollection canonicalCollection() {
        System.out.println("Enhanced grammar: " + enhancedGrammar.getProductions());
        CanonicalCollection canonicalCollection = new CanonicalCollection();

        Set<String> initialProductions = enhancedGrammar.productionsForNonterminal(enhancedGrammar.getInitialState());
        List<String> initialProductionsList = new ArrayList<>(initialProductions);

        Item initialItem = new Item(
                enhancedGrammar.getInitialState(),
                initialProductionsList,
                0
        );

        canonicalCollection.addState(closure(initialItem));

        int index = 0;
        while (index < canonicalCollection.getStates().size()) {
            for (String symbol : canonicalCollection.getStates().get(index).getSymbolsAfterTheDot()) {
                State newState = goTo(canonicalCollection.getStates().get(index), symbol);
                if (newState.getItems().size() != 0) {
                    int indexState = canonicalCollection.getStates().indexOf(newState);
                    if (indexState == -1) {
                        canonicalCollection.addState(newState);
                        indexState = canonicalCollection.getStates().size() - 1;
                    }
                    canonicalCollection.pairStates(index, symbol, indexState);
                }
            }
            ++index;
        }

        return canonicalCollection;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public Grammar getEnhancedGrammar() {
        return enhancedGrammar;
    }

    public ParsingTable getParsingTable(CanonicalCollection canonicalCollection) throws Exception {
        ParsingTable parsingTable = new ParsingTable();

        for (int i = 0; i < this.canonicalCollection().getStates().size(); i++) {

            State state = this.canonicalCollection().getStates().get(i);
            RowTable row = new RowTable();

            // We set the number of the state (the index)
            row.stateIndex = i;

            row.action = state.getStateActionType();

            // We initialize the shifts list, in case there will be the case to add them.
            row.shifts = new ArrayList<>();

            //if we have conflicts, we stop the alg and print the state that has conflicts
            if (state.getStateActionType() == StateActionType.SHIFT_REDUCE_CONFLICT || state.getStateActionType() == StateActionType.REDUCE_REDUCE_CONFLICT) {
                for (Map.Entry<Pair<Integer, String>, Integer> e2 : canonicalCollection.getGoToPairs().entrySet()) {
                    Pair<Integer, String> k2 = e2.getKey();
                    Integer v2 = e2.getValue();

                    if (v2.equals(row.stateIndex)) {
                        System.out.println("State index -> " + row.stateIndex);
                        writeToFile("Lab5/src/out2.txt", "State index -> " + row.stateIndex);
                        System.out.println("Symbol -> " + k2.getSecond());
                        writeToFile("Lab5/src/out2.txt", "Symbol -> " + k2.getSecond());
                        System.out.println("Initial State -> " + k2.getFirst());
                        writeToFile("Lab5/src/out2.txt", "Initial State -> " + k2.getFirst());
                        System.out.println("( " + k2.getFirst() + ", " + k2.getSecond() + " )" + " -> " + row.stateIndex);
                        writeToFile("Lab5/src/out2.txt", "( " + k2.getFirst() + ", " + k2.getSecond() + " )" + " -> " + row.stateIndex);
                        System.out.println("STATE -> " + state);
                        writeToFile("Lab5/src/out2.txt", "State -> " + state);

                        break;
                    }
                }
                parsingTable.entries = new ArrayList<>();
                return parsingTable;

                //we set the reduceLhs and reduceRhs, of the item that has the dot at the end
            } else if (state.getStateActionType() == StateActionType.REDUCE) {
                Item item = state.getItems().stream()
                        .filter(item1 -> item1.getDotPosition() == item1.getRhs().size())
                        .findAny()
                        .orElse(null);
                if (item != null) {
                    row.shifts = null;
                    row.reduceLhs = item.getLhs();
                    row.reduceRhs = item.getRhs();
                } else {
                    throw new Exception("The item is null");
                }
                // we set the reduceLhs and reduceRhs to null
            } else if (state.getStateActionType() == StateActionType.ACCEPT) {
                row.reduceRhs = null;
                row.reduceLhs = null;
                row.shifts = null;

                //add all states that are created from the intial state to the shifts list
            } else if (state.getStateActionType() == StateActionType.SHIFT) {

                List<Pair<String, Integer>> shiftsList = new ArrayList<>();

                for (Map.Entry<Pair<Integer, String>, Integer> entry : canonicalCollection.getGoToPairs().entrySet()) {

                    Pair<Integer, String> key = entry.getKey();
                    if(key.getFirst() == row.stateIndex){
                        shiftsList.add(new Pair<>(key.getSecond(), entry.getValue()));
                    }
                }

                row.shifts = shiftsList;
                row.reduceRhs = null;
                row.reduceLhs = null;
            }

            parsingTable.entries.add(row);
        }

        return parsingTable;
    }

    public void parse(Stack<String> inputStack, ParsingTable parsingTable, String filePath) throws IOException {
        Stack<Pair<String, Integer>> workStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        Stack<Integer> outputNumberStack = new Stack<>();

        String lastSymbol = "";
        int stateIndex = 0;

        boolean isNotAccepted = true;

        workStack.push(new Pair<>(lastSymbol, stateIndex));
        RowTable lastRow = null;
        String onErrorSymbol = null;

        try {
            do{
                if(!inputStack.isEmpty()){
                    //save the last symbol from the input stack, in case there is an error
                    onErrorSymbol = inputStack.peek();
                }
                //save the last row from the parsing table, in case there is an error
                lastRow = parsingTable.entries.get(stateIndex);
                RowTable entry = parsingTable.entries.get(stateIndex);

                //if we have a shift, we pop the symbol from the input stack and push it on the work stack
                //we also push the state we go to on the work stack
                if(entry.action.equals(StateActionType.SHIFT)) {
                    String symbol = inputStack.pop();
                    Pair<String, Integer> state = entry.shifts.stream().filter(it -> it.getFirst().equals(symbol)).findAny().orElse(null);

                    if (state != null) {
                        stateIndex = state.getSecond();
                        lastSymbol = symbol;
                        workStack.push(new Pair<>(lastSymbol, stateIndex));
                    }
                    else {
                        throw new NullPointerException("The state is null");
                    }
                } else
                    // We push the non-terminal and the state we go to on the working stack
                    // We also push the production string on the output stack
                    if(entry.action.equals(StateActionType.REDUCE)){

                    List<String> reduceRhs = new ArrayList<>(entry.reduceRhs);

                    while(reduceRhs.contains(workStack.peek().getFirst()) && !workStack.isEmpty()){
                        reduceRhs.remove(workStack.peek().getFirst());
                        workStack.pop();
                    }

                    Pair<String, Integer> state = parsingTable.entries.get(workStack.peek().getSecond()).shifts.stream()
                            .filter(it -> it.getFirst().equals(entry.reduceLhs)).findAny().orElse(null);

                    stateIndex = state.getSecond();
                    lastSymbol = entry.reduceLhs;
                    workStack.push(new Pair<>(lastSymbol, stateIndex));

                    outputStack.push(entry.reduceProductionString());


                    // We push the production number on the output number stack
                    var index = new Pair<>(entry.reduceLhs, entry.reduceRhs);
                    int productionNumber = this.orderedProductions.indexOf(index);

                    outputNumberStack.push(productionNumber);
                } else {
                    if(entry.action.equals(StateActionType.ACCEPT)){
                        List<String> output = new ArrayList<>(outputStack);
                        Collections.reverse(output);
                        List<Integer> numberOutput = new ArrayList<>(outputNumberStack);
                        Collections.reverse(numberOutput);

                        System.out.println("ACCEPTED");
                        writeToFile(filePath, "ACCEPTED");
                        System.out.println("Production strings: " + output);
                        writeToFile(filePath, "Production strings: " + output);
                        System.out.println("Production number: " + numberOutput);
                        writeToFile(filePath, "Production number: " + numberOutput);

                        OutputTree outputTree = new OutputTree(grammar);
                        outputTree.generateTreeFromSequence(numberOutput);
                        System.out.println("The output tree: ");
                        writeToFile(filePath, "The output tree: ");
                        outputTree.printTree(outputTree.getRoot(), filePath);


                        isNotAccepted = false;
                    }

                }
            } while(isNotAccepted);
        } catch (NullPointerException ex){
            System.out.println("ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            System.out.println(lastRow);

            writeToFile(filePath, "ERROR at state " + stateIndex + " - before symbol " + onErrorSymbol);
            writeToFile(filePath, lastRow.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToFile(String file, String line) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line);
        bw.newLine();
        bw.close();
    }


}


