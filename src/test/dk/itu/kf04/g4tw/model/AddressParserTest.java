package dk.itu.kf04.g4tw.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Black box tests for the AddressParser.
 */
public class AddressParserTest extends AddressParser {
    
    String[] arr;

    @Test(expected = IllegalArgumentException.class)
    public void empty() {
        AddressParser.getRoad("");
    }
    
    @Test
    public void typical() {
        arr = new String[] {"Rued Langgaards Vej", "7", "A", "5. sal", "2300", "København S"};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards Vej, 7A, 5. sal, 2300, København S"));
    }
    
    @Test
    public void dupePostalCode() {
        arr = new String[] {"Rued Langgaards Vej", "7", "A", "5. sal", "2300", "København S"};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards Vej, 7A, 5. sal, 2300, 2586, København S"));
    }
    
    @Test
    public void symbols() {
    	arr = new String[] {"Rued Langgårds Vej", "7", "A", "5. sal", "2300", "København S"};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgårds Vej, 7A, 5. sal, 2300, København S"));
    }
    
    @Test
    public void noSpaces() {
    	arr = new String[] {"RuedLanggaardsvej", "7", "A", "5.Sal", "2300", ""};
        assertArrayEquals(arr, AddressParser.parseAddress("RuedLanggaardsvej7A5.Sal2300KøbenhavnS"));
	}
    
    @Test
    public void dupeHouseNumber() {
    	arr = new String[] {"Rued Langgaards vej", "8", "A", "5. sal", "2300", "København S"};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej, 8A 7A, 8, 5. sal, 2300, København S"));
    }
    
    @Test
    public void mixUp() {
    	arr = new String[] {"København", "7", "", "", "2300", "Rued Langgaards vej"};
        assertArrayEquals(arr, AddressParser.parseAddress("København S, A7, sal 5, 2300, Rued Langgaards vej"));
    }
    
    @Test
    public void mixUpHouseLetter() {
    	arr = new String[] {"Rued Langgaards vej", "7", "A", "5. sal", "2300", "København S"};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej, 7A, 5. sal B, 2300, København S"));
    }
    
    @Test
    public void lackInfo1() {
    	arr = new String[] {"Rued Langgaards vej", "7", "A", "5. sal", "2300", ""};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej, 7A, 5. sal, 2300"));
    }
    
    @Test
    public void lackInfo2() {
    	arr = new String[] {"Rued Langgaards vej", "", "", "5. sal", "2300", ""};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej, 5. sal, 2300"));
    }
    
    @Test
    public void lackInfo3() {
    	arr = new String[] {"Rued Langgaards vej", "", "", "", "", "København S"};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej, København S"));
    }
    
    @Test
    public void lackInfo4() {
    	arr = new String[] {"Rued Langgaards vej", "", "", "", "2300", ""};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej, 2300"));
    }
    
    @Test
    public void lackInfo5() {
    	arr = new String[] {"Rued Langgaards vej", "", "", "", "", ""};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej"));
    }
    
    @Test
    public void lackInfo6() {
    	arr = new String[] {"Rued Langgaards vej", "7", "A", "", "", ""};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Langgaards vej, 7A"));
    }
    
    @Test
    public void overload() {
    	arr = new String[] {"Rued Den Lange Gaards Herlige Vidunderlige vej", "123", "A", "5. sal t. v.", "2300", "Køb mandes havn syd"};
        assertArrayEquals(arr, AddressParser.parseAddress("Rued Den Lange Gaards Herlige Vidunderlige vej, 123A, 5. sal t. v., 2300, Køb mandes havn syd"));
    }
    
    @Test
    public void numbersOnly() {
    	arr = new String[] {"København", "7", "", "", "2300", ""};
        assertArrayEquals(arr, AddressParser.parseAddress("7, 2300, København S"));
    }
}
