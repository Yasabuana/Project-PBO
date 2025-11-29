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
    public Item getLoot() {
        double random = Math.random(); // Angka acak 0.0 sampai 1.0
        
        // 50% kemungkinan dapat Potion
        if (random < 0.5) {
            return new Potion("Health Potion", "Common", 20);
        } 
        // 30% kemungkinan dapat Weapon (Pedang Besi)
        else if (random < 0.8) {
            // (Nama, Rarity, Damage, Skill)
            return new Weapon("Iron Sword", "Rare", 15, "Slash");
        } 
        // 20% kemungkinan ZONK (Tidak dapat apa-apa)
        else {
            return null;
        }
    }
    
}
