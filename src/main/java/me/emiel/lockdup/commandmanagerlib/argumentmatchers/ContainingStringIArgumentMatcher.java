package me.emiel.lockdup.commandmanagerlib.argumentmatchers;

import me.emiel.lockdup.commandmanagerlib.IArgumentMatcher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filters to leave all the strings that containing the argument string.
 * Example: kill, kick, looking | ki -> kick, kill, looking
 */
public class ContainingStringIArgumentMatcher implements IArgumentMatcher
{
    @Override
    public List<String> filter (List<String> tabCompletions, String argument)
    {
        return tabCompletions.stream().filter(tabCompletion -> tabCompletion.contains(argument)).collect(Collectors.toList());
    }
}
