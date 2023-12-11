import java.util.*;

public class LR {

    private final Grammar grammar;
    private final Grammar enhancedGrammar;


    public LR(Grammar grammar) {
        this.grammar = grammar;
        this.enhancedGrammar = enhanceGrammar();
    }

    public Grammar enhanceGrammar() {
        Grammar enrichedGrammar = new Grammar();

        String newStartSymbol = "S'";
        enrichedGrammar.getNonterminals().add(newStartSymbol);

        Set<String> newStartProductions = new HashSet<>();
        newStartProductions.add(grammar.getInitialState());
        enrichedGrammar.getProductions().put(newStartSymbol, newStartProductions);

        enrichedGrammar.getNonterminals().addAll(grammar.getNonterminals());
        enrichedGrammar.getTerminals().addAll(grammar.getTerminals());
        enrichedGrammar.getProductions().putAll(grammar.getProductions());

        enrichedGrammar.initialState = newStartSymbol;

        return enrichedGrammar;
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
                    System.out.println(newItem);

                    if (closureItems.add(newItem)) {
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

public State goTo(State state, String symbol) {
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
        int stateNumber = 0;

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
                    }
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
}
