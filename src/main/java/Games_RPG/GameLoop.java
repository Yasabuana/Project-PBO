/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;


import java.util.HashSet;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class GameLoop extends Application {

    // --- 1. SETTINGAN LAYAR (Fix dari Checklist) ---
    // Agar konsisten dengan aset visual temanmu
    final int originalTileSize = 16; // Ukuran asli sprite: 16x16 pixel
    final int scale = 3; // Skala perbesaran

    public final int tileSize = originalTileSize * scale; // 48x48 pixel
    public final int maxScreenCol = 16; // Lebar: 16 tile
    public final int maxScreenRow = 12; // Tinggi: 12 tile

    // Ukuran layar final (768 x 576 pixel)
    public final int screenWidth = tileSize * maxScreenCol; 
    public final int screenHeight = tileSize * maxScreenRow; 
    // -----------------------------------------------------------

    private GraphicsContext gc;
    public static GameState gameState;
    private Set<KeyCode> activeKeys = new HashSet<>();

    // --- VARIABEL GAMEPLAY ---
    private Character player;
    private Enemy slime;
    private boolean isSlimeDead = false;
    
    // Cooldown & Range
    private long lastSlimeAttackTime = 0;
    private final long SLIME_ATTACK_COOLDOWN_NS = 2_000_000_000L; 
    private final double AGGRO_RANGE = 250; 
    private final double ATTACK_RANGE = 45;    
    
    private long lastPlayerAttackTime = 0;
    private final long PLAYER_ATTACK_COOLDOWN_NS = 1_000_000_000L;
    
    // Waktu Global
    private long globalNanoTime; 

    
    // private TileManager tileManager;
    // private EnemyManager enemyManager;
    // private UI ui;

    public static void main(String[] args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Game RPG Project PBO - Kelompok 5");

        //VARIABEL SCREENWIDTH & HEIGHT 
        Canvas canvas = new Canvas(screenWidth, screenHeight);
        gc = canvas.getGraphicsContext2D(); 

        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Inisialisasi Objek
        player = new Fighter("Hero", 100, 300);
        slime = new Enemy("Slime", 50, 5, 10, 600, 300);

        // Set State Awal
        gameState = GameState.MENU;

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                globalNanoTime = currentNanoTime;
                update();
                render();
            }
        };
        gameLoop.start(); 
    }
    
    private void update() {
        switch(gameState) {
            case MENU:
                if (activeKeys.contains(KeyCode.ENTER)) {
                    gameState = GameState.PLAYING;
                    System.out.println("Game Started!");
                }
                break;
                
            case PLAYING:
             
                
                //  Gerak Player
                double oldX = player.getPositionX();
                if (activeKeys.contains(KeyCode.A)) player.moveLeft();
                if (activeKeys.contains(KeyCode.D)) player.moveRight();

                if (slime.isAlive() && isColliding(player, slime)) {
                    player.setPosition(oldX, player.getPositionY());
                }

                double oldY = player.getPositionY();
                if (activeKeys.contains(KeyCode.W)) player.moveUp();
                if (activeKeys.contains(KeyCode.S)) player.moveDown();
                
                if (slime.isAlive() && isColliding(player, slime)) {
                    player.setPosition(player.getPositionX(), oldY);
                }

                // Serangan Player
                if (activeKeys.contains(KeyCode.SPACE)) {
                    if ((globalNanoTime - lastPlayerAttackTime) > PLAYER_ATTACK_COOLDOWN_NS) {
                        if (getDistance(player, slime) < 50 && slime.isAlive()) {
                            player.basicAttack(slime); 
                            lastPlayerAttackTime = globalNanoTime;
                        }
                    }
                }
                
                //  AI Slime
                if (slime.isAlive() && player.isAlive()) {
                    double distance = getDistance(player, slime);
                    
                    if (distance < ATTACK_RANGE) { 
                        if ((globalNanoTime - lastSlimeAttackTime) > SLIME_ATTACK_COOLDOWN_NS) {
                            slime.basicAttack(player);
                            lastSlimeAttackTime = globalNanoTime;
                        }
                    }
                    else if (distance < AGGRO_RANGE) {
                        double dx = player.getPositionX() - slime.getPositionX();
                        double dy = player.getPositionY() - slime.getPositionY();
                        double slimeSpeed = 2.0; 
                        
                        if (distance > 0) { 
                            double moveX = (dx / distance) * slimeSpeed;
                            double moveY = (dy / distance) * slimeSpeed;
                            slime.setPosition(slime.getPositionX() + moveX, slime.getPositionY() + moveY);
                        }
                    }
                }
                
                // 4. Loot Drop 
                if (!isSlimeDead && !slime.isAlive() ) {
                    isSlimeDead = true;
                   
                    Potion loot = new Potion("Small Potion", "Common", 30);
                    player.addItemToInventory(loot);
                    System.out.println(slime.getName() + " dropped a " + loot.getItemName());
                }
                
              
                break;
                
            case GAME_OVER:
                if (activeKeys.contains(KeyCode.ENTER)) {
                    gameState = GameState.MENU;
               
                }
                break;
        }
    }
    
 
    private void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, screenWidth, screenHeight); // Pakai variabel layar dinamis
        
        switch(gameState) {
            case MENU:
                drawMenu();
                break;
                
            case PLAYING:
                
                
                // Gambar Player
                gc.setFill(Color.BLUE);
                gc.fillRect(player.getPositionX(), player.getPositionY(), 40, 40); 
                gc.setFill(Color.WHITE);
                gc.fillText(player.getName(), player.getPositionX(), player.getPositionY() - 20);
                gc.fillText("HP: " + player.getHealth(), player.getPositionX(), player.getPositionY() - 5);

                // Gambar Slime
                if (slime.isAlive()) {
                    gc.setFill(Color.GREEN);
                } else {
                    gc.setFill(Color.DARKRED);
                }
                gc.fillRect(slime.getPositionX(), slime.getPositionY(), 30, 30); 
                gc.setFill(Color.WHITE);
                gc.fillText(slime.getName(), slime.getPositionX(), slime.getPositionY() - 20);
                gc.fillText("HP: " + slime.getHealth(), slime.getPositionX(), slime.getPositionY() - 5);
                
              
                break;
                
            case GAME_OVER:
                drawGameOver();
                break;
        }
    }

    // --- HELPER METHODS ---
    private void drawMenu() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 40));
        // Posisi tengah dinamis
        gc.fillText("RPG GAME PROJECT", screenWidth / 2 - 180, screenHeight / 2);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Press ENTER to Play", screenWidth / 2 - 100, screenHeight / 2 + 50);
    }

    private void drawGameOver() {
        gc.setFill(Color.RED);
        gc.setFont(new Font("Arial", 50));
        gc.fillText("GAME OVER", screenWidth / 2 - 140, screenHeight / 2);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Press ENTER to Menu", screenWidth / 2 - 100, screenHeight / 2 + 50);
    }

    private boolean isColliding(Entity player, Entity enemy) {
        double playerWidth = 40;
        double playerHeight = 40;
        double enemyWidth = 30;
        double enemyHeight = 30;
        
        return player.getPositionX() < enemy.getPositionX() + enemyWidth &&
               player.getPositionX() + playerWidth > enemy.getPositionX() &&
               player.getPositionY() < enemy.getPositionY() + enemyHeight &&
               player.getPositionY() + playerHeight > enemy.getPositionY();
    }
    
    private double getDistance(Entity e1, Entity e2) {
        double dx = e1.getPositionX() - e2.getPositionX();
        double dy = e1.getPositionY() - e2.getPositionY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}


