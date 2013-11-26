package com.babson.sssi;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

// This is the game thread class (which is just a regular old Java thread) and it handles the constant updating of the screen and the objects
public class GameThread extends Thread
{
	private boolean runState; // This for the thread to know if it should be running or not
	private SurfaceHolder surfaceHolder; // This is for the surface holder passed from the surface view
	private GameSurface gameSurface; // This is the surface view

	// Constructor, we give the game surface and the holder that goes with it
	public GameThread(SurfaceHolder surfaceHolder, GameSurface gameSurface) {
		super(); // Call the Thread constructor to make the thread
		this.surfaceHolder = surfaceHolder;
		this.gameSurface = gameSurface;
		Alien.lastMove = System.currentTimeMillis(); // Manually set the aliens last move to the startime of the thread to prevent the aliens from "jumping the gun"
	}

	// Change the run state of the thread, used to switch the thread on or off
	public void setRunState(boolean runState) {
		this.runState = runState;
	}

	// The actual run method which is called in rapid update intervals once the thread is started (like an engine)
	public void run() {
		Canvas canvas;
		while(runState == true) {
			canvas = null;
			try {
				canvas = this.surfaceHolder.lockCanvas(); // If we can get the canvas, then update the game surface and render on the canvas
				synchronized (surfaceHolder) {
					this.gameSurface.update();
					this.gameSurface.render(canvas);
				}
			} finally {
				if(canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas); // If we somehow lose control of the canvas or there is an error, just unlock it and output everything we've gots
				}
			}
		}
	}
}