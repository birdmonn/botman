package com.sapo.botman.utils;

public class SplitString {
    private static SplitString instance;

    public static SplitString getInstance() {
        if (instance == null) {
            instance = new SplitString();
        }
        return instance;
    }

    public String stringPathReport(String path) {
        String[] partSplit = path.split("/");
        return "/" + partSplit[2] + "/" + partSplit[3];
    }
}
