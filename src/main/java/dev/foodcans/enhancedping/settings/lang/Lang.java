package dev.foodcans.enhancedping.settings.lang;

import dev.foodcans.enhancedping.EnhancedPing;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

import static dev.foodcans.pluginutils.PluginUtils.Strings.translateColor;

public enum Lang
{
    TOGGLED_SHOWING_ON("Toggled-Showing-On", "&8<&9Ping&8> &7Now showing ping bar."),
    TOGGLED_SHOWING_OFF("Toggled-Showing-Off", "&8<&9Ping&8> &7No longer showing ping bar."),
    PING_FORMAT("Ping-Format", "&9Ping: {0}"),
    PING_FORMAT_ADMIN("Ping-Format-Admin", "&8<&9Ping&8> &7Ping for &f{0}&7: {1}"),
    PING_BAR_FORMAT("Ping-Bar-Format", "&9Ping: {0} {1}"),
    CONFIG_RELOADED("Config-Reloaded", "&8<&9Ping&8> &7Config and pings reloaded!"),
    PLAYER_NOT_FOUND("Player-Not-Found", "&8<&cPing&8> &7Player &f{0} &7not found!"),
    HELP("Help", "&8<&9Ping&8> &7Available commands: &f/pingadmin {0}"),
    HELP_NONE("Help-None", "&8<&cPing&8> &7No available commands!"),
    NOT_ENOUGH_ARGS("Not-Enough-Args", "&8<&cPing&8> &7Not enough args: &f{0}"),
    TOO_MANY_ARGS("Too-Many-Args", "&8<&cPing&8> &7Too many args: &f{0}"),
    COMMAND_ONLY_RUN_BY_PLAYERS("Command-Only-Run-By-Players",
            "&8<&cPing&8> &7This command can only be run by players."),
    LIST_COMMANDS("List-Commands", "&8<&cPing&8> &7To view a list of commands type: &f/pingadmin help"),
    COMMAND_NOT_FOUND("Command-Not-Found",
            "&8<&cPing&8> &7Command not found, to view a list of commands type: &f/pingadmin help"),
    ARGUMENT_MUST_BE_INTEGER("Argument-Must-Be-Integer", "&8<&cPing&8> &f{0} &7must be an integer."),
    ARGUMENT_MUST_BE_DOUBLE("Argument-Must-Be-Double",
            "&8<&cPing&8> &f{0} &7must be an number with or without decimal places."),
    NO_PERMISSION_COMMAND("No-Permission-Command",
            "&8<&cPing&8> &7You do not have the required permission to perform this command: &f{0}");

    private final String path;
    private final String def;

    Lang(String path, String def)
    {
        this.path = path;
        this.def = def;
    }

    public void sendMessage(CommandSender sender, String... replacements)
    {
        String[] messages = replace(replacements).split("\\n");
        Arrays.stream(messages).forEach((message) -> sender.sendMessage(translateColor(message)));
    }

    public String getMessage(String... replacements)
    {
        return translateColor(replace(replacements));
    }

    public String getValue()
    {
        return EnhancedPing.getInstance().getLangFile().getValue(path, def);
    }

    public String getPath()
    {
        return path;
    }

    public String getDefault()
    {
        return def;
    }

    private String replace(String... replacements)
    {
        String message = getValue();
        for (int i = 0; i < replacements.length; i++)
        {
            message = message.replace("{" + i + "}", replacements[i]);
        }
        return message;
    }
}
