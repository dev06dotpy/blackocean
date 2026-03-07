package blackOcean.entities;

import blackOcean.controllers.Controller;
import blackOcean.core.Game;
import utilities.SoundManager;
import utilities.Vector2D;
import static utilities.SoundManager.bangLarge;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static blackOcean.core.Constants.*;

public class PlayerShip extends Ship {

    public PlayerShip(Controller ctrl) {
        super(new Vector2D(FRAME_WIDTH / 2, FRAME_HEIGHT / 2), new Vector2D(0, -1), 10);
        this.ctrl = ctrl;
        direction = new Vector2D(0,-1);
        thrusting = false;
        bullet = null;
        color = Color.CYAN;
        maxHealth = 100;
        health = 100;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);
        g.setColor(color);
        g.fillPolygon(XP, YP, XP.length);
        if (thrusting) {
            g.setColor(Color.red);
            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
        }
        g.setTransform(at);
    }

    @Override
    public void hit(int damage) {
        System.out.println("Player took " + damage + " damage. Health before = " + health);
        super.hit(damage);
        System.out.println("Health after = " + health + ", dead = " + dead);
        if (dead){
            Game.loseLife();
            //SoundManager.play(bangLarge);
            System.out.println("Ship hit");
        }
        // to avoid ship loss while testing
    }

    public int getHealth(){return health;}

    public int getMaxHealth(){return maxHealth;}

    public void heal(int amount) {health = Math.min(maxHealth, health + amount);}

    public String toString() {
        return "Ship: " + super.toString();
    }
}
