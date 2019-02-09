package com.example.youmeelee.handinhand;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.youmeelee.handinhand.R.id.imageView;
import static com.example.youmeelee.handinhand.R.id.imageView2;
import static com.example.youmeelee.handinhand.R.id.imageView3;


/**
 * Created by yumiLee on 10/17/16.
 */
@SuppressLint("ValidFragment")
public class SecondFragment extends Fragment implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://samdaejang123.dothome.co.kr/upload.php";
    public static final String UPLOAD_KEY = "image";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonUpload;
    private Button startBluetooth;

    public ImageView imageViewSelect;
    public ImageView imageView2Select;
    public ImageView imageView3Select;

    public Bitmap bitmap, bitmap2, bitmap3;
    public static Uri filePath1, filePath2, filePath3;

    public String imgCount;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.second_fragment, null);

        //버튼 선언
        buttonUpload = (Button) view.findViewById(R.id.buttonUpload);
        startBluetooth = (Button) view.findViewById(R.id.btn_start_bluetooth);

        //갤러리에서 선택한 이미지 띄우기
        imageViewSelect = (ImageView) view.findViewById(imageView);
        imageView2Select = (ImageView) view.findViewById(imageView2);
        imageView3Select = (ImageView) view.findViewById(imageView3);

        buttonUpload.setOnClickListener(this);
        startBluetooth.setOnClickListener(this);

        imageViewSelect.setOnClickListener(this);
        imageView2Select.setOnClickListener(this);
        imageView3Select.setOnClickListener(this);

        imgCount = "6";

        return view;
    }

    //갤러리로부터 사진 선택
    private void showFileChooser(int selected_index) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST + selected_index);
    }


    //갤러리에서 선택된 이미지 보여주는 함수
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == PICK_IMAGE_REQUEST + 1 && data != null && data.getData() != null) {
                filePath1 = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath1);
                Picasso.with(getActivity().getApplication()).load(filePath1).into(imageViewSelect);
                //imageViewSelect.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));
            } else if (requestCode == PICK_IMAGE_REQUEST + 2 && data != null && data.getData() != null) {
                filePath2 = data.getData();
                bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath2);
                Picasso.with(getActivity().getApplication()).load(filePath2).into(imageView2Select);
                //imageView2Select.setImageBitmap(Bitmap.createScaledBitmap(bitmap2, 120, 120, false));
            } else if (requestCode == PICK_IMAGE_REQUEST + 3 && data != null && data.getData() != null) {
                filePath3 = data.getData();
                bitmap3 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath3);
                Picasso.with(getActivity().getApplication()).load(filePath3).into(imageView3Select);
                //imageView3Select.setImageBitmap(Bitmap.createScaledBitmap(bitmap3, 120, 120, false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //imageViewSelect.setImageBitmap(bitmap);
    //imageViewSelect.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //이미지 서버에 업로드
    class UploadImage extends AsyncTask<Void, Void, String> {
        Bitmap a;
        Bitmap b;
        Bitmap c;

        public UploadImage(Bitmap a, Bitmap b, Bitmap c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        ProgressDialog loading;
        RequestHandler rh = new RequestHandler();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getContext(), "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            //Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity().getApplicationContext(),"사진 업로드 완료!",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String uploadImage = getStringImage(a);
            String uploadImage2 = getStringImage(b);
            String uploadImage3 = getStringImage(c);

            HashMap<String, String> data = new HashMap<>();

            data.put(UPLOAD_KEY + 1, uploadImage);
            data.put(UPLOAD_KEY + 2, uploadImage2);
            data.put(UPLOAD_KEY + 3, uploadImage3);

            String result = rh.sendPostRequest(UPLOAD_URL, data);

            return result;
        }
    }

    //이미지 count세기
    private void count(String imgCount) {
        class CountAsync extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String count = params[0];
                InputStream is = null;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("imgCount", count));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://samdaejang123.dothome.co.kr/getCount.php");
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
                if (s.equalsIgnoreCase("success")) {
                    Intent intent = new Intent(getActivity(), btConnectActivity.class);
                    startActivity(intent);
                    (getActivity()).finish();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"상대방의 사진을 기다리고 있습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }

        CountAsync getp = new CountAsync();
        getp.execute(imgCount);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //이미지뷰 클릭했을 때
            case imageView:
                showFileChooser(1);
                Toast.makeText(getActivity().getApplicationContext(),"모든 사진을 3분안에 올려주세요.", Toast.LENGTH_LONG).show();
                break;

            case imageView2:
                showFileChooser(2);
                Toast.makeText(getActivity().getApplicationContext(),"모든 사진을 3분안에 올려주세요.", Toast.LENGTH_LONG).show();
                break;

            case imageView3:
                showFileChooser(3);
                Toast.makeText(getActivity().getApplicationContext(),"모든 사진을 3분안에 올려주세요.", Toast.LENGTH_LONG).show();
                break;

            case R.id.buttonUpload:

                //bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.4), (int) (bitmap.getHeight() * 0.4), true);
                //bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int) (bitmap2.getWidth() * 0.4), (int) (bitmap2.getHeight() * 0.4), true);
                //bitmap3 = Bitmap.createScaledBitmap(bitmap3, (int) (bitmap3.getWidth() * 0.4), (int) (bitmap3.getHeight() * 0.4), true);
                new UploadImage(bitmap, bitmap2, bitmap3).execute();

                break;

            case R.id.btn_start_bluetooth:

                //블루투스 액티비티 넘어가기 전에 이미지 카운트 6개 체크하기
                count(imgCount);
                break;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}