package blackOcean.entities;

import blackOcean.graphics.Sprite;
import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;

import static blackOcean.core.Constants.*;

public class Asteroid extends GameObject {
    public Sprite sprite;
    public double rotationPerFrame;
    public Vector2D direction;
    public int damage;

    public static final int RADIUS = 10;
    public static final double MAX_SPEED = 100;
    public boolean isLarge = true;
    public List<Asteroid> spawnedAsteroids = new ArrayList<Asteroid>();

    public Asteroid(Vector2D pos, double vx, double vy, boolean isLarge, Sprite spr) {
        super(pos, new Vector2D(vx, vy), spr.getRadius());
        this.isLarge = isLarge;
        double dir = Math.random() * 2 * Math.PI;
        direction = new Vector2D(Math.cos(dir), Math.sin(dir));
        position = new Vector2D(pos);
        sprite = new Sprite(spr.image, position, direction, spr.width, spr.height);
        if (!isLarge) {
            sprite.height *= 2/3.0;
            sprite.width *= 2/3.0;
        }
        radius = sprite.getRadius();
        rotationPerFrame = Math.random()  * 0.1;

    }

    public void draw(Graphics2D g) {
        sprite.draw(g);
        //g.setColor(Color.RED);
        //g.fillOval((int) (position.x - radius), (int) (position.y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public Asteroid() {
        super(new Vector2D(Math.random() * WORLD_WIDTH, Math.random() * WORLD_HEIGHT), new Vector2D(0, 0), 0);
        double vx = Math.random() * MAX_SPEED;
        if (Math.random() < 0.5) vx *= -1;
        double vy = Math.random() * MAX_SPEED;
        if (Math.random() < 0.5) vy *= -1;
        velocity.set(new Vector2D(vx, vy));
        double width = Math.min(Math.max(20+new Random().nextGaussian()*30, 30), 50);
        Image im = Sprite.ASTEROID1;
        double height = width * im.getHeight(null)/im.getWidth(null);
        double dir = Math.random() * 2 * Math.PI;
        direction = new Vector2D(Math.cos(dir), Math.sin(dir));
        sprite = new Sprite(im, position, direction, width, height);
        radius = sprite.getRadius();
        rotationPerFrame = Math.random()  * 0.1;
    }

    private void spawn() {
        for (int i = 0; i < 2; i++) {
            double vx = Math.random() * MAX_SPEED;
            if (Math.random() < 0.5) vx *= -1;
            double vy = Math.random() * MAX_SPEED;
            if (Math.random() < 0.5) vy *= -1;
            spawnedAsteroids.add(new Asteroid(new Vector2D(position.x, position.y), vx, vy, false, sprite));
        }

    }

    @Override
    public void hit() {
        super.hit();

        if (isLarge) {
            SoundManager.play(SoundManager.bangMedium);
            spawn();
        } else {
            SoundManager.play(SoundManager.bangSmall);
        }
    }

    @Override
    public boolean canCollide(GameObject other) {
        return !(other instanceof Saucer);
    }
    public String toString() {
        return "Asteroid: " + super.toString();
    }

    public void update() {
        super.update();
        direction.rotate(rotationPerFrame);

    }
}
