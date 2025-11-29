package projectakhirppbo;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Player {

    private int x, y;

    private int speed = 5;
    private int velocityY = 0;
    private boolean onGround = true;
    private final int gravity = 5;

    private boolean moving = false;

    // --- IMAGE ---
    private Image idleImage;
    private Image walkImage;      // hanya 1 frame jalan
    private Image[] attackFrames;

    private int attackIndex = 0;
    private boolean isAttacking = false;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;

        idleImage = load("/projectakhirppbo/assets/characters/idle.png");
        walkImage = load("/projectakhirppbo/assets/characters/walk1.png"); // Gambar jalan 1 frame

        attackFrames = new Image[]{
                load("/projectakhirppbo/assets/characters/attack1.png"),
                load("/projectakhirppbo/assets/characters/attack2.png"),
                load("/projectakhirppbo/assets/characters/attack3.png"),
                load("/projectakhirppbo/assets/characters/attack4.png"),
                load("/projectakhirppbo/assets/characters/attack5.png"),
                load("/projectakhirppbo/assets/characters/attack6.png")
        };
    }

    private Image load(String path) {
        try {
            return new ImageIcon(getClass().getResource(path)).getImage();
        } catch (Exception e) {
            System.out.println("GAGAL LOAD GAMBAR: " + path);
            return null;
        }
    }

    // MOVEMENT
    public void moveLeft() { x -= speed; moving = true; }
    public void moveRight() { x += speed; moving = true; }
    public void moveUp() { y -= speed; moving = true; }
    public void moveDown() { y += speed; moving = true; }

    public void stopMoving() { moving = false; }

    // JUMP
    public void jump() {
        if (onGround) {
            velocityY = -15;
            onGround = false;
        }
    }

    // ATTACK
    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
            attackIndex = 0;
        }
    }

    // UPDATE ANIMASI + GRAVITY
    public void update() {

        // Gravitasi
        if (!onGround) {
            y += velocityY;
            velocityY += gravity;

            if (y >= 300) {
                y = 300;
                onGround = true;
            }
        }

        // Animasi attack
        if (isAttacking) {
            attackIndex++;

            if (attackIndex >= attackFrames.length) {
                attackIndex = 0;
                isAttacking = false;
            }
        }
    }

    public Image getImage() {
        if (isAttacking) return attackFrames[attackIndex];
        if (moving) return walkImage;
        return idleImage;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
