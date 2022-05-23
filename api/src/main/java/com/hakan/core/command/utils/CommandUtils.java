package com.hakan.core.command.utils;

public class CommandUtils {

    public static boolean isMatching(String[] args1, String[] args2) {
        if (args2.length < args1.length)
            return false;

        for (int i = 0; i < args1.length; i++)
            if (!args1[i].equalsIgnoreCase(args2[i]))
                return false;
        return true;
    }
}