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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import java.util.Set;

public class Player extends Fighter {

    private GameLoop gp; 
    
    // --- GAMBAR / ASET ---
    private Image idleImage;
    private Image walk1, walk2; 
    private Image[] attackFrames;
    
    // --- VARIABEL ANIMASI ---
    private boolean isMoving = false;
    private boolean isAttacking = false;
    
    // Animasi Jalan
    private int spriteCounter = 0;
    private int spriteNum = 1; 
    
    // Animasi Serang
    private int attackIndex = 0;
    private long lastFrameTime = 0; 
    
    // Variabel Arah (Default hadap kanan)
    private String facing = "right";

    public Player(GameLoop gp, String name, int x, int y) {
        super(name, x, y); 
        this.gp = gp;
        this.speed = 4; 
        
        loadImages();
    }

    private void loadImages() {
        attackFrames = new Image[5]; // Sesuaikan dengan jumlah gambar attack (5)

        try {
            idleImage = new Image(getClass().getResourceAsStream("/assets/characters/idle.png"));
            walk1 = new Image(getClass().getResourceAsStream("/assets/characters/walk1.png"));
            
            // Coba load walk2, kalau ga ada pake walk1
            if (getClass().getResourceAsStream("/assets/characters/walk2.png") != null) {
                walk2 = new Image(getClass().getResourceAsStream("/assets/characters/walk2.png"));
            } else {
                walk2 = walk1; 
            }

            for (int i = 0; i < 5; i++) {
                String path = "/assets/characters/attack" + (i + 1) + ".png";
                if (getClass().getResourceAsStream(path) != null) {
                    attackFrames[i] = new Image(getClass().getResourceAsStream(path));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
        }
    }

    public void update(Set<KeyCode> activeKeys) {
        isMoving = false; 

        // 1. LOGIKA GERAK & ANIMASI JALAN
        if (!isAttacking) {
            if (activeKeys.contains(KeyCode.W)) { moveUp(); isMoving = true; }
            if (activeKeys.contains(KeyCode.S)) { moveDown(); isMoving = true; }
            
            if (activeKeys.contains(KeyCode.A)) { 
                moveLeft(); isMoving = true; 
                facing = "left"; // <--- Hadap Kiri
            }
            if (activeKeys.contains(KeyCode.D)) { 
                moveRight(); isMoving = true; 
                facing = "right"; // <--- Hadap Kanan
            }
            
            // Update Animasi Jalan (Ganti kaki)
            if (isMoving) {
                spriteCounter++;
                if (spriteCounter > 10) { 
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else {
                        spriteNum = 1;
                    }
                    spriteCounter = 0;
                }
            } else {
                spriteNum = 1; 
            }
        }

        // 2. LOGIKA SERANGAN
        if (activeKeys.contains(KeyCode.SPACE) && !isAttacking) {
            isAttacking = true;
            attackIndex = 0;
            lastFrameTime = System.nanoTime(); 
        }
        
        if (isAttacking) {
            updateAttackAnimation();
        }
    }

    private void updateAttackAnimation() {
        long now = System.nanoTime();
        if (now - lastFrameTime > 100_000_000) { 
            attackIndex++;
            if (attackIndex >= 5) { 
                attackIndex = 0;
                isAttacking = false; 
            }
            lastFrameTime = now;
        }
    }

    public void draw(GraphicsContext gc) {
        Image imgToDraw = null;

        // A. Pilih Gambar
        if (isAttacking && attackFrames != null) {
            if (attackIndex < 5 && attackFrames[attackIndex] != null) {
                imgToDraw = attackFrames[attackIndex];
            }
        } 
        else if (isMoving) {
            if (spriteNum == 1) imgToDraw = walk1;
            if (spriteNum == 2) imgToDraw = walk2;
        } 
        else {
            imgToDraw = idleImage;
        }

        // B. Gambar ke Layar dengan FLIP (Cermin)
        if (imgToDraw != null) {
            gc.setImageSmoothing(false);
            
            double visualSize = 96; 
            double visualX = getPositionX() - (visualSize - 48) / 2;
            double visualY = getPositionY() - (visualSize - 48) / 2;

            if (facing.equals("left")) {
                // FLIP GAMBAR (Geser ke kanan, lalu gambar lebar negatif)
                gc.drawImage(imgToDraw, visualX + visualSize, visualY, -visualSize, visualSize);
            } else {
                // GAMBAR NORMAL
                gc.drawImage(imgToDraw, visualX, visualY, visualSize, visualSize);
            }
            
        } else {
            // Fallback Kotak Biru
            gc.setFill(javafx.scene.paint.Color.BLUE);
            gc.fillRect(getPositionX(), getPositionY(), 48, 48);
        }
    }
}