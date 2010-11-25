/*
 * Copyright (c) 2010 AnMiPo
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package com.anmipo.kindle.snake;

import java.awt.BorderLayout;
import java.awt.Container;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.ui.KindletUIResources;
import com.amazon.kindle.kindlet.ui.KindletUIResources.KFontFamilyName;

public class SnakeKindlet extends AbstractKindlet {
	
	private KindletContext context;
	private GamePanel snakePanel;
	private Container rootContainer;
	
	public void create(KindletContext context) {
		this.context = context;
		rootContainer = context.getRootContainer();
		
		snakePanel = new GamePanel();
		rootContainer.add(snakePanel, BorderLayout.CENTER);
		rootContainer.setFocusTraversalKeysEnabled(false);
		rootContainer.addKeyListener(snakePanel);
		snakePanel.setFocusTraversalKeysEnabled(false);
		snakePanel.addKeyListener(snakePanel);
		
		KindletUIResources uiRes = context.getUIResources();
		snakePanel.setTitleFont(uiRes.getFont(KFontFamilyName.SANS_SERIF, 48));
		snakePanel.setSubTitleFont(uiRes.getFont(KFontFamilyName.SANS_SERIF, 20));
	}
	
	public void start() {
		snakePanel.init();

		rootContainer.validate();
		rootContainer.setVisible(true);
		rootContainer.repaint();

		snakePanel.requestFocus();
		snakePanel.start();
	}
	
	public void stop() {
		snakePanel.stop();
	}
}
