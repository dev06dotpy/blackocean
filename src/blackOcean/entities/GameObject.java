package blackOcean.entities;

import blackOcean.core.Game;
import blackOcean.core.Planet;
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
    public int fuel;
    public int maxFuel;
    public int shields;
    public int maxShields;


    public GameObject(Vector2D position, Vector2D velocity, double radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.dead = false;
    }
    public void update() {
        position.addScaled(velocity, DT);
        if (Game.getCurrentMode() == Game.GameMode.SPACE) {
            if (position.x < radius) {
                position.x = radius;
                velocity.x = -velocity.x;
            } else if (position.x > WORLD_WIDTH - radius) {
                position.x = WORLD_WIDTH - radius;
                velocity.x = -velocity.x;
            }

            if (position.y < radius) {
                position.y = radius;
                velocity.y = -velocity.y;
            } else if (position.y > WORLD_HEIGHT - radius) {
                position.y = WORLD_HEIGHT - radius;
                velocity.y = -velocity.y;
            }
        } else if (Game.getCurrentMode() == Game.GameMode.PLANET) {
            Planet planet = Game.getCurrentPlanet();
            if (planet != null && planet.collidesWithWall(position, radius)) {
                position.addScaled(velocity, -DT);
                velocity = new Vector2D(0, 0);
            }
        }
        //position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
//        if(position.x < radius){
//            position.x = radius;
//            velocity.x = - velocity.x;
//        }else if(position.x > WORLD_WIDTH - radius){
//            position.x = WORLD_WIDTH - radius;
//            velocity.x = - velocity.x;
//        }
//
//        if(position.y < radius){
//            position.y = radius;
//            velocity.y = - velocity.y;
//        }else if(position.y > WORLD_HEIGHT - radius){
//            position.y = WORLD_HEIGHT - radius;
//            velocity.y = - velocity.y;
//        }
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

    public abstract void draw(Graphics2D g);

    public double distance(GameObject other) {
        return position.distWithWrap(other.position, FRAME_WIDTH, FRAME_HEIGHT);
    }

    public String toString() {
        return position.x + "," + position.y;
    }
}
