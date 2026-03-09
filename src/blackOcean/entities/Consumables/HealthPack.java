package blackOcean.entities.Consumables;

import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

public class HealthPack extends Consumable{

      public HealthPack(Vector2D position){
            super(position, 12, Color.GREEN);
            System.out.println("HealthPack spawned at: " + position);
      }

      @Override
      public void apply(PlayerShip playerShip){
            playerShip.addHealth(50);
            dead = true;
      }

//      @Override
//      public void draw(Graphics2D g) {
//            g.setColor(Color.GREEN);
//            g.fillOval(
//                  (int)(position.x - radius),
//                  (int)(position.y - radius),
//                  (int)(2 * radius),
//                  (int)(2* radius)
//            );
//      }
}
