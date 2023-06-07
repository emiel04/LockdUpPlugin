package me.emiel.lockdup.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum Size{
    Small,
    Medium,
    Large;

    public static Collection<String> names() {
        List<String> result = new ArrayList<>();
        for (Size size :
                Size.values()) {
            result.add(size.name());
        }
        return result;
    }
}
