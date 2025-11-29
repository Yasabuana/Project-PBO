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
    private int spriteCounter = 0;
    private int spriteNum = 1; 
    private int attackIndex = 0;
    private long lastFrameTime = 0; 
    
    private String facing = "right";

    // --- POSISI LAYAR (KAMERA) ---
    public final double screenX;
    public final double screenY;

    public Player(GameLoop gp, String name, int x, int y) {
        super(name, x, y); 
        this.gp = gp;
        this.speed = 4; 
        
        // Hitung tengah layar
        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        
        loadImages();
    }

    private void loadImages() {
        attackFrames = new Image[5];
        try {
            idleImage = new Image(getClass().getResourceAsStream("/assets/characters/idle.png"));
            walk1 = new Image(getClass().getResourceAsStream("/assets/characters/walk1.png"));
            
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

        // 1. LOGIKA GERAK (Hanya jalan kalau tidak sedang menyerang)
        if (!isAttacking) {
            
            // Cek Tabrakan sebelum jalan
            if (activeKeys.contains(KeyCode.W)) { 
                if (gp.tileManager.checkCollision(this, "up") == false) {
                    moveUp(); 
                }
                isMoving = true; 
            }
            if (activeKeys.contains(KeyCode.S)) { 
                if (gp.tileManager.checkCollision(this, "down") == false) {
                    moveDown(); 
                }
                isMoving = true; 
            }
            if (activeKeys.contains(KeyCode.A)) { 
                if (gp.tileManager.checkCollision(this, "left") == false) {
                    moveLeft(); 
                }
                isMoving = true; 
                facing = "left"; 
            }
            if (activeKeys.contains(KeyCode.D)) { 
                if (gp.tileManager.checkCollision(this, "right") == false) {
                    moveRight(); 
                }
                isMoving = true; 
                facing = "right"; 
            }
            
            // Update Animasi Jalan
            if (isMoving) {
                spriteCounter++;
                if (spriteCounter > 10) { 
                    if (spriteNum == 1) spriteNum = 2;
                    else spriteNum = 1;
                    spriteCounter = 0;
                }
            } else {
                spriteNum = 1; 
            }
        }

        // 2. LOGIKA SERANGAN (Ini yang kemarin hilang!)
        if (activeKeys.contains(KeyCode.SPACE) && !isAttacking) {
            isAttacking = true;
            attackIndex = 0;
            lastFrameTime = System.nanoTime(); 
        }
        
        // Update Frame Animasi Serangan
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

        if (imgToDraw != null) {
            gc.setImageSmoothing(false);
            
            double visualSize = 96; 
            
            // Menggambar di posisi Layar (screenX/Y) agar kamera bekerja
            double visualX = screenX - (visualSize - 48) / 2;
            double visualY = screenY - (visualSize - 48) / 2;

            if (facing.equals("left")) {
                gc.drawImage(imgToDraw, visualX + visualSize, visualY, -visualSize, visualSize);
            } else {
                gc.drawImage(imgToDraw, visualX, visualY, visualSize, visualSize);
            }
            
        } else {
            gc.setFill(javafx.scene.paint.Color.BLUE);
            gc.fillRect(screenX, screenY, 48, 48);
        }
    }
}