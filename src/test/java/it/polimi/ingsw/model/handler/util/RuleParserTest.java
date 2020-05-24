package it.polimi.ingsw.model.handler.util;

import it.polimi.ingsw.exceptions.model.handler.RuleParserException;
import org.junit.Test;

public class RuleParserTest {

    @Test
    public void test1() throws RuleParserException {
        RuleParser parser = new RuleParser();
        parser.extractPredicate("near(sum(after, coord(-1,0)), sum(before, coord(1,1)))");
    }
}
