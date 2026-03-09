package blackOcean.entities.Consumables;

import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

public class ShieldBattery extends Consumable{

      public ShieldBattery(Vector2D position){
            super(position, 12, Color.BLUE);
            System.out.println("ShieldBattery spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addShields(35);
            dead = true;
      }

}
