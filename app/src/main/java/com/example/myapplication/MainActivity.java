package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ImageButton[] buttons;
    private int[] imageResources = {
            R.drawable.camel, R.drawable.coala, R.drawable.fox,
            R.drawable.lion, R.drawable.monkey, R.drawable.wolf,
            R.drawable.camel, R.drawable.coala, R.drawable.fox,
            R.drawable.lion, R.drawable.monkey, R.drawable.wolf
    };
    private boolean[] matchedButtons;
    private ImageButton firstSelected = null;
    private int firstImageId = -1;
    private boolean isBusy = false;

    private TextView flipCounterTextView;
    private int flipCount = 0;
    private int highScore = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipCounterTextView = findViewById(R.id.flipCounterTextView);
        Button restartButton = findViewById(R.id.restartButton);
        buttons = new ImageButton[]{
                findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3),
                findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6),
                findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9),
                findViewById(R.id.button10), findViewById(R.id.button11), findViewById(R.id.button12)
        };

        restartButton.setOnClickListener(view -> restartGame());

        setupGame();
    }

    private void setupGame() {
        ArrayList<Integer> imageList = new ArrayList<>();
        for (int img : imageResources) {
            imageList.add(img);
        }
        Collections.shuffle(imageList);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setTag(imageList.get(i));
            buttons[i].setImageResource(R.drawable.code);
            buttons[i].setEnabled(true);
            int finalI = i;
            buttons[i].setOnClickListener(view -> onButtonClick(buttons[finalI]));
        }

        matchedButtons = new boolean[buttons.length];
        flipCount = 0;
        updateFlipCounter();
        firstSelected = null;
        firstImageId = -1;
        isBusy = false;
    }

    private void onButtonClick(ImageButton button) {
        if (isBusy || matchedButtons[getButtonIndex(button)]) return;

        int imageId = (int) button.getTag();
        button.setImageResource(imageId);
        flipCount++;
        updateFlipCounter();

        if (firstSelected == null) {
            firstSelected = button;
            firstImageId = imageId;
        } else {
            if (button == firstSelected) {
                return;
            }

            isBusy = true;
            if (firstImageId == imageId) {
                matchedButtons[getButtonIndex(button)] = true;
                matchedButtons[getButtonIndex(firstSelected)] = true;

                firstSelected = null;
                firstImageId = -1;
                isBusy = false;

                if (isGameComplete()) {
                    if (flipCount < highScore) {
                        highScore = flipCount;
                        Toast.makeText(this, "New High Score: " + highScore + " flips!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "You Win!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    button.setImageResource(R.drawable.code);
                    firstSelected.setImageResource(R.drawable.code);
                    firstSelected = null;
                    firstImageId = -1;
                    isBusy = false;
                }, 1000);
            }
        }
    }

    private int getButtonIndex(ImageButton button) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == button) {
                return i;
            }
        }
        return -1;
    }

    private boolean isGameComplete() {
        for (boolean matched : matchedButtons) {
            if (!matched) return false;
        }
        return true;
    }

    private void updateFlipCounter() {
        flipCounterTextView.setText("Flips: " + flipCount);
    }

    private void restartGame() {
        setupGame();
    }
}
