package com.kj.javacoursework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Cactus {

	private Bitmap sprite;
	private float posX, posY, speed;


	public Cactus(Context context) {
		sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.cactus);
		sprite = Bitmap.createScaledBitmap(
				sprite, 128, 128, false);
		speed = 600f;
	}

	public boolean update(float delta, float speedUp,Player player, Game game) {
		if(posX <= -sprite.getWidth()) return false;
		posX -= speed * delta * speedUp;
		float distX = player.getX() - posX;
		float distY = player.getY() - posY;
		if((distX >= -player.getWidth()) &&
				(distX <= getWidth()) &&
				(distY >= -getHeight())) game.gameOver();
		return true;
	}

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
