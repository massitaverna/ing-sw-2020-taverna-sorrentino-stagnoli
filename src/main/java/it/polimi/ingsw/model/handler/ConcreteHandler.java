/*
Rep:

rules != null
rules.size() > 0
 */

package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.exceptions.UndeterminedSpaceException;
import it.polimi.ingsw.model.Space;

import java.util.List;


class ConcreteHandler extends BaseHandler {

    private List<Rule> rules;

    public ConcreteHandler(List<Rule> rules) {
        this.rules = rules;
    }
    public ConcreteHandler(List<Rule> rules, RuleHandler next) {
        super(next);
        this.rules = rules;
    }

    //---------------------------------------------------------------------------------------


    @Override
    public void handleValidationRequest(ValidationContainer vc) throws UndeterminedSpaceException {

        //Do your work

        Space current = vc.getCurrentSpace();

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


        //Pass responsibility
        if (!vc.allSpacesValidated()) {
            super.handleValidationRequest(vc);
        }

    }
    public void handle() throws Exception {
        // Do standard checks

        // If (cannot resolve decidability) {
            super.handle();
        // }
    }
}
