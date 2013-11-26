package com.babson.sssi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class World extends Activity
{
	public static final int BORDER = 15;

	public static final int ROW_NUMBER = 3;
	public static final int COL_NUMBER = 8;

	public static final int ALIEN_STRENGTH = 2;
	public static final float ALIEN_SPEED = 15f;
	public static final long ALIEN_ADVANCE = 3000;

	public static final int LASER_DAMAGE = 1;
	public static final float LASER_SPEED = 25f;

	public static boolean victory = false;

	public void onCreate(Bundle savedInstanceState) {
		System.out.println(LASER_SPEED);
		super.onCreate(savedInstanceState);
		setContentView(new GameSurface(this));
	}
}