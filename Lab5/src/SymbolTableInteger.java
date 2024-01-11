public class SymbolTableInteger {

    private HashTable<Integer> hashTable;

    public SymbolTableInteger(Integer size) {
        hashTable = new HashTable<>(size);
    }

    public boolean addSymbolInteger(Integer symbol) {
        if (hashTable.put(symbol))
            return hashTable.put(symbol);
        return false;
    }

    public Integer getSymbolPosition(Integer symbol) {
        Pair position = hashTable.findPosOfKey(symbol);
        if (position != null) {
            return (Integer) position.getFirst();
        }
        return null;
    }

    public String displayTable() {
        return hashTable.displayTable();
    }}