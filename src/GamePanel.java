import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.prefs.Preferences;
import javax.imageio.*;
import java.io.*;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener  {
	
	private static final long serialVersionUID = 1L; 
    static final int WIDTH = 500; 
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT)/(UNIT_SIZE * UNIT_SIZE);
    
    private static final int TOP_MENU = 0;
    private static final int TOP_BOUNDARY = 20;
    private static final int BOUNDARY_SIZE = 1;
    private static final int BOTTOM_BOUNDARY = HEIGHT - BOUNDARY_SIZE;
    private static final int LEFT_BOUNDARY = 0;
    private static final int RIGHT_BOUNDARY = WIDTH - BOUNDARY_SIZE;
       
    // coordinates of the snake 
    
    final int x[] = new int[NUMBER_OF_UNITS];
    final int y[] = new int[NUMBER_OF_UNITS];
    
    // initial snake length
    
    int snakeLength = 5; 
    
    private int foodEaten, bestScore;
    int foodX,foodY;
    char direction = 'D';
    boolean running = false;
    boolean gameStarted = false;
    Random random;
    Timer timer;
    
    //apple image
    
    private BufferedImage appleImage, snakeHeadImage,snakeBodyImage,snakeTailImage;
    
    //panels
    
    private PausePanel pausePanel;
    private StartPanel startPanel;
    
    GamePanel(){
    	
    	//GamePanel
    	
    	random = new Random();
    	this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
    	this.setBackground(Color.LIGHT_GRAY);
    	this.setFocusable(true);
    	this.setVisible(true);
    	this.addKeyListener(new MyKeyAdapter());
        this.requestFocusInWindow(true);
    	bestScore = loadBestScoreFromPreferences();
    	play();
    	
    	//PausePanel 
    	
    	pausePanel = new PausePanel(WIDTH, HEIGHT);
        this.add(pausePanel);
        pausePanel.setVisible(false);
        
        //StartPanel
        
        startPanel = new StartPanel(WIDTH,HEIGHT, this);
        this.add(startPanel);
        startPanel.setVisible(true);
        
        //load apple image 
        
        try {
        	appleImage = ImageIO.read(getClass().getResource("apple.png"));
        //	snakeHeadImage = ImageIO.read(getClass().getResource("head.png"));
        //	snakeBodyImage = ImageIO.read(getClass().getResource("body.png"));
        //	snakeTailImage = ImageIO.read(getClass().getResource("tail.png"));
        	
        } catch(IOException e){
        	e.printStackTrace();
        }
        
        
    	
    }
    
    public void play() {
    	
        
        initialPosition();
        generateFood();
        running = true;
        timer = new Timer(120, this);
        timer.start();

        // Set gameStarted to true when the game starts
        gameStarted = true;
    }
    
    public void initialPosition() {
    	
    	// Reinitialize the snake's position
        
        for (int i = 0; i < snakeLength; i++) {
        	direction = 'R';
        	x[i] = 100;
            y[i] = 300;
        }
        
    }
    
    private void drawCubedBackground(Graphics graphics) {
        for (int i = 0; i < WIDTH / UNIT_SIZE; i++) {
            for (int j = 0; j < HEIGHT / UNIT_SIZE; j++) {
            	if(running) {
                if ((i + j) % 2 == 0) {
                    graphics.setColor(Color.GRAY); // Color for the even squares
                } else {
                    graphics.setColor(Color.DARK_GRAY); // Color for the odd squares
                }
                graphics.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            	}else {
            	    if ((i + j) % 2 == 0) {
                        graphics.setColor(Color.BLACK); 
                    } else {
                        graphics.setColor(Color.BLACK); 
                    }
                    graphics.fillRect(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            	}
            }
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
    	
    	super.paintComponent(graphics);
    	drawCubedBackground(graphics);
    	draw(graphics);
    } 
    
    public void move() {
    	
    	if (gameStarted) {
    	for (int i = snakeLength; i > 0; i--) {
    		x[i] = x[i-1];
    		y[i] = y[i-1];
    	}
    	
    	if (direction == 'L') {
    		x[0] = x[0] - UNIT_SIZE;
    	} else if (direction == 'R') {
    		x[0] = x[0] + UNIT_SIZE;
    	} else if (direction == 'U') {
    		y[0] = y[0] - UNIT_SIZE;
    	} else {
    		y[0] = y[0] + UNIT_SIZE;
    	}
    	}
    	checkHit();
    	
    }
	
    public void checkFood() {
    	if (Math.abs(x[0] - foodX) < UNIT_SIZE && Math.abs(y[0] - foodY) < UNIT_SIZE) {
            snakeLength++;
            foodEaten++;

            if (foodEaten > bestScore) {
                bestScore = foodEaten;

                // Save the best score to preferences
                saveBestScoreToPreferences(bestScore);
            }

            generateFood();
        }
    }
	
    public void draw(Graphics graphics) {
    	
    	if (running) {
    		
    		// food color and drawing
    		
//    		graphics.setColor(Color.RED);
//    		graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
    		
    		graphics.drawImage(appleImage, foodX, foodY, UNIT_SIZE,UNIT_SIZE,null);
    		
    		// snakes head
    		
    		graphics.setColor(Color.WHITE);
    		graphics.fillRoundRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE, 8, 5);
    		
    		// snakes body
    		
    		for (int i = 1; i < snakeLength; i++) {
    			graphics.setColor(Color.GREEN);
    			graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
    			
    		}
    		
    		// walls
    		
    		graphics.setColor(Color.DARK_GRAY);
            graphics.fillRect(LEFT_BOUNDARY, TOP_BOUNDARY, BOUNDARY_SIZE, HEIGHT); // Left wall
            graphics.fillRect(RIGHT_BOUNDARY, TOP_BOUNDARY, BOUNDARY_SIZE, HEIGHT); // Right wall
            graphics.fillRect(LEFT_BOUNDARY, TOP_BOUNDARY, WIDTH, BOUNDARY_SIZE*40); // Top wall
            graphics.fillRect(LEFT_BOUNDARY, BOTTOM_BOUNDARY, WIDTH, BOUNDARY_SIZE); // Bottom wall
    		
            //score menu
            
            graphics.fillRect(LEFT_BOUNDARY, TOP_MENU, WIDTH, BOUNDARY_SIZE*40); // Top wall
            
    		// score text 
    		
    		graphics.setColor(Color.WHITE);
    		graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 20));
    		FontMetrics metrics = getFontMetrics(graphics.getFont());
    		//graphics.drawString("Score: "+ foodEaten, (WIDTH - metrics.stringWidth("Score: "+foodEaten))/2 , graphics.getFont().getSize());
    		graphics.drawString("Score: "+ foodEaten, 20 , graphics.getFont().getSize()+10);
    	
    		
    	} else {
    		gameOver(graphics);
    	}
    }
    
    public void generateFood() {
        // Random food generation
        boolean validPosition = false;
        while (!validPosition) {
        	foodX = random.nextInt((WIDTH - 2 * BOUNDARY_SIZE - UNIT_SIZE) / UNIT_SIZE) * UNIT_SIZE + BOUNDARY_SIZE;
            foodY = random.nextInt((HEIGHT - (2 * BOUNDARY_SIZE) - (2 * TOP_MENU) - UNIT_SIZE) / UNIT_SIZE) * UNIT_SIZE + (BOUNDARY_SIZE * 40) + TOP_MENU;

            // Ensure foodX is within the range of 0 to 400 and can be divided by 20
            foodX = Math.min(foodX, 400);
            foodX = foodX - (foodX % UNIT_SIZE);

            // Ensure foodY is within the range of 60 to 580 and can be divided by 20
            foodY = Math.min(Math.max(foodY, 60), 580);
            foodY = foodY - (foodY % UNIT_SIZE);
        	 System.out.println(foodX+" "+foodY);   

            // Check if the food is not on the snake's body
            boolean onSnake = false;
            for (int i = 0; i < snakeLength; i++) {
                if (x[i] == foodX && y[i] == foodY) {
                    onSnake = true;
                     
                    break;
                }
            }

            // If the food is not on the snake's body and not inside the boundary area, it is considered valid
            validPosition = !onSnake && (foodX >= BOUNDARY_SIZE && foodX < WIDTH - BOUNDARY_SIZE && foodY >= BOUNDARY_SIZE + 20 && foodY < HEIGHT - BOUNDARY_SIZE + 20);
        }
    }

    
    public void checkHit() {
    	
    	// snakes run into walls check 
    	
    	for (int i = 0; i < snakeLength; i++) {
    	if (x[i] < LEFT_BOUNDARY || x[i] > RIGHT_BOUNDARY || y[i] < TOP_BOUNDARY*2 || y[i] > BOTTOM_BOUNDARY ) {
                running = false; 
        }
    	}
    	
    	// check if head run into its body
    	for (int i = snakeLength; i > 0; i--) {
    		if (x[0] == x[i] && y[0] == y[i]) {
    			running = false;
    			break;
    		}
    	}
    	
    	if(!running) {
    		timer.stop();
    	}
    }
    
    public void gameOver(Graphics graphics) {
    	
    	// game over
    	
    	graphics.setColor(Color.WHITE); // set text color 
    	graphics.setFont(new Font("Sans serif",Font.ROMAN_BASELINE,50)); // set new Font
    	FontMetrics metrics = getFontMetrics(graphics.getFont()); // use the new Font 
    	graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT/2); // center of the screen 
    
    	//score
    	
    	graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Score: "+ foodEaten, (WIDTH - metrics.stringWidth("Score: "+foodEaten))/2 , graphics.getFont().getSize()+30);
		
		// best score 
		
		graphics.setColor(Color.white);
		graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE,25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Best: " + bestScore, ((WIDTH - metrics.stringWidth("Best: " + bestScore)) / 2), graphics.getFont().getSize()+80);
		
		// press any key to restart
		
		graphics.setColor(Color.white);
		graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE,15));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Press any key to restart", ((WIDTH - metrics.stringWidth("Game Over")) / 2)-34, (HEIGHT/2)+30); // center of the screen 
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    	
        if (gameStarted) { // Only update the game if it has started
            if (running) {
                move();
                checkFood();
                checkHit();
            }

            repaint();
        }
    }

	public void restartGame() {
        // Reset all the game variables to their initial values
        snakeLength = 5;
        foodEaten = 0;
        direction = 'R';
        running = true;

        // Reinitialize the snake's position
       
        for (int i = 0; i < snakeLength; i++) {
        	
        	x[i] = 100;
            y[i] = 300;
        }

        // Generate a new food
        
        generateFood();

        // Start the game again
        
        timer.start();
    }
	
	public void pause() {
		
		timer.stop();
		pausePanel.setVisible(true);
		
	}
	
	public void unpause() {
		
		timer.start();
		pausePanel.setVisible(false);
		
	}
	
    public class MyKeyAdapter extends KeyAdapter {
    	
    	private boolean gameStarted = false;
    	
		@Override
		public void keyPressed(KeyEvent e) {
			if (!gameStarted) { // If the game hasn't started yet
	            gameStarted = true;
	            startGame();
	        } else {
			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if ( direction != 'R' && running) {
						direction = 'L';
					}
					break;
				case KeyEvent.VK_RIGHT:
					if ( direction != 'L' && running) {
						direction = 'R';
					}
					break;
				case KeyEvent.VK_UP:
					if ( direction != 'D' && running) {
						direction = 'U';
					}
					break;
				case KeyEvent.VK_DOWN:
					if (direction != 'U' && running) {
						direction = 'D';
					}
					break;
				case KeyEvent.VK_SPACE:
					if (timer.isRunning()) {
						pause();
					} else if (!timer.isRunning()) {
						unpause();
					}
					break;
				case KeyEvent.VK_ESCAPE:
					System.exit(0);
					break;
						
			}
			if (!running) {
				restartGame();
			}	
		}
		}
    }
	
    private void saveBestScoreToPreferences(int bestScore) {
        Preferences prefs = Preferences.userNodeForPackage(GamePanel.class);
        prefs.putInt("bestScore", bestScore);
    }

    private int loadBestScoreFromPreferences() {

        Preferences prefs = Preferences.userNodeForPackage(GamePanel.class);
        return prefs.getInt("bestScore", 0);
    }
    
    public void startGame() {
        startPanel.setVisible(false);
        gameStarted = true;
        //restartGame();
    }

}


