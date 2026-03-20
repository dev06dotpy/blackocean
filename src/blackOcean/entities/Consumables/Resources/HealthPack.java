package blackOcean.entities.Consumables.Resources;

import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.HEALTH_PACK;

public class HealthPack extends Consumable {

      public HealthPack(Vector2D position){
            super(position, 12, Color.GREEN, HEALTH_PACK);
            System.out.println("HealthPack spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addHealth(50);
            dead = true;
      }
}
