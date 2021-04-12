package fr.youki300.giantv2.utils.command;

import fr.youki300.giantv2.GiantPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class GCommand implements CommandExecutor {

    private final String commandName;
    private final String permission;
    private final boolean consoleCanExecute;
    private final GiantPlugin giantPlugin;

    public GCommand(GiantPlugin giantPlugin, String commandName, String permission, boolean consoleCanExecute) {
        this.permission = permission;
        this.commandName = commandName;
        this.consoleCanExecute = consoleCanExecute;
        this.giantPlugin = giantPlugin;
        giantPlugin.getCommand(commandName).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!command.getLabel().equalsIgnoreCase(commandName))
            return true;

        if (!consoleCanExecute && !(sender instanceof Player)) {
            sender.sendMessage(giantPlugin.getMessageConfig().getString("NOT-PLAYER"));
            return true;
        }

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(giantPlugin.getMessageConfig().getString("NO-PERMISSION"));
            return true;
        }

        return execute(sender, args);
    }

    public abstract boolean execute(CommandSender sender, String[] args);
}