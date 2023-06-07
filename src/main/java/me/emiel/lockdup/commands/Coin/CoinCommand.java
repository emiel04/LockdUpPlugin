package me.emiel.lockdup.commands.Coin;

import me.emiel.lockdup.commandmanagerlib.ArgumentMatchers.ContainingAllCharsOfStringIArgumentMatcher;
import me.emiel.lockdup.commandmanagerlib.MainCommand;
import me.emiel.lockdup.commandmanagerlib.SubCommand;

import java.util.List;

public class CoinCommand extends MainCommand {
    public CoinCommand() {
        super("You do not have permission to use this command.", new ContainingAllCharsOfStringIArgumentMatcher());
    }

    @Override
    protected void registerSubCommands() {
        SubCommand[] commands = {
                new Balance(),
                new GetCoin(),
                new RemoveCoin()
        };
        subCommands.addAll(List.of(commands));
    }
}
