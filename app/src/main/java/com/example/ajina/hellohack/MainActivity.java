package com.example.ajina.hellohack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btStartClick = (Button) findViewById(R.id.btStartRec);
        RecWaveListener listener = new RecWaveListener();
        btStartClick.setOnClickListener(listener);

        Button btStopClick = (Button) findViewById(R.id.btStopRec);
        btStopClick.setOnClickListener(listener);

    }

    private class RecWaveListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            MyRecordProc MRP = new MyRecordProc();
            int id = view.getId();
            switch(id) {
                case R.id.btStartRec:
                    MRP.startAudioRecord();
                    break;
                case R.id.btStopRec:
                    MRP.stopAudioRecord();
                    break;
            }
        }
    }
}
