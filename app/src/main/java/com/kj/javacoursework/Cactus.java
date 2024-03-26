package com.kj.javacoursework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Cactus {

	private Bitmap sprite;
	private float posX, posY, speed;	// Position and Speed of the cacti


	// Constructor
	public Cactus(Context context) {
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.cactus);	// Set bitmap
		sprite = Bitmap.createScaledBitmap(													// Resize bitmap
				sprite, 64, 128, false);
		speed = 600f;																		// Set speed
	}

	// Update the position of the cactus and detect if it collides with the player
	public boolean update(float delta, float speedUp,Player player, Game game) {
		if(posX <= -sprite.getWidth()) return false;		// Returns false when off screen which is used to remove the object
		posX -= speed * delta * speedUp;					// Updates its position
		float distX = player.getX() - posX;					// Gets the X distance to player
		float distY = player.getY() - posY;					// Gets the Y distance to player
		if((distX >= -player.getWidth()) &&					// Checks the distances are within the sprite sizes
				(distX <= getWidth()) &&
				(distY >= -getHeight())) game.gameOver();	// Calls the game over function
		return true;										// Returns true to prevent it from being removed from the game
	}

	// Getters and setters
	public void setX(float x) {
		posX = x;
	}

	public void setY(float y) {
		posY = y;
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
