/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Tank extends Character {
    private int defense;
    private int tauntRange;
    
    public Tank(String name, double x, double y){
        super(name, 250, 1, x, y);
        this.defense = 20;
        this.tauntRange = 5;
    }
    @Override 
    public void basicAttack(Entity target) {
        int damage = 5;
        System.out.println(this.name + " bashes " + target.getName() + " with shield");
        target.takeDamage(damage);
    }
    @Override 
    public void takeDamage(int amount) {
        int reducedDamage = amount - this.defense;
        if (reducedDamage < 1) {
            reducedDamage = 1;
        }
        super.takeDamage(reducedDamage);
    }
    public void shieldUp() {
        this.defense += 10;
        System.out.println(this.name + " raises shield, Defense increased");
    }
    public void taunt() {
        System.out.println(this.name + " taunts all nearby enemies");
    }

}
