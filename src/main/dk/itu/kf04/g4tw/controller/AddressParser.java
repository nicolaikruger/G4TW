package dk.itu.kf04.g4tw.controller;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class AddressParser {

    /**
     * The regex pattern for a floor.
     */
    private static String floor = "(s|S)(a|A)(l|L)";

    /**
     * The regex pattern for letters in the alphabet (including strange danish letters).
     */
    private static String alphabet = "[a-zæøåüöéA-ZÆØÅÜÖÉ]";

    /**
     * The patterns used to match an address.
     */
    private static String[] patterns = new String[] {
                /* Street Name */				"((?<=\\s|^)(?!"+ floor +")"+ alphabet +"{3,}\\s?)+",
                /* House Number */				"(?<=\\W|"+ alphabet +"|^)\\d{1,3}(?!\\d|\\.)",
                /* House Letter */				"(?<=\\W\\d{1,3}\\s?)\\s?"+ alphabet +"{1}(\\b?|$)",
                /* Floor */						"(?<=(\\d|"+ alphabet +"|\\W)\\s?)(\\d{1,2}\\s?("+ floor +"|\\.\\s?"+ floor +")(\\s[fFhHmMtTvV\\.]{2,3})?)",
                /* Postal Code */				"\\d{4}\\s?",
                /* City */						"(?<=\\s|^)(?!"+ floor +")("+ alphabet +"{3,}\\s?)+\\s"+ alphabet +"+"};
    
    /**
     * Parses an address given by the input string s.
     * This function is a
     *
     * @param s  The address to parse.
     */
    public static String[] parseAddress(String s) throws IllegalArgumentException {
        String[] address = new String[6];
        Arrays.fill(address, "");

        s = s.replaceAll(",", " ");
        s = s.replaceAll("#", " ");

        for(int i = 0; i < patterns.length; i++)
        {
            Pattern p = Pattern.compile(patterns[i]);
            Matcher m = p.matcher(s);
            if(m.find())
            {
                address[i] = m.group().trim();
                if(i == 0) s = s.replaceAll(m.group(), " ");
            }
        }

        if(address[0].equals("")) {
            throw new IllegalArgumentException();
        }

        return address;
    }
}
