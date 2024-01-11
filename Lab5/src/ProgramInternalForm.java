

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ProgramInternalForm {
    private List<String> operators;
    private List<String> separators;
    private List<String> reservedWords;
    private final HashMap<String, Integer> tokens;
    private List<Pair<String, Pair<Integer, Integer>>> pif = new ArrayList<>();

    public ProgramInternalForm(){
        operators = new ArrayList<>(List.of("+", "-", "*", "/", "%", "<", ">", "<=", ">=", "=", "and", "or", "==", "!="));
        separators = new ArrayList<>(List.of("[", "]", "(", ")", "{", "}", ":", ";", ",", "'", " ", "\n", "\t","."));
        reservedWords = new ArrayList<>(List.of("if", "@", "else", "read", "write", "integer", "string", "for", "in", "range", "while"));

        tokens = new HashMap<>();
        loadListOfTokens();
    }

    private void loadListOfTokens() {
        tokens.put("identifier", 1);
        tokens.put("constant", 0);

        int next = 2;

        List<List<String>> tokenGroups = List.of(reservedWords, separators, operators);

        for (List<String> tokenGroup : tokenGroups) {
            for (String token : tokenGroup) {
                tokens.put(token, next++);
            }
        }
    }

    public Integer getCode(String token) {
        return tokens.get(token);
    }

    public boolean isOperator(String token) {
        return operators.contains(token);
    }

    public boolean isPartOfOperator(char op) {
        // the case when we encounter ">= or <=" or "==" or "!="
        return op == '>' || op == '<' || op == '=' || op == '!' || isOperator(String.valueOf(op));

    }

    public boolean isSeparator(String token) {
        return separators.contains(token);
    }

    public boolean isReservedWord(String token) {
        return reservedWords.contains(token);
    }

    public boolean isIdentifier(String token) {
        String identifierPattern = "^[a-zA-Z]([a-zA-Z0-9_]*$)";
        String characterPattern ="^\'[a-zA-Z0-9_?!#*,./%-+=<>;)(}{ ]\'";
        String stringPattern = "^\"[a-zA-Z0-9_?!#*,./%-+=<>;)(}{ ]*\"";
        return token.matches(identifierPattern) || token.matches(characterPattern) || token.matches(stringPattern);
    }

    public boolean isIntegerConstant(String token){
        String integerPattern = "^0|[+|-][1-9]([0-9])*|[1-9]([0-9])*$";
        return token.matches(integerPattern);
    }

    public void addToPif(String token, Pair<Integer, Integer> pos) {
        Pair<String, Pair<Integer, Integer>> pair = new Pair<>(token, pos);
        pif.add(pair);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Pair<String, Pair<Integer, Integer>> pair : pif) {
            String token = pair.getFirst();
            Pair<Integer, Integer> pos = pair.getSecond();
            String formattedToken = null;

            if (isSeparator(token) || isOperator(token) || isReservedWord(token) ){
                formattedToken = token;
            }else if (isIdentifier(token)) {
                formattedToken = "identifier";
            } else if (isIntegerConstant(token)) {
                formattedToken = "constant";
            } else {
                formattedToken = token;
            }

            result.append(formattedToken).append("->").append(pos.toString()).append("\n");
        }
        return result.toString();
    }

}