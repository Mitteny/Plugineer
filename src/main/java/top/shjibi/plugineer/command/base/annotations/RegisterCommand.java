package top.shjibi.plugineer.command.base.annotations;

import top.shjibi.plugineer.command.base.CommandHandler;

import java.lang.annotation.*;

/**
 * Use this annotation on a {@link CommandHandler} implementation to register a command without writing it in plugin.yml
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(RegisterCommands.class)
public @interface RegisterCommand {

    /**
     * Name of this command
     */
    String name();

    /**
     * Description of this command
     */
    String description() default "";

    /**
     * Usage message of this command
     */
    String usageMessage() default "";

    /**
     * Aliases of this command
     */
    String[] aliases() default "";

    /**
     * If a command with the same name is already registered, whether to replace the old one with this
     */
    boolean force() default false;
}
