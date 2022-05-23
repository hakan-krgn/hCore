package com.hakan.core.command.executors.sub;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * SubCommandData class to get datas of
 * sub commands from annotated methods.
 */
public final class SubCommandData implements Comparable<SubCommandData> {

    private final Method method;
    private final String[] args;
    private final String permission;
    private final String permissionMessage;

    /**
     * Constructor to create SubCommandData object.
     *
     * @param method     Method of annotation.
     * @param subCommand Annotation.
     */
    public SubCommandData(@Nonnull Method method, @Nonnull SubCommand subCommand) {
        Objects.requireNonNull(method, "method cannot be null!");
        Objects.requireNonNull(subCommand, "subCommand cannot be null!");

        this.method = method;
        this.args = subCommand.args();
        this.permission = subCommand.permission();
        this.permissionMessage = subCommand.permissionMessage();
    }

    /**
     * Gets method from annotation.
     *
     * @return Method from annotation.
     */
    @Nonnull
    public Method getMethod() {
        return this.method;
    }

    /**
     * Gets args from annotation.
     *
     * @return Args from annotation.
     */
    @Nonnull
    public String[] getArgs() {
        return this.args;
    }

    /**
     * Gets permission from annotation.
     *
     * @return Permission from annotation.
     */
    @Nonnull
    public String getPermission() {
        return this.permission;
    }

    /**
     * Gets permission message from annotation.
     *
     * @return Permission message from annotation.
     */
    @Nonnull
    public String getPermissionMessage() {
        return this.permissionMessage;
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param that Object to be compared.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(@Nonnull SubCommandData that) {
        return Integer.compare(that.args.length, this.args.length);
    }
}