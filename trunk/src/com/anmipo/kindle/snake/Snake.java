/*
 * Copyright (c) 2010 AnMiPo
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package com.anmipo.kindle.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Snake {
	public static Color SNAKE_COLOR = Color.BLACK;
	
	private LinkedList body;	//snake body segments
	private int dir;		//the actual direction of movement
	private int tmpDir;		//proposed direction, the user can change it before the move()
	private boolean grown;	//true when ate an apple, next move() resets to false
	private int score;
	
	public Snake() {
		body = new LinkedList(); 
		dir = Direction.UP;
		tmpDir = dir;
		grown = false;
		score = 0;
	}
	
	public void init() {
		dir = Direction.UP;
		body.clear();
		body.addFirst(new Point(0, 0));
		body.addFirst(new Point(1, 0));
		body.addFirst(new Point(2, 0));
		score = 0;
	}

	public boolean isHeadAt(Point pt) {
		return getHead().equals(pt);
	}

	public void moveBy(int x, int y) {
		Point newHead = new Point(getHead());
		newHead.translate(x, y);
		body.addFirst(newHead);
		if (!grown) {
			body.removeLast();
		}
		grown = false; //reset 'grown' flag
	}

	public void paint(Graphics gr, int cellSize) {
		final int d = 2;
		gr.setColor(SNAKE_COLOR);
		Iterator it = body.iterator();
		while (it.hasNext()) {
			Point pt = (Point) it.next();
			gr.fillRect(pt.x * cellSize + d, pt.y * cellSize + d, cellSize - 2*d, cellSize - 2*d);		
//			gr.fillOval(pt.x * cellSize, pt.y * cellSize, cellSize, cellSize);		
		}
	}

	protected Point getHead() {
		return (Point)body.getFirst();
	}
	protected Point getBody(int i) {
		return (Point)body.get(i);
	}

	//true if the snake head collides with its body
	public boolean isSelfCollision() {
		final int START_FROM = 4;
		boolean result = false;
		if (body.size()>START_FROM) {
			Point head = getHead();
			ListIterator it = body.listIterator(START_FROM);
			while (it.hasNext())
				if (head.equals(it.next())) {
					result = true;
					break;
				}
		}
		return result;
	}

	public boolean isWithinBounds(int x, int y, int width, int height) {
		int headX = getHead().x;
		int headY = getHead().y;
		return (headX >= x) && (headX < width) && (headY >= y) && (headY < height);
	}
	
	public class Direction {
		public static final int RIGHT = 0; 
		public static final int LEFT = 1; 
		public static final int UP = 2; 
		public static final int DOWN = 3; 
	}

	public void move() {
		dir = tmpDir;
		switch (dir) {
		case Direction.RIGHT:
			moveBy(1, 0);
			break;
		case Direction.LEFT:
			moveBy(-1, 0);
			break;
		case Direction.UP:
			moveBy(0, -1);
			break;
		case Direction.DOWN:
			moveBy(0, 1);
			break;
		}
	}

	public boolean canGoToDirection(int dir) {
		boolean result = false;
		switch (dir) {
		case Direction.RIGHT:
			result = (getBody(1).x != getHead().x + 1);
			break;
		case Direction.LEFT:
			result = getBody(1).x != getHead().x - 1;
			break;
		case Direction.UP:
			result = getBody(1).y != getHead().y - 1;
			break;
		case Direction.DOWN:
			result = getBody(1).y != getHead().y + 1;
			break;
		}
		return result;
	}
	public void setDirection(int newDir) {
		if (this.dir!=newDir && canGoToDirection(newDir)) {
			this.tmpDir = newDir;
		}
	}

	public void grow() {
		grown = true;
		score++;
	}

	public int getScore() {
		return score;
	}
}
