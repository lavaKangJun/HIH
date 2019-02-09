package com.example.youmeelee.handinhand;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by  kangjun-young on 2016-05-11.
 */
public class VideoView extends Activity {


    Handler handler;
    MediaPlayer mediaPlayer;
    ImageView slide;

    //이미지 애니메이션
    Animation animFadein;
    Animation animFadeout;
    Animation animBounce;

    int repeat = 0;

    //getimage.php에서 path를 불러옴
    // private static final String IMAGES_URL = "http://samdaejang123.dothome.co.kr/cardView_slide.php";

    static final int WHAT1 = 0;
    static final int WHAT2 = 1;
    static final int WHAT3 = 2;
    static final int WHAT4 = 3;
    static final int WHAT5 = 4;
    static final int WHAT6 = 5;

    long PERIODR = 3000;

    Activity activity;
    public String path2[];
    String timelineId;
    int min = 1;
    int max = 4;
    Random r;
    int music_num;
    int str_max=10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        slide = (ImageView)findViewById(R.id.videoView);
        activity = this;
        Intent it = getIntent();
        timelineId = it.getStringExtra("it_id_video");

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

        getPath();

    }

    void musicStart(int num){
        switch (num) {
            case 1:
                mediaPlayer = MediaPlayer.create(this,R.raw.one);
                mediaPlayer.start();
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this,R.raw.two);
                mediaPlayer.start();
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this,R.raw.three);
                mediaPlayer.start();
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this,R.raw.four);
                mediaPlayer.start();
                break;
        }

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();

        handler.removeCallbacksAndMessages(null);
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(mediaPlayer != null)
            mediaPlayer.start();
    }
    public  void handle(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg1){
                super.handleMessage(msg1);

                switch (msg1.what) {
                    case WHAT1:
                        String path_1 = path2[0];
                        Picasso.with(activity).load(path_1).into(slide);
                        slide.startAnimation(animFadein);
                        break;
                    case WHAT2:
                        String path_2 = path2[1];
                        Picasso.with(activity).load(path_2).into(slide);
                        slide.startAnimation(animBounce);
                        break;
                    case WHAT3:
                        String path_3 = path2[2];
                        Picasso.with(activity).load(path_3).into(slide);
                        slide.startAnimation(animFadeout);
                        break;
                    case WHAT4:
                        String path_4 = path2[3];
                        Picasso.with(activity).load(path_4).into(slide);
                        slide.startAnimation(animFadeout);
                        break;
                    case WHAT5:
                        String path_5 = path2[4];
                        Picasso.with(activity).load(path_5).into(slide);
                        slide.startAnimation(animFadein);
                        break;
                    case WHAT6:
                        String path_6 = path2[5];
                        Picasso.with(activity).load(path_6).into(slide);
                        slide.startAnimation(animFadeout);
                        break;
                    default:
                        break;
                }
            }
        };
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                mediaPlayer.stop();
                finish();
            }
        }, 18000);
    }




    private void getPath() {
        path2 = new String[str_max];
        class GetPath extends AsyncTask<String, Void, String> {
            //  ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //dialog = ProgressDialog.show(btConnectActivity.this, "PleaseWait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) { //s는 백그라운드 실행후 리턴값
                super.onPostExecute(s);
                // dialog.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                path2 = s.split(",");
                for(int i=0; i<path2.length;i++)
                    Log.d("onPostPath", i+" 행 " + path2[i] + "\n");

                handle();

                r = new Random();
                music_num = r.nextInt(4) + 1;

                musicStart(music_num);

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(repeat%6);
                        repeat++;
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask,0,PERIODR);
            }

            @Override
            protected String doInBackground(String... params) {

                String timelineId = params[0];
                String add = "http://samdaejang123.dothome.co.kr/cardView_slide.php?$timelineId="+timelineId;

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(add);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    Log.d("Http","con"+con);
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.d("BR"," bufferedReader "+ bufferedReader );
                    String path2;
                    while((path2 = bufferedReader.readLine())!= null){
                        sb.append(path2+"\n");
                    }
                    Log.d("sb",sb.toString());

                    //trim으로 공백 제거
                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetPath getp = new GetPath();
        getp.execute(timelineId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        finish();
    }

}