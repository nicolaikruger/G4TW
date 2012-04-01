package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.model.tree.Tree2D;

/**
 * A part of a larger map containing only the specified entity-types.
 */
public class MapPart extends Tree2D {

    public final int type;

    /**
     * Creates a new part of a map.
     * @param type  The entity-type this map-part contains.
     */
    public MapPart(int type) {
        this.type = type;
    }

}
