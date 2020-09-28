package com.out.activitymusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import android.annotation.SuppressLint;
import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.util.Log;

import android.widget.ImageView;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.datatype.Duration;

import Service.ServiceMediaPlay;

public class MainActivity extends AppCompatActivity implements DisplayMediaFragment,DataFragment{
    public static IntentFilter Broadcast_PLAY_NEW_AUDIO;
    String PRIVATE_MODE ="color" ;
    AllSongsFragment allSongsFragment;
    MediaPlaybackFragment mediaPlaybackFragment;
    DataFragment dataFragment;
    DataList dataList;

    public ServiceMediaPlay getPlayer() {
        return player;
    }

    public ServiceMediaPlay player;
    MediaPlayer mediaPlayer;
    boolean serviceBound = false;
    private Song song;
    private TextView mTitle, mTime2;
    private ImageView mPictureSmall;
    private ListAdapter adapter;
    private ImageView image;
    private ImageView mPlayPauseMedia;
    private RelativeLayout mLinearLayout;
    private DisplayMediaFragment displayMediaFragment;
    private ArrayList<Song> mListSong;
    SharedPreferences sharedPreferences;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServiceMediaPlay.LocalBinder binder = (ServiceMediaPlay.LocalBinder) service;
            player = binder.getService();
//            mListSong= new ArrayList<>();
            player.setListSong(mListSong);
            Log.d("HoangCV7", "onSaveInstanceState: "+player);

            Log.d("nhungltk", "onServiceConnected: "+player);
            //Bkav Nhungltk: tai sao lai thuc hien connect o day
            iConnectActivityAndBaseSong.connectActivityAndBaseSong();
            serviceBound = true;

                allSongsFragment.setService(player);



            Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
    private int possision;


 /*   private void playAudio(String media) {
        //Check is service is active
        if (!serviceBound) {
            Intent playerIntent = new Intent(this, ServiceMediaPlay.class);
            playerIntent.putExtra("media", media);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }*/

    // Gọi playAudio()hàm từ phương thức Activitys onCreate()và tham chiếu tệp âm thanh.
    //   playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
    private void setSaveInStanceState(){
        Image img1,img2;
        TextView tv;
        int Time;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        UpdateUI updateUI=new UpdateUI(getApplicationContext());
       // updateUI.UpdateSeekbar();
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        possision = savedInstanceState.getInt("possision");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            //player.stopSelf();
        }
    }
    public void setSong(Song songs) {
        this.song = songs;
    }

    public void FileSong(Song song) {
        song.getFile();
    }


private Boolean IsBoolean=false;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = findViewById(R.id.song1);
        mTime2 = findViewById(R.id.Time2);
        mPictureSmall = findViewById(R.id.picture_small);
        mLinearLayout = findViewById(R.id.bottom);
        mediaPlaybackFragment = new MediaPlaybackFragment();


        //final ListView list = findViewById(R.id.list_view);
        int orientation = this.getResources().getConfiguration().orientation;
         allSongsFragment = new AllSongsFragment(this, this.mediaPlaybackFragment,this);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            FragmentManager manager = this.getSupportFragmentManager();
            allSongsFragment.setBoolean(true);
            manager.beginTransaction()
                    .replace(R.id.fragmentSongOne, allSongsFragment)
                    .commit();
        } else {
            allSongsFragment.setBoolean(false);
            FragmentManager manager = this.getSupportFragmentManager();

            manager.beginTransaction()
                    .replace(R.id.fragmentSongOne, allSongsFragment)
                    .commit();

            FragmentManager manager1 = this.getSupportFragmentManager();

            manager1.beginTransaction()
                    .replace(R.id.fragmentMediaTwo, mediaPlaybackFragment)

                    .commit();
        }
        /* Log.d("HoangCV", "onCreateView: "+song.getTitle());*/

        Intent intent = new Intent(this, ServiceMediaPlay.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        mediaPlaybackFragment.setService(player);
//        if(savedInstanceState!=null) {
//            savedInstanceState.getInt("possision");
//            Log.d("HoangCV9", "onCreate: " + savedInstanceState.getInt("possision"));
//            Log.d("HoangCV9", "onCreate: " + mListSong.get(4));
//            possision = savedInstanceState.getInt("possision");
//        }
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
    }


    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }
*/




    @Override
    public void onclick(Song song) {
        Log.d("HoangCV7", "onSaveInstanceState: "+player);
         mediaPlaybackFragment = new MediaPlaybackFragment().newInstance(song);
        FragmentManager manager1 = this.getSupportFragmentManager();
        manager1.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentSongOne, mediaPlaybackFragment)
                .commit();
        mediaPlaybackFragment.setService(player);
        mediaPlaybackFragment.setListSong(mListSong);


    }

    @Override
    public void onclickData(ArrayList ListSong) {
        this.mListSong=ListSong;

    }
    public void setService(ServiceMediaPlay service){
        this.player=service;
    }

    //Bkav Nhungltk
    interface IConnectActivityAndBaseSong {
        void connectActivityAndBaseSong();
    }
    private IConnectActivityAndBaseSong iConnectActivityAndBaseSong;
    public void setiConnectActivityAndBaseSong(IConnectActivityAndBaseSong iConnectActivityAndBaseSong) {
        this.iConnectActivityAndBaseSong = iConnectActivityAndBaseSong;
    }

    @Override
    protected void onResume() {
        Log.d("HoangCV7", "onResume: "+getPlayer());
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("HoangCV7", "onPause: "+player);
        setService(player);
    }
}