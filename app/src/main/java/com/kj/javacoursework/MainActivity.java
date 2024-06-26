package com.kj.javacoursework;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvHighScore;
    TextView tvLastScore;
    SharedPreferences sharedPreferences;

    // Constructor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);                                            // Remove boarders
        setContentView(R.layout.activity_main);                                                 // Displays main menu xml
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load data from shared preferences
        sharedPreferences = getSharedPreferences("my_pref", 0);
        int highScore = sharedPreferences.getInt("highScore", 0);
        int lastScore = sharedPreferences.getInt("lastScore", 0);
        tvHighScore = findViewById(R.id.ma_txt_highScore);
        tvLastScore = findViewById(R.id.ma_txt_lastScore);
        tvHighScore.setText(Integer.toString(highScore));       // Display the high score
        tvLastScore.setText(Integer.toString(lastScore));       // Display the last score
    }

    // Loads the game
    public void startGame(View view) {
        Log.d("Menu", "Test");
        Game game = new Game(this);
        setContentView(game);
    }
}