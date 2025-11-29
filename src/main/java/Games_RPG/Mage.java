/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Mage extends Character {
    private int mana;
    private int intelligence;
    private String spellBook;
    
    public Mage(String name, double x, double y) {
        super(name, 100, 1, x, y);
        this.mana = 150;
        this.intelligence = 15;
        this.spellBook = "Fireball";
    }
    @Override
    public void basicAttack(Entity target) {
        int damage = this.intelligence;
        System.out.println("casts a magic bolt at " + target.getName());
        target.takeDamage(damage);
    } 
    public void castSpell(Entity target) {
        if (this.mana >= 20) {
            int damage = this.intelligence * 3;
            System.out.println(this.name + " casts " + this.spellBook);
            target.takeDamage(damage);
            this.mana -= 20;
        } else {
            System.out.println(this.name +" is out of mana");
        }
    }
    public void regenerateMana(){
        this.mana += 10;
        if(this.mana > 150) this.mana = 150; 
    }
    public void magicBurst(Entity target) {
        
    }
    
}
