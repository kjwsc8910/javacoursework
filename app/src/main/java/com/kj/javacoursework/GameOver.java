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


	// Constructor
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		Log.d("Test", "GameOver");
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);								// Remove boarders
		setContentView(R.layout.game_over);											// Loads the game over xml
		tvPoints = findViewById(R.id.go_txt_points);								// Gets the points text
		tvHighScoreAlert = findViewById(R.id.go_txt_highScoreAlert);				// Gets the high score alert text
		int points = getIntent().getExtras().getInt("points");					// Retrieves the points from the intent
		tvPoints.setText(Integer.toString(points));									// Sets the text to show the points
		sharedPreferences = getSharedPreferences("my_pref", 0);			// Loads the shared preferences
		int highScore = sharedPreferences.getInt("highScore", 0);		// Loads the high score from shared preferences
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if(points > highScore) {													// Updates high score when needed
			editor.putInt("highScore", points);
			tvHighScoreAlert.setText("New High Score!");
		}
		editor.putInt("lastScore", points);											// Stores the current score in the shared preferences
		editor.commit();															// Commits the changes
	}

	// Loads the main menu
	public void mainMenu(View view)  {
		Intent intent = new Intent(GameOver.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
