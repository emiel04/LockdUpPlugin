package me.emiel.lockdup.Helper;

import me.emiel.lockdup.LockdUp;
import me.emiel.lockdup.Model.Cell;
import me.emiel.lockdup.Model.Size;

public class RentCalculator {
    public static int GetDefaultRent(Size size){
        LockdUp plugin = LockdUp.getInstance();
        return (plugin.getConfig().getInt( "rent." + size.name().toLowerCase()));
    }

    public static int GetRent(Cell cell) {
        int secondsLeft = cell.get_timeLeft();
        int rent = GetDefaultRent(cell.get_size());
        //(5441799) / (86400*2) *64
        int newRent = (int) (secondsLeft / (86400f * 2f) * rent);
        return Math.max(newRent, rent);
    }

}
