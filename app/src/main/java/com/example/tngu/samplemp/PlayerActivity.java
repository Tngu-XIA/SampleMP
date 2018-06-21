package com.example.tngu.samplemp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

//http://up.mcyt.net/?down/47737.mp3  音乐地址
public class PlayerActivity extends AppCompatActivity {
    private TextView tv_title;
    private ImageView img_show;
    private ImageButton btn_play_pause, btn_share;
    private AppCompatSeekBar seekBar_music;
    private MusicPlayer musicPlayer;
    private String musicURL_Now = "http://up.mcyt.net/?down/47737.mp3";
    private boolean isFirst = true;
    private RoundImageView roundImageView;
    private ObjectAnimator animator;
    private MyAnimatorUpdateListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        musicPlayer = new MusicPlayer(musicURL_Now, seekBar_music);
        tv_title.setText("这是一个demo哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈");
        btn_play_pause.setOnClickListener(new ClickEvent());
        seekBar_music.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        roundImageView.setOutsideColor(Color.TRANSPARENT);
        roundImageView.setInsideColor(Color.TRANSPARENT);
        roundImageView.setImageDrawable(getResources().getDrawable(R.drawable.lm));
        img_show.getBackground().setAlpha(80);



        LinearInterpolator linearInterpolator = new LinearInterpolator();
        animator = ObjectAnimator.ofFloat(roundImageView, "rotation", 0f, 360f);
        animator.setDuration(30000);
        animator.setInterpolator(linearInterpolator);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(-1);
        listener = new MyAnimatorUpdateListener(animator);
        animator.addUpdateListener(listener);

    }

    private void initView() {
        btn_play_pause = findViewById(R.id.btn_play_pause);
        btn_share = findViewById(R.id.btn_share);
        seekBar_music = findViewById(R.id.seekBar_music);
        tv_title = findViewById(R.id.tv_showTitle);
        roundImageView = findViewById(R.id.roundImg_view);
        img_show = findViewById(R.id.img_show);
    }

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_play_pause) {
                if (isFirst) {
                    Log.d("click", "onClick: isfirst");
                    musicPlayer.isFirstPlay();
                    animator.start();
                    btn_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.widget_ic_play));
                    isFirst = false;
                } else {
                    Log.d("click", "onClick: play_pause");
                    boolean pause = musicPlayer.play_pause();
                    if (pause) {
                        listener.pause();
                        btn_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.widget_ic_stop));
                    } else {
                        listener.play();
                        btn_play_pause.setImageDrawable(getResources().getDrawable(R.drawable.widget_ic_play));
                    }
                }
            }
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.progress = progress * musicPlayer.mediaPlayer.getDuration() / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            musicPlayer.mediaPlayer.seekTo(progress);
        }
    }
}
