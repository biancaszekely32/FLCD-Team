import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class State {


    private StateActionType stateActionType;
    private Set<Item> items;

    public State(Set<Item> items) {
        this.items = items;
        this.setActionForState();
    }

    public Set<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return  stateActionType +  "->" + items.toString();
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

    public StateActionType getStateActionType(){
        return this.stateActionType;
    }


    public void setActionForState(){
        if(items.size() == 1 && ((Item)items.toArray()[0]).getRhs().size() == ((Item)items.toArray()[0]).getDotPosition() && ((Item)this.items.toArray()[0]).getLhs() == "S'"){
            this.stateActionType = StateActionType.ACCEPT;
        } else if(items.size() == 1 && ((Item) items.toArray()[0]).getRhs().size() == ((Item) items.toArray()[0]).getDotPosition())
        {
            this.stateActionType = StateActionType.REDUCE;
        } else if(items.size() >= 1 && this.items.stream().allMatch(i -> i.getRhs().size() > i.getDotPosition())){
            this.stateActionType = StateActionType.SHIFT;
        } else if(items.size() > 1 && this.items.stream().allMatch(i -> i.getRhs().size() == i.getDotPosition())){
            this.stateActionType = StateActionType.REDUCE_REDUCE_CONFLICT;
        } else {
            this.stateActionType = StateActionType.SHIFT_REDUCE_CONFLICT;
        }
    }
}
