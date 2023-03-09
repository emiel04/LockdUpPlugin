package me.emiel.lockdup.Commands;

import me.emiel.lockdup.CommandManagerLib.ArgumentMatcher;
import me.emiel.lockdup.CommandManagerLib.ArgumentMatchers.ContainingAllCharsOfStringArgumentMatcher;
import me.emiel.lockdup.CommandManagerLib.MainCommand;
import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Commands.Skills.AddExp;
import me.emiel.lockdup.Commands.Skills.CreateSkill;
import me.emiel.lockdup.Helper.MessageSender;
import me.emiel.lockdup.Model.HelpModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCommand extends MainCommand {
    public BaseCommand() {

        super("You do not have permission to use this command.", new ContainingAllCharsOfStringArgumentMatcher());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        getHelpSubCommand().perform(sender, args);

        return true;
    }

    @Override
    protected void registerSubCommands() {
        SubCommand[] commands = {
            new AddExp(),
            new CreateSkill(),
            new HelpCmd()
        };
        subCommands.addAll(List.of(commands));

    }
}
