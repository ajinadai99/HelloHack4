package com.example.ajina.hellohack;

/**
 * Created by ajina on 2017/08/06.
 */

import android.media.AudioRecord;
import android.media.AudioFormat;
import android.media.MediaRecorder;

import java.util.Date;
import java.text.SimpleDateFormat;

public class MyRecordProc {

    AudioRecord audioRecord = null; //録音用のオーディオレコードクラス
    private int bufSize;//オーディオレコード用バッファのサイズ
    private short[] shortData; //オーディオレコード用バッファ
    private MyWaveFile wav1 = new MyWaveFile();
    private int silentFlameCount=0;
    private int noisyFlameCount=0;
    private int flameCountMax;
    private int fileFlg=0;
    private String fullFileName="";


    private void initAudioRecord(){
        // AudioRecordオブジェクトを作成
        bufSize = android.media.AudioRecord.getMinBufferSize(SoundDefine.SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SoundDefine.SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufSize);

        shortData = new short[bufSize / 2];
        flameCountMax = (int) ((double)SoundDefine.SAMPLING_RATE * SoundDefine.SILENT_DURATION/((double)bufSize/2));
        System.out.println("flameCountMax=" + flameCountMax + "\n");

        // コールバックを指定
        audioRecord.setRecordPositionUpdateListener(new AudioRecord.OnRecordPositionUpdateListener() {
            // フレームごとの処理
            @Override
            public void onPeriodicNotification(AudioRecord recorder) {
                // TODO Auto-generated method stub
                audioRecord.read(shortData, 0, bufSize / 2); // 読み込む
                double shortDataVol =0;

                for(int index=0; index<(int)(bufSize/2) ;index ++){ shortDataVol = shortDataVol + shortData[index]*shortData[index]; }
                shortDataVol = Math.abs(Math.sqrt(shortDataVol/((int)(bufSize/2)))/SoundDefine.SHORT_MAX);

                System.out.println(shortDataVol);

                if(fileFlg==0) {
                    if(shortDataVol>=SoundDefine.SILENT_LEVEL) {noisyFlameCount ++;}
                    if(noisyFlameCount>=SoundDefine.START_REC_FLAME_COUNT) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        fullFileName = SoundDefine.fileName + sdf.format( new Date())+".wav";
                        wav1.createFile(fullFileName);
                        wav1.addBigEndianData(shortData);
                        fileFlg=1;
                        noisyFlameCount=0;
                        silentFlameCount = 0;
                    }
                }else {
                    if(shortDataVol< SoundDefine.SILENT_LEVEL) {silentFlameCount ++;}
                    if(shortDataVol>= SoundDefine.SILENT_LEVEL) {silentFlameCount =0;}

                    if (silentFlameCount < flameCountMax) {
                        wav1.addBigEndianData(shortData); // ファイルに書き出す
                    } else {
                        wav1.close();
                        fileFlg = 0;
                        silentFlameCount = 0;
                        noisyFlameCount = 0;

                        FileUploadProc myFileUpload = new FileUploadProc();
                        myFileUpload.WavUploadProc("the_file",fullFileName);

                    }
                }
            }

            @Override
            public void onMarkerReached(AudioRecord recorder) {
                // TODO Auto-generated method stub

            }
        });
        // コールバックが呼ばれる間隔を指定
        audioRecord.setPositionNotificationPeriod(bufSize / 2);
    }

    public void startAudioRecord(){
        System.out.println("録音を開始します");
        initAudioRecord();
        audioRecord.startRecording();
        audioRecord.read(shortData, 0, bufSize/2);
    }

    //オーディオレコードを停止する
    public void stopAudioRecord(){
        System.out.println("録音を停止します");
        audioRecord.stop();
    }
}
