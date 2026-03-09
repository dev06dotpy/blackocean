package blackOcean.core;

import utilities.Vector2D;

import java.awt.*;
import java.util.Random;

import static blackOcean.core.Constants.*;

public class Planet {

      private boolean[][] walls;

      public Planet(){
            walls = new boolean[PLANET_HEIGHT][PLANET_WIDTH];
      }

      public void generate(){
            Random random = new Random();

            //tile fill
            for(int y = 0; y < PLANET_HEIGHT; y++){
                  for(int x = 0; x < PLANET_WIDTH; x++){
                        if(x == 0 || y == 0 || x == PLANET_WIDTH - 1 || y == PLANET_HEIGHT - 1) walls[y][x] = true;
                        else walls[y][x] = random.nextDouble() < 0.53;
                  }
            }

            //smoothing
            for(int i = 0; i < 3; i++){
                  smoothMap();
            }

            clearSpawn();
      }

      private void smoothMap(){
            boolean[][] newWalls = new boolean[PLANET_HEIGHT][PLANET_WIDTH];

            for(int y = 0; y < PLANET_HEIGHT; y++){
                  for(int x = 0; x < PLANET_WIDTH; x++){
                        int wallCount = countWallNeighbours(x, y);

                        if(wallCount >= 5) newWalls[y][x] = true;
                        else newWalls[y][x] = false;
                  }
            }
            walls = newWalls;
      }

      private int countWallNeighbours(int x, int y) {
            int count = 0;

            for (int ny = y - 1; ny <= y + 1; ny++){
                  for (int nx = x - 1; nx <= x + 1; nx++){
                        if(nx == x && ny == y) continue;
                        if(nx < 0 || ny < 0 || nx >= PLANET_WIDTH || ny >= PLANET_HEIGHT) count++;
                        else if (walls[ny][nx]) count++;
                  }
            }
            return count;
      }

      public Vector2D spawnPosition(){
            return new Vector2D(PLANET_PIXEL_WIDTH / 2, PLANET_PIXEL_HEIGHT / 2);
      }

      private void clearSpawn(){
            int centerX = PLANET_WIDTH / 2;
            int centerY = PLANET_HEIGHT / 2;

            for(int y = centerY - 3; y < centerY + 3; y++){
                  for(int x = centerX - 3; x < centerX + 3; x++){
                        if(x >= 0 && y >= 0 && x < PLANET_WIDTH && y < PLANET_HEIGHT) walls[y][x] = false;
                  }
            }
      }

      public boolean isWallTile(int tileX, int tileY) {
            if (tileX < 0 || tileY < 0 || tileX >= PLANET_WIDTH || tileY >= PLANET_HEIGHT) return true;
            return walls[tileY][tileX];
      }

      public boolean isWallAtPixel(double px, double py) {
            int tileX = (int) (px / TILE_SIZE);
            int tileY = (int) (py / TILE_SIZE);
            return isWallTile(tileX, tileY);
      }

      public boolean collidesWithWall(Vector2D pos, double radius) {
            return isWallAtPixel(pos.x, pos.y) ||
                  isWallAtPixel(pos.x - radius, pos.y) ||
                  isWallAtPixel(pos.x + radius, pos.y) ||
                  isWallAtPixel(pos.x, pos.y - radius) ||
                  isWallAtPixel(pos.x, pos.y + radius);
      }

      public Vector2D randomCavePosition(double radius) {
            Random random = new Random();

            while (true) {
                  double x = random.nextInt(PLANET_PIXEL_WIDTH);
                  double y = random.nextInt(PLANET_PIXEL_HEIGHT);
                  Vector2D pos = new Vector2D(x, y);

                  if (!collidesWithWall(pos, radius)) {
                        return pos;
                  }
            }
      }

      public void draw(Graphics2D g){
            for(int y = 0; y < PLANET_HEIGHT; y++){
                  for(int x = 0; x < PLANET_WIDTH; x++){
                        if(walls[y][x]) g.setColor(Color.DARK_GRAY);
                        else g.setColor(Color.BLACK);
                        g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                  }
            }
      }
}
