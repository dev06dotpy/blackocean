package blackOcean.entities.Consumables;

import blackOcean.entities.GameObject;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

public abstract class Consumable extends GameObject {

      public Consumable(Vector2D position, double radius){
            super(position, new Vector2D(0, 0), radius);
      }

      public abstract void apply(PlayerShip playerShip);
}
