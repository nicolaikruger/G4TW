package dk.itu.kf04.g4tw.RoadKill.tree;

import java.awt.geom.Point2D;

/**
 * A rectangle that encapsulates a road.
 */
public class RoadRectangle {
	
	public final double xMin;
	public final double yMin;
	public final double xMax;
	public final double yMax;
	
	public RoadRectangle(double x1, double y1, double x2, double y2) {
		this.xMin = Math.min(x1, x2);
		this.yMin = Math.min(y1, y2);
		this.xMax = Math.max(x1, x2);
		this.yMax = Math.max(y1, y2);
	}

	/**
	 * Examines whether a given rectangle intersects with this rectangle.
	 *
	 * A = this, B = that.
	 * Cond1.  If A's left edge is to the right of the B's right edge, then A is Totally to right Of B
	 * Cond2.  If A's right edge is to the left of the B's left edge,  then A is Totally to left Of B
	 * Cond3.  If A's top edge is below B's bottom  edge,              then A is Totally below B
	 * Cond4.  If A's bottom edge is above B's top edge,               then A is Totally above B
	 *
	 * Reference: http://stackoverflow.com/questions/306316/determine-if-two-rectangles-overlap-each-other/306332#306332
	 */
	public boolean intersects(RoadRectangle that) {
		return (xMin < that.xMax || xMax > that.xMin || yMin < that.yMax || yMax > that.yMin);
	}
	
}
