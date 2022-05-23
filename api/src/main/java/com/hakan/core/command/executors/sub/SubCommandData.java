package com.hakan.core.command.executors.sub;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

public class SubCommandData implements Comparable<SubCommandData> {

    private final Method method;
    private final String[] args;
    private final String permission;
    private final String permissionMessage;

    public SubCommandData(Method method, SubCommand subCommand) {
        this.method = method;
        this.args = subCommand.args();
        this.permission = subCommand.permission();
        this.permissionMessage = subCommand.permissionMessage();
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

    public String getPermissionMessage() {
        return this.permissionMessage;
    }

    @Override
    public int compareTo(@Nonnull SubCommandData that) {
        return Integer.compare(that.args.length, this.args.length);
    }
}