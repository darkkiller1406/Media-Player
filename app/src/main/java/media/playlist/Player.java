package media.playlist;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {

    ArrayList<File> mySongs;
    int position;

    private MediaPlayer mediaPlayer;
    public TextView songName, duration,songStar;
    private double TGBatdau = 0, TGDung = 0;
    private Handler songStarHandler = new Handler();
    private SeekBar seekbar;
    ImageButton imgplay,imgnext,imgprevious,imgshuffle, imgrepeat, imgplaylist;
    private boolean isShuffle = false;
    private boolean isRepeat = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // import
        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);
        Uri u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);










        // set up

        songName = (TextView) findViewById(R.id.songName);
        imgnext = (ImageButton) findViewById(R.id.media_next);
        imgprevious = (ImageButton) findViewById(R.id.media_pre);
        TGDung = mediaPlayer.getDuration();
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar = (SeekBar) findViewById(R.id.seekBar);

        songStar = (TextView) findViewById(R.id.songStar);
        seekbar.setMax((int) TGDung);
        seekbar.setClickable(false);
        imgplay = (ImageButton) findViewById(R.id.media_play);
        imgshuffle = (ImageButton) findViewById(R.id.media_Shuffle);
        imgrepeat = (ImageButton) findViewById(R.id.media_Repeat);
        imgplaylist = (ImageButton) findViewById(R.id.media_playlist);


        String songTitle = mySongs.get(position).getName().replace(".mp3","");
        songName.setText(songTitle);

        // trở lại playlist
        imgplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Playlist.class);
                startActivityForResult(i, 100);
                mediaPlayer.reset();

            }
        });




        // set seekbar



        TGBatdau = mediaPlayer.getCurrentPosition();
        TGDung = mediaPlayer.getDuration();
        seekbar.setProgress((int) TGBatdau);
        seekbar.setMax((int) TGDung);
        songStarHandler.postDelayed(updateSeekBarTime, 100);



        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (isShuffle) {
                            // shuffle is on - play a random song
                            mediaPlayer.reset();
                            Random random = new Random();
                            position = random.nextInt((mySongs.size() - 1 - 0 + 1) + 0);
                            Uri u = Uri.parse(mySongs.get(position).toString());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                            String songTitle = mySongs.get(position).getName().replace(".mp3", "");
                            songName.setText(songTitle);

                            //seekbar
                            TGBatdau = mediaPlayer.getCurrentPosition();
                            TGDung = mediaPlayer.getDuration();
                            seekbar.setProgress((int) TGBatdau);
                            seekbar.setMax((int) TGDung);
                            songStarHandler.postDelayed(updateSeekBarTime, 100);

                            mediaPlayer.start();
                        } else {
                            position = (position + 1) % mySongs.size();
                            Uri u = Uri.parse(mySongs.get(position).toString());
                            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                            String songTitle = mySongs.get(position).getName().replace(".mp3", "");
                            songName.setText(songTitle);

                            //seekbar
                            TGBatdau = mediaPlayer.getCurrentPosition();
                            TGDung = mediaPlayer.getDuration();
                            seekbar.setProgress((int) TGBatdau);
                            seekbar.setMax((int) TGDung);
                            songStarHandler.postDelayed(updateSeekBarTime, 100);


                            mediaPlayer.start();
                        }
                    }
                });


            }


        });


        mediaPlayer.start();















    }




        // next

    public void next (View view) {
        imgplay.setImageResource(R.drawable.pausee);
        mediaPlayer.reset();
        position = (position + 1) % mySongs.size();
        Uri u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        String songTitle = mySongs.get(position).getName().replace(".mp3", "");
        songName.setText(songTitle);
            TGBatdau = mediaPlayer.getCurrentPosition();
            TGDung = mediaPlayer.getDuration();
            seekbar.setProgress((int) TGBatdau);
            seekbar.setMax((int) TGDung);

        mediaPlayer.start();
    }


        //previous
        public void previous (View view){

        imgplay.setImageResource(R.drawable.pausee);
        mediaPlayer.reset();
        mediaPlayer.release();
        position = (position-1<0)? mySongs.size()-1: position-1;
        Uri u = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        String songTitle = mySongs.get(position).getName().replace(".mp3", "");
        songName.setText(songTitle);
            TGBatdau = mediaPlayer.getCurrentPosition();
            TGDung = mediaPlayer.getDuration();
            seekbar.setProgress((int) TGBatdau);
            seekbar.setMax((int) TGDung);

        mediaPlayer.start();

    }

    // phát nhạc ngẫu nhiên
    public void shuffle (View view) {
        if (isShuffle) {
            isShuffle = false;
            imgshuffle.setImageResource(R.drawable.shuffle_a);
            Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();

        } else
        // make repeat to true
            isShuffle = true;
            imgshuffle.setImageResource(R.drawable.shuffle_b);
            Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
            isRepeat = false;
            mediaPlayer.setLooping(false);
            imgrepeat.setImageResource(R.drawable.repeat_a);
        }





    // lặp nhạc
    public  void repeat (View view){
        if(isRepeat){
            isRepeat = false;
            mediaPlayer.setLooping(false);
            imgrepeat.setImageResource(R.drawable.repeat_a);
            Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
        }else{
            // make repeat to true
            isRepeat = true;
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            imgplay.setImageResource(R.drawable.pausee);
            imgrepeat.setImageResource(R.drawable.repeat_b);
            Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();


            // make shuffle to false
            isShuffle = false;
            imgshuffle.setImageResource(R.drawable.shuffle_a);
        }
    }








    // chạy nhạc
    public void play(View view) {

        if (mediaPlayer.isPlaying()) {

                mediaPlayer.pause();
                // Changing button image to play button
                imgplay.setImageResource(R.drawable.btnext);

                Toast.makeText(getApplicationContext(), "Đã dừng nhạc", Toast.LENGTH_SHORT).show();

            }
         else {
            // Resume song

                mediaPlayer.start();
                // Changing button image to pause button
                imgplay.setImageResource(R.drawable.pausee);

                Toast.makeText(getApplicationContext(), "Bắt đầu chạy nhạc", Toast.LENGTH_SHORT).show();

        }



    }




    // update thanh seekbar
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get thoi diem dau va cuoi
            TGBatdau = mediaPlayer.getCurrentPosition();
            TGDung = mediaPlayer.getDuration();
            //chuyen thoi gian cua seekbar
            seekbar.setProgress((int) TGBatdau);



            songStar.setText(String.format("%d ' %d ", TimeUnit.MILLISECONDS.toMinutes((long) TGBatdau),
                    TimeUnit.MILLISECONDS.toSeconds((long) TGBatdau)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) TGBatdau))));



            duration.setText(String.format("%d ' %d ", TimeUnit.MILLISECONDS.toMinutes((long) TGDung),
                    TimeUnit.MILLISECONDS.toSeconds((long) TGDung)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) TGDung))));


            //Cập nhật
            songStarHandler.postDelayed(this, 100);


        }
    };

    // back
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.reset();

    }






}
