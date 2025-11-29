package projectakhirppbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    private Player player;

    // Key states
    private boolean leftPressed, rightPressed, upPressed, downPressed;

    public GamePanel() {

        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        player = new Player(100, 300);

        // Timer update
        Timer timer = new Timer(50, e -> {
            updateMovement();
            player.update();
            repaint();
        });

        timer.start();
        setFocusable(true);
        addKeyListener(new KeyHandler());
    }

    private void updateMovement() {

        player.stopMoving();

        if (leftPressed)  player.moveLeft();
        if (rightPressed) player.moveRight();
        if (upPressed)    player.moveUp();
        if (downPressed)  player.moveDown();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(player.getImage(), player.getX(), player.getY(), null);
    }

    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {

                // --- ARAH KIRI ---
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    leftPressed = true;
                    break;

                // --- ARAH KANAN ---
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    rightPressed = true;
                    break;

                // --- MAJU / NAIK ---
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                case KeyEvent.VK_PAGE_UP:
                    upPressed = true;
                    break;

                // --- TURUN ---
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                case KeyEvent.VK_PAGE_DOWN:
                    downPressed = true;
                    break;

                // --- LOMPAT ---
                case KeyEvent.VK_SPACE:
                    player.jump();
                    break;

                // --- SERANG ---
                case KeyEvent.VK_P:
                    player.attack();
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    leftPressed = false;
                    break;

                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    rightPressed = false;
                    break;

                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                case KeyEvent.VK_PAGE_UP:
                    upPressed = false;
                    break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                case KeyEvent.VK_PAGE_DOWN:
                    downPressed = false;
                    break;
            }
        }
    }
}
