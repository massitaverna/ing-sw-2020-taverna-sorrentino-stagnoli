package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.God;
import it.polimi.ingsw.model.Level;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RuleTest {

    private static final Rule r = new Rule();

    @BeforeClass
    public static void setAllAttributes() {
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.GRANT);
        r.setActionType(ActionType.MOVE);
        r.setCondition((a, b) -> true);
        r.setBuildLevel(Level.GROUND);
        r.setTarget(Target.MYSELF);
        r.setGeneratedRules(new ArrayList<>());
        r.setForceSpaceFunction((a, b) -> a);
        //r.setSymbolicCondition((a, b, c) -> true);

    }
    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangePurpose() {
        r.setPurpose(Purpose.VALIDATION);
    }

    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangeDecision() {
        r.setDecision(Decision.GRANT);
    }

    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangeActionType() {
        r.setActionType(ActionType.MOVE);
    }

    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangeBuildLevel() {
        r.setBuildLevel(Level.GROUND);
    }

    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangeTarget() {
        r.setTarget(Target.MYSELF);
    }

    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangeGeneratedRules() {
        r.setGeneratedRules(new ArrayList<>());
    }

    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangeForceSpaceFunction() {
        r.setForceSpaceFunction((a, b) -> a);
    }

    @Test ( expected = java.lang.AssertionError.class )
    public void cannotChangeSymbolicCondition() {
        Rule r = new Rule();
        r.setPurpose(Purpose.VALIDATION);
        r.setDecision(Decision.GRANT);
        r.setActionType(ActionType.MOVE);
        r.setBuildLevel(Level.GROUND);
        r.setTarget(Target.MYSELF);
        r.setGeneratedRules(new ArrayList<>());
        r.setForceSpaceFunction((a, b) -> a);
        r.setSymbolicCondition((a, b, c) -> true);
        //---

        r.setSymbolicCondition((a, b, c) -> true);
    }

}
