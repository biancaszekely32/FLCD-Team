

import java.util.ArrayList;

public class HashTable<T> {

    private Integer size;
    private ArrayList<ArrayList<T>> table;

    public HashTable(Integer size) {
        this.size = size;
        this.table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.table.add(new ArrayList<>());

        }
    }

    public Integer hash(T key) {
        int hash=0;
        if (key instanceof Integer) {
            hash = (int) key % size;
        } else if (key instanceof String) {
            for (char c : ((String) key ).toCharArray()) {
                hash = (31 * hash + c) % size;
            }
            hash= Math.abs(hash);
        }
        return hash;
    }

    public Integer getSize() {
        return size;
    }

    public Pair<Integer,Integer> findPosOfKey(T key) {
        int index = hash(key);

        if (!table.get(index).isEmpty()) {
            ArrayList<T> elems = this.table.get(index);
            for (int i = 0; i < elems.size(); i++) {
                if (elems.get(i).equals(key)) {
                    return new Pair<>(index, i);
                }
            }
        }
        return null;
    }

    public boolean put(T key) {
        if (findPosOfKey(key) == null) {
            int index = hash(key);
            ArrayList<T> list = this.table.get(index);
            list.add(key);
            return true;
        }
        else return false;
    }


    public T get(Pair<Integer,Integer> key) {
        int pos = (int) key.getFirst();
        int symbol = (int) key.getSecond();
        return this.table.get(pos).get(symbol);
    }

    public T getByPos(int pos) {
        return this.table.get(pos).get(0);
    }


    public String displayTable() {
        StringBuilder sb = new StringBuilder("ST { \n");
        for (ArrayList<T> list : table) {
            sb.append(list).append(", ").append("\n");
        }
        sb.setLength(sb.length() - 2); // Remove the trailing comma and space
        sb.append(" }");
        return sb.toString();
    }
}