package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.exceptions.model.handler.RuleParserException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.handler.util.LambdaParser;
import it.polimi.ingsw.model.handler.util.Pair;
import it.polimi.ingsw.model.handler.util.TriPredicate;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;


public class RuleParser {
    private String line;
    private int numLine;
    private int indentationLevel;

    private List<Rule> rules;
    private Rule rule;
    private TriPredicate<Pair<Coord>, Pair<Coord>, Board> condition;
    private boolean isParsingCondition;
    private boolean hasSymbols;
    private boolean isSymbolic;
    private boolean isGeneration;
    private boolean isGenerated;

    private boolean secondary;
    private boolean isFile;
    private final Map<String, List<Rule>> generationMap;
    private final Set<String> idSet;
    private boolean idAdded;
    private List<Rule> rulesFromFile;
    private final Scanner scanner;



    public RuleParser(String file) throws FileNotFoundException {
        numLine = 0;
        indentationLevel = 0;
        condition = (oldPair, cPair, board) -> true;
        generationMap = new HashMap<>();
        idSet = new HashSet<>();
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(file);
        if (inputStream == null) {
            throw new FileNotFoundException("The file " + file + " does not exist.");
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

            if (indentationLevel == 0 && line.equals("rule:")) { // If new rule
                if (rule != null) {
                    setCondition();
                    if (!secondary) {
                        addRule();
                    }
                }
                checkAndReset();
                rule = new Rule();
            }
            else if (indentationLevel == 0 && line.matches("secondary\\x20+rule:")) { // If new rule
                if (rule != null) {
                    setCondition();
                    if (!secondary) {
                        addRule();
                    }
                }
                checkAndReset();
                rule = new Rule();
                secondary = true;
            }
            else if (indentationLevel == 0 && line.equals("file:")) {
                if (rule != null) {
                    setCondition();
                    if (!secondary) {
                        addRule();
                    }
                }
                checkAndReset();
                rule = null;
                isFile = true;
            }
            else if (indentationLevel == 0 && line.equals("secondary file:")) {
                if (rule != null) {
                    setCondition();
                    if (!secondary) {
                        addRule();
                    }
                }
                checkAndReset();
                rule = null;
                isFile = true;
                secondary = true;
            }
            else if (indentationLevel == 1) {
                if (line.contains("=")) { // If setting a parameter
                    String[] lineElements = line.split("=");
                    String parameter = lineElements[0].strip();
                    String value = lineElements[1].strip();
                    if (!isFile) {
                        setParameter(parameter, value);
                    } else {
                        setFileParameter(parameter, value);
                    }
                }
                else if (!isFile && line.matches("(condition|symbolicCondition):")) { // If beginning a condition
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

        if (rule != null) {
            setCondition();
            if (!secondary) {
                addRule();
            }
        }
        checkAndReset();
        scanner.close();

    }

    public List<Rule> getRules() {
        if (rules == null) {
            throw new IllegalStateException("A parsing is needed before calling this method.");
        }
        return new ArrayList<>(rules);
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
            error("Incorrect indentation. (Maybe using spaces instead of tabs?)");
        }

        line = line.substring(indentationLevel);
        line = line.strip();
    }

    private /*helper*/ void setParameter(String parameter, String value) throws RuleParserException {
        switch (parameter) {
            case "purpose": {
                try {
                    rule.setPurpose(Purpose.valueOf(value.toUpperCase()));
                    if (value.toUpperCase().equals("GENERATION")) {
                        isGeneration = true;
                    }
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
            When a new generation rule with id = ID is created:
            if (ID exists) --> rule.setGeneratedRules(generationMap.get(id))
            else --> generationMap.put(id, new ArrayList<>)
                     rule.setGeneratedRules(generationMap.get(id))
                     // This means: CREATE THE ASSOCIATION + SET THE GENERATED RULES FOR THE RULE
            When a rule is 'generatedBy' ID:
            if (ID exists) --> generationMap.get(ID).add(rule)
            else --> generationMap.put(ID, new ArrayList<>)
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
                isGenerated = true;
                break;
            }

            default: {
                error("Parameter " + parameter + " is invalid.");
            }
        }
    }

    private /*helper*/ void setFileParameter(String parameter, String value) throws RuleParserException {
        switch (parameter) {
            case "source": {
                RuleParser ruleParser;
                try {
                    ruleParser = new RuleParser(value);
                    ruleParser.parse();
                    rulesFromFile = ruleParser.getRules();
                    if (!secondary) {
                        rules.addAll(rulesFromFile);
                    }
                } catch (FileNotFoundException e) {
                    error("File " + value + " doesn't exist.");
                }
                break;
            }
            case "generatedBy": {
                if (!generationMap.containsKey(value)) {
                    generationMap.put(value, new ArrayList<>());
                }
                generationMap.get(value).addAll(rulesFromFile);
                isGenerated = true;
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
        if (idAdded && !isGeneration) {
            error("End of rule with ID assigned, but rule's purpose is not generation.");
        }
        else if (!idAdded && isGeneration) {
            error("End of generation rule without an ID.");
        }
        rules.add(rule);
    }

    private /*helper*/ void checkAndReset() throws RuleParserException {
        if(secondary && !isGenerated) {
            error("End of secondary rule (or file) that is not generated by any other rule.");
        }
        idAdded = false;
        isGeneration = false;
        secondary = false;
        isGenerated = false;
        isFile = false;
    }

    private /*helper*/ void error(String message) throws RuleParserException {
        throw new RuleParserException(message, line, numLine);
    }
}
