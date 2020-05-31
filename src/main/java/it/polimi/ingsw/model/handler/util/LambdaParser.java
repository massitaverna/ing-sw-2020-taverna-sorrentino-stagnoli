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


import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LambdaParser {

    public static final Pattern coordPattern =
            Pattern.compile("coord\\x20*\\(\\x20*(-?\\d)\\x20*,\\x20*(-?\\d)\\x20*\\)");

    public static TriPredicate<Pair<Coord>, Pair<Coord>, Board> extractPredicate(String line) throws RuleParserException {

        TriPredicate<Pair<Coord>, Pair<Coord>, Board> condition;
        String function = getFunction(line);
        String argument = getArgument(line);
        argument = reduceParentheses(argument);

        switch (function) {
            case "occupied": {
                BiFunction<Pair<Coord>, Pair<Coord>, Coord> arg = fromCoordToSymbolicFunction(argument);
                condition = (oldPair, cPair, board) -> board.getSpace(arg.apply(oldPair, cPair)).isOccupied();
                break;
            }

            case "dome": {
                BiFunction<Pair<Coord>, Pair<Coord>, Coord> arg = fromCoordToSymbolicFunction(argument);
                condition = (oldPair, cPair, board) -> board.getSpace(arg.apply(oldPair, cPair)).isDome();
                break;
            }

            case "near": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 2) {
                    throw new RuleParserException(function.toUpperCase() + " takes exactly 2 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                List<BiFunction<Pair<Coord>, Pair<Coord>, Coord>> coords = new ArrayList<>();
                for (String arg : arguments) {
                    coords.add(fromCoordToSymbolicFunction(arg));
                }

                condition = (oldPair, cPair, board) -> coords.get(0).apply(oldPair, cPair)
                        .isNear(coords.get(1).apply(oldPair, cPair));
                break;
            }

            case "equalCoordinates": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 2) {
                    throw new RuleParserException(function.toUpperCase() + " takes exactly 2 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                List<BiFunction<Pair<Coord>, Pair<Coord>, Coord>> coords = new ArrayList<>();
                for (String arg : arguments) {
                    coords.add(fromCoordToSymbolicFunction(arg));
                }

                condition = (oldPair, cPair, board) -> coords.get(0).apply(oldPair, cPair)
                        .equals(coords.get(1).apply(oldPair, cPair));
                break;
            }

            case "samePlayer": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 2) {
                    throw new RuleParserException(function.toUpperCase() + " takes exactly 2 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                List<BiFunction<Pair<Coord>, Pair<Coord>, Coord>> coords = new ArrayList<>();
                for (String arg : arguments) {
                    coords.add(fromCoordToSymbolicFunction(arg));
                }

                condition = (oldPair, cPair, board) ->
                        board.getWorkerCopy(coords.get(0).apply(oldPair, cPair)).getPlayerNickname()
                                .equals(board.getWorkerCopy(coords.get(1).apply(oldPair, cPair)).getPlayerNickname());
                break;
            }

            case "valid": {
                BiFunction<Pair<Coord>, Pair<Coord>, Coord> arg = fromCoordToSymbolicFunction(argument);
                condition = (oldPair, cPair, board) -> Coord.validCoord(arg.apply(oldPair, cPair));
                break;
            }
            case "compareLevels": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 3) {
                    throw new RuleParserException(function.toUpperCase() + " takes exactly 3 " +
                            "arguments, " + arguments.size() + " passed.");
                }

                List<TriFunction<Pair<Coord>, Pair<Coord>, Board, Level>> levels = new ArrayList<>();
                levels.add(fromLevelToSymbolicFunction(arguments.get(0)));
                levels.add(fromLevelToSymbolicFunction(arguments.get(1)));
                String comparator = arguments.get(2);

                if (comparator.matches("=\\d")) {
                    condition = (oldPair, cPair, board) ->
                            levels.get(1).apply(oldPair, cPair, board).ordinal() -
                                    levels.get(0).apply(oldPair, cPair, board).ordinal() ==
                                    Integer.parseInt(arguments.get(2).substring(1));
                } else if (comparator.matches("<\\d")) {
                    condition = (oldPair, cPair, board) ->
                            levels.get(1).apply(oldPair, cPair, board).ordinal() -
                                    levels.get(0).apply(oldPair, cPair, board).ordinal() <
                                    Integer.parseInt(arguments.get(2).substring(1));
                } else if (comparator.matches(">\\d")) {
                    condition = (oldPair, cPair, board) ->
                            levels.get(1).apply(oldPair, cPair, board).ordinal() -
                                    levels.get(0).apply(oldPair, cPair, board).ordinal() >
                                    Integer.parseInt(arguments.get(2).substring(1));
                } else {
                    throw new RuleParserException("Incorrect comparator: " + comparator);
                }
                break;
            }

            case "negate": {
                TriPredicate<Pair<Coord>, Pair<Coord>, Board> internalPredicate = extractPredicate(argument);
                condition = internalPredicate.negate();
                break;
            }

            case "or":
            case "and": {
                List<String> arguments = parseArguments(argument);
                if (arguments.size() != 2) {
                    throw new RuleParserException(function.toUpperCase() + " takes exactly 2 " +
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
                throw new RuleParserException(function + " function does not exist.");
        }

        return condition;
    }

    public static BiFunction<Pair<Coord>, Pair<Coord>, Coord> extractCoordFunction(String argument) throws RuleParserException {
        return fromCoordToSymbolicFunction(argument);
    }


    //-----------------------------HELPER METHODS------------------------------

    static List<String> /*helper*/ parseArguments(String source) throws RuleParserException {

        /*COMMAS IDENTIFICATION*/
        List<String> splitsOnComma = Arrays.asList(source.split(","));
        splitsOnComma = new ArrayList<>(splitsOnComma);

        if (splitsOnComma.stream().anyMatch(s -> s.matches("\\s*"))) {
            throw new RuleParserException("Found empty argument(s) (,,) in " + source);
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
                        .map(s -> s + ",").reduce(String::concat).orElseThrow(); // before: .orElse(piece)
                arg = arg.substring(0, arg.length() - 1);
                arg = arg.strip();
                arg = reduceParentheses(arg);
                arguments.add(arg);
                start = i + 1;
            }
        }
        return arguments;
    }

    static String /*helper*/ getFunction(String line) {
        String function = line.split("\\(", 2)[0];
        function = function.strip();
        return function;
    }

    static String /*helper*/ getArgument(String line) throws RuleParserException {
        if (!line.matches(".*\\(.+\\)")) {
            throw new RuleParserException("Argument format is incorrect.");
        }
        String argument = "(" + line.split("\\(", 2)[1];
        argument = argument.strip();
        return argument;
    }

    static String /*helper*/ reduceParentheses(String source) {
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

    static /*helper*/ BiFunction<Pair<Coord>, Pair<Coord>, Coord> fromCoordToSymbolicFunction(String c)
            throws RuleParserException {
        BiFunction<Pair<Coord>, Pair<Coord>, Coord> result;
        Matcher m = coordPattern.matcher(c);

        if (c.equals("before")) {
            result = (oldPair, cPair) -> cPair.get(0);
        } else if (c.equals("after")) {
            result = (oldPair, cPair) -> cPair.get(1);
        } else if (c.equals("oldBefore")) {
            result = (oldPair, cPair) -> oldPair.get(0);
        } else if (c.equals("oldAfter")) {
            result = (oldPair, cPair) -> oldPair.get(1);
        } else if (m.matches()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            result = (oldPair, cPair) -> new Coord(x, y);
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
            throw new RuleParserException("A coordinate can only be 'before', 'after', 'oldBefore', 'oldAfter', " +
                    "a constant like coord(n,m), a sum or a diff of two coordinates. Provided: " + c);
        }
        return result;
    }

    static /*helper*/ TriFunction<Pair<Coord>, Pair<Coord>, Board, Level> fromLevelToSymbolicFunction(String level)
            throws RuleParserException {

        TriFunction<Pair<Coord>, Pair<Coord>, Board, Level> result;

        if (level.toUpperCase().matches("GROUND|LVL1|LVL2|LVL3|DOME")) {
            result = (oldPair, cPair, board) -> Level.valueOf(level.toUpperCase());
        } else {
            try {
                BiFunction<Pair<Coord>, Pair<Coord>, Coord> coord = fromCoordToSymbolicFunction(level);
                result = (oldPair, cPair, board) -> board.getSpace(coord.apply(oldPair, cPair)).getHeight();
            }
            catch (RuleParserException e) {
                throw new RuleParserException("A level can only be a constant like GROUND, LVLn, DOME or " +
                        "referenced through its coordinate. Provided: " + level);
            }
        }
        return result;
    }


    static /*helper*/ BiFunction<Pair<Coord>, Pair<Coord>, Coord> getSumFunction(String argument)
            throws RuleParserException {

        List<String> arguments = parseArguments(argument);
        if (arguments.size() != 2) {
            throw new RuleParserException("SUM takes exactly 2 " +
                    "arguments, " + arguments.size() + " passed:\n" + arguments);
        }

        List<BiFunction<Pair<Coord>, Pair<Coord>, Coord>> coords = new ArrayList<>();
        for (String arg : arguments) {
            coords.add(fromCoordToSymbolicFunction(arg));
        }


        BiFunction<Pair<Coord>, Pair<Coord>, Coord> firstAddend = coords.get(0);
        BiFunction<Pair<Coord>, Pair<Coord>, Coord> secondAddend = coords.get(1);
        BiFunction<Pair<Coord>, Pair<Coord>, Coord> sumFunction =
                (oldPair, cPair) -> firstAddend.apply(oldPair, cPair).sum(secondAddend.apply(oldPair, cPair));

        return sumFunction;
    }

    static /*helper*/ BiFunction<Pair<Coord>, Pair<Coord>, Coord> getDiffFunction(String argument)
            throws RuleParserException {

        List<String> arguments = parseArguments(argument);
        if (arguments.size() != 2) {
            throw new RuleParserException("DIFF takes exactly 2 " +
                    "arguments, " + arguments.size() + " passed:\n" + arguments);
        }

        List<BiFunction<Pair<Coord>, Pair<Coord>, Coord>> coords = new ArrayList<>();
        for (String arg : arguments) {
            coords.add(fromCoordToSymbolicFunction(arg));
        }

        BiFunction<Pair<Coord>, Pair<Coord>, Coord> minuend = coords.get(0);
        BiFunction<Pair<Coord>, Pair<Coord>, Coord> subtrahend =
                (oldPair, cPair) -> new Coord(-coords.get(1).apply(cPair, oldPair).x,
                        -coords.get(1).apply(cPair, oldPair).y);
        BiFunction<Pair<Coord>, Pair<Coord>, Coord> diffFunction =
                (oldPair, cPair) -> minuend.apply(cPair, oldPair).sum(subtrahend.apply(cPair, oldPair));
        return diffFunction;
    }
}
