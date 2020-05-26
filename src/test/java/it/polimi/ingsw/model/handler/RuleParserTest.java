package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.exceptions.model.handler.RuleParserException;
import it.polimi.ingsw.model.handler.RuleParser;
import org.junit.Test;

public class RuleParserTest {

    @Test
    public void test1() throws RuleParserException {
        RuleParser parser = new RuleParser(null);
        parser.extractPredicate("near(sum(after, coord(-1,0)), sum(before, coord(1,1)))");
    }
}
