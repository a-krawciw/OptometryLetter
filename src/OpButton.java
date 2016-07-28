import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by Windows on 2016-07-25.
 */
public class OpButton extends JButton {

    private Image image = null;
    public OpButton(String text, ActionListener a){
        super(text);
        addActionListener(a);
        setOpaque(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(image != null){
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Main.mainBlue);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

            // get metrics from the graphics
            g.setFont(new Font("Calibri", Font.BOLD, 18));
            FontMetrics metrics = g.getFontMetrics();
            int hgt = metrics.getHeight();
            int adv = metrics.stringWidth(getText());;

            g.setColor(Color.black);
            g.drawString(super.getText(), (getWidth() - adv) / 2, (getHeight() + hgt) / 2 - 6);
        }

    }

    public void setImage(Image i ){
        image = i;
    }


}
