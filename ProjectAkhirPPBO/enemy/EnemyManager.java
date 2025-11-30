/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enemy;

/**
 *
 * @author Hi Luluuu
 */
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class EnemyManager {

    private ArrayList<Enemy> enemies = new ArrayList<>();

    public void addEnemy(Enemy e) {
        enemies.add(e);
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void update(Player player, long globalTime) {

        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;

            double distance = getDistance(player, e);

            // Attack range
            if (distance < 45) {
                e.basicAttack(player);
            }
            // Chase range
            else if (distance < 250) {
                chase(player, e);
            }
        }
    }

    public void draw(GraphicsContext gc) {
        for (Enemy e : enemies) {
            if (!e.isAlive()) {
                gc.setFill(Color.DARKRED);
            } else {
                gc.setFill(Color.GREEN);
            }
            gc.fillRect(e.getPositionX(), e.getPositionY(), 30, 30);

            gc.setFill(Color.WHITE);
            gc.fillText(e.getName(), e.getPositionX(), e.getPositionY() - 15);
            gc.fillText("HP: " + e.getHealth(), e.getPositionX(), e.getPositionY() - 2);
        }
    }

    private void chase(Player player, Enemy enemy) {
        double dx = player.getPositionX() - enemy.getPositionX();
        double dy = player.getPositionY() - enemy.getPositionY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) return;

        // Normalisasi vektor
        double moveX = (dx / distance) * enemy.getMoveSpeed();
        double moveY = (dy / distance) * enemy.getMoveSpeed();

        enemy.setPosition(enemy.getPositionX() + moveX, enemy.getPositionY() + moveY);
    }

    private double getDistance(Entity a, Entity b) {
        double dx = a.getPositionX() - b.getPositionX();
        double dy = a.getPositionY() - b.getPositionY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
