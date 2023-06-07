package me.emiel.lockdup.helper;

import java.util.regex.Pattern;

public class StringValidator {
    private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_.]+$");

    public static boolean isValidFileName(String fileName) {
        return FILE_NAME_PATTERN.matcher(fileName).matches();
    }
}
