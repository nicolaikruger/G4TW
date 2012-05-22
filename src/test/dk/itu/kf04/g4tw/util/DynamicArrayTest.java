package dk.itu.kf04.g4tw.util;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Iterator;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Tests the DynamicArray.        
 */
public class DynamicArrayTest {

    DynamicArray<Integer> arr;
    
    @Before public void setUp() throws Exception {
        arr = new DynamicArray<Integer>();
    }
    
    @Test public void singleAdditionTest() {
        assertEquals(0, arr.length());
        arr.add(1);
        assertEquals(1, arr.length());
        arr.add(12);
        assertEquals(2, arr.length());
        arr.add(13);
        assertEquals(3, arr.length());
        arr.add(1);
        assertEquals(4, arr.length());
        arr.add(-0);
        assertEquals(5, arr.length());
        
        assertTrue(1 == arr.get(0));
        assertTrue(12 == arr.get(1));
        assertTrue(13 == arr.get(2));
        assertTrue(1 == arr.get(3));
        assertTrue(0 == arr.get(4));
    }
    
    @Test public void arrayAdditionTest() {
        DynamicArray<Integer> that = new DynamicArray<Integer>();
        that.add(17); that.add(23); that.add(-640);
        
        arr.add(9);
        arr.add(that);
        assertEquals(4, arr.length());
        assertTrue(9 == arr.get(0));
        assertTrue(17 == arr.get(1));
        assertTrue(23 == arr.get(2));
        assertTrue(-640 == arr.get(3));
    }
    
    @Test public void emptyTest() {
        assertTrue(arr.isEmpty());
        
        arr.add(132);
        assertFalse(arr.isEmpty());
        
        arr.remove(0);
        assertTrue(arr.isEmpty());
    }
    
    @Test public void iteratorTest() {
        arr.add(12);
        arr.add(27);
        arr.add(-230);
        
        Iterator<Integer> i = arr.iterator();
        assertTrue(i.hasNext());
        assertTrue(12 == i.next());
        assertTrue(i.hasNext());
        assertTrue(27 == i.next());
        assertTrue(i.hasNext());
        assertTrue(-230 == i.next());
        assertFalse(i.hasNext());
    }
    
    @Test public void removeTest() {
        // Test on empty
        assertEquals(0, arr.length());
        arr.remove(12);
        assertEquals(0, arr.length());
        
        // Test on a single element
        arr.add(801923);
        arr.remove(0);
        
        // Test on a number of elements
        arr.add(1);
        arr.add(1231);
        arr.add(-3912);
        arr.add(9723);
        
        arr.remove(3);
        assertTrue(1 == arr.get(0));
        assertTrue(1231 == arr.get(1));
        assertTrue(-3912 == arr.get(2));
        
        arr.remove(1);
        assertTrue(1 == arr.get(0));
        assertTrue(-3912 == arr.get(1));
        
        arr.remove(0);
        assertTrue(-3912 == arr.get(0));
        
        arr.remove(0);
        assertTrue(0 == arr.length());
    }
    
    @Test (expected = ArrayIndexOutOfBoundsException.class) 
    public void removeFailTest() {              
        // Test on non-existing index
        arr.add(801923);
        arr.remove(30);
    }

}
