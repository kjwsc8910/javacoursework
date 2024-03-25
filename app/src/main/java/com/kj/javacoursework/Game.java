package com.kj.javacoursework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


public class Game extends View {
	Context context;
	Handler handler;
	Runnable runnable;
	final long frameTime = 8;
	float start;		// Time stamp of last frame
	float delta = 0;	// Frametime in seconds (Used to change from change per frame, to change per second)
	Player player;
	boolean alive = true;	// Alive status of the player
	ArrayList<Cactus> cactusList = new ArrayList<Cactus>();
	float cactusTimer = 2f;	// Time untill next cactus spawns
	float speedUp = 1f;		// Speed modifier to all difficulty change
	static int dWidth, dHeight;	// Width and Height of the device
	int points;				// The players score
	Paint textPaint = new Paint();
	int textSize = 150;		// Size of the score text
	boolean pressed;		// Stores if the player is pressing the screen or not
	Random random = new Random();
	MediaPlayer deathSound;	// Sound for when the player touches a cactus

	// Constructor
	// Sets up the variables for the game loop
	public Game(Context context) {
		super(context);
		this.context = context;

		// Get device size
		Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		dWidth = size.x;
		dHeight = size.y;

		// Set text paint variables
		textPaint.setColor(Color.rgb(0, 0, 0));
		textPaint.setTextSize(textSize);
		textPaint.setTextAlign(Paint.Align.CENTER);

		// Load sound files
		deathSound = MediaPlayer.create(context, R.raw.death);

		// Initialise a player
		player = new Player(context);
		player.setX(dWidth/4 - player.getWidth()/2); 	// Puts the player 1/4 of the screen width
		player.setY(dHeight - player.getHeight()); 		// Puts the player at the bottom of the screen
		player.setGroundY(player.getY()); 				// Saves this position as ground position

		// Initialize a cactus
		cactusList.add(createCactus());

		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run(){
				invalidate();
			}
		};
		this.setOnTouchListener(handleTouch); 	// Add an event listener
		start = System.nanoTime(); 				// Record timestamp
	}

	// Function for creating cacti
	public Cactus createCactus() {
		Cactus cactus = new Cactus(context);
		cactus.setY(dHeight - cactus.getHeight()); 	// Places cactus at the bottom of the screen
		cactus.setX(dWidth); 						// Places cactus offscreen at the right side
		return cactus;
	}

	// On touch even listener, which tracks weather the screen is being pressed
	private View.OnTouchListener handleTouch = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			//int action = event.getAction();
			switch (event.getAction()) {

				// User presses the screen
				case MotionEvent.ACTION_DOWN:
					pressed = true;							// Sets pressed status
					Log.d("Input", "Press");
					break;

				// User is moving around on the screen
				case MotionEvent.ACTION_MOVE:
					break;

				// User releases their finger
				case MotionEvent.ACTION_UP:
					pressed = false;						// Resets the pressed status
					Log.d("Input", "Release");
					break;
			}
			return pressed;
		}
	};

	// Updates the status of the game, plays a death sound, and displays the
	// game over screen after a short duration
	public synchronized void gameOver() {
		alive = false;
		deathSound.start();
		final Handler endGame = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(context, GameOver.class);
				intent.putExtra("points", points);
				context.startActivity(intent);
				((Activity) context).finish();
			}
		}, 3500);

	}

	// Performs or calls functions that update the current state of the game
	// based off of weather the player is a live
	public void updateState(float delta) {

		// Whist the character is dead, plays a death animation
		if(!alive) {
			player.deathAnimation(delta);
			return; // Prevents all other updates during this state
		}

		// Update the player
		player.update(delta, speedUp, pressed);

		// Update the cactus, remove and increment score if off screen
		if(cactusList.removeIf(cactus -> !cactus.update(delta, speedUp, player, this))) points += 1;

		// Updates the time until the next cactus spawns
		// Spawning one when it reaches 0
		cactusTimer -= delta;
		if(cactusTimer <= 0) {
			cactusTimer = 1f/speedUp + (random.nextFloat() * 4f)/speedUp;
			cactusList.add(createCactus());
		}

		speedUp += 0.01f * delta; // Apply the speed up
	}

	// Updates the graphics for the game
	public void drawState(Canvas canvas) {
		canvas.drawBitmap(player.getSprite(), player.getX(), player.getY(), null);
		for (Cactus cactus : cactusList) { // Update each cacti
			canvas.drawBitmap(cactus.getSprite(), cactus.getX(), cactus.getY(), null);
		}
		canvas.drawText(Integer.toString(points), dWidth/2, dWidth/2, textPaint);
	}

	// Event onDraw triggers updateState and drawState to trigger
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		delta = (System.nanoTime() - start) / 1000000000f; 	// Calculate time since last frame
		//Log.d("FrameTime", Float.toString(delta));		// Send timeframe to Log Cat for debugging
		start = System.nanoTime(); 							// Record current time stamp
		updateState(delta);									// Update the game state
		drawState(canvas);									// Draw the game state
		handler.postDelayed(runnable, frameTime);			// Call the next frame after a delay
	}
}
