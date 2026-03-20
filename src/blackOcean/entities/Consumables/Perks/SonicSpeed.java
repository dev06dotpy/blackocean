package blackOcean.entities.Consumables.Perks;

import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.PlayerShip;
import blackOcean.entities.Ship;
import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.POWER_UP;

public class SonicSpeed extends Consumable {

      public SonicSpeed(Vector2D position){
            super(position, 12, Color.MAGENTA, POWER_UP);
            System.out.println("SonicSpeed spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            Ship.MAG_ACC += 50;
            dead = true;
      }
}
