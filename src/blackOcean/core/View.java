package blackOcean.core;

import blackOcean.entities.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

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
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        //g.drawImage(im, bgTransf, null);
        g.setColor(BG_COLOR);
        g.fillRect(0,0,getWidth(), getHeight());

        AffineTransform old = g.getTransform();

        int cameraX = (int) game.getPlayerShip().position.x - FRAME_WIDTH / 2;
        int cameraY = (int) game.getPlayerShip().position.y - FRAME_HEIGHT / 2;

        g.translate(-cameraX, -cameraY);

        g.setColor(Color.WHITE);
        g.drawRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        game.getPlanet().draw(g);
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

}
