package com.example.youmeelee.handinhand;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class tempTutorialActivity extends AppCompatActivity {

    NetworkInfo mobile, wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_tutorial);

        connectWifiCheck();

        if(connectWifiCheck() == 0)
            Toast.makeText(this, "네트워크 연결 상태를 확인해주세요", Toast.LENGTH_LONG).show();

    }

    public void onJoinButtonClicked(View v){

        connectWifiCheck();

        if(connectWifiCheck() == 1){
            Intent intent = new Intent(this,JoinActivity.class);
            startActivity(intent);
        }else
            Toast.makeText(this, "네트워크 연결 상태를 확인해주세요", Toast.LENGTH_LONG).show();
    }

    public void onLoginButtonClicked(View v){

        connectWifiCheck();

        if(connectWifiCheck() == 1){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }else
            Toast.makeText(this, "네트워크 연결 상태를 확인해주세요", Toast.LENGTH_LONG).show();
    }

    //와이파이 연결 확인
    public int connectWifiCheck(){
        ConnectivityManager manager =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        //네트워크 연결되있을 때
        if (mobile.isConnected() || wifi.isConnected()){
            return 1;
        }else{  //네트워크 연결 실패시
            return 0;
        }

    }

    public void skip(View v){
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

}
