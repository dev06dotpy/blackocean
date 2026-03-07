package blackOcean.entities;

import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.*;

public abstract class GameObject {
    public Vector2D position;
    public Vector2D velocity;
    public double radius;
    public boolean dead;
    public int health;
    public int maxHealth;


    public GameObject(Vector2D position, Vector2D velocity, double radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.dead = false;
    }
    public void update() {
        position.addScaled(velocity, DT);
        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void hit() {
        dead = true;
    }

    public void hit(int damage){
        health -= damage;
        if (health <= 0) dead = true;
    }

    public boolean canCollide(GameObject other) {
        return true;
    }

    public boolean overlap(GameObject other) {
        return this.position.distWithWrap(other.position, FRAME_WIDTH, FRAME_HEIGHT) < this.radius + other.radius;
    }

//    public void collisionHandling(GameObject other) {
//        if (this.getClass() != other.getClass() && this.overlap(other) && this.canCollide(other)) {
//            // System.out.println(this + " " + other);
//            this.hit();
//            other.hit();
//            if (this instanceof Asteroid && other instanceof Bullet) {
//                Bullet b = (Bullet) other;
//                if (b.firedByShip) Game.incScore(100);
//            }
//            if (this instanceof Bullet && other instanceof Asteroid) {
//                Bullet b = (Bullet) this;
//                if (b.firedByShip) Game.incScore(100);
//            }
//            if (this instanceof Saucer && other instanceof Bullet) {
//                Bullet b = (Bullet) other;
//                if (b.firedByShip) Game.incScore(500);
//            }
//            if (this instanceof Bullet && other instanceof Saucer) {
//                Bullet b = (Bullet) this;
//                if (b.firedByShip) Game.incScore(500);
//            }
//        }
//    }

    public abstract void draw(Graphics2D g);

    public double distance(GameObject other) {
        return position.distWithWrap(other.position, FRAME_WIDTH, FRAME_HEIGHT);
    }

    public String toString() {
        return position.x + "," + position.y;
    }
}
