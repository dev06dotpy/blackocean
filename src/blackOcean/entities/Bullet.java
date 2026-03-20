package blackOcean.entities;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;


import static blackOcean.core.Constants.DT;
import static blackOcean.core.Constants.BULLET;

public class Bullet extends GameObject {
    private double lifetime;
    public static final int RADIUS = 2;
    public static final int BULLET_LIFE = 1; // seconds
    public boolean firedByShip;
    public int damage = 10;

    public Bullet(Vector2D pos, Vector2D vel, boolean firedByShip) {
        super(pos, vel, RADIUS);
        this.lifetime = BULLET_LIFE;
        this.firedByShip = firedByShip;
    }

    @Override
    public void update() {
        super.update();
        lifetime -= DT;
        if (lifetime <= 0) dead = true;
    }

    @Override
    public void draw(Graphics2D g) {
        if (BULLET != null) {
            int drawSize = 20;
            AffineTransform t0 = g.getTransform();
            g.translate(position.x, position.y);
            g.rotate(velocity.angle());
            g.drawImage(BULLET, -drawSize / 2, -drawSize / 2, drawSize, drawSize, null);
            g.setTransform(t0);
            return;
        }
        g.setColor(Color.WHITE);
        g.fillOval((int) position.x - RADIUS, (int) position.y - RADIUS, 2 * RADIUS, 2 * RADIUS);

    }

    public String toString() {
        return "Bullet; " + super.toString();
    }

    public void setLifetime(double lifetime) {
        this.lifetime = Math.max(0.1, lifetime);
    }
}
