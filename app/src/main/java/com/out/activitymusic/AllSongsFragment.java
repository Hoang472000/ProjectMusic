package com.out.activitymusic;

import android.content.ContentUris;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Service.ServiceMediaPlay;

import static android.content.Context.MODE_PRIVATE;


public class AllSongsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private static final String SHARED_PREFERENCES_NAME = "1";
    private ListAdapter mListAdapter;

    private RecyclerView mRecyclerView;
    private Song song;

    private RelativeLayout mLinearLayout, mBottom;
    TextView title, mTitle, mTime;
    TextView artist;
    ImageView img, mImageSmall;
    private SharedPreferences mSharePreferences;
    ArrayList<Song> songs;
    ServiceMediaPlay serviceMediaPlay;
    MediaPlaybackFragment mediaPlaybackFragment;
    DataFragment dataFragment;


    private DisplayMediaFragment displayMediaFragment;
    private ImageView mPlayPause;
    private Boolean IsBoolean = false;
    private ImageView mMusicPop;
    MediaPlayer mediaPlayer;


    public void setBoolean(Boolean aBoolean) {
        IsBoolean = aBoolean;
    }

    public Boolean getIsBoolean() {
        return IsBoolean;
    }

    public void setService(ServiceMediaPlay service) {
        this.serviceMediaPlay = service;
    }


    public AllSongsFragment(DisplayMediaFragment displayMediaFragment, MediaPlaybackFragment mediaPlaybackFragment, DataFragment dataFragment) {
        this.displayMediaFragment = displayMediaFragment;
        this.mediaPlaybackFragment = mediaPlaybackFragment;
        this.dataFragment = dataFragment;

    }

    public AllSongsFragment() {

    }

    UpdateUI UpdateUI;
    int index;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("nhungltk", "onCreateView: ");
        View mInflater = inflater.inflate(R.layout.allsongsfragment, container, false);
        mRecyclerView = mInflater.findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLinearLayout = mInflater.findViewById(R.id.bottom);
        mPlayPause = mInflater.findViewById(R.id.play_pause);
        Log.d("HoangCV1", "onCreateView: " + mPlayPause);
        UpdateUI = new UpdateUI(getContext());
        Log.i("HoangCV11", "onCreateView:1 " + UpdateUI.getTitle());
        LoaderManager.getInstance(this).initLoader(1, null, this);
        title = mInflater.findViewById(R.id.title);
        artist = mInflater.findViewById(R.id.artist);
        img = mInflater.findViewById(R.id.picture);
        mTitle = mInflater.findViewById(R.id.song1);
        mTime = mInflater.findViewById(R.id.Time2);
        mImageSmall = mInflater.findViewById(R.id.picture_small);
        mMusicPop = mInflater.findViewById(R.id.music_pop);
        //Bkav Nhungltk: doan nay nghia la sao
        ((MainActivity) getActivity()).setiConnectActivityAndBaseSong(new MainActivity.IConnectActivityAndBaseSong() {
            @Override
            public void connectActivityAndBaseSong() {
                if (((MainActivity) getActivity()).player != null) {
                    Log.d("nhungltk", "onCreateView: " + "not null");
                    setService((((MainActivity) getActivity()).player));
                }
            }
        });
        title.setText(UpdateUI.getTitle());
        artist.setText(UpdateUI.getArtist());
        img.setImageURI(Uri.parse(UpdateUI.getAlbum()));
        final Song updateSong= new Song(UpdateUI.getIndex(),UpdateUI.getTitle(),UpdateUI.getFile(),UpdateUI.getAlbum(),UpdateUI.getArtist(),String.valueOf(UpdateUI.getDuration()));
        if(serviceMediaPlay!=null)
            song=updateSong;
        Log.d("HoangCV444", "onCreateView: "+updateSong);




        index=UpdateUI.getIndex();
        Log.d("HoangCV33", "onCreateView: "+UpdateUI.getAlbum());

        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMediaFragment.onclick(updateSong);

            }
        });

        onClickPause();

        return mInflater;
    }
    private  void clickLinearLayout(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("nhungltk", "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("nhungltk", "onCreateLoader: ");
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        CursorLoader cursorLoader = new CursorLoader(getContext(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        return cursorLoader;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("nhungltk", "onLoadFinished: ");
        mSharePreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        songs = new ArrayList<>();
        boolean isCreate = mSharePreferences.getBoolean("create_db", false);
        int id = 0;
        String title = "";
        String file = "";
        String album = "";
        String artist = "";
        String duration = "";
        Song song = new Song(id, title, file, album, artist, duration);
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                id++;
                song.setID(id);
                song.setTitle(data.getString(data.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.setFile(data.getString(data.getColumnIndex(MediaStore.Audio.Media.DATA)));
                song.setAlbum(data.getString(data.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                song.setArtist(data.getString(data.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                song.setDuration(data.getString(data.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                title = song.getTitle();
                file = song.getFile();
                album = song.getAlbum();
                artist = song.getArtist();
                duration = song.getDuration();
                songs.add(new Song(id, title, file, album, artist, duration));
                Log.d("nhungltk1", "onLoadFinished: " + title);


               /* if (isCreate == false) {
                    ContentValues values = new ContentValues();
                    values.put(FavoriteSongsProvider.ID_PROVIDER, id);
                    values.put(FavoriteSongsProvider.FAVORITE, 0);
                    values.put(FavoriteSongsProvider.COUNT, 0);
                    Uri uri = getActivity().getContentResolver().insert(Uri.parse(FavoriteSongsProvider.CONTENT_URI), values);
                    mSharePreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharePreferences.edit();
                    editor.putBoolean("create_db", true);
                    editor.commit();
                }*/
            } while (data.moveToNext());
        }
        mListAdapter = new ListAdapter(getContext(), songs, this);
        mRecyclerView.setAdapter(mListAdapter);
        dataFragment.onclickData(songs);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            if (mLinearLayout.getVisibility() == View.VISIBLE)
                mLinearLayout.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(Song song) {
        this.song = song;
        Log.d("HoangCV", "onClick: 123");
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mediaPlaybackFragment.setListSong(songs);
            mediaPlaybackFragment.getText(song);
        }
        if (serviceMediaPlay != null) {
            Log.d("nhungltk", "onClick: " + "playMusic");
            try {
                serviceMediaPlay.playMedia(song);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        title.setText(song.getTitle());
        artist.setText(song.getArtist());
        img.setImageURI(queryAlbumUri(song.getAlbum()));

        Ischeck = true;
    }

    private boolean Ischeck;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Ischeck) {
            outState.putString("Title", song.getTitle());
        }
        Ischeck = false;
    }


    public Uri queryAlbumUri(String imgUri) {

        final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artworkUri, Long.parseLong(imgUri));//noi them mSrcImageSong vao artworkUri
    }


    public void onClickPause() {

        mPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceMediaPlay.isPlaying()) {
                    serviceMediaPlay.pauseMedia();
                    mPlayPause.setImageResource(R.drawable.ic_media_play_light);
                    Log.d("HoangCV2", "onClick: " + serviceMediaPlay.isPlaying());
                } else {
                    try {
                        serviceMediaPlay.playMedia(song);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPlayPause.setImageResource(R.drawable.ic_pause_black_large);
                    Log.d("HoangCV2", "onClick: " + serviceMediaPlay.isPlaying());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HoangCV10", "onResume: " + serviceMediaPlay);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.menuSearch);
        Log.d("HoangCV3", "onCreateOptionsMenu: " + searchItem);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        Log.d("HoangCV3", "onCreateOptionsMenu: " + searchView);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        mListAdapter.getFilter().filter(s);
        return false;
    }


}









