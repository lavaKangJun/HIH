package com.example.youmeelee.handinhand;

/**
 * Created by youmeelee on 16. 5. 28..
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by kangjun-young on 2016. 5. 27..
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {


    private List<Recycler_item> item;
    Context context;
    // SwipeRefreshLayout swipeRefreshLayout;
    private View emptyView;
    int position_cv;
    // FirstFragment firstFragment = new FirstFragment();

    public void clear() {
        item.clear();
    }

    public void addItems(ArrayList<Recycler_item> item) {
        this.item = item;
    }

    public ViewAdapter(Context context, ArrayList<Recycler_item> item) {
        super();
        this.context = context;
        this.item = item;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recycler view에 반복될 아이템 레이아웃 연결

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_fragment, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recycler_item recycler_item = item.get(position);

        holder.timeline_id.setText(recycler_item.getTimelineId());
        holder.video_date.setText(recycler_item.getVideo_date());
        holder.user_a.setText(recycler_item.getUser_a());
        holder.temp_a.setText(recycler_item.getTemp_a());
        holder.user_b.setText(recycler_item.getUser_b());
        holder.temp_b.setText(recycler_item.getTemp_b());
        //Glide.with(context).load(recycler_item.getImage()).into(holder.image_path);
        Picasso.with(context).load(recycler_item.getImage()).into(holder.image_path);
    }
    @Override
    public int getItemCount() {
        return item.size();
    }


    // @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_path;
        public TextView video_date;
        public TextView user_a;
        public TextView user_b;
        public TextView temp_a;
        public TextView temp_b;
        public TextView timeline_id;
        CardView cv;


        public ViewHolder(View itemView){
            super(itemView);
            image_path = (ImageView) itemView.findViewById(R.id.cv_img);
            video_date = (TextView)itemView.findViewById(R.id.date);
            user_a = (TextView) itemView.findViewById(R.id.user_a);
            user_b = (TextView) itemView.findViewById(R.id.user_b);
            temp_a = (TextView) itemView.findViewById(R.id.tempa);
            temp_b = (TextView) itemView.findViewById(R.id.tempb);
            timeline_id = (TextView) itemView.findViewById(R.id.timelineId);
            cv = (CardView) itemView.findViewById(R.id.cv);
            timeline_id.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public  void onClick(View v){
                    // int id = v.getId();
                    position_cv = getPosition();
                    // LinearLayout layout = (LinearLayout)v.findViewById(R.id.linealayout) ;
                    Recycler_item recycler_item3 = item.get(position_cv);
                    String tag= recycler_item3.getTimelineId();

                    Intent it = new Intent(v.getContext(),VideoView.class);
                    String str_id_video = tag;
                    it.putExtra("it_id_video", str_id_video);
                    v.getContext().startActivity(it);
                }
            });

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }




}