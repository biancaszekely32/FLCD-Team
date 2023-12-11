import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class State {

    private Set<Item> items;

    public State(Set<Item> items) {
        this.items = items;
    }

    public Set<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return items.toString();
    }

    public Set<String> getSymbolsAfterTheDot() {
        Set<String> symbols = new HashSet<>();

        for (Item i : items) {
            if (i.getDotPosition() < i.getRhs().size())
                symbols.add(i.getRhs().get(i.getDotPosition()));
        }

        return symbols;
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        State state = (State) obj;
        return Objects.equals(items, state.items);
    }
}
