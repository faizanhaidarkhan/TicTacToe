package com.example.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe.databinding.ActivityBotBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivityBot extends AppCompatActivity implements View.OnClickListener {

    private ActivityBotBinding binding;
    private final List<Integer> player1 = new ArrayList<>(5);
    private final List<Integer> robot = new ArrayList<>(5);
    private final List<Integer> emptyCells = new ArrayList<>(9);

    private final int[][] winPositionList = {
            {1, 2, 3}, {4, 5, 6}, {7, 8, 9},  // Rows
            {1, 4, 7}, {2, 5, 8}, {3, 6, 9},  // Columns
            {1, 5, 9}, {3, 5, 7}             // Diagonals
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        for (int i = 1; i <= 9; i++) {
            getSelectedButton(i).setOnClickListener(this);
        }

        binding.btnHome.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onClick(View view) {
        ImageView but = (ImageView) view;
        int cellID = 0;
        if (but.getId() == R.id.button) {
            cellID = 1;
        } else if (but.getId() == R.id.button2) {
            cellID = 2;
        } else if (but.getId() == R.id.button3) {
            cellID = 3;
        } else if (but.getId() == R.id.button4) {
            cellID = 4;
        } else if (but.getId() == R.id.button5) {
            cellID = 5;
        } else if (but.getId() == R.id.button6) {
            cellID = 6;
        } else if (but.getId() == R.id.button7) {
            cellID = 7;
        } else if (but.getId() == R.id.button8) {
            cellID = 8;
        } else if (but.getId() == R.id.button9) {
            cellID = 9;
        }
        playNow(but, cellID);
    }

    private void playNow(ImageView buttonSelected, int currCell) {
        buttonSelected.setImageResource(R.drawable.ic_cross);
        player1.add(currCell);
        emptyCells.add(currCell);
        buttonSelected.setEnabled(false);
        if (checkWinner() == 1) {
            reset();
        } else {
            robot();
        }
    }

    private int checkWinner() {
        if (checkWinner(player1)) {
            showToast("You have won the game");
            return 1;
        } else if (checkWinner(robot)) {
            showToast("Robot has won the game");
            return 1;
        } else if (emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) &&
                emptyCells.contains(4) && emptyCells.contains(5) && emptyCells.contains(6) &&
                emptyCells.contains(7) && emptyCells.contains(8) && emptyCells.contains(9)) {

            showToast("Game Draw");
            return 1;
        }
        return 0;
    }

    // Check for all moves of the player if there are any of the win moves
    private boolean checkWinner(List<Integer> moves) {
        if ((moves.contains(1) && moves.contains(2) && moves.contains(3)) ||
                (moves.contains(1) && moves.contains(4) && moves.contains(7)) ||
                (moves.contains(3) && moves.contains(6) && moves.contains(9)) ||
                (moves.contains(7) && moves.contains(8) && moves.contains(9)) ||
                (moves.contains(4) && moves.contains(5) && moves.contains(6)) ||
                (moves.contains(1) && moves.contains(5) && moves.contains(9)) ||
                (moves.contains(3) && moves.contains(5) && moves.contains(7)) ||
                (moves.contains(2) && moves.contains(5) && moves.contains(8))) {
            return true;
        }
        return false;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void reset() {
        new Handler().postDelayed(() -> {
            player1.clear();
            robot.clear();
            emptyCells.clear();

            for (int i = 1; i <= 9; i++) {
                ImageView buttonSelected = getSelectedButton(i);
                buttonSelected.setEnabled(true);
                buttonSelected.setImageResource(0);
            }
        }, 400);
    }

    private ImageView getSelectedButton(int i) {
        switch (i) {
            case 2:
                return binding.button2;
            case 3:
                return binding.button3;
            case 4:
                return binding.button4;
            case 5:
                return binding.button5;
            case 6:
                return binding.button6;
            case 7:
                return binding.button7;
            case 8:
                return binding.button8;
            case 9:
                return binding.button9;
            default:
                return binding.button;
        }
    }

    private void robot() {
        int winningMove = findWinMove();
        if (winningMove != -1) {
            performMove(winningMove);
            return;
        }

        int blockingWinMove = findBlockMove();
        if (blockingWinMove != -1) {
            performMove(blockingWinMove);
            return;
        }

        int secondWinMove = markSecondMoveForWin();
        if (secondWinMove != -1) {
            performMove(secondWinMove);
            return;
        }

        int winMove = markFromWinMove();
        if (winMove != -1) {
            performMove(winMove);
            return;
        }

        int rnd = getRandomMove();
        performMove(rnd);
    }

    // Check if Bot has marked any of the two win positions and 3rd position is also empty then return the 3rd position
    private int findWinMove() {
        for (int[] winPositions : winPositionList) {
            int count = 0;
            int positionNotOccupied = -1;
            for (int currentWinPosition : winPositions) {
                if (robot.contains(currentWinPosition)) {
                    count++;
                } else {
                    positionNotOccupied = currentWinPosition;
                }
            }
            if (count == 2 && positionNotOccupied != -1 && !emptyCells.contains(positionNotOccupied)) {
                return positionNotOccupied;
            }
        }
        return -1;
    }

    // Check if player has any of the 2 win positions then mark the 3rd position
    private int findBlockMove() {
        for (int[] winPositions : winPositionList) {
            int count = 0;
            int positionNotOccupied = -1;
            for (int currentWinPosition : winPositions) {
                if (player1.contains(currentWinPosition)) {
                    count++;
                } else {
                    positionNotOccupied = currentWinPosition;
                }
            }
            if (count == 2 && positionNotOccupied != -1 && !emptyCells.contains(positionNotOccupied)) {
                return positionNotOccupied;
            }
        }
        return -1;
    }

    //Check if bot has marked a position from win position and rest of the two are available then mark one if the remaining
    private int markSecondMoveForWin() {
        for (int[] winPositions : winPositionList) {
            int count = 0;
            int positionNotOccupied = -1;
            boolean remainingTwoPositionsAvailable = true;
            for (int currentWinPosition : winPositions) {
                if (robot.contains(currentWinPosition)) {
                    count++;
                } else {
                    if (emptyCells.contains(currentWinPosition)) {
                        remainingTwoPositionsAvailable = false;
                    } else {
                        positionNotOccupied = currentWinPosition;
                    }
                }
            }
            if (count == 1 && positionNotOccupied != -1 && !emptyCells.contains(positionNotOccupied) && remainingTwoPositionsAvailable == true) {
                return positionNotOccupied;
            }
        }
        return -1;
    }

    // Check if all the 3 positions of win positions are available then mark the 1st position
    private int markFromWinMove() {
        for (int[] winPositions : winPositionList) {
            boolean isAnyPositionOccupied = false;
            for (int currentWinPosition : winPositions) {
                if (emptyCells.contains(currentWinPosition)) {
                    isAnyPositionOccupied = true;
                    break;
                }
            }
            if (!isAnyPositionOccupied) {
                return winPositions[0];
            }
        }
        return -1;
    }

    private void performMove(int cell) {
        if (cell != -1) {
            ImageView buttonSelected = getSelectedButton(cell);
            emptyCells.add(cell);
            buttonSelected.setImageResource(R.drawable.ic_circle);
            robot.add(cell);
            buttonSelected.setEnabled(false);
            if (checkWinner() == 1) {
                reset();
            }
        }
    }

    private int getRandomMove() {
        int rnd = new Random().nextInt(9) + 1;
        if (emptyCells.contains(rnd)) {
            getRandomMove();
        } else {
            return rnd;
        }
        return -1;
    }
}