package com.babson.sssi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class Alien
{
	// Public static variables are accessible from outside the class and do apply to instances, only the class
	public static long lastMove;

	// We declare as many member variables private as possible to encapsulate behavior within the class, using the public methods
	private Bitmap bitmap;
	private int width;
	private int height;
	private float x;
	private float y;

	private float speed;
	private int damageTaken;
	private int strength;
	private boolean isAlive;
	private boolean isErased;

	// This is the constructor, it sets up some variables based on arguments and some static calls to the World class
	public Alien(Bitmap bitmap, int x, int y, float speed, int strength) {
		this.bitmap = bitmap;
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.x = x * (width * 1.5f) + World.BORDER;
		this.y = y * (height * 1.5f) + World.BORDER;
		this.speed = speed;
		this.strength = strength;
		
		// Defaults, so the alien knows that it's alive and to draw itself
		this.isAlive = true;
		this.isErased = false;
	}

	// These "get" methods are so that we can expose these properties to other classes that may need to use them
	// This is considered bad practice if done excessively or without good cause
	// If you find yourself putting in a lot of "get" or "set" methods, consider rethinking your approach to the program's design
	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	// This updates the alien
	public void update() {
		if(isAlive) { // Are we still alive?
			this.y += speed; // Okay, let's advance forward
		}
	}

	public void draw(Canvas canvas) {
		if(isAlive || !isErased) { // Are we still alive or not perhaps dead but erased?
			canvas.drawBitmap(bitmap, this.x, this.y, null); // Okay, draw
		}

		if(!isAlive) { // If we're dead...
			isErased = true; // Then we've just been erased by the line above so let's stop drawing
		}
	}

	// This is how you destroy an alien--
	public void destroy() {
		// Relocate alien to coordinates outside of screen
		this.x = -100f;
		this.y = -100f;
		
		// Inform the alien that it is no longer alive, so it will stop updating and erase itself
		this.isAlive = false;
	}
}