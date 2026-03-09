package blackOcean.entities.Consumables;

import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

public class FuelTank extends Consumable{

      public FuelTank(Vector2D position){
            super(position, 12, Color.YELLOW);
            System.out.println("FuelTank spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addFuel(100);
            dead = true;
      }

}
