/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
public class Support extends Character {
    private int healingPower;
    private int buffDuration;
    
    public Support(String name, double x, double y){
        super(name, 130, 1, x, y);
        this.healingPower = 25;
        this.buffDuration = 10;
    }
    @Override
    public void basicAttack(Entity target){
        int damage = 3;
        System.out.println(this.name +" hits " + target.getName()+ " with a staff" );
        target.takeDamage(damage);
    }
    public void healTeam(Character target) {
        target.heal(this.healingPower);
        System.out.println(this.name + " heals " + target.getName() + " for " + this.healingPower + " HP");
    }
    public void buffTeam(Character target) {
        System.out.println(this.name +" buff " + target.getName());
    }
}
