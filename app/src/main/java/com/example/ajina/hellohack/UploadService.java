package com.example.ajina.hellohack;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by ajina on 2017/08/11.
 */

public interface UploadService {
    @Multipart

    // PathはベースURLを抜いたものでOK
    @POST("yebisu")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);
}
