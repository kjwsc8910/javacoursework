package com.kj.javacoursework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

	TextView tvPoints;
	TextView tvHighScoreAlert;
	SharedPreferences sharedPreferences;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		Log.d("Test", "GameOver");
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.game_over);
		tvPoints = findViewById(R.id.go_txt_points);
		tvHighScoreAlert = findViewById(R.id.go_txt_highScoreAlert);
		int points = getIntent().getExtras().getInt("points");
		tvPoints.setText(Integer.toString(points));
		sharedPreferences = getSharedPreferences("my_pref", 0);
		int highScore = sharedPreferences.getInt("highScore", 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if(points > highScore) {
			editor.putInt("highScore", points);
			tvHighScoreAlert.setText("New High Score!");
		}
		editor.putInt("lastScore", points);
		editor.commit();
	}

	public void mainMenu(View view)  {
		Intent intent = new Intent(GameOver.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
