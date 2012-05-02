package dk.itu.kf04.g4tw.model;

import dk.itu.kf04.g4tw.util.DynamicArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdressParser {

    /**
     * Named roads, all road segments with the same name is grouped
     */
    private static HashMap<String, DynamicArray<Road>> namedRoads;

    static String sal = "(s|S)(a|A)(l|L)";
    static String bs = "[a-zæøåüöéA-ZÆØÅÜÖÉ]";

    static String[] patterns = new String[] {
            /* Street Name */				"((?<=\\s|^)(?!"+sal+")"+bs+"{3,}\\s?)+",
            /* House Number */				"(?<=\\W|"+bs+"|^)\\d{1,3}(?!\\d|\\.)",
            /* House Letter */				"(?<=\\W\\d{1,3}\\s?)\\s?"+bs+"{1}(\\b?|$)",
            /* Floor */						"(?<=(\\d|"+bs+"|\\W)\\s?)(\\d{1,2}\\s?("+sal+"|\\.\\s?"+sal+")(\\s[fFhHmMtTvV\\.]{2,3})?)",
            /* Postal Code */				"\\d{4}\\s?",
            /* City */						"(?<=\\s|^)(?!"+sal+")("+bs+"{3,}\\s?)+\\s"+bs+"+"};

    public static Road getRoad(String adress)
    {
        /**
         * index 0 = street name
         * index 1 = street number
         * index 2 = house letter
         * index 3 = house floor
         * index 4 = postal code
         * index 5 = city
         */
        String[] adressInfo = parseAddress(adress);

        return null;
    }

    public static void setNamedRoads(HashMap<String, DynamicArray<Road>> map)
    {
        namedRoads = map;
    }

    private static DynamicArray<Road> sortByLetter(DynamicArray<Road> arr)
    {
        return null;
    }

    private static DynamicArray<Road> sortByNumber(DynamicArray<Road> arr)
    {
        return null;
    }

    private static DynamicArray<Road> sortByCity(DynamicArray<Road> arr)
    {
        return null;
    }

    private static String[] parseAddress(String s) throws IllegalArgumentException {
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
