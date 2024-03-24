package com.kj.javacoursework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Player {
	private Bitmap sprite;
	private int posX, posY, groundY, velocity, maxVelocity = -50, gravity = -10;
	private boolean grounded;

	public Player(Context context, int x, int y) {
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.dinosaur);
		sprite = Bitmap.createScaledBitmap(
				sprite, 128, 128, false);
		posX = x;
		posY = y;
		groundY = y;
		grounded = true;
	}

	public void update(boolean pressed) {
		if (posY <= groundY) {
			posY = groundY;
			grounded = true;
		}

		if(grounded == false) {
			velocity -= gravity;
			if(velocity < maxVelocity) velocity = maxVelocity;
			posY =+ velocity;
		}

		if(grounded == true && pressed == true) Log.d("Test", "Jump");
	}

	public void setX(int x) {
		posX = x;
	}

	public void setY(int y) {
		posY = y;
	}

	public int getWidth() {
		return sprite.getWidth();
	}

	public int getHeight() {
		return sprite.getHeight();
	}

	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public Bitmap getSprite() {
		return sprite;
	}
}
