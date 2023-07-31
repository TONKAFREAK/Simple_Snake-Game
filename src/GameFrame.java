import javax.swing.JFrame;

public class GameFrame extends JFrame {
	
	private static final long sericalVersionUID = 1L;
	
	GameFrame(){
		GamePanel panel = new GamePanel();
		this.add(panel);
		this.setTitle("Snake Game by TONKAFREAK");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}