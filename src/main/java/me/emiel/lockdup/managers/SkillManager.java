package me.emiel.lockdup.managers;

import me.emiel.lockdup.storage.DataManager;

import java.util.ArrayList;
import java.util.List;

public class SkillManager {
    private final List<String> skills;

    public SkillManager(DataManager dataManager){
        this.skills = dataManager.getSkillData();
    }

    public List<String> getSkills() {
        return skills;
    }
    public boolean exists(String skill){
        return skills.contains(skill);
    }
    public void add(String skill){
        if (skills.contains(skill)){
            return;
        }

        skills.add(skill);
    }

    public void remove(String skill) {
        skills.remove(skill);
    }
}
