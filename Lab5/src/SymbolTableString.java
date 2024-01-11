public class SymbolTableString {
    private HashTable<String> hashTable;

    public SymbolTableString(Integer size) {
        hashTable = new HashTable<>(size);
    }

    public boolean addSymbolString(String symbol) {
        if (hashTable.put(symbol))
            return hashTable.put(symbol);
        return false;
    }

    public Integer getSymbolPosition(String symbol) {
        Pair position = hashTable.findPosOfKey(symbol);
        if (position != null) {
            return (Integer) position.getFirst();
        }
        return null;
    }

    public String displayTable() {
        return hashTable.displayTable();
    }}