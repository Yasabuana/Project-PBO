/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
import javafx.scene.image.Image; // 1. Jangan lupa Import ini

public class Potion extends Item {
    
    private int healAmount;

    public Potion(String itemName, String rarity, int healAmount) {
        super(itemName, rarity);
        this.healAmount = healAmount;

        // --- 2. LOGIKA LOAD GAMBAR ---
        try {
            // Arahkan ke file potion_red.png
            image = new Image(getClass().getResourceAsStream("/assets/items/potion_red.png"));
        } catch (Exception e) {
            System.out.println("Gagal load gambar potion: " + e.getMessage());
        }
        // -----------------------------
    }

    @Override
    public String use(Entity user) {
        // Logika heal
        user.heal(this.healAmount);
        return user.getName() + " used " + this.itemName + " and recovered " + this.healAmount + " HP!";
    }
    
    public int getHealAmount() {
        return healAmount;
    }
}