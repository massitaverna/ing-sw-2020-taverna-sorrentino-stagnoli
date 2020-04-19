/*
Rep:
\forall space in allSpaces (moveGrant.contains(space) <==> !moveDeny.contains(space))
\forall space in allSpaces (buildGrant.contains(space) <==> !buildDeny.contains(space))

forceList.contains(space) ==> moveGrantList.contains(space)

*/


/*
SuperList structure:
0. moveGrant list
1. moveDeny list
2. buildGrant_GROUND list
3. buildDeny_GROUND list
4. buildGrant_LVL1 list
5. buildDeny_LVL1 list
6. buildGrant_LVL2 list
7. buildDeny_LVL2 list
8. buildGrant_LVL3 list
9. buildDeny_LVL3 list
10. buildGrant_DOME list
11. buildDeny_DOME list

Every list is accessed with index = 2*actionType + decision (+ 2*level if level != null)
 */
package it.polimi.ingsw.model.handler;

import it.polimi.ingsw.model.Level;
import it.polimi.ingsw.model.Space;

import java.util.*;
import java.util.stream.Collectors;

// Inner class used to gather necessary collections for a validation request
class ValidationContainer {
    private Space currentSpace;
    private List<Space> allSpaces;
    private List<Space> moveGrant;
    private List<Space> moveDeny;
    private Map<Level, List<Space>> buildGrant;
    private Map<Level, List<Space>> buildDeny;
    private List<List<Space>> superList;
    private Map<Space, Space> forces;

    public ValidationContainer(Space currentSpace, List<Space> allSpaces) {
        this.allSpaces = allSpaces; // COPIA!!!
        this.currentSpace = currentSpace; // COPIA
        this.superList = new ArrayList<>();
        this.forces = new HashMap<>();
        for (int i=0; i<12; i++) {
            this.superList.add(new ArrayList<>());
        }
        /*
        moveGrant = new ArrayList<>();
        moveDeny = new ArrayList<>();
        buildGrant = new HashMap<>();
        buildDeny = new HashMap<>();
        buildGrant.put(Level.GROUND, new ArrayList<>());
        buildGrant.put(Level.LVL1, new ArrayList<>());
        buildGrant.put(Level.LVL2, new ArrayList<>());
        buildGrant.put(Level.LVL3, new ArrayList<>());
        buildGrant.put(Level.DOME, new ArrayList<>());
        buildDeny.put(Level.GROUND, new ArrayList<>());
        buildDeny.put(Level.LVL1, new ArrayList<>());
        buildDeny.put(Level.LVL2, new ArrayList<>());
        buildDeny.put(Level.LVL3, new ArrayList<>());
        buildDeny.put(Level.DOME, new ArrayList<>());
        */

}

    /*
    public void setCurrentSpace(Space space) {
        this.currentSpace = space;
    }

    public void setValidMovableSpaces(Map<Space, Rule> validMovableSpaces) {
        this.validMovableSpaces = validMovableSpaces;
    }

    public void setForbiddenMovableSpaces(Map<Space, Rule> forbiddenMovableSpaces) {
        this.forbiddenMovableSpaces = forbiddenMovableSpaces;
    }

    public void setValidBuildableSpaces(Map<Map<Space, List<Level>>, Rule> validBuildableSpaces) {
        this.validBuildableSpaces = validBuildableSpaces;
    }

    public void setForbiddenBuildableSpaces(Map<Map<Space, List<Level>>, Rule>
                                            forbiddenBuildableSpaces) {
        this.forbiddenBuildableSpaces = forbiddenBuildableSpaces;
    }

    public List<Space> getToBeChecked(ActionType actionType) {
        return toBeChecked.get(actionType); // Copia!!!
    }

    */

    public Space getCurrentSpace() {
        return currentSpace;
    }

    public List<Space> getAllSpaces() {
        return allSpaces;
    }

    public void validateSpace(
            Space s, ActionType at, Decision d, Space forceDest, Level buildLevel) {

        int index = getSuperListIndex(at, d, buildLevel);

        Decision oppositeDecision = d.getOpposite();
        int oppositeIndex = getSuperListIndex(at, oppositeDecision, buildLevel);

        if (superList.get(oppositeIndex).contains(s)) {
            return;
        }

        superList.get(index).add(s);

        if (forceDest != null) {
            forces.put(s, forceDest);
        }
    }

    public List<Space> getMovableSpaces() {
        return superList.get(0); // COPIA!!!
    }
    public Map<Level, List<Space>> getBuildableSpaces() {
        Map<Level, List<Space>> result = new HashMap<>();


        Arrays.asList(Level.values())
                .forEach(level ->
                        result.put(
                                level, superList.get(2 + 2*level.ordinal())
                        )
                );

        return result;
    }

    public Map<Space, Space> getForces() {
        return forces;
    }


    public boolean allSpacesValidated() {
        for (int i = 0; i < superList.size(); i += 2) {
            final int idx = i;
            if (allSpaces.stream()
                    .filter(s -> !superList.get(idx).contains(s))
                    .filter(s -> !superList.get(idx+1).contains(s))
                    .count()
                != 0) {
                return false;
            }
        }
        return true;
    }

    public Set<Space> getNotValidatedSpaces() {
        Set<Space> result = new HashSet<>();
        for (int i = 0; i < superList.size(); i += 2) {
            final int idx = i;
            result.addAll(
                    allSpaces.stream()
                            .filter(s -> !superList.get(idx).contains(s))
                            .filter(s -> !superList.get(idx + 1).contains(s))
                            .collect(Collectors.toSet())
            );
        }

        return result;
    }

    private /*helper*/ int getSuperListIndex(ActionType at, Decision d, Level buildLevel) {
        int index = 2*at.ordinal() + d.ordinal();
        if (buildLevel != null) {
            index += 2*buildLevel.ordinal();
        }

        return index;
    }
}
