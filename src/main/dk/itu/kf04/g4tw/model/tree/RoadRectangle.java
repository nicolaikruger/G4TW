package dk.itu.kf04.g4tw.model.tree;

import java.awt.geom.Point2D;

/**
 * A rectangle that encapsulates a road.
 */
public class RoadRectangle implements ComparableByDimension {
	
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
    
    public RoadRectangle(Point2D.Double p1, Point2D.Double p2) {
        this.xMin = Math.min(p1.getX(), p2.getX());
        this.yMin = Math.min(p1.getY(), p2.getY());
        this.xMax = Math.max(p1.getX(), p2.getX());
        this.yMax = Math.max(p1.getY(), p2.getY());
    }

    public int compareTo(ComparableByDimension that, byte dimension) {
        double d1 = getDimensionValue(dimension).doubleValue();
        double d2 = that.getDimensionValue(dimension).doubleValue();
        return Double.compare(d1, d2);
    }   
    
    @Override public boolean equals(Object obj) {
        if (obj instanceof RoadRectangle) {
            RoadRectangle that = (RoadRectangle) obj;
            return (that.xMin == this.xMin && that.yMin == this.yMin &&
                    that.xMax == this.xMax && that.yMax == this.xMax);
        } else {
            return false;
        }
    }

    public Number getDimensionValue(byte dimension) {
        // Make sure input is ok
        if (dimension < 1) {
            throw new IllegalArgumentException("Dimension cannot be less than 1.");
        } else if (dimension > 4) {
            throw new IllegalArgumentException("RoadRectangle cannot handle more than 4 dimensions.");
        }

        if      (dimension == 1) return xMin;
        else if (dimension == 2) return yMin;
        else if (dimension == 3) return xMax;
        else                     return yMax;
    }

	/**
	 * Examines whether a given rectangle intersects with this ComparableByDimension.
	 *
	 * A = this, B = that.
	 * Cond1.  If A's left edge is to the right of the B's right edge, then A is Totally to right Of B
	 * Cond2.  If A's right edge is to the left of the B's left edge,  then A is Totally to left Of B
	 * Cond3.  If A's top edge is below B's bottom  edge,              then A is Totally below B
	 * Cond4.  If A's bottom edge is above B's top edge,               then A is Totally above B
	 *
	 * Reference: http://stackoverflow.com/questions/306316/determine-if-two-rectangles-overlap-each-other/306332#306332
     *
     * @param that  The rectangle to examine.
     * @return True if there is an intersection, false otherwise.
	 */
	public boolean intersects(ComparableByDimension that) {
		double xMin = that.getDimensionValue((byte) 1).doubleValue();
		double yMin = that.getDimensionValue((byte) 2).doubleValue();
		double xMax = that.getDimensionValue((byte) 3).doubleValue();
		double yMax = that.getDimensionValue((byte) 4).doubleValue();
		return (this.xMin < xMax || this.xMax > xMin || this.yMin < yMax || this.yMax > yMin);
	}
    
    @Override public String toString() {
        return "RoadRectangle: (" + xMin + ", " + yMin + "), (" + xMax + ", " + yMax + ")";
    }
}
