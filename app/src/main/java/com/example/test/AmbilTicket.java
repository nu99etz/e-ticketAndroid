package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.network.BaseAPIService;
import com.example.test.network.ConnectorAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmbilTicket extends AppCompatActivity {

    TextView id;
    String no,pesan;
    int param;
    ProgressDialog loading;
    Context mContext;
    BaseAPIService ApiServices;
    CardView Tiket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_ticket);
        mContext = this;
        Tiket = (CardView)findViewById(R.id.njupuk);
    }


    public void Click(View view) {
        ApiServices = ConnectorAPI.getAPIService();
        Bundle tambah = getIntent().getExtras();
        no = tambah.getString("result_id");
        loading = ProgressDialog.show(mContext,null,"Harap Ditunggu",
                true,false);
        requestTiket(no);
    }

    private void requestTiket(String no) {

        ApiServices.tiketRequest(no).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    loading.dismiss();
                    try{
                        JSONObject JSONResult = new JSONObject(response.body().string());
                        if(JSONResult.getString("success").equals("true")){
                            pesan = JSONResult.getString("messages");
                            Toast.makeText(mContext, pesan, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug","onFailure: ERROR > " + t.toString());
                loading.dismiss();
            }
        });
    }

}
