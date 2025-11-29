/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
/**
 *
 * @author mayza
 */
public class GamePanel extends JPanel implements Runnable{
     // PENGATURAN LAYAR
    final int originalTileSize = 16; //Ukuran ubin sebenarnya komponen pixel dalam game: 16x16 pixel 
    final int scale = 3; //Skala
    
    public final int tileSize = originalTileSize * scale; //Ukuran 48x48 yang akan ditampilkan di monitor
    
    //Mengatur ukuran yang akan ditampilkan di monitor
    final int maxScreenCol = 16; //Ukuran maksimal secara Horizontal
    final int maxScreenRow = 12; //Ukuran maksimal secara Vertikal
    final int screenWidth = tileSize * maxScreenCol; //Lebar: 768 pixels
    final int screenHeight = tileSize * maxScreenRow; //Tinggi: 576 pixels
    
    Thread gameThread;
    
    //Konstruktor GamePanel
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black); //Mengatur warna background
        this.setDoubleBuffered(true); //Mengatur agar rendering game lebih baik
        
    }
    
    public void startGameThread() { //Membuat instansiasi GameThread
        gameThread = new Thread(this); //Meneruskan "this" ke konstruktor Thread, berarti "this" berarti class ini (GamePanel)
        gameThread.start();
    }

    @Override //Ketika menjalankan gameThread, method ini otomatis langsung tercipta
    public void run() {
       
    }
    
    //Method update
    public void update() {
        player.update;
    }
    
    //Method paintComponent
    public void paintComponen(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        player.draw(g2);
        g2.dispose();
    }
}
