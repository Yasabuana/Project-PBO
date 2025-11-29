package Games_RPG;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yasaw
 */
public abstract class Entity {
    protected String name;
    protected int health;
    protected int maxhealth;
    protected double positionX;
    protected double positionY;
    protected double speed = 4.0;
    
    public Entity(String name, int maxhealth, double x, double y){
        this.name = name;
        this.health = maxhealth;
        this.maxhealth = maxhealth;
        this.positionX = x;
        this.positionY = y;
    }
    
    public abstract void basicAttack(Entity target);
    
    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
        System.out.println(name + " takes " + amount + " damage ");
    }
    
    public boolean isAlive() {
        return this.health > 0;
    }

    public String getName(){
        return this.name;
    }
    
    public int getHealth(){
        return this.health;
    }
    public void heal(int amount){
        this.health += amount;
        if (this.health > this.maxhealth) {
            this.health = this.maxhealth;
    }
    System.out.println(this.name + " healed for " + amount + " HP");
    }
    public void moveUp(){
        this.positionY -= speed;
    }
    public void moveDown(){
        this.positionY += speed;
    }
    public void moveLeft(){
        this.positionX -= speed;
    }
    public void moveRight(){
        this.positionX += speed;
    }
    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }
    public double getPositionX() {
        return this.positionX;
    }
    public double getPositionY() {
        return this.positionY;
    }
}

            

