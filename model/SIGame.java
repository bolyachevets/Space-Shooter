package ca.ubc.cpsc210.spaceinvaders.model;

import ca.ubc.cpsc210.spaceinvaders.ui.SpaceInvaders;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Represents a space invaders game.
 */
public class SIGame {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int MARGIN = 20;
	public static final int MAX_MISSILES = 10;
	public static final Random RND = new Random();
	private static final int INVASION_PERIOD = 250;   // on average, one invader each 250 updates

	private List<Missile> missiles;
	private List<Invader> invaders;
	private List<Explosion> explosions;
	private Tank tank;
	private boolean isGameOver;
	private static boolean paused = false;
	private int numInvadersDestroyed;

	// EFFECTS:  creates empty lists of missiles and invaders, centres tank on screen
	public SIGame() {
		missiles = new ArrayList<Missile>();
		invaders = new ArrayList<Invader>();
		explosions = new ArrayList<Explosion>();
		setUp();
	}


	// MODIFIES: this
	// EFFECTS:  updates tank, missiles and invaders
    public void update() {
	    if(paused) {
	        return;
        }

		moveMissiles();
        moveInvaders();
        tank.move();

        checkMissiles();
		invade();
		checkCollisions();
		checkExplosions();
		checkGameOver();
	}

	// MODIFIES: this
	// EFFECTS:  turns tank, fires missiles and resets game in response to
	//           given key pressed code
	public void keyPressed(int keyCode) {
		if (keyCode == KeyEvent.VK_SPACE)
            fireMissile();
		else if (keyCode == KeyEvent.VK_R && isGameOver)
			setUp();
		else if (keyCode == KeyEvent.VK_X)
			System.exit(0);
		else if (keyCode == KeyEvent.VK_P)
            paused = !paused;
        else
			tankControl(keyCode);
	}

	public boolean isOver() {
		return isGameOver;
	}
	
	public int getNumMissiles() {
		return missiles.size();
	}
	
	public int getNumInvadersDestroyed() {
		return numInvadersDestroyed;
	}
	
	public List<Invader> getInvaders() {
		return invaders;
	}
	
	public List<Missile> getMissiles() {
		return missiles;
	}

	public List<Explosion> getExplosions() {
	    return explosions;
    }


	
	public Tank getTank() {
		return tank;
	}

	// MODIFIES: this
	// EFFECTS:  clears list of missiles and invaders, initializes tank
	private void setUp() {
		invaders.clear();
		missiles.clear();
		explosions.clear();
		tank = new Tank(WIDTH / 2);
		isGameOver = false;
		numInvadersDestroyed = 0;

        SoundEffect.startMidi("src/level3.mid");
	}

	// MODIFIES: this
	// EFFECTS:  fires a missile if max number of missiles in play has
	//           not been exceeded, otherwise silently returns
	private void fireMissile() {
		if (missiles.size() < MAX_MISSILES) {
			Missile m = new Missile(tank.getX(), Tank.Y_POS);
			missiles.add(m);
			if (!SpaceInvaders.isMute() && !paused)
                SoundEffect.shootingSound();
		}
	}

	// MODIFIES: this
	// EFFECTS: turns tank in response to key code
	private void tankControl(int keyCode) {
		if (keyCode == KeyEvent.VK_KP_LEFT || keyCode == KeyEvent.VK_LEFT)
			tank.faceLeft();
		else if (keyCode == KeyEvent.VK_KP_RIGHT || keyCode == KeyEvent.VK_RIGHT)
			tank.faceRight();
	}

	// MODIFIES: this
	// EFFECTS: moves the missiles
	private void moveMissiles() {
		for (Missile next : missiles ) {
			next.move();
		}	
	}

	// MODIFIES: this
	// EFFECTS: moves the invaders
	private void moveInvaders() {
		for (Invader next : invaders) {
			next.move();
		}
	}

	// MODIFIES: this
	// EFFECTS:  removes any missile that has traveled off top of screen
	private void checkMissiles() {
		List<Missile> missilesToRemove = new ArrayList<Missile>();
		
		for (Missile next : missiles) {
            if (next.getY() < 0) {
                missilesToRemove.add(next);
			}
		}
		
		missiles.removeAll(missilesToRemove);
	}
	
	// MODIFIES: this
    // EFFECTS: generates new invaders
	private void invade() {
		if (RND.nextInt(INVASION_PERIOD) < 1) {
			Invader i = new Invader(RND.nextInt(SIGame.WIDTH - SIGame.MARGIN) + SIGame.MARGIN, 0);
			invaders.add(i);
		}
	}

	// MODIFIES: this
	// EFFECTS:  removes any invader that has been shot with a missile
	//           and removes corresponding missile from play
	private void checkCollisions() {
		List<Invader> invadersToRemove = new ArrayList<Invader>();
		List<Missile> missilesToRemove = new ArrayList<Missile>();
		
		for (Invader target : invaders) {
			if (checkInvaderHit(target, missilesToRemove)) {
				invadersToRemove.add(target);
				explosions.add(new Explosion(target.getX(), target.getY()));
                if (!SpaceInvaders.isMute())
                    SoundEffect.invaderShotSound();
			}
		}
		invaders.removeAll(invadersToRemove);
		missiles.removeAll(missilesToRemove);
	}

	//MODIFIES: this
    //EFFECTS: removes expired explosions
	private void checkExplosions() {

        List<Explosion> explosionsToRemove = new ArrayList<Explosion>();

        for (Explosion ex : explosions) {
            if (ex.getRadius() == 0) {
                explosionsToRemove.add(ex);
            }
        }
        explosions.removeAll(explosionsToRemove);
    }

    //MODIFIES: list of missiles
	//EFFECTS: true if one of the missiles on the list collided with the invader target
	private boolean checkInvaderHit(Invader target, List<Missile> missilesToRemove) {
		for (Missile next : missiles) {
			if (target.collidedWith(next)) {
				missilesToRemove.add(next);
				numInvadersDestroyed++;
				return true;
			}
		}
		return false;
	}

	// MODIFIES: this
	// EFFECTS:  if an invader has landed, game is marked as
	//           over and lists of invaders & missiles cleared
	private void checkGameOver() {
		for (Invader next : invaders) {
			if (next.getY() > HEIGHT) {
				isGameOver = true;
                if (!SpaceInvaders.isMute())
                    SoundEffect.playerExplosionSound();
			}
		}
		
		if (isGameOver) {
			invaders.clear();
			missiles.clear();
			explosions.clear();
            SoundEffect.stopMidi();
		}
	}

	//Effects: returns true if the game has been paused
	public boolean isPaused() {
	    return paused;
    }
}
