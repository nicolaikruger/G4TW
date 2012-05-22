package dk.itu.kf04.g4tw.model.tree;

import java.awt.geom.Point2D;

/**
 * The smallest rectangle that encapsulated two given points.
 */
public class RoadRectangle {

    /**
     * The least x-coordinate.
     */
	public final double xMin;

    /**
     * The least y-coordinate.
     */
	public final double yMin;

    /**
     * The biggest x-coordinate.
     */
	public final double xMax;

    /**
     * The biggest y-coordinate.
     */
	public final double yMax;

    /**
     * Creates a RoadRectangle that spans the four coordinates, interpreted as pairs of two points in a
     * 2-dimensional space.
     * <br />
     * The constructor tests for the least value between x1 and x2 and y1 and y2 and stores them in the
     * appropriate variables.
     * @param x1  The first x-coordinate.
     * @param y1  The first y-coordinate.
     * @param x2  The second x-coordinate.
     * @param y2  The second y-coordinate.
     * @throws IllegalArgumentException If the given coordinates are the same.
     */
	public RoadRectangle(double x1, double y1, double x2, double y2) throws IllegalArgumentException {
        // Make sure the rectangle isn't empty
        if (Double.compare(x1, x2) == 0) throw new IllegalArgumentException("The two given x-coordinates are the same.");
        if (Double.compare(y1, y2) == 0) throw new IllegalArgumentException("The two given y-coordinates are the same.");

        this.xMin = Math.min(x1, x2);
        this.yMin = Math.min(y1, y2);
        this.xMax = Math.max(x1, x2);
        this.yMax = Math.max(y1, y2);
    }

    /**
     * Builds a RoadRectangle from two points testing for the least values and storing them in the 
     * appropriate places.
     * @param p1  The first point of the rectangle.
     * @param p2  The second point of the rectangle.
     * @throws IllegalArgumentException If the given coordinates are the same.
     */
    public RoadRectangle(Point2D.Double p1, Point2D.Double p2)throws IllegalArgumentException {
        // Make sure the rectangle isn't empty
        if (Double.compare(p1.getX(), p2.getX()) == 0) throw new IllegalArgumentException("The two given x-coordinates are the same.");
        if (Double.compare(p1.getY(), p2.getY()) == 0) throw new IllegalArgumentException("The two given y-coordinates are the same.");

        this.xMin = Math.min(p1.getX(), p2.getX());
        this.yMin = Math.min(p1.getY(), p2.getY());
        this.xMax = Math.max(p1.getX(), p2.getX());
        this.yMax = Math.max(p1.getY(), p2.getY());
    }

    /**
     * Compares this RoadRectangle to another by testing the dimension given by the <code>dimension</code> variable.
     * The comparisons follow the convention of the <code>compare</code> method in {@link Double}.
     * @param that  The other rectangle to test.
     * @param dimension  The dimension on which to compare.
     * @return  An int < 0 if the dimension of the other rectangle is larger, 0 if equality and > 0 if the dimension of other rectangle is smaller.
     */
    public int compareTo(RoadRectangle that, byte dimension) {
        double d1 = getDimensionValue(dimension).doubleValue();
        double d2 = that.getDimensionValue(dimension).doubleValue();
        return Double.compare(d1, d2);
    }

    /**
     * The equality method tests for equality between the coordinate-values.
     * @param obj  The other object to compare.
     * @return  True if the object is a RoadRectangle and its points are the same, false otherwise.
     */
    @Override public boolean equals(Object obj) {
        if (obj instanceof RoadRectangle) {
            RoadRectangle that = (RoadRectangle) obj;
            return (Double.compare(that.xMin, this.xMin) == 0 && 
                    Double.compare(that.yMin, this.yMin) == 0 &&
                    Double.compare(that.xMax, this.xMax) == 0 &&
                    Double.compare(that.yMax, this.yMax) == 0);
        } else {
            return false;
        }
    }

    /**
     * Returns the value associated with the dimension.
     * @param dimension  The dimension whose value we are interested in.
     * @return  The value of the given dimension in this rectangle
     * @throws IllegalArgumentException If the dimension input is less than one or larger than 4.
     */
    public Number getDimensionValue(byte dimension) throws IllegalArgumentException {
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
	public boolean intersects(RoadRectangle that) {
		return (this.xMin <= that.xMax && this.xMax >= that.xMin && this.yMin <= that.yMax && this.yMax >= that.yMin);
	}
    
    @Override public String toString() {
        return "RoadRectangle: (" + xMin + ", " + yMin + "), (" + xMax + ", " + yMax + ")";
    }
}
