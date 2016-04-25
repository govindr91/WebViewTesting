package in.helpchat.webviewtesting;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by adarshpandey on 4/19/16.
 */
public class AnimActivity extends AppCompatActivity implements Animation.AnimationListener {


    ImageView imgLogo;
    Button btnStart;

    // Animation
    Animation animSequential;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sequential);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        btnStart = (Button) findViewById(R.id.btnStart);

        // load the animation
        animSequential = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.sequential);

        // set animation listener
        animSequential.setAnimationListener(this);

        // button click event
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the animation
                imgLogo.startAnimation(animSequential);
            }
        });


        imgLogo.startAnimation(animSequential);

    }

    Handler handler = new Handler();
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        imgLogo.setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgLogo.startAnimation(animSequential);
            }
        }, 1000);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
