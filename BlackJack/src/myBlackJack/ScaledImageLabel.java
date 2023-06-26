package myBlackJack;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
 
public class ScaledImageLabel extends JLabel {
    protected void paintComponent(Graphics g) {
        ImageIcon icon = (ImageIcon) getIcon();
        if (icon != null) {
            ImageDrawer.drawScaledImage(icon.getImage(), this, g);
        }
    }
}