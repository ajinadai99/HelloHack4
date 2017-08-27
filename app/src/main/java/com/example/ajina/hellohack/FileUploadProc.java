package com.example.ajina.hellohack;

import android.os.Environment;

import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ajina on 2017/08/11.
 */

public class FileUploadProc {
    public void WavUploadProc(String IdName, String FilePathName) {
        UploadService service = ServiceGenerator.createService(UploadService.class);

        // POSTする画像・音楽・動画等のファイル

        File file = new File(Environment.getExternalStorageDirectory(),FilePathName);
        System.out.println(FilePathName);


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData(IdName, file.getName(), requestFile);

        Call<ResponseBody> call = service.upload(body);
        call.enqueue(new Callback<ResponseBody>() {

            // ステータスコードが４００等エラーコード以外のとき呼ばれる
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("成功" + response.body());
                // 成功時の処理
                // response.body();でHTMLレスポンスのbodyタグ内が取れる
            }

            // ステータスコードが４００等エラーコードのとき呼ばれる
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 失敗時の処理
                System.out.println("失敗" + t.getMessage());
            }
        });
    }
}
