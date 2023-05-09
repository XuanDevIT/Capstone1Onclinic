package com.example.onclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;


public class VideoCall extends AppCompatActivity{
    Button btnJoin;
    EditText edtSecrectCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        addControls();
        URL server;
        try{
            server =new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(server)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        addEvents();
    }

    private void addEvents() {
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(edtSecrectCode.getText().toString())
                        .setWelcomePageEnabled(false).build();
                JitsiMeetActivity.launch(VideoCall.this,options);
            }
        });
    }

    private void addControls() {
        btnJoin = findViewById(R.id.btnJoin);
        edtSecrectCode = findViewById(R.id.edtSecrectCode);
    }
}