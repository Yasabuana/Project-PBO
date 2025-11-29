/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Fighter extends Character{
    private int strength;
    private int stamina;
    
    public Fighter(String name, double x, double y) {
        super(name, 150, 1, x, y);
        this.strength = 10;
        this.stamina = 100;
    }
    
    @Override
    public void basicAttack(Entity target) {
        int damage = this.strength + (this.levelCharacter * 2);
        System.out.println(this.name +" Fighter attacks " + target.getName());
        target.takeDamage(damage);
    }
    
    public void powerStrike(Entity target) {
        int damage = (int)(this.strength * 2.5);
        this.stamina -= 20;
        System.out.println(this.name + " uses Power Strike");
        target.takeDamage(damage);
    }
}
