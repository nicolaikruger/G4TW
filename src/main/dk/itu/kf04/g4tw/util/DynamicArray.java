package dk.itu.kf04.g4tw.util;

import java.util.Random;

/**
 * An array that can be resized depending on the input.
 * @param <Item>  The type of the elements to store.
 */
public class DynamicArray<Item> {

	private Item[] a;
	private int N = 0;

    /**
     * Creates a new DynamicArray of size 1.
     */
    public DynamicArray() { a = (Item[]) new Object[1]; }

    /**
     * Creates a new DynamicArray with a given size.
     * @param size The size of the elements to store.
     */
    public DynamicArray(int size) { a = (Item[]) new Object[size]; }
	
    /**
     * Adds an element to the array.
     * @param item  The item to add.
     */
	public void add(Item item)
	{
		if(N == a.length)
			resize(a.length << 1);
		
		a[N++] = item;
	}

    /**
     * Adds another DynamicArray to the array.
     */
    public void add(DynamicArray<Item> that) {
        for (int i = 0; i < that.length(); i++) {
            add(that.get(i));
        }
    }
	
    /**
     * Removes an element from its index.
     * @param index  The index of the element to remove.
     */
	public void remove(int index)
	{
		if(isEmpty())
			throw new RuntimeException("The array is empty! FFS... -.-''");
		
		//a[index] = a[N];

        // Moves all elements after the index one spot to the left;
        for(int i = index+1; i < N-1; i++)
        {
            a[i] = a[i+1];
        }
        a[--N] = null;
		
		if(N > 0 && N == a.length/4)
			resize(a.length/2);
	}
	
	/**
	 * Replaces an item with a new one.
	 * @param oldItem  The item to replace
	 * @param newItem  The item to insert
     * @return a boolean value to indicate success or failure.
	 */
	public boolean replace(Item oldItem, Item newItem) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(oldItem)) { // equals because the Integer type is a moron and can not understand ==
                a[i] = newItem;
                return true;
            }
        }
        return false;
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
     * Take a wild guess.
     */
	public int length()
	{
		return N;
	}
	
    /**
     * Retrieves the element from the given index.
     * @param index  The index of the element to remove.
     */
	public Item get(int index)
	{
		return a[index];
	}
	
    /**
     * Resizes the array (hence the "dynamic" in DynamicArray).
     */
	private void resize(int length)
	{
		Item[] temp = (Item[]) new Object[length];
		
		for(int i = 0; i < N; i++)
			temp[i] = a[i];
		
		a = temp;
	}

	/**
	 * Shuffle the array.
	 */
	public void shuffle() {
		Random r = new Random();
		for (int i = 0; i < N; i++) swap(i, r.nextInt(N));
	}

	/**
	 * Swaps two elements in the array.
	 */
	private void swap(int x, int y) {
		Item tmp = a[x];
		a[x] = a[y];
		a[y] = tmp;
	}
}
