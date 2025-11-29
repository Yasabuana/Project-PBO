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

    final int originalTileSize = 16; 
    final int scale = 3; 
    public final int tileSize = originalTileSize * scale; // 48x48

    // --- 2. SETTINGAN LAYAR (KAMERA) ---
    // Variabel ini HARUS ADA sebelum screenWidth dihitung
    public final int maxScreenCol = 16; 
    public final int maxScreenRow = 12; 
    
    // Baru hitung ini (menggunakan variabel di atasnya)
    public final int screenWidth = tileSize * maxScreenCol;  
    public final int screenHeight = tileSize * maxScreenRow;
    
    public final int maxWorldCol = 50; 
    public final int maxWorldRow = 50; 
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    private GraphicsContext gc;
    public static GameState gameState;
    private Set<KeyCode> activeKeys = new HashSet<>();
    
    public TileManager tileManager;

    // --- VARIABEL GAMEPLAY ---
    public Player player;
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
        
        tileManager = new TileManager(this);
        // Inisialisasi Objek
        player = new Player(this, "Hero", 500, 500);
        slime = new Enemy("Slime", 50, 5, 10, 500, 700);
        slime = new Enemy("Slime", 50, 5, 10, 600, 740);
        slime = new Enemy("Slime", 50, 5, 10, 800, 800);

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
    
    // --- METODE UPDATE (OTAK) ---
    private void update() {
        switch(gameState) {
            case MENU:
                if (activeKeys.contains(KeyCode.ENTER)) {
                    gameState = GameState.PLAYING;
                    System.out.println("Game Started!");
                }
                break;
                
            case PLAYING:
                // 1. UPDATE PLAYER (Gerak, Animasi, Serang)
                double oldX = player.getPositionX();
                double oldY = player.getPositionY();
                
                player.update(activeKeys); // Biarkan Player urus diri sendiri

                // 2. CEK COLLISION
                if (slime.isAlive() && isColliding(player, slime)) {
                    player.setPosition(oldX, oldY);
                }

                // 3. LOGIKA DAMAGE (Saat Player Serang)
                if (activeKeys.contains(KeyCode.SPACE)) {
                    if ((globalNanoTime - lastPlayerAttackTime) > PLAYER_ATTACK_COOLDOWN_NS) {
                        if (getDistance(player, slime) < 50 && slime.isAlive()) {
                            player.basicAttack(slime); 
                            lastPlayerAttackTime = globalNanoTime;
                        }
                    }
                }
                
                // 4. AI SLIME (Kejar & Serang)
             
                if (slime.isAlive() && player.isAlive()) {
                    double distance = getDistance(player, slime);
                    
                    if (distance < ATTACK_RANGE) { 
                        if ((globalNanoTime - lastSlimeAttackTime) > SLIME_ATTACK_COOLDOWN_NS) {
                            slime.basicAttack(player);
                            lastSlimeAttackTime = globalNanoTime;
                        }
                    }
                    else if (distance < AGGRO_RANGE) {
                        // Hitung arah
                        double dx = player.getPositionX() - slime.getPositionX();
                        double dy = player.getPositionY() - slime.getPositionY();
                        
                        // Set kecepatan slime (PENTING: update variabel speed di entity juga biar collision akurat)
                        double slimeSpeed = 1.0; 
                        slime.speed = slimeSpeed; // Sinkronkan speed entity
                        
                        if (distance > 0) { 
                            double moveX = (dx / distance) * slimeSpeed;
                            double moveY = (dy / distance) * slimeSpeed;
                            
                            // --- CEK TABRAKAN SUMBU X ---
                            boolean hitWallX = false;
                            if (moveX > 0) hitWallX = tileManager.checkCollision(slime, "right");
                            else if (moveX < 0) hitWallX = tileManager.checkCollision(slime, "left");
                            
                            double nextX = slime.getPositionX() + moveX;
                            // Cek tabrak player di X
                            boolean hitPlayerX = nextX < player.getPositionX() + 40 &&
                                                 nextX + 30 > player.getPositionX() &&
                                                 slime.getPositionY() < player.getPositionY() + 40 &&
                                                 slime.getPositionY() + 30 > player.getPositionY();

                            // Kalau aman (gak nabrak tembok & gak nabrak player), jalan X
                            if (!hitWallX && !hitPlayerX) {
                                slime.setPosition(nextX, slime.getPositionY());
                            }

                            // --- CEK TABRAKAN SUMBU Y ---
                            boolean hitWallY = false;
                            if (moveY > 0) hitWallY = tileManager.checkCollision(slime, "down");
                            else if (moveY < 0) hitWallY = tileManager.checkCollision(slime, "up");
                            
                            double nextY = slime.getPositionY() + moveY;
                            // Cek tabrak player di Y
                            boolean hitPlayerY = slime.getPositionX() < player.getPositionX() + 40 &&
                                                 slime.getPositionX() + 30 > player.getPositionX() &&
                                                 nextY < player.getPositionY() + 40 &&
                                                 nextY + 30 > player.getPositionY();

                            // Kalau aman, jalan Y
                            if (!hitWallY && !hitPlayerY) {
                                slime.setPosition(slime.getPositionX(), nextY);
                            }
                        }
                    }
                }
                
               // 5. LOOT DROP (DENGAN SISTEM GACHA)
                if (!isSlimeDead && !slime.isAlive() ) {
                    isSlimeDead = true;
                    
                    // Minta item dari musuh
                    Item loot = slime.getLoot();
                    
                    if (loot != null) {
                        // Kalau dapat item, masukkan ke tas player
                        player.addItemToInventory(loot);
                        
                        // Cek tipe item untuk pesan yang lebih detail
                        if (loot instanceof Weapon) {
                            System.out.println("WOW! " + slime.getName() + " dropped a WEAPON: " + loot.getItemName());
                        } else {
                            System.out.println(slime.getName() + " dropped a " + loot.getItemName());
                        }
                    } else {
                        System.out.println(slime.getName() + " died but dropped nothing.");
                    }
                }
                break; // Break yang benar
                
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
                
                if (tileManager != null) {
                    tileManager.draw(gc);
                 }
                // Gambar Player
                player.draw(gc);
                gc.setFill(Color.WHITE);
                gc.fillText(player.getName(), player.screenX, player.screenY - 20);
                gc.fillText("HP: " + player.getHealth(), player.screenX, player.screenY - 5);

                // Gambar Slime
                // --- GAMBAR SLIME (DENGAN KAMERA) ---
                if (slime.isAlive()) {
                    // 1. Hitung Posisi Slime di Layar (Rumus Kamera)
                    // Posisi Screen = Posisi Dunia Slime - Posisi Dunia Player + Posisi Tengah Layar
                    double slimeScreenX = slime.getPositionX() - player.getPositionX() + player.screenX;
                    double slimeScreenY = slime.getPositionY() - player.getPositionY() + player.screenY;

                    // 2. Cek apakah Slime masuk layar (Optimasi biar ga berat)
                    if (slime.getPositionX() + 48 > player.getPositionX() - player.screenX &&
                        slime.getPositionX() - 48 < player.getPositionX() + player.screenX &&
                        slime.getPositionY() + 48 > player.getPositionY() - player.screenY &&
                        slime.getPositionY() - 48 < player.getPositionY() + player.screenY) {

                        // Tentukan Warna
                        if (slime.isAlive()) {
                            gc.setFill(Color.GREEN);
                        } else {
                            gc.setFill(Color.DARKRED);
                        }
                        
                        // 3. GAMBAR DI KOORDINAT SCREEN (bukan getPositionX lagi)
                        gc.fillRect(slimeScreenX, slimeScreenY, 30, 30); 
                        
                        // Gambar Nama & HP Slime (ikut posisi screen juga)
                        gc.setFill(Color.WHITE);
                        gc.fillText(slime.getName(), slimeScreenX, slimeScreenY - 20);
                        gc.fillText("HP: " + slime.getHealth(), slimeScreenX, slimeScreenY - 5);
                    }
                }
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


