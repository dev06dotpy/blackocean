package blackOcean.core;

import blackOcean.controllers.Action;
import blackOcean.controllers.Controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keys extends KeyAdapter implements Controller {
      Action action;
      Game game;

      public Keys(Game game) {
            this.game = game;
            action = new Action();
      }

      @Override
      public Action action() {
            return action;
      }

      @Override
      public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                  case KeyEvent.VK_UP:
                        action.thrust = 1;
                        break;
                  case KeyEvent.VK_LEFT:
                        action.turn = -1;
                        break;
                  case KeyEvent.VK_RIGHT:
                        action.turn = 1;
                        break;
                  case KeyEvent.VK_SPACE:
                        action.shoot = true;
                        break;
                  case KeyEvent.VK_P:
                        game.togglePlanetMode();
                        break;
            }

      }

      @Override
      public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                  case KeyEvent.VK_UP:
                        action.thrust = 0;
                        break;
                  case KeyEvent.VK_LEFT:
                        action.turn = 0;
                        break;
                  case KeyEvent.VK_RIGHT:
                        action.turn = 0;
                        break;
                  case KeyEvent.VK_SPACE:
                        action.shoot = false;
                        break;
            }
      }
}

