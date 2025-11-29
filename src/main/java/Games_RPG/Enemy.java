/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Enemy extends Entity{
    private int baseDamage;
    private int expDropped;
    
    public Enemy(String name, int maxHealth, int baseDamage, int expDropped, double x, double y) {
        super(name, maxHealth, x, y);
        this.baseDamage = baseDamage;
        this.expDropped = expDropped;
    }
    @Override
    public void basicAttack(Entity target) {
        System.out.println(this.name + " attacks " + target.getName());
        target.takeDamage(this.baseDamage);
    }
    
}
