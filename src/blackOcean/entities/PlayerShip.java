package blackOcean.entities;

import blackOcean.controllers.Controller;
import blackOcean.core.Game;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static blackOcean.core.Constants.*;

public class PlayerShip extends Ship {

    private int fuelDrain = 0;
    private int fuelDrainRate = 20;
    private int bulletDamage = 10;
    private double bulletLifetime = Bullet.BULLET_LIFE;

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
        if (PLAYERSHIP != null) {
            AffineTransform at = g.getTransform();
            g.translate(position.x, position.y);
            g.rotate(direction.angle() + Math.PI / 2);
            int size = (int) (2 * radius * DRAWING_SCALE);
            g.drawImage(PLAYERSHIP, -size / 2, -size / 2, size, size, null);
            g.setTransform(at);
            return;
        }

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
            System.out.println("Ship hit");
        }
    }

    @Override
    public void update() {
        super.update();

        fuelDrain++;

        // drain 1 fuel every N ticks
        if (fuelDrain >= fuelDrainRate) {
            fuel = Math.max(0, fuel - 1);
            fuelDrain = 0;
        }

        if (fuel == 0) {
            dead = true;
            Game.loseLife();
        }
    }

    @Override
    protected void mkBullet() {
        super.mkBullet();
        if (bullet != null) {
            bullet.damage = bulletDamage;
            bullet.setLifetime(bulletLifetime);
        }
    }

    // Carry persistent upgrades when recreating the ship between modes/lives.

    public void copyUpgradesFrom(PlayerShip other) {
        if (other == null) return;

        maxHealth = other.maxHealth;
        maxFuel = other.maxFuel;
        maxShields = other.maxShields;
        fuelDrainRate = other.fuelDrainRate;
        bulletDamage = other.bulletDamage;
        bulletLifetime = other.bulletLifetime;
        health = maxHealth;
        fuel = maxFuel;
        shields = maxShields;
    }

    public int getHealth(){return health;}
    public int getMaxHealth(){return maxHealth;}
    public void addHealth(int amount) {health = Math.min(maxHealth, health + amount);}
    public void addMaxHealth(int amount) {maxHealth = Math.max(1, maxHealth + amount);}

    public int getFuel() {return fuel;}
    public int getMaxFuel() { return maxFuel;}
    public void addFuel(int amount) {fuel = Math.min(maxFuel, fuel + amount);}
    public int getFuelDrainRate() {return fuelDrainRate;}
    public void setFuelDrainRate(int rate) {fuelDrainRate = Math.max(1, rate);}

    public int getShields() {return shields;}
    public int getMaxShields() {return maxShields;}
    public void addShields(int amount) {shields = Math.min(maxShields, shields + amount);}

    public int getBulletDamage() {return bulletDamage;}
    public void addBulletDamage(int amount) {bulletDamage = Math.max(1, bulletDamage + amount);}
    public double getBulletLifetime() {return bulletLifetime;}
    public void addBulletLifetime(double amount) {bulletLifetime = Math.max(0.1, bulletLifetime + amount);}



    public String toString() {
        return "Ship: " + super.toString();
    }





}
