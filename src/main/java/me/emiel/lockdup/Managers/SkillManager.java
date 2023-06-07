package me.emiel.lockdup.Managers;

import me.emiel.lockdup.Storage.DataManager;

import java.util.ArrayList;

public class SkillManager {
    private final ArrayList<String> skills;

    public SkillManager(DataManager dataManager){
        this.skills = dataManager.getSkillData();
    }

    public ArrayList<String> getSkills() {
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
