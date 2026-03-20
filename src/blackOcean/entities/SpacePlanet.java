package blackOcean.entities;

import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static blackOcean.core.Constants.PLANET_LOCK;
import static blackOcean.core.Constants.PLANET_UNLOCK;

public class SpacePlanet extends GameObject {
      private List<Saucer> defenders;
      private boolean unlocked;

      public static final int RADIUS = 125;
      public static final double ENTER_DISTANCE = 2.0 * RADIUS;

      public SpacePlanet(Vector2D position){
            super(position, new Vector2D(0, 0), RADIUS);
            defenders = new ArrayList<>();
            unlocked = false;
            System.out.println("Planet current position: " + position.x + ", " + position.y);
      }

      public void addDefender(Saucer saucer){defenders.add(saucer);}

      public boolean isUnlocked(){return unlocked;}

      public boolean playerInRange(PlayerShip player){return position.dist(player.position) <= ENTER_DISTANCE;}

      @Override
      public void update(){
            defenders.removeIf(s -> s.dead);
            unlocked = defenders.isEmpty();
      }

      @Override
      public void draw(Graphics2D g){
            int size = (int) (radius * 2);
            int x = (int) (position.x - radius);
            int y = (int) (position.y - radius);

            Image planetImage = unlocked ? PLANET_UNLOCK : PLANET_LOCK;
            if (planetImage != null) {
                  g.drawImage(planetImage, x, y, size, size, null);
            } else {
                  Color old = g.getColor();
                  if(unlocked) g.setColor(Color.GREEN);
                  else g.setColor(Color.RED);
                  g.fillOval(x, y, size, size);
                  g.setColor(old);
            }

            if(unlocked) g.drawString("Press E to Enter", (int) position.x - 20, (int) position.y - 45);
      }


}
