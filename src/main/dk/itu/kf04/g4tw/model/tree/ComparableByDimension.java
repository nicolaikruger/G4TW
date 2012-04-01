package dk.itu.kf04.g4tw.model.tree;

/**
 * An interface describing the ability to compare two other ComparableByDimension objects by a given dimension.
 *
 * @param T  The type of the objects to compare.
 */
public interface ComparableByDimension<T extends ComparableByDimension> {

    /**
     * Returns the value for the given dimension.
     * @param dimension  The dimension to return the value from.
     * @return  The position of the object in that dimension, that is the value as a Number.
     */
    public Number getDimensionValue(byte dimension);
    
    /**
     * Compares two ComparableByDimension objects by the given dimension.
     * By convention the method returns zero if an object is compared to itself.
     *
     * @param that  The object to compare to this
     * @param dimension  The dimension on which to compare.
     * @return A negative number if obj1 < obj2, zero if they are equal and a positive number if obj1 > obj2
     */
    public int compareTo(T that, byte dimension);

	/**
	 * Write this...
	 * @param that
	 * @return
	 */
	public boolean intersects(T that);

}
