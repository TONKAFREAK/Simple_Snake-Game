import java.awt.*;
import javax.swing.*;

public class PausePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final int WIDTH ;
    private final int HEIGHT  ;

    PausePanel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(0, 0, 0, 190));
     
       
    }

   
}
