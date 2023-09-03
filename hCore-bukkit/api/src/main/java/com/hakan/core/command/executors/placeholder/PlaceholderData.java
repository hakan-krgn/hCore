package com.hakan.core.command.executors.placeholder;

import com.hakan.core.command.executors.basecommand.BaseCommandData;
import com.hakan.core.utils.ReflectionUtils;
import com.hakan.core.utils.Validate;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.List;

/**
 * PlaceholderData class to
 * manage placeholder methods.
 */
@SuppressWarnings({"unchecked"})
public final class PlaceholderData {

    private final BaseCommandData baseCommandData;
    private final Method method;
    private final String name;
    private final String permission;

    /**
     * Constructor to create SubCommandData object.
     *
     * @param method      Method of annotation.
     * @param placeholder Annotation.
     */
    public PlaceholderData(@Nonnull BaseCommandData baseCommandData,
                           @Nonnull Method method,
                           @Nonnull Placeholder placeholder) {
        Validate.notNull(baseCommandData, "baseCommandData cannot be null!");
        Validate.notNull(method, "method cannot be null!");
        Validate.notNull(placeholder, "placeholder cannot be null!");

        this.baseCommandData = baseCommandData;
        this.method = method;
        this.name = placeholder.name();
        this.permission = placeholder.permission();
    }

    /**
     * Gets base command data class.
     *
     * @return Base command data class.
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
     * Gets name from annotation.
     *
     * @return Name from annotation.
     */
    @Nonnull
    public String getName() {
        return this.name;
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
     * Gets all values of placeholder
     * as running the method.
     *
     * @return Values.
     */
    public List<String> getValues() {
        return ReflectionUtils.invoke(this.baseCommandData.getAdapter(), this.method);
    }
}
