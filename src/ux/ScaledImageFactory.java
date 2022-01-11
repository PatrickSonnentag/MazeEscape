package ux;

import javax.swing.*;
import java.awt.*;

/**
 * This class takes a given Image and JComponent and scales the image in by a given amount. This can be used so, that
 * the images fit the JComponents which contain them
 * // TODO authors
 */
public class ScaledImageFactory {

    /**
     * Scales the image to a given dimension. We use the most intensive algorithm that is provided out of the box.
     * @param srcImg    Image to be scaled
     * @param xSize New width
     * @param ySize New height
     * @return  Resized image
     */
    public ImageIcon getScaledImage(ImageIcon srcImg, int xSize, int ySize){
        Image image = srcImg.getImage();
        Image newImage = image.getScaledInstance(xSize, ySize,  Image.SCALE_AREA_AVERAGING);
        return new ImageIcon(newImage);
    }
}