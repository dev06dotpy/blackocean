package blackOcean.core;
import utilities.ImageManager;

import java.awt.*;
import java.io.IOException;

public class Constants {
    public static final int FRAME_HEIGHT = 480;
    public static final int FRAME_WIDTH = 640;
    public static final int WORLD_HEIGHT = FRAME_HEIGHT * 2;
    public static final int WORLD_WIDTH = FRAME_WIDTH * 2;
    public static final Dimension FRAME_SIZE = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    public static final int DELAY = 20;  // milliseconds
    public static final double DT = DELAY/1000.0;  // seconds
    public static final int TILE_SIZE = 32;
    public static final int PLANET_WIDTH = 64;
    public static final int PLANET_HEIGHT = 64;
    public static final int PLANET_PIXEL_WIDTH = PLANET_WIDTH * TILE_SIZE;
    public static final int PLANET_PIXEL_HEIGHT = PLANET_HEIGHT * TILE_SIZE;

    public static Image ASTEROID, SPACE, PLANET_LOCK, PLANET_UNLOCK;
    public static Image PLAYERSHIP, SAUCER1, SAUCER2, BULLET;
    public static Image FUEL_PACK, HEALTH_PACK, SHIELD_PACK, LANDMINE, ARTIFACT;
    public static Image POWER_UP, BLASTER_MOD;
    public static Image SNIPER_MOD, DOUBLE_DAMAGE_MOD, BURST_FIRE_MOD;
    public static Image WALL1;
    public static Image HEALTH_ICON, SHIELD_ICON, FUEL_ICON;
    static{
        try {
            ASTEROID = ImageManager.loadImage("asteroid1.png");
            SPACE = ImageManager.loadImage("space_background.jpg");
            PLANET_LOCK = ImageManager.loadImage("planet08.png");
            PLANET_UNLOCK = ImageManager.loadImage("planet01.png");

            PLAYERSHIP = ImageManager.loadImage("playerShip1_blue.png");
            SAUCER1 = ImageManager.loadImage("enemyRed5.png");
            SAUCER2 = ImageManager.loadImage("enemyGreen3.png");
            BULLET = ImageManager.loadImage("bullet.png");
            ARTIFACT = ImageManager.loadImage("artifact.png");

            HEALTH_PACK = ImageManager.loadImage("health_pack.png");
            SHIELD_PACK = ImageManager.loadImage("shield_pack.png");
            FUEL_PACK = ImageManager.loadImage("fuel.png");
            LANDMINE = ImageManager.loadImage("landmine.png");
            POWER_UP = ImageManager.loadImage("power-up.png");
            BLASTER_MOD = ImageManager.loadImage("blaster.png");

//            SNIPER_MOD = ImageManager.loadImage("");
//            DOUBLE_DAMAGE_MOD = ImageManager.loadImage("");
//            BURST_FIRE_MOD = ImageManager.loadImage("");

            WALL1 = ImageManager.loadImage("platformIndustrial_005.png");

            HEALTH_ICON = ImageManager.loadImage("hospital.png");
            SHIELD_ICON = ImageManager.loadImage("health-insurance.png");
            FUEL_ICON = ImageManager.loadImage("gasoline-pump.png");

        } catch (IOException e) { e.printStackTrace(); }
    }

}
