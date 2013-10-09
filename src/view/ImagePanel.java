package view;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Arch. Don Saborrido
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel{

    private BufferedImage image;

    public ImagePanel(BufferedImage image ){
        this.image = image;
    }

    public void setImage(BufferedImage image){
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        @SuppressWarnings("unused")
		Dimension d = getSize();
        g.drawImage( image, 0, 0, this );
    }

}
