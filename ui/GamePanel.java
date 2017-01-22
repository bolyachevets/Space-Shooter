package ca.ubc.cpsc210.spaceinvaders.ui;

import ca.ubc.cpsc210.spaceinvaders.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/*
 * The panel in which the game is rendered.
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private static final String OVER = "Game Over!";
	private static final String REPLAY = "R to replay";
	private SIGame game;
    private ImageIcon icon;
    private AffineTransform transform = new AffineTransform();

	// EFFECTS:  sets size and background colour of panel,
	//           updates this with the game to be displayed
	public GamePanel(SIGame g) {
		setPreferredSize(new Dimension(SIGame.WIDTH, SIGame.HEIGHT));
        icon = new ImageIcon("src/invaders.png");
        this.game = g;
	}

	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(icon.getImage(), 0,0, null);
        drawGame(g);
		
		if (game.isOver()) {
			gameOver(g);
		}
	}

	// MODIFIES: g
	// EFFECTS:  draws the game onto g
	private void drawGame(Graphics g) {
		drawTank(g);
		drawInvaders(g);
		drawMissiles(g);
		drawExplosions(g);

	}

    // MODIFIES: g
    // EFFECTS:  draws the spaceship onto g
    private void drawTank(Graphics g) {
        HandleFrames hf = new HandleFrames();
        hf.loadImages();
        Tank t = game.getTank();
        Graphics2D g2d = (Graphics2D) g;
        transform.setToIdentity();

        int i = Math.round(hf.imgFramesSize()/2);

        if (t.isFacingRight()) {
            int width = hf.getImgFrames(0).getWidth(null);
            int height = hf.getImgFrames(0).getHeight(null);

            // The origin is initially set at the top-left corner of the image.
            // Move the center of the image to (x, y).
            transform.translate(t.getX() - width/2, Tank.Y_POS - height/2);
            g2d.drawImage(hf.getImgFrames(0), transform, null);

        }
        if (!t.isFacingRight()) {
            int width = hf.getImgFrames(1).getWidth(null);
            int height = hf.getImgFrames(1).getHeight(null);

            // The origin is initially set at the top-left corner of the image.
            // Move the center of the image to (x, y).
            transform.translate(t.getX() - width/2, Tank.Y_POS - height/2);
            g2d.drawImage(hf.getImgFrames(1), transform, null);
        }
    }

	// MODIFIES: g
	// EFFECTS:  draws the invaders onto g
	private void drawInvaders(Graphics g) {
		for(Invader next : game.getInvaders()) {
			drawInvader(g, next);
		}
	}

	// MODIFIES: g
	// EFFECTS:  draws the invader i onto g
    private void drawInvader(Graphics g, Invader i) {
        Tank t = game.getTank();
        Graphics2D g2d = (Graphics2D) g;
        transform.setToIdentity();

        // Apply the transform to the image and draw
        try {
            Image image = ImageIO.read(getClass().getClassLoader().getResource("speedship.png"));
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            transform.rotate(Math.PI, i.getX(), i.getY());
            transform.translate(i.getX() - width/2, i.getY() - height/2);
            g2d.drawImage(image, transform, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	// MODIFIES: g
	// EFFECTS:  draws the missiles onto g
	private void drawMissiles(Graphics g) {
		for(Missile next : game.getMissiles()) {
			drawMissile(g, next);
		}
	}

	// MODIFIES: g
	// EFFECTS:  draws the missile m onto g
	private void drawMissile(Graphics g, Missile m) {
		Color savedCol = g.getColor();
		g.setColor(Missile.COLOR);
		g.fillOval(m.getX() - Missile.SIZE_X/2, m.getY() - Missile.SIZE_Y / 2, Missile.SIZE_X, Missile.SIZE_Y);
		g.setColor(savedCol);
	}

    // MODIFIES: g
    // EFFECTS:  draws explosions onto g
    private void drawExplosions(Graphics g) {
        for(Explosion next : game.getExplosions()) {
            drawExplosion(g, next.fade());
        }
    }

    // MODIFIES: g
    // EFFECTS:  draws explosion e onto g
	private void drawExplosion(Graphics g, Explosion ex) {
        Color savedCol = g.getColor();
        g.setColor(ex.getColor());
        g.fillOval(ex.getX()-ex.getRadius(), ex.getY()-ex.getRadius(), (int)Math.round(2*ex.getRadius()), (int)Math.round(2*ex.getRadius()));
        g.setColor(savedCol);
    }

	// MODIFIES: g
	// EFFECTS:  draws "game over" and replay instructions onto g
	private void gameOver(Graphics g) {
		Color saved = g.getColor();
		g.setColor(new Color( 0, 0, 0));
		g.setFont(new Font("Arial", Font.BOLD, 20));
		FontMetrics fm = g.getFontMetrics();
		centreString(OVER, g, fm, SIGame.HEIGHT / 2);
		centreString(REPLAY, g, fm, SIGame.HEIGHT / 2 + 50);
		g.setColor(saved);
	}

	// MODIFIES: g
	// EFFECTS:  centres the string str horizontally onto g at vertical position yPos
	private void centreString(String str, Graphics g, FontMetrics fm, int yPos) {
		int width = fm.stringWidth(str);
		g.drawString(str, (SIGame.WIDTH - width) / 2, yPos);
	}

}
