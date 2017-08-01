/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		
		//Sets up and plays Game
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);	
		setup();
		playGame();
		
		//Finds Winner
		if(brickCounter== 0) {
			removeAll();
			printWinningMessage();
		}
		
		//Finds loser
		if(brickCounter > 0) {
			ball.setVisible(false);
			paddle.setVisible(false);
			printLosingMessage();
		}
		
	}
	
	
	//This method initiates the setup of the game
	private void setup() {
		
		//Initiates the x location of first brick 
		int x = ((getWidth() - BRICK_WIDTH*NBRICKS_PER_ROW) / 2) - BRICK_WIDTH/2;
		
		//Initiates the y location of gameBoard
		int y = BRICK_HEIGHT;
				
			
		
		/*
		 * The nested for loops creates a 10 by 10 set of bricks with different colors in the 
		 * atari breakout game. At the end of each loop, the values of 
		 * x and y are incremented to allow for stacking of the 
		 * Bricks! :))
		 */
		
		
		//Adds Rows
			for(int row = 0; row< NBRICK_ROWS; row++) {
				for(int i = 0; i<NBRICKS_PER_ROW; i++) {
					rect = new GRect (x, y + BRICK_Y_OFFSET, BRICK_WIDTH, BRICK_HEIGHT);
					rect.setFilled(true);
					add(rect);
					x+= (BRICK_WIDTH + BRICK_SEP);
					
					
					if(row < 2) {
						rect.setFillColor(Color.RED);
					}
					
					if(row == 2 || row == 3) {
						rect.setFillColor(Color.ORANGE);
						
					}
					
					if(row == 4 || row == 5) {
						rect.setFillColor(Color.YELLOW);
					}
					
					if(row == 6 || row == 7) {
						rect.setFillColor(Color.GREEN);
					}
					
					if(row == 8 || row == 9) {
						rect.setFillColor(Color.CYAN);
					}
				}
				//Allows for next row to be stacked
				y+= (BRICK_HEIGHT + BRICK_SEP);
				x-= ((BRICK_WIDTH + BRICK_SEP) *NBRICKS_PER_ROW);
				
				
				
			}
			
			
			
			//Adds paddle
			paddle = new GRect ((getWidth() - PADDLE_WIDTH) / 2, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
			paddle.setFilled(true);
			add(paddle);
			addMouseListeners();
			
			//Adds Ball
			ball = new GOval ((WIDTH - 2*BALL_RADIUS) / 2, getHeight() / 2, 2*BALL_RADIUS, 2*BALL_RADIUS);
			ball.setFilled(true);
			add(ball);
			
			
			
			
	}
	
	
	//ADD MOUSE LISTENER HERE!!
	public void mouseMoved(MouseEvent e) {
		if(e.getX() + PADDLE_WIDTH/2 < APPLICATION_WIDTH && e.getX() - PADDLE_WIDTH/2 > 0) {
			double y_paddle_location = (double) getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT;
			paddle.setLocation( e.getX() - (60 / 2), y_paddle_location);
		}
		
	}
	
	private void playGame() {
		waitForClick();
		
		
		//Defines the values for the velocity of the ball in x and y direction
				vy = +3.0;
				vx = rgen.nextDouble(1.0, 3.0); 
				if (rgen.nextBoolean(0.5)) vx = -vx;
	
				

		//Moves the ball while path is clear		
		while(true) {
			
			ball.move(vx, vy);
			pause(10);
			if(ball.getX() + 2*BALL_RADIUS > APPLICATION_WIDTH || ball.getX() < 0) {
				vx = -vx;
			}
			
			if(ball.getY() < 0) {
				vy = -vy;
			}

			
			
			//Checks for Collisions
			GObject collider = getCollidingObject();
			if(collider == paddle) {
				//Adds incrementor
				if(counter % 7 == 0 && counter > 0) {
					vx += .25;
					vy += .25;
				}
				println("I found " + collider);
				counter++;
				println(counter);
				vy = -vy;
			}
			
			else if(collider != null) {
				println("I found " + collider);
				println(brickCounter + " bricks left");
				remove(collider);
				vy = -vy;
				brickCounter--;
				bounceClip.play();
				}
			
			
			
			
			//Finds the loser
			if(ball.getY() + 2*BALL_RADIUS >= APPLICATION_HEIGHT) {
				vy= -vy;
				ball.setVisible(false);
				paddle.setVisible(false);
				counter = 0;
				break;
			}
			
			
		}
		
	}
	
	private GObject getCollidingObject() {
		double x = ball.getX();
		double y = ball.getY();
		if(getElementAt(x, y) != null) {
			return getElementAt(x, y);
		} 
		else if(getElementAt(x + 2*BALL_RADIUS, y) != null) {
			return getElementAt(x + 2*BALL_RADIUS, y);
		}
		else if(getElementAt(x, y + 2*BALL_RADIUS) != null) {
			return getElementAt(x, y + 2*BALL_RADIUS);
		}
		else if(getElementAt(x + 2*BALL_RADIUS, y + 2*BALL_RADIUS) != null) {
			return getElementAt(x + 2*BALL_RADIUS, y + 2*BALL_RADIUS);
		}
		else{
			return null;
		}
		
	}
	
	private void printWinningMessage() {
		GLabel winner = new GLabel("Congratulations, You Win!", getWidth()/2, getHeight()/2);
		winner.move(-winner.getWidth(), 0);
		winner.setFont("SansSerif-26");
		winner.setColor(Color.BLUE);
		add(winner);
	}
	
	private void printLosingMessage() {
		GLabel loser = new GLabel("You Lose :(, try again?", getWidth()/2, BRICK_Y_OFFSET/2);
		loser.move(-loser.getWidth(), 0);
		loser.setFont("SansSerif-26");
		loser.setColor(Color.BLUE);
		add(loser);
	}
	
	
	
	
	
	
	/* private instance variables
	 * 
	 */
	private GRect paddle;
	private GRect rect;
	private GOval ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int brickCounter = 99;
	private int counter = 0;
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au"); 
}
