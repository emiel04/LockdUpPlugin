package me.emiel.lockdup.Commands.Coin;

import me.emiel.lockdup.CommandManagerLib.ArgumentMatchers.ContainingAllCharsOfStringIArgumentMatcher;
import me.emiel.lockdup.CommandManagerLib.MainCommand;
import me.emiel.lockdup.CommandManagerLib.SubCommand;
import me.emiel.lockdup.Commands.Cells.*;

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
