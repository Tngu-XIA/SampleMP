package com.example.tngu.samplemp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener{
    public MediaPlayer mediaPlayer;
    private AppCompatSeekBar seekBarProgress;
    private Timer timer = new Timer();
    private String musicURL;
    private boolean isPause;
   // private boolean isFirst;
    //private int playPosition;


    public MusicPlayer(String musicURL,AppCompatSeekBar seekBarProgress){
        this.seekBarProgress = seekBarProgress;
        this.musicURL = musicURL;


        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        }catch (Exception e){
            Log.d("mediaplayer", "MusicPlayer:error ");
        }
        timer.schedule(mTimerTask,0,1000);
    }

    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if(mediaPlayer == null){
                return;
            }
            if(mediaPlayer.isPlaying() && seekBarProgress.isPressed() == false){
                handlerProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handlerProgress = new Handler(){
        public void handleMessage(Message message){
            int position  = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            if(duration > 0){
                long pos = seekBarProgress.getMax() * position / duration ;
                seekBarProgress.setProgress((int) pos);
            }
        };
    };

    /*public void player(){
        if(isFirst){
           isFirstPlay();
           isFirst = false;
        }
        play_pause();
    }*/

    public void isFirstPlay(){
        playNet();
    }

    public boolean play_pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause = true;
        }else {
            if(isPause){
                mediaPlayer.start();
                isPause = false;
            }
        }
        return isPause;
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void playNet() {
        try {
            mediaPlayer.reset();// 把各项参数恢复到初始状态
            mediaPlayer.setDataSource(musicURL);
            mediaPlayer.prepare();// 进行缓冲
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarProgress.setSecondaryProgress(percent);
        int currentProgress = seekBarProgress.getMax()
                *mediaPlayer.getCurrentPosition()/ mediaPlayer.getDuration();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        Log.d("mediaPlayer", "onCompletion");


    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d("mediaPlayer", "onPrepared");

    }
}
