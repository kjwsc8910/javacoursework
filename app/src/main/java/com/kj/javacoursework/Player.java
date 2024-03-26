package com.kj.javacoursework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;

public class Player {
	private Bitmap sprites[] = new Bitmap[4];
	private Bitmap sprite;
	private boolean alive = true;	// Tracks the status of the player

	// Stores the position of the character, ground and the velocity, max velocity and gravity used in the physics calculations
	private float posX, posY, groundY, velocity, maxVelocity = -3000f, gravity = -5000f;
	private boolean grounded;	// Tracks weather the player is touching the ground
	private MediaPlayer jumpSound;		// Sound for when the player jumps
	private float animTime = 0.25f;
	private int animFrame = 0;

	// Constructor
	public Player(Context context) {
		sprites[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.dinosaur2);	// Set the bitmap
		sprites[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.dinosaur3);
		sprites[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.dinosaur);
		sprites[0] = Bitmap.createScaledBitmap(														// Resize the bitmap
				sprites[0], 128, 128, false);
		sprites[1] = Bitmap.createScaledBitmap(
				sprites[1], 128, 128, false);
		sprites[2] = Bitmap.createScaledBitmap(
				sprites[2], 128, 128, false);
		grounded = true;										// Set the grounded state
		jumpSound = MediaPlayer.create(context, R.raw.jump);	// Sets the jump sound
		sprite = sprites[animFrame];
	}

	// Updates the players position
	public void update(float delta, float speedUp, boolean pressed) {

		animTime -= delta * speedUp;	// Update the time untill next animation frame

		// Check if the next frame should be displayed
		if(animTime <= 0f && grounded) {
			animFrame += 1;		// Select the next frame in the animation
			if(animFrame >= 2) animFrame = 0;	// Go back to first frame when at the end of the animation
			sprite = sprites[animFrame];
			animTime = 0.25f;	// Restart the timer
		}

		// If on or below ground
		// Place on ground and set its grounded state
		if (posY >= groundY) {
			posY = groundY;
			grounded = true;
		}

		// If not touching the ground, apply physics calculations
		if(grounded == false) {
			velocity += gravity * delta * speedUp;				// Modify its velocity
			if(velocity < maxVelocity) velocity = maxVelocity;	// Ensure it dosnt go over maxVelocity
			if(posY - velocity * delta * speedUp < groundY) {	// Check if its next position will clip with the ground
				posY -= velocity * delta * speedUp;				// Update position
			} else {
				posY = groundY;									// Place on ground if clipping
			}
		}

		// If on the ground, and user is pressing space, Jump
		if((grounded == true) && (pressed == true)) {
			jumpSound.start();						// Plays the jump sound
			Log.d("Action", "Jump");		// Log this to LogCat
			velocity = 2000f;						// Set its velocity
			posY -= velocity * delta * speedUp;		// Update is position
			grounded = false;						// Update its grounded status
			animFrame = 2;							// Set anim to show jump sprite
			sprite = sprites[animFrame];
		}
	}

	// Performs the next frame in its death animation when called consecutively
	public void deathAnimation(float delta) {
		if (alive) velocity = 2000f;	// Checks if this is the first frame based off alive status
		alive = false;					// Updates alive status to prevent initial velocity being set again

		posY -= velocity * delta;		// Updates the position based off the velocity
		velocity += gravity * delta;	// Updates the velocity
	}


	// Getters and Setters
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
