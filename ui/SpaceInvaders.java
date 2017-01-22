package ca.ubc.cpsc210.spaceinvaders.ui;

import ca.ubc.cpsc210.spaceinvaders.model.SIGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * Represents the main window in which the space invaders
 * game is played
 */
@SuppressWarnings("serial")
public class SpaceInvaders extends JFrame {

	private static final int INTERVAL = 20;
	private SIGame game;
	private GamePanel gp;
	private ScorePanel sp;
    private static boolean checkedMute = false;


	// EFFECTS: sets up window in which Space Invaders game will be played
	public SpaceInvaders() {
		super("Space Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		game = new SIGame();
		gp = new GamePanel(game);
		sp = new ScorePanel(game);
		add(gp);
		add(sp, BorderLayout.NORTH);
        addMenu();
		addKeyListener(new KeyHandler());
		pack();
		centreOnScreen();
		setVisible(true);
		addTimer();
	}

	// MODIFIES: none
	// EFFECTS:  initializes a timer that updates game each
	//           INTERVAL milliseconds
	private void addTimer() {
		Timer t = new Timer(INTERVAL, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae) {
				game.update();
				gp.repaint(); 
				sp.update();
			}
		});
		
		t.start();
	}

	// MODIFIES: this
	// EFFECTS:  location of frame is set so frame is centred on desktop
	private void centreOnScreen() {
		Dimension scrn = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scrn.width - getWidth()) / 2, (scrn.height - getHeight()) / 2);
	}

    // MODIFIES: this
    // EFFECTS:  adds menu bar at the top of the JFrame
	private void addMenu() {
        JMenuBar mainMenuBar = new JMenuBar();
        this.setJMenuBar(mainMenuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        mainMenuBar.add(fileMenu);
        JMenu options = new JMenu("Options");
        options.setMnemonic('O');
        mainMenuBar.add(options);
        JMenu helpMenu = new JMenu("Help");
        mainMenuBar.add(helpMenu);

        JMenuItem play = new JMenuItem("Play");
        play.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
        fileMenu.add(play);

        JMenuItem pause = new JMenuItem("Pause");
        pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        fileMenu.add(pause);

        JMenuItem exit = new JMenuItem("Quit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0));
        fileMenu.addSeparator();
        fileMenu.add(exit);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.keyPressed(KeyEvent.VK_R);
                } catch (Exception e2) {
                    e2.getMessage();
                }
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    game.keyPressed(KeyEvent.VK_P);
                } catch (Exception e2) {
                    e2.getMessage();
                }
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        StayOpenCheckBoxMenuItem chckbxSound = new StayOpenCheckBoxMenuItem("Mute");
        options.add(chckbxSound);

        chckbxSound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkedMute == false) {
                    checkedMute = true;
                } else {
                    checkedMute = false;
                }
            }
        });

        JMenuItem about = new JMenuItem("About");
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(about);

        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.keyPressed(KeyEvent.VK_P);
                JOptionPane.showMessageDialog(SpaceInvaders.this,
                        "Use arrow keys to control the tank and spacebar to shoot.",
                        "About Space Invaders", JOptionPane.PLAIN_MESSAGE);
                int input = JOptionPane.OK_OPTION;
                if(input == JOptionPane.OK_OPTION && game.isPaused())
                {
                    game.keyPressed(KeyEvent.VK_P);
                }
            }
        });
    }

    // EFFECTS:  returns true if the mute box is checked in the options menu
    public static boolean isMute() {
	    return checkedMute;
    }
	
	/*
	 * A key handler to respond to key events
	 */
	private class KeyHandler extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			game.keyPressed(e.getKeyCode());
		}
	}

	// Play the game
	public static void main(String[] args) {new SpaceInvaders(); }
}
