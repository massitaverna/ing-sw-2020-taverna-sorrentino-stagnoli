package it.polimi.ingsw.model.handler;

interface RuleHandler {
    void handleValidationRequest(ValidationContainer vc);

    void handle() throws Exception;
}
