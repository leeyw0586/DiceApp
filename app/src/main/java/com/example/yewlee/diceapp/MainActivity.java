package com.example.yewlee.diceapp;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RelativeLayout content_area;
    ImageView[] dices;
    TextView result;

    boolean firstRun;
    int maxDice = 4; // 주사위 최대 갯수 지정
    int diceCount;
    int width_constraint;
    int height_constraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content_area = (RelativeLayout) findViewById(R.id.content_area);
        result = (TextView) findViewById(R.id.result);
        dices = new ImageView[maxDice];

        content_area.post(new Runnable() {
            @Override
            public void run() {
                width_constraint = content_area.getWidth() - 200;
                height_constraint = content_area.getHeight() - 200;

                // 시작 시 주사위 한개 생성
                firstRun = true;
                diceCount = 1;
                addDice();
            }
        });


        // 돌아가는 애니메이션 생성
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 3600, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(900L);


        // 하단 버튼 생성
        FloatingActionButton runBtn      = (FloatingActionButton) findViewById(R.id.run);
        FloatingActionButton addBtn      = (FloatingActionButton) findViewById(R.id.add);
        FloatingActionButton subtractBtn = (FloatingActionButton) findViewById(R.id.subtract);

        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sum = 0;
                for (int i = 0; i < diceCount; i++){
                    sum += runDice(dices[i]);
                    dices[i].startAnimation(rotateAnimation);
                }
                result.setText("합 : " + sum);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diceCount >= maxDice) {
                    Toast.makeText(getApplicationContext(), R.string.max_dices, Toast.LENGTH_SHORT).show();
                    return;
                }
                diceCount++;
                addDice();
            }
        });

        subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diceCount <= 1) return;
                content_area.removeViewAt(diceCount-1);
                dices[diceCount-1] = null;
                diceCount--;
            }
        });
    }


    // 주사위 추가
    private void addDice(){

        View item_view = getLayoutInflater().inflate(R.layout.item_dice, null);
        ImageView dice = item_view.findViewById(R.id.dice);

        int width = getRandomNumber(width_constraint);
        int height = getRandomNumber(height_constraint);

        if(firstRun){ // 처음 위치는 가운데로
            width = width_constraint / 2;
            height = height_constraint / 2;
            firstRun = false;
        }

        FrameLayout.LayoutParams layoutParam = new FrameLayout.LayoutParams(100, 100);
        layoutParam.gravity= Gravity.CENTER;
        layoutParam.leftMargin = width;
        layoutParam.topMargin = height;

        dices[diceCount - 1] = dice;

        content_area.addView(dice, layoutParam);
    }



    // 주사위 굴리고 이미지 변경
    private int runDice(ImageView dice){
        int num = getRandomNumber(6);
        int diceResource = getResources().getIdentifier("dice_" + num, "drawable", this.getPackageName());
        dice.setImageResource(diceResource);

        return num;
    }


    // 범위에 해당하는 랜덤값 가져오기
    private int getRandomNumber(int bound){
        Random random = new Random();
        int num = random.nextInt(bound) + 1;
        return num;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
