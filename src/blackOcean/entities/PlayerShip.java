package blackOcean.entities;

import blackOcean.controllers.Controller;
import blackOcean.core.Game;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static blackOcean.core.Constants.*;

public class PlayerShip extends Ship {

    private int fuelDrain = 0;

    public PlayerShip(Controller ctrl) {
        super(new Vector2D(FRAME_WIDTH / 2, FRAME_HEIGHT / 2), new Vector2D(0, -1), 10);
        this.ctrl = ctrl;
        direction = new Vector2D(0,-1);
        thrusting = false;
        bullet = null;
        color = Color.CYAN;
        maxHealth = 100;
        health = 100;
        fuel = 100;
        maxFuel = 100;
        shields = 100;
        maxShields = 100;
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

        if(shields > 0){
            if (shields >= damage) shields -= damage/2;
            else{
                int remainingDamage = damage - shields;
                shields = 0;
                super.hit(remainingDamage);
            }
        }
        else super.hit(damage);
        System.out.println("Health after = " + health + ", dead = " + dead);
        if (dead){
            Game.loseLife();
            //SoundManager.play(bangLarge);
            System.out.println("Ship hit");
        }
        // to avoid ship loss while testing
    }

    @Override
    public void update() {
        super.update();

        fuelDrain++;

        // drain 1 fuel every 20 ticks
        if (fuelDrain >= 5) {
            fuel = Math.max(0, fuel - 1);
            fuelDrain = 0;
        }

        if (fuel == 0) {
            dead = true;
            Game.loseLife();
        }
    }

    //health
    public int getHealth(){return health;}
    public int getMaxHealth(){return maxHealth;}
    public void addHealth(int amount) {health = Math.min(maxHealth, health + amount);}

    //fuel
    public int getFuel() {return fuel;}
    public int getMaxFuel() { return maxFuel;}
    public void addFuel(int amount) {fuel = Math.min(maxFuel, fuel + amount);}

    //shields
    public int getShields() {return shields;}
    public int getMaxShields() {return maxShields;}
    public void addShields(int amount) {shields = Math.min(maxShields, shields + amount);}

    public String toString() {
        return "Ship: " + super.toString();
    }





}
