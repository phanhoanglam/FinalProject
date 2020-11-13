package com.spring.Final.core.helpers;

import java.util.Map;
import java.util.regex.Pattern;

public class PermissionHelper {
    public static boolean compareAuthorizedUrls(Map<String, String[]> validPaths, String validSubject, String path, String method, String subject) {
        for (String validPath : validPaths.keySet()) {
            String[] validMethods = validPaths.get(validPath);

            if (Pattern.matches(validPath, path) && !subject.equals(validSubject)) {
                for (String validMethod : validMethods) {
                    if (validMethod.equals(method)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean compareUrls(Map<String, String[]> validPaths, String path, String method) {
        for (String validPath : validPaths.keySet()) {
            String[] validMethods = validPaths.get(validPath);

            if ((validPath.contains("[") && Pattern.matches(validPath, path)) || validPath.equals(validPath)) {
                for (String validMethod : validMethods) {
                    if (validMethod.equals(method)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
