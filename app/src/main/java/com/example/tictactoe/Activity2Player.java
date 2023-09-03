package com.example.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe.databinding.ActivityTwoPlayerBinding;

import java.util.Arrays;

public class Activity2Player extends AppCompatActivity implements View.OnClickListener {


    private static int GAME_LEVEL = 3;
    private ImageView[][] buttons = new ImageView[GAME_LEVEL][GAME_LEVEL];

    boolean gameActive = true;

    // Player representation
    // 0 - X
    // 1 - O
    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // State meanings:
    //    0 - X
    //    1 - O
    //    2 - Null
    // put all win positions in a 2D array
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    public static int counter = 0;
    private ActivityTwoPlayerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTwoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        for (int i = 0; i < GAME_LEVEL; i++) {
            for (int j = 0; j < GAME_LEVEL; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        binding.btnHome.setOnClickListener(view -> finish());
    }

    @Override
    public void onClick(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        // game reset function will be called
        // if someone wins or the boxes are full
        if (!gameActive) {
            gameReset(view);
        }

        // if the tapped image is empty
        if (gameState[tappedImage] == 2) {
            // increase the counter
            // after every tap
            counter++;

            // check if its the last box
            if (counter == (GAME_LEVEL * GAME_LEVEL)) {
                // reset the game
                gameActive = false;
            }

            // mark this position
            gameState[tappedImage] = activePlayer;

            // change the active player
            // from 0 to 1 or 1 to 0
            if (activePlayer == 0) {
                // set the image of x
                img.setImageResource(R.drawable.ic_cross);
                activePlayer = 1;
                TextView status = findViewById(R.id.status);

                // change the status
                status.setText("O's Turn - Tap to play");
            } else {
                // set the image of o
                img.setImageResource(R.drawable.ic_circle);
                activePlayer = 0;
                TextView status = findViewById(R.id.status);

                // change the status
                status.setText("X's Turn - Tap to play");
            }
        }

        int flag = 0;
        // Check if any player has won if counter is > 4 as min 5 taps are
        // required to declare a winner
        if (counter > 4) {
            for (int[] winPosition : winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                        gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[0]] != 2) {
                    flag = 1;

                    // Somebody has won! - Find out who!
                    String winnerStr;

                    // game reset function be called
                    gameActive = false;
                    if (gameState[winPosition[0]] == 0) {
                        Toast.makeText(this, "X has won", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "O has won", Toast.LENGTH_SHORT).show();
                    }
                    gameReset(view);
                }
            }
            // set the status if the match draw
            if (counter == (GAME_LEVEL * GAME_LEVEL) && flag == 0) {
                Toast.makeText(this, "Match Draw", Toast.LENGTH_SHORT).show();
                gameReset(view);
            }
        }

    }

    public void gameReset(View view) {

        new Handler().postDelayed(() -> {
            gameActive = true;
            activePlayer = 0;
            counter = 0;
            Arrays.fill(gameState, 2);
            // remove all the images from the boxes inside the grid
            for (int i = 0; i < GAME_LEVEL; i++) {
                for (int j = 0; j < GAME_LEVEL; j++) {
                    buttons[i][j].setImageResource(0);
                }
            }

            binding.status.setText("X's Turn - Tap to play");
        }, 200);
    }
}