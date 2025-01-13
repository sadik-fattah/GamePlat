package com.guercifzone.gameplate.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guercifzone.gameplate.GameDetails_Activity;
import com.guercifzone.gameplate.Models.Feed;
import com.guercifzone.gameplate.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mcontext;
    private List<Feed> feedList;
    RequestOptions options;
    @SuppressWarnings("unchecked")
    public RecyclerViewAdapter(Context mcontext, List<Feed> feedList) {
        this.mcontext = mcontext;
        this.feedList = feedList;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.ca22lp01);;
    }
    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        view = inflater.inflate(R.layout.recyclerview_resource_list,viewGroup,false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, GameDetails_Activity.class);
                intent.putExtra("Game_title",feedList.get(viewHolder.getAdapterPosition()).getGamename());
                intent.putExtra("Game_loc",feedList.get(viewHolder.getAdapterPosition()).getGameLoc());
               intent.putExtra("Game_date",feedList.get(viewHolder.getAdapterPosition()).getGameType());
               intent.putExtra("Game_image",feedList.get(viewHolder.getAdapterPosition()).getGameImage());
                mcontext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder myViewHolder, int i) {
        myViewHolder.game_name.setText(feedList.get(i).getGamename());
        myViewHolder.game_loc.setText(feedList.get(i).getGameLoc());
        myViewHolder.game_type.setText(feedList.get(i).getGameType());
        Glide.with(mcontext).load(feedList.get(i).getGameImage()).apply(options).into(myViewHolder.img_thumbnail);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void setFilteredList(List<Feed> filteredList){
        this.feedList = filteredList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView game_name;
        TextView game_loc;
        TextView game_type;
        ImageView img_thumbnail;
        LinearLayout view_content;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            view_content = itemView.findViewById(R.id.contailner);
            game_name = itemView.findViewById(R.id.gametv_name);
            game_loc = itemView.findViewById(R.id.gametv_loc);
            game_type = itemView.findViewById(R.id.gametv_type);
            img_thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }
}
