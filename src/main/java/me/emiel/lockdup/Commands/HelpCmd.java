package me.emiel.lockdup.Commands;

import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.LockdUp;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCmd implements SubCommand
{
    @Override
    public String getName ()
    {
        return "help";
    }

    @Override
    public String getDescription ()
    {
        return "Shows help for this plugin";
    }

    @Override
    public String getSyntax ()
    {
        return "/lockdup help";
    }

    @Override
    public String getPermission ()
    {
        return "lockdup.help";
    }

    @Override
    public List<String> getTabCompletion (int index, String[] args)
    {
        return null;
    }

    @Override
    public void perform (CommandSender sender, String[] args)
    {
        sender.sendMessage("LockdUp version " + LockdUp.getInstance().getDescription().getVersion());
        for (SubCommand subCommand : LockdUp.getMainCommand().getSubCommands())
        {
            if (sender.hasPermission(subCommand.getPermission()))
                sender.sendMessage(subCommand.getSyntax() + " - " + subCommand.getDescription() + " (" + subCommand.getPermission() + ")");
        }

    }
}
