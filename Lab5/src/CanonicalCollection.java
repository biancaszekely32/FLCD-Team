import java.util.*;

public class CanonicalCollection {

    private List<State> states;
    private Map<Pair<Integer, String>, Integer> goToPairs;

    public CanonicalCollection() {
        this.states = new ArrayList<>();
        this.goToPairs = new LinkedHashMap<>();

    }

    public void pairStates(Integer indexFirstState, String symbol, Integer indexSecondState){
        this.goToPairs.put(new Pair<>(indexFirstState, symbol), indexSecondState);
    }
    public Map<Pair<Integer, String>, Integer> getGoToPairs(){
        return this.goToPairs;
    }

    public void addState(State state) {
        this.states.add(state);
    }

    public List<State> getStates() {
        return this.states;
    }

}
