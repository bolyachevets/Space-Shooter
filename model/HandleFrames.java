package ca.ubc.cpsc210.spaceinvaders.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Created by andre on 2017-01-21.
 */
public class HandleFrames {


/*   private String[] imgFileNames = {
            "smallfighter0001.png", "smallfighter0002.png", "smallfighter0003.png", "smallfighter0004.png", "smallfighter0005.png",
            "smallfighter0006.png", "smallfighter0007.png", "smallfighter0008.png", "smallfighter0009.png", "smallfighter0010.png",
            "smallfighter0011.png"};*/

    private String[] imgFileNames = {
            "smallfighter0001.png", "smallfighter0011.png"};

    private Image[] imgFrames;    // array of Images to be animated
    private final int frameRate = 5;


    /** Helper method to load all image frames, with the same height and width */
    public void loadImages() {
        int numFrames = imgFileNames.length;
        imgFrames = new Image[numFrames];  // allocate the array
        URL imgUrl = null;
        for (int i = 0; i < numFrames; ++i) {
            imgUrl = getClass().getClassLoader().getResource(imgFileNames[i]);
            try {
                imgFrames[i] = ImageIO.read(imgUrl);  // load image via URL
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            }
    }

    public Image getImgFrames(int i) {
        return imgFrames[i];
    }

    public int imgFramesSize() {
        return imgFrames.length;
    }
}
