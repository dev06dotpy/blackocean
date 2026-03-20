package blackOcean.entities.Consumables.WeaponMods;

import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.BLASTER_MOD;

public class RailGun extends Consumable {
      public RailGun(Vector2D position){
            super(position, 12, Color.GRAY, BLASTER_MOD);
            System.out.println("RailGun spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addBulletLifetime(0.5);
            playerShip.addBulletDamage(-2);
            dead = true;
      }
}
