package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.exceptions.model.handler.RuleParserException;
import org.junit.Test;

public class LambdaParserTest {

    @Test
    public void test1() throws RuleParserException {
        LambdaParser.extractPredicate("near(sum(after, coord(-1,0)), sum(before, coord(1,1)))");
    }
}
