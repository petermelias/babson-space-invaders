package com.babson.sssi;

import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback
{
	private GameThread thread;

	private Alien[] aliens;

	private Laser laser;

	private int aliensRemaining;

	// The game SurfaceView is where we put all of our stuff, the drawings, the coordinates etc...
	// It also handles our events, like user touches and collisions etc...
	public GameSurface(Context context) {
		super(context); // Calls the SurfaceView constructor using the given context
		getHolder().addCallback(this); // This just makes it so that when there is an event on the surface holder, it calls back to this class

		aliensRemaining = World.COL_NUMBER * World.ROW_NUMBER; // A value representing the number of aliens left
		aliens = new Alien[aliensRemaining]; // The array holding our aliens that are about to be made

		// This is a nested for-loop construct, basically, it is 2 for-loops, one inside the other.
		// The first loop creates each row of aliens, and the second loop fills that row with an alien
		// The length of each row and the number of rows come from our static "constants" in the World class
		for(int i = 0; i < World.ROW_NUMBER; i++) {
			for(int z = 0; z < World.COL_NUMBER; z++) {
				int alienNumber = z + (i * World.COL_NUMBER); // This calculates the incremental alien number based on it's ROW/COL position, could easily use a counter for this but we like to get fancy in Java
				// This creates a new alien and adds it to the alien array at the index corresponding to the alien number (incremental)
				this.aliens[alienNumber] = new Alien(BitmapFactory.decodeResource(getResources(), R.drawable.alien), z, i, (float)World.ALIEN_SPEED, World.ALIEN_STRENGTH);
			}
		}

		// Make our giant laser
		laser = Laser.getLaser();
		
		// Make a game thread to run the game, pass in this game surface
		thread = new GameThread(getHolder(), this);
		
		// Allow this surface view to be focusable by the user (touches, drags etc...)
		setFocusable(true);
	}

	// This method is automatically run when the surface is changed, we don't use it currently
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	// This method is run ONCE at the time of the surface's creation, we use it to setup the laser and start the thread
	public void surfaceCreated(SurfaceHolder holder) {
		laser.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.laser));
		laser.reload(getHeight());

		// Tell the thread it is going to run
		thread.setRunState(true);
		// Start it running
		thread.start();
	}

	// This is automatically called when the surface is destroyed, we use this method to gracefully shut down our thread in case the user quits the game
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean shutdown = false;
		while(!shutdown) {
			try {
				thread.join();
				shutdown = true;
			} catch(InterruptedException e) {
				// Thread failed to shutdown, it will continue to try and shutdown.
			}
		}
	}

	// This is called if the user touches the screen in anyway
	public boolean onTouchEvent(MotionEvent event) {
		// The only event we are handling is a simple press, this is identified using the ACTION_DOWN constant from the MotionEvent class (built into Android)
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			laser.shoot(event.getX()); // If the user presses on the screen, fire a laser from where they put their finger
			return true;
		}
		return false;
	}

	// This is the "global" update method. Meaning it calls all of the individual update methods on the objects in the game
	public void update() {
		// If the laser has missed the aliens, reload it so the user can fire again
		if(laser.getY() < 0) {
			laser.reload(getHeight());
		}

		// If the laser has hit an alien, reload the laser, decrease the aliens remaining so we can keep track if the user kills all aliens
		if(laser.impact(aliens)) {
			laser.reload(getHeight());
			aliensRemaining--;
		}

		// If the user has just killed the last alien, award the user with victory
		if(aliensRemaining <= 0) {
			World.victory = true; // The World victory variable to TRUE
			thread.setRunState(false); // Shutdown the game thread so that updates and drawing stops
		}

		// This advances the aliens based on time
		long current = System.currentTimeMillis(); // Current time
		long elapsed = current - Alien.lastMove; // Last time the aliens moved
		if(elapsed >= World.ALIEN_ADVANCE) { // If they are overdue for a move, move them.
			for(Alien alien : this.aliens) {
				alien.update(); // Loops through all aliens and moves them
			}
			Alien.lastMove = current; // Marks the current time as the aliens having moved
		}

		laser.update(); // Updates the laser, laser flies continuously at a constant speed
	}

	// The render method does all of our drawing, it also calls the individual draw methods on objects such as Aliens and the Laser
	public void render(Canvas canvas) { // We need to give the render method a canvas to draw on, we get this from the surface holder
		if(canvas != null) {
			if(World.victory) { // If the player has won, paint the board black, display some VICTORY text and then quit
				Paint victoryPaint = new Paint();
				victoryPaint.setARGB(100, 0, 255, 0);
				canvas.drawColor(Color.BLACK);
				canvas.drawText("VICTORY!", getWidth() / 2 - 25, getHeight() / 2 - 5, victoryPaint);
			} else { // If the player has not won, keep drawing aliens and lasers
				canvas.drawColor(Color.BLACK);
				for (Alien alien : aliens) {
					alien.draw(canvas);
				}
			}
			laser.draw(canvas);
		}
	}
}