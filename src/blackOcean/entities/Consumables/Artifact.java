package blackOcean.entities.Consumables;

import blackOcean.core.Game;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

public class Artifact extends Consumable{

      public Artifact(Vector2D position){
            super(position, 30, Color.red);
            System.out.println("Artifact spawned at: " + position.x + ", " + position.y);
      };

      @Override
      public void apply(PlayerShip playerShip){
            //TODO: implement collectArtifact();
            Game.collectArtifact();
            dead = true;
      }

      @Override
      public void draw(Graphics2D g){
            g.setColor(Color.PINK);
            g.fillOval(
                  (int)(position.x - radius),
                  (int)(position.y - radius),
                  (int)(2 * radius),
                  (int)(2 * radius)
            );

            g.setColor(Color.WHITE);
            g.drawOval(
                  (int)(position.x - radius),
                  (int)(position.y - radius),
                  (int)(2 * radius),
                  (int)(2 * radius)
            );
      }
}
