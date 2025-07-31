package com.scrapperapp;

public class JsonUtility {

    public static int findMatchingDelimiter(String json, int start, char[] delimiter) {
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            if (json.charAt(i) == delimiter[0]) {
                depth++;
            } else if (json.charAt(i) == delimiter[1]) {
                depth--;
            }
            if (depth == 0) {
                return i;
            }
        }
        return -1;
    }

    public static String extractValue(String source, String key) {
        int idx = source.indexOf(key);
        if (idx == -1) {
            return "";
        }
        int colon = source.indexOf(":", idx);
        int comma = source.indexOf(",", colon);
        int end = (comma == -1) ? source.indexOf("}", colon) : comma;
        return source.substring(colon + 1, end).trim();
    }

    public static String extractObject(String source, String key, char[] delimiter) {
        int idx = source.indexOf(key + ":" + delimiter[0]);
        if (idx == -1) {
            return "";
        }
        int braceStart = source.indexOf(delimiter[0], idx);
        int braceEnd = findMatchingDelimiter(source, braceStart, delimiter);
        if (braceStart == -1 || braceEnd == -1) {
            return "";
        }
        return source.substring(braceStart, braceEnd + 1);
    }

    public static String removeQuotes(String source) {
        if (source.length() >= 2 && source.startsWith("\"") && source.endsWith("\"")) {
            source = source.substring(1, source.length() - 1);
        }
        return source;
    }
}
