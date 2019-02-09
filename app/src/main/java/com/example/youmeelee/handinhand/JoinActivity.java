package com.example.youmeelee.handinhand;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    EditText et_name, et_first_name, et_email, et_pass;
    String name, first_name, email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        setTitle("가입하기");

        et_name = (EditText) findViewById(R.id.et_name);
        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_pass = (EditText) findViewById(R.id.et_pass);

        //editText 줄바꿈 & 완료버튼 효과
        et_name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_first_name.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        et_pass.setImeOptions(EditorInfo.IME_ACTION_DONE);

        // 영문자와 숫자만 입력할 수 있도록 필터링.
        et_name.setFilters(new InputFilter[]{new CustomInputFilter()});
        et_first_name.setFilters(new InputFilter[]{new CustomInputFilter()});

        // 포커스가 주어졌을 때 보여지는 키보드의 타입을 영어로 설정.
        et_name.setPrivateImeOptions("defaultInputmode=english;");
        et_first_name.setPrivateImeOptions("defaultInputmode=english;");
        et_email.setPrivateImeOptions("defaultInputmode=english;");

    }

    public class CustomInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start,
                                   int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z]+$");

            if(source.equals("") || ps.matcher(source).matches()){
                return source;
            }

            Toast.makeText(JoinActivity.this,"영문명을 입력해주세요.", Toast.LENGTH_SHORT).show();

            return "";
        }
    }

    public void onMakeAccountClicked(View v) {

        name = et_name.getText().toString();
        first_name = et_first_name.getText().toString();
        email = et_email.getText().toString();
        pass = et_pass.getText().toString();

        if (name.length() == 0 || first_name.length() == 0 || email.length() == 0 || pass.length() == 0)
            Toast.makeText(this, "데이터를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();

        else {
            insertToDatabase(name, first_name, email, pass);
            Intent it = new Intent(this, LoginActivity.class);
            startActivity(it);
        }

    }

    private void insertToDatabase(String name, String first_name, String email, String pass) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(JoinActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String name = (String) params[0];
                    String first_name = (String) params[1];
                    String email = (String) params[2];
                    String pass = (String) params[3];

                    String link = "http://samdaejang123.dothome.co.kr/insert_customers.php";
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(name, first_name, email,pass);
    }

}