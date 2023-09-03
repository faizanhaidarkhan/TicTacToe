package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoe.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnTwoPlayer.setOnClickListener(view -> {
            this.startActivity(new Intent(this, Activity2Player.class));
        });

        binding.btnSinglePlayer.setOnClickListener(view -> {
            this.startActivity(new Intent(this, ActivityBot.class));
        });
    }
}