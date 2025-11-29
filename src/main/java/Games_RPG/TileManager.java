/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Games_RPG;

/**
 *
 * @author yasaw
 */


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class TileManager {
    
    GameLoop gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GameLoop gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        System.out.println("--- MULAI LOAD TILEMANAGER ---");
        getTileImage();
        loadMap("/maps/map01.txt");
        System.out.println("--- SELESAI LOAD TILEMANAGER ---");
    }

    public void getTileImage() {
        try {
            // 0: RUMPUT
            tile[0] = new Tile();
            tile[0].image = new Image(getClass().getResourceAsStream("/assets/tiles/grass.png"));
            tile[0].collision = false;

            // 1: TEMBOK
            tile[1] = new Tile();
            tile[1].image = new Image(getClass().getResourceAsStream("/assets/tiles/wall.png"));
            tile[1].collision = true;

            // 2: AIR 
            tile[2] = new Tile();
            tile[2].image = new Image(getClass().getResourceAsStream("/assets/tiles/water.png"));
            tile[2].collision = true; // Gak bisa berenang

            // 3: POHON 
            tile[3] = new Tile();
            tile[3].image = new Image(getClass().getResourceAsStream("/assets/tiles/tree.png"));
            tile[3].collision = true; // Gak bisa nembus pohon

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            
            if (is == null) {
                System.out.println("❌ ERROR: File Map tidak ditemukan di: " + filePath);
                return; // Stop kalau map ga ada
            } else {
                System.out.println("✅ File Map ditemukan: " + filePath);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                
                if (line == null) break; // Safety check kalau file kependekan

                String numbers[] = line.trim().split("\\s+"); // Split pakai spasi (lebih aman)

                while (col < gp.maxWorldCol) {
                    if (col < numbers.length) {
                        int num = Integer.parseInt(numbers[col]);
                        mapTileNum[col][row] = num;
                    }
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
            System.out.println("✅ Map berhasil di-parsing ke array.");
            
        } catch (Exception e) {
            System.out.println("❌ EXCEPTION saat Load Map:");
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext gc) {
        int col = 0;
        int row = 0;

        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            int tileNum = mapTileNum[col][row];

            // 1. Hitung Posisi Asli di Dunia
            int worldX = col * gp.tileSize;
            int worldY = row * gp.tileSize;

            // 2. Hitung Posisi di Layar (Relatif terhadap Player)
            // Rumus: PosisiAsli - PosisiPlayer + PosisiTengahLayar
            // (Kita perlu casting ke int biar koordinat gambarnya pas)
           double screenX = worldX - gp.player.getPositionX() + gp.player.screenX;
            double screenY = worldY - gp.player.getPositionY() + gp.player.screenY;


            // 3. Cek Apakah Tile Masuk Layar (Optimasi/Culling)
            // Hanya gambar tile yang dekat dengan player, biar ga berat
            if (worldX + gp.tileSize > gp.player.getPositionX() - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.getPositionX() + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.getPositionY() - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.getPositionY() + gp.player.screenY) {

                if (tile[tileNum] != null && tile[tileNum].image != null) {
                    gc.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize);
                }
            }

            col++;
            
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    
    // (Method checkCollision tetap sama seperti sebelumnya, tidak perlu diubah)
    public boolean checkCollision(Entity entity, String direction) {
        int entityLeftWorldX = (int) entity.getPositionX() + 8;
        int entityRightWorldX = (int) entity.getPositionX() + 40;
        int entityTopWorldY = (int) entity.getPositionY() + 8;
        int entityBottomWorldY = (int) entity.getPositionY() + 40;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch(direction) {
            case "up":
                entityTopRow = (int) (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = mapTileNum[entityRightCol][entityTopRow];
                if(tile[tileNum1].collision == true || tile[tileNum2].collision == true) return true;
                break;
            case "down":
                entityBottomRow = (int) (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = mapTileNum[entityRightCol][entityBottomRow];
                if(tile[tileNum1].collision == true || tile[tileNum2].collision == true) return true;
                break;
            case "left":
                entityLeftCol = (int) (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = mapTileNum[entityLeftCol][entityBottomRow];
                if(tile[tileNum1].collision == true || tile[tileNum2].collision == true) return true;
                break;
            case "right":
                entityRightCol = (int) (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = mapTileNum[entityRightCol][entityBottomRow];
                if(tile[tileNum1].collision == true || tile[tileNum2].collision == true) return true;
                break;
        }
        return false;
    }
}