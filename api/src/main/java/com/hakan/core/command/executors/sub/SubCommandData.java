package com.hakan.core.command.executors.sub;

import java.lang.reflect.Method;

public class SubCommandData {

    private final Method method;
    private final String[] args;
    private final String permission;

    public SubCommandData(Method method, SubCommand subCommand) {
        this.method = method;
        this.args = subCommand.args();
        this.permission = subCommand.permission();
    }

    public Method getMethod() {
        return this.method;
    }

    public String[] getArgs() {
        return this.args;
    }

    public String getPermission() {
        return this.permission;
    }
}