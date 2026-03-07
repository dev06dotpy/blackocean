package blackOcean;

public class CollisionSystem {

      public static void handle(GameObject a, GameObject b) {

            if (!a.overlap(b)) return;

            // bullet hits asteroid
            if (a instanceof Bullet && b instanceof Asteroid) {
                  b.hit(((Bullet) a).damage);
                  a.dead = true;
                  Game.incScore(100);
            }

            else if (b instanceof Bullet && a instanceof Asteroid) {
                  a.hit(((Bullet) b).damage);
                  b.dead = true;
                  Game.incScore(100);
            }

            // bullet hits saucer
            else if (a instanceof Bullet && b instanceof Saucer) {
                  b.hit(((Bullet) a).damage);
                  a.dead = true;
                  Game.incScore(500);
            }

            else if (b instanceof Bullet && a instanceof Saucer) {
                  a.hit(((Bullet) b).damage);
                  b.dead = true;
                  Game.incScore(500);
            }

            // ship collision
            else if (a instanceof Ship && b instanceof Asteroid) {
                  a.hit(10);
                  b.hit(10);
            }

            else if (b instanceof Ship && a instanceof Asteroid) {
                  b.hit(10);
                  a.hit(10);
            }
      }
}