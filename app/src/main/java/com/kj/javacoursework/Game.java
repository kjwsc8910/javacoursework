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
	final long frameTime = 16;
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
		player = new Player(context, dWidth/4 - player.getWidth()/2, dHeight - player.getHeight());
		handler = new Handler();
		runnable = new Runnable() {
			@Override
			public void run(){
				invalidate();
			}
		};
		super.setOnTouchListener(handleTouch);
	}

	private View.OnTouchListener handleTouch = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			//int action = event.getAction();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					pressed = true;
					Log.d("Test", "Press");
					break;

				case MotionEvent.ACTION_MOVE:
					//User is moving around on the screen
					break;

				case MotionEvent.ACTION_UP:
					pressed = false;
					break;
			}
			return pressed;
		}
	};

	public void updateState() {
		player.update(pressed);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		updateState();
		canvas.drawBitmap(player.getSprite(), player.getX(), player.getY(), null);
		handler.postDelayed(runnable, frameTime);
	}
}
