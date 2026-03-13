package blackOcean.core;

import blackOcean.controllers.AimNShoot;
import blackOcean.controllers.RandomAction;
import blackOcean.controllers.Controller;
import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.Consumables.FuelTank;
import blackOcean.entities.Consumables.HealthPack;
import blackOcean.entities.Consumables.ShieldBattery;
import blackOcean.systems.CollisionSystem;
import blackOcean.entities.*;
import utilities.JEasyFrame;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static blackOcean.core.Constants.*;

public class Game {
    public static final int N_INITIAL_ASTEROIDS = 5;
    public List<GameObject> objects;
    public List<Ship> ships;
    PlayerShip playerShip;
    Keys ctrl;
    Controller controller;

    //private Planet planet;

    private static int score = 0;
    private static int lives = 50;  // should be about 5 but made large during testing of collision handling
    private static int level = 1;
    public static boolean gameOver = false;
    public enum GameMode{ SPACE, PLANET}
    private static GameMode mode = GameMode.SPACE;
    private static Planet planet;
    public Game() {
        objects = new ArrayList<GameObject>();
        ships = new ArrayList<Ship>();
        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
            objects.add(new Asteroid());

        }
        ctrl = new Keys(this);  //always create this (even when not used) to avoid
        // having to comment out adding of action listener
        controller = ctrl;
        // alternate controller options to replace above line
        // controller = new RandomAction();
        // controller = new RotateNShoot();
//        playerShip = new PlayerShip(controller);
//        objects.add(playerShip);
//        ships.add(playerShip);
//
//        mode = GameMode.SPACE;
//        currentMode = mode;
//        currentPlanet = null;
//
//        addSaucers();
//        addConsumables();

        spaceMode();
    }

    public void spaceMode(){
        objects.clear();
        ships.clear();

        planet = null;
        mode = GameMode.SPACE;

        for(int i = 0; i < N_INITIAL_ASTEROIDS + 2 * (level - 1); i++) objects.add(new Asteroid());

        playerShip = new PlayerShip(controller);
        objects.add(playerShip);
        ships.add(playerShip);

        addSaucers();
        addConsumables();
    }

    public void planetMode(){
        objects.clear();
        ships.clear();

        planet = new Planet();
        planet.generate();
        mode = GameMode.PLANET;

        playerShip = new PlayerShip(controller);
        playerShip.position = planet.spawnPosition();
        playerShip.velocity = new Vector2D(0, 0);
        objects.add(playerShip);
        ships.add(playerShip);

        addSaucers();
        addConsumables();
    }

    public void newLevel() {
        level++;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        synchronized (Game.class) {
            spaceMode();
        }

    }

    public void newLife() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        synchronized (Game.class) {
            if (mode == GameMode.PLANET) planetMode();
            else spaceMode();
        }
    }

    private void addSaucers() {
        for (int i = 0; i < 3; i++) {
            Controller ctrl = (i % 3 != 0 ? new RandomAction() : new AimNShoot(this));
            Color colorBody = (i % 3 != 0 ? Color.PINK : Color.GREEN);
            Random r = new Random();
            Vector2D pos;
            double spawnRadius = 12;

            if(mode == GameMode.PLANET && planet != null) pos = planet.randomCavePosition(spawnRadius);
            else{
                pos = new Vector2D(
                      r.nextInt(WORLD_WIDTH),
                      r.nextInt(WORLD_HEIGHT)
                );
            }
            Ship saucer = new Saucer(ctrl, colorBody, Color.white);
            saucer.position = pos;
            if (i % 3 == 0) {
                ((AimNShoot) ctrl).setShip(saucer);
                // move the saucer if it is too close to the player ship;
                // otherwise it will shoot ship immediately before player
                // has time to flee
                Vector2D posDiff = new Vector2D(saucer.position).subtract(playerShip.position);
                if (posDiff.mag() < AimNShoot.SHOOTING_DISTANCE) {
                    saucer.position = new Vector2D(playerShip.position).addScaled(posDiff.normalise(), AimNShoot.SHOOTING_DISTANCE * 0.75);
                }
            }
            objects.add(saucer);
            ships.add(saucer);
        }
    }

    private GameObject addConsumables() {

        Random random = new Random();
        int chance = random.nextInt(10);

        Vector2D pos;
        double spawnRadius = 12;

        if(mode == GameMode.PLANET && planet != null) pos = planet.randomCavePosition(spawnRadius);
        else{
            pos = new Vector2D(
              random.nextInt(WORLD_WIDTH),
              random.nextInt(WORLD_HEIGHT)
            );
        }
        if(chance < 4) return new HealthPack(pos);
        else if(chance < 8) return new FuelTank(pos);
        else return new ShieldBattery(pos);
    }

    public static void main(String[] args) {
        Game game = new Game();
        View view = new View(game);
        new JEasyFrame(view, "Game with AI").addKeyListener(game.ctrl);
        while (!gameOver) {
            game.update();
            view.repaint();
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
            }


        }
    }

    public void update() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject o1 = objects.get(i);
            if (o1.dead) continue;

            for (int j = i + 1; j < objects.size(); j++) {
                GameObject o2 = objects.get(j);
                if (o2.dead) continue;

                CollisionSystem.handle(o1, o2);
            }
        }
        List<GameObject> alive = new ArrayList<>();
        boolean noEnemies = true;
        boolean noPlayer = true;
        int consumableCount = 0;
        for (GameObject o : objects) {
            o.update();

            //applyWorldRules(o);

            if (o instanceof Asteroid) {
                Asteroid a = (Asteroid) o;
                if (!a.spawnedAsteroids.isEmpty()) {
                    alive.addAll(a.spawnedAsteroids);
                    a.spawnedAsteroids.clear();
                }
            }

            if (!o.dead) {
                if (o instanceof Asteroid || o instanceof Saucer) noEnemies = false;
                if (o instanceof PlayerShip) noPlayer = false;
                if (o instanceof Consumable) consumableCount++;
                alive.add(o);
            }
        }

        for (Ship s : ships) {
            if (s.bullet != null) {
                alive.add(s.bullet);
                s.bullet = null;
            }
        }

        while (consumableCount < 5) {
            alive.add(addConsumables());
            consumableCount++;
        }

        synchronized (Game.class) {
            objects.clear();
            objects.addAll(alive);
        }
        if (noEnemies) {
            newLevel();
        } else if (noPlayer) {
            newLife();
        }

    }

//    private void applyWorldRules(GameObject o) {
//        if (mode == GameMode.SPACE) {
//            if (o.position.x < o.radius) {
//                o.position.x = o.radius;
//                o.velocity.x = -o.velocity.x;
//            } else if (o.position.x > WORLD_WIDTH - o.radius) {
//                o.position.x = WORLD_WIDTH - o.radius;
//                o.velocity.x = -o.velocity.x;
//            }
//
//            if (o.position.y < o.radius) {
//                o.position.y = o.radius;
//                o.velocity.y = -o.velocity.y;
//            } else if (o.position.y > WORLD_HEIGHT - o.radius) {
//                o.position.y = WORLD_HEIGHT - o.radius;
//                o.velocity.y = -o.velocity.y;
//            }
//
//        } else if (mode == GameMode.PLANET) {
//            if (planet != null && planet.collidesWithWall(o.position, o.radius)) {
//                o.position.addScaled(o.velocity, -DT);
//                o.velocity = new Vector2D(0, 0);
//            }
//        }
//    }

    public static void incScore(int inc) {
        int oldScore = score;
        score += inc;
        System.out.println("Score " + score);
        if (score / 5000 > oldScore / 5000) {
            System.out.println("Adding life");
            lives++;
        }
    }

    public static void loseLife() {
        lives--;
        if (lives == 0)
            gameOver = true;
    }

    public void togglePlanetMode() {

        synchronized (Game.class){
            if (mode == GameMode.SPACE) planetMode();
            else spaceMode();
        }
    }


    public static int getScore() {
        return score;
    }

    public static int getLevel() {
        return level;
    }

    public static int getLives() {
        return lives;
    }

    public PlayerShip getPlayerShip(){return playerShip;}

    public static GameMode getCurrentMode(){return mode;}
    public static Planet getCurrentPlanet(){return planet;}
}

