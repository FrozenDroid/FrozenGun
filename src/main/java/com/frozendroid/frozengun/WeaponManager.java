package com.frozendroid.frozengun;

import com.frozendroid.frozengun.models.Gun;
import com.frozendroid.frozengun.models.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WeaponManager {

    public static ArrayList<Gun> getGuns() {
        ArrayList<Gun> guns = new ArrayList<>();
        List<Weapon> weapons = MinigameManager.getWeapons().stream().filter(weapon -> weapon instanceof Gun).collect(Collectors.toList());
        for (Weapon weapon : weapons) {
            Gun gun = (Gun) weapon;
            guns.add(gun);
        }
        return guns;
    }

    public static ArrayList<Weapon> getWeapons() {
        return (ArrayList<Weapon>) MinigameManager.getWeapons();
    }

    public static void addWeapon(Weapon weapon) {
        MinigameManager.addWeapon(weapon);
    }

    public static Weapon findByName(String name) {
        return MinigameManager.getWeapons().stream().filter(weapon -> weapon.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
