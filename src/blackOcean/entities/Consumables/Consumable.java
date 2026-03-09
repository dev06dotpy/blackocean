package blackOcean.entities.Consumables;

import blackOcean.entities.GameObject;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

public abstract class Consumable extends GameObject {

      protected Color color;

      public Consumable(Vector2D position, double radius, Color color){
            super(position, new Vector2D(0, 0), radius);
            this.color = color;
      }

      public abstract void apply(PlayerShip playerShip);

      @Override
      public void draw(Graphics2D g) {
            g.setColor(color);
            g.fillOval(
                  (int)(position.x - radius),
                  (int)(position.y - radius),
                  (int)(2 * radius),
                  (int)(2* radius)
            );
      }
}
