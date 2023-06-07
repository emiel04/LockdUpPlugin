package me.emiel.lockdup.Commands.Skills;

import me.emiel.lockdup.CommandManagerLib.ArgumentMatchers.ContainingAllCharsOfStringIArgumentMatcher;
import me.emiel.lockdup.CommandManagerLib.MainCommand;
import me.emiel.lockdup.CommandManagerLib.SubCommand;

import java.util.List;

public class SkillCommand extends MainCommand {
    public SkillCommand() {
        super("You do not have permission to use this command.", new ContainingAllCharsOfStringIArgumentMatcher());
    }

    @Override
    protected void registerSubCommands() {
        SubCommand[] commands = {
                new HelpCmd(),
                new AddExp(),
                new CreateSkill(),
                new ListSkills(),
                new RemoveSkill(),
                new SkillInfo()
        };
        subCommands.addAll(List.of(commands));

    }
}
