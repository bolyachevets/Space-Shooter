package ca.ubc.cpsc210.spaceinvaders.model;

import java.awt.*;

import static ca.ubc.cpsc210.spaceinvaders.model.SIGame.RND;

/**
 * Created by andre on 2017-01-21.
 */
public class Explosion {

    private final int DR = 1;
    private final int DC = 5;
    private final int MIN_RAD = 20;
    private final int MAX_RAD = 100;
    private final int MOST_GREEN = 50;

    private int x;
    private int y;
    private int radius;
    private Color color;

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
        this.radius = RND.nextInt(MAX_RAD - MIN_RAD) + MIN_RAD;
        this.color = new Color(100, 0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }

    public Explosion fade() {
            this.color = new Color(color.getRed(), color.getGreen() + DC, 0);
            int r = getRadius() + DR;
            if (color.getGreen() < MOST_GREEN) {
                this.radius = r;
                this.color = new Color(color.getRed(), color.getGreen(), 0);
            } else {
                this.radius = 0;
                this.color = new Color(100, 100, 0);
            }
            return this;
    }

}
