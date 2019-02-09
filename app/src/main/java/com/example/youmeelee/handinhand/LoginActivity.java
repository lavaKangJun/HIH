package com.example.youmeelee.handinhand;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_pass;
    private TextView join_us;
    public static final String USER_EMAIL = "USEREMAIL";

    public String email, pass;
    public static String ID_EMAIL = null;

    public static String login_name[];

    //temper에 띄울 이름
    public static String ID_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = (EditText) findViewById(R.id.et_email);
        et_pass = (EditText) findViewById(R.id.et_pass);
        join_us= (TextView)findViewById(R.id.join);

        //editText 줄바꿈 & 완료버튼 효과
        et_email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_pass.setImeOptions(EditorInfo.IME_ACTION_DONE);


        et_pass.setPrivateImeOptions("defaultInputmode=english;");;
        et_email.setPrivateImeOptions("defaultInputmode=english;");


        join_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,JoinActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onLoginButtonClicked(View view) {
        email = et_email.getText().toString();
        ID_EMAIL = email;
        pass = et_pass.getText().toString();

        login(email, pass);
        Log.d("TAG","id: "+ID_EMAIL);
    }

    private void login(final String email, String pass) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String p_email = params[0];
                String p_pass = params[1];

                InputStream is = null;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", p_email));
                nameValuePairs.add(new BasicNameValuePair("pass", p_pass));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://samdaejang123.dothome.co.kr/find_customers_original.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                login_name = s.split(",");

                loadingDialog.dismiss();
                if (login_name[0].equalsIgnoreCase("success")) {
                //if (s != "failure") {
                    ID_NAME = login_name[1];
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(USER_EMAIL, email);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "회원 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(email, pass);

    }


    public String getId() {
        return email;
    }
}
