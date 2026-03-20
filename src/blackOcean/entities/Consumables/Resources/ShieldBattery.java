package blackOcean.entities.Consumables.Resources;

import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.SHIELD_PACK;

public class ShieldBattery extends Consumable {

      public ShieldBattery(Vector2D position){
            super(position, 12, Color.BLUE, SHIELD_PACK);
            System.out.println("ShieldBattery spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addShields(35);
            dead = true;
      }

}
