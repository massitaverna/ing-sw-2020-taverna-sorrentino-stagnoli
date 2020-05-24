/*
Contract logic:
The caller must clean the argument to be passed to the callee (i.e. strip() + reduceParentheses())
The callee can safely call parseArguments() on the argument passed by the caller
*/

package it.polimi.ingsw.model.handler.util;

import it.polimi.ingsw.exceptions.model.handler.RuleParserException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coord;
import it.polimi.ingsw.model.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleParser {
    private String line;
    private int numLine;
    public static final Pattern coordPattern =
            Pattern.compile("coord\\x20*\\(\\x20*(-?\\d)\\x20*,\\x20*(-?\\d)\\x20*\\)");
    public static final Pattern levelPattern =
            Pattern.compile("GROUND|LVL1|LVL2|LVL3|DOME");
    public static final Pattern booleanOperator =
            Pattern.compile("<|<=|==|>=|>");
    public static final Pattern arithmeticOperator =
            Pattern.compile("[+-]");

    /*public void parse() throws LambdaParserException {
        while (indentation) {
            String line = nextLine();
            extractPredicate(line);
        }
    }*/

    public BiPredicate<Pair<Coord>, Board> extractPredicate(String line) throws RuleParserException {
        BiPredicate<Pair<Coord>, Board> condition = null;
        String function = getFunction(line);
        String argument = getArgument(line);

        if (!Pattern.matches("\\(.+\\)", argument)) {
            error("Argument format of " + function.toUpperCase() +
                    " operation is not correct.");
        }
        argument = reduceParentheses(argument);

        switch (function) {
            case "occupied": {
                Function<Pair<Coord>, Coord> arg = fromCoordToFunction(argument);
                condition = (cPair, board) -> board.getSpace(arg.apply(cPair)).isOccupied();
                break;
            }

            case "dome": {
                Function<Pair<Coord>, Coord> arg = fromCoordToFunction(argument);
                condition = (cPair, board) -> board.getSpace(arg.apply(cPair)).isDome();
                break;
            }

            case "near": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 2) {
                    error(function.toUpperCase() + " takes exactly 2 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                List<Function<Pair<Coord>, Coord>> coords = new ArrayList<>();
                for (String arg : arguments) {
                    coords.add(fromCoordToFunction(arg));
                }

                condition = (cPair, board) -> coords.get(0).apply(cPair).isNear(coords.get(1).apply(cPair));
                // START_TEST
                Function<Pair<Coord>, Pair<Coord>> f = (cPair) ->
                        new Pair<>(coords.get(0).apply(cPair), coords.get(1).apply(cPair));
                Function<Pair<Coord>, Boolean> g = (cPair) ->
                        coords.get(0).apply(cPair).isNear(coords.get(1).apply(cPair));
                Pair<Coord> pair = new Pair<>(new Coord(3,3), new Coord(4,4));
                System.out.println("Coordinates: " + f.apply(pair) + "\nNear? " + g.apply(pair));
                // END_TEST
                break;
            }

            case "samePlayer": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 2) {
                    error(function.toUpperCase() + " takes exactly 2 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                List<Function<Pair<Coord>, Coord>> coords = new ArrayList<>();
                for (String arg : arguments) {
                    coords.add(fromCoordToFunction(arg));
                }

                condition = (cPair, board) ->
                        board.getWorkerCopy(coords.get(0).apply(cPair)).getPlayerNickname()
                        .equals(board.getWorkerCopy(coords.get(1).apply(cPair)).getPlayerNickname());
                break;
            }

            case "valid": {
                Function<Pair<Coord>, Coord> arg = fromCoordToFunction(argument);
                condition = (cPair, board) -> Coord.validCoord(arg.apply(cPair));
                break;
            }
            case "compareLevels": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 3) {
                    error(function.toUpperCase() + " takes exactly 3 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                List<BiFunction<Pair<Coord>,Board, Level>> levels = new ArrayList<>();
                levels.add(fromLevelToFunction(arguments.get(0)));
                levels.add(fromLevelToFunction(arguments.get(1)));
                String comparator = arguments.get(2);

                if (comparator.equals("<")) {
                    condition = (cPair, board) -> levels.get(0).apply(cPair, board).ordinal() <
                            levels.get(1).apply(cPair, board).ordinal();
                } else if (comparator.equals("=")) {
                    condition = (cPair, board) -> levels.get(0).apply(cPair, board).ordinal() ==
                            levels.get(1).apply(cPair, board).ordinal();
                } else if (comparator.equals(">")) {
                    condition = (cPair, board) -> levels.get(0).apply(cPair, board).ordinal() >
                            levels.get(1).apply(cPair, board).ordinal();
                } else if (comparator.matches("\\d")) {
                    condition = (cPair, board) -> levels.get(1).apply(cPair, board).ordinal() -
                            levels.get(0).apply(cPair, board).ordinal() ==
                            Integer.parseInt(arguments.get(2));
                } else {
                    error("Incorrect comparator: " + comparator);
                }
                break;
            }

            /*case "compareLevels": {
                List<String> arguments = Arrays.asList(argument.split(booleanOperator.pattern()));
                arguments = new ArrayList<>(arguments);
                if (arguments.size() != 2) {
                    error("More than one boolean operator in same comparison.");
                }
                Pattern arithmeticExpression = Pattern.compile(
                        "\\x20*"+"(\\S+)"+arithmeticOperator.pattern()+"(\\S+)"+"\\x20*");
                Matcher m;
                List<Level>
                for (String arg : arguments) {
                    int first;
                    int second;
                    m = arithmeticExpression.matcher(arg);
                    if (m.matches()) {
                        if (levelPattern.matcher(m.group(1)).matches()) {
                            first = Level.valueOf(m.group(1)).ordinal();
                        } else first = Integer.parseInt(m.group(1));
                        if (levelPattern.matcher(m.group(2)).matches()) {
                            second = Level.valueOf(m.group(2)).ordinal();
                        } else second = Integer.parseInt(m.group(2));
                        if (arg.contains("+")) {

                        } else {

                        }
                    }
                }
                List<Function<Pair<Coord>, Coord>> coords = new ArrayList<>();
                coords.add(fromCoordToFunction(arguments.get(0)));
                coords.add(fromCoordToFunction(arguments.get(1)));

                String comparator = arguments.get(2);
                if (comparator.equals("=")) {
                    //condition = (cPair, board) -> board.getSpace(coords.get(0).apply(cPair)).getHeight().ordinal()
                }
                break;
            }*/

            case "negate": {
                BiPredicate<Pair<Coord>, Board> internalPredicate = extractPredicate(argument);
                condition = internalPredicate.negate();
                break;
            }

            case "or":
            case "and": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 2) {
                    error(function.toUpperCase() + " takes exactly 2 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                String argumentOne = arguments.get(0);
                String argumentTwo = arguments.get(1);

                if (function.equals("or")) {
                    condition = extractPredicate(argumentOne).or(extractPredicate(argumentTwo));
                } else {
                    condition = extractPredicate(argumentOne).and(extractPredicate(argumentTwo));
                }
                break;
            }

            default:
                error(function + " function does not exist.");
        }

        return condition;
    }




    //-----------------------------HELPER METHODS------------------------------

    private List<String> /*helper*/ parseArguments(String source) throws RuleParserException {

        /*COMMAS IDENTIFICATION*/
        List<String> splitsOnComma = Arrays.asList(source.split(","));
        splitsOnComma = new ArrayList<>(splitsOnComma);

        if (splitsOnComma.contains("")) {
            error("Found empty argument(s) (,,) in " + source);
        }

        /*ARGUMENTS IDENTIFICATION*/
        List<String> arguments = new ArrayList<>();
        int count = 0;
        int start = 0;

        for (int i = 0; i < splitsOnComma.size(); i++) {
            String piece = splitsOnComma.get(i);

            count += piece.chars().filter(c -> c == '(').count() -
                    piece.chars().filter(c -> c == ')').count();

            if (count == 0) {
                String arg = splitsOnComma.subList(start, i + 1).stream()
                        .map(s -> s + ",").reduce(String::concat).orElse(piece);
                arg = arg.substring(0, arg.length() - 1);
                arg = arg.strip();
                arg = reduceParentheses(arg);
                arguments.add(arg);
                start = i + 1;
            }
        }
        return arguments;
    }

    private String /*helper*/ getFunction(String line) {
        String function = line.split("\\(", 2)[0];
        function = function.strip();
        return function;
    }

    private String /*helper*/ getArgument(String line) {
        String argument = "(" + line.split("\\(", 2)[1];
        argument = argument.strip();
        return argument;
    }

    private String /*helper*/ reduceParentheses(String source) {
        while (Pattern.matches("\\(.+\\)", source)) {
            boolean canReduce = false;
            int count = 0;
            for (int pos = 0; pos < source.length(); pos++) {
                // Test case: or((or(f(a),g(b))),h(c))
                char c = source.charAt(pos);
                if (c == '(') count++;
                if (c == ')') count--;
                if (count == 0) {
                    if(pos == source.length() - 1) {
                        canReduce = true;
                    }
                    break;
                }
            }
            if (canReduce) {
                source = source.substring(1, source.length() - 1);
                source = source.strip();
            }
            else {
                break;
            }
        }
        return source;
    }

    private int /*helper*/ fromSymbolToValue(String c) throws RuleParserException {
        int index = -1;

        if (c.equals("before")) {
            index = 0;
        }
        else if (c.equals("after")) {
            index = 1;
        }
        else {
            error("Symbol " + c + " is unknown.");
        }
        return index;
    }

    private Function<Pair<Coord>,Coord> /*helper*/ fromCoordToFunction(String c)
            throws RuleParserException {

        Function<Pair<Coord>,Coord> result = null;
        Matcher m = coordPattern.matcher(c);

        if (c.equals("before")) {
            result = (cPair -> cPair.get(0));
        } else if (c.equals("after")) {
            result = (cPair -> cPair.get(1));
        } else if (m.matches()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            result = cPair -> new Coord(x, y);
        } else if (c.startsWith("sum")) {
            String sumArgument = c.split("sum", 2)[1];
            sumArgument = sumArgument.strip();
            sumArgument = reduceParentheses(sumArgument);
            result = getSumFunction(sumArgument);
        } else if (c.startsWith("diff")) {
            String diffArgument = c.split("diff", 2)[1];
            diffArgument = diffArgument.strip();
            diffArgument = reduceParentheses(diffArgument);
            result = getDiffFunction(diffArgument);
        }
        else {
            error("A coordinate can only be 'before', 'after', a constant like [n,m] " +
                    "a sum or a diff of two coordinates. Provided: " + c);
        }
        return result;
    }

    private /*helper*/ BiFunction<Coord, Coord, Coord> fromCoordToBiFunction(String c) throws RuleParserException {
        Function<Pair<Coord>, Coord> function = fromCoordToFunction(c);
        BiFunction<Coord, Coord, Coord> biFunction =
                (before, after) -> function.apply(new Pair<>(before, after));
        return biFunction;
    }

    private BiFunction<Pair<Coord>, Board, Level> /*helper*/ fromLevelToFunction(String level)
            throws RuleParserException {

        BiFunction<Pair<Coord>, Board, Level> result = null;
        Matcher m = levelPattern.matcher(level);

        if (level.equals("before")) {
            result = (cPair, board) -> board.getSpace(cPair.get(0)).getHeight();
        } else if (level.equals("after")) {
            result = (cPair, board) -> board.getSpace(cPair.get(1)).getHeight();
        } else if (m.matches()) {
            result = (cPair, board) -> Level.valueOf(level);
        } else {
            error("A level can only be 'before', 'after', or a constant like " +
                    "GROUND, LVLn, DOME. Provided: " + level);
        }
        return result;
    }

    private Function<Pair<Coord>, Coord> /*helper*/ getSumFunction(String argument)
            throws RuleParserException {

        List<String> arguments = parseArguments(argument);
        if (arguments.size() != 2) {
            error("SUM takes exactly 2 " +
                    "arguments, " + arguments.size() + " passed:\n" + arguments);
        }

        /*
        if (
                !arguments.stream().allMatch(arg -> arg.equals("before") ||
                        arg.equals("after") ||
                        coordPattern.matcher(arg).matches())
        ) {
            error("A coord in SUM function " +
                    "can only be 'before', 'after', or like [n,m]. Provided: " + arguments);
        }
        */
        List<Function<Pair<Coord>, Coord>> coords = new ArrayList<>();
        for (String arg : arguments) {
            coords.add(fromCoordToFunction(arg));
        }


        Function<Pair<Coord>, Coord> firstAddend = coords.get(0);
        Function<Pair<Coord>, Coord> secondAddend = coords.get(1);
        Function<Pair<Coord>, Coord> sumFunction =
                cPair -> firstAddend.apply(cPair).sum(secondAddend.apply(cPair));
        /*
        String firstAddend = arguments.get(0);
        String secondAddend = arguments.get(1);

        if (!firstAddend.equals("before") && !firstAddend.equals("after")) {
            String temp = firstAddend;
            firstAddend = secondAddend;
            secondAddend = temp;
        }
        System.out.println("1:" + firstAddend + ";2:" + secondAddend);

        if (firstAddend.equals("before") || firstAddend.equals("after")) {
            int firstAddendValue = fromSymbolToValue(firstAddend);
            if (secondAddend.equals("before") || secondAddend.equals("after")) {
                int secondAddendValue = fromSymbolToValue(firstAddend);
                sumFunction = cPair -> cPair.get(firstAddendValue).sum(cPair.get(secondAddendValue));
            }
            else {
                Matcher m = coordPattern.matcher(secondAddend);
                m.matches();
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                sumFunction = cPair -> cPair.get(firstAddendValue).sum(new Coord(x, y));
            }
        }
        else {
            Matcher m1 = coordPattern.matcher(firstAddend);
            m1.matches();
            Matcher m2 = coordPattern.matcher(secondAddend);
            m2.matches();
            int x1 = Integer.parseInt(m1.group(1));
            int y1 = Integer.parseInt(m1.group(2));
            int x2 = Integer.parseInt(m2.group(1));
            int y2 = Integer.parseInt(m2.group(2));
            sumFunction = cPair -> new Coord(x1 + x2, y1 + y2);
        }
        */
        return sumFunction;
    }

    private Function<Pair<Coord>, Coord> /*helper*/ getDiffFunction(String argument)
            throws RuleParserException {

        List<String> arguments = parseArguments(argument);
        if (arguments.size() != 2) {
            error("DIFF takes exactly 2 " +
                    "arguments, " + arguments.size() + " passed:\n" + arguments);
        }

        List<Function<Pair<Coord>, Coord>> coords = new ArrayList<>();
        for (String arg : arguments) {
            coords.add(fromCoordToFunction(arg));
        }

        Function<Pair<Coord>, Coord> minuend = coords.get(0);
        Function<Pair<Coord>, Coord> subtrahend =
                cPair -> new Coord(-coords.get(1).apply(cPair).x, -coords.get(1).apply(cPair).y);
        Function<Pair<Coord>, Coord> diffFunction =
                cPair -> minuend.apply(cPair).sum(subtrahend.apply(cPair));
        return diffFunction;
    }

    private /*helper*/ void error(String message) throws RuleParserException {
        throw new RuleParserException(message, line, numLine);
    }
}
