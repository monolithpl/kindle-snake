/*
 * Copyright (c) 2010 AnMiPo
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package com.anmipo.kindle.snake.applet;

import java.awt.BorderLayout;

import com.anmipo.kindle.snake.GamePanel;

//Launches Snake as a Java applet. Useful for debug
public class SnakeApplet extends java.applet.Applet {
	private GamePanel snakePanel;
	
	public void init() {
//		this.setSize(600,760);
		this.setLayout(new BorderLayout(0,0));
		
		snakePanel = new GamePanel();
		this.add(snakePanel, BorderLayout.CENTER);
		this.addKeyListener(snakePanel);
		
		validate();
		setVisible(true);
		repaint();
	}
	
	public void start() {
		snakePanel.init();
		snakePanel.start();
	}
  
	public void stop() {
		snakePanel.stop();
	}
}
