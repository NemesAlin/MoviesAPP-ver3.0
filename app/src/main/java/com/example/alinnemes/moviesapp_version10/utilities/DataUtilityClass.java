package com.example.alinnemes.moviesapp_version10.utilities;

/**
 * Created by alin.nemes on 09-Aug-16.
 */
public class DataUtilityClass {

    public static boolean isNumeric(String s) {
        return java.util.regex.Pattern.matches("\\d+", s);
    }
}
