package blackOcean.core;

import blackOcean.controllers.AimNShoot;
import blackOcean.controllers.RandomAction;
import blackOcean.controllers.Controller;
import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.Consumables.Perks.FuelEfficiency;
import blackOcean.entities.Consumables.Perks.ReinforcedHull;
import blackOcean.entities.Consumables.Perks.SonicSpeed;
import blackOcean.entities.Consumables.WeaponMods.FMJRounds;
import blackOcean.entities.Consumables.WeaponMods.RailGun;
import blackOcean.entities.Consumables.Resources.FuelTank;
import blackOcean.entities.Consumables.Resources.HealthPack;
import blackOcean.entities.Consumables.Resources.ShieldBattery;
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
    private static int score = 0;
    private static int lives = 5;
    private static int level = 1;
    private static int artifacts = 0;
    private static int artifactsNeeded = 3;
    public static boolean winState = false;
    public static boolean gameOver = false;
    public enum GameMode{ SPACE, PLANET}
    private static GameMode mode = GameMode.SPACE;
    private static Planet planet;
    private static boolean returnToSpace;
    private List<SpacePlanet> spacePlanets;

    public Game() {
        objects = new ArrayList<GameObject>();
        ships = new ArrayList<Ship>();
        spacePlanets = new ArrayList<>();
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
        PlayerShip previousShip = playerShip;
        objects.clear();
        ships.clear();
        spacePlanets.clear();

        planet = null;
        mode = GameMode.SPACE;

        for(int i = 0; i < N_INITIAL_ASTEROIDS + 2 * (level - 1); i++) objects.add(new Asteroid());

        playerShip = new PlayerShip(controller);
        playerShip.copyUpgradesFrom(previousShip);
        objects.add(playerShip);
        ships.add(playerShip);

        addSpacePlanet();
        objects.add(addConsumables());
    }

    public void planetMode(){
        PlayerShip previousShip = playerShip;
        objects.clear();
        ships.clear();

        planet = new Planet();
        planet.generate();
        mode = GameMode.PLANET;

        playerShip = new PlayerShip(controller);
        playerShip.copyUpgradesFrom(previousShip);
        playerShip.position = planet.spawnPosition();
        playerShip.velocity = new Vector2D(0, 0);
        objects.add(playerShip);
        ships.add(playerShip);

        addSaucers();
        objects.add(addConsumables());
        addPlanetPerks();
        objects.add(addArtifact());
    }

    // TODO: Make each level harder and add every time you collect an artifact level++. Level++ also increases enemy ships health
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
        int saucerCount = getSaucerCount();
        int saucerMaxHealth = getSaucerMaxHealth();
        for (int i = 0; i < saucerCount; i++) {
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
            Ship saucer = new Saucer(ctrl, colorBody, Color.white, saucerMaxHealth);
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

        Vector2D pos = randomConsumablePosition();
        if(chance < 4) return new HealthPack(pos);
        else if(chance < 8) return new FuelTank(pos);
        else return new ShieldBattery(pos);
    }

    private Vector2D randomConsumablePosition() {
        Random random = new Random();
        double spawnRadius = 12;

        if(mode == GameMode.PLANET && planet != null) {
            return planet.randomCavePosition(spawnRadius);
        }

        return new Vector2D(
                random.nextInt(WORLD_WIDTH),
                random.nextInt(WORLD_HEIGHT)
        );
    }

    private void addPlanetPerks() {
        if (mode != GameMode.PLANET) return;
        objects.add(new FuelEfficiency(randomConsumablePosition()));
        objects.add(new ReinforcedHull(randomConsumablePosition()));
        objects.add(new SonicSpeed(randomConsumablePosition()));
        objects.add(new FMJRounds(randomConsumablePosition()));
        objects.add(new RailGun(randomConsumablePosition()));
    }

    public static void main(String[] args) {
        Game game = new Game();
        View view = new View(game);
        new JEasyFrame(view, "blackocean").addKeyListener(game.ctrl);
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

          if(mode == GameMode.SPACE && ctrl.action().interact) {
                SpacePlanet targetPlanet = getNearbyPlanet();

                if(targetPlanet != null) {
                      ctrl.action().interact = false;
                      planetMode();
                      return;
                }
          }

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
        if (noPlayer) {
            newLife();
        }

        if(returnToSpace && mode == GameMode.PLANET){
            returnToSpace = false;
            spaceMode();
        }

    }

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

    private void addSpacePlanet(){
          Vector2D planetPos = new Vector2D(WORLD_WIDTH * 0.75, WORLD_HEIGHT * 0.5);
          SpacePlanet spacePlanet = new SpacePlanet(planetPos);

          spacePlanets.add(spacePlanet);
          objects.add(spacePlanet);
          addPlanetDefenders(spacePlanet);
    }

    private void addPlanetDefenders(SpacePlanet planet){
          int defenderCount = getSaucerCount();
          int saucerMaxHealth = getSaucerMaxHealth();
          for(int i = 0; i < defenderCount; i++){
                Controller ctrl = (i == 0 ? new AimNShoot(this) : new RandomAction());
                Color colourBody = (i == 0 ? Color.GREEN : Color.PINK);

                Saucer saucer = new Saucer(ctrl, colourBody, Color.WHITE, saucerMaxHealth);
                double angle = (2 * Math.PI / defenderCount) * i;
                double distanceFromPlanet = SpacePlanet.RADIUS + saucer.radius + 30;

                saucer.position = new Vector2D(
                      planet.position.x + Math.cos(angle) * distanceFromPlanet,
                      planet.position.y + Math.sin(angle) * distanceFromPlanet
                );

                saucer.setHomePlanet(planet);
                planet.addDefender(saucer);

                if(ctrl instanceof AimNShoot) ((AimNShoot) ctrl).setShip(saucer);

                objects.add(saucer);
                ships.add(saucer);
          }
    }

    private SpacePlanet getNearbyPlanet(){
          for(SpacePlanet sp : spacePlanets){
                if(sp.isUnlocked() && sp.playerInRange(playerShip)) return sp;
          }
          return null;
    }

    public static void collectArtifact(){
        artifacts++;
        level++;
        System.out.println("Artifacts collected: " + artifacts);
        System.out.println("Level " + level);

        if(artifacts >= artifactsNeeded){
            winState = true;
            gameOver = true;
        }
        else{ returnToSpace = true;}
    }

    private GameObject addArtifact(){
        double spawnRadius = 14;
        Vector2D pos = planet.randomCavePosition(spawnRadius);
        return new blackOcean.entities.Consumables.Artifact(pos);
    }

    private int getSaucerCount() {return 5 + (level - 1) * 2;}

    private int getSaucerMaxHealth() {return 80 + 10 * (level - 1);}

    public List<Vector2D> getPlanetPositions(){return new ArrayList<>();}

    public static int getScore() {return score;}

    public static int getLevel() {return level;}

    public static int getLives() {return lives;}

    public PlayerShip getPlayerShip(){return playerShip;}

    public static GameMode getCurrentMode(){return mode;}

    public static Planet getCurrentPlanet(){return planet;}

    public static int getArtifacts(){return artifacts;}

    public static boolean hasPlayerWon(){return winState;}
}

