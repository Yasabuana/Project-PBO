/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Armor extends Item {
    private int defense;
    private int durability;
    
    public Armor(String itemName, String rarity, int defense, int durability) {
        super(itemName, rarity);
        this.defense = defense;
        this.durability = durability;
    }
    
    @Override
    public String use(Entity user) {
        if (user instanceof Character) {
            Character player = (Character) user;
            player.equipArmor(this);
            return user.getName() +" equipped " + this.itemName;
        }
        return "This item can only used by player";
    }
    
    public int getDefense() {
        return defense;
    }
    
}
