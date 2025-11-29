/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image; // <-- Jangan lupa import ini

public abstract class Item {
    protected String itemName;
    protected String rarity;
    
    // --- TAMBAHAN PENTING (Biar Weapon ga merah) ---
    public Image image; 
    public double worldX, worldY;
    public boolean collision = false;
    // -----------------------------------------------
    
    public Item(String itemName, String rarity){
        this.itemName = itemName;
        this.rarity = rarity;
    }
    
    public abstract String use(Entity user);
    
    public String getItemName(){ return this.itemName; }
    public String getRarity(){ return this.rarity; }

    // Metode Gambar (Biar item bisa muncul di tanah)
    public void draw(GraphicsContext gc, GameLoop gp) {
        
        double renderX = worldX - gp.player.getPositionX() + gp.player.screenX;
        double renderY = worldY - gp.player.getPositionY() + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.getPositionX() - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.getPositionX() + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.getPositionY() - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.getPositionY() + gp.player.screenY) {

            if(image != null) {
                gc.drawImage(image, renderX + 8, renderY + 8, 32, 32); 
            } else {
                gc.setFill(javafx.scene.paint.Color.YELLOW);
                gc.fillOval(renderX + 10, renderY + 10, 28, 28);
            }
        }
    }
}
