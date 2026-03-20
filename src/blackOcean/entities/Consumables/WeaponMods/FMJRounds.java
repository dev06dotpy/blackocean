package blackOcean.entities.Consumables.WeaponMods;

import blackOcean.entities.Consumables.Consumable;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

import static blackOcean.core.Constants.BLASTER_MOD;

public class FMJRounds extends Consumable {
      public FMJRounds(Vector2D position){
            super(position, 12, Color.ORANGE, BLASTER_MOD);
            System.out.println("FMJRounds spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addBulletDamage(5);
            dead = true;
      }
}
