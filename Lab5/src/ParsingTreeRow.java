public class ParsingTreeRow {

    private Integer index;
    private String info;
    private ParsingTreeRow father;
    private ParsingTreeRow sibling;

    private ParsingTreeRow child;

    private Integer level;

    public ParsingTreeRow(String info) {
        this.info = info;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index){
        this.index = index;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public ParsingTreeRow getFather() {
        return father;
    }

    public void setFather(ParsingTreeRow father){
        this.father = father;
    }

    public ParsingTreeRow getSibling() {
        return sibling;
    }

    public void setSibling(ParsingTreeRow sibling){
        this.sibling = sibling;
    }

    public ParsingTreeRow getChild(){
        return this.child;
    }

    public void setChild(ParsingTreeRow child){
        this.child = child;
    }

    public Integer getLevel(){
        return this.level;
    }

    public void setLevel(Integer level){
        this.level = level;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();

        result.append("Row: ");
        result.append("index = ").append(index);
        result.append(", info = ").append(info);
        result.append(", child = ").append(child != null ? child.getIndex() : -1);
        result.append(", sibling = ").append(sibling != null ? sibling.getIndex() : -1);
        result.append(", father = ").append(father != null ? father.getIndex() : -1);
        result.append(", level = ").append(level);

        return result.toString();
    }
}
