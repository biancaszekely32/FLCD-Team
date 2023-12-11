import java.util.*;

public class CanonicalCollection {

    private List<State> states;

    public CanonicalCollection() {
        this.states = new ArrayList<>();

    }

    public void addState(State state) {
        this.states.add(state);
    }

    public List<State> getStates() {
        return this.states;
    }

}
