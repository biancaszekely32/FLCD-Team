import java.util.ArrayList;
import java.util.List;

public class RowTable {

    public int stateIndex;

    public StateActionType action;

    public String reduceLhs;

    public List<String> reduceRhs = new ArrayList<>();

    public List<Pair<String, Integer>> shifts = new ArrayList<>();

    public String reduceProductionString(){
        return this.reduceLhs + " -> " + this.reduceRhs;
    }

    @Override
    public String toString(){
        return "Row: " +
                "stateIndex= " + stateIndex +
                ", action='" + action + '\'' +
                ", reduceLhs='" + reduceLhs + '\'' +
                ", reduceRhs = " + reduceRhs +
                ", shifts = " + shifts;
    }

}
