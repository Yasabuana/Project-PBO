/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
import javafx.scene.image.Image; // 1. Jangan lupa import ini

public class Weapon extends Item {
    private int damage;
    private String specialSkill;
    
    public Weapon(String itemName, String rarity, int damage, String specialSkill) {
        super(itemName, rarity);
        this.damage = damage;
        this.specialSkill = specialSkill;

        // --- 2. LOGIKA LOAD GAMBAR (Di sini naruhnya) ---
        try {
            // Kita arahkan ke file 'iron_sword.png' yang tadi dibuat
            image = new Image(getClass().getResourceAsStream("/assets/items/iron_sword.png"));
        } catch (Exception e) {
            System.out.println("Gagal load gambar weapon: " + e.getMessage());
        }
        // ------------------------------------------------
    }

    @Override
    public String use(Entity user) {
        if (user instanceof Character) {
            Character player = (Character) user;
            player.equipWeapon(this);
            return user.getName() + " equipped " + this.itemName;
        }
        return "This item can only used by player";
    }
    
    public int getDamage() {
        return damage;
    }
}
