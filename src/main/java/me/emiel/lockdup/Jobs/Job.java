package me.emiel.lockdup.Jobs;

import com.sk89q.worldguard.protection.flags.StateFlag;
import me.emiel.lockdup.LockdUp;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class Job {
    protected String name;
    protected StateFlag flag;
    public Job(String name, StateFlag flag){
        this.name = name;
        this.flag = flag;;
    }
    protected abstract List<Listener> getListeners();
    public void setListeners(){
        List<Listener> listeners = getListeners();
        LockdUp plugin = LockdUp.getInstance();
        for (Listener l:
                listeners) {
            plugin.getServer().getPluginManager().registerEvents(l, plugin);
        }
    }
}
