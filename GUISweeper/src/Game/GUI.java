package Game;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class GUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	Date startDate = new Date();
	Date endDate;
	
	int spacing = 5;
	
	int neighs = 0;
	
	public int mx = -100;
	public int my = -100;
	
	public int smileyX = 605;
	public int smileyY = 5;
	
	public int timeX = 1100;
	public int timeY = 5;
	
	public int sec = 0;
	
	public int vicMesX = 700;
	public int vicMesY = -50;
	
	String vicMes = "Nothing Yet!";
	
	public int smileyCenterX = smileyX + 35;
	public int smileyCenterY = smileyY + 35;
	
	public boolean resetter = false;
	public boolean happiness = true;
	public boolean victory = false;
	public boolean defeat = false;
	
	Random rand = new Random();
	
	int[][] mines = new int [16][9];
	int[][] neighbors = new int[16][9];
	boolean[][] revealed = new boolean[16][9];
	boolean[][] flagged = new boolean[16][9];
	
	/**
	 * GUI constructor
	 * Setting up the frame, mines.
	 * Using the Board class to set up the board
	 * Click class to uncover all the squares
	 * Mouse class to check the movements
	 */
	
	
	public GUI() {
		this.setTitle("Extreme Minesweeper");
		this.setSize(1286,829);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		/**
		 * Sets up the mines randomly throughout the board
		 * Mines are 1
		 * Non-Mines are 0
		 */
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (rand.nextInt(100) < 10) {
					mines[i][j] = 1;
				} else {
					mines[i][j] = 0;
				}
				revealed[i][j] = false;
			}
		}
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				neighs = 0;
				for (int m = 0; m < 16; m++) {
					for (int n = 0; n < 9; n++) {
						if (!(m ==i && n == j)) {
							if (isN(i,j,m,n) == true) {
							neighs++;
						}
						}
					}
					neighbors[i][j] = neighs;
				}
			}
		}
		
		
		Board board = new Board();
		this.setContentPane(board);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
	}
	
	public class Board extends JPanel {
		
		private static final long serialVersionUID = 1L;

		/**
		 * Paint Component method to draw the board 
		 * Graphics class to set colors and draw shapes
		 */
		
		
		public void paintComponent(Graphics g) {
			g.setColor(new Color(22,145,217)); // Dark Blue board
			g.fillRect(0, 0, 1280, 800); // The frame is 1280 x 800
			
			for (int i = 0; i < 16; i++) 
			{
				for (int j = 0; j < 9; j++)
				{
					g.setColor(Color.gray); // The squares are grey color
					if (revealed[i][j] == true) { 
						g.setColor(Color.white); // Uncovered squares are white color
						if (mines[i][j] == 1) {
							g.setColor(Color.red); // Mines are red color
						}
					} 
					
					/**
					 * If mouse position is hovered on any squares, the square will turn light gray. 
					 */
					
					if (mx >= spacing + i*80 && mx < spacing + i*80 + 80 - 2* spacing && my >= spacing + j * 80 + 80 + 26 && my < spacing + j* 80 + 26 + 80 + 80 - 2*spacing) {
						g.setColor(Color.lightGray);
					}
					g.fillRect(spacing + i*80, spacing + j * 80 + 80, 80-2*spacing, 80-2*spacing);
					if (revealed[i][j] == true) {
						g.setColor(Color.black);
						
						/**
						 * Check for neighboring mines from 1 - 8
						 * Draws the text for all the number digits
						 */
			
						if (mines[i][j] == 0 && neighbors[i][j] != 0) {
							if (neighbors[i][j] == 1) {
								g.setColor(Color.blue);
							} else if (neighbors[i][j] == 2) {
								g.setColor(Color.green);
							} else if (neighbors[i][j] == 3) {
								g.setColor(Color.red);
							}else if (neighbors[i][j] == 4) {
								g.setColor(new Color(0,0,128));
							} else if (neighbors[i][j] == 5) {
								g.setColor(new Color(178,34,34));
							}else if (neighbors[i][j] == 6) {
								g.setColor(new Color(72,209,204));
							}else if (neighbors[i][j] == 8) {
								g.setColor(Color.darkGray);
							}
							g.setFont(new Font("Arial",Font.BOLD, 40));
							g.drawString(Integer.toString(neighbors[i][j]), i*80+27, j*80+80+55);
						
						
						/**
						 * If mine is revealed, the square would be a red color. 
						 * Draws the shape of the mine.
						 */
						
						} else if (mines[i][j]==1){
							g.fillRect(i*80+10+20, j*80+80+20, 20, 40);
							g.fillRect(i*80+20,j*80+80+10+20, 40, 20);
							g.fillRect(i*80+5+20,j*80+80+5+20, 30, 30);
							g.fillRect(i*80+38, j*80+80+15, 4, 50);
							g.fillRect(i*80+15, j*80+80+38, 50, 4);
						}
					} 
				}
			}
			
			//Logo painting
			
			Graphics2D gr = (Graphics2D)g;
			gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Font font = new Font("Tahoma", Font.PLAIN, 72);
			gr.setFont(font);
			gr.setColor(Color.ORANGE);
			gr.drawString("MINESWEEPER", 35, 70);
			
			
			//smile painting
			
			g.setColor(Color.cyan);
			g.fillOval(smileyX, smileyY, 70, 70);
			g.setColor(Color.black);
			g.fillOval(smileyX + 15, smileyY + 20, 10, 10);
			g.fillOval(smileyX + 45, smileyY + 20, 10, 10);
			if (happiness == true) {
				g.fillRect(smileyX + 20, smileyY + 50, 30, 5);
				g.fillRect(smileyX + 17, smileyY + 45, 5, 5);
				g.fillRect(smileyX + 48, smileyY + 45, 5, 5);
			} else {
				g.fillRect(smileyX + 20, smileyY + 45, 30, 5);
				g.fillRect(smileyX + 17, smileyY + 50, 5, 5);
				g.fillRect(smileyX + 48, smileyY + 50, 5, 5);
			}
			
			//time counter painting
			
			g.setColor(Color.black);
			g.fillRect(timeX, timeY, 140, 70);
			
			if (defeat == false && victory == false) {
				sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
			}
			if (sec > 999) {
				sec = 999;
			}
			g.setColor(Color.white);
			if (victory == true) {
				g.setColor(Color.green);
			} else if (defeat == true) {
				g.setColor(Color.red);
			}
			g.setFont(new Font("Tahoma", Font.PLAIN, 80));
			if (sec < 10) {
				g.drawString("00" +Integer.toString(sec), timeX, timeY + 65);
			} else if (sec < 100) {
				g.drawString("0" + Integer.toString(sec), timeX, timeY + 65);
			} else {
				g.drawString(Integer.toString(sec), timeX, timeY + 65);
			}
			
			//victory message painting
			if (victory == true) {
				g.setColor(Color.green);
				vicMes = "YOU WIN";
			} else if (defeat == true) {
				g.setColor(Color.red);
				vicMes = "YOU LOSE";
			}
			
			if (victory == true || defeat == true) {
				vicMesY = - 50 + (int) (new Date().getTime() - endDate.getTime()) / 10;
				if (vicMesY > 70) {
					vicMesY = 70;
				}
				g.drawString(vicMes, vicMesX, vicMesY);
			}
			
		}
	}
	
	public class Move extends MouseAdapter {

		public void mouseMoved(MouseEvent e) {
			System.out.println("The mouse was moved");
			mx = e.getX();
			my = e.getY();
			System.out.println("X: " + mx + ", " + "Y: " + my);
		}
		
	}
	
	public class Click extends MouseAdapter {

		
		public void mouseClicked(MouseEvent e) {
			
			if (inBoxX() != -1 && inBoxY() != -1) {
				revealed[inBoxX()][inBoxY()] = true;
			}
			
			
			if (inBoxX() != -1 && inBoxY() != -1) {
				System.out.println("The mouse is in the [" + inBoxX() + "," + inBoxY() + "], Number of mine neighs: " + neighbors[inBoxX()][inBoxY()]);
			} else {
				System.out.println("The pointer is not inside of any box");
			}
			
			
			//  If the smile face was clicked, then the game would reset itself
			
			if (inSmiley() == true) {
				resetAll();
			}
		}
	}
	
	/**
	 * Game over logic 
	 * Time counter will stop either if the player wins or loses
	 */
	
	
	public void checkVictoryStatus() {
		
		if (defeat == false) {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (revealed[i][j]==true && mines[i][j] == 1) {
					defeat = true;
					happiness = false;
					endDate = new Date();
					showMines();
				}
			}
		}
	}
		
		if (totalBoxesRevealed( ) >= 144 && victory == false) {
			victory = true;
			endDate = new Date();
		}
		
	}
	
	
	public int totalMines() {
		int total = 0;
		for (int i =0 ; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (mines[i][j]==1) {
					total++;
				}
			}
		}
		return total;
	}
	
	public int totalBoxesRevealed() {
		int total = 0;
		for (int i =0 ; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (revealed[i][j]== true) {
					total++;
				}
			}
		}
		return total;
	}
	
	/**
	 * Displays all the mines within the board
	 * The mines are set to a value of 1
	 */
	
	
	public void showMines() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (mines[i][j]==1) {
					revealed[i][j] = true;
				}
			}
		}
	}
	
	/**
	 * The reset method is used to restart the game
	 * The smile face, timer, status of the game is reseted
	 * Mines are randomized again
	 */
	
	
	public void resetAll() {
		
		startDate = new Date();
		
		resetter = true;
		happiness = true;
		victory = false;
		defeat = false;
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				if (rand.nextInt(100) < 10) {
					mines[i][j] = 1;
				} else {
					mines[i][j] = 0;
				}
				revealed[i][j] = false;
				flagged[i][j] = false; 
			}
		}
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 9; j++) {
				neighs = 0;
				for (int m = 0; m < 16; m++) {
					for (int n = 0; n < 9; n++) {
						if (!(m ==i && n == j)) {
							if (isN(i,j,m,n) == true) {
							neighs++;
						}
						}
					}
					neighbors[i][j] = neighs;
				}
			}
		}
		resetter = false;
	}
	
	
	/**
	 * Smile method to return the area of the drawn face
	 * If the difference in the pixels is less than 35, than it will return true.
	 */
	
	public boolean inSmiley() {
		int dif = (int) Math.sqrt(Math.abs(mx - smileyCenterX) *Math.abs(mx - smileyCenterX)+ Math.abs(my - smileyCenterY) *Math.abs(my - smileyCenterY));
				if (dif < 35) {
					return true;
				}
		return false;
	}
	
	
	
	
	public int inBoxX() {
		for (int i = 0; i < 16; i++) 
		{
			for (int j = 0; j < 9; j++)
			{
				if (mx >= spacing + i*80 && mx < spacing + i*80 + 80 - 2* spacing && my >= spacing + j * 80 + 80 + 26 && my < spacing + j* 80 + 26 + 80 + 80 - 2*spacing) {
					return i;
				}
			}
		}
		return -1;
	}
		
	
	public int inBoxY() {
		for (int i = 0; i < 16; i++) 
		{
			for (int j = 0; j < 9; j++)
			{
				if (mx >= spacing + i*80 && mx < spacing + i*80 + 80 - 2* spacing && my >= spacing + j * 80 + 80 + 26 && my < spacing + j* 80 + 26 + 80 + 80 - 2*spacing) {
					return j;
				}
			}
		}
		return -1;
	}
	
	
	public boolean isN(int mX, int mY, int cX, int cY) {
		if (mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1) {
			return true;
		}
		return false;
	}
	
}
