/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Assasin extends Character {
    private int agility;
    private int criticalDamage;
    private boolean stealthMode;
    
    public Assasin(String name, double x, double y){
        super(name, 120, 1, x, y);
        this.agility = 20;
        this.criticalDamage = 50;
        this.stealthMode = false;
    }
    @Override 
    public void basicAttack(Entity target) {
        int damage = this.agility;
        if (Math.random() < 0.20){
            damage += this.criticalDamage;
            System.out.println("Critical Hit");
        }
        System.out.println(this.name + " quickly stabs " + target.getName());
        target.takeDamage(damage);
    }
    
    public void backStab(Entity target) {
        int damage = this.agility * 4;
        if(this.stealthMode) {
            damage *= 2;
            System.out.println(this.name +" performs a deadly backstab");
            this.stealthMode = false;
        } else {
            System.out.println(this.name +" performs a backstab");
        }
        target.takeDamage(damage);
    }
    public void enterStealth() {
        this.stealthMode = true;
        System.out.println(this.name +" enters stealth mode");
    }
}
