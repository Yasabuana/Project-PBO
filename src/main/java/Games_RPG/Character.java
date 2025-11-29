/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author yasaw
 */
public abstract class Character extends Entity {
    protected int levelCharacter;
    protected List<Item> inventory;
    protected Weapon equippedWeapon;
    protected Armor equippedArmor;
    
    public Character(String name, int maxHealth, int level, double x, double y) {
        super(name, maxHealth, x, y);
        this.levelCharacter = level;
        this.inventory = new ArrayList<>();
    }
    
    public void addItemToInventory(Item item) {
        this.inventory.add(item);
        System.out.println(item.getItemName() + " added to inventory");
    }
    
    public void equipWeapon(Weapon weapon) {
        this.equippedWeapon = weapon;
        System.out.println(weapon.getItemName() + " has been equipped");
    }
    
    public void equipArmor(Armor armor) {
        this.equippedArmor = armor;
        System.out.println(armor.getItemName() + " has been equipped");
    }
}
