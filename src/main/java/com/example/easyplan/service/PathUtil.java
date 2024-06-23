package com.example.easyplan.service;

public class PathUtil {
    public static String normalizePath(String path) {
        return path.replace("\\", "/");
    }
}
