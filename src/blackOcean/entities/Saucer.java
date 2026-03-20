package blackOcean.entities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import static blackOcean.core.Constants.*;

import blackOcean.controllers.Controller;
import utilities.Vector2D;

public class Saucer extends Ship {
    public static final int HEIGHT = 12;
    public static final int WIDTH = 24;
    public static final int WIDTH_ELLIPSE = 20;
    private static final int IMAGE_DRAW_WIDTH = 2 * WIDTH;
    private static final int IMAGE_DRAW_HEIGHT = 2 * HEIGHT;
    public double damage;
    private double maxDistance = 120;
    private double minDistance = 60;
    private boolean planetDefender = false;
    public Color colorBelt;
    private SpacePlanet homePlanet;
    private final Image saucerImage;

    public Saucer(Controller ctrl, Color colorBody, Color colorBelt){
        this(ctrl, colorBody, colorBelt, 80);
    }

    public Saucer(Controller ctrl, Color colorBody, Color colorBelt, int maxHealth){
        super(new Vector2D(WORLD_WIDTH*Math.random(), WORLD_HEIGHT*Math.random()), new Vector2D(0, -1), 10);
        this.ctrl = ctrl;
        direction = new Vector2D(0,-1);
        thrusting = false;
        bullet = null;
        color = colorBody;
        this.colorBelt = colorBelt;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.saucerImage = Color.GREEN.equals(colorBody) ? SAUCER2 : SAUCER1;
        this.radius = Math.max(IMAGE_DRAW_WIDTH, IMAGE_DRAW_HEIGHT) / 2.0;
    }

    public void setHomePlanet(SpacePlanet planet){
        this.homePlanet = planet;
        this.planetDefender = true;
        this.minDistance = SpacePlanet.RADIUS + this.radius + 20;
        this.maxDistance = this.minDistance + 80;
    }

    private void keepNearPlanet(){
        if(!planetDefender || homePlanet == null) return;

        Vector2D toPlanet = new Vector2D(homePlanet.position).subtract(position);
        double dist = toPlanet.mag();

        if (dist > maxDistance){
            Vector2D pull = toPlanet.normalise().mult(0.5);
            velocity.add(pull);

            double speed = velocity.mag();
            double maxSpeed = 4;

            if(speed > maxSpeed) velocity = velocity.normalise().mult(maxSpeed);
        } else if (dist < minDistance) {
            Vector2D push = new Vector2D(position).subtract(homePlanet.position).normalise().mult(0.6);
            velocity.add(push);
        }
    }

    public SpacePlanet getHomePlanet(SpacePlanet planet){return homePlanet;}
    public boolean isPlanetDefender(){return planetDefender;}

    public void draw(Graphics2D g) {
        if (saucerImage != null) {
            AffineTransform at = g.getTransform();
            g.translate(position.x, position.y);
            g.rotate(direction.angle() + Math.PI / 2);
            g.drawImage(
                    saucerImage,
                    -IMAGE_DRAW_WIDTH / 2,
                    -IMAGE_DRAW_HEIGHT / 2,
                    IMAGE_DRAW_WIDTH,
                    IMAGE_DRAW_HEIGHT,
                    null
            );
            g.setTransform(at);
            return;
        }

        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        Ellipse2D ellipse = new Ellipse2D.Double(-WIDTH_ELLIPSE / 2.0,
                -HEIGHT / 2.0, WIDTH_ELLIPSE, HEIGHT);
        g.setColor(color);
        g.fill(ellipse);
        g.setColor(colorBelt);
        g.drawLine(-WIDTH / 2, 0, WIDTH / 2, 0);
        g.setTransform(at);
    }

    @Override
    public boolean canCollide(GameObject other) {
        return !(other instanceof Asteroid);
    }

    public void hit() {
        super.hit(10);
        //SoundManager.play(bangMedium);
    }

    @Override
    public void update(){
        super.update();
        keepNearPlanet();
    }
}
