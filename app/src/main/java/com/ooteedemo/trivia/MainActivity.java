package com.ooteedemo.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ooteedemo.trivia.data.AnswerListAsyncResponse;
import com.ooteedemo.trivia.data.QuestionBank;
import com.ooteedemo.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private TextView scoreTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton prevButton;
    private ImageButton nextButton;

    private int currentQuestionIndex=0;
    private List<Question> questionList;

    Score score = new Score();
    private int scoreCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionCounterTextView = findViewById(R.id.counter_textView);
        questionTextView = findViewById(R.id.question_textView);
        scoreTextView = findViewById(R.id.score_textView);

        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);


            questionList =  new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounterTextView.setText(currentQuestionIndex+"/"+questionArrayList.size());
                Log.d("MYMAIN", "onCreate: "+questionArrayList);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_button:
                if (currentQuestionIndex>0){
                currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                updateQuestion();
            }
                break;
            case R.id.next_button:
//                currentQuestionIndex++;
                currentQuestionIndex = (currentQuestionIndex +1) % questionList.size();
                updateQuestion();
                break;
            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
        }

    }

    private void checkAnswer(boolean userAnswer) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId = 0;
        if (userAnswer==answerIsTrue) {
            fadeView();
            addPoints();
            toastMessageId = R.string.correct_answer;
        } else {
            shakeAnimation();
            deductPoints();
            toastMessageId = R.string.wrong_answer;
        }

        Toast.makeText(MainActivity.this,toastMessageId,Toast.LENGTH_SHORT).show();
    }

    private void deductPoints() {
        if (scoreCounter>0) {
            scoreCounter -= 100;
        } else {
            scoreCounter = 0;
        }
        score.setScore(scoreCounter);
        scoreTextView.setText(String.valueOf(score.getScore()));
        Log.d("MYSCORE", "deductPoints: "+score.getScore());
    }

    private void addPoints() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        scoreTextView.setText(String.valueOf(score.getScore()));
        Log.d("MYSCORE", "addPoints: "+score.getScore());
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextView.setText(question);
        questionCounterTextView.setText(currentQuestionIndex+"/"+questionList.size());
    }

    private void fadeView() {
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}