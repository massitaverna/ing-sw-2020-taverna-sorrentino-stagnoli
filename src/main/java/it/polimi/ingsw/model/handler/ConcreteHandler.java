/*
Rep:

rules != null
rules.size() > 0
 */

package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.exceptions.UndeterminedSpaceException;
import it.polimi.ingsw.model.Coord;

import java.util.ArrayList;
import java.util.List;


class ConcreteHandler implements RuleHandler {

    private List<Rule> permanentRules;
    private List<Rule> temporaryRules;

    public ConcreteHandler(List<Rule> permanentRules) {
        this.permanentRules = permanentRules;
    }


    //---------------------------------------------------------------------------------------


    @Override
    public void handleValidationRequest(ValidationContainer vc) throws UndeterminedSpaceException {

        //Do your work

        Coord current = vc.getCurrentPosition();

        /*
        //FOR-LOOP WAY
        List<Rule> validationRules = rules.stream()
                .filter(r -> r.getPurpose() == Purpose.VALIDATION)
                .collect(Collectors.toList());

        for (Rule r : validationRules) {
            BiPredicate<Space, Space> condition = r.getCondition();
            vc.getAllSpaces().stream()
                    .filter(s -> condition.test(current, s))
                    .forEach(s -> vc.validateSpace(
                            s,
                            r.getActionType(),
                            r.getDecision(),
                            r.getForceSpaceFunction().apply(current, s),
                            r.getBuildLevel())
                    );

        }
         */

        // STREAM WAY
        List<Rule> rules = new ArrayList<>(temporaryRules);
        rules.addAll(permanentRules);
        rules.stream()
                .filter(r -> r.getPurpose() == Purpose.VALIDATION)
                .forEach(r ->
                        vc.getAllSpaces().stream()
                        .filter(s -> r.getCondition().test(current, s))
                        .forEach(s -> vc.validateSpace(
                                s,
                                r.getActionType(),
                                r.getDecision(),
                                r.getForceSpaceFunction().apply(current, s),
                                r.getBuildLevel())
                        )
                );

        // GENERATE

        temporaryRules = null;


        //Check if validation is completed
        if (!vc.allSpacesValidated()) {
            String exceptionalMessage = "Cannot determine validity of the followings:\n";
            exceptionalMessage += vc.getNotValidatedSpaces().toString();

            throw new UndeterminedSpaceException(exceptionalMessage);
        }

    }
}
