package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.model.tree.Tree2D;

import java.io.Serializable;

/**
 * A part of a larger map containing only the specified entity-types.
 */
public class RoadTypeTree extends Tree2D implements Serializable {

    public final int type;

    /**
     * Creates a new part of a map.
     * @param type  The entity-type this map-part contains.
     */
    public RoadTypeTree(int type) {
        this.type = type;
    }

}
