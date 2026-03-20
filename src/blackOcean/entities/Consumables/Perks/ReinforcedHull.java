package blackOcean.entities.Consumables.Perks;

import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.POWER_UP;

public class ReinforcedHull extends Consumable {
      public ReinforcedHull(Vector2D position){
            super(position, 12, Color.LIGHT_GRAY, POWER_UP);
            System.out.println("ReinforcedHull spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addMaxHealth(25);
            dead = true;
      }
}
