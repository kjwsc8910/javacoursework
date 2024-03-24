package com.kj.javacoursework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Player {
	private Bitmap sprite;
	private boolean alive = true;
	private float posX, posY, groundY, velocity, maxVelocity = -3000f, gravity = -5000f;
	private boolean grounded;

	public Player(Context context) {
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.dinosaur);
		sprite = Bitmap.createScaledBitmap(
				sprite, 128, 128, false);
		grounded = true;
	}

	public void update(float delta, float speedUp, boolean pressed) {
		if (posY >= groundY) {
			posY = groundY;
			grounded = true;
		}

		if(grounded == false) {
			velocity += gravity * delta * speedUp;
			if(velocity < maxVelocity) velocity = maxVelocity;
			if(posY - velocity * delta * speedUp < groundY) {
				posY -= velocity * delta * speedUp;
			} else {
				posY = groundY;
			}
		}

		if((grounded == true) && (pressed == true)) {
			Log.d("Action", "Jump");
			velocity = 2000f;
			posY -= velocity * delta * speedUp;
			grounded = false;
		}
	}

	public void deathAnimation(float delta) {
		if (alive) velocity = 2000f;
		alive = false;

		posY -= velocity * delta;
		velocity += gravity * delta;
	}

	public void setX(float x) {
		posX = x;
	}

	public void setY(float y) {
		posY = y;
	}

	public void setGroundY(float y) {
		groundY = y;
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	public float getX() {
		return posX;
	}

	public float getY() {
		return posY;
	}

	public Bitmap getSprite() {
		return sprite;
	}
}
