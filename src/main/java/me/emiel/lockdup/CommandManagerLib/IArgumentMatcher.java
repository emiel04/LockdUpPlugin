package me.emiel.lockdup.CommandManagerLib;

import java.util.List;

public interface IArgumentMatcher
{
    /**
     * Filters tabCompletions based on argument string.
     * @param tabCompletions The tabCompletions to filter.
     * @param argument The argument string.
     * @return The filtered tabCompletions.
     */
    public List<String> filter (List<String> tabCompletions, String argument);
}
