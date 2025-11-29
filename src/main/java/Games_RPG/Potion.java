/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Potion extends Item{
    private int effect;
    private int duration;
    
    public Potion(String itemName, String rarity, int effect){
        super(itemName,rarity);
        this.effect = effect;
        this.duration = 0;
    }
    @Override
    public String use(Entity user) {
        user.health += this.effect;
        if (user.health > user.maxhealth){
            user.health = user.maxhealth;
        }
        return user.getName() + " uses " + this.itemName + " and heals " + this.effect + "HP";
    }
}
