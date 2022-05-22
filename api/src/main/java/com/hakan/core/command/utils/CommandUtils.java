package com.hakan.core.command.utils;

public class CommandUtils {

    //admin set
    //admin set <arg> <arg>

    public static boolean isMatching(String[] args1, String[] args2) {
        if (args1.length < args2.length)
            return false;

        for (int i = 0; i < args2.length; i++)
            if (!args1[i].equalsIgnoreCase(args2[i]) && !args2[i].equalsIgnoreCase("<arg>"))
                return false;
        return true;
    }
}