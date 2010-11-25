/*
 * Copyright (c) 2010 AnMiPo
 * Licensed under the MIT license: http://www.opensource.org/licenses/mit-license.php
 */
package com.anmipo.kindle.snake;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

//Represents a game sprite (image)
public class Sprite {
	private Image img;
	
	protected Sprite(Image src) {
		this.img = src;
	}

	public static Sprite load(String url) {
		Sprite result = null;
		
		//TODO: does not work on Kindle
		Image img = Toolkit.getDefaultToolkit().createImage(url);
		if (img!=null)
			result = new Sprite(img);
		return result;
	}
	public int getWidth() {
		return img.getWidth(null);
	}
	public int getHeight() {
		return img.getHeight(null);
	}
	
	public void draw(Graphics gr, int x, int y) {
		gr.drawImage(img, x, y, null);
	}
	public void drawCentered(Graphics gr, int graphicsWidth, int graphicsHeight) {
		draw(gr, (graphicsWidth - getWidth())/2, (graphicsHeight - getHeight())/2);
	}

	public void flush() {
		img.flush();
		img = null;
	}
	public String toString() {
		return "Sprite[width: "+getWidth()+", height: "+getHeight()+"]";
	}
}
