import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;



public class StartPanel extends JPanel {

	 private static final long serialVersionUID = 1L;
	 private final int WIDTH ;
	 private final int HEIGHT ;

	 StartPanel(int width, int height, GamePanel gamePanel) {
	        this.WIDTH = width;
	        this.HEIGHT = height;
	        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	        this.setBackground(new Color(0, 0, 0, 255));
	        setLayout(null);
	        JButton button = new JButton("Start");
	        button.setForeground(Color.white);
	        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        
	        button.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 20));
	        button.setBackground(Color.BLACK);
	        button.setBorderPainted(true);
	        
	        
	        button.setBounds((width/2) -50 , 400,100,40);
	        button.addActionListener(e -> gamePanel.startGame());
	        add(button);
	        
	       
	    }
	 
	 public void draw (Graphics graphics) {
		 
		 // SNAKE GAME 
		 
 		graphics.setColor(Color.WHITE);
 		graphics.setFont(new Font("Sans serif", Font.BOLD, 50));
 		FontMetrics metrics = getFontMetrics(graphics.getFont());
 		//graphics.drawString("Score: "+ foodEaten, (WIDTH - metrics.stringWidth("Score: "+foodEaten))/2 , graphics.getFont().getSize());
 		graphics.drawString("Snake Game", 100 , 300);
 		
 		//
 		
 		graphics.setColor(Color.WHITE);
 		graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 10));
 		 metrics = getFontMetrics(graphics.getFont());
 		//graphics.drawString("Score: "+ foodEaten, (WIDTH - metrics.stringWidth("Score: "+foodEaten))/2 , graphics.getFont().getSize());
 		graphics.drawString("Or press any button to start the game", 170 , 460);
 		graphics.setColor(Color.BLACK);
		 
	 }
	 
	 @Override
	 public void paintComponent(Graphics graphics) {
	    	
	    	super.paintComponent(graphics);
	    	draw(graphics);
	} 
	    
	 

	   
	
}
