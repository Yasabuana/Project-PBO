/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enemy;

/**
 *
 * @author Hi Luluuu
 */

public class Enemy extends Entity {

    private double damage;
    private double moveSpeed;
    private boolean alive = true;

    public Enemy(String name, int maxHealth, double damage, double moveSpeed, double x, double y) {
        super(name, maxHealth, x, y);
        this.damage = damage;
        this.moveSpeed = moveSpeed;
    }

    public void basicAttack(Entity target) {
        if (!alive) return;
        target.takeDamage(damage);
        System.out.println(name + " hits " + target.getName() + " for " + damage);
    }

    public boolean isAlive() {
        return alive && health > 0;
    }

    @Override
    public void takeDamage(double dmg) {
        super.takeDamage(dmg);
        if (health <= 0) {
            alive = false;
        }
    }

    public double getDamage() { return damage; }
    public double getMoveSpeed() { return moveSpeed; }
}
