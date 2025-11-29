/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Weapon extends Item {
    private int damage;
    private String specialSkill;
    
    public Weapon(String itemName, String rarity, int damage, String specialSkill) {
        super(itemName, rarity);
        this.damage = damage;
        this.specialSkill = specialSkill;
    }
    @Override
    public String use(Entity user) {
        if (user instanceof Character) {
            Character player = (Character) user;
            player.equipWeapon(this);
            return user.getName() + " equipped " + this.itemName;
    }
        return "This item can only used by player";
    }
    
    public int getDamage() {
        return damage;
    }
}
