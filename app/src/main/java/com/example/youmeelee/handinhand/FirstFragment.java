package com.example.youmeelee.handinhand;

/**
 * Created by home on 2016-05-11.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


/**
 * Created by home on 2016-05-11.
 */
public class FirstFragment extends Fragment {

    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private ViewAdapter  adapter;
    private String timelienURL = "http://samdaejang123.dothome.co.kr/test1.php";
    private com.android.volley.RequestQueue requestQueue ;
    private Recycler_item recycler_item;
    private ArrayList<Recycler_item> recycler_items = new ArrayList<>();
    private XmlPullParserFactory factory;
    private XmlPullParser parser;
    private  ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View view = inflater.inflate(R.layout.fragment_main,container,false);
        requestQueue = Volley.newRequestQueue(getActivity());

        return view;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        layoutManager =  new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        getData();
    }

    public void getData() {
        recycler_items = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        class GetData extends AsyncTask<String, Void, String> {
            ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(getActivity(), "PleaseWait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) { //s는 백그라운드 실행후 리턴값
                super.onPostExecute(s);
                dialog.dismiss();

            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                try {
                    factory = XmlPullParserFactory.newInstance();
                    parser = factory.newPullParser();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                final StringRequest stringRequest = new StringRequest(uri, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parser.setInput(new StringReader(response));
                            int event = parser.getEventType();

                            String tag = "", val = "";

                            while (event != XmlPullParser.END_DOCUMENT) {

                                tag = parser.getName();
                                switch (event) {
                                    case XmlPullParser.START_TAG:
                                        if (tag.equals("timeLine")) {
                                            recycler_item = new Recycler_item();
                                            recycler_items.add(recycler_item);
                                        }
                                        break;

                                    case XmlPullParser.TEXT:
                                        val = parser.getText().trim();
                                        break;

                                    case XmlPullParser.END_TAG:

                                        switch (tag) {
                                            case "timeline_id":
                                                recycler_item.setTimeline_id(val);
                                                break;
                                            case "user_a":
                                                recycler_item.setUser_a(val);
                                                break;
                                            case "temp_a":
                                                recycler_item.setTemp_a(val);
                                                break;
                                            case "user_b":
                                                recycler_item.setUser_b(val);
                                                break;
                                            case "temp_b":
                                                recycler_item.setTemp_b(val);
                                                break;
                                            case "video_date":
                                                recycler_item.setVideo_date(val);
                                                break;
                                            case "image_path":
                                                recycler_item.setImage(val);
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                }
                                event = parser.next();
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        adapter = new ViewAdapter(getActivity(),recycler_items);
                        rv.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });
                requestQueue.add(stringRequest);
                return  null;
            }
        }
        GetData getd = new GetData();
        getd.execute(timelienURL);
    }
    public FirstFragment() {

    }

    public static FirstFragment newInstance() {
        FirstFragment firstFragment = new FirstFragment();
        Bundle args = new Bundle();
        firstFragment.setArguments(args);
        return firstFragment;
    }

}