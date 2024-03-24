package com.kj.javacoursework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class Game extends View {
	Context context;
	Handler handler;
	Runnable runnable;
	final long frameTime = 32;
	float start;
	float delta = 0;
	Player player;
	static int dWidth, dHeight;
	int points;
	boolean pressed;

	public Game(Context context) {
		super(context);
		this.context = context;
		Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		dWidth = size.x;
		dHeight = size.y;
		player = new Player(context);
		player.setX(dWidth/4 - player.getWidth()/2);
		player.setY(dHeight - player.getHeight());
		player.setGroundY(player.getY());
		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run(){
				invalidate();
			}
		};
		this.setOnTouchListener(handleTouch);
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

	public void updateState(float delta) {
		player.update(delta, pressed);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		delta = (System.nanoTime() - start) / 1000000000f;
		//Log.d("FrameTime", Float.toString(delta));
		start = System.nanoTime();
		updateState(delta);
		canvas.drawBitmap(player.getSprite(), player.getX(), player.getY(), null);
		handler.postDelayed(runnable, frameTime);
	}
}
