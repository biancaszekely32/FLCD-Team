import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputTree {

    private ParsingTreeRow root;

    private final Grammar grammar;

    private int currentIndex = 1;

    private int indexInInput = 1;

    private int maxLevel = 0;

    private List<ParsingTreeRow> treeList;

    public OutputTree(Grammar grammar){
        this.grammar = grammar;
    }

    public ParsingTreeRow getRoot(){
        return this.root;
    }

    //generate the tree from the input sequence
    public ParsingTreeRow generateTreeFromSequence(List<Integer> inputSequence){
        int productionIndex = inputSequence.get(0);

        Pair<String, List<String>> productionString = this.grammar.getOrderedProductions().get(productionIndex);

        this.root = new ParsingTreeRow(productionString.getFirst());
        this.root.setIndex(0);
        this.root.setLevel(0);

        this.root.setChild(buildRecursive(1, this.root, productionString.getSecond(), inputSequence));

        return this.root;
    }


    //build each node recursively and set its right sibling and left child (only for non-terminal)
    public ParsingTreeRow buildRecursive(int level, ParsingTreeRow parent, List<String> currentRhs, List<Integer> inputSequence){
        if(currentRhs.isEmpty() || this.indexInInput >= inputSequence.size() + 1){
            return null;
        }

        String currentSymbol = currentRhs.get(0);

        if(this.grammar.getTerminals().contains(currentSymbol)){
            ParsingTreeRow node = new ParsingTreeRow(currentSymbol);
            node.setIndex(this.currentIndex);
            this.currentIndex++;

            node.setLevel(level);
            node.setFather(parent);

            // a new list with the rest of the rhs
            List<String> newList = new ArrayList<>(currentRhs);
            newList.remove(0);

            //call the recursion again to set the right sibling
            node.setSibling(buildRecursive(level, parent, newList, inputSequence));

            return node;
        }
        else if(this.grammar.getNonterminals().contains(currentSymbol)){
            int productionIndex = inputSequence.get(this.indexInInput);
            Pair<String, List<String>> productionString = this.grammar.getOrderedProductions().get(productionIndex);
            ParsingTreeRow node = new ParsingTreeRow(currentSymbol);
             node.setIndex(this.currentIndex);
             node.setLevel(level);
             node.setFather(parent);
             int newLevel = level + 1;
             if(newLevel > this.maxLevel)
                 this.maxLevel = newLevel;

             this.currentIndex++;
             this.indexInInput++;

             node.setChild(buildRecursive(newLevel, node, productionString.getSecond(), inputSequence));

             List<String> newList = new ArrayList<>(currentRhs);
             newList.remove(0);

             node.setSibling(buildRecursive(level, parent, newList, inputSequence));

             return node;
        }
        else {
            System.out.println("ERROR");
            return null;
        }
    }

    public void printTree(ParsingTreeRow node, String filePath) throws IOException {
        this.treeList = new ArrayList<>();
        createList(node);

        for(int i = 0; i <= this.maxLevel; i++){
            for(ParsingTreeRow n: this.treeList){
                if(n.getLevel() == i){
                    printAndWriteToFile(filePath, n);
                }
            }
        }
    }

    public void printAndWriteToFile(String file, ParsingTreeRow line) throws IOException {
        System.out.println(line);
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line.toString());
        bw.newLine();
        bw.close();
    }


    //create a list with all the nodes from the tree in order
    public void createList(ParsingTreeRow node){
        if(node == null)
            return;

        while(node != null){

            this.treeList.add(node);
            if(node.getChild() != null){
                createList(node.getChild());
            }

            node = node.getSibling();
        }

    }



}
