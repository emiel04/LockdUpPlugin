package me.emiel.lockdup.Commands.Cells;

import me.emiel.lockdup.CommandManagerLib.ArgumentMatchers.ContainingAllCharsOfStringIArgumentMatcher;
import me.emiel.lockdup.CommandManagerLib.MainCommand;
import me.emiel.lockdup.CommandManagerLib.SubCommand;

import java.util.List;

public class CellCommand extends MainCommand {
    public CellCommand() {
        super("You do not have permission to use this command.", new ContainingAllCharsOfStringIArgumentMatcher());
    }

    @Override
    protected void registerSubCommands() {
        SubCommand[] commands = {
            new AddTrust(),
                new RemoveTrust(),
                new CreateCell(),
                new DeleteCell(),
                new RemoveAllCells(),
                new GetCellDoor(),
                new GetCellVoucher(),
                new InitializeCellWorld(),
                new ListAllCells()
        };
        subCommands.addAll(List.of(commands));

    }
}
