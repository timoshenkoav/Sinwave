package com.views.sinwave;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SinWave lSinWave = (SinWave) findViewById(R.id.sin);
        SeekBar lSeekBar = (SeekBar) findViewById(R.id.progress);
        lSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lSinWave.setCoef(progress/100f);
                Log.d("MainActivity", String.valueOf(progress/100f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final WaveFormView lWaveFormView = (WaveFormView) findViewById(R.id.wave_form);
        SeekBar lSeekBar2 = (SeekBar) findViewById(R.id.progress2);
        lSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lWaveFormView.updateAmplitude(progress/100f,true);
                Log.d("MainActivity", String.valueOf(progress/100f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lSinWave.toggle();
//                ObjectAnimator anim1 = ObjectAnimator.ofFloat(lSinWave, "globalCoef", 1f, 0f);
//                anim1.setDuration(200);
//                ObjectAnimator anim2 = ObjectAnimator.ofFloat(lSinWave, "globalCoef", 0f, 1f);
//                anim2.setDuration(200);
//                ObjectAnimator anim3 = ObjectAnimator.ofFloat(lSinWave, "globalCoef", 1f, 0f);
//                anim3.setDuration(200);
//                ObjectAnimator anim4 = ObjectAnimator.ofFloat(lSinWave, "globalCoef", 0f, 1f);
//                anim4.setDuration(200);
//
//                AnimatorSet lAnimatorSet = new AnimatorSet();
//                lAnimatorSet.playSequentially(anim1,anim2,anim3,anim4);
//                lAnimatorSet.start();
            }
        });


        lWaveFormView.updateAmplitude(0.3f,true);
    }
}
