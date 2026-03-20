package blackOcean.core;

import blackOcean.entities.Consumables.Artifact;
import blackOcean.entities.GameObject;
import blackOcean.entities.PlayerShip;
import blackOcean.entities.Saucer;
import blackOcean.entities.Ship;
import blackOcean.entities.SpacePlanet;
import blackOcean.entities.Bullet;
import utilities.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import static blackOcean.core.Constants.*;

public class View extends JComponent {
    public static final Color BG_COLOR = Color.BLACK;
    private Game game;
    Image im;
    AffineTransform bgTransf;

    public View(Game game) {
        this.game = game;
        this.im = Constants.SPACE;

        System.out.println("Constants.SPACE = " + Constants.SPACE);
        System.out.println("this.im = " + this.im);

        bgTransf = new AffineTransform();

        if (im != null) {
            double imWidth = im.getWidth(null);
            double imHeight = im.getHeight(null);
            double stretchx = imWidth > FRAME_WIDTH ? FRAME_WIDTH / imWidth : 1;
            double stretchy = imHeight > FRAME_HEIGHT ? FRAME_HEIGHT / imHeight : 1;
            bgTransf.scale(stretchx, stretchy);
        }
    }
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        AffineTransform old = g.getTransform();

        int cameraX = (int) game.getPlayerShip().position.x - FRAME_WIDTH / 2;
        int cameraY = (int) game.getPlayerShip().position.y - FRAME_HEIGHT / 2;
        int boundsWidth = (Game.getCurrentMode() == Game.GameMode.PLANET) ? PLANET_PIXEL_WIDTH : WORLD_WIDTH;
        int boundsHeight = (Game.getCurrentMode() == Game.GameMode.PLANET) ? PLANET_PIXEL_HEIGHT : WORLD_HEIGHT;
        cameraX = Math.max(0, Math.min(cameraX, boundsWidth - FRAME_WIDTH));
        cameraY = Math.max(0, Math.min(cameraY, boundsHeight - FRAME_HEIGHT));
        g.translate(-cameraX, -cameraY);

        if (Game.getCurrentMode() == Game.GameMode.PLANET && Game.getCurrentPlanet() != null)
            Game.getCurrentPlanet().draw(g);
        else if (Game.getCurrentMode() == Game.GameMode.SPACE) {
            if (im != null) {
                g.drawImage(im, 0, 0, WORLD_WIDTH, WORLD_HEIGHT, null);
            } else {
                g.setColor(BG_COLOR);
                g.fillRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            }

            g.setColor(Color.WHITE);
            g.drawRect(0, 0, WORLD_WIDTH - 1, WORLD_HEIGHT - 1);
        }
        synchronized (Game.class) {
            for (GameObject object : game.objects)
                object.draw(g);
        }

        g.setTransform(old);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("dialog", Font.BOLD, 20));
        g.drawString("Lives: " + Game.getLives(), 20, 30);
        g.drawString("Artifacts: " + Game.getArtifacts() + "/3", 20, 140);
        g.drawString("Damage = " + game.getPlayerShip().getBulletDamage(), 20, 165);
        g.drawString("Range = " + game.getPlayerShip().getBulletLifetime(), 20, 190);
        drawBar(g, 20, 40, 140, 15,
              game.getPlayerShip().getHealth(), game.getPlayerShip().getMaxHealth(),
              Color.GREEN, HEALTH_ICON, "Health: ");
        drawBar(g, 20, 90, 140, 15,
              game.getPlayerShip().getFuel(), game.getPlayerShip().getMaxFuel(),
              Color.YELLOW, FUEL_ICON, "Fuel: ");
        drawBar(g, 20, 65, 140, 15,
              game.getPlayerShip().getShields(), game.getPlayerShip().getMaxShields(),
              Color.BLUE, SHIELD_ICON, "Shields: ");
        drawActivePerks(g);

        drawMinimap(g);

        if (Game.hasPlayerWon()) {
            g.drawString("YOU WIN! Artifacts Collected: " + Game.getArtifacts(), FRAME_WIDTH / 2 - 170, FRAME_HEIGHT / 2 - 20);
        } else if (Game.getLives() == 0)
            g.drawString("GAME OVER Score " + Game.getScore(), FRAME_WIDTH / 2 - 100, FRAME_HEIGHT / 2 - 20);
    }

    public Dimension getPreferredSize() {
        return Constants.FRAME_SIZE;
    }

    private void drawActivePerks(Graphics2D g) {
        PlayerShip ship = game.getPlayerShip();
        Vector<String> activePerks = new Vector<>();

        if (ship.getFuelDrainRate() > 20) activePerks.add("FuelEfficiency");
        if (ship.getMaxHealth() > 100) activePerks.add("ReinforcedHull");
        if (Ship.MAG_ACC > 200) activePerks.add("SonicSpeed");
        if (ship.getBulletDamage() > 10) activePerks.add("FMJRounds");
        if (ship.getBulletLifetime() > Bullet.BULLET_LIFE) activePerks.add("RailGun");

        if (activePerks.isEmpty()) return;

        g.setFont(new Font("dialog", Font.BOLD, 16));
        g.setColor(Color.YELLOW);

        int x = 20;
        int y = FRAME_HEIGHT - 10 - ((activePerks.size() - 1) * 18);
        for (String perk : activePerks) {
            g.drawString(perk, x, y);
            y += 18;
        }
    }


    public void drawBar(Graphics2D g,
                        int x, int y,
                        int width, int height,
                        int current, int max,
                        Color color, Image icon, String stat) {

        int iconSize = 16;
        int iconGap = 6;
        int barX = x;

        if (icon != null) {
            int iconY = y + (height - iconSize) / 2;
            int iconX = x + width + iconGap;
            g.setColor(new Color(230, 230, 230));
            g.fillRoundRect(iconX - 1, iconY - 1, iconSize + 2, iconSize + 2, 4, 4);
            g.drawImage(icon, iconX, iconY, iconSize, iconSize, null);
        }

        int filledWidth = (int) (current / (double) max * width);

        //background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, y, width, height);

        //color
        g.setColor(color);
        g.fillRect(barX, y, filledWidth, height);

        //border
        g.setColor(Color.WHITE);
        g.drawRect(barX, y, width, height);
    }

    public void drawMinimap(Graphics2D g) {
        int mapX = FRAME_WIDTH - 150;
        int mapY = 10;
        int mapWidth = 140;
        int mapHeight = 140;

        g.setColor(Color.BLACK);
        g.fillRect(mapX, mapY, mapWidth, mapHeight);

        g.setColor(Color.WHITE);
        g.drawRect(mapX, mapY, mapWidth, mapHeight);

        if (Game.getCurrentMode() == Game.GameMode.SPACE) {
            g.setColor(Color.CYAN);
            for (Vector2D p : game.getPlanetPositions()) {
                int px = mapX + (int) (p.x / WORLD_WIDTH * mapWidth);
                int py = mapY + (int) (p.y / WORLD_HEIGHT * mapHeight);
                g.fillOval(px - 3, py - 3, 6, 6);
            }

            Vector2D playerPos = game.getPlayerShip().position;
            int playerX = mapX + (int) (playerPos.x / WORLD_WIDTH * mapWidth);
            int playerY = mapY + (int) (playerPos.y / WORLD_HEIGHT * mapHeight);

            g.setColor(Color.RED);
            g.fillOval(playerX - 3, playerY - 3, 8, 8);
        } else if (Game.getCurrentMode() == Game.GameMode.PLANET && Game.getCurrentPlanet() != null) {
            boolean[][] walls = Game.getCurrentPlanet().getWalls();
            double scaleX = mapWidth / (double) PLANET_WIDTH;
            double scaleY = mapHeight / (double) PLANET_HEIGHT;

            g.setColor(Color.DARK_GRAY);
            for (int y = 0; y < PLANET_HEIGHT; y++) {
                for (int x = 0; x < PLANET_WIDTH; x++) {
                    if (walls[y][x]) {
                        int rx = mapX + (int) Math.floor(x * scaleX);
                        int ry = mapY + (int) Math.floor(y * scaleY);
                        int rw = Math.max(1, (int) Math.ceil(scaleX));
                        int rh = Math.max(1, (int) Math.ceil(scaleY));
                        g.fillRect(rx, ry, rw, rh);
                    }
                }
            }

            for (GameObject obj : game.objects) {
                int dotX = mapX + (int) ((obj.position.x / (double) PLANET_PIXEL_WIDTH) * mapWidth);
                int dotY = mapY + (int) ((obj.position.y / (double) PLANET_PIXEL_HEIGHT) * mapHeight);

                if (obj instanceof PlayerShip) {
                    g.setColor(Color.CYAN);
                    g.fillOval(dotX - 3, dotY - 3, 6, 6);
                } else if (obj instanceof Artifact) {
                    g.setColor(Color.MAGENTA);
                    g.fillOval(dotX - 3, dotY - 3, 6, 6);
                } else if (obj instanceof SpacePlanet) {
                    g.setColor(Color.GREEN);
                    g.fillOval(dotX - 4, dotY - 4, 8, 8);
                } else if (obj instanceof Saucer) {
                    g.setColor(Color.RED);
                    g.fillOval(dotX - 2, dotY - 2, 4, 4);
                }

            }
        }

    }
}
