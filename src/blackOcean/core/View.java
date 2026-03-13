package blackOcean.core;

import blackOcean.entities.GameObject;
import utilities.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import static blackOcean.core.Constants.*;

public class View extends JComponent {
    public static final Color BG_COLOR = Color.BLACK;
    private Game game;
    Image im = Constants.MILKYWAY2;
    AffineTransform bgTransf;

    public View(Game game){
        this.game = game;
        double imWidth = im.getWidth(null);
        double imHeight = im.getHeight(null);
        double stretchx = imWidth > FRAME_WIDTH ? FRAME_WIDTH / imWidth : 1;
        double stretchy = imHeight > FRAME_HEIGHT ? FRAME_HEIGHT / imHeight : 1;
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchx, stretchy);

    }
    //TODO: add artifact counter
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        AffineTransform old = g.getTransform();

        int cameraX = (int) game.getPlayerShip().position.x - FRAME_WIDTH / 2;
        int cameraY = (int) game.getPlayerShip().position.y - FRAME_HEIGHT / 2;
        g.translate(-cameraX, -cameraY);

        if(Game.getCurrentMode() == Game.GameMode.PLANET && Game.getCurrentPlanet() != null ) Game.getCurrentPlanet().draw(g);
        else if (Game.getCurrentMode() == Game.GameMode.SPACE){
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, WORLD_WIDTH - 1, WORLD_HEIGHT - 1);

            g.setColor(BG_COLOR);
            g.fillRect(0,0,WORLD_WIDTH, WORLD_HEIGHT);
        }
        synchronized (Game.class) {
            for (GameObject object : game.objects)
                object.draw(g);
        }

        g.setTransform(old);

        g.setColor(Color.YELLOW);g.setFont(new Font("dialog", Font.BOLD, 20));
        g.drawString("Level: "+Game.getLevel(), 20, FRAME_HEIGHT-20);
        g.drawString("Score: "+Game.getScore(), FRAME_WIDTH/3+20, FRAME_HEIGHT-20);
        g.drawString("Lives: "+Game.getLives(), 2*FRAME_WIDTH/3+20, FRAME_HEIGHT-20);
        drawBar(g, 20, FRAME_HEIGHT - 70, 200, 20,
              game.getPlayerShip().getHealth(), game.getPlayerShip().getMaxHealth(),
              Color.GREEN, "Health: ");
        drawBar(g, FRAME_WIDTH - 220, FRAME_HEIGHT - 70, 200, 20,
              game.getPlayerShip().getFuel(), game.getPlayerShip().getMaxFuel(),
              Color.YELLOW, "Fuel: ");
        drawBar(g, 20, FRAME_HEIGHT - 130, 200, 20,
              game.getPlayerShip().getShields(), game.getPlayerShip().getMaxShields(),
              Color.BLUE, "Shields: ");

        drawMinimap(g);


        if (Game.getLives()==0)
            g.drawString("GAME OVER Score "+Game.getScore(), FRAME_WIDTH/2-100, FRAME_HEIGHT/2-20);
    }

    public Dimension getPreferredSize(){
        return Constants.FRAME_SIZE;
    }


    public void drawBar(Graphics2D g,
                        int x, int y,
                        int width, int height,
                        int current, int max,
                        Color color, String stat){

        int filledWidth = (int) (current / (double) max * width);

        //background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, width, height);

        //color
        g.setColor(color);
        g.fillRect(x, y, filledWidth, height);

        //border
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);

        //label
        g.setColor(Color.YELLOW);
        g.drawString(stat + current + "/" + max, x, y -5);
    }

    public void drawMinimap(Graphics2D g){
        int mapX = FRAME_WIDTH - 150;
        int mapY = 10;
        int mapWidth = 140;
        int mapHeight = 140;

        g.setColor(Color.BLACK);
        g.fillRect(mapX, mapY, mapWidth, mapHeight);

        g.setColor(Color.WHITE);
        g.drawRect(mapX, mapY, mapWidth, mapHeight);

        if(Game.getCurrentMode() == Game.GameMode.SPACE){
            g.setColor(Color.CYAN);
            for(Vector2D p: game.getPlanetPositions()){
                int px = mapX +(int)(p.x / WORLD_WIDTH * mapWidth);
                int py = mapY + (int)(p.y / WORLD_HEIGHT * mapHeight);
                g.fillOval(px - 3, py - 3, 6, 6);
            }

            Vector2D playerPos = game.getPlayerShip().position;
            int playerX = mapX + (int)(playerPos.x / WORLD_WIDTH * mapWidth);
            int playerY = mapY + (int)(playerPos.y / WORLD_HEIGHT * mapHeight);

            g.setColor(Color.RED);
            g.fillOval(playerX - 3, playerY - 3, 8, 8);
        }
        else if(Game.getCurrentMode() == Game.GameMode.PLANET && Game.getCurrentPlanet() != null) {
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

            Vector2D playerPos = game.getPlayerShip().position;
            int playerX = mapX + (int) (playerPos.x / PLANET_PIXEL_WIDTH * mapWidth);
            int playerY = mapY + (int) (playerPos.y / PLANET_PIXEL_HEIGHT * mapHeight);

            g.setColor(Color.RED);
            g.fillOval(playerX - 3, playerY - 3, 8, 8);

        }
    }

}
