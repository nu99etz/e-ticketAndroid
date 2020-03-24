package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.test.network.BaseAPIService;
import com.example.test.network.ConnectorAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    String foto,nama,nip,id,pesan,hitung;
    CardView Tiket;
    ImageView img;
    TextView nama_petugas, nip_petugas;
    ProgressDialog loading;
    Context context;
    BaseAPIService ApiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = this;
        initComponents();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            final String URL;
            foto = extras.getString("result_foto");
            URL = "http://192.168.1.87/"+foto;
            //Picasso.with(context).load(URL).into(img);
            Glide.with(this).load(URL).apply(RequestOptions.circleCropTransform()).into(img);
            nama = extras.getString("result_nama");
            nip = extras.getString("result_nip");
            nama_petugas.setText(nama);
            nip_petugas.setText(nip);
        }

        Tiket = (CardView)findViewById(R.id.ambilID);
    }

    public void initComponents() {
        img = (ImageView)findViewById(R.id.foto);
        nama_petugas = (TextView)findViewById(R.id.nama_petugas);
        nip_petugas = (TextView)findViewById(R.id.nip_petugas);
    }

    public void Click(View view) {
        Bundle tambah = getIntent().getExtras();
        id = tambah.getString("result_id");
        Intent intent = new Intent(context,AmbilTicket.class);
        intent.putExtra("result_id",id);
        startActivity(intent);
    }

    public void Realisasi(View view) {
        ApiServices = ConnectorAPI.getAPIService();
        Bundle tambah = getIntent().getExtras();
        id = tambah.getString("result_id");
        loading = ProgressDialog.show(context,null,"Harap Ditunggu",
                true,false);
        lihatrealiasasi(id);
    }

    private void lihatrealiasasi(String id) {
        ApiServices.realisasiRequest(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    loading.dismiss();
                    try{
                        JSONObject JSONResult = new JSONObject(response.body().string());
                        if(JSONResult.getString("success").equals("true")){
                            hitung = JSONResult.getString("hitung").toString();
                            if(hitung.equals("1")) {
                                pesan = JSONResult.getString("messages").toString();
                                Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
                                String keluhan = JSONResult.getString("keluhan");
                                //String realisasi = JSONResult.getString("")
                                String id = JSONResult.getString("id").toString();
                                Intent intent = new Intent(context,LihatRealisasiActivity.class);
                                intent.putExtra("result_keluhan",keluhan);
                                intent.putExtra("result_id",id);
                                startActivity(intent);
                            } else if(hitung.equals("0")) {
                                pesan = JSONResult.getString("messages").toString();
                                Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loading.dismiss();
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
