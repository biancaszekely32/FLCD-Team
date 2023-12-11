import java.util.List;
import java.util.Objects;

public class Item {

    private String lhs;
    private List<String> rhs;
    private Integer dotPosition;

    public Item(String lhs, List<String> rhs, Integer dotPos) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.dotPosition = dotPos;
    }

    public String getLhs() {
        return this.lhs;
    }

    public List<String> getRhs() {
        return this.rhs;
    }

    public Integer getDotPosition() {
        return this.dotPosition;
    }

    @Override
    public String toString() {
        List<String> rhs1 = this.rhs.subList(0, dotPosition);
        String stringRhs1 = String.join("", rhs1);

        List<String> rhs2 = this.rhs.subList(dotPosition, this.rhs.size());
        String stringRhs2 = String.join("", rhs2);

        return lhs + "->" + stringRhs1 + "." + stringRhs2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs, dotPosition);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(lhs, item.lhs) &&
                Objects.equals(rhs, item.rhs) &&
                Objects.equals(dotPosition, item.dotPosition);
    }
}
