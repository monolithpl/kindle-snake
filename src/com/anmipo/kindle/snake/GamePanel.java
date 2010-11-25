/*
 * Copyright (c) 2010 AnMiPo
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package com.anmipo.kindle.snake;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.amazon.kindle.kindlet.event.KindleKeyCodes;
import com.anmipo.kindle.snake.resources.Messages;

public class GamePanel extends Component implements KeyListener {
	private static final long FRAME_DELAY = 150;	//delay between repaints
	private static final int CELL_SIZE = 20;	//size of a field cell, in pixels
	
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static final Color TEXT_COLOR = Color.BLACK;
	private static final Color APPLE_COLOR = Color.LIGHT_GRAY;
	
	private Font titleFont = new Font("Arial", Font.BOLD, 48);	//default font for splash screens
	private Font subtitleFont = new Font("Arial", Font.BOLD, 20);	//default font for scores

	private Image bufferImage = null;	//painting buffer
	private Graphics bufferGraphics;	//...and its graphics
	
	Snake snake;
	Point apple = new Point(12, 7);
	Thread updater;		//game timer
	int highScore = 0;

	private int fieldWidth;		//horizontal field size, in cells
	private int fieldHeight;	//vertical field size, in cells
	
	boolean game = false;
	boolean paused = false;
	boolean isQuitRequested = false;

	public GamePanel() {
		super();
		snake = new Snake();
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent arg) {
				fieldWidth = getWidth()/CELL_SIZE;
				fieldHeight = getHeight()/CELL_SIZE;
				reinitPaintBuffer();
				repaint();
			}
		});
	}
	
	public void setTitleFont(Font font) {
		titleFont = font;
	}
	public void setSubTitleFont(Font font) {
		subtitleFont = font;
	}

	//(re)initializes the buffer image for double-buffered painting
	private synchronized void reinitPaintBuffer() {
		disposePaintBuffer();
		bufferImage = createImage(getWidth(), getHeight());
		bufferGraphics = bufferImage.getGraphics();
	}
	
	private synchronized void disposePaintBuffer() {
		if (bufferGraphics!=null) { 
			bufferGraphics.dispose();
			bufferGraphics = null;
		}
		if (bufferImage!=null) {
			bufferImage.flush();
			bufferImage = null;
		}
	}

	public void init() {
		snake.init();
	}

	public void start() {
		if (updater == null) {
			updater = new UpdaterThread();
			updater.start();
		}
	}

	public void stop() {
		isQuitRequested = true;
		if (updater != null) {
			try {
				updater.join(1000);
			} catch (InterruptedException ignored) {
			}
			updater = null;
		}
		disposePaintBuffer();
	}

	public void paint(Graphics gr) {
		//painting the buffered image
		gr.drawImage(bufferImage, 0, 0, null);
	}
	public void update(Graphics gr) {
		paint(gr);
	}
	
	//paint game field to the buffer image
	protected synchronized void paintBuffer() {
		//Clear the field
		bufferGraphics.setColor(BACKGROUND_COLOR);
		bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
		bufferGraphics.setColor(TEXT_COLOR);
		bufferGraphics.drawRect(0, 0, fieldWidth *CELL_SIZE-1, fieldHeight *CELL_SIZE-1);
		
		// draw the apple and new body
		drawApple(bufferGraphics, apple, APPLE_COLOR, CELL_SIZE);
		snake.paint(bufferGraphics, CELL_SIZE);
		
		//draw the score
		drawScore(bufferGraphics);

		if (paused) {
			drawPausedSplash(bufferGraphics);
		}
	}
	
	//paints current score
	protected void drawScore(Graphics gr) {
		FontMetrics metrics = gr.getFontMetrics(subtitleFont);
		
		gr.setColor(TEXT_COLOR);
		gr.setFont(subtitleFont);
		String scoreStr = Messages.SCORE + snake.getScore();
		gr.drawString(scoreStr, 
				getWidth() - metrics.stringWidth(scoreStr) - 10, 
				metrics.getHeight());
	}

	//paints "paused" splash screen
	protected void drawPausedSplash(Graphics gr) {
		FontMetrics titleMetrics = gr.getFontMetrics(titleFont);
		FontMetrics subTitleMetrics = gr.getFontMetrics(subtitleFont);
		
		gr.setColor(TEXT_COLOR);
		gr.setFont(titleFont);
		gr.drawString(Messages.PAUSED, 
				(getWidth()-titleMetrics.stringWidth(Messages.PAUSED))/2, 
				getHeight()/2);
		gr.setFont(subtitleFont);
		gr.drawString(Messages.PRESS_ARROW_KEY_TO_CONTINUE, 
				(getWidth()-subTitleMetrics.stringWidth(Messages.PRESS_ARROW_KEY_TO_CONTINUE))/2, 
				getHeight()/2 + subTitleMetrics.getHeight());
	}

	protected void drawApple(Graphics gr, Point pt, Color color, int cellSize) {
		gr.setColor(color);
		gr.fillOval(pt.x * cellSize, pt.y * cellSize, cellSize, cellSize);
	}
	
	public void keyPressed(KeyEvent key) {
		if (isRightKey(key)) {
			snake.setDirection(Snake.Direction.RIGHT);
			game = true;
			paused = false;
		} else if (isLeftKey(key)) {
			snake.setDirection(Snake.Direction.LEFT);
			game = true;
			paused = false;
		} else if (isUpKey(key)) {
			snake.setDirection(Snake.Direction.UP);
			game = true;
			paused = false;
		} else if (isDownKey(key)) {
			snake.setDirection(Snake.Direction.DOWN);
			game = true;
			paused = false;
		} else if (isPauseKey(key)) {
			paused = true;
		}
	}
	private boolean isPauseKey(KeyEvent key) {
		return (key.getKeyCode() == KindleKeyCodes.VK_FIVE_WAY_SELECT) ||
			(key.getKeyCode() == KeyEvent.VK_SPACE);
	}
	private boolean isUpKey(KeyEvent key) {
		return (key.getKeyCode() == KindleKeyCodes.VK_FIVE_WAY_UP) ||
			(key.getKeyCode() == KeyEvent.VK_UP);
	}
	private boolean isDownKey(KeyEvent key) {
		return (key.getKeyCode() == KindleKeyCodes.VK_FIVE_WAY_DOWN) ||
			(key.getKeyCode() == KeyEvent.VK_DOWN);
	}
	private boolean isLeftKey(KeyEvent key) {
		return (key.getKeyCode() == KindleKeyCodes.VK_FIVE_WAY_LEFT) ||
			(key.getKeyCode() == KeyEvent.VK_LEFT);
	}
	private boolean isRightKey(KeyEvent key) {
		return (key.getKeyCode() == KindleKeyCodes.VK_FIVE_WAY_RIGHT) ||
			(key.getKeyCode() == KeyEvent.VK_RIGHT);
	}

	public void keyReleased(KeyEvent arg0) {
		//nothing to do
	}
	public void keyTyped(KeyEvent arg0) {
		//nothing to do
	}

	//periodically updates the state machine
	private class UpdaterThread extends Thread {
		public void run() {
			while (!isQuitRequested) {
				if (snake.isSelfCollision() || !snake.isWithinBounds(0, 0, fieldWidth, fieldHeight)) {
					//ate self or bumped into a wall - reinit everything
					game = false;
					if (snake.getScore() > highScore)
						highScore = snake.getScore();
					snake.init();
					apple.setLocation(fieldWidth/2, fieldHeight/2);
				}
				if (game) {
					if (snake.isHeadAt(apple)) {
						//ate an apple - grow up :)
						snake.grow();
						//update apple position
						apple.x = (int) (Math.random() * fieldWidth);
						apple.y = (int) (Math.random() * fieldHeight);
					}

					if (!paused)
						snake.move();	//moving the snake forward
				}
				
				paintBuffer();
				repaint();
				
				try {
					sleep(FRAME_DELAY);
				} catch (InterruptedException ignored) {
				}
			}
		}

	}
}
