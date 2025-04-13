package io.renatofreire.servletspoc.utils;

import java.util.Optional;

public class PathUtils {

    public static Optional<Integer> extractId(String pathInfo) {
        try {
            if (pathInfo == null || pathInfo.length() <= 1) return Optional.empty();
            return Optional.of(Integer.parseInt(pathInfo.substring(1)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}