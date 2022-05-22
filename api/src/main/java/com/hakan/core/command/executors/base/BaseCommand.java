package com.hakan.core.command.executors.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BaseCommand {

    String name();

    String description() default "";

    String usage() default "";

    String[] aliases() default "";
}