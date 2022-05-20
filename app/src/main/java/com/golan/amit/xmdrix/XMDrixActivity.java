package com.golan.amit.xmdrix;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class XMDrixActivity extends AppCompatActivity implements View.OnClickListener {

    TextView[][] tv;
    TextView tvDisplay;
    XMDrixHelper xmdh ;
    Button btnAgain;
    SoundPool sp;
    int sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmdrix);

        init();

        setListeners();

//        displayData();

        playInit();
    }

    private void playInit() {
        tvDisplay.setText("Now plays: " + xmdh.getCurrentPlayString());
        for(int i = 0; i < XMDrixHelper.SQUARE; i++) {
            for(int j = 0; j < XMDrixHelper.SQUARE; j++) {
                tv[i][j].setClickable(true);
                tv[i][j].setText("");
                tv[i][j].setBackgroundColor(getResources().getColor(R.color.colorBlack));
                tv[i][j].setTextColor(getResources().getColor(R.color.colorWhite));
            }
        }
    }

    private void init() {
//        tv = new TextView[XMDrixHelper.SQUARE][XMDrixHelper.SQUARE];
        tv = new TextView[][] {
                { findViewById(R.id.tv00), findViewById(R.id.tv01), findViewById(R.id.tv02) },
                { findViewById(R.id.tv10), findViewById(R.id.tv11), findViewById(R.id.tv12) },
                { findViewById(R.id.tv20), findViewById(R.id.tv21), findViewById(R.id.tv22) }
        };

        tvDisplay = findViewById(R.id.tvDisplay);
        xmdh = new XMDrixHelper();
        btnAgain = findViewById(R.id.btnAgain);

        /**
         * Sound
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME). build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(10).setAudioAttributes(aa).build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        sound = sp.load(this, R.raw.laser, 1);

    }


    private void setListeners() {
        for(int i = 0; i < tv.length; i++) {
            for(int j = 0; j < tv[i].length; j++) {
//                tv[i][j].setOnTouchListener(this);
                tv[i][j].setOnClickListener(this);
            }
        }
        btnAgain.setOnClickListener(this);
    }

    private boolean isEvaluated() {
        boolean won = false;
        int tmpInt = -1;
        /**
         * Horizontal validation
         */
        for (int i = 0; i < XMDrixHelper.SQUARE; i++) {
            int count = 0;
            for (int j = 0; j < XMDrixHelper.SQUARE; j++) {
                if ((tv[i][j].getText().toString().equalsIgnoreCase(xmdh.getCurrentPlayString()))) {
                    count++;
                }
            }
            if (count == XMDrixHelper.SQUARE) {
                tmpInt = i;
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "We have a win in horizontal check");
                }
                won = true;
            }
        }

        if (won) {
            if (tmpInt != -1) {

                for (int j = 0; j < XMDrixHelper.SQUARE; j++) {
                    tv[tmpInt][j].setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    tv[tmpInt][j].setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
            return true;
        }

        /**
         * Vertical verification
         */
        tmpInt = -1;
        for (int j = 0; j < XMDrixHelper.SQUARE; j++) {
            int count = 0;
            for (int i = 0; i < XMDrixHelper.SQUARE; i++) {
                if (tv[i][j].getText().toString().equalsIgnoreCase(xmdh.getCurrentPlayString())) {
                    count++;
                }
            }
            if (count == XMDrixHelper.SQUARE) {
                tmpInt = j;
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "We have a win in vertical check");
                }
                won = true;
            }
        }

        if (won) {
            if (tmpInt != -1) {
                for (int j = 0; j < XMDrixHelper.SQUARE; j++) {
                    tv[j][tmpInt].setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    tv[j][tmpInt].setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }
            return true;
        }

        /**
         * Diagonals verifications
         */

        int count = 0;
        for (int i = 0; i < XMDrixHelper.SQUARE; i++) {
            if (tv[i][i].getText().toString().equalsIgnoreCase(xmdh.getCurrentPlayString())) {
                count++;
            }
        }
        if (count == XMDrixHelper.SQUARE) {
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "won in right diagonal");
            }
            won = true;
        }

        if (won) {
            for (int i = 0; i < XMDrixHelper.SQUARE; i++) {
                tv[i][i].setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tv[i][i].setTextColor(getResources().getColor(R.color.colorBlack));
            }
            return true;
        }

        count = 0;
        int tmp = XMDrixHelper.SQUARE - 1;
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, " ---> tmp =" + tmp);
        }
        for (int i = tmp; i >= 0; i--) {
            if (tv[tmp - i][i].getText().toString().equalsIgnoreCase(xmdh.getCurrentPlayString())) {
                if(MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "found in left " + i + " diagonal");
                }
                count++;
            }
        }
        if (count == XMDrixHelper.SQUARE) {
            if(MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "won in left diagonal");
            }
            won = true;
        }

        if (won) {
            for (int i = tmp; i >= 0; i--) {
                tv[tmp - i][i].setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tv[tmp - i][i].setTextColor(getResources().getColor(R.color.colorBlack));
            }
            return true;
        }

        return false;
    }


    @Override
//    public boolean onTouch(View v, MotionEvent event) {
    public void onClick(View v) {

        if (v == btnAgain) {
            xmdh = new XMDrixHelper();
            playInit();
            btnAgain.setVisibility(View.INVISIBLE);
            return;
        }

        int row = -1, col = -1;
        for (int i = 0; i < tv.length; i++) {
            for (int j = 0; j < tv[i].length; j++) {
                if (v == tv[i][j]) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        if (row == -1 || col == -1) {
            if (MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "illegal position (textview button) was unrecognized");
            }
            return;
        }
        String tmpContent = null;
        try {
            tmpContent = tv[row][col].getText().toString();
        } catch (Exception e) {
            Log.e(MainActivity.DEBUGTAG, "get text exception");
            return;
        }
        if (MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "cell " + row + "," + col + " was touched. content: {" + tmpContent + "}");
        }

        /**
         * Here the fun begins
         */

        if (tmpContent == null) {
            if (MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "string content is null");
            }
            return;
        }

        if (tmpContent.equals("")) {
            if (MainActivity.DEBUG) {
                Log.d(MainActivity.DEBUGTAG, "Empty, ready for player setting");
            }
            tv[row][col].setText(xmdh.getCurrentPlayString());
            sp.play(sound, 1, 1, 0, 0, 1);
            v.setAlpha((float) 0.8);
            if (isEvaluated()) {
                //  WON
                Toast.makeText(this, "player " + (xmdh.getTruePlayIndicator() + 1) + " won", Toast.LENGTH_SHORT).show();
                if (MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "WIN");
                }
                tvDisplay.setText("player " + (xmdh.getTruePlayIndicator() + 1) + " won");
                finishGame();
                return;
            }
            xmdh.increaseGame_rounds();
            if (xmdh.getGame_rounds() == (XMDrixHelper.SQUARE * XMDrixHelper.SQUARE)) {
                Toast.makeText(this, "Tide - game over", Toast.LENGTH_SHORT).show();
                if (MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "Game over after " + (xmdh.getGame_rounds())
                            + " rounds, with tide");
                }
                tvDisplay.setText("Tide");
                finishGame();
                return;
            }
            xmdh.increasePlay_indicator();
            tvDisplay.setText("Now plays: " + xmdh.getCurrentPlayString());
        } else {
            Toast.makeText(this, "Already taken", Toast.LENGTH_SHORT).show();
        }
        return;

    }

    private void finishGame() {
        for(int i = 0; i < (XMDrixHelper.SQUARE ); i++) {
            for(int j = 0; j < (XMDrixHelper.SQUARE ); j++) {
                tv[i][j].setClickable(false);
            }
        }
        btnAgain.setVisibility(View.VISIBLE);
    }

    private void displayData() {
        /**
         * width and height of screen
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if (MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "resplution. width " + width + ", height: " + height);
        }
    }
}
