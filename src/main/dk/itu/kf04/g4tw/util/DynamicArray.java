package dk.itu.kf04.g4tw.util;

import java.util.Iterator;

/**
 * An array that can be resized depending on the input.
 * @param <Item>  The type of the elements to store.
 */
public class DynamicArray<Item> implements Iterable<Item> {

	@SuppressWarnings("unchecked") protected Item[] a = (Item[]) new Object[1];
	protected int N = 0;
	
    /**
     * Adds an element to the array.
     * @param item  The item to add.
     */
	public void add(Item item) {
		if(N == a.length)
			resize(a.length << 1);
		
		a[N++] = item;
	}

    /**
     * Concatenate this array with another DynamicArray.
     * @param that  The other array to concatenate.
     */
    public void add(DynamicArray<Item> that) {
        for (int i = 0; i < that.length(); i++) {
            add(that.get(i));
        }
	}
	
    /**
     * Retrieves the element from the given index.
     * @param index  The index of the element to remove.
     * @return  The element at the given index or null if no element could be found               
     */
	public Item get(int index) {
		return a[index];
	}      
	
    /**
     * Is the array empty?!
	 * @return boolean 
     */
	public boolean isEmpty()
	{
		return N == 0;
	}

    /**
     * Provides an iterator over the elements in the DynamicArray.
     * @return  An iterator capable of traversing the elements of the array.
     */
    public Iterator<Item> iterator() {
        final DynamicArray<Item> object = this;
        return new Iterator<Item>() {
            int i = 0;
            public boolean hasNext() {
                return i <= N;
            }

            public Item next() {
                return a[i++];
            }

            public void remove() {
                object.remove(i);
            }
        };
    }    
	
    /**
     * Fetches the number of elements in the array
     * @return The number of elements.
     */
	public int length() {
		return N;
	}   
	
    /**
     * Removes an element from its index.
     * @param index  The index of the element to remove.
     */
	public void remove(int index)
	{
		if(!isEmpty()) {
            // Moves all elements after the index one spot to the left;
            // TODO: Use System.arraycopy
            for(int i = index+1; i < N-1; i++) {
                a[i] = a[i+1];
            }
            a[--N] = null;

            // Resize to half if size decreases to a fourth
            if(N > 0 && N == (a.length >> 2))
                resize(a.length/2);
        }
    }
	
	
    /**
     * Resizes the array (hence the "dynamic" in DynamicArray).
     * @param length  The new length of the array
     */
	private void resize(int length) {
        @SuppressWarnings("unchecked") Item[] temp = (Item[]) new Object[length];

        System.arraycopy(a, 0, temp, 0, N);

		a = temp;
	}
}
