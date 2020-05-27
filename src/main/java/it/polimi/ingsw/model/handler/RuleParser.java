package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.exceptions.model.handler.RuleParserException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.handler.util.Pair;
import it.polimi.ingsw.model.handler.util.TriPredicate;

import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;


public class RuleParser {
    private String line;
    private int numLine;
    private int indentationLevel;
    private Rule rule;
    private TriPredicate<Pair<Coord>, Pair<Coord>, Board> condition;
    private boolean isParsingCondition;
    private boolean hasSymbols;
    private boolean isSymbolic;
    private final Map<String, List<Rule>> generationMap;
    private final Set<String> idSet;
    private boolean idAdded;
    private List<Rule> rules;
    private final Scanner scanner;



    public RuleParser(String file) {
        numLine = 0;
        indentationLevel = 0;
        condition = (oldPair, cPair, board) -> true;
        generationMap = new HashMap<>();
        idSet = new HashSet<>();
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("rules/example");
        //TODO: Remember to change to file instead of rules/example
        if (inputStream == null) {
            throw new IllegalArgumentException("The file " + file + " does not exist.");
        }
        scanner = new Scanner(inputStream);
    }

    public void parse() throws RuleParserException {
        //State check
        if (rules == null) {
            rules = new ArrayList<>();
        }
        else {
            throw new IllegalStateException("This method has already been called and " +
                    "can be called only once.");
        }

        //Loop for parsing
        while (scanner.hasNextLine()) {
            parseLine();

            if (indentationLevel != 2 && isParsingCondition) { // If condition is finished
                setCondition();
            }

            if (indentationLevel == 0 && line.equals("rule:")) { // If new rule
                if (rule != null) {
                    addRule();
                }
                rule = new Rule();
            }
            else if (indentationLevel == 1) {
                if (line.contains("=")) { // If setting a parameter
                    String[] lineElements = line.split("=");
                    String parameter = lineElements[0].strip();
                    String value = lineElements[1].strip();
                    setParameter(parameter, value);
                }
                else if (line.matches("(condition|symbolicCondition):")) { // If beginning a condition
                    condition = (oldPair, cPair, board) -> true;
                    isParsingCondition = true;
                    if (line.equals("symbolicCondition:")) isSymbolic = true;
                }
                else {
                    error("Unexpected line.");
                }
            }
            else if (indentationLevel == 2 && isParsingCondition) { // If parsing a condition
                try {
                    condition = condition.and(LambdaParser.extractPredicate(line));
                } catch (RuleParserException e) {
                    error(e.getMessage());
                }
                if (line.contains("oldBefore") || line.contains("oldAfter")) {
                    hasSymbols = true;
                    if (!isSymbolic) {
                        error("Symbols used in a non-symbolic condition.");
                    }
                }
            }
            else {
                error("Unexpected line.");
            }


        }
        setCondition();
        addRule();
        scanner.close();

    }

    public List<Rule> getRules() {
        if (rules == null) {
            throw new IllegalStateException("A parsing is needed before calling this method.");
        }
        return rules;
    }

    private /*helper*/ void parseLine() throws RuleParserException {
        do {
            line = scanner.nextLine();
            numLine++;
        } while (scanner.hasNextLine() && line.strip().equals(""));

        if (line.matches("\\S+.*")) {
            indentationLevel = 0;
        }
        else if (line.matches("\\t\\S+.*")) {
            indentationLevel = 1;
        }
        else if (line.matches("\\t\\t\\S+.*")) {
            indentationLevel = 2;
        }
        else {
            error("Incorrect indentation.");
        }

        line = line.substring(indentationLevel);
        line = line.strip();
    }

    private /*helper*/ void setParameter(String parameter, String value) throws RuleParserException {
        switch (parameter) {
            case "purpose": {
                try {
                    rule.setPurpose(Purpose.valueOf(value.toUpperCase()));
                }
                catch (IllegalArgumentException e) {
                    error("Value " + value + " is invalid for " + parameter + " parameter.");
                }
                break;
            }
            case "actionType": {
                try {
                    rule.setActionType(ActionType.valueOf(value.toUpperCase()));
                }
                catch (IllegalArgumentException e) {
                    error("Value " + value + " is invalid for " + parameter + " parameter.");
                }
                break;
            }
            case "decision": {
                try {
                    rule.setDecision(Decision.valueOf(value.toUpperCase()));
                }
                catch (IllegalArgumentException e) {
                    error("Value " + value + " is invalid for " + parameter + " parameter.");
                }
                break;
            }
            case "target": {
                try {
                    rule.setTarget(Target.valueOf(value.toUpperCase()));
                }
                catch (IllegalArgumentException e) {
                    error("Value " + value + " is invalid for " + parameter + " parameter.");
                }
                break;
            }
            case "buildLevel": {
                try {
                    rule.setBuildLevel(Level.valueOf(value.toUpperCase()));
                }
                catch (IllegalArgumentException e) {
                    error("Value " + value + " is invalid for " + parameter + " parameter.");
                }
                break;
            }
            case "forceSpaceFunction": {
                try {
                    BiFunction<Pair<Coord>, Pair<Coord>, Coord> coord =
                            LambdaParser.extractCoordFunction(value);
                    BiFunction<Coord, Coord, Coord> forceSpaceFunction = (before, after) ->
                            coord.apply(null, new Pair<>(before, after));
                    rule.setForceSpaceFunction(forceSpaceFunction);
                } catch (RuleParserException e) {
                    error(e.getMessage());
                }
                break;
            }

            /*
            GENERATION ASSOCIATION
            When a new generation rule is created:
            if (id exists) --> rule.setGeneratedRules(generationMap.get(id))
            else --> generationMap.put(id, new ArrayList<>)
                     rule.setGeneratedRules(generationMap.get(id))
                     // This means: CREATE THE ASSOCIATION
            When a rule is 'generatedBy' ID:
            if (ID exists) --> generationMap.get(ID).add(rule)
            else --> generationMap.put(id, new ArrayList<>)
                     generationMap.get(ID).add(rule)
                     // This means: CREATE THE ASSOCIATION + ADD THE RULE
         */
            case "id": {
                idAdded = true;
                if (idSet.contains(value)) {
                    error("A previously defined rule has the same ID " + value);
                }
                idSet.add(value);
                if (!generationMap.containsKey(value)) {
                    generationMap.put(value, new ArrayList<>());
                }
                rule.setGeneratedRules(generationMap.get(value));
                break;
            }

            case "generatedBy": {
                if (!generationMap.containsKey(value)) {
                    generationMap.put(value, new ArrayList<>());
                }
                generationMap.get(value).add(rule);
                break;
            }

            default: {
                error("Parameter " + parameter + " is invalid.");
            }
        }
    }

    private /*helper*/ void setCondition() throws RuleParserException {
        if (!isSymbolic) { // If 'condition'
            TriPredicate<Pair<Coord>, Pair<Coord>, Board> finalCondition = condition;
            BiPredicate<Pair<Coord>, Board> ruleCondition = (cPair, board) -> finalCondition.test(null, cPair, board);
            rule.setCondition(ruleCondition);
        }
        else if (hasSymbols) { // If 'symbolicCondition' with symbols
            rule.setSymbolicCondition(condition);
        }
        else { // If 'symbolicCondition' without symbols
            error("End of a symbolic condition with no symbols.");
        }

        isParsingCondition = false;
        isSymbolic = false;
        hasSymbols = false;
        condition = (oldPair, cPair, board) -> true;
    }

    private /*helper*/ void addRule() throws RuleParserException {
        if (idAdded && rule.getPurpose() != Purpose.GENERATION) {
            error("End of rule with ID assigned, but rule's purpose is not generation.");
        }
        else if (!idAdded && rule.getPurpose() == Purpose.GENERATION) {
            error("End of generation rule without an ID.");
        }

        idAdded = false;
        rules.add(rule);
    }

    private /*helper*/ void error(String message) throws RuleParserException {
        throw new RuleParserException(message, line, numLine);
    }
}
