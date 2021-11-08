package com.example.math;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.math.databinding.ActivityMainBinding;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public String Level;
    public int levelNum;
    public String answer;
    public String question;
    public int res;
    public int second;
    public int time;
    public int extraTime;
    public Button btnGo;
    public EditText answerEditText;
    public TextView questionTextView;
    public TextView timerTextView;
    public TextView levelTextView;
    public TextView commentTextView;
    public boolean start;
    public Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        extraTime = 0;
        Level = "01";
        levelNum = 1;
        answer = "";
        second = 10;
        time = 0;
        btnGo = (Button) findViewById(R.id.btnGo);
        answerEditText = (EditText) findViewById(R.id.answer);
        timerTextView = (TextView) findViewById(R.id.time);
        levelTextView = (TextView) findViewById(R.id.level);
        commentTextView = (TextView) findViewById(R.id.comment);
        questionTextView = (TextView) findViewById(R.id.question);

        answerEditText.setEnabled(true);
        answerEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        timer = new Timer();
        GameTimer();
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                answer = answerEditText.getText().toString();
                int ans = 0;

                if (btnGo.getText() == "next" && !start) {
                    StartGame();
                }

                else if (btnGo.getText() == "Go!" && start) {

                    try {
                        ans = Integer.parseInt(answer);
                    } catch (Exception exception) {
                        ans = 0;
                    }

                    if (ans == res) {
                        commentTextView.setText(question + " = " + answer + "\n is correct!");
                        answerEditText.setBackgroundColor(getResources().getColor(R.color.green));
                        levelNum++;
                        Level = String.valueOf(levelNum);
                        levelTextView.setText(Level);
                    } else {
                        answerEditText.setBackgroundColor(getResources().getColor(R.color.red));
                        commentTextView.setText(question + " = " + answer + " \n is wrong!");
                        levelNum--;
                        if (levelNum < 1) levelNum = 1;
                        Level = String.valueOf(levelNum);
                        levelTextView.setText(Level);
                    }
                    questionTextView.setText(question + " = " + res);
                    btnGo.setText("next");
                    start = false;
                }
            }
        });

        StartGame();

    }

    public void StartGame(){
        answerEditText.setBackgroundColor(getResources().getColor(R.color.white));
        commentTextView.setBackgroundColor(getResources().getColor(R.color.white));
        btnGo.setText("Go!");
        Random rnd = new Random();
        int num1 = rnd.nextInt(10 * levelNum * levelNum * levelNum);
        int num2 = rnd.nextInt(10 * levelNum * levelNum * levelNum);
        question = num1 + " + " + num2;
        res = num1 + num2;
        questionTextView.setText("" + question + " = ?");
        if ( levelNum % 2 == 0 ) extraTime++;
        second = 10 + extraTime;
        start = true;
        commentTextView.setText("do math\n");
        answerEditText.setText("");
        answerEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    public void GameTimer(){

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (start) {
                    if (second <= 0) {
                        start = false;
                        commentTextView.setText("Time is over!\n");
                        commentTextView.setBackgroundColor(getResources().getColor(R.color.red));
                        questionTextView.setText(question + " = " + res);
                        btnGo.setText("next");
                        levelNum--;
                        if (levelNum < 1) levelNum = 1;
                        Level = String.valueOf(levelNum);
                        levelTextView.setText(Level);

                    } else {
                        second--;
                        setText(timerTextView,"" + second);
                    }
                }
                TimerMethod();
            }
        }, 0, 1000);

    }

    private void TimerMethod() {

        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(Timer_Tick);
    }


    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.

            //Do something to the UI thread here

        }
    };

    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

}