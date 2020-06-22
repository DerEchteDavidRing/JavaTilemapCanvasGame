package com.example.zawarudo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    MediaPlayer PlayZaWarudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        PlayZaWarudo = MediaPlayer.create(this, R.raw.zawarudo);


        findViewById(R.id.ZAWARUDO_TEXT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayZaWarudo.start();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
}
