package com.example.test.network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

import com.example.test.response.*;

public interface BaseAPIService {


    // Login Request
    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseBody> loginRequest(@Field("user_username") String user_username,
                                    @Field("user_userpassword") String user_userpassword);

    // Request Tiket
    @GET("ticketact/{ticketact}/create")
    Call<ResponseBody> tiketRequest(@Path("ticketact") String ticketact);

    // Request View Realisasi
    @GET("realisasi/{realisasi}")
    Call<ResponseBody> realisasiRequest(@Path("realisasi") String realisasi);

    // Upload Realisasi
    @Multipart
    @PUT("realisasi/update/{realisasi}")
    Call<ResponseBody> realisasiUpload(@Path("realisasi") String realisasi,
                                       @Field("realisasi_nama") String realisasi_nama
                                       //@Part("realisasi_foto_1") MultipartBody.Part realisasi_foto_1,
                                       //@Part("realisasi_foto_2") MultipartBody.Part realisasi_foto_2,
                                       //@Part("realisasi_foto_3") MultipartBody.Part realisasi_foto_3,
                                       //@Part("realisasi_foto_4") MultipartBody.Part realisasi_foto_4
    );


}
