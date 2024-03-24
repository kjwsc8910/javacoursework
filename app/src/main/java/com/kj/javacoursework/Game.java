package com.kj.javacoursework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.zip.Adler32;

public class Game extends View {
	Context context;
	Handler handler;
	Runnable runnable;
	final long frameTime = 8;
	float start;
	float delta = 0;
	Player player;
	boolean alive = true;
	ArrayList<Cactus> cactusList = new ArrayList<Cactus>();
	float cactusTimer = 2f;
	float speedUp = 1f;
	static int dWidth, dHeight;
	int points;
	Paint textPaint = new Paint();
	int textSize = 150;
	boolean pressed;
	Random random = new Random();

	public Game(Context context) {
		super(context);
		this.context = context;
		Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		dWidth = size.x;
		dHeight = size.y;
		textPaint.setColor(Color.rgb(0, 0, 0));
		textPaint.setTextSize(textSize);
		textPaint.setTextAlign(Paint.Align.CENTER);

		player = new Player(context);
		player.setX(dWidth/4 - player.getWidth()/2);
		player.setY(dHeight - player.getHeight());
		player.setGroundY(player.getY());

		cactusList.add(createCactus());

		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run(){
				invalidate();
			}
		};
		this.setOnTouchListener(handleTouch);
		start = System.nanoTime();
	}

	public Cactus createCactus() {
		Cactus cactus = new Cactus(context);
		cactus.setY(dHeight - cactus.getHeight());
		cactus.setX(dWidth);
		return cactus;
	}

	private View.OnTouchListener handleTouch = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			//int action = event.getAction();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pressed = true;
					Log.d("Input", "Press");
					break;

				case MotionEvent.ACTION_MOVE:
					//User is moving around on the screen
					break;

				case MotionEvent.ACTION_UP:
					pressed = false;
					Log.d("Input", "Release");
					break;
			}
			return pressed;
		}
	};

	public void gameOver() {
		alive = false;
	}

	public void updateState(float delta) {
		if(!alive) {
			player.deathAnimation(delta);
			return;
		}
		player.update(delta, speedUp, pressed);

		if(cactusList.removeIf(cactus -> !cactus.update(delta, speedUp, player, this))) points += 1;
		cactusTimer -= delta;
		if(cactusTimer <= 0) {
			cactusTimer = 1f/speedUp + (random.nextFloat() * 4f)/speedUp;
			cactusList.add(createCactus());
		}

		speedUp += 0.01f * delta;
	}

	public void drawState(Canvas canvas) {
		canvas.drawBitmap(player.getSprite(), player.getX(), player.getY(), null);
		for (Cactus cactus : cactusList) {
			canvas.drawBitmap(cactus.getSprite(), cactus.getX(), cactus.getY(), null);
		}
		canvas.drawText(Integer.toString(points), dWidth/2, dWidth/2, textPaint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		delta = (System.nanoTime() - start) / 1000000000f;
		//Log.d("FrameTime", Float.toString(delta));
		start = System.nanoTime();
		updateState(delta);
		drawState(canvas);
		handler.postDelayed(runnable, frameTime);
	}
}
