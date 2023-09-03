package com.hakan.core.command.executors.subcommand;

import com.hakan.core.command.executors.basecommand.BaseCommandData;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * SubCommandData class to get datas of
 * sub commands from annotated methods.
 */
public final class SubCommandData implements Comparable<SubCommandData> {

    private final BaseCommandData baseCommandData;
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
    public SubCommandData(@Nonnull BaseCommandData baseCommandData,
                          @Nonnull Method method,
                          @Nonnull SubCommand subCommand) {
        Validate.notNull(baseCommandData, "baseCommandData cannot be null!");
        Validate.notNull(method, "method cannot be null!");
        Validate.notNull(subCommand, "subCommand cannot be null!");

        this.baseCommandData = baseCommandData;
        this.method = method;
        this.args = subCommand.args();
        this.permission = subCommand.permission();
        this.permissionMessage = subCommand.permissionMessage();
    }

    /**
     * Gets base com.hakan.core.command data class.
     *
     * @return Base com.hakan.core.command data class.
     */
    public BaseCommandData getBaseCommandData() {
        return this.baseCommandData;
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
     * Gets permission com.hakan.core.message from annotation.
     *
     * @return Permission com.hakan.core.message from annotation.
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
