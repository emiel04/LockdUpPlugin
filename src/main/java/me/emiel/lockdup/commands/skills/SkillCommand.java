package me.emiel.lockdup.commands.skills;

import me.emiel.lockdup.commandmanagerlib.argumentmatchers.ContainingAllCharsOfStringIArgumentMatcher;
import me.emiel.lockdup.commandmanagerlib.MainCommand;
import me.emiel.lockdup.commandmanagerlib.SubCommand;

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
