package it.polimi.ingsw.model.handler;

public enum Decision {
    GRANT, DENY;

    public Decision getOpposite() {
        if (this == GRANT) {
            return DENY;
        } else {
            return GRANT;
        }
    }
}
