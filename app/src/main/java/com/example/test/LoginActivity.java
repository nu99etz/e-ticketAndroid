package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    EditText user_username;
    EditText user_userpassword;
    Button login;
    ProgressDialog loading;
    Context mContext ;
    BaseAPIService ApiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        mContext = this;
        ApiServices = ConnectorAPI.getAPIService();
        initComponents();
    }

    public void initComponents(){
        user_username = (EditText)findViewById(R.id.petugas_username);
        user_userpassword = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext,null,"Tolong Tunggu Sebentar"
                        ,true,false);
                requestLogin();
            }
        });
    }

    private void requestLogin() {
        ApiServices.loginRequest(user_username.getText().toString(),
                user_userpassword.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    loading.dismiss();
                    try{
                        JSONObject JSONresult = new JSONObject(response.body().string());
                        if(JSONresult.getString("success").equals("true")){
                            String pesan_sukses = JSONresult.getString("messages").toString();
                            Toast.makeText(mContext ,pesan_sukses, Toast.LENGTH_SHORT).show();
                            String foto = JSONresult.getString("foto").toString();
                            String nama_petugas = JSONresult.getString("nama").toString();
                            String id = JSONresult.getString("id").toString();
                            String petugas_nip = JSONresult.getString("nip").toString();
                            Intent intent = new Intent(mContext,DashboardActivity.class);
                            intent.putExtra("result_id",id);
                            intent.putExtra("result_foto",foto);
                            intent.putExtra("result_nama",nama_petugas);
                            intent.putExtra("result_nip",petugas_nip);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loading.dismiss();
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
