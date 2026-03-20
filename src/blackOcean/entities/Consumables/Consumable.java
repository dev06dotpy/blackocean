package blackOcean.entities.Consumables;

import blackOcean.entities.GameObject;
import blackOcean.entities.PlayerShip;
import utilities.Vector2D;

import java.awt.*;

public abstract class Consumable extends GameObject {

      protected Color color;
      protected Image image;
      private static final double IMAGE_SCALE = 1.5;

      public Consumable(Vector2D position, double radius, Color color){
            this(position, radius, color, null);
      }

      public Consumable(Vector2D position, double radius, Color color, Image image){
            super(position, new Vector2D(0, 0), radius);
            this.color = color;
            this.image = image;
      }

      public abstract void apply(PlayerShip playerShip);

      @Override
      public void draw(Graphics2D g) {
            if (image != null) {
                  int size = (int) Math.round(2 * radius * IMAGE_SCALE);
                  g.drawImage(image,
                        (int) Math.round(position.x - size / 2.0),
                        (int) Math.round(position.y - size / 2.0),
                        size,
                        size,
                        null
                  );
                  return;
            }

            g.setColor(color);
            g.fillOval(
                  (int)(position.x - radius),
                  (int)(position.y - radius),
                  (int)(2 * radius),
                  (int)(2* radius)
            );
      }
}
