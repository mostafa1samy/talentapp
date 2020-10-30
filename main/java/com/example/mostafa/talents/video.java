package com.example.mostafa.talents;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class video extends AppCompatActivity {
    VideoView simolevideoview;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        simolevideoview=findViewById(R.id.videoview);
        String c=getIntent().getExtras().getString("mos");
        Toast.makeText(video.this,c,Toast.LENGTH_LONG).show();
        Uri uri=Uri.parse(c);
        mediaController=new MediaController(video.this);

            mediaController.setAnchorView(simolevideoview);

        simolevideoview.setMediaController(mediaController);
        simolevideoview.setVideoURI(uri);
        simolevideoview.start();
        simolevideoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(video.this,"thank you",Toast.LENGTH_LONG).show();
            }
        });
        simolevideoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(video.this,"erro",Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }
}
