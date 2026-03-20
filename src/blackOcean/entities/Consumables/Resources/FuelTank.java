package blackOcean.entities.Consumables;

import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.FUEL_PACK;

public class FuelTank extends Consumable{

      public FuelTank(Vector2D position){
            super(position, 12, Color.YELLOW, FUEL_PACK);
            System.out.println("FuelTank spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addFuel(100);
            dead = true;
      }

}
