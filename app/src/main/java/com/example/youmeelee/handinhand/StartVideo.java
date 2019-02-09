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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kangjun-young on 2016. 6. 22..
 */
public class StartVideo extends Activity {

    Handler handler;
    ImageView imgSlide;
    MediaPlayer mediaPlayer;
    TextView tema,temb,heart;

    //이미지 애니메이션
    Animation animFadein;
    Animation animFadeout;
    Animation animBounce;

    int repeat = 0;

    //getimage.php에서 path를 불러옴
    private String temp_url="http://samdaejang123.dothome.co.kr/fetch_temp.php";
    private static final String IMAGES_URL = "http://samdaejang123.dothome.co.kr/getimage.php";
    private  String video_url = "http://samdaejang123.dothome.co.kr/insert_video.php";
    static final int WHAT_1 = 0;
    static final int WHAT_2 = 1;
    static final int WHAT_3 = 2;
    static final int WHAT_4 = 3;
    static final int WHAT_5 = 4;
    static final int WHAT_6 = 5;

    static  final long PERIOD = 3000;

    Activity activity;
    public String path[];
    public String temp[];
    int min = 1;
    int max = 5;
    Random r;
    int music_num;
    int str_max=10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.start_video);

        imgSlide = (ImageView)findViewById(R.id.imageSlide);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animFadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);

        tema = (TextView)findViewById(R.id.tema);
        temb = (TextView)findViewById(R.id.temb);
        heart = (TextView)findViewById(R.id.heart);

        handler = new Handler();
        getPath();
        //    addVideo();
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

    private void getPath() {
        path = new String[str_max];
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

                //php에서 보낸 이미지 경로를 path배열에 저장 (path[0],path[1],path[2],path[3],path[4],path[5])
                path = s.split(",");

                for(int i=0; i<path.length; i++)
                    Log.d("onPostPath", i+" 행 " + path[i] + "\n");

                getTemp();
            }
            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
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
        getp.execute(IMAGES_URL);

    }

    private void getTemp() {
        temp = new String[str_max];
        class GetTemp extends AsyncTask<String, Void, String> {
            //  ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //dialog = ProgressDialog.show(btConnectActivity.this, "PleaseWait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) { //s는 백그라운드 실행후 리턴값
                super.onPostExecute(s);

                //php에서 보낸 이미지 경로를 path배열에 저장 (path[0],path[1],path[2],path[3],path[4],path[5])
                temp = s.split(",");
                tema.setText(temp[0]);
                temb.setText(temp[1]);

                for(int i=0; i<temp.length; i++)
                    Log.d("onPostPath", i+" 행 " + temp[i] + "\n");


                handle();

                r = new Random();
                music_num = r.nextInt(4) + 1;


                musicStart(music_num);
                //  mediaPlayer = MediaPlayer.create(this,R.raw.carefree);
                // mediaPlayer.start();

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(repeat%6);
                        repeat++;
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask,0,PERIOD);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    //getimage.php 실행
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    Log.d("Http","con"+con);
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.d("BR"," bufferedReader "+ bufferedReader );

                    //getimage.php에서 받아온 이미지 path를 path2에 저장
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

        GetTemp getT = new GetTemp();
        getT.execute(temp_url);

    }

    public  void handle(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);


                switch (msg.what) {
                    case WHAT_1:
                        String path1 = path[0];
                        Picasso.with(activity).load(path1).into(imgSlide);
                        imgSlide.startAnimation(animFadein);
                        break;
                    case WHAT_2:
                        String path2 = path[1];
                        Picasso.with(activity).load(path2).into(imgSlide);
                        imgSlide.startAnimation(animBounce);
                        break;
                    case WHAT_3:
                        String path3 = path[2];
                        Picasso.with(activity).load(path3).into(imgSlide);
                        imgSlide.startAnimation(animFadeout);
                        break;
                    case WHAT_4:
                        String path4 = path[3];
                        Picasso.with(activity).load(path4).into(imgSlide);
                        imgSlide.startAnimation(animFadeout);
                        break;
                    case WHAT_5:
                        String path5 = path[4];
                        Picasso.with(activity).load(path5).into(imgSlide);
                        imgSlide.startAnimation(animFadein);
                        break;
                    case WHAT_6:
                        String path6 = path[5];
                        Picasso.with(activity).load(path6).into(imgSlide);
                        imgSlide.startAnimation(animFadeout);
                        break;
                }

            }
        };

        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                mediaPlayer.stop();
                Intent i = new Intent(StartVideo.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 18000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();

        Intent i = new Intent(StartVideo.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}