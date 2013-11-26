package com.babson.sssi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

public class Laser
{
	private static final Laser LASER = new Laser();
	private Bitmap bitmap;
	private float width;
	private float height;
	private float x;
	private float y;
	private float speed;
	private boolean firing;
	
	private Laser() {
		this.speed = World.LASER_SPEED;
	}

	public static Laser getLaser() {
		return Laser.LASER;
	}

	public float getY() {
		return this.y;
	}

	public void update() {
		if(this.firing) {
			this.y -= speed;
		}
	}

	public void shoot(float x) {
		if(!this.firing) {
			this.x = x;
			this.firing = true;
		}
	}
	
	public void reload(float y) {
		this.firing = false;
		this.y = y;
	}

	public boolean impact(Alien[] aliens) {
		for(Alien alien : aliens) {
			RectF hitArea = new RectF(alien.getX(), alien.getY(), alien.getX() + alien.getWidth(), alien.getY() + alien.getHeight());
			if(hitArea.contains(this.x, this.y)) {
				alien.destroy();
				return true;
			}
		}
		return false;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
	}

	public void draw(Canvas canvas) {
		if(canvas != null) {
			canvas.drawBitmap(bitmap, this.x, this.y, null);
		}
	}
}