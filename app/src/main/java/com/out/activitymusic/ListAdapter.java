package com.out.activitymusic;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.xml.datatype.Duration;

import Service.ServiceMediaPlay;
import es.claucookie.miniequalizerlibrary.EqualizerView;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements Filterable,PopupMenu.OnMenuItemClickListener {
    private ArrayList<Song> mListSong;
    private ArrayList<Song> listSongFull;
    private LayoutInflater mInflater;
    private View playMediaSong;
    RelativeLayout mLinearLayout;
    AllSongsFragment allSongsFragment;
    private Context mContext;
    private ItemClickListener itemClickListener;
    private ListAdapter mListAdapter;
    ArrayList<Song> songs;
    private ImageView image;
    private ImageView mPlayPause;
    DataFragment dataFragment;
    private  Boolean aBoolean;
    private int mPosision;

    public ListAdapter(Context context, ArrayList<Song> ListView,ItemClickListener itemClickListener) {
        mInflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
        this.mListSong = ListView;
        listSongFull=new ArrayList<>(ListView);
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_view, parent, false);
        playMediaSong = mInflater.inflate(R.layout.allsongsfragment, parent, false);
        mLinearLayout = playMediaSong.findViewById(R.id.bottom);
        mPlayPause=playMediaSong.findViewById(R.id.play_pause);
        allSongsFragment = new AllSongsFragment();
        image= (ImageView) mItemView.findViewById(R.id.menu_pop);
        Popmenu();
        Log.d("HoangCV6", "onCreateViewHolder: ");
        return new ViewHolder(mItemView, this);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d("HoangCV6", "onBindViewHolder: "+position);
        Log.d("HoangCV6", "onBindViewHolder: "+mPosision);
        final Song mCurrent = mListSong.get(position);

        holder.mId.setText((position + 1) + "");
        holder.mTitle.setText(mCurrent.getTitle());
        holder.mDuration.setText(getDurationTime(mCurrent.getDuration()));

//        String mCurrent1=mListSTT.get(position);
        if(position==mPosision) {
            holder.mId.setVisibility(View.INVISIBLE);
            holder.mTitle.setTypeface(null, Typeface.BOLD);
            holder.mEqualizer.animateBars();
            holder.mEqualizer.setVisibility(View.VISIBLE);
        }
        else {
            holder.mId.setVisibility(View.VISIBLE);
            holder.mTitle.setTypeface(null, Typeface.NORMAL);
       //     holder.mEqualizer.animateBars();
            holder.mEqualizer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public Filter getFilter() {

        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Song> filterdList = new ArrayList<>();
            if (constraint== null || constraint.length()==0){
                filterdList.addAll(listSongFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Song item: listSongFull){
                    if (item.getTitle().toLowerCase().contains(filterPattern)){
                        filterdList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values= filterdList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mListSong.clear();
            mListSong.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };



    private String getDurationTime(String str) {
        int mili = Integer.parseInt(str) / 1000;
        int phut = mili / 60;
        int giay = mili % 60;
        if (giay<10)
            return String.valueOf(phut) + ":0" + String.valueOf(giay);
        else return String.valueOf(phut) + ":" + String.valueOf(giay);
    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public EqualizerView mEqualizer;
        public TextView mTitle;
        public TextView mDuration;
        final ListAdapter mAdapter;
        public TextView mId;
        private UpdateUI mUpdateUI;


        public ViewHolder(@NonNull View itemView, ListAdapter adapter) {
            super(itemView);
            Log.d("HoangCV6", "ViewHolder: ");
            this.mAdapter = adapter;
            mEqualizer = itemView.findViewById(R.id.equalizer);
            mId=itemView.findViewById(R.id.STT);
            mTitle = itemView.findViewById(R.id.music);
            mDuration = itemView.findViewById(R.id.tvTime);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            Log.d("HoangCV6", "onClick: ");

            mPosision=Integer.parseInt(String.valueOf(mId.getText()))-1;
            itemClickListener.onClick(mListSong.get(mPosision));
            notifyDataSetChanged();
        }
    }

    public void Popmenu(){
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext,view);
                popup.setOnMenuItemClickListener(ListAdapter.this );
                popup.inflate(R.menu.poupup_menu);
                popup.show();
            }
        });
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Toast.makeText(mContext,"Hoang"+menuItem.getTitle(), Toast.LENGTH_SHORT).show();
        switch (menuItem.getItemId()) {
            case R.id.search_item:
                // do your code
                return true;
            case R.id.upload_item:
                // do your code
                return true;
            case R.id.copy_item:
                // do your code
                return true;
            case R.id.print_item:
                // do your code
                return true;
            case R.id.share_item:
                // do your code
                return true;
            case R.id.bookmark_item:
                // do your code
                return true;
            default:
                return false;
        }
    }



}


